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

/** Indicator, whose value can be set and unset from the outside. */
trait MutableIndicator[Value] extends Indicator[Value] {
  private var _value: Option[Value] = None

  final def optionalValue: Option[Value] =
    try {
      _value.orElse(Some(default))
    } catch {
      case _: Indicator.ValueNotSet => None
    }

  /** Sets current value of the indicator. */
  final def set(newValue: Option[Value]) {
    _value = newValue
  }

  /** Sets current value of the indicator. */
  final def set(newValue: Value) {
    _value = Some(newValue)
  }

  /** Un-sets current value of the indicator. */
  final def unset() {
    _value = None
  }
}
