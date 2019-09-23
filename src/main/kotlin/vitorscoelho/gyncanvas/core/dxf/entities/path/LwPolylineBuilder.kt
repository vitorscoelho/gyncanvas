package vitorscoelho.gyncanvas.core.dxf.entities.path

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.entities.LwPolyline
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.math.Vetor2D

interface LwPolylineBuilderStep1 {
    fun startPoint(point: Vetor2D): LwPolylineBuilderStep2
}

interface LwPolylineBuilderStep2 {
    fun lineTo(point: Vetor2D): LwPolylineBuilderStep2
    fun lineTo(x: Double, y: Double): LwPolylineBuilderStep2
    fun deltaLineTo(deltaX: Double = 0.0, deltaY: Double = 0.0): LwPolylineBuilderStep2
    fun arcTo(tangentPoint1: Vetor2D, tangentPoint2: Vetor2D, radius: Double): LwPolylineBuilderStep2
    fun arcTo(
        xTangent1: Double,
        yTangent1: Double,
        xTangent2: Double,
        yTangent2: Double,
        radius: Double
    ): LwPolylineBuilderStep2

    fun deltaArcTo(
        deltaXTangent1: Double,
        deltaYTangent1: Double,
        deltaXTangent2: Double,
        deltaYTangent2: Double,
        radius: Double
    ): LwPolylineBuilderStep2

    fun build(): LwPolyline
    fun closeAndBuild(): LwPolyline
}

class LwPolylineBuilder private constructor(val layer: Layer, val color: Color) :
    LwPolylineBuilderStep1,
    LwPolylineBuilderStep2 {
    private val pathSteps = mutableListOf<PathStep>()
    private var pontoFinalAtual: Vetor2D = Vetor2D.ZERO

    override fun startPoint(point: Vetor2D): LwPolylineBuilderStep2 {
        pontoFinalAtual = point
        pathSteps += MoveTo(point = point)
        return this
    }

    override fun lineTo(point: Vetor2D): LwPolylineBuilderStep2 {
        pontoFinalAtual = point
        pathSteps += LineTo(point = point)
        return this
    }

    override fun lineTo(x: Double, y: Double): LwPolylineBuilderStep2 =
        lineTo(point = Vetor2D(x = x, y = y))


    override fun deltaLineTo(deltaX: Double, deltaY: Double): LwPolylineBuilderStep2 =
        lineTo(point = pontoFinalAtual.somar(deltaX = deltaX, deltaY = deltaY))

    override fun arcTo(tangentPoint1: Vetor2D, tangentPoint2: Vetor2D, radius: Double): LwPolylineBuilderStep2 {
        pontoFinalAtual = tangentPoint2
        pathSteps += ArcTo(tangentPoint1 = tangentPoint1, tangentPoint2 = tangentPoint2, radius = radius)
        return this
    }

    override fun arcTo(
        xTangent1: Double,
        yTangent1: Double,
        xTangent2: Double,
        yTangent2: Double,
        radius: Double
    ): LwPolylineBuilderStep2 =
        arcTo(
            tangentPoint1 = Vetor2D(x = xTangent1, y = yTangent1),
            tangentPoint2 = Vetor2D(x = xTangent2, y = yTangent2),
            radius = radius
        )

    override fun deltaArcTo(
        deltaXTangent1: Double,
        deltaYTangent1: Double,
        deltaXTangent2: Double,
        deltaYTangent2: Double,
        radius: Double
    ): LwPolylineBuilderStep2 =
        arcTo(
            tangentPoint1 = pontoFinalAtual.somar(deltaX = deltaXTangent1, deltaY = deltaYTangent1),
            tangentPoint2 = pontoFinalAtual.somar(deltaX = deltaXTangent2, deltaY = deltaYTangent2),
            radius = radius
        )

    override fun build(): LwPolyline =
        LwPolyline(layer = layer, color = color, closed = false, pathSteps = pathSteps)


    override fun closeAndBuild(): LwPolyline =
        LwPolyline(layer = layer, color = color, closed = true, pathSteps = pathSteps)

    companion object {
        internal fun init(layer: Layer, color: Color): LwPolylineBuilderStep1 {
            return LwPolylineBuilder(layer = layer, color = color)
        }
    }
}