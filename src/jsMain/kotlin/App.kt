import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import vitorscoelho.JSCanvasController
import vitorscoelho.gyncanvas.core.CanvasController
import vitorscoelho.gyncanvas.core.event.CanvasMouseButton
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D
import vitorscoelho.gyncanvas.testes.desenhar
import vitorscoelho.gyncanvas.webgl.OrthographicCamera
import vitorscoelho.gyncanvas.webgl.WebGLStaticDrawer
import vitorscoelho.gyncanvas.webgl.primitives.COLOR_BLACK
import vitorscoelho.gyncanvas.webgl.primitives.COLOR_RED
import vitorscoelho.gyncanvas.webgl.primitives.COLOR_WHITE
import vitorscoelho.gyncanvas.webgl.primitives.Line2D

fun main() {
    document.body!!.append.div {
        h1 {
            +"Welcome to Kotlin/JS!"
        }
        p {
            +"Fancy joining this year's "
            a("https://kotlinconf.com/") {
                +"KotlinConf"
            }
            +"?"
        }
        canvas {
            id = "canvasTeste"
            width = "600px"
            height = "400px"
        }
    }
    val canvas = document.getElementById("canvasTeste") as HTMLCanvasElement
//    val context = canvas.getContext("2d") as CanvasRenderingContext2D
//    context.fillStyle = "blue"
//    context.fillRect(50.0, 80.0, 50.0, 80.0)
//    canvas.addEventListener(type = "click", callback = { ev ->
//        context.fillStyle = "red"
//        context.fillRect(50.0, 80.0, 50.0, 80.0)
//        console.log(ev)
//    })
//    canvas.addEventListener(type = "wheel", callback = { ev ->
//        ev.preventDefault()
//        context.fillStyle = "green"
//        context.fillRect(200.0,200.0,100.0,40.0)
//    })
//    val controller = JSCanvasController(canvas = canvas)
//    desenhar(controller = controller)
//    desenharTriangulo(canvas)
    testar(canvas)
}