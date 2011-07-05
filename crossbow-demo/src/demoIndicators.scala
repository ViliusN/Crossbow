/*
 * Copyright 2010-2011 Vilius Normantas <code@norma.lt>
 *
 * This file is part of Crossbow library.
 *
 * Crossbow is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Crossbow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Crossbow.  If not,
 * see <http://www.gnu.org/licenses/>.
 */

package lt.norma.crossbow.demo

import lt.norma.crossbow.core._
import lt.norma.crossbow.indicators._

/** Empty indicator. */
class Indicator1 extends Indicator[Double] {
  def name = "My first indicator"
  def dependencies = Empty
  def calculate = Empty
}

/** Calculates square of last price. */
class Indicator2 extends Indicator[Double] {
  def name = "Last price squared"
  def dependencies = Empty
  def calculate = {
    case Trade(_, price, _, _) => math.pow(price.doubleValue, 2)
    case SessionClose(_) => None
  }
}

/** Calculates sum of two indicators. */
class Indicator3(target1: Indicator[Double], target2: Indicator[Double]) extends Indicator[Double] {
  def name = target1.name+" + "+target2.name
  def dependencies = Set(target1, target2)
  def calculate = {
    case _ if(target1.isSet && target2.isSet) => target1.value + target2.value
    case _ => None
  }
}

/** Calculates number of ticks per bar. */
class Indicator4(instrument: Instrument) extends Indicator[Int] {
  def name = "Number of ticks for "+instrument
  def dependencies = Empty

  override def default = 0

  def calculate = {
    case Trade(`instrument`, _, _, _) => value + 1
    case BarOpen(_) => None
  }
}

/** Calculates percent of up-ticks during the recent bar. */
class Indicator5(instrument: Instrument) extends Indicator[Double] {
  def name = "Up-tick percent ("+instrument+")"
  val upCounter = new TickCounter(true)
  val downCounter = new TickCounter(false)
  def dependencies = Set(upCounter, downCounter)
  def calculate = {
    case _ =>
      val total = upCounter.value + downCounter.value
      if(total > 0) {
        upCounter.value.toDouble / total * 100
      } else {
        None
      }
  }
  class TickCounter(up: Boolean) extends Indicator[Int] {
    def name = "Number of "+(if(up) "up" else "down")+"-ticks for "+instrument
    val previousPrice = new Variable[Double]
    def dependencies = Set(previousPrice)
    override def default = 0
    def calculate = {
      case Trade(`instrument`, price, _, _) if(previousPrice.isSet) =>
        val p = price.doubleValue
        if(up && p > previousPrice.value || !up && p < previousPrice.value) {
          previousPrice.set(p)
          value + 1
        } else {
          previousPrice.set(p)
          value
        }
      case Trade(`instrument`, price, _, _) =>
        previousPrice.set(price.doubleValue)
        None
      case BarOpen(_) =>
        previousPrice.unset()
        None
    }
  }
}
