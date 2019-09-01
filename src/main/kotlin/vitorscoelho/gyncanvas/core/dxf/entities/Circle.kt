package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vetor2D

class Circle(
    override val layer: Layer,
    override val color: Color= Color.BY_LAYER,
    val centerPoint: Vetor2D,
    val diameter: Double
) : Entity {
    override fun draw(drawer: Drawer) {
        drawer.strokeCircle(xCenter = centerPoint.x, yCenter = centerPoint.y, diameter = diameter)
    }
}