package vitorscoelho.gyncanvas

import vitorscoelho.gyncanvas.core.EventManager

abstract class DrawingArea {
    abstract var width: Int
        protected set
    abstract var height: Int
        protected set
    abstract val drawer: Drawer
    abstract val listeners: EventManager
}