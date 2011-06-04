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

class AskTest extends FunSuite {
  test("Ask test") {
    val s = new Stock("AA", Exchange.nasdaq, "USD")
    val lq = new LastQuote(s)
    val ask = new Ask(lq)
    expect("Ask Price") { ask.name }
    expect(Set(lq)) { ask.dependencies }
    expect(None) { ask.optionalValue }

    val q1 = Quote(s, 5, 100, 4.5, 800, new DateTime)
    lq.send(q1)
    ask.send(q1)
    expect(5) { ask.value }

    val q2 = Quote(s, 6.5, 100, 4.5, 800, new DateTime)
    lq.send(q2)
    ask.send(q2)
    expect(6.5) { ask.value }

    lq.unset()
    ask.send(new Data { })
    expect(None) { ask.optionalValue }

    val q3 = Quote(s, 8, 100, 4.5, 800, new DateTime)
    lq.send(q3)
    ask.send(q3)
    expect(8) { ask.value }
  }
}
