package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.primitives.Primitive

interface CompositeEntity : Entity {
    val entities: List<Entity>

    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        entities.forEach { entity -> entity.forEachPrimitive(action) }
    }
}