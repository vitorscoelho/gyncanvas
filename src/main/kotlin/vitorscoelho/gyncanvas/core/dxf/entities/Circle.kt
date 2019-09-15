package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vetor2D

data class Circle(
    override val properties: EntityProperties,
    val centerPoint: Vetor2D,
    val diameter: Double
) : Entity {
    override fun applyProperties(drawer: Drawer) {
        applyLineWidth(drawer = drawer)
        applyColor(drawer = drawer, layer = layer, color = color)
    }

    override fun draw(drawer: Drawer) {
        drawer.strokeCircle(xCenter = centerPoint.x, yCenter = centerPoint.y, diameter = diameter)
    }

    override fun transform(transformationMatrix: TransformationMatrix): Circle =
        copy(
            centerPoint = centerPoint.transform(transformationMatrix),
            diameter = diameter * transformationMatrix.scale
        )
}