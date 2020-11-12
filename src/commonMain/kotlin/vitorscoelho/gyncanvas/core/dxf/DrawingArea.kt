package vitorscoelho.gyncanvas.core.dxf

import vitorscoelho.gyncanvas.core.dxf.entities.CompositeEntity
import vitorscoelho.gyncanvas.core.dxf.entities.Entity

abstract class DrawingArea(
    val backgroundColor: Color = Color.INDEX_250
) {
    protected abstract val drawer: Drawer
    val camera: Camera
        get() = drawer.camera
    private val entities = mutableListOf<Entity>()

    fun addEntity(entity: Entity) {
        entities += entity
    }

    fun addEntities(entities: Iterable<Entity>) {
        entities.forEach { addEntity(entity = it) }
    }

    fun removeEntity(entity: Entity) {
        entities -= entity
    }

    fun removeEntities(entity: Entity) {
        entities.forEach { addEntity(entity = it) }
    }

    fun removeEntity(index: Int) {
        entities.removeAt(index)
    }

    fun removeAllEntities() {
        entities.clear()
    }

    fun draw() {
        drawer.apllyCameraTransform()
        drawer.fill = backgroundColor
        drawer.fillBackground()
        entities.forEach { entity -> drawEntity(entity) }
    }

    private fun drawEntity(entity: Entity) {
        if (entity is CompositeEntity) {
            entity.entities.forEach { drawEntity(it) }
        } else {
            entity.shapeType.applyColor(drawer = drawer, entity = entity)
            entity.draw(drawer = drawer)
        }//TODO dar um jeito de remover esse 'if'
    }

//    private fun resetTransform() {
//        drawer.copyToTransform(TransformationMatrix.IDENTITY)
//    }
}