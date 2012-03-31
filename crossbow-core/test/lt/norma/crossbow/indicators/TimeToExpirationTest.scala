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

import lt.norma.crossbow.core.{Derivative, IndicatorList, Instrument}
import lt.norma.crossbow.core.Exchange.Nyse
import lt.norma.crossbow.messages.{Message, EmptyMessage, SessionOpen}
import lt.norma.crossbow.messages.{EmptyMessage, Message, SessionOpen}
import org.scalatest.FunSuite
import org.joda.time.{DateMidnight, DateTime, Days, Duration}
import lt.norma.crossbow.indicators.{InstrumentWrapper, TimeToExpiration}

class TimeToExpirationTest extends FunSuite {
  test("name") {
    val tte = new TimeToExpiration(new InstrumentWrapper)
    expect("TimeToExpiration") {
      tte.name
    }
  }

  test("dependencies") {
    val d = new InstrumentWrapper
    val tte = new TimeToExpiration(d)
    expect(2) {
      tte.dependencies.size
    }
  }

  test("calculation") {
    val derivativeInstrument = new Derivative {
      def expiration = new DateMidnight(2011, 12, 26)

      def underlying = new Instrument {
        def exchange = Nyse;

        def currency = ""
      }

      def exchange = Nyse

      def currency = ""
    }
    val derivative = new InstrumentWrapper(derivativeInstrument)
    val tte = new TimeToExpiration(derivative)
    val list = new IndicatorList(tte)
    list.send(SessionOpen(new DateTime(2011, 12, 25, 0, 0, 0)))
    expect(Some(Days.ONE.toStandardDuration)) {
      tte()
    }
    list.send(SessionOpen(new DateTime(2011, 12, 25, 23, 0, 0)))
    expect(Some(Days.ONE.toStandardDuration)) {
      tte()
    }
    list.send(EmptyMessage)
    expect(Some(Days.ONE.toStandardDuration)) {
      tte()
    }
    list.send(SessionOpen(new DateTime(2011, 12, 23, 0, 0, 0)))
    expect(Some(Days.THREE.toStandardDuration)) {
      tte()
    }
    list.send(SessionOpen(new DateTime(2011, 12, 26, 0, 0, 0)))
    expect(Some(new Duration(0))) {
      tte()
    }
    list.send(SessionOpen(new DateTime(2011, 12, 26, 23, 0, 0)))
    expect(Some(new Duration(0))) {
      tte()
    }
    list.send(SessionOpen(new DateTime(2011, 12, 30, 0, 0, 0)))
    expect(Some(new Duration(0))) {
      tte()
    }
    derivative.unset()
    list.send(SessionOpen(new DateTime(2011, 12, 23, 0, 0, 0)))
    expect(None) {
      tte()
    }
  }

  test("calculation - instrument is not set") {
    val derivative = new InstrumentWrapper()
    val tte = new TimeToExpiration(derivative)
    val list = new IndicatorList(tte)
    list.send(SessionOpen(new DateTime(2011, 12, 23, 0, 0, 0)))
    expect(None) {
      tte()
    }
  }

  test("calculation - initial value") {
    val derivativeInstrument = new Derivative {
      def expiration = new DateMidnight(2011, 12, 26)

      def underlying = new Instrument {
        def exchange = Nyse;

        def currency = ""
      }

      def exchange = Nyse

      def currency = ""
    }
    val derivative = new InstrumentWrapper(derivativeInstrument)
    val tte = new TimeToExpiration(derivative)
    val list = new IndicatorList(tte)
    expect(None) {
      tte()
    }
  }

  test("calculation - non-data messages") {
    val derivativeInstrument = new Derivative {
      def expiration = new DateMidnight(2011, 12, 26)

      def underlying = new Instrument {
        def exchange = Nyse;

        def currency = ""
      }

      def exchange = Nyse

      def currency = ""
    }
    val derivative = new InstrumentWrapper(derivativeInstrument)
    val tte = new TimeToExpiration(derivative)
    val list = new IndicatorList(tte)
    list.send(EmptyMessage)
    list.send(new Message {
      def marketTime = new DateTime(123)
    })
    expect(None) {
      tte()
    }
  }

  test("calculation - non-derivative instrument") {
    val nonDerivativeInstrument = new Instrument {
      def exchange = Nyse

      def currency = ""
    }
    val derivative = new InstrumentWrapper(nonDerivativeInstrument)
    val tte = new TimeToExpiration(derivative)
    val list = new IndicatorList(tte)
    list.send(SessionOpen(new DateTime(2011, 12, 25, 0, 0, 0)))
    expect(None) {
      tte()
    }
  }

  test("alternative constructor") {
    val di = new Derivative {
      def expiration = new DateMidnight(0)

      def underlying = new Instrument {
        def exchange = Nyse;

        def currency = ""
      }

      def exchange = Nyse

      def currency = ""
    }
    val tte = new TimeToExpiration(di)
    expect(Some(di)) {
      tte.derivative()
    }
  }
}
