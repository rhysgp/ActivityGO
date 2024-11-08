package activitygo

import org.specs2.mutable.Specification

import scala.util.Random

class GoRulesSpec extends Specification with GoPlayUtils {

  private lazy val r = new Random()

  "Checking for invalid moves" should {

    "return true when placing the first black stone on the board" in {
      GoRules.isValid(GoState.initialise(19), b(r.nextInt(19), r.nextInt(19))) must beTrue
    }

    "return false when white is playing and it is black's turn" in {
      GoRules.isValid(GoState.initialise(19), w(r.nextInt(19), r.nextInt(19))) must beFalse
    }

    "return false when black is playing and it is white's turn" in {
      val game1 = GoRules.applyPlay(GoState.initialise(19), b(10, 10))
      GoRules.isValid(game1, b(11, 10)) must beFalse
    }

    "return false when trying to place a stone where there is already a stone" in {
      val play = b(10, 10)
      val game1 = GoRules.applyPlay(GoState.initialise(19), play)
      GoRules.isValid(game1, play) must beFalse
      GoRules.isValid(game1, play.reverse) must beFalse
    }

    "return false when immediately recapturing a ko" in {
      ko
    }

    "return false when attempting group suicide" in {
      val board =
        """·xxxxx····
          |xooooox···
          |xo·ooox···
          |xooooox···
          |·xxxxx····
          |··········
          |··········
          |··········
          |··········
          |··········""".stripMargin

      val state = GoState.fromString(board)

      GoRules.isSuicide(state, WhitePlay(IntersectionPoint(2, 2))) must beTrue
    }

    "return false when a stone would be in a captured state" in {
      // this kind of the same as above, but for a single stone
      ko
    }
  }
}
