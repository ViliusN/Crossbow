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
import org.joda.time.DateTime
import org.scalatest.FunSuite

class BidTest extends FunSuite {
  test("Bid test") {
    val s = new Stock("AA", Exchange.nasdaq, "USD")
    val lq = new LastQuote(s)
    val bid = new Bid(lq)
    expect("Bid Price") { bid.name }
    expect(Set(lq)) { bid.dependencies }
    expect(None) { bid.optionalValue }

    val q1 = Quote(s, 5, 100, 4.5, 800, new DateTime)
    lq.send(q1)
    bid.send(q1)
    expect(4.5) { bid.value }

    val q2 = Quote(s, 6.5, 100, 3.5, 800, new DateTime)
    lq.send(q2)
    bid.send(q2)
    expect(3.5) { bid.value }

    lq.unset()
    bid.send(new Data { })
    expect(None) { bid.optionalValue }

    val q3 = Quote(s, 8, 100, 2.5, 800, new DateTime)
    lq.send(q3)
    bid.send(q3)
    expect(2.5) { bid.value }
  }
}
