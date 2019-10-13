package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.DimStyleOverrides
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vetor2D
import kotlin.math.atan

data class AlignedDimension(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    override val dimStyle: DimStyle,
    override val dimStyleOverrides: DimStyleOverrides = DimStyleOverrides.NONE,
    override val xPoint1: Vetor2D,
    override val xPoint2: Vetor2D,
    private val dimensionLineReferencePoint: Vetor2D,
    override val text: String = "<>"
) : LinearDimension by RotatedDimension(
    layer = layer,
    color = color,
    dimStyle = dimStyle,
    dimStyleOverrides = dimStyleOverrides,
    xPoint1 = xPoint1,
    xPoint2 = xPoint2,
    dimensionLineReferencePoint = dimensionLineReferencePoint,
    text = text,
    angle = run {
        val points = arrayOf(xPoint1, xPoint2)
        val rightPoint: Vetor2D = points.maxBy { it.x }!!
        val leftPoint: Vetor2D = points.minBy { it.x }!!
        val delta = rightPoint - leftPoint
        if (delta.x == 0.0) return@run Math.toRadians(90.0)
        return@run atan(delta.y / delta.x)
    }
) {
    override fun dimContinue(point: Vetor2D): AlignedDimension = dimContinueXPoint2(point)

    override fun dimContinueXPoint1(point: Vetor2D): AlignedDimension {
        return copy(xPoint1 = this.xPoint1, xPoint2 = point)
    }

    override fun dimContinueXPoint2(point: Vetor2D): AlignedDimension {
        return copy(xPoint1 = this.xPoint2, xPoint2 = point)
    }

    override fun createSequence(points: List<Vetor2D>): List<AlignedDimension> {
        val dimensions = mutableListOf(this)
        points.forEach { dimensions.add(dimensions.last().dimContinue(it)) }
        return dimensions
    }

    override fun initSequence(): LinearDimensionSequence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}