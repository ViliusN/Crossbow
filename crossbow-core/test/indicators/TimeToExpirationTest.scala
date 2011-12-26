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
import org.scalatest.FunSuite
import org.joda.time.{ DateMidnight, DateTime, Days, Duration }

class TimeToExpirationTest extends FunSuite {
  test("name") {
    val tte = new TimeToExpiration(new InstrumentWrapper)
    expect("TimeToExpiration") { tte.name }
  }
  test("dependencies") {
    val d = new InstrumentWrapper
    val tte = new TimeToExpiration(d)
    expect(Set(d)) { tte.dependencies }
  }
  test("calculation") {
    val di = new Derivative {
      def expiration = new DateMidnight(2011, 12, 26)
      def underlying = new Instrument { def exchange = Nyse; def currency = "" }
      def exchange = Nyse
      def currency = ""
    }
    val d = new InstrumentWrapper(di)
    val tte = new TimeToExpiration(d)
    expect(None) { tte() }
    tte.send(SessionOpen(new DateTime(2011, 12, 25, 0, 0, 0)))
    expect(Some(Days.ONE.toStandardDuration)) { tte() }
    tte.send(SessionOpen(new DateTime(2011, 12, 25, 23, 0, 0)))
    expect(Some(Days.ONE.toStandardDuration)) { tte() }
    tte.send(EmptyMessage)
    expect(Some(Days.ONE.toStandardDuration)) { tte() }
    tte.send(SessionOpen(new DateTime(2011, 12, 23, 0, 0, 0)))
    expect(Some(Days.THREE.toStandardDuration)) { tte() }
    tte.send(SessionOpen(new DateTime(2011, 12, 26, 0, 0, 0)))
    expect(Some(new Duration(0))) { tte() }
    tte.send(SessionOpen(new DateTime(2011, 12, 26, 23, 0, 0)))
    expect(Some(new Duration(0))) { tte() }
    tte.send(SessionOpen(new DateTime(2011, 12, 30, 0, 0, 0)))
    expect(Some(new Duration(0))) { tte() }
    d.unset()
    tte.send(SessionOpen(new DateTime(2011, 12, 23, 0, 0, 0)))
    expect(None) { tte() }
  }
  test("alternative constructor") {
    val di = new Derivative {
      def expiration = new DateMidnight(0)
      def underlying = new Instrument { def exchange = Nyse; def currency = "" }
      def exchange = Nyse
      def currency = ""
    }
    val tte = new TimeToExpiration(di)
    expect(Some(di)) { tte.derivative() }
  }
}
