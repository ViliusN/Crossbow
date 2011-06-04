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

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.scalatest.FunSuite

class TradeTest extends FunSuite {
  test("Trade") {
    val s = Stock("MSFT", Exchange.nasdaq, "USD")
    val t = Trade(s, 30.05, 100000, new DateTime(2011, 3, 4, 7, 9, 8, 155, Settings.timeZone))
    expect("Trade MSFT 100000@30.05 [2011-03-04 07:09:08]") { t.toString }
    expect("Trade MSFT 100000@30.05 [Mar, 11 7:9:8.155]") {
      val dateTimeFormatter =
        DateTimeFormat.forPattern("MMM, YY h:m:s.SSS").withZone(Settings.timeZone)
      t.toString(dateTimeFormatter)
    }
  }
}
