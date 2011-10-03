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

class LastQuoteTest extends FunSuite {
  test("LastQuote") {
    val s = new Stock("AA", Nasdaq, "USD")
    val sOther = new Stock("AA", Nyse, "USD")
    val i = new LastQuote(s)
    expect("Last Quote") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }
    expect(s) { i.instrument.value }

    val q1 = Quote(s, 5, 100, 4.5, 800, new DateTime)
    i.send(q1)
    expect(q1) { i.value }

    val q2 = Quote(s, 6.5, 100, 4.5, 800, new DateTime)
    i.send(q2)
    expect(q2) { i.value }

    i.unset()
    expect(None) { i.optionalValue }

    val q3 = Quote(s, 8, 100, 4.5, 800, new DateTime)
    i.send(q3)
    expect(q3) { i.value }

    val q4 = Quote(sOther, 9.5, 100, 4.5, 800, new DateTime)
    i.send(q4)
    expect(q3) { i.value }

    val q5 = Quote(sOther, 11, 100, 4.5, 800, new DateTime)
    i.instrument.set(sOther)
    expect(sOther) { i.instrument.value }
    i.send(q5)
    expect(q5) { i.value }
  }
  test("empty instrument") {
    val s = new Stock("AA", Nasdaq, "USD")
    val i = new LastQuote()
    expect(None) { i() }
    expect(None) { i.instrument() }

    val q1 = Quote(s, 5, 100, 4.5, 800, new DateTime)
    i.send(q1)
    expect(None) { i() }
    i.instrument.set(s)
    i.send(q1)
    expect(q1) { i.value }
    i.instrument.unset
    i.send(q1)
    expect(None) { i() }
  }
  test("constructors") {
    val s = new Stock("AA", Nasdaq, "USD")
    expect(s) {
      val i = new LastQuote(Some(s))
      i.instrument.value
    }
    expect(s) {
      val i = new LastQuote(s)
      i.instrument.value
    }
    expect(None) {
      val i = new LastQuote(None)
      i.instrument()
    }
    expect(None) {
      val i = new LastQuote()
      i.instrument()
    }
  }
}
