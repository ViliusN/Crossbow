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

class DefaultTest extends FunSuite {
  test("Default") {
    val target = new Variable[Double] { override def name = "T" }
    val i = new Default(target, 8.0)
    expect("T with default 8.0") { i.name }
    expect(2) { i.dependencies.size }
    expect(8) { i.value }

    i.send(EmptyData)
    expect(8) { i.value }
    target.set(5)
    i.send(EmptyData)
    expect(5) { i.value }
    target.set(-1)
    i.send(EmptyData)
    expect(-1) { i.value }
    target.unset()
    i.send(EmptyData)
    expect(8) { i.value }
  }
}
