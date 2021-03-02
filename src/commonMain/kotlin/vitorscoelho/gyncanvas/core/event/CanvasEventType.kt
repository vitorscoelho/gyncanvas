package vitorscoelho.gyncanvas.core.event

interface CanvasEventType

enum class CanvasMouseEventType : CanvasEventType {
    MOUSE_PRESSED,
    MOUSE_RELEASED,
    MOUSE_CLICKED,
    MOUSE_ENTERED,
    MOUSE_EXITED,
    MOUSE_MOVED
}

enum class CanvasScrollEventType : CanvasEventType {
    SCROLL
}