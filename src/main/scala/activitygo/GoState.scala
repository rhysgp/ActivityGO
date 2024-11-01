package activitygo

import scala.annotation.targetName

/*
 * Represents the current state of a board, including captured stones and history.
 */

case class GoState(
  board: Array[Array[IntersectionState]],
  captures: (CountOfBlack, CountOfWhite),
  plays: Seq[Play]
)

object GoState:
  def initialise(size: Int): GoState =
    GoState(
      Array.fill(size)(Array.fill(size)(IntersectionState.Empty)),
      (CountOfBlack(0), CountOfWhite(0)),
      Seq.empty
    )
end GoState

enum IntersectionState:
   case Empty, Black, White

opaque type CountOfBlack = Int
object CountOfBlack:
  def apply(i: Int): CountOfBlack = i
  extension (x: CountOfBlack)
    @targetName("plusBlack") def +(y: CountOfBlack): CountOfBlack = CountOfBlack(x + y)

opaque type CountOfWhite = Int
object CountOfWhite:
  def apply(i: Int): CountOfWhite = i
  extension (x: CountOfWhite)
    @targetName("plusWhite") def +(y: CountOfWhite): CountOfWhite = CountOfWhite(x + y)

trait Play
case class BlackPlay(x: Int, y: Int) extends Play
case class WhitePlay(x: Int, y: Int) extends Play
case object BlackPass extends Play
case object WhitePass extends Play
