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

package lt.norma.crossbow.indicators

import lt.norma.crossbow.core._
import org.joda.time.DateTime
import org.scalatest.FunSuite

class CrossedMarketTest extends FunSuite {
  test("CrossedMarket test") {
    val s = new Stock("AA", Exchange.nasdaq, "USD")
    val lq = new LastQuote(s)
    val ask = new Ask(lq)
    val bid = new Bid(lq)
    val i = new CrossedMarket(ask, bid)
    expect("Crossed Market") { i.name }
    expect(Set(ask, bid)) { i.dependencies }
    expect(None) { i() }

    ask.set(5)
    expect(None) { i() }
    ask.unset()
    bid.set(3)
    expect(None) { i() }

    ask.set(5)
    bid.set(4.5)
    i.send(new Data { })
    expect(false) { i.value }

    ask.set(4.49)
    bid.set(4.5)
    i.send(new Data { })
    expect(true) { i.value }

    ask.unset()
    i.send(new Data { })
    expect(None) { i() }

    ask.set(8)
    bid.set(8)
    i.send(new Data { })
    expect(false) { i.value }

    bid.unset()
    i.send(new Data { })
    expect(None) { i() }

    ask.set(0)
    bid.set(0.000001)
    i.send(new Data { })
    expect(true) { i.value }
  }
}
