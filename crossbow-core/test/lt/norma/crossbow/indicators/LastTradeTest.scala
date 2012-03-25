package lt.norma.crossbow.indicators

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

import lt.norma.crossbow.core._
import lt.norma.crossbow.messages._
import org.joda.time.DateTime
import org.scalatest.FunSuite
import lt.norma.crossbow.indicators.{InstrumentWrapper, LastTrade}

class LastTradeTest extends FunSuite {
  test("LastTrade") {
    val s = new Stock("AA", Exchange.Nasdaq, "USD")
    val sOther = new Stock("AA", Exchange.Nyse, "USD")
    val i = new LastTrade(s)

    expect("Last Trade") {
      i.name
    }
    expect(1) {
      i.dependencies.size
    }
    expect(None) {
      i()
    }
    expect(s) {
      i.instrument.value
    }

    val t1 = Trade(s, 5, 100, new DateTime)
    i.send(t1)
    expect(t1) {
      i.value
    }

    val t2 = Trade(s, 10.01, 100, new DateTime)
    i.send(t2)
    expect(t2) {
      i.value
    }

    i.unset()
    expect(None) {
      i()
    }

    val t3 = Trade(s, 8, 100, new DateTime)
    i.send(t3)
    expect(t3) {
      i.value
    }

    val t4 = Trade(sOther, 9.5, 100, new DateTime)
    i.send(t4)
    expect(t3) {
      i.value
    }

    val t5 = Trade(sOther, 11, 100, new DateTime)
    i.instrument.set(sOther)
    expect(sOther) {
      i.instrument.value
    }
    i.send(t5)
    expect(t5) {
      i.value
    }
  }
  test("empty instrument") {
    val s = new Stock("AA", Exchange.Nasdaq, "USD")
    val i = new LastTrade()
    expect(None) {
      i()
    }
    expect(None) {
      i.instrument()
    }

    val t1 = Trade(s, 5, 100, new DateTime)
    i.send(t1)
    expect(None) {
      i()
    }
    i.instrument.set(s)
    i.send(t1)
    expect(t1) {
      i.value
    }
    i.instrument.unset
    i.send(t1)
    expect(None) {
      i()
    }
  }
  test("constructors") {
    val s = new Stock("AA", Exchange.Nasdaq, "USD")
    expect(Some(s)) {
      val i = new LastTrade(new InstrumentWrapper(s))
      i.instrument()
    }
    expect(Some(s)) {
      val i = new LastTrade(s)
      i.instrument()
    }
    expect(None) {
      val i = new LastTrade(new InstrumentWrapper)
      i.instrument()
    }
    expect(None) {
      val i = new LastTrade()
      i.instrument()
    }
  }
}
