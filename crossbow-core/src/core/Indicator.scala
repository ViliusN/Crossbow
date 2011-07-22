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

/** Extend this class to create custom indicators.
  *
  * Examples: {{{
  * // This indicator does not update it's value and has no dependencies, but can hold a value if
  * // it is set from outside via `set`/`unset` methods. This is useful as a replacement for mutable
  * // variables inside other indicators.
  * class EmptyIndicator extends Indicator[Int] {
  *   def name = "Holds mutable value"
  *   def dependencies = Empty
  *   def calculate = Empty
  * }
  * }}}*/
abstract class Indicator[Value : Manifest] extends BasicDataListener with Dependant[Indicator[_]]
    with HistoryHolder[Value] with Name {
  import Indicator._

  implicit def valueToOption(v: Value): Option[Value] = Some(v)
  implicit def emptyToCalculator(empty: Empty): PartialFunction[Data, Option[Value]] = {
    case _ => optionalValue
  }

  private var _value: Option[Value] = None
  private var _manifest = manifest[Value]
  private var _optionManifest = manifest[Option[Value]]

  /** Attempts to obtain value of the indicator. If the value is not set, returns the result of
    *  `default` method, which throws `ValueNotSetException` unless overridden. */
  def value: Value = _value getOrElse default

  /** Returns optional value of the indicator. If the value is not set, returns optional result of
    *  `default` method, unless it throws `ValueNotSetException`, in which case returns `None`. */
  def optionalValue: Option[Value] =
    try { Some(value) } catch { case _: ValueNotSetException => None }

  /** Returns optional value of the indicator. If the value is not set, returns optional result of
    *  `default` method, unless it throws `ValueNotSetException`, in which case returns `None`. */
  def apply() = optionalValue

  /** Result of this method is returned by `value` and `optionalValue` methods if the value of this
    * indicator is not set. Unless overridden, `default` method throws `ValueNotSetException`. */
  def default: Value = { throw ValueNotSetException() }

  /** Sets value of the indicator. */
  def set(newValue: Option[Value]) { _value = newValue }

  /** Sets value of the indicator. */
  def set(newValue: Value) { set(Some(newValue)) }

  /** Unsets value of the indicator. */
  def unset() { _value = None }

  /** Checks if indicator's value is set. */
  def isSet = _value.isDefined

  /** Checks if indicator's value is not set. */
  def isEmpty = !isSet

  /** Converts the specified value to string. Override this method if custom formatting is
    * needed. */
  def valueToString(valueToConvert: Value): String = {
    valueToConvert.toString
  }

  /** Converts the specified value to string. Override this method if custom formatting is
    * needed. */
  final def valueToString(valueToConvert: Option[Value]): String = {
    valueToConvert map { v => valueToString(v) } getOrElse(valueNotSetString)
  }

  /** Converts the specified value to string if it's type matches the type of indicator's
    * value. Otherwise returns the returns result of `notAvailableString`. */
  final def valueToString[T : Manifest](valueToConvert: T): String = {
    if(manifest[T].erasure == _manifest.erasure) {
      valueToString(valueToConvert.asInstanceOf[Value])
    } else if(manifest[T].erasure == _optionManifest.erasure) {
      valueToString(valueToConvert.asInstanceOf[Option[Value]])
    } else {
      valueNotSetString
    }
  }

  /** Converts current value to string. */
  final def valueToString(): String = valueToString(_value)

  def valueNotSetString = "N/A"

  final def receive = {
    case data if(calculate.isDefinedAt(data)) => set(calculate(data))
  }

  /** Override this method to update indicator's value on data update. Use object `Empty` when there
    * is no need to calculate value on data updates, for example when indicator's value is assigned
    * externally via `set` method. */
  protected def calculate: PartialFunction[Data, Option[Value]]

  override def toString = name+": "+valueToString()
}

object Indicator {
  case class ValueNotSetException() extends Exception("Indicator's value has not been set")
}

trait Name {
  def name: String
}
