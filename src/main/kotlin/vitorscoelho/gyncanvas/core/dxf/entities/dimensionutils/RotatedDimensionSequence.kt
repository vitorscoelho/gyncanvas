package vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.DimStyleOverrides
import vitorscoelho.gyncanvas.core.dxf.entities.LinearDimensionSequence
import vitorscoelho.gyncanvas.core.dxf.entities.LinearDimensionSequenceStart
import vitorscoelho.gyncanvas.core.dxf.entities.RotatedDimension
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vector2D

interface RotatedDimensionSequenceStart : LinearDimensionSequenceStart {
    override fun firstPoint(point: Vector2D): RotatedDimensionSequence
}

interface RotatedDimensionSequence : LinearDimensionSequence {
    override fun next(point: Vector2D): RotatedDimensionSequence
    override fun next(x: Double, y: Double): RotatedDimensionSequence
    override fun nextWithDelta(deltaX: Double, deltaY: Double): RotatedDimensionSequence
    override fun toList(): List<RotatedDimension>

    companion object {
        fun init(
            layer: Layer, color: Color,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides,
            dimensionLineReferencePoint: Vector2D,
            angle: Double, text: String = "<>"
        ): RotatedDimensionSequenceStart = RotatedDimensionSequenceImplementation(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            dimensionLineReferencePoint = dimensionLineReferencePoint,
            angle = angle, text = text
        )
    }
}

private class RotatedDimensionSequenceImplementation(
    val layer: Layer,
    val color: Color,
    val dimStyle: DimStyle,
    val dimStyleOverrides: DimStyleOverrides,
    val dimensionLineReferencePoint: Vector2D,
    val angle: Double,
    val text: String = "<>"
) : RotatedDimensionSequenceStart, RotatedDimensionSequence {
    private val points = mutableListOf<Vector2D>()

    override fun firstPoint(point: Vector2D) = next(point)

    override fun next(point: Vector2D): RotatedDimensionSequence {
        points.add(point)
        return this
    }

    override fun next(x: Double, y: Double) = next(Vector2D(x = x, y = y))

    override fun nextWithDelta(deltaX: Double, deltaY: Double) = next(
        x = points.last().x + deltaX,
        y = points.last().y + deltaY
    )

    override fun toList(): List<RotatedDimension> {
        if (points.size < 2) return emptyList()
        return (1..points.lastIndex).map { index ->
            RotatedDimension(
                layer = layer, color = color, dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
                xPoint1 = points[index - 1], xPoint2 = points[index],
                dimensionLineReferencePoint = dimensionLineReferencePoint, angle = angle,
                text = text
            )
        }
    }
}