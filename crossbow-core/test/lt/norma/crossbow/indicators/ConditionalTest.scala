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

class ConditionalTest extends FunSuite {
  test("Conditional") {
    val target = new Variable[Double] {
      override def name = "T"
    }
    val i = new Conditional(target)(_ > 5)
    expect("Conditional T") {
      i.name
    }
    expect(Set(target)) {
      i.dependencies
    }
    expect(None) {
      i()
    }

    target.set(1)
    expect(None) {
      i()
    }
    target.set(5)
    expect(None) {
      i()
    }
    target.set(6)
    expect(6) {
      i.value
    }
    target.set(999)
    expect(999) {
      i.value
    }
    target.set(0)
    expect(None) {
      i()
    }
    target.unset()
    expect(None) {
      i()
    }
  }
}
