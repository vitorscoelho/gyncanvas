package vitorscoelho.gyncanvas.core.dxf.entities.path

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.math.Vetor2D

sealed class PathStep {
    abstract fun draw(drawer: Drawer)
}

class MoveTo(val point: Vetor2D) : PathStep() {
    override fun draw(drawer: Drawer) = drawer.moveTo(x = point.x, y = point.y)
}

class LineTo(val point: Vetor2D) : PathStep() {
    override fun draw(drawer: Drawer) = drawer.lineTo(x = point.x, y = point.y)
}

class ArcTo(val tangentPoint1: Vetor2D, val tangentPoint2: Vetor2D, val radius: Double) : PathStep() {
    override fun draw(drawer: Drawer) = drawer.arcTo(
        xTangent1 = tangentPoint1.x,
        yTangent1 = tangentPoint1.y,
        xTangent2 = tangentPoint2.x,
        yTangent2 = tangentPoint2.y,
        radius = radius
    )
}