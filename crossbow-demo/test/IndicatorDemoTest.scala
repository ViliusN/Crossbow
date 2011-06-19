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

package lt.norma.crossbow.demo

import lt.norma.crossbow.core._
import org.scalatest.FunSuite

class Indicator1Test extends FunSuite {
  val i = new Indicator1
  expect("My first indicator") { i.name }
  expect(Set.empty) { i.dependencies }

  expect(None) { i.optionalValue }
  expect(None) { i() }
  intercept[Indicator.ValueNotSetException] { i.value }
  assert { !i.isSet }

  i.set(1.5)
  expect(Some(1.5)) { i.optionalValue }
  expect(Some(1.5)) { i() }
  expect(1.5) { i.value }
  assert { i.isSet }

  i.unset()
  expect(None) { i.optionalValue }
  assert { !i.isSet }
}
