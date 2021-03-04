package vitorscoelho.gyncanvas.core

class FXCanvasController(val canvas: FXDrawingArea) : CanvasController {
    override val drawer: Drawer = FXDrawer(canvas = canvas.canvas)
    override val listeners: EventManager = FXEventManager(canvas = canvas)
}