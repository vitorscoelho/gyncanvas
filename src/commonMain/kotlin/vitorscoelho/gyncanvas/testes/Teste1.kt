package vitorscoelho.gyncanvas.testes

import vitorscoelho.gyncanvas.core.DrawingArea
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.entities.*
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle
import vitorscoelho.gyncanvas.core.event.CanvasMouseButton
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.core.event.CanvasMouseEventType
import vitorscoelho.gyncanvas.math.Vector2D

fun desenhar(drawingArea: DrawingArea) {
    val entitiesList = listOf<Entity>(
        Line(
            layer = Layer(name = "la", color = Color.INDEX_10),
            startPoint = Vector2D(x = 0.0, y = 0.0),
            endPoint = Vector2D(x = 600.0, y = -300.0)
        ),
        Circle(
            layer = Layer(name = "ci", color = Color.INDEX_103),
            centerPoint = Vector2D(x = 600.0, y = -300.0),
            diameter = 200.0
        ),
//        MText(
//            layer = Layer(name = "te", color = Color.INDEX_10),
//            style = TextStyle(name = "ts", fontName = "Ubuntu Light", fontFileName = "Ubuntu Light"),
//            justify = AttachmentPoint.MIDDLE_LEFT,
//            size = 40.0,
//            rotation = 0.0,
//            position = Vector2D(x = 600.0, y = -300.0),
//            content = "Título"
//        )
    )
    val drawer = drawingArea.drawer
    var x = 600.0
    drawer.camera.setPosition(x, 100.0)
    drawer.draw(backgroundColor = Color.INDEX_250, entities = entitiesList)
    val panClicked = PanClicked(
        mouseButton = CanvasMouseButton.MIDDLE,
        drawingArea = drawingArea,
        drawFunction = { drawer.draw(backgroundColor = Color.INDEX_250, entities = entitiesList) }
    )
    panClicked.enable()
    drawingArea.addEventListener(eventType = CanvasMouseEventType.MOUSE_CLICKED) { event ->
        if (event.button == CanvasMouseButton.PRIMARY) {
            println("Ligou")
            panClicked.enable()
        } else if (event.button == CanvasMouseButton.SECONDARY) {
            println("Desligou")
            panClicked.disable()
        }
    }
}

class PanClicked(
    val mouseButton: CanvasMouseButton,
    /*val cursorPan: Cursor,*/
    val drawingArea: DrawingArea,
    val drawFunction: () -> Unit
) {
    private var xStartPan: Double = 0.0
    private var yStartPan: Double = 0.0
    private var started: Boolean = false

    private val eventHandlerMouseClicked = { event: CanvasMouseEvent ->
        if (event.button == mouseButton) {
            started = !started
            if (started) {
//                currentDefaultCursor = drawingArea.node.cursor ?: Cursor.DEFAULT
//                drawingArea.node.cursor = cursorPan
                xStartPan = event.x
                yStartPan = event.y
            } else {
//                drawingArea.node.cursor = currentDefaultCursor
            }
        }
    }

    private val eventHandlerMouseMoved = { event: CanvasMouseEvent ->
        if (started) {
            val camera = drawingArea.drawer.camera
//            val worldStartPan = camera.worldCoordinates(
//                xCamera = xStartPan, yCamera = yStartPan
//            )
//            val worldEndPan = camera.worldCoordinates(
//                xCamera = event.x, yCamera = event.y
//            )
            val worldStartPan = Vector2D(x = xStartPan, y = yStartPan)
            val worldEndPan = Vector2D(x = event.x, y = event.y)
            val deltaWorld = worldEndPan - worldStartPan
            camera.translate(deltaWorld.x, deltaWorld.y)
            xStartPan = event.x
            yStartPan = event.y
            drawFunction()
        }
    }

    fun enable() {
        disable()
        drawingArea.addEventListener(
            eventType = CanvasMouseEventType.MOUSE_CLICKED,
            eventHandler = eventHandlerMouseClicked
        )
        drawingArea.addEventListener(
            eventType = CanvasMouseEventType.MOUSE_MOVED,
            eventHandler = eventHandlerMouseMoved
        )
    }

    fun disable() {
        drawingArea.removeEventListener(
            eventType = CanvasMouseEventType.MOUSE_CLICKED,
            eventHandler = eventHandlerMouseClicked
        )
        drawingArea.removeEventListener(
            eventType = CanvasMouseEventType.MOUSE_MOVED,
            eventHandler = eventHandlerMouseMoved
        )
    }
}