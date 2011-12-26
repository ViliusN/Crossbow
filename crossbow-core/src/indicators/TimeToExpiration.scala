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

import lt.norma.crossbow.core._
import org.joda.time.{ Duration }

/** Calculates time to expiration of the specified derivative instrument. */
class TimeToExpiration(val derivative: InstrumentWrapper) extends Indicator[Duration] {
  def this(derivativeInstrument: Derivative) = this(new InstrumentWrapper(derivativeInstrument))
  def name = "TimeToExpiration"
  def dependencies = Set(derivative)
  def calculate = {
    case _ if(!checkDerivative) =>
      None
    case data: Data if(checkDerivative) =>
      derivative.value.asInstanceOf[Derivative].timeToExpiration(data.marketTime.toDateMidnight)
  }
  private def checkDerivative = derivative.isSet && derivative.value.isInstanceOf[Derivative]
}
