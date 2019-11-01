package vitorscoelho.gyncanvas.core.dxf.blocks

import vitorscoelho.gyncanvas.core.dxf.entities.Entity

class Block(
    val name: String,
    val entities: List<Entity>,
    val description: String = "",
    val explodable: Boolean = true
) {
    companion object {
        val NONE: Block by lazy { Block(name = "_None", entities = emptyList()) }
    }
}