package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.blocks.Block
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vector2D

data class Insert(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    val block: Block,
    val insertionPoint: Vector2D,
    private val scaleFactor: Double = 1.0,
    val rotationAngle: Double = 0.0
) : CompositeEntity {
    override val entities: List<Entity> = TODO("Ainda não implementou as transformações")
    val xScaleFactor: Double
        get() = scaleFactor
    val yScaleFactor: Double
        get() = scaleFactor

    init {
        val transformationMatrix: TransformationMatrix = TransformationMatrix.IDENTITY
            .translate(tx = insertionPoint.x, ty = insertionPoint.y)
            .scale(factor = scaleFactor, xOrigin = 0.0, yOrigin = 0.0)
            .rotate(angle = rotationAngle)
        //TODO Tem que implementar as transformações pra tornar isso possível
//        this.entities = block.entities.map { it.transform(transformationMatrix) }
        this.entities = emptyList()//TODO Remover isto depois de implementar as transformações
        TODO("Ainda não implementou as transformações")
    }
}