package vitorscoelho.gyncanvas.core

import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import vitorscoelho.gyncanvas.core.event.*

class FXEventManager(val canvas: FXDrawingArea) : EventManager() {
    private val listenersMap = hashMapOf<EventWrapper, EventHandler<*>>()

    override fun add(eventType: CanvasEventType, eventHandler: (event: CanvasEvent) -> Unit) {
        val wrapper = EventWrapper(canvasEventType = eventType, handler = eventHandler)
        if (listenersMap.contains(wrapper)) return
        val jfxEventHandler = when (eventType) {
            is CanvasMouseEventType -> addMouseEventListener(canvas, eventType, eventHandler)
            is CanvasScrollEventType -> addScrollEventListener(canvas, eventType, eventHandler)
            else -> throw IllegalArgumentException("|eventType| not supported")
        }
        listenersMap[wrapper] = jfxEventHandler
    }

    override fun remove(eventType: CanvasEventType, eventHandler: (event: CanvasEvent) -> Unit) {
        val wrapper = EventWrapper(canvasEventType = eventType, handler = eventHandler)
        if (!listenersMap.contains(wrapper)) return
        val jfxEventHandler = listenersMap[wrapper]!!
        when (eventType) {
            is CanvasMouseEventType -> removeMouseEventListener(canvas, eventType, jfxEventHandler)
            is CanvasScrollEventType -> removeScrollEventListener(canvas, eventType, jfxEventHandler)
            else -> throw IllegalArgumentException("|eventType| not supported")
        }
        listenersMap.remove(wrapper)
    }
}

private data class EventWrapper(val canvasEventType: CanvasEventType, val handler: (event: CanvasEvent) -> Unit)

private val mapMouseButton by lazy {
    mapOf(
        MouseButton.NONE to CanvasMouseButton.NONE,
        MouseButton.PRIMARY to CanvasMouseButton.PRIMARY,
        MouseButton.MIDDLE to CanvasMouseButton.MIDDLE,
        MouseButton.SECONDARY to CanvasMouseButton.SECONDARY
    )
}

class ImplementacaoCanvasMouseEvent(private val mouseEventJFX: MouseEvent) : CanvasMouseEvent {
    override val x: Double get() = mouseEventJFX.x
    override val y: Double get() = mouseEventJFX.y
    override val button: CanvasMouseButton
        get() = mapMouseButton.getOrDefault(
            mouseEventJFX.button, CanvasMouseButton.NONE
        )
}

class ImplementacaoCanvasScrollEvent(private val scrollEventJFX: ScrollEvent) : CanvasScrollEvent {
    override val x: Double get() = scrollEventJFX.x
    override val y: Double get() = scrollEventJFX.y
    override val deltaY: Double get() = scrollEventJFX.deltaY
}

private fun jfxMouseEventType(canvasMouseEventType: CanvasMouseEventType) = when (canvasMouseEventType) {
    CanvasMouseEventType.MOUSE_PRESSED -> MouseEvent.MOUSE_PRESSED
    CanvasMouseEventType.MOUSE_RELEASED -> MouseEvent.MOUSE_RELEASED
    CanvasMouseEventType.MOUSE_CLICKED -> MouseEvent.MOUSE_CLICKED
    CanvasMouseEventType.MOUSE_ENTERED -> MouseEvent.MOUSE_ENTERED
    CanvasMouseEventType.MOUSE_EXITED -> MouseEvent.MOUSE_EXITED
    CanvasMouseEventType.MOUSE_MOVED -> MouseEvent.MOUSE_MOVED
//    else -> throw IllegalArgumentException("|eventType| not supported")
}

private fun jfxScrollEventType(canvasScrollEventType: CanvasScrollEventType) = when (canvasScrollEventType) {
    CanvasScrollEventType.SCROLL -> ScrollEvent.SCROLL
//    else -> throw IllegalArgumentException("|eventType| not supported")
}

private fun addMouseEventListener(
    canvas: FXDrawingArea,
    eventType: CanvasMouseEventType,
    eventHandler: (event: CanvasMouseEvent) -> Unit
): EventHandler<MouseEvent> {
    val jfxEventType = jfxMouseEventType(eventType)
    val jfxEventHandler = EventHandler<MouseEvent> { event -> eventHandler(ImplementacaoCanvasMouseEvent(event)) }
    canvas.node.addEventHandler(jfxEventType, jfxEventHandler)
    return jfxEventHandler
}

private fun addScrollEventListener(
    canvas: FXDrawingArea,
    eventType: CanvasScrollEventType,
    eventHandler: (event: CanvasScrollEvent) -> Unit
): EventHandler<ScrollEvent> {
    val jfxEventType = jfxScrollEventType(eventType)
    val jfxEventHandler = EventHandler<ScrollEvent> { event -> eventHandler(ImplementacaoCanvasScrollEvent(event)) }
    canvas.node.addEventHandler(jfxEventType, jfxEventHandler)
    return jfxEventHandler
}

@Suppress("UNCHECKED_CAST")
private fun removeMouseEventListener(
    canvas: FXDrawingArea,
    eventType: CanvasMouseEventType,
    jfxEventHandler: EventHandler<*>
) {
    val jfxEventType = jfxMouseEventType(eventType)
    canvas.node.removeEventHandler(jfxEventType, jfxEventHandler as EventHandler<in MouseEvent>)
}

@Suppress("UNCHECKED_CAST")
private fun removeScrollEventListener(
    canvas: FXDrawingArea,
    eventType: CanvasScrollEventType,
    jfxEventHandler: EventHandler<*>
) {
    val jfxEventType = jfxScrollEventType(eventType)
    canvas.node.removeEventHandler(jfxEventType, jfxEventHandler as EventHandler<in ScrollEvent>)
}