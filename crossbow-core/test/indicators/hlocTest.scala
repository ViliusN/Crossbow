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

class HighSoFarTest extends FunSuite {
  test("HighSoFar indicator") {
    val i1 = new Variable[Double] { override def name = "A" }
    val i = new HighSoFar(i1)

    expect("HighSoFar(A)") { i.name }
    expect(Set(i1)) { i.dependencies }
    expect(None) { i() }

    i.send(new Data { })
    expect(None) { i() }

    i1.set(8)
    i.send(new Data { })
    expect(8) { i.value }

    i1.set(9)
    i.send(new Data { })
    expect(9) { i.value }

    i1.set(7)
    i.send(new Data { })
    expect(9) { i.value }

    i1.unset()
    i.send(new Data { })
    expect(9) { i.value }

    i1.set(6)
    i.send(new Data { })
    expect(9) { i.value }

    i1.set(20)
    i.send(new Data { })
    expect(20) { i.value }

    i.send(BarOpen(new DateTime))
    expect(None) { i() }
  }

  test("High indicator") {
    val i1 = new Variable[Double] { override def name = "A" }
    val i = new High(i1)
    val l = new IndicatorList(i)

    expect("High(A)") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }

    i1.set(8)
    l.send(new Data { })
    expect(None) { i() }

    i1.set(9)
    l.send(new Data { })
    expect(None) { i() }

    i1.set(7)
    l.send(new Data { })
    expect(None) { i() }

    i1.unset()
    l.send(new Data { })
    expect(None) { i() }

    i1.set(6)
    l.send(new Data { })
    expect(None) { i() }

    l.send(BarClose(new DateTime))
    expect(9) { i.value }

    l.send(BarOpen(new DateTime))
    expect(None) { i() }
  }

  test("LowSoFar indicator") {
    val i1 = new Variable[Double] { override def name = "A" }
    val i = new LowSoFar(i1)

    expect("LowSoFar(A)") { i.name }
    expect(Set(i1)) { i.dependencies }
    expect(None) { i() }

    i.send(new Data { })
    expect(None) { i() }

    i1.set(8)
    i.send(new Data { })
    expect(8) { i.value }

    i1.set(9)
    i.send(new Data { })
    expect(8) { i.value }

    i1.set(7)
    i.send(new Data { })
    expect(7) { i.value }

    i1.unset()
    i.send(new Data { })
    expect(7) { i.value }

    i1.set(8)
    i.send(new Data { })
    expect(7) { i.value }

    i1.set(-1)
    i.send(new Data { })
    expect(-1) { i.value }

    i.send(BarOpen(new DateTime))
    expect(None) { i() }
  }

  test("Low indicator") {
    val i1 = new Variable[Double] { override def name = "A" }
    val i = new Low(i1)
    val l = new IndicatorList(i)

    expect("Low(A)") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }

    i1.set(8)
    l.send(new Data { })
    expect(None) { i() }

    i1.set(9)
    l.send(new Data { })
    expect(None) { i() }

    i1.set(7)
    l.send(new Data { })
    expect(None) { i() }

    i1.unset()
    l.send(new Data { })
    expect(None) { i() }

    i1.set(6)
    l.send(new Data { })
    expect(None) { i() }

    l.send(BarClose(new DateTime))
    expect(6) { i.value }

    l.send(BarOpen(new DateTime))
    expect(None) { i() }
  }

  test("Open indicator") {
    val i1 = new Variable[Double] { override def name = "A" }
    val i = new Open(i1)
    val l = new IndicatorList(i)

    expect("Open(A)") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }

    i1.set(8)
    l.send(new Data { })
    expect(8) { i.value }

    i1.set(9)
    l.send(new Data { })
    expect(8) { i.value }

    i1.set(7)
    l.send(new Data { })
    expect(8) { i.value }

    i1.unset()
    l.send(new Data { })
    expect(8) { i.value }

    i1.set(6)
    l.send(new Data { })
    expect(8) { i.value }

    l.send(BarClose(new DateTime))
    expect(8) { i.value }

    l.send(BarOpen(new DateTime))
    expect(None) { i() }
  }

  test("Close indicator") {
    val i1 = new Variable[Double] { override def name = "A" }
    val i = new Close(i1)
    val l = new IndicatorList(i)

    expect("Close(A)") { i.name }
    expect(1) { i.dependencies.size }
    expect(None) { i() }

    l.send(new Data { })
    expect(None) { i() }

    i1.set(8)
    l.send(new Data { })
    expect(None) { i() }

    i1.unset()
    l.send(new Data { })
    expect(None) { i() }

    i1.set(6)
    l.send(new Data { })
    expect(None) { i() }

    l.send(BarClose(new DateTime))
    expect(6) { i.value }

    l.send(BarOpen(new DateTime))
    expect(None) { i() }
  }
}
