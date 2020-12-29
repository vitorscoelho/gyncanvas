import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.RenderingContext
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import vitorscoelho.JSDrawingArea
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.testes.desenhar

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
            width = "1300px"
            height = "600px"
        }
    }
    val canvas = document.getElementById("canvasTeste") as HTMLCanvasElement
//    val context = canvas.getContext("2d") as CanvasRenderingContext2D
//    context.fillStyle = "blue"
//    context.fillRect(50.0, 80.0, 50.0, 80.0)
//    canvas.addEventListener(type = "click", callback = {ev->
//        context.fillStyle="red"
//        context.fillRect(50.0,80.0,50.0,80.0)
//        console.log(ev)
//    })
    val drawingArea = JSDrawingArea(canvas = canvas)
    desenhar(drawingArea = drawingArea)
}