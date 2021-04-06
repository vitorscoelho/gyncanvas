package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.math.Vector

class Line(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    startPoint: Vector,
    endPoint: Vector
) : Entity {
    private val primitiveLine = vitorscoelho.gyncanvas.core.primitives.Line(
        startPoint = startPoint, endPoint = endPoint, color = color.effectiveColor(entity = this)
    )
    val startPoint: Vector get() = primitiveLine.startPoint
    val endPoint: Vector get() = primitiveLine.endPoint

    override val primitivesCount: Int get() = 1

    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        action(primitiveLine)
    }
}