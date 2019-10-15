package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.math.Vector2D

interface LinearDimension : Dimension {
    val angle: Double
    val xPoint1: Vector2D
    val xPoint2: Vector2D

    fun dimContinue(point: Vector2D): LinearDimension = dimContinueXPoint2(point)
    fun dimContinueXPoint1(point: Vector2D): LinearDimension
    fun dimContinueXPoint2(point: Vector2D): LinearDimension

    fun createSequence(points: List<Vector2D>): List<LinearDimension>

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