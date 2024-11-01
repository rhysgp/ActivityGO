package activitygo

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

@main
def LiveChart(): Unit =
  println("HERE I AM IN SCALA!!")

//  val app = createApp(App)
//  app.mount("#app")
//  app.use(createPinia())
//  app.use(Router)

  dom.document.querySelector("#app").innerHTML = s"""
    <div>
      <a href="https://vitejs.dev" target="_blank">
        <img src="/vite.svg" class="logo" alt="Vite logo" />
      </a>
      <h1>Hello Scala.js!</h1>
      <div class="card">
        <button id="counter" type="button"></button>
      </div>
      <p class="read-the-docs">
        Click on the Vite logo to learn more
      </p>
    </div>
  """

end LiveChart
