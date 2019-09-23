package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix

data class Hatch internal constructor(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    internal val pathSteps: List<PathStep>
) : Entity {
    override fun draw(drawer: Drawer) {
        applyColor(drawer = drawer, layer = layer, color = color)
        drawer.beginPath()
        pathSteps.forEach { it.draw(drawer = drawer) }
        drawer.closePath()
        drawer.fill()
    }

    override fun transform(transformationMatrix: TransformationMatrix): Hatch =
        copy(pathSteps = pathSteps.map { it.transform(transformationMatrix) })

    companion object {
        fun fromLwPolyline(layer: Layer, color: Color = Color.BY_LAYER, lwPolyline: LwPolyline): Hatch =
            Hatch(layer = layer, color = color, pathSteps = lwPolyline.pathSteps)
    }
}