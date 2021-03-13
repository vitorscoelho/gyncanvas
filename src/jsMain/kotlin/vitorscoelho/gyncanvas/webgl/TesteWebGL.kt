package vitorscoelho.gyncanvas.webgl

import org.w3c.dom.HTMLCanvasElement
import vitorscoelho.gyncanvas.core.EventManager
import vitorscoelho.gyncanvas.core.event.CanvasMouseButton
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.core.primitives.Color
import vitorscoelho.gyncanvas.core.primitives.Line
import vitorscoelho.gyncanvas.core.primitives.Triangle
import vitorscoelho.gyncanvas.math.Vector2D

val COLOR_BLACK = object : Color {
    override val red = 0f
    override val green = 0f
    override val blue = 0f
}
val COLOR_WHITE = object : Color {
    override val red = 1f
    override val green = 1f
    override val blue = 1f
}
val COLOR_RED = object : Color {
    override val red = 1f
    override val green = 0f
    override val blue = 0f
}
val COLOR_GREEN = object : Color {
    override val red = 0f
    override val green = 1f
    override val blue = 0f
}
val COLOR_BLUE = object : Color {
    override val red = 0f
    override val green = 0f
    override val blue = 1f
}

fun testar(canvas: HTMLCanvasElement) {
    val drawingArea = JSDrawingArea(canvas)
    val drawer = WebGLStaticDrawer2D(drawingArea)
    val camera = OrthographicCamera2D(drawingArea)
    val drawFunction = { drawer.draw(COLOR_BLACK, camera) }
    drawFunction()
    panClicked(drawingArea.listeners, camera, drawFunction)
    zoomScroll(drawingArea.listeners, 1.2f, camera, drawFunction)
}

private fun zoomWindow(listeners: EventManager, camera: OrthographicCamera2D, drawFunction: () -> Unit) {
    var started = false
    var x1: Float = 0f
    var y1: Float = 0f
    var handler: (CanvasMouseEvent) -> Unit = {}
    handler = { event: CanvasMouseEvent ->
        if (event.button == CanvasMouseButton.PRIMARY) {
            if (!started) {
                started = true
                x1 = camera.xWorld(event.x)
                y1 = camera.yWorld(event.y)
                println("x1: $x1 // y1: $y1")
            } else {
                started = false
                val x2 = camera.xWorld(event.x)
                val y2 = camera.yWorld(event.y)
                println("x2: $x2 // y2: $y2")
                camera.zoomWindow(x1 = x1, y1 = y1, x2 = x2, y2 = y2)
                drawFunction()
//                listeners.removeMouseClicked(handler)
            }
        }
    }
    listeners.addMouseClicked(handler)
}

private fun panClicked(listeners: EventManager, camera: OrthographicCamera2D, drawFunction: () -> Unit) {
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
            val deltaX = camera.worldDistance(event.x - xStartPan)
            val deltaY = camera.worldDistance(yStartPan - event.y)
            camera.translate(tx = -deltaX, ty = -deltaY)
            xStartPan = event.x
            yStartPan = event.y
            drawFunction()
        }
    }
}

private fun zoomScroll(
    listeners: EventManager,
    zoomFactor: Float,
    camera: OrthographicCamera2D,
    drawFunction: () -> Unit
) {
    require(zoomFactor > 0.0) { "|zoomFactor| must be greater than zero" }
    listeners.addScroll { event ->
        val factor: Float = when {
            event.deltaY > 0f -> zoomFactor
            event.deltaY < 0f -> 1f / zoomFactor
            else -> 0f
        }
        if (factor != 0f) {
            camera.appendZoom(
                pivotX = camera.xWorld(event.x),
                pivotY = camera.yWorld(event.y),
                zoomFactor = factor
            )
            drawFunction()
        }
    }
}