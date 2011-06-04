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
import lt.norma.crossbow.testutils._
import org.joda.time.DateTime
import org.scalatest.FunSuite

class RsiTest extends FunSuite {
  class I(n: String) extends Indicator[Double] {
    def name = n
    def dependencies = Empty
    def calculate = Empty
  }

  test("RSI indicator") {
/* TODO
    val i1 = new I("A")
    val i = new Rsi(5, i1)
    val l = new IndicatorList(i)
    val e = 0.000005

    expect("EMA(5; A)") { i.name }
    expect(2) { i.dependencies.size }
    expect(None) { i() }

    i1.set(0.5)
    l.send(new BarClose(new DateTime))
    expect(None) { i() }
*/
  }

  test("RSI indicator - invalid period") {
    intercept[IllegalArgumentException] { new Rsi(0, new I("A") with History)  }
    intercept[IllegalArgumentException] { new Rsi(-5, new I("A") with History)  }
  }
}
