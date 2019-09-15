package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix

data class Hatch internal constructor(
    override val properties: EntityProperties,
    internal val pathSteps: List<PathStep>
) : Entity {
    override fun applyProperties(drawer: Drawer) {
        applyColor(drawer = drawer, layer = layer, color = color)
    }

    override fun draw(drawer: Drawer) {
        drawer.beginPath()
        pathSteps.forEach { it.draw(drawer = drawer) }
        drawer.closePath()
        drawer.fill()
    }

    override fun transform(transformationMatrix: TransformationMatrix): Hatch =
        copy(pathSteps = pathSteps.map { it.transform(transformationMatrix) })

    companion object {
        fun fromLwPolyline(properties: EntityProperties, lwPolyline: LwPolyline): Hatch =
            Hatch(properties = properties, pathSteps = lwPolyline.pathSteps)
    }
}