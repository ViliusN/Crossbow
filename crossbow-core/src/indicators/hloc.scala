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

package lt.norma.crossbow.indicators

import lt.norma.crossbow.core._

/** Highest value of the indicator since the opening of the current bar. */
class HighSoFar(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "HighSoFar("+indicator.name+")"
  def dependencies = Set(indicator)
  def calculate = {
    case BarOpen(_) => None
    case _ if(!isSet || indicator.isSet && indicator.value > value) => indicator()
  }
}

/** Highest value of the indicator during the current bar. Indicator's value is set only at the
  * moment of bar closing. */
class High(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "High("+indicator.name+")"
  private val currentHigh = new HighSoFar(indicator)
  def dependencies = Set(currentHigh)
  def calculate = {
    case BarOpen(_) => None
    case BarClose(_) => currentHigh()
  }
}

/** Lowest value of the indicator since the opening of the current bar. */
class LowSoFar(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "LowSoFar("+indicator.name+")"
  def dependencies = Set(indicator)
  def calculate = {
    case BarOpen(_) => None
    case _ if(!isSet || indicator.isSet && indicator.value < value) => indicator()
  }
}

/** Lowest value of the indicator during the current bar. Indicator's value is set only at the
  * moment of bar closing. */
class Low(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "Low("+indicator.name+")"
  private val currentLow = new LowSoFar(indicator)
  def dependencies = Set(currentLow)
  def calculate = {
    case BarOpen(_) => None
    case BarClose(_) => currentLow()
  }
}

/** First value of the indicator after opening of the current bar. */
class Open(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "Open("+indicator.name+")"
  def dependencies = Set(indicator)
  def calculate = {
    case BarOpen(_) => None
    case _ if(!isSet)=> indicator()
  }
}

/** Last value of the indicator before the end of the current bar. Indicator's value is set only at
  * the moment of bar closing. */
class Close(indicator: Indicator[Double]) extends Indicator[Double] {
  def name = "Close("+indicator.name+")"
  def dependencies = Set(indicator)
  def calculate = {
    case BarOpen(_) => None
    case BarClose(_) => indicator()
  }
}
