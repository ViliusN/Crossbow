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

package lt.norma.crossbow.core.indicators

import lt.norma.crossbow.core._
import lt.norma.crossbow.core.messages._
import org.scalatest.FunSuite

class AlternativeTest extends FunSuite {
  test("indicator test") {
    val target1 = new Variable[Double] { override def name = "T1" }
    val target2 = new Variable[Double] { override def name = "T2" }
    val i = new Alternative(target1, target2)
    val list = new IndicatorList(i)
    expect("T1 or alternative T2") { i.name }
    expect(Set(target1, target2)) { i.dependencies }
    expect(None) { i() }

    expect(None) { i() }
    target2.set(1)
    expect(1) { i.value }
    target1.set(2)
    expect(2) { i.value }
    target1.unset()
    expect(1) { i.value }
    target2.unset()
    expect(None) { i() }
  }
  test("initialization") {
    expect(None) {
      val target1 = new Variable[Double]
      val target2 = new Variable[Double]
      val i = new Alternative(target1, target2)
      val list = new IndicatorList(i)
      i()
    }
    expect(123) {
      val target1 = new Variable[Double]
      val target2 = new Variable[Double]
      target1.set(123)
      val i = new Alternative(target1, target2)
      val list = new IndicatorList(i)
      i.value
    }
    expect(456) {
      val target1 = new Variable[Double]
      val target2 = new Variable[Double]
      target2.set(456)
      val i = new Alternative(target1, target2)
      val list = new IndicatorList(i)
      i.value
    }
    expect(789) {
      val target1 = new Variable[Double]
      target1.set(789)
      val target2 = new Variable[Double]
      target2.set(555)
      val i = new Alternative(target1, target2)
      val list = new IndicatorList(i)
      i.value
    }
  }
}
