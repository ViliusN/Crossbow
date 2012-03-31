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

import org.scalatest.FunSuite

class VariableTest extends FunSuite {
  test("Variable indicator") {
    val i = new Variable[Double]
    expect("Variable") {
      i.name
    }
    expect(Set()) {
      i.dependencies
    }
    expect(None) {
      i()
    }

    i.set(52.123)
    expect(52.123) {
      i.value
    }
    i.unset()
    expect(None) {
      i()
    }
    i.set(0)
    expect(0) {
      i.value
    }
  }
  test("constructor with initial value") {
    expect("ABC") {
      val i = new Variable("ABC")
      i.value
    }
  }
}
