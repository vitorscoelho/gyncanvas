package vitorscoelho.gyncanvas.core.dxf

import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import tornadofx.fitToParentSize
import vitorscoelho.gyncanvas.core.dxf.entities.CompositeEntity
import vitorscoelho.gyncanvas.core.dxf.entities.Entity
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix

abstract class DrawingArea(
    val backgroundColor: Color = Color.INDEX_250
) {
    protected abstract val drawer: Drawer
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

    fun draw(transformationMatrix: TransformationMatrix) {
        resetTransform()
        drawer.fill = backgroundColor
        drawer.fillBackground()
        drawer.copyToTransform(transformationMatrix)
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

    private fun resetTransform() {
        drawer.copyToTransform(TransformationMatrix.IDENTITY)
    }
}

class FXDrawingArea(
    backgroundColor: Color = Color.INDEX_250
) : DrawingArea(backgroundColor = backgroundColor) {
    private val canvas = Canvas()
    protected override val drawer = FXDrawer(canvas = canvas)
    val node: Node

    init {
        node = Pane(canvas)
        this.node.parentProperty().addListener {/*observable, oldValue, newValue*/ _, _, _ ->
            this.node.fitToParentSize()
        }
        canvas.widthProperty().bind(node.widthProperty())
        canvas.heightProperty().bind(node.heightProperty())
    }
}