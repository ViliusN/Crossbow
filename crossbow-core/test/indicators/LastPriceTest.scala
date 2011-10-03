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

class LastPriceTest extends FunSuite {
  test("Last Price indicator") {
    val s = new Stock("AA", Nasdaq, "USD")
    val lt = new LastTrade(s)
    val i = new LastPrice(lt)
    expect("Last Price") { i.name }
    expect(Set(lt)) { i.dependencies }
    expect(None) { i() }

    val t1 = Trade(s, 5, 100, new DateTime)
    lt.send(t1)
    i.send(t1)
    expect(5) { i.value }

    val t2 = Trade(s, 6.5, 100, new DateTime)
    lt.send(t2)
    i.send(t2)
    expect(6.5) { i.value }

    lt.unset()
    i.send(new Data { })
    expect(None) { i() }

    val t3 = Trade(s, 8, 100, new DateTime)
    lt.send(t3)
    i.send(t3)
    expect(8) { i.value }
  }
}
