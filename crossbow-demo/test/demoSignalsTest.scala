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
import lt.norma.crossbow.indicators._
import org.scalatest.FunSuite
import TestUtils._

class DifferenceSignalTest extends FunSuite {
  val i1 = new Variable[Double]
  val i2 = new Variable[Double]
  val signal = new DifferenceSignal(i1, i2, 0.5)
  val list = new IndicatorList(signal)

  expect("Difference signal") { signal.name }
  expect(1) { signal.dependencies.size }
  assert { signal.isEmpty }
  assert { signal.isFlat }

  i2.set(5)
  list.send(EmptyData)
  assert { signal.isEmpty }
  assert { signal.isFlat }

  i1.set(5.4)
  list.send(EmptyData)
  assert { signal.isEmpty }
  assert { signal.isFlat }

  i1.set(6)
  list.send(EmptyData)
  expect(Direction.Long) { signal.value }
  assert { signal.isLong }

  i1.set(4.4)
  list.send(EmptyData)
  expect(Direction.Short) { signal.value }
  assert { signal.isShort }
}

class CombiningSignalsTest extends FunSuite {
  import Direction._

  val s1 = new VariableSignal { override def name = "S1" }
  val s2 = new VariableSignal { override def name = "S2" }
  val s3 = new VariableSignal { override def name = "S3" }

  test("all signals") {
    val all = new AllSignals(s1, s2, s3)
    val list = new IndicatorList(all)
    assert { all.isFlat }
    list.send(EmptyData)
    assert { all.isFlat }
    s1.set(Long)
    list.send(EmptyData)
    assert { all.isFlat }
    s2.set(Long)
    list.send(EmptyData)
    assert { all.isFlat }
    s3.set(Long)
    list.send(EmptyData)
    assert { all.isLong }
    s1.set(Short)
    list.send(EmptyData)
    assert { all.isFlat }
    s2.set(Short)
    list.send(EmptyData)
    assert { all.isFlat }
    s3.set(Short)
    list.send(EmptyData)
    assert { all.isShort }
    s1.unset()
    list.send(EmptyData)
    assert { all.isFlat }
  }

  test("most signals") {
    val most = new MostSignals(s1, s2, s3)
    val list = new IndicatorList(most)
    assert { most.isFlat }
    list.send(EmptyData)
    assert { most.isFlat }
    s1.set(Long)
    list.send(EmptyData)
    assert { most.isLong }
    s2.set(Long)
    list.send(EmptyData)
    assert { most.isLong }
    s3.set(Long)
    list.send(EmptyData)
    assert { most.isLong }
    s1.set(Short)
    list.send(EmptyData)
    assert { most.isFlat }
    s2.set(Short)
    list.send(EmptyData)
    assert { most.isFlat }
    s3.set(Short)
    list.send(EmptyData)
    assert { most.isShort }
    s1.unset()
    list.send(EmptyData)
    assert { most.isFlat }
  }

/*
  val most = new MostSignals(s1, s2, s3)
  val notS1AndS2 = new AllSignals(new ReversedSignal(s1), s2)
  val list = new IndicatorList(all, most, notS1AndS2)

  assert { all.isFlat }
  assert { most.isFlat }
  assert { notS1AndS2.isFlat }

  s1.set(Long)
  assert { all.isFlat }
  assert { most.isFlat }
  assert { notS1AndS2.isFlat }
*/
}
