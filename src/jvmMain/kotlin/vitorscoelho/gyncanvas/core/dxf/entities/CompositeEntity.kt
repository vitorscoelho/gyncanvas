package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.ShapeType

interface CompositeEntity : Entity {
    override val shapeType: ShapeType
        get() = ShapeType.NONE

    val entities: List<Entity>

    override fun draw(drawer: Drawer) {
        entities.forEach { it.draw(drawer = drawer) }
    }
}