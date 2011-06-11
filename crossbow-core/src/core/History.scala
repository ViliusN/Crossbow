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

package lt.norma.crossbow.core

/** Holds history of indicator's values */
final class IndicatorHistory[Value](optionalValue: () => Option[Value],
    valueToString: Option[Value] => String) {
  private var reversedValues = List[Option[Value]]()

  def update() { reversedValues = optionalValue() :: reversedValues }
  def values = reversedValues.reverse
  def size = reversedValues.size
  def isEmpty = reversedValues.isEmpty
  def last = if(reversedValues.isEmpty) None else reversedValues.head
  def lastSet = reversedValues.find(_.isDefined).map(_.get)
  def take(n: Int) = reversedValues take(n) reverse
  def valuesToStrings: List[String] = values map { valueToString }
}

/** Extend this trait to create indicators capable of collecting history of their values. To
  * actually collect history, marker trait `History` must be extended as well. */
trait HistoryHolder[Value] {
  def name: String
  def optionalValue: Option[Value]
  def valueToString(valueToConvert: Option[Value]): String

  def hasHistory = this.isInstanceOf[History]
  lazy val history = if(hasHistory) new IndicatorHistory(optionalValue _, valueToString _)
    else throw new Exception("Indicator "+name+" does not support history")
}

/** Mixin this trait to enable particular instance of indicator to collect history.
  *
  * Examples: {{{
  * // This indicator does not collect historical values
  * val i1 = new MyIndicator
  *
  * // This indicator collects historical values
  * val i2 = new MyIndicator with History
  *
  * // If the indicator uses historical values internally, `History` can be included in extends
  * // clause of the indicator's class:
  * class MyIndicator extends Indicator[Double] with History { ... }
  * }}} */
trait History
