package vitorscoelho.gyncanvas.core

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import tornadofx.fitToParentSize
import vitorscoelho.gyncanvas.core.event.CanvasMouseButton
import vitorscoelho.gyncanvas.core.event.CanvasMouseEvent
import vitorscoelho.gyncanvas.core.event.CanvasMouseEventType

private val mapMouseButton by lazy {
    mapOf(
        MouseButton.NONE to CanvasMouseButton.NONE,
        MouseButton.PRIMARY to CanvasMouseButton.PRIMARY,
        MouseButton.MIDDLE to CanvasMouseButton.MIDDLE,
        MouseButton.SECONDARY to CanvasMouseButton.SECONDARY
    )
}

private fun mouseButton(jfxMouseButton: MouseButton) = mapMouseButton.getOrDefault(
    jfxMouseButton, CanvasMouseButton.NONE
)

private val mapMouseEventType by lazy {
    mapOf(
        CanvasMouseEventType.MOUSE_PRESSED to MouseEvent.MOUSE_PRESSED,
        CanvasMouseEventType.MOUSE_RELEASED to MouseEvent.MOUSE_RELEASED,
        CanvasMouseEventType.MOUSE_CLICKED to MouseEvent.MOUSE_CLICKED,
        CanvasMouseEventType.MOUSE_ENTERED to MouseEvent.MOUSE_ENTERED,
        CanvasMouseEventType.MOUSE_EXITED to MouseEvent.MOUSE_EXITED,
        CanvasMouseEventType.MOUSE_MOVED to MouseEvent.MOUSE_MOVED
    )
}

private fun mouseEventType(canvasMouseEventType: CanvasMouseEventType) = mapMouseEventType[canvasMouseEventType]!!

class ImplementacaoCanvasMouseEvent(private val mouseEventJFX: MouseEvent) : CanvasMouseEvent {
    override val x: Double get() = mouseEventJFX.x
    override val y: Double get() = mouseEventJFX.y
    override val button: CanvasMouseButton get() = mouseButton(mouseEventJFX.button)
}

class FXDrawingArea : DrawingArea() {
    private val canvas = Canvas()
    override val drawer = FXDrawer(canvas = canvas)
    val node: Node

    init {
        node = Pane(canvas)
        this.node.parentProperty().addListener {/*observable, oldValue, newValue*/ _, _, _ ->
            this.node.fitToParentSize()
        }
        canvas.widthProperty().bind(node.widthProperty())
        canvas.heightProperty().bind(node.heightProperty())
//        canvas.widthProperty().onChange { draw() }
//        canvas.heightProperty().onChange { draw() }
    }

    override fun addEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) {
        val jfxEventType = mouseEventType(eventType)
        if (listenersManager.listenerJaIncluso(eventType, eventHandler)) return
        val jfxEventHandler = EventHandler<MouseEvent> { event -> eventHandler(ImplementacaoCanvasMouseEvent(event)) }
        listenersManager.add(eventType, eventHandler, jfxEventHandler)
        node.addEventHandler(jfxEventType, jfxEventHandler)
    }

    override fun removeEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) {
        val jfxEventType = mouseEventType(eventType)
        if (!listenersManager.listenerJaIncluso(eventType, eventHandler)) return
        val jfxEventHandler = listenersManager.getJFXEventHandler(eventType, eventHandler)
        listenersManager.remove(eventType, eventHandler)
        node.removeEventHandler(jfxEventType, jfxEventHandler as EventHandler<in MouseEvent>)
    }

    private val listenersManager = object {
        private val listenersMap =
            hashMapOf<Pair<CanvasMouseEventType, (event: CanvasMouseEvent) -> Unit>, EventHandler<*>>()

        fun listenerJaIncluso(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit): Boolean {
            return listenersMap.containsKey(key = Pair(eventType, eventHandler))
        }

        fun add(
            eventType: CanvasMouseEventType,
            eventHandler: (event: CanvasMouseEvent) -> Unit,
            jfxEventHandler: EventHandler<*>
        ) {
            listenersMap[Pair(eventType, eventHandler)] = jfxEventHandler
        }

        fun remove(
            eventType: CanvasMouseEventType,
            eventHandler: (event: CanvasMouseEvent) -> Unit
        ) {
            listenersMap.remove(Pair(eventType, eventHandler))
        }

        fun getJFXEventHandler(
            eventType: CanvasMouseEventType,
            eventHandler: (event: CanvasMouseEvent) -> Unit
        ): EventHandler<*>? {
            return listenersMap[Pair(eventType, eventHandler)]
        }
    }
}