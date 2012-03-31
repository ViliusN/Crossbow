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

import java.math.MathContext
import java.math.RoundingMode
import org.joda.time.DateMidnight
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

/**
 * Static project-wide settings. These settings should not be changed without rebuilding the entire
 * library and running unit tests.
 */
object Settings {
  /**Default date format. */
  lazy val dateFormat = "yyyy-MM-dd"
  /**Default time format. */
  lazy val timeFormat = "HH:mm:ss"
  /**Default date-time format. */
  lazy val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"
  /**Default time zone. */
  lazy val timeZone = DateTimeZone.forID("America/New_York")
  /**Default date formatter. */
  lazy val dateFormatter = DateTimeFormat.forPattern(dateFormat).withZone(timeZone)
  /**Default date-time formatter. */
  lazy val dateTimeFormatter = DateTimeFormat.forPattern(dateTimeFormat).withZone(timeZone)
  /**Math context for BigDecimal price calculations. Precision is set to 15 decimal places and
   *  rounding mode to "half up". */
  lazy val priceMathContext = new MathContext(15, RoundingMode.HALF_UP);
}
