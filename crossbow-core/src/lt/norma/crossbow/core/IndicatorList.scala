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

import lt.norma.crossbow.messages.{ BarClose, Message }
import lt.norma.crossbow.indicators.{ Indicator, History }

/** Stores list of indicators and their dependencies. Only top level items have to be provided,
  * their dependencies in full depth will be automatically added to the deep list.
  *
  * Received data is forwarded to all indicators in the deep list. Dependencies are ensured to be
  * updated before the dependents as they appear higher in the deep list. */
class IndicatorList(indicators: Indicator[_]*) extends Listener {
  /**`IndicatorList` updates contained indicators by itself. Therefore there is no need to
    * depend on them. */
  def dependencies = Empty

  /** Root dependant to hold all indicators of this list. */
  private val root = new Dependant[Indicator[_]] {
    def dependencies = indicators.toSet
  }

  /** Shallow contents of the list. Includes only indicators, explicitly added to the list via
    * constructor, excluding any dependencies. Duplicate entries are filtered out. For complete
    * (deep) list of indicators use `deep`. */
  lazy val shallow: List[Indicator[_]] =
    indicators.toList.distinct.filter(root.shallowDependencies.contains)

  /** Contents of the list including dependencies in full depth. */
  lazy val deep: List[Indicator[_]] = root.deepDependencies

  /** Indicators of the deep list, which collect historical values. */
  lazy val deepWithHistory: List[Indicator[_] with History] =
    deep.collect {
      case ih: Indicator[_] with History => ih
    }

  /** Indicators of the deep list, which listen to data messages. */
  lazy val deepWithBasicListener: List[Indicator[_] with BasicListener] =
    deep.collect {
      case il: Indicator[_] with BasicListener => il
    }

  /** Forwards all messages to the indicators in `deep` list. On `BarClose` message, indicators'
    * histories are updated. Historical values are collected '''after''' dispatching `BarClose`
    * messages, so that indicators could update their values, before historical value is
    * recorded. */
  def receive = {
    case bc: BarClose =>
      dispatchMessage(bc)
      deepWithHistory.foreach(_.history.update())
    case m =>
      dispatchMessage(m)
  }

  /** Forwards the specified message to al indicators in `deep` list. */
  private def dispatchMessage(message: Message) {
    deepWithBasicListener.foreach(_.send(message))
  }

  /** Finds largest required history of all indicators. */
  def maxRequiredHistory: Int = (0 :: deep.map(_.requiredHistory)).max

  /** Truncates history of all indicators to the amount specified by `leave` or the result of
    * `maxRequiredHistory`, whichever is bigger. */
  def truncateHistory(leave: Int) {
    deepWithHistory.foreach(_.history.truncate(math.max(leave, maxRequiredHistory)))
  }

  // Send IndicatorCreated message to all indicators on creation of the list.
  dispatchMessage(IndicatorList.IndicatorCreated)
}

object IndicatorList {
  /** Message sent after creation of indicator list. */
  case object IndicatorCreated extends Message
}
