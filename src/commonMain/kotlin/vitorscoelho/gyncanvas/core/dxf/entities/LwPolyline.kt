package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.Path
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.math.Vector
import vitorscoelho.gyncanvas.math.Vector2D

data class LwPolyline internal constructor(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    internal val path: Path
) : Entity {
    //    private val primitivePolyline :Polyline= if (closed){
//        ClosedPolyline()
//    }
    val closed: Boolean get() = path.closed

    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        TODO("Not yet implemented")
    }

    //    override fun draw(drawer: Drawer) {
//        applyLineWidth(drawer = drawer)
//        drawer.beginPath()
//        pathSteps.forEach { it.draw(drawer = drawer) }
//        if (closed) drawer.closePath()
//        drawer.stroke()
//    }

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