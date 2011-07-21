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

/** Option rights: call and put. */
sealed trait OptionRight {
  def invert: OptionRight
}
/** Option rights: Call and Put. */
object OptionRight {
  /** Call option right. */
  case object Call extends OptionRight {
    def invert = Put
  }
  /** Put option right. */
  case object Put extends OptionRight {
    def invert = Call
  }

  /** Tries to parse the specified string (case-insensitive) to option right. Strings "c" or "call"
    * are parsed to `OptionRight.Call`. Strings "p" or "put" are parsed to `OptionRight.Put`.
    * Otherwise throws an exception. */
  def parse(string: String) = string.toLowerCase match {
    case "c" | "call" => Call
    case "p" | "put" => Put
    case _ => throw new Exception("Unable to parse '"+string+"' as option right.")
  }
}
