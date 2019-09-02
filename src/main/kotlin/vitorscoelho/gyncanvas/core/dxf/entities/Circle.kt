package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.math.Vetor2D

class Circle(
    override val properties: EntityProperties,
    val centerPoint: Vetor2D,
    val diameter: Double
) : Entity {
    override fun draw(drawer: Drawer) {
        drawer.strokeCircle(xCenter = centerPoint.x, yCenter = centerPoint.y, diameter = diameter)
    }
}