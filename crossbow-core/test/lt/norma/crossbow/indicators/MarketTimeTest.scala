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
import lt.norma.crossbow.indicators.MarketTime
import lt.norma.crossbow.messages.{Message, Data, EmptyMessage}

class MarketTimeTest extends FunSuite {
  test("name") {
    val marketTime = new MarketTime
    expect("Market Time") {
      marketTime.name
    }
  }

  test("dependencies") {
    val marketTime = new MarketTime
    expect(Set()) {
      marketTime.dependencies
    }
  }

  test("calculation - initial value") {
    val marketTime = new MarketTime
    expect(None) {
      marketTime()
    }
  }

  test("calculation") {
    val marketTime = new MarketTime
    marketTime.send(new Data {
      def marketTime = new DateTime(11)
    })
    expect(Some(new DateTime(11))) {
      marketTime()
    }
    marketTime.send(new Data {
      def marketTime = new DateTime(22)
    })
    expect(Some(new DateTime(22))) {
      marketTime()
    }
    marketTime.send(EmptyMessage)
    expect(Some(new DateTime(22))) {
      marketTime()
    }
  }

  test("calculation - non-data messages") {
    val marketTime = new MarketTime
    marketTime.send(EmptyMessage)
    expect(None) {
      marketTime()
    }
    marketTime.send(new Message {
      def marketTime = new DateTime(55)
    })
    expect(None) {
      marketTime()
    }
  }
}
