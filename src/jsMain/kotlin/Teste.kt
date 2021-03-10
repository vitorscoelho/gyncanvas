import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import vitorscoelho.gyncanvas.core.EventManager
import vitorscoelho.gyncanvas.core.event.CanvasMouseButton
import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D
import vitorscoelho.gyncanvas.webgl.JSDrawingArea
import vitorscoelho.gyncanvas.webgl.OrthographicCamera
import vitorscoelho.gyncanvas.webgl.WebGLStaticDrawer
import vitorscoelho.gyncanvas.webgl.primitives.COLOR_BLACK
import vitorscoelho.gyncanvas.webgl.primitives.COLOR_RED
import vitorscoelho.gyncanvas.webgl.primitives.COLOR_WHITE
import vitorscoelho.gyncanvas.webgl.primitives.Line2D

fun testar(canvas: HTMLCanvasElement) {
    val drawingArea = JSDrawingArea(canvas)
    val drawer = WebGLStaticDrawer(drawingArea)
    drawer.setElements(
        listOf(
            Line2D(
                startPoint = Vector2D(-1.0, -1.0),
                endPoint = Vector2D(1.0, 1.0),
                color = COLOR_WHITE
            ),
            Line2D(
                startPoint = Vector2D(-1.0, 1.0),
                endPoint = Vector2D(1.0, -1.0),
                color = COLOR_RED
            ),
            Line2D(
                startPoint = Vector2D(-1.0, 0.0),
                endPoint = Vector2D(1.0, 0.0),
                color = COLOR_RED
            ),
            Line2D(
                startPoint = Vector2D(0.0, -1.0),
                endPoint = Vector2D(0.0, 1.0),
                color = COLOR_WHITE
            )
        )
    )
    val camera = OrthographicCamera(drawingArea)
    camera.setPosition(xCenter = -1f, yCenter = 0f, zoom = 200f)
    println("Zoom=${camera.zoom}")
//    camera.set(-1.5f,1.5f,-1f,1f,-1f,1f)
    drawer.draw(COLOR_BLACK, camera)
//    drawingArea.listeners.addMouseMoved { event ->
//        val xWorld = camera.xWorld(event.x)
//        val yWorld = camera.yWorld(event.y)
//        println("Canvas: x=${event.x} // y=${event.y}  //// World: x=$xWorld // y=$yWorld")
//    }
//    panClicked(drawingArea.listeners, camera) { drawer.draw(COLOR_BLACK, camera) }
//    drawingArea.listeners.addMouseClicked { event ->
//        if (event.button == CanvasMouseButton.PRIMARY) {
//            camera.setPosition(xCenter = camera.xWorld(event.x), yCenter = camera.yWorld(event.y), zoom = camera.zoom)
//            println("Camera: ${camera.xWorld(event.x)}")
//            drawer.draw(COLOR_BLACK, camera)
//        }
//    }
    canvas.addEventListener("mousemove", { event ->
        event as MouseEvent
        println("x: ${event.offsetX} /// y: ${event.offsetY}")
    })
}

private fun panClicked(listeners: EventManager, camera: OrthographicCamera, drawFunction: () -> Unit) {
    var xStartPan: Float = 0f
    var yStartPan: Float = 0f
    var started: Boolean = false

    listeners.addMouseClicked { event ->
        if (event.button == CanvasMouseButton.MIDDLE) {
            started = !started
            if (started) {
                xStartPan = event.x
                yStartPan = event.y
            }
        }
    }
    listeners.addMouseMoved { event ->
        if (started) {
            val deltaX = (event.x - xStartPan) / camera.zoom
            val deltaY = -(event.y - yStartPan) / camera.zoom
            camera.setPosition(xCenter = camera.xCenter - deltaX, yCenter = camera.yCenter - deltaY, zoom = camera.zoom)
            xStartPan = event.x
            yStartPan = event.y
            drawFunction()
//            println(deltaX)
        }
    }
}