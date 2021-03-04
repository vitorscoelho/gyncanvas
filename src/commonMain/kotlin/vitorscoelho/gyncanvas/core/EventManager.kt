package vitorscoelho.gyncanvas.core

import vitorscoelho.gyncanvas.core.event.*
import vitorscoelho.gyncanvas.core.event.CanvasEvent

abstract class EventManager {
    protected abstract fun add(eventType: CanvasEventType, eventHandler: (event: CanvasEvent) -> Unit)
    protected abstract fun remove(eventType: CanvasEventType, eventHandler: (event: CanvasEvent) -> Unit)

    //Mouse events
    @Suppress("UNCHECKED_CAST")
    private fun addMouseEvent(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) =
        add(eventType = eventType, eventHandler = eventHandler as (CanvasEvent) -> Unit)

    @Suppress("UNCHECKED_CAST")
    private fun removeMouseEvent(eventType: CanvasMouseEventType, eventHandler: (event: CanvasMouseEvent) -> Unit) =
        remove(eventType = eventType, eventHandler = eventHandler as (CanvasEvent) -> Unit)

    fun addMousePressed(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        addMouseEvent(eventType = CanvasMouseEventType.MOUSE_PRESSED, eventHandler = eventHandler)

    fun removeMousePressed(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        removeMouseEvent(eventType = CanvasMouseEventType.MOUSE_PRESSED, eventHandler = eventHandler)

    fun addMouseReleased(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        addMouseEvent(eventType = CanvasMouseEventType.MOUSE_RELEASED, eventHandler = eventHandler)

    fun removeMouseReleased(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        removeMouseEvent(eventType = CanvasMouseEventType.MOUSE_RELEASED, eventHandler = eventHandler)

    fun addMouseClicked(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        addMouseEvent(eventType = CanvasMouseEventType.MOUSE_CLICKED, eventHandler = eventHandler)

    fun removeMouseClicked(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        removeMouseEvent(eventType = CanvasMouseEventType.MOUSE_CLICKED, eventHandler = eventHandler)

    fun addMouseEntered(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        addMouseEvent(eventType = CanvasMouseEventType.MOUSE_ENTERED, eventHandler = eventHandler)

    fun removeMouseEntered(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        removeMouseEvent(eventType = CanvasMouseEventType.MOUSE_ENTERED, eventHandler = eventHandler)

    fun addMouseExited(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        addMouseEvent(eventType = CanvasMouseEventType.MOUSE_EXITED, eventHandler = eventHandler)

    fun removeMouseExited(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        removeMouseEvent(eventType = CanvasMouseEventType.MOUSE_EXITED, eventHandler = eventHandler)

    fun addMouseMoved(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        addMouseEvent(eventType = CanvasMouseEventType.MOUSE_MOVED, eventHandler = eventHandler)

    fun removeMouseMoved(eventHandler: (event: CanvasMouseEvent) -> Unit) =
        removeMouseEvent(eventType = CanvasMouseEventType.MOUSE_MOVED, eventHandler = eventHandler)

    //Scroll events
    @Suppress("UNCHECKED_CAST")
    private fun addScrollEvent(eventType: CanvasScrollEventType, eventHandler: (event: CanvasScrollEvent) -> Unit) =
        add(eventType = eventType, eventHandler = eventHandler as (CanvasEvent) -> Unit)

    @Suppress("UNCHECKED_CAST")
    private fun removeScrollEvent(eventType: CanvasScrollEventType, eventHandler: (event: CanvasScrollEvent) -> Unit) =
        remove(eventType = eventType, eventHandler = eventHandler as (CanvasEvent) -> Unit)

    fun addScroll(eventHandler: (event: CanvasScrollEvent) -> Unit) =
        addScrollEvent(eventType = CanvasScrollEventType.SCROLL, eventHandler = eventHandler)

    fun removeScroll(eventHandler: (event: CanvasScrollEvent) -> Unit) =
        removeScrollEvent(eventType = CanvasScrollEventType.SCROLL, eventHandler = eventHandler)
}