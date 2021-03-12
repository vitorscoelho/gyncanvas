package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.ShapeType
import vitorscoelho.gyncanvas.core.primitives.Primitive

interface CompositeEntity : Entity {
    override val shapeType: ShapeType
        get() = ShapeType.NONE

    val entities: List<Entity>

    override val primitives: List<Primitive> get() = entities.flatMap { it.primitives }
}