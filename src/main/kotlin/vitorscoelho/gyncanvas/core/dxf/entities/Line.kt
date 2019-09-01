package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vetor2D

class Line(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    val startPoint: Vetor2D,
    val endPoint: Vetor2D
) : Entity {
    override fun draw(drawer: Drawer) {
        drawer.strokeLine(
            x1 = startPoint.x, y1 = startPoint.y,
            x2 = endPoint.x, y2 = endPoint.y
        )
    }
}