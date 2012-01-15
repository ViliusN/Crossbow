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

package lt.norma.crossbow.core.indicators

import lt.norma.crossbow.core._
import lt.norma.crossbow.core.messages._
import org.joda.time.DateTime
import org.scalatest.FunSuite

class MidPriceTest extends FunSuite {
  test("MidPrice test") {
    val s = new Stock("AA", Exchange.Nasdaq, "USD")
    val lq = new LastQuote(s)
    val i = new MidPrice(new Ask(lq), new Bid(lq))
    val l = new IndicatorList(i)

    expect("Mid-price") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(Quote(s, 5, 500, 4.5, 800, new DateTime))
    expect(4.75) { i.value }

    l.send(Quote(s, 4.49, 500, 4.5, 800, new DateTime))
    expect(4.495) { i.value }

    l.send(Quote(s, 8, 500, 8, 800, new DateTime))
    expect(8) { i.value }
  }
}
