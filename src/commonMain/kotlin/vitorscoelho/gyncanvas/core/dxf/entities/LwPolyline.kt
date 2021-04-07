package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.Path
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.math.Vector

class LwPolyline internal constructor(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    path: Path
) : Entity {
    private val primitivePolyline = vitorscoelho.gyncanvas.core.primitives.Polyline(
        path = path, color = color.effectiveColor(entity = this)
    )
    val path: Path get() = primitivePolyline.path
    val closed: Boolean get() = primitivePolyline.path.closed

    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        action(primitivePolyline)
    }

    companion object {
        fun rectangle(
            layer: Layer,
            color: Color = Color.BY_LAYER,
            startPoint: Vector,
            deltaX: Double,
            deltaY: Double
        ) = LwPolyline(
            layer = layer, color = color,
            path = Path.rectangle(startPoint = startPoint, deltaX = deltaX, deltaY = deltaY)
        )
    }
}