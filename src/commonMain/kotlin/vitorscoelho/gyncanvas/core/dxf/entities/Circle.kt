package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.core.primitives.StrokedCircle
import vitorscoelho.gyncanvas.math.Vector

class Circle(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    centerPoint: Vector,
    radius: Double
) : Entity {
    private val primitiveCircle = StrokedCircle(
        centerPoint = centerPoint, radius = radius, color = color.effectiveColor(this)
    )

    val centerPoint: Vector get() = primitiveCircle.centerPoint
    val radius: Double get() = primitiveCircle.radius
    val diameter: Double get() = radius * 2.0

    override val primitivesCount: Int get() = 1

    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        action(primitiveCircle)
    }
}