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

/** Base trait for any indicator. Most custom indicators extend `MutableIndicator`,
  * `FunctionalIndicator`, or `ListenerIndicator`, instead of directly extending `Indicator`. */
trait Indicator[Value] extends Dependant[Indicator[_]] {
  /** Implicitly convert specified value to `Some(value)` */
  implicit def valueToOption(v: Value): Option[Value] = Some(v)

  /** Name of the indicator. */
  def name: String;

  /** Optional value of the indicator. */
  def optionalValue: Option[Value]

  /** Value of the indicator. If the `optionalValue` is `None`, this method returns result of
    * `default` value method (which throws `ValueNotSet` exception, unless overridden). */
  final def value: Value = optionalValue.getOrElse(default)

  /** Default value of the indicator. This method throws `ValueNotSet` exception, unless
    * overridden. */
  def default: Value = { throw Indicator.ValueNotSet() }

  /** Shorter way to access indicator's `optionalValue`. */
  final def apply() = optionalValue

  /** Checks whether indicator's `optionalValue` is not `None` */
  final def isSet = optionalValue.isDefined

  /** Converts specified value to string. Override this method (together with `valueNotSetString`)
    * to use custom formatting of indicator's values. */
  def valueToString(valueToConvert: Value): String = valueToConvert.toString

  /** Converts specified optional value to string. */
  final def valueToString(valueToConvert: Option[Value]): String = {
    valueToConvert.map(valueToString).getOrElse(valueNotSetString)
  }

  /** Result of this method is returned by `valueToString` in case of `None` value. */
  def valueNotSetString = "N/A"

  /** Converts current indicator's value to string. */
  final def valueToString(): String = valueToString(optionalValue)

  /** Converts indicator to string. */
  override def toString = name+": "+valueToString

  /** Stores historical values of the indicator. */
  final lazy val history = if(hasHistory) new HistoricalValues(optionalValue _, valueToString _)
    else throw Exception("Indicator "+name+" does not support history")

  /** Checks whether this indicator extends `History` trait. */
  final def hasHistory = this.isInstanceOf[History]

  /** Number of historic values required by this indicator to work properly. */
  def requiredHistory: Int = 0
}

object Indicator {
  /** Exception used when requested indicator's value is not defined. */
  case class ValueNotSet() extends java.lang.Exception("Indicator's value has not been set")
}
