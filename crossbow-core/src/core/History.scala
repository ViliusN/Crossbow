/*
 * Copyright 2011 Vilius Normantas <code@norma.lt>
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

/** Mixin this trait to create indicators capable of collecting history of their values. History is
  * updated by calling `update` method. To actually collect history, marker trait `History` must be
  * extended as well. */
trait HistoryHolder[Value] {
  // TODO convert to class HistoryContainer, take optionalValue: => Value as parameter
  private var reversedHistory = List[Option[Value]]()

  def optionalValue: Option[Value]

  def update() { reversedHistory = optionalValue :: reversedHistory }
  def history = reversedHistory.reverse
  def last = if(reversedHistory.isEmpty) None else reversedHistory.head
  def lastSet = reversedHistory.find(_.isDefined).map(_.get)
  def take(n: Int) = reversedHistory take(n) reverse
  def valueToString(v: Option[Value]): String
  def historyStrings: List[String] = history map { valueToString }
}

/** Mixin this trait to mart particular instance of indicator to collect history. To be able to
  * collect historical values, the indicator must also mixin `HistoryHolder` trait.
  *
  * Examples: {{{
  * // This indicator does not collect historical values
  * val i1 = new MyIndicator
  *
  * // This indicator collects historical values
  * val i2 = new MyIndicator with History
  *
  * // If the indicator uses historical values internally, History can included in extends clause at
  * // the declaration of indicator's class:
  * class MyIndicator extends Indicator[Double] with History { ... }
  * }}} */
trait History {
  def update()
}
