package activitygo

import activitygo.IntersectionState.{Black, Empty, OutsideBoard, White}

import scala.collection.mutable

object GoRules:

  def isValid(state: GoState, play: Play): Boolean = {
    isPlayersTurn(state, play)
      && isPassOrAtEmptyIntersection(state, play)
  }

  def applyPlay(state: GoState, play: Play): GoState = {
    if (isValid(state, play)) {
      play match {
        case BlackPlay(IntersectionPoint(x, y)) =>
          state.copy(
            board = mutate(state.board, x, y, IntersectionState.Black),
            plays = state.plays :+ play
          )
        case WhitePlay(IntersectionPoint(x, y)) =>
          state.copy(
            board = mutate(state.board, x, y, IntersectionState.White),
            plays = state.plays :+ play
          )
        case _ =>
          state.copy(
            plays = state.plays :+ play
          )
      }
    } else throw new InvalidPlayException()
  }

  private def mutate(board: Array[Array[IntersectionState]], x: Int, y: Int, is: IntersectionState): Array[Array[IntersectionState]] = {
    val newBoard = board.map(_.clone())
    newBoard(x)(y) = is
    newBoard
  }

  private def isPlayersTurn(state: GoState, play: Play): Boolean =
    state.plays.lastOption match {
      case Some(BlackPass) | Some(BlackPlay(_)) if play.toIntersectionState == White => true
      case None | Some(WhitePass) | Some(WhitePlay(_)) if play.toIntersectionState == Black => true
      case _ => false
    }

  private def isPassOrAtEmptyIntersection(state: GoState, play: Play): Boolean =
    play match {
      case BlackPass | WhitePass => true
      case BlackPlay(IntersectionPoint(x, y)) if isEmpty(state, x, y) => true
      case WhitePlay(IntersectionPoint(x, y)) if isEmpty(state, x, y) => true
      case _ => false
    }

  private def isEmpty(state: GoState, x: Int, y: Int) = state.at(IntersectionPoint(x, y)) == Empty


  /**
   * Returns the state of the four regions around the target intersection.
   * They are always returned as top, right, bottom, left.
   */
  private def adjacentStates(state: GoState, point: IntersectionPoint): AdjacentStates =
    val top = if point.y == 0 then OutsideBoard else state.at(point.copy(y = point.y - 1))
    val right = if point.x == state.board.length - 1 then OutsideBoard else state.at(point.copy(x = point.x + 1))
    val bottom = if point.y == state.board.length - 1 then OutsideBoard else state.at(point.copy(y = point.y + 1))
    val left = if point.x == 0 then OutsideBoard else state.at(point.copy(x = point.x - 1))
    AdjacentStates(IntersectionPoint(point.x, point.y), top, right, bottom, left)
  end adjacentStates


  /**
   * Find the group the play would be a part of. The play cannot be a pass.
   * That space must be empty.
   */
  def findGroup(state: GoState, play: StonePlay): (GoState, List[StonePlay]) =
    if play.isPass then throw IllegalArgumentException("The play cannot be a pass")
    if state.at(play.intersection) != Empty then throw IllegalArgumentException("The intersection must be empty")

    val newState = state.play(play)

    // collect the stones linked to that space that are of the same colour:
    val intersectionsInGroup = mutable.Set[StonePlay]()
    val toDo = mutable.Stack[StonePlay](play)
    while (toDo.nonEmpty)
      val p = toDo.pop()
      intersectionsInGroup.addOne(p)
      adjacentStates(newState, p.intersection)
        .toList
        .filter(_.toIntersectionState == play.toIntersectionState)
        .filterNot(p => intersectionsInGroup.contains(p))
        .foreach(toDo.addOne)
    end while

    (newState, intersectionsInGroup.toList)
  end findGroup



  /**
   * Performs an exhaustive search of intersections starting with the x/y coordinates.
   * If there are no adjacent spaces that are empty, that's suicide!
   */
  def isSuicide(state: GoState, play: StonePlay): Boolean =
    val (newState, intersectionsInGroup) = findGroup(state, play)
    !intersectionsInGroup
      .map(sp => adjacentStates(newState, sp.intersection))
      .exists(_.hasAdjacentEmptyIntersections)
  end isSuicide

end GoRules

class InvalidPlayException(msg: String) extends Exception(msg) {
  def this() = this("Invalid play")
}

case class AdjacentStates(centre: IntersectionPoint, top: IntersectionState, right: IntersectionState, bottom: IntersectionState, left: IntersectionState) {
  def toList: List[StonePlay] =
    List(
      StonePlay.from(centre.top, top),
      StonePlay.from(centre.right, right),
      StonePlay.from(centre.bottom, bottom),
      StonePlay.from(centre.left, left)
    ).collect { case Some(stonePlay) => stonePlay }

  def hasAdjacentEmptyIntersections: Boolean =
    top == Empty || right == Empty || bottom == Empty || left == Empty
}
