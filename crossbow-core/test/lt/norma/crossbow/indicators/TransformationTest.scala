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

import lt.norma.crossbow.core.Empty
import lt.norma.crossbow.indicators.ImplicitValueConverter._
import org.scalatest.FunSuite

class TransformationTest extends FunSuite {
  test("TransformationN - name") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[Int] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Int] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new TransformationN(target1, target2, target3)(_ => None)
    expect("Transformation(T1;T2;T3)") {
      transformed.name
    }
  }

  test("TransformationN - dependencies") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[Int] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Int] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new TransformationN(target1, target2, target3)(_ => None)
    expect(Set(target1, target2, target3)) {
      transformed.dependencies
    }
  }

  test("TransformationN - calculation - initial value") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[Int] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Int] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new TransformationN(target1, target2, target3)(_ => None)
    expect(None) {
      transformed()
    }
  }

  test("TransformationN - calculation") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[Int] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Int] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new TransformationN(target1, target2, target3)({
      case Seq(Some(t1), Some(t2), Some(t3)) => t1 + t2 + t3
      case _ => None
    })
    target1.set(9)
    target2.set(10)
    target3.set(11)
    expect(Some(30)) { transformed() }
  }

  test("TransformationN - calculation - one target") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val transformed = new TransformationN(target1)({
      case Seq(Some(t1)) => t1 + t1
      case _ => None
    })
    target1.set(9)
    expect(Some(18)) {
      transformed()
    }
  }

  test("Transformation - name") {
    val target = new MutableIndicator[Int] {
      def name = "T"

      def dependencies = Empty
    }
    val transformed = new Transformation(target)(_ => None)
    expect("Transformation(T)") {
      transformed.name
    }
  }

  test("Transformation - dependencies") {
    val target = new MutableIndicator[Int] {
      def name = "T"

      def dependencies = Empty
    }
    val transformed = new Transformation(target)(_ => None)
    expect(Set(target)) {
      transformed.dependencies
    }
  }

  test("Transformation - calculation - initial value") {
    val target = new MutableIndicator[Int] {
      def name = "T"

      def dependencies = Empty
    }
    val transformed = new Transformation(target)({
      case Some(t) => Some(t * t)
      case _ => None
    })
    expect(None) {
      transformed()
    }
  }

  test("Transformation - calculation") {
    val target = new MutableIndicator[Int] {
      def name = "T"

      def dependencies = Empty
    }
    val transformed = new Transformation(target)({
      case Some(t) => Some(t * t)
      case _ => None
    })
    target.set(8)
    expect(Some(64)) {
      transformed()
    }
  }

  test("Transformation2 - name") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"

      def dependencies = Empty
    }
    val transformed = new Transformation2(target1, target2)((_, _) => None)
    expect("Transformation(T1;T2)") {
      transformed.name
    }
  }

  test("Transformation2 - dependencies") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"
      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"
      def dependencies = Empty
    }
    val transformed = new Transformation2(target1, target2)((_, _) => None)
    expect(Set(target1, target2)) {
      transformed.dependencies
    }
  }

  test("Transformation2 - calculation - initial value") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"

      def dependencies = Empty
    }
    val transformed = new Transformation2(target1, target2)((_, _) => None)
    expect(None) {
      transformed()
    }
  }

  test("Transformation2 - calculation") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"

      def dependencies = Empty
    }
    val transformed = new Transformation2(target1, target2)({
      case (Some(t), Some(s)) => Some(s + s + (t * t))
      case _ => None
    })
    target1.set(9)
    target2.set("abc")
    expect(Some("abcabc81")) {
      transformed()
    }
  }

  test("Transformation3 - name") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Double] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new Transformation3(target1, target2, target3)((_, _, _) => None)
    expect("Transformation(T1;T2;T3)") {
      transformed.name
    }
  }

  test("Transformation3 - dependencies") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Double] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new Transformation3(target1, target2, target3)((_, _, _) => None)
    expect(Set(target1, target2, target3)) {
      transformed.dependencies
    }
  }

  test("Transformation3 - calculation - initial value") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Double] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new Transformation3(target1, target2, target3)((_, _, _) => None)
    expect(None) {
      transformed()
    }
  }

  test("Transformation3 - calculation") {
    val target1 = new MutableIndicator[Int] {
      def name = "T1"

      def dependencies = Empty
    }
    val target2 = new MutableIndicator[String] {
      def name = "T2"

      def dependencies = Empty
    }
    val target3 = new MutableIndicator[Double] {
      def name = "T3"

      def dependencies = Empty
    }
    val transformed = new Transformation3(target1, target2, target3)({
      case (Some(t), Some(s), Some(d)) => s + s + (t + d)
      case _ => None
    })
    target1.set(9)
    target2.set("abc")
    target3.set(0.5)
    expect(Some("abcabc9.5")) {
      transformed()
    }
  }
}
