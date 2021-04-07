package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.entities.dimensionutils.RotatedDimensionSequence
import vitorscoelho.gyncanvas.math.Vector

interface LinearDimension : Dimension {
    val angle: Double
    val xPoint1: Vector
    val xPoint2: Vector

    fun dimContinue(point: Vector): LinearDimension = dimContinueXPoint2(point)
    fun dimContinueXPoint1(point: Vector): LinearDimension
    fun dimContinueXPoint2(point: Vector): LinearDimension

    fun initSequence(): RotatedDimensionSequence
    fun createSequence(points: List<Vector>): List<LinearDimension>

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