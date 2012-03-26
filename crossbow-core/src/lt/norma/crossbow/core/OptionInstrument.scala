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

/** A trait for all options. Extend this trait to create custom option instruments. */
trait OptionInstrument extends Derivative {
  /** Right (call/put) of the option instrument. */
  def right: OptionRight

  /** Strike price of the option instrument.*/
  def strike: BigDecimal

  /** Returns an option with the same parameters, but inverted right of the option (`Call` is
    * changed to `Put` and vice versa). */
  def invert: OptionInstrument
}
