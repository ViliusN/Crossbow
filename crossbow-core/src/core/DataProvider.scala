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

import lt.norma.crossbow.core.messages._

/** Manages list of data listeners and dispatches data messages to them. Guarantees that
  * dependencies receive messages before the listener depending on them. Otherwise the order in
  * which the listeners get updated is undefined and should not be relied on. */
trait DataProvider {
  class RootListener(deps: Listener*) extends Dependant[Listener] {
    def dependencies = deps.toSet
  }

  private var root = new RootListener

  /** Deep list of data listeners served by this provider. */
  def listeners = root.deepDependencies

  /** Sends out data messages to the listeners. */
  def dispatch(message: Message) { listeners foreach { _ send message } }

  /** Adds the specified listeners. */
  def add(listeners: Listener*) {
    root = new RootListener((listeners.toList ::: root.shallowDependencies) :_*)
  }

  /** Removes the specified listeners. Listeners will still remain in deep list if other listeners
    * depend on them. */
  def remove(listeners: Listener*) {
    root = new RootListener((root.shallowDependencies.filterNot(l => listeners.contains(l))) :_*)
  }
}
