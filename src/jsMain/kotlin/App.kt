import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.RenderingContext
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
    val drawingArea = JSDrawingArea(canvas = canvas)
    desenhar(drawingArea = drawingArea)
}