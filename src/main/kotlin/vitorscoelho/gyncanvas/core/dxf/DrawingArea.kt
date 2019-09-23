package vitorscoelho.gyncanvas.core.dxf

import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import tornadofx.fitToParentSize
import vitorscoelho.gyncanvas.core.dxf.entities.CompositeEntity
import vitorscoelho.gyncanvas.core.dxf.entities.Entity
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix

interface DrawingArea {
    fun addEntity(entity: Entity)
    fun addEntities(entities: Iterable<Entity>)
    fun draw(transformationMatrix: TransformationMatrix)
}

class FXDrawingArea : DrawingArea {
    private val canvas = Canvas()
    private val drawer = FXDrawer(canvas = canvas)

    private val backgroundColor = Color.INDEX_250
    private val entities = mutableListOf<Entity>()

    val node: Node

    init {
        node = Pane(canvas)
        this.node.parentProperty().addListener {/*observable, oldValue, newValue*/ _, _, _ ->
            this.node.fitToParentSize()
        }
        canvas.widthProperty().bind(node.widthProperty())
        canvas.heightProperty().bind(node.heightProperty())
    }

    override fun addEntity(entity: Entity) {
        entities += entity
    }

    override fun addEntities(entities: Iterable<Entity>) {
        entities.forEach { addEntity(entity = it) }
    }

    override fun draw(transformationMatrix: TransformationMatrix) {
        resetTransform()
        drawer.fill = backgroundColor
        drawer.fillBackground()
        drawer.copyToTransform(transformationMatrix)
        entities.forEach { entity -> entity.draw(drawer = drawer) }
    }

    private fun resetTransform() {
        drawer.copyToTransform(TransformationMatrix.IDENTITY)
    }
}