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
import lt.norma.crossbow.testutils._
import org.joda.time.DateTime
import org.scalatest.FunSuite

class SpreadTest extends FunSuite {
  val e = 0.000001

  test("Spread test") {
    val s = new Stock("AA", Exchange.nasdaq, "USD")
    val lq = new LastQuote(s)
    val ask = new Ask(lq)
    val bid = new Bid(lq)
    val i = new Spread(ask, bid)
    val l = new IndicatorList(i)
    expect("Spread") { i.name }
    expect(1) { i.dependencies.size }

    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }

    l.send(Quote(s, 5, 100, 4.5, 800, new DateTime))
    expect(0.5) { i.value }

    l.send(Quote(s, 4.49, 100, 4.5, 800, new DateTime))
    approx(-0.01, e) { i.value }

    lq.unset()
    l.send(new Data { })
    expect(None) { i() }

    l.send(Quote(s, 8, 100, 8, 800, new DateTime))
    expect(0) { i.value }
  }
}
