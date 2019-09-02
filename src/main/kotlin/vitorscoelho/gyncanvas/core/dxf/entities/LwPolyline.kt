package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilder
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilderStep1
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep
import vitorscoelho.gyncanvas.math.Vetor2D

class LwPolyline internal constructor(
    override val properties: EntityProperties,
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
        fun initBuilder(properties: EntityProperties): LwPolylineBuilderStep1 {
            return LwPolylineBuilder.init(properties = properties)
        }

        fun rectangle(
            properties: EntityProperties,
            startPoint: Vetor2D,
            deltaX: Double,
            deltaY: Double
        ): LwPolyline {
            return initBuilder(properties = properties)
                .startPoint(startPoint)
                .deltaLineTo(deltaX = deltaX)
                .deltaLineTo(deltaY = deltaY)
                .deltaLineTo(deltaX = -deltaX)
                .closeAndBuild()
        }
    }
}