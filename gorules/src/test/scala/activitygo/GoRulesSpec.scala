package activitygo

import org.specs2.mutable.Specification

import scala.util.Random

class GoRulesSpec extends Specification with GoPlayUtils {

  private lazy val r = new Random()


  "Finding a group" should {
    "successfully find a group of two horizontal stones" in {
      val state = GoState.fromString(
        """··········
          |··········
          |··········
          |····x·····
          |··········
          |··········
          |··········
          |··········
          |··········
          |··········""".stripMargin
      )

      val (_, group) = GoRules.findGroup(state, BlackPlay(IntersectionPoint(3, 3)))

      group.length must be_==(2)
      group must contain(BlackPlay(IntersectionPoint(3, 3)), BlackPlay(IntersectionPoint(4, 3)))
    }
    "successfully find a group of two vertical stones" in {
      val state = GoState.fromString(
        """··········
          |··········
          |··········
          |····x·····
          |··········
          |··········
          |··········
          |··········
          |··········
          |··········""".stripMargin
      )

      val (_, group) = GoRules.findGroup(state, BlackPlay(IntersectionPoint(4, 4)))

      group.length must be_==(2)
      group must contain(BlackPlay(IntersectionPoint(4, 4)), BlackPlay(IntersectionPoint(4, 3)))
    }
    "successfully find a group covering multiple columns and rows" in {
      val state = GoState.fromString(
        """··········
          |··xox·····
          |·xoo··ooo·
          |··xo··o···
          |···ooo····
          |·····ooooo
          |·····o····
          |····o·o···
          |··········
          |··········""".stripMargin, BlackPlay(IntersectionPoint(2, 1)) :: Nil
      )
      val (_, group) = GoRules.findGroup(state, WhitePlay(IntersectionPoint(4, 3)))
      group.length must be_==(14)
    }
  }

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

    "return true when attempting group suicide" in {
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

      val state = GoState.fromString(board, BlackPlay(IntersectionPoint(1, 0)) :: Nil)

      println(state.toString)

      GoRules.isSuicide(state, WhitePlay(IntersectionPoint(2, 2))) must beTrue
    }

    "return false when not quite attempting group suicide because there is a liberty" in {
      val board =
        """·xxxxx····
          |xooooox···
          |xo·ooo·x··
          |xooooox···
          |·xxxxx····
          |··········
          |··········
          |··········
          |··········
          |··········""".stripMargin

      val state = GoState.fromString(board, BlackPlay(IntersectionPoint(1, 0)) :: Nil)

      println(state.toString)

      GoRules.isSuicide(state, WhitePlay(IntersectionPoint(2, 2))) must beFalse
    }

    "return false when a stone would be in a captured state" in {
      // this kind of the same as above, but for a single stone
      ko
    }
  }
}
