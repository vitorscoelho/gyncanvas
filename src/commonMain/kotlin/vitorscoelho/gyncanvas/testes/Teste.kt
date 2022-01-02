package vitorscoelho.gyncanvas.testes

import vitorscoelho.gyncanvas.DrawingArea
import vitorscoelho.gyncanvas.OrthographicCamera2D
import vitorscoelho.gyncanvas.core.EventManager
import vitorscoelho.gyncanvas.core.dxf.entities.Circle
import vitorscoelho.gyncanvas.core.dxf.entities.LwPolyline
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.event.CanvasMouseButton
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.core.primitives.*
import vitorscoelho.gyncanvas.math.Vector2D
import vitorscoelho.gyncanvas.math.toRadians
import kotlin.math.tan

val COLOR_BLACK = object : Color {
    override val red: Short = 0
    override val green: Short = 0
    override val blue: Short = 0
}
val COLOR_WHITE = object : Color {
    override val red: Short = 255
    override val green: Short = 255
    override val blue: Short = 255
}
val COLOR_RED = object : Color {
    override val red: Short = 255
    override val green: Short = 0
    override val blue: Short = 0
}
val COLOR_GREEN = object : Color {
    override val red: Short = 0
    override val green: Short = 255
    override val blue: Short = 0
}
val COLOR_BLUE = object : Color {
    override val red: Short = 0
    override val green: Short = 0
    override val blue: Short = 255
}

val linhaDXF = vitorscoelho.gyncanvas.core.dxf.entities.Line(
    layer = Layer(name = "layer qualquer", color = vitorscoelho.gyncanvas.core.dxf.Color.INDEX_100),
    startPoint = Vector2D(-10.0, -10.0),
    endPoint = Vector2D(10.0, 10.0),
)

val circleDXF = Circle(
    layer = Layer(name = "layer circle", color = vitorscoelho.gyncanvas.core.dxf.Color.INDEX_181),
    centerPoint = Vector2D(x = 2.0, y = 0.0),
    radius = 1.5
)

val polylineDXF = LwPolyline(
    layer = Layer(name = "layer polyline", color = vitorscoelho.gyncanvas.core.dxf.Color.INDEX_111),
    path = Path
        .initBuilder(x = -10.0, y = -5.0)
        .lineTo(x = 2.0, y = 0.0)
        .arcTo(x = -10.0, y = 3.0, bulge = 0.2)
        .closeAndBuild()
)

//fun variosElementos(): List<Primitive> {
//    val cores = listOf(COLOR_GREEN, COLOR_RED, )
//    val lista = mutableListOf<Primitive>()
//    val maxV1 = 999
//    val maxV2 = 99
//    (0..maxV1).forEach { v1 ->
//        (0..maxV2).forEach { v2 ->
////            lista += StrokedCircle(
////                centerPoint = Vector2D(x = v1.toDouble(), y = v2.toDouble()),
////                radius = 1.0,
////                color = COLOR_WHITE
////            )
//            lista += Line(
//                startPoint = Vector2D(x = v1.toDouble(), y = v2.toDouble()),
//                endPoint = Vector2D(x = maxV1.toDouble(), y = maxV2.toDouble()),
//                color = cores.random()
//            )
//        }
//    }
//    return lista
//}

fun testarDesenho(drawingArea: DrawingArea) {
    val drawer = drawingArea.drawer
    drawer.setElements(
        listOf(
            linhaDXF,
            Triangle(
                p1 = Vector2D(-1.0, 0.0),
                p2 = Vector2D(0.0, 1.0),
                p3 = Vector2D(1.0, 0.0),
                color = COLOR_BLUE
            ),
            Line(
                startPoint = Vector2D(-1.0, -1.0),
                endPoint = Vector2D(1.0, 1.0),
                color = COLOR_WHITE
            ),
            Line(
                startPoint = Vector2D(-1.0, 1.0),
                endPoint = Vector2D(1.0, -1.0),
                color = COLOR_RED
            ),
            Line(
                startPoint = Vector2D(-1.0, 0.0),
                endPoint = Vector2D(2.0, 0.0),
                color = COLOR_RED
            ),
            Line(
                startPoint = Vector2D(0.0, -1.0),
                endPoint = Vector2D(0.0, 1.0),
                color = COLOR_WHITE
            ),
            StrokedCircle(
                centerPoint = Vector2D(0.0, 0.0),
                radius = 1.0,
                color = COLOR_GREEN
            ),
            Polyline(
                path = Path
                    .initBuilder(x = 0.0, y = -5.0)
                    .deltaLineTo(deltaX = 5.0)
                    .deltaArcTo(deltaX = 5.0, deltaY = 5.0, bulge = tan(toRadians(90.0) / 4.0))
                    .build(),
                color = COLOR_GREEN
            ),
            polylineDXF,
            circleDXF,
//            RotatedDimension.horizontal(
//                layer = Layer(name = "Cota", color = vitorscoelho.gyncanvas.core.dxf.Color.INDEX_7),
//                dimStyle = DimStyle(
//                    name = "estilo",
//                    textStyle = TextStyle(
//                        name = "EstiloTexto",
//                        fontName = "Ubuntu",
//                        fontFileName = "UbuntuFileName"
//                    )
//                ),
//                xPoint1 = Vector.ZERO, xPoint2 = Vector2D(x = 20.0, y = 0.0),
//                yDimensionLine = 10.0
//            )
        )// + variosElementos()
    )
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