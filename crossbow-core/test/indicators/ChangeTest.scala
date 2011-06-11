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

class ChangeTest extends FunSuite {
  class I(n: String) extends Indicator[Double] {
    def name = n
    def dependencies = Empty
    def calculate = Empty
  }

  test("Change test") {
    val i1 = new I("A") with History
    val i = new Change(3, i1)

    expect("Change(3; A)") { i.name }
    expect(Set(i1)) { i.dependencies }
    expect(None) { i() }

    i1.set(1)
    i.send(new Data { })
    expect(None) { i() }

    i1.set(1)
    i1.history.update()
    i1.set(2)
    i1.history.update()
    i.send(new Data { })
    expect(None) { i() }

    i1.set(3)
    i1.history.update()
    i.send(new Data { })
    expect(Some(2)) { i() }

    i1.set(4)
    i.send(new Data { })
    expect(Some(3)) { i() }

    i1.history.update()
    i.send(new Data { })
    expect(Some(2)) { i() }

    i1.unset()
    i.send(new Data { })
    expect(None) { i() }

    i1.history.update()
    i1.set(8)
    i.send(new Data { })
    expect(Some(5)) { i() }
    i1.history.update()
    i.send(new Data { })
    expect(Some(4)) { i() }
    i1.history.update()
    i.send(new Data { })
    expect(None) { i() }
  }

  test("Change test - invalid period") {
    intercept[IllegalArgumentException] { new Change(0, new I("A") with History) }
    intercept[IllegalArgumentException] { new Change(-5, new I("A") with History) }
  }

  test("Change test - last bar") {
    val i1 = new I("A") with History
    val i = new Change(1, i1)

    expect(None) { i() }

    i1.set(1)
    i.send(new Data { })
    expect(None) { i() }

    i1.history.update()
    i.send(new Data { })
    expect(Some(0)) { i() }

    i1.set(3)
    i.send(new Data { })
    expect(Some(2)) { i() }

    i1.history.update()
    i.send(new Data { })
    expect(Some(0)) { i() }

    i1.set(6)
    i.send(new Data { })
    expect(Some(3)) { i() }
  }
}
