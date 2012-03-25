package lt.norma.crossbow.indicators

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

/**Apply specified transformation function to multiple target indicators of the same type. */
class TransformationN[TargetValue, Value]
(targets: Indicator[TargetValue]*)
(transformation: (Option[TargetValue] *) => Option[Value])
  extends FunctionalIndicator[Value] {
  def name = "Transformation(" + targets.map(_.name).mkString(";") + ")"

  def dependencies = targets.toSet

  def calculate = transformation(targets.map(_()): _*)
}

/**Apply specified transformation function to the target indicator. */
class Transformation[TargetValue, Value](target: Indicator[TargetValue])
                                        (transformation: Option[TargetValue] => Option[Value])
  extends TransformationN[TargetValue, Value](target)({
    case Seq(v) => transformation(v)
  }) {
}

/**Apply specified transformation function to two target indicators. */
class Transformation2[TargetValue1, TargetValue2, Value]
(target1: Indicator[TargetValue1], target2: Indicator[TargetValue2])
(transformation: (Option[TargetValue1], Option[TargetValue2]) => Option[Value])
  extends FunctionalIndicator[Value] {
  def name = "Transformation(" + target1.name + ";" + target2.name + ")"

  def dependencies = Set(target1, target2)

  def calculate = transformation(target1(), target2())
}

/**Apply specified transformation function to three target indicators. */
class Transformation3[TargetValue1, TargetValue2, TargetValue3, Value]
(target1: Indicator[TargetValue1], target2: Indicator[TargetValue2],
 target3: Indicator[TargetValue3])
(transformation: (Option[TargetValue1], Option[TargetValue2], Option[TargetValue3]) =>
  Option[Value])
  extends FunctionalIndicator[Value] {
  def name = "Transformation(" + target1.name + ";" + target2.name + ";" + target3.name + ")"

  def dependencies = Set(target1, target2, target3)

  def calculate = transformation(target1(), target2(), target3())
}

