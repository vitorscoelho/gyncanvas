package vitorscoelho.gyncanvas.core

import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import tornadofx.fitToParentSize
import vitorscoelho.gyncanvas.core.event.*
import java.lang.IllegalArgumentException

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

private fun jfxMouseEventType(canvasMouseEventType: CanvasMouseEventType) = mapMouseEventType[canvasMouseEventType]!!
private fun jfxScrollEventType(canvasScrollEventType: CanvasScrollEventType) = ScrollEvent.SCROLL
private fun jfxEventType(canvasEventType: CanvasEventType): EventType<*> = when (canvasEventType) {
    is CanvasMouseEventType -> jfxMouseEventType(canvasEventType)
    is CanvasScrollEventType -> jfxScrollEventType(canvasEventType)
    else -> throw IllegalArgumentException("Unsupported EventType")
}

class ImplementacaoCanvasMouseEvent(private val mouseEventJFX: MouseEvent) : CanvasMouseEvent {
    override val x: Double get() = mouseEventJFX.x
    override val y: Double get() = mouseEventJFX.y
    override val button: CanvasMouseButton get() = mouseButton(mouseEventJFX.button)
}

class ImplementacaoCanvasScrollEvent(private val scrollEventJFX: ScrollEvent) : CanvasScrollEvent {
    override val x: Double get() = scrollEventJFX.x
    override val y: Double get() = scrollEventJFX.y
    override val deltaY: Double get() = scrollEventJFX.deltaY
}

private fun createJfxEventHandler(
    eventType: CanvasEventType,
    eventHandler: (event: CanvasEvent) -> Unit
): EventHandler<*> {
    return when (eventType) {
        is CanvasMouseEventType -> EventHandler<MouseEvent> { event -> eventHandler(ImplementacaoCanvasMouseEvent(event)) }
        is CanvasScrollEventType -> EventHandler<ScrollEvent> { event ->
            eventHandler(ImplementacaoCanvasScrollEvent(event))
        }
        else -> throw IllegalArgumentException("Unsupported EventType")
    }
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

    private fun addEventListenerPadrao(eventType: CanvasEventType, eventHandler: (event: CanvasEvent) -> Unit) {
        if (listenersManager.listenerJaIncluso(eventType, eventHandler)) return
        val jfxEventType: EventType<*> = jfxEventType(eventType)
        val jfxEventHandler: EventHandler<*> = createJfxEventHandler(eventType, eventHandler)
        listenersManager.add(eventType, eventHandler, jfxEventHandler)
        node.addEventHandler(jfxEventType, jfxEventHandler)
    }

    override fun addEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) {
        addEventListenerPadrao(eventType, eventHandler as (CanvasEvent) -> Unit)
    }

    override fun removeEventListener(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) {
        val jfxEventType = jfxMouseEventType(eventType)
        if (!listenersManager.listenerJaIncluso(eventType, eventHandler)) return
        val jfxEventHandler = listenersManager.getJFXEventHandler(eventType, eventHandler)
        listenersManager.remove(eventType, eventHandler)
        node.removeEventHandler(jfxEventType, jfxEventHandler as EventHandler<in MouseEvent>)
    }

    override fun addEventListener(eventType: CanvasScrollEventType, eventHandler: (event: CanvasScrollEvent) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun removeEventListener(
        eventType: CanvasScrollEventType,
        eventHandler: (event: CanvasScrollEvent) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    private val listenersManager = object {
        private val listenersMap =
            hashMapOf<Pair<CanvasEventType, (event: CanvasEvent) -> Unit>, EventHandler<*>>()

        fun listenerJaIncluso(
            eventType: CanvasEventType,
            eventHandler: (event: CanvasEvent) -> Unit
        ): Boolean {
            return listenersMap.containsKey(key = Pair(eventType, eventHandler))
        }

        fun add(
            eventType: CanvasEventType,
            eventHandler: (event: CanvasEvent) -> Unit,
            jfxEventHandler: EventHandler<*>
        ) {
            listenersMap[Pair(eventType, eventHandler)] = jfxEventHandler
        }

        fun remove(
            eventType: CanvasEventType,
            eventHandler: (event: CanvasEvent) -> Unit
        ) {
            listenersMap.remove(Pair(eventType, eventHandler))
        }

        fun getJFXEventHandler(
            eventType: CanvasEventType,
            eventHandler: (event: CanvasEvent) -> Unit
        ): EventHandler<*>? {
            return listenersMap[Pair(eventType, eventHandler)]
        }
    }
}