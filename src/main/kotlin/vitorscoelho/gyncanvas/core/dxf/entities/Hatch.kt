package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep
import vitorscoelho.gyncanvas.core.dxf.tables.Layer

class Hatch internal constructor(
    override val layer: Layer,
    override val color: Color,
    internal val pathSteps: List<PathStep>
) : Entity {
    override fun draw(drawer: Drawer) {
        drawer.beginPath()
        pathSteps.forEach { it.draw(drawer = drawer) }
        drawer.closePath()
        drawer.fill()
    }

    companion object {
        fun fromLwPolyline(layer: Layer, color: Color = Color.BY_LAYER, lwPolyline: LwPolyline): Hatch =
            Hatch(layer = layer, color = color, pathSteps = lwPolyline.pathSteps)
    }
}