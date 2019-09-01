package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilder
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilderStep1
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vetor2D

class LwPolyline internal constructor(
    override val layer: Layer,
    override val color: Color,
    val closed: Boolean,
    internal val pathSteps: List<PathStep>
) : Entity {
    override fun draw(drawer: Drawer) {
        drawer.beginPath()
        pathSteps.forEach { it.draw(drawer = drawer) }
        if (closed) drawer.closePath()
        drawer.stroke()
    }

    companion object {
        fun initBuilder(layer: Layer, color: Color = Color.BY_LAYER): LwPolylineBuilderStep1 {
            return LwPolylineBuilder.init(layer = layer, color = color)
        }

        fun rectangle(
            layer: Layer,
            color: Color = Color.BY_LAYER,
            startPoint: Vetor2D,
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