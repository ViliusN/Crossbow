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

/**Functional indicators are useful to wrap one function as an indicator. Value of this indicator
 * is recalculated every time it's accessed. There are no methods to set or unset functional
 * indicator's value from outside.
 *
 * Care must be taken, when accessing value of functional indicator multiple times, as every call
 * to `calculate` method will repeat the calculation with potentially different result (and
 * performance penalty). Therefore a situation is possible were for example `true` result of
 * `isSet` method is immediately followed by `None` result of `optionalValue`. */
trait FunctionalIndicator[Value] extends Indicator[Value] {
  final def optionalValue: Option[Value] =
    try {
      calculate.orElse(Some(default))
    } catch {
      case _: Indicator.ValueNotSet => None
    }

  /**Override this method to create custom behaviour of your indicator. */
  def calculate: Option[Value]
}
