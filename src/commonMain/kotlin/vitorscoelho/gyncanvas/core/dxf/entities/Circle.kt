package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.ShapeType
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.math.Vector

class Circle(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    centerPoint: Vector,
    radius: Double
) : Entity {
    private val primitiveCircle = vitorscoelho.gyncanvas.core.primitives.StrokedCircle(
        centerPoint = centerPoint, radius = radius, color = color
    )

    val centerPoint: Vector get() = primitiveCircle.centerPoint
    val radius: Double get() = primitiveCircle.radius
    val diameter: Double get() = radius * 2.0

    override val shapeType: ShapeType
        get() = ShapeType.STROKED

    override val primitives: List<Primitive> get() = listOf(primitiveCircle)
}