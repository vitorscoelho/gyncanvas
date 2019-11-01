package vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.DimStyleOverrides
import vitorscoelho.gyncanvas.core.dxf.entities.RotatedDimension
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vector2D

interface RotatedDimensionSequenceStart {
    fun firstPoint(point: Vector2D): RotatedDimensionSequence
    fun firstPoint(x: Double, y: Double): RotatedDimensionSequence
}

class RotatedDimensionSequence private constructor(
    val layer: Layer,
    val color: Color,
    val dimStyle: DimStyle,
    val dimStyleOverrides: DimStyleOverrides,
    val dimensionLineReferencePoint: Vector2D,
    val angle: Double,
    val text: String = "<>"
) : RotatedDimensionSequenceStart {
    private val points = mutableListOf<Vector2D>()

    override fun firstPoint(point: Vector2D) = next(point)
    override fun firstPoint(x: Double, y: Double) = next(x = x, y = y)

    fun next(point: Vector2D): RotatedDimensionSequence {
        points.add(point)
        return this
    }

    fun next(x: Double, y: Double) = next(Vector2D(x = x, y = y))

    fun nextDelta(deltaX: Double = 0.0, deltaY: Double = 0.0) = next(
        x = points.last().x + deltaX,
        y = points.last().y + deltaY
    )

    fun toList(): List<RotatedDimension> {
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

    companion object {
        fun init(
            layer: Layer, color: Color,
            dimStyle: DimStyle, dimStyleOverrides: DimStyleOverrides,
            dimensionLineReferencePoint: Vector2D,
            angle: Double, text: String = "<>"
        ): RotatedDimensionSequenceStart = RotatedDimensionSequence(
            layer = layer, color = color,
            dimStyle = dimStyle, dimStyleOverrides = dimStyleOverrides,
            dimensionLineReferencePoint = dimensionLineReferencePoint,
            angle = angle, text = text
        )
    }
}