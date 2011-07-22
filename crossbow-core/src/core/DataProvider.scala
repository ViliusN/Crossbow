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

/** Manages list of data listeners and dispatches data messages to them. Guarantees that
  * dependencies receive messages before the listener depending on them. Otherwise the order of in
  * which the listeners get updated is undefined and should not be relied on. */
trait DataProvider {
  class RootListener(deps: DataListener*) extends Dependant[DataListener] {
    def dependencies = deps.toSet
  }

  private var root = new RootListener

  /** Sends out data messages to the listeners. */
  def dispatch(data: Data) { root.deepDependencies foreach { _ send data } }

  /** Adds the specified listener. */
  def add(listener: DataListener) {
    root = new RootListener((listener :: root.shallowDependencies) :_*)
  }

  /** Removes the specified listener. Throws an exception if other listeners depend on the
    * removed listener. */
  def remove(listener: DataListener) {
    if(root dependsOn listener) {
      if(root hasOnTopLevel listener) {
        root = new RootListener((root.shallowDependencies.filterNot(_ == listener)) :_*)
      } else {
        throw new Exception("Unable to remove listener as other listeners depend on it.")
      }
    }
  }
}

/** Performs instrument look-up requests. */
trait InstrumentProvider {
  def requestOptionsChain(underlying: Instrument)
  // TODO return instruments via data messages
}
