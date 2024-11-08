package activitygo

trait GoPlayUtils:

  def b(x: Int, y: Int): BlackPlay = BlackPlay(IntersectionPoint(x, y))
  def w(x: Int, y: Int): WhitePlay = WhitePlay(IntersectionPoint(x, y))

  extension (bp: BlackPlay)
    def reverse: WhitePlay = WhitePlay(bp.intersection)

  extension (wp: WhitePlay)
    def reverse: BlackPlay = BlackPlay(wp.intersection)

