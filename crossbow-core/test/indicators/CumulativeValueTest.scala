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

class CumulativeValueTest extends FunSuite {
  test("name") {
    val target = new MutableIndicator[Double] { def name = "T"; def dependencies = Empty }
    val cumulative = new CumulativeValue(target)
    expect("Cumulative(T)") { cumulative.name }
  }

  test("dependencies") {
    val target = new MutableIndicator[Double] { def name = "T"; def dependencies = Empty }
    val cumulative = new CumulativeValue(target)
    expect(1) { cumulative.dependencies.size }
  }

  test("initial value") {
    val target = new MutableIndicator[Double] { def name = "T"; def dependencies = Empty }
    val cumulative = new CumulativeValue(target)
    expect(0) { cumulative.value }
  }

  test("calculation") {
    val target = new MutableIndicator[Double] { def name = "T"; def dependencies = Empty }
    val cumulative = new CumulativeValue(target)
    target.set(12)
    expect(0) { cumulative.value }
    cumulative.send(BarClose(new DateTime))
    expect(12) { cumulative.value }
    cumulative.send(BarClose(new DateTime))
    expect(24) { cumulative.value }
    target.set(-1)
    cumulative.send(BarClose(new DateTime))
    cumulative.send(BarClose(new DateTime))
    expect(22) { cumulative.value }
    target.unset()
    cumulative.send(BarClose(new DateTime))
    expect(22) { cumulative.value }
  }
}
