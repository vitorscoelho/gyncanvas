package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.Path
import vitorscoelho.gyncanvas.core.primitives.Primitive

data class Hatch internal constructor(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    internal val path: Path
) : Entity {
//    override fun draw(drawer: Drawer) {
//        drawer.beginPath()
//        pathSteps.forEach { it.draw(drawer = drawer) }
//        drawer.closePath()
//        drawer.fill()
//    }

    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        TODO("Not yet implemented")
    }
}

interface HatchPattern {

}