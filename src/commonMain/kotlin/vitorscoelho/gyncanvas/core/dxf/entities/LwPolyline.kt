package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.Drawer
import vitorscoelho.gyncanvas.core.dxf.ShapeType
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilder
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilderStep1
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.primitives.ClosedPolyline
import vitorscoelho.gyncanvas.core.primitives.Polyline
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D

data class LwPolyline internal constructor(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    val closed: Boolean,
    internal val pathSteps: List<PathStep>
) : Entity {
//    private val primitivePolyline :Polyline= if (closed){
//        ClosedPolyline()
//    }

    override val primitives: List<Primitive>
        get() = TODO("Not yet implemented")

    override val shapeType: ShapeType
        get() = ShapeType.STROKED

//    override fun draw(drawer: Drawer) {
//        applyLineWidth(drawer = drawer)
//        drawer.beginPath()
//        pathSteps.forEach { it.draw(drawer = drawer) }
//        if (closed) drawer.closePath()
//        drawer.stroke()
//    }

    companion object {
        fun initBuilder(layer: Layer, color: Color = Color.BY_LAYER): LwPolylineBuilderStep1 {
            return LwPolylineBuilder.init(layer = layer, color = color)
        }

        fun rectangle(
            layer: Layer,
            color: Color = Color.BY_LAYER,
            startPoint: Vector2D,
            deltaX: Double,
            deltaY: Double
        ): LwPolyline {
            return initBuilder(layer = layer, color = color)
                .startPoint(startPoint)
                .deltaLineTo(deltaX = deltaX)
                .deltaLineTo(deltaY = deltaY)
                .deltaLineTo(deltaX = -deltaX)
                .closeAndBuild()
        }
    }
}