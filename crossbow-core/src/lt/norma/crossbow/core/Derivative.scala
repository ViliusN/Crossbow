package lt.norma.crossbow.core

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

import org.joda.time.{DateMidnight, Duration}

/**A trait for all derivative instruments. Extend this trait to create custom derivatives. */
trait Derivative extends Instrument {
  /**Underlying instrument. */
  def underlying: Instrument

  /**Expiration time of derivative instrument. */
  def expiration: DateMidnight

  /**Checks whether this derivative instrument is expired on the specified date. */
  def isExpired(now: DateMidnight) = !(now isBefore expiration)

  /**Calculates remaining time to expiration of derivatives instrument. Returns 0 for expired
   * instruments. As `DateMidnight` is used to store expiration date, the result of
   * `timeToExpiration` is rounded to 24-hour precision. Weekends and bank holidays are
   * included. */
  def timeToExpiration(now: DateMidnight): Duration = {
    if (!isExpired(now)) {
      new Duration(now, expiration)
    } else {
      new Duration(0)
    }
  }
}
