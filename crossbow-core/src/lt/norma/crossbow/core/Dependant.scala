package lt.norma.crossbow.core

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

/**Represents any instance capable of having dependencies of the specified type `A`. Dependencies
 * are used mostly to ensure that some instances (the dependencies) receive data messages before
 * others (the dependants). */
trait Dependant[A <: Dependant[A]] {
  /**Implicitly convert `Empty` object to an empty set. */
  implicit def emptyToDependencies(empty: Empty): Set[A] = Set.empty

  /**Instances used by or relied on by this `Dependant`. */
  protected def dependencies: Set[A]

  /**List of dependencies. Every instance appears only once. */
  lazy val shallowDependencies: List[A] = {
    if (hasCycles()) throw new Exception("Circular dependency found.")
    dependencies.toList distinct
  }

  /**Flat list of all dependencies in full depth. It is guaranteed that the dependencies appear
   * before the dependants in the list, except that the order is not defined and should not be
   * relied on. Every instance appears only once. */
  lazy val deepDependencies: List[A] =
    (shallowDependencies map {
      d => d.deepDependencies ::: List(d)
    } flatten) distinct

  /**Checks if this dependant has the specified item in it's dependencies at any depth. */
  def dependsOn(item: A): Boolean = deepDependencies contains item

  /**Checks if the specified item is a top level dependency of this item. */
  def hasOnTopLevel(item: A): Boolean = {
    (shallowDependencies contains item) &&
      !(shallowDependencies exists {
        _ dependsOn item
      })
  }

  /**Checks for cycles in dependency graph. */
  def hasCycles(seen: List[Dependant[A]] = List(this)): Boolean = {
    dependencies exists {
      d => (seen contains d) || d.hasCycles(d :: seen)
    }
  }
}
