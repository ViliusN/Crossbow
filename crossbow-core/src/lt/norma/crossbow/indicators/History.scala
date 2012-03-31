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

/** Mixin this trait at declaration or creation of concrete indicators to enable collection of
  * historical values.
  *
  * === Examples ===
  *
  * Normally, trait `History` is mixed in at creation of particular indicator:
  * {{{
  *   val i = new MyIndicator with History
  *   assert(i.hasHistory)
  * }}}
  * If the indicator uses historical values internally, `History` can be mixed in at the declaration
  * of indicator's class:
  * {{{
  *   class MyIndicator extends Indicator[Double] with History { ... }
  *   i = new MyIndicator
  *   assert(i.hasHistory)
  * }}}
  * Indicators without `History` trait either at declaration or at creation do not collect
  * historical values:
  * {{{
  *   val i = new MyIndicator
  *   assert(i.hasHistory == false)
  * }}} */
trait History
