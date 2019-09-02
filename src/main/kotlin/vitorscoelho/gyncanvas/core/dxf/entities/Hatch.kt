package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.entities.path.PathStep

class Hatch internal constructor(
    override val properties: EntityProperties,
    internal val pathSteps: List<PathStep>
) : Entity {
    override fun draw(drawer: Drawer) {
        drawer.beginPath()
        pathSteps.forEach { it.draw(drawer = drawer) }
        drawer.closePath()
        drawer.fill()
    }

    companion object {
        fun fromLwPolyline(properties: EntityProperties, lwPolyline: LwPolyline): Hatch =
            Hatch(properties = properties, pathSteps = lwPolyline.pathSteps)
    }
}