/*
 * Copyright 2011 Vilius Normantas <code@norma.lt>
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

/** Stores list of indicators and their dependencies. Only top level items have to be provided,
  * their dependencies in full depth will be automatically added to the deep list.
  *
  * Received data is forwarded to all indicators in the deep list. Dependencies are ensured to be
  * updated before the dependents as they appear higher in the deep list. */
class IndicatorList(indicators: Indicator[_]*) extends DataListener {
  private val root = new Dependant[Indicator[_]] {
    def dependencies = indicators.toSet
  }

  /** Contents of the list. */
  lazy val list = indicators.toList.distinct filter { root.shallowDependencies contains }

  /** Contents of the list including dependencies in full depth. */
  lazy val deepList = root.deepDependencies

  /** `IndicatorList` updates contained indicators by itself. Therefore there is no need to
    * depend on them. */
  def dependencies = Set.empty

  def receive = {
    case barClose: BarClose => updateIndicators(barClose); updateHistory(barClose)
    case d: Data => updateIndicators(d)
  }

  private def updateIndicators(data: Data) { deepList foreach { _.send(data) } }

  private def updateHistory(barClose: BarClose) {
    deepList foreach {
      case ih: History => ih.history.update()
      case _ =>
    }
  }
}
