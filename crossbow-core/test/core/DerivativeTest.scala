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

package lt.norma.crossbow.core

import org.joda.time.{ DateMidnight, Days, Duration }
import org.scalatest.FunSuite

class DerivativeTest extends FunSuite {
  test("isExpired") {
    val instrument = new Instrument { val currency = "USD"; val exchange = Nasdaq }
    val derivative = new Derivative {
      val currency = "USD"
      val exchange = Nasdaq
      val underlying = instrument
      val expiration = new DateMidnight(2011, 11, 30)
    }
    assert { !derivative.isExpired(new DateMidnight(2011, 11, 20)) }
    assert { !derivative.isExpired(new DateMidnight(2011, 11, 29)) }
    assert { derivative.isExpired(new DateMidnight(2011, 11, 30)) }
    assert { derivative.isExpired(new DateMidnight(2011, 12, 1)) }
    assert { derivative.isExpired(new DateMidnight(2012, 1, 1)) }
  }

  test("timeToExpiration") {
    val instrument = new Instrument { val currency = "USD"; val exchange = Nasdaq }
    val derivative = new Derivative {
      val currency = "USD"
      val exchange = Nasdaq
      val underlying = instrument
      val expiration = new DateMidnight(2011, 11, 30)
    }
    expect(Days.ZERO.toStandardDuration()) {
      derivative.timeToExpiration(new DateMidnight(2011, 11, 30))
    }
    expect(Days.ONE.toStandardDuration()) {
      derivative.timeToExpiration(new DateMidnight(2011, 11, 29))
    }
    expect(Days.days(10).toStandardDuration()) {
      derivative.timeToExpiration(new DateMidnight(2011, 11, 20))
    }
    expect(Days.days(3652).toStandardDuration()) {
      derivative.timeToExpiration(new DateMidnight(2001, 11, 30))
    }
    expect(Days.ZERO.toStandardDuration()) {
      derivative.timeToExpiration(new DateMidnight(2012, 11, 30))
    }
  }
}
