package vitorscoelho.gyncanvas

import vitorscoelho.gyncanvas.core.primitives.Color
import vitorscoelho.gyncanvas.core.primitives.Drawable

abstract class Drawer {
    abstract fun setElements(elements: List<Drawable>)
    abstract fun draw(backgroundColor: Color, camera: OrthographicCamera2D)
}