package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.blocks.Block
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vetor2D

data class Insert(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    val block: Block,
    val insertionPoint: Vetor2D,
    private val scaleFactor: Double = 1.0,
    val rotationAngle: Double = 0.0
) : CompositeEntity {
    override val entities: List<Entity>
    val xScaleFactor: Double
        get() = scaleFactor
    val yScaleFactor: Double
        get() = scaleFactor

    init {
        val transformationMatrix: TransformationMatrix = MutableTransformationMatrix()
            .scale(factor = scaleFactor, xOrigin = 0.0, yOrigin = 0.0)
            .translate(xOffset = insertionPoint.x, yOffset = insertionPoint.y)
        this.entities = block.entities.map { it.transform(transformationMatrix) }
    }

    override fun transform(transformationMatrix: TransformationMatrix): Insert =
        copy(
            insertionPoint = insertionPoint.transform(transformationMatrix),
            scaleFactor = scaleFactor * transformationMatrix.scale
//            rotationAngle = rotationAngle TODO
        )
}