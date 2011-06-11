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

package lt.norma.crossbow.core

import org.scalatest.FunSuite

class DependantTest extends FunSuite {
  class D(deps: D*) extends Dependant[D] {
    def dependencies = deps.toSet
  }

  test("Deep dependencies") {
    val d4_1 = new D()
    val d4_2 = new D()
    expect(Set.empty) { d4_1.dependencies }
    expect(Set.empty) { d4_2.dependencies }
    expect(Nil) { d4_1.deepDependencies }
    expect(Nil) { d4_2.deepDependencies }

    val d3_1 = new D(d4_1)
    val d3_2 = new D()
    val d3_3 = new D(d4_2)
    val d3_4 = new D()
    expect(Set(d4_1)) { d3_1.dependencies }
    expect(Set.empty) { d3_2.dependencies }
    expect(Set(d4_2)) { d3_3.dependencies }
    expect(Set.empty) { d3_4.dependencies }
    expect(List(d4_1)) { d3_1.deepDependencies }
    expect(Nil) { d3_2.deepDependencies }
    expect(List(d4_2)) { d3_3.deepDependencies }
    expect(Nil) { d3_4.deepDependencies }

    val d2_1 = new D(d3_1)
    val d2_2 = new D(d4_2, d3_3, d3_1, d3_2)
    val d2_3 = new D(d3_3)
    val d2_4 = new D(d3_3, d3_4)
    expect(Set(d3_1)) { d2_1.dependencies }
    expect(Set(d3_1, d3_2, d3_3, d4_2)) { d2_2.dependencies }
    expect(Set(d3_3)) { d2_3.dependencies }
    expect(Set(d3_3, d3_4)) { d2_4.dependencies }
    expect(List(d4_1, d3_1)) { d2_1.deepDependencies }
    expect(5) { d2_2.deepDependencies.size }
    expect(Set(d3_1, d3_2, d3_3, d4_1, d4_2)) { d2_2.deepDependencies.toSet }
    assert { d2_2.deepDependencies.indexOf(d4_1) < d2_2.deepDependencies.indexOf(d3_1) }
    assert { d2_2.deepDependencies.indexOf(d4_2) < d2_2.deepDependencies.indexOf(d3_3) }
    expect(List(d4_2, d3_3)) { d2_3.deepDependencies }
    expect(3) { d2_4.deepDependencies.size }
    expect(Set(d3_3, d3_4, d4_2)) { d2_4.deepDependencies.toSet }
    assert { d2_4.deepDependencies.indexOf(d4_2) < d2_4.deepDependencies.indexOf(d3_3) }

    val d1_1 = new D(d2_1, d2_2, d2_3)
    val d1_2 = new D(d2_4, d2_3)
    val d1_3 = new D()
    val d0_1 = new D(d1_2)
    expect(Set(d2_1, d2_2, d2_3)) { d1_1.dependencies }
    expect(Set(d2_4, d2_3)) { d1_2.dependencies }
    expect(Set.empty) { d1_3.dependencies }
    expect(Set(d1_2)) { d0_1.dependencies }
    expect(8) { d1_1.deepDependencies.size }
    expect(Set(d2_1, d2_2, d2_3, d3_1, d3_2, d3_3, d4_1, d4_2)) { d1_1.deepDependencies.toSet }
    assert { d1_1.deepDependencies.indexOf(d4_1) < d1_1.deepDependencies.indexOf(d3_1) }
    assert { d1_1.deepDependencies.indexOf(d4_1) < d1_1.deepDependencies.indexOf(d2_1) }
    assert { d1_1.deepDependencies.indexOf(d4_1) < d1_1.deepDependencies.indexOf(d2_2) }
    assert { d1_1.deepDependencies.indexOf(d4_2) < d1_1.deepDependencies.indexOf(d3_3) }
    assert { d1_1.deepDependencies.indexOf(d4_2) < d1_1.deepDependencies.indexOf(d2_2) }
    assert { d1_1.deepDependencies.indexOf(d4_2) < d1_1.deepDependencies.indexOf(d2_3) }
    assert { d1_1.deepDependencies.indexOf(d3_1) < d1_1.deepDependencies.indexOf(d2_1) }
    assert { d1_1.deepDependencies.indexOf(d3_1) < d1_1.deepDependencies.indexOf(d2_2) }
    assert { d1_1.deepDependencies.indexOf(d3_2) < d1_1.deepDependencies.indexOf(d2_2) }
    assert { d1_1.deepDependencies.indexOf(d3_3) < d1_1.deepDependencies.indexOf(d2_2) }
    assert { d1_1.deepDependencies.indexOf(d3_3) < d1_1.deepDependencies.indexOf(d2_3) }
    expect(5) { d1_2.deepDependencies.size }
    expect(Set(d2_3, d2_4, d3_3, d3_4, d4_2)) { d1_2.deepDependencies.toSet }
    assert { d1_2.deepDependencies.indexOf(d4_2) < d1_2.deepDependencies.indexOf(d3_3) }
    assert { d1_2.deepDependencies.indexOf(d4_2) < d1_2.deepDependencies.indexOf(d2_3) }
    assert { d1_2.deepDependencies.indexOf(d4_2) < d1_2.deepDependencies.indexOf(d2_4) }
    assert { d1_2.deepDependencies.indexOf(d3_3) < d1_2.deepDependencies.indexOf(d2_3) }
    assert { d1_2.deepDependencies.indexOf(d3_3) < d1_2.deepDependencies.indexOf(d2_4) }
    assert { d1_2.deepDependencies.indexOf(d3_4) < d1_2.deepDependencies.indexOf(d2_4) }
    expect(Nil) { d1_3.deepDependencies }
    expect(6) { d0_1.deepDependencies.size }
    expect(Set(d1_2, d2_3, d2_4, d3_3, d3_4, d4_2)) { d0_1.deepDependencies.toSet }
    assert { d0_1.deepDependencies.indexOf(d4_2) < d0_1.deepDependencies.indexOf(d3_3) }
    assert { d0_1.deepDependencies.indexOf(d4_2) < d0_1.deepDependencies.indexOf(d2_3) }
    assert { d0_1.deepDependencies.indexOf(d4_2) < d0_1.deepDependencies.indexOf(d2_4) }
    assert { d0_1.deepDependencies.indexOf(d4_2) < d0_1.deepDependencies.indexOf(d1_2) }
    assert { d0_1.deepDependencies.indexOf(d3_3) < d0_1.deepDependencies.indexOf(d2_3) }
    assert { d0_1.deepDependencies.indexOf(d3_3) < d0_1.deepDependencies.indexOf(d2_4) }
    assert { d0_1.deepDependencies.indexOf(d3_3) < d0_1.deepDependencies.indexOf(d1_2) }
    assert { d0_1.deepDependencies.indexOf(d3_4) < d0_1.deepDependencies.indexOf(d2_4) }
    assert { d0_1.deepDependencies.indexOf(d3_4) < d0_1.deepDependencies.indexOf(d1_2) }
    assert { d0_1.deepDependencies.indexOf(d2_3) < d0_1.deepDependencies.indexOf(d1_2) }
    assert { d0_1.deepDependencies.indexOf(d2_4) < d0_1.deepDependencies.indexOf(d1_2) }
  }

  test("Method dependsOn") {
    val d1 = new D()
    val d2 = new D(d1)
    val d3 = new D(d2)
    val d4 = new D(d2, d1)

    assert { !(d1 dependsOn d2) }
    assert { !(d1 dependsOn d3) }
    assert { !(d1 dependsOn d4) }
    assert { d2 dependsOn d1 }
    assert { !(d2 dependsOn d3) }
    assert { !(d2 dependsOn d4) }
    assert { d3 dependsOn d1 }
    assert { d3 dependsOn d2 }
    assert { !(d3 dependsOn d4) }
    assert { d4 dependsOn d1 }
    assert { d4 dependsOn d2 }
    assert { !(d4 dependsOn d3) }
  }

  test("Method isTopLevel") {
    val l0 = new D()
    val l1 = new D()
    val l2 = new D(l1)
    val l3 = new D(l2)
    val l4 = new D(l1, l2)

    assert { !(l0 hasOnTopLevel l1) }
    assert { !(l1 hasOnTopLevel l2) }
    assert { !(l2 hasOnTopLevel l0) }
    assert { l2 hasOnTopLevel l1 }
    assert { !(l2 hasOnTopLevel l3) }
    assert { !(l3 hasOnTopLevel l1) }
    assert { l3 hasOnTopLevel l2 }
    assert { !(l4 hasOnTopLevel l1) }
    assert { l4 hasOnTopLevel l2 }
  }

  test("Depending on self") {
    class SelfDep extends Dependant[SelfDep] { def dependencies = Set(this) }

    intercept[Exception] { new SelfDep deepDependencies }
  }

  test("Empty dependencies") {
    class D extends Dependant[D] {
      def dependencies = Empty
    }

    val d = new D
    expect(Set()) { d.dependencies }
  }

  test("Circular dependencies") {
    class Dep(deps: Dep*) extends Dependant[Dep] { def dependencies = deps.toSet }
    class Dep1 extends Dep { override def dependencies = Set(new Dep(this)) }
    class Dep2 extends Dep { override def dependencies = Set(new Dep(new Dep(this))) }
    class Dep3 extends Dep { override def dependencies = Set(new Dep1) }
    class Dep4 extends Dep { override def dependencies = Set(new Dep3) }
    class Dep5 extends Dep { override def dependencies = Set(new Dep(), new Dep3) }
    class Dep6 extends Dep { override def dependencies = Set(new Dep(this), new Dep3) }
    class DepOk extends Dep { override def dependencies = Set(new Dep(), new Dep(new Dep())) }

    assert { (new Dep1) hasCycles() }
    assert { (new Dep2) hasCycles() }
    assert { (new Dep3) hasCycles() }
    assert { (new Dep4) hasCycles() }
    assert { (new Dep5) hasCycles() }
    assert { (new Dep6) hasCycles() }
    assert { !((new DepOk) hasCycles()) }
    intercept[Exception] { new Dep1 deepDependencies }
    intercept[Exception] { new Dep2 deepDependencies }
    intercept[Exception] { new Dep3 deepDependencies }
    intercept[Exception] { new Dep4 deepDependencies }
    intercept[Exception] { new Dep5 deepDependencies }
    intercept[Exception] { new Dep6 deepDependencies }
    new DepOk deepDependencies match {
      case List(_: Dep, _: Dep, _: Dep) => // Ok
      case _ => fail
    }
  }
}
