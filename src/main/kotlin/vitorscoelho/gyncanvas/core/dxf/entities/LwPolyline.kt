package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilder
import vitorscoelho.gyncanvas.core.dxf.entities.path.LwPolylineBuilderStep1
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vetor2D

data class LwPolyline internal constructor(
    override val properties: EntityProperties,
    val closed: Boolean,
    internal val pathSteps: List<PathStep>
) : Entity {
    override fun applyProperties(drawer: Drawer) {
        applyLineWidth(drawer = drawer)
        applyColor(drawer = drawer, layer = layer, color = color)
    }

    override fun draw(drawer: Drawer) {
        drawer.beginPath()
        pathSteps.forEach { it.draw(drawer = drawer) }
        if (closed) drawer.closePath()
        drawer.stroke()
    }

    override fun transform(transformationMatrix: TransformationMatrix): LwPolyline =
        copy(pathSteps = pathSteps.map { it.transform(transformationMatrix) })

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