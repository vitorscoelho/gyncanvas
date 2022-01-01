import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.HTMLCanvasElement
import vitorscoelho.gyncanvas.webgl.testar

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
            width = "1800px"
            height = "750px"
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