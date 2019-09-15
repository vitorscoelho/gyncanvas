package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer

interface CompositeEntity : Entity {
    val entities: List<Entity>

    override fun applyProperties(drawer: Drawer) {
        entities.forEach { it.applyProperties(drawer = drawer) }
    }

    override fun draw(drawer: Drawer) {
        entities.forEach { it.draw(drawer = drawer) }
    }
}