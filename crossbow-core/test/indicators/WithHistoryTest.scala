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

class WithHistoryTest extends FunSuite {
  test("is History trait extended") {
    val target = new Variable[Int]
    val indicator = new WithHistory(target)
    assert ( !target.isInstanceOf[History], "should not extend History" )
    assert ( indicator.isInstanceOf[History], "should extend History" )
  }
  test("name") {
    val target = new Variable[Int] { override def name = "ABC" }
    val indicator = new WithHistory(target)
    expect("WithHistory(ABC)") { indicator.name }
  }
  test("dependencies") {
    val target = new Variable[Int]
    val indicator = new WithHistory(target)
    expect(Set(target)) { indicator.dependencies }
  }
  test("initial empty value") {
    val target = new Variable[Int]
    val indicator = new WithHistory(target)
    val list = new IndicatorList(indicator)
    assert ( indicator.isEmpty, "should be empty as target is empty" )
  }
  test("initial value") {
    val target = new Variable[Int]
    target.set(15)
    expect(15) { target.value }
    val indicator = new WithHistory(target)
    val list = new IndicatorList(indicator)
    expect(15) { indicator.value }
  }
}
