package activitygo

import scala.annotation.targetName

/*
 * Represents the current state of a board, including captured stones and history.
 */

case class GoState(
  board: Array[Array[IntersectionState]],
  captures: (CountOfBlack, CountOfWhite),
  plays: Seq[Play]
):

  def play(p: Play): GoState = {
    if GoRules.isValid(this, p) then
      GoRules.applyPlay(this, p)
    else throw new InvalidPlayException()
  }

  def at(intersectionPoint: IntersectionPoint): IntersectionState =
    board(intersectionPoint.y)(intersectionPoint.x)
  end at

  override def toString: String =
    import IntersectionState.{Black, White, Empty}
    val sb = new StringBuilder()
    board.foreach(lines => {
      lines.foreach {
        case Empty => sb.append('·')
        case Black => sb.append('x')
        case White => sb.append('o')
        case _ => sb.append('?')
      }
      sb.append('\n')
    })
    sb.toString()
  end toString

end GoState


object GoState:
  def initialise(size: Int): GoState =
    GoState(
      Array.fill(size)(Array.fill(size)(IntersectionState.Empty)),
      (CountOfBlack(0), CountOfWhite(0)),
      Seq.empty
    )

  def fromString(s: String, plays: List[Play] = Nil): GoState =
    val lines = s.split("\n").toList
    val size = lines.length

    if (!lines.forall(_.length == size)) throw new Exception("Board is not complete")
    if (!lines.forall(line => line.forall(ch => ch == '·' || ch == 'o' || ch == 'x'))) throw new Exception("Invalid character(s) in board")
    val state =
      lines.map( line =>
        line.map {
          case 'x' => IntersectionState.Black
          case 'o' => IntersectionState.White
          case '·' => IntersectionState.Empty
          case c => throw new Exception(s"Invalid character: $c")
        }.toArray
      ).toArray

    GoState(state, (CountOfBlack(0), CountOfWhite(0)), plays)
  end fromString

end GoState

enum IntersectionState:
   case Empty, Black, White, OutsideBoard

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

case class IntersectionPoint(x: Int, y: Int):
  def top: IntersectionPoint = IntersectionPoint(x, y - 1)
  def right: IntersectionPoint = IntersectionPoint(x + 1, y)
  def bottom: IntersectionPoint = IntersectionPoint(x, y + 1)
  def left: IntersectionPoint = IntersectionPoint(x - 1, y)

trait Play:
  def toIntersectionState: IntersectionState
  def isPass: Boolean

trait StonePlay extends Play:
  def intersection: IntersectionPoint

object StonePlay:
  def from(point: IntersectionPoint, is: IntersectionState): Option[StonePlay] =
    import IntersectionState.{White, Black}
    is match
      case Black => Some(BlackPlay(point))
      case White => Some(WhitePlay(point))
      case _     => None
    end match
  end from
end StonePlay

case class BlackPlay(intersection: IntersectionPoint) extends StonePlay:
  def toIntersectionState: IntersectionState = IntersectionState.Black
  def isPass: Boolean = false

case class WhitePlay(intersection: IntersectionPoint) extends StonePlay:
  def toIntersectionState: IntersectionState = IntersectionState.White
  def isPass: Boolean = false

case object BlackPass extends Play:
  def toIntersectionState: IntersectionState = IntersectionState.White
  def isPass: Boolean = true

case object WhitePass extends Play:
  def toIntersectionState: IntersectionState = IntersectionState.White
  def isPass: Boolean = true

