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

import org.joda.time.format.DateTimeFormatter

/** A trait for all options. Extend this trait to create custom option instruments. */
trait OptionInstrument extends Derivative {
  def right: OptionRight
  def strike: BigDecimal
  /** Writes data of the option to a string. Uses default date formatter. */
  override def toString: String =
    "["+underlying+"]"+" "+right+" "+Settings.dateFormatter.print(expiration)+" @"+strike.toString()
  /** Returns an option with the same parameters, but inverted right of the option (`Call` is
    * changed to `Put` and vice versa). */
  def invert: OptionInstrument
}
