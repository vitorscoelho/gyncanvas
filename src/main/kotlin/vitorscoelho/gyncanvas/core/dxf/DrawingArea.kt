package vitorscoelho.gyncanvas.core.dxf

import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import tornadofx.fitToParentSize
import tornadofx.onChange
import vitorscoelho.gyncanvas.core.dxf.entities.CompositeEntity
import vitorscoelho.gyncanvas.core.dxf.entities.Entity

abstract class DrawingArea(
    val backgroundColor: Color = Color.INDEX_250
) {
    protected abstract val drawer: Drawer
    val camera: Camera
        get() = drawer.camera
    private val entities = mutableListOf<Entity>()

    fun addEntity(entity: Entity) {
        entities += entity
    }

    fun addEntities(entities: Iterable<Entity>) {
        entities.forEach { addEntity(entity = it) }
    }

    fun removeEntity(entity: Entity) {
        entities -= entity
    }

    fun removeEntities(entity: Entity) {
        entities.forEach { addEntity(entity = it) }
    }

    fun removeEntity(index: Int) {
        entities.removeAt(index)
    }

    fun removeAllEntities() {
        entities.clear()
    }

    fun draw() {
        drawer.apllyCameraTransform()
        drawer.fill = backgroundColor
        drawer.fillBackground()
        entities.forEach { entity -> drawEntity(entity) }
    }

    private fun drawEntity(entity: Entity) {
        if (entity is CompositeEntity) {
            entity.entities.forEach { drawEntity(it) }
        } else {
            entity.shapeType.applyColor(drawer = drawer, entity = entity)
            entity.draw(drawer = drawer)
        }//TODO dar um jeito de remover esse 'if'
    }

//    private fun resetTransform() {
//        drawer.copyToTransform(TransformationMatrix.IDENTITY)
//    }
}

class FXDrawingArea(
    backgroundColor: Color = Color.INDEX_250
) : DrawingArea(backgroundColor = backgroundColor) {
    private val canvas = Canvas()
    protected override val drawer = FXDrawer(canvas = canvas)
    val node: Node

    init {
        node = Pane(canvas)
        this.node.parentProperty().addListener {/*observable, oldValue, newValue*/ _, _, _ ->
            this.node.fitToParentSize()
        }
        canvas.widthProperty().bind(node.widthProperty())
        canvas.heightProperty().bind(node.heightProperty())
        canvas.widthProperty().onChange { draw() }
        canvas.heightProperty().onChange { draw() }
    }
}

class ZoomScroll(val zoomFactor: Double, val drawingArea: FXDrawingArea) {
    init {
        require(zoomFactor > 0.0) { "|zoomFactor| must be greater than zero" }
    }

    private val eventHandlerZoomScroll: EventHandler<ScrollEvent> by lazy {
        EventHandler<ScrollEvent> { event ->
            val worldCoordinates = drawingArea.camera.worldCoordinates(xCamera = event.x, yCamera = event.y)
            val factor = when {
                event.deltaY > 0 -> zoomFactor
                event.deltaY < 0 -> 1.0 / zoomFactor
                else -> 0.0
            }
            if (factor == 0.0) return@EventHandler
            drawingArea.camera.appendZoom(factor = factor, xTarget = worldCoordinates.x, yTarget = worldCoordinates.y)
            drawingArea.draw()
        }
    }

    fun enable() {
        disable()
        drawingArea.node.addEventHandler(ScrollEvent.SCROLL, eventHandlerZoomScroll)
    }

    fun disable() {
        drawingArea.node.removeEventHandler(ScrollEvent.SCROLL, eventHandlerZoomScroll)
    }
}

class PanDragged(val mouseButton: MouseButton, val cursorWhenDraged: Cursor, val drawingArea: FXDrawingArea) {
    private var xStartPan: Double = 0.0
    private var yStartPan: Double = 0.0
    private var currentDefaultCursor: Cursor = Cursor.DEFAULT

    private val eventHandlerMousePressedPanDragged: EventHandler<MouseEvent> by lazy {
        EventHandler<MouseEvent> { event ->
            if (event.button == mouseButton) {
                currentDefaultCursor = drawingArea.node.cursor ?: Cursor.DEFAULT
                xStartPan = event.x
                yStartPan = event.y
            }
        }
    }
    private val eventHandlerMouseReleasedPanDragged: EventHandler<MouseEvent> by lazy {
        EventHandler<MouseEvent> { event ->
            if (event.button == mouseButton) drawingArea.node.cursor = currentDefaultCursor
        }
    }
    private val eventHandlerMouseDraggedPanDragged: EventHandler<MouseEvent> by lazy {
        EventHandler<MouseEvent> { event ->
            if (event.isMiddleButtonDown) {
                drawingArea.node.cursor = cursorWhenDraged
                val worldStartPan = drawingArea.camera.worldCoordinates(
                    xCamera = xStartPan, yCamera = yStartPan
                )
                val worldEndPan = drawingArea.camera.worldCoordinates(
                    xCamera = event.x, yCamera = event.y
                )
                val deltaWorld = worldEndPan - worldStartPan
                drawingArea.camera.translate(-deltaWorld.x, -deltaWorld.y)
                xStartPan = event.x
                yStartPan = event.y
            }
            drawingArea.draw()
        }
    }

    fun enable() {
        disable()
        drawingArea.node.addEventHandler(MouseEvent.MOUSE_PRESSED, eventHandlerMousePressedPanDragged)
        drawingArea.node.addEventHandler(MouseEvent.MOUSE_RELEASED, eventHandlerMouseReleasedPanDragged)
        drawingArea.node.addEventHandler(MouseEvent.MOUSE_DRAGGED, eventHandlerMouseDraggedPanDragged)
    }

    fun disable() {
        drawingArea.node.removeEventHandler(MouseEvent.MOUSE_PRESSED, eventHandlerMousePressedPanDragged)
        drawingArea.node.removeEventHandler(MouseEvent.MOUSE_RELEASED, eventHandlerMouseReleasedPanDragged)
        drawingArea.node.removeEventHandler(MouseEvent.MOUSE_DRAGGED, eventHandlerMouseDraggedPanDragged)
    }
}