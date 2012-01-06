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
  def name: String;
  def optionalValue: Option[Value]
  final def value: Value = optionalValue.getOrElse(default)
  def default: Value = { throw Indicator.ValueNotSet() }
  final def apply() = optionalValue
  final def isSet = optionalValue.isDefined
  final def isEmpty = !isSet
  def valueToString(valueToConvert: Value): String = valueToConvert.toString
  final def valueToString(valueToConvert: Option[Value]): String = {
    valueToConvert.map(valueToString).getOrElse(valueNotSetString)
  }
  def valueNotSetString = "N/A"
  final def valueToString(): String = valueToString(value)
  def requiredHistory: Int = 0
  def hasHistory = this.isInstanceOf[History]
  lazy val history = if(hasHistory) new HistoricalValues(optionalValue _, valueToString _)
    else throw new Exception("Indicator "+name+" does not support history")
  override def toString = name+": "+valueToString()
}

object Indicator {
  case class ValueNotSet() extends java.lang.Exception("Indicator's value has not been set")
}

trait FunctionalIndicator[Value] extends Indicator[Value] {
  def calculate: Option[Value]
  def optionalValue: Option[Value] = calculate
}

trait MutableIndicator[Value] extends Indicator[Value] {
  private var _value: Option[Value] = None
  def optionalValue: Option[Value] = _value
  def set(newValue: Option[Value]) { _value = newValue }
  def set(newValue: Value) { _value = Some(newValue) }
  def unset() { _value = None }
}

trait ListenerIndicator[Value] extends MutableIndicator[Value] with BasicListener
