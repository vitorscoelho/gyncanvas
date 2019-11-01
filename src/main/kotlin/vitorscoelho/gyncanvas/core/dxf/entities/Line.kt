package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.ShapeType
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D

data class Line(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    val startPoint: Vector2D,
    val endPoint: Vector2D
) : Entity {
    override val shapeType: ShapeType
        get() = ShapeType.STROKED

    override fun draw(drawer: Drawer) {
        applyLineWidth(drawer = drawer)
        drawer.strokeLine(
            x1 = startPoint.x, y1 = startPoint.y,
            x2 = endPoint.x, y2 = endPoint.y
        )
    }

    override fun transform(transformationMatrix: TransformationMatrix): Line =
        copy(
            startPoint = startPoint.transform(transformationMatrix),
            endPoint = endPoint.transform(transformationMatrix)
        )
}