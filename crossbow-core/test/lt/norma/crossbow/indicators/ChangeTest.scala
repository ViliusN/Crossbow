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
import lt.norma.crossbow.indicators.{Change, History, MutableIndicator}

class ChangeTest extends FunSuite {

  class I(n: String) extends MutableIndicator[Double] {
    def name = n

    def dependencies = Empty
  }

  test("name") {
    val target = new I("T") with History
    val change = new Change(3, target)
    expect("Change(3; T)") {
      change.name
    }
  }

  test("dependencies") {
    val target = new I("T") with History
    val change = new Change(3, target)
    expect(2) {
      change.dependencies.size
    }
    assert {
      change.dependencies.contains(target)
    }
  }

  test("calculation") {
    val target = new I("A") with History
    val change = new Change(3, target)
    expect(None) {
      change()
    }
    target.set(1)
    expect(None) {
      change()
    }
    target.history.update()
    target.set(2)
    target.history.update()
    expect(None) {
      change()
    }
    target.set(3)
    target.history.update()
    expect(Some(2)) {
      change()
    }
    target.set(4)
    expect(Some(3)) {
      change()
    }
    target.history.update()
    expect(Some(2)) {
      change()
    }
    target.unset()
    expect(None) {
      change()
    }
    target.history.update()
    target.set(8)
    expect(Some(5)) {
      change()
    }
    target.history.update()
    expect(Some(4)) {
      change()
    }
    target.history.update()
    expect(None) {
      change()
    }
  }

  test("Change test - invalid period") {
    intercept[Exception] {
      new Change(0, new I("A") with History)
    }
    intercept[Exception] {
      new Change(-5, new I("A") with History)
    }
  }

  test("Change test - last bar") {
    val target = new I("A") with History
    val change = new Change(1, target)
    expect(None) {
      change()
    }
    target.set(1)
    expect(None) {
      change()
    }
    target.history.update()
    expect(Some(0)) {
      change()
    }
    target.set(3)
    expect(Some(2)) {
      change()
    }
    target.history.update()
    expect(Some(0)) {
      change()
    }
    target.set(6)
    expect(Some(3)) {
      change()
    }
  }
}
