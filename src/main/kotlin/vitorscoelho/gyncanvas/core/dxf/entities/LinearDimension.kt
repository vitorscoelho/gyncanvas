package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.DimStyleOverrides
import vitorscoelho.gyncanvas.core.dxf.tables.DimStyle
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vetor2D
import java.lang.Math.toRadians
import kotlin.math.absoluteValue
import kotlin.math.atan

interface LinearDimension : Dimension {
    val angle: Double
    val xPoint1: Vetor2D
    val xPoint2: Vetor2D

    fun dimContinue(point: Vetor2D): LinearDimension = dimContinueXPoint2(point)
    fun dimContinueXPoint1(point: Vetor2D): LinearDimension
    fun dimContinueXPoint2(point: Vetor2D): LinearDimension

    fun createSequence(points: List<Vetor2D>): List<LinearDimension>
    fun initSequence(): LinearDimensionSequence

    //PRIMARY UNITS PROP
    //Propriedade: LinearDimensions
    val linearDimensionPrecision: Int
        get() = dimStyleOverrides.linearDimensionPrecision(dimStyle)
    val decimalSeparator: Char
        get() = dimStyleOverrides.decimalSeparator(dimStyle)
    val unitRound: Double
        get() = dimStyleOverrides.unitRound(dimStyle)
    val prefix: String
        get() = dimStyleOverrides.prefix(dimStyle)
    val suffix: String
        get() = dimStyleOverrides.suffix(dimStyle)
    val scaleFactor: Double
        get() = dimStyleOverrides.scaleFactor(dimStyle)
    val linearDimensionSuppressLeadingZeros: Boolean
        get() = dimStyleOverrides.linearDimensionSuppressLeadingZeros(dimStyle)
    val linearDimensionSuppressTrailingZeros: Boolean
        get() = dimStyleOverrides.linearDimensionSuppressTrailingZeros(dimStyle)
}

interface LinearDimensionSequenceStart {
    fun firstPoint(point: Vetor2D): LinearDimensionSequence
}

interface LinearDimensionSequence {
    fun next(point: Vetor2D): LinearDimensionSequence
    fun next(x: Double, y: Double): LinearDimensionSequence
    fun nextWithDelta(deltaX: Double, deltaY: Double): LinearDimensionSequence
    fun toList(): List<LinearDimension>
}