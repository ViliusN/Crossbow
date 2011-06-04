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

/** Extend this trait to create custom indicators. */
trait Indicator[Value] extends BasicDataListener with Dependant[Indicator[_]]
    with HistoryHolder[Value] with Name {
  // TODO hold history as val in trait History
  // TODO:
  // lazy val history = if(hasHistory) new HistoryContainer(otionalValue _)
  //   else throw new Exception("Indicator "+name+" does not support history")
  // def hasHistory = this isInstanceOf[History]
  import Indicator._

  implicit def valueToOptional(v: Value): Option[Value] = Some(v)
  implicit def emptyToCalculator(empty: Empty): PartialFunction[Data, Option[Value]] = {
    case _ => optionalValue
  }

  private var _value: Option[Value] = None

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

  def set(newValue: Option[Value]) { _value = newValue }
  def set(newValue: Value) { _value = Some(newValue) }
  def unset() { _value = None }
  def isSet = _value.isDefined

  /** Converts the specified value to string. Override this method if custom formatting is
    * needed. */
  def valueToString(valueToConvert: Option[Value]): String =
    valueToConvert map { _.toString } getOrElse(notAvailableString)

  /** Converts current value to string. */
  final def valueToString(): String = valueToString(_value)

  final def receive = {
    case data if(calculate.isDefinedAt(data)) => set(calculate(data))
  }

  protected def calculate: PartialFunction[Data, Option[Value]]
}

object Indicator {
  case class ValueNotSetException() extends Exception("Indicator's value has not been set")

  val notAvailableString = "N/A"
}

trait Name {
  def name: String
}
