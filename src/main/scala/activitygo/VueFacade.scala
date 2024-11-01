package activitygo

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native @JSImport("@/App.vue", JSImport.Default)
object App extends js.Object

@js.native @JSImport("@/router", JSImport.Default)
object Router extends js.Object

@js.native @JSImport("vue", "createApp")
object createApp extends js.Function1[js.Any, VueApp] {
  def apply(component: js.Any): VueApp = js.native
}

@js.native
trait VueApp extends js.Object:
  def mount(selector: String): VueApp = js.native
  def use(plugin: Any): VueApp = js.native


trait Pinia extends js.Object:
end Pinia


@js.native @JSImport("pinia", "createPinia")
object createPinia extends js.Function0[Pinia] {
  def apply(): Pinia = js.native
}
