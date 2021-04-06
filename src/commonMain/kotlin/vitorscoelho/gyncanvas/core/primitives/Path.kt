package vitorscoelho.gyncanvas.core.primitives

import vitorscoelho.gyncanvas.math.Arc
import vitorscoelho.gyncanvas.math.Vector
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.math.*

private fun linearPointsArc(
    startPoint: Vector2D,
    endPoint: Vector2D,
    bulge: Double,
    minAngleDiscretization: Double,
    minNSegments: Int
): List<Vector> {
    if (bulge == 0.0) return listOf(endPoint)
    val arc = Arc.create(startPoint = startPoint, endPoint = endPoint, bulge = bulge)
    val nSegments = max(minNSegments, (ceil(arc.angularDistance / minAngleDiscretization) + 0.5f).toInt())
    val angularDelta = arc.bulge.sign * arc.angularDistance / nSegments
    return List(size = nSegments) { index ->
        val currentAngle = arc.startAngle + (index + 1) * angularDelta
        Vector2D(
            x = arc.radius * cos(currentAngle) + arc.centerPoint.x,
            y = arc.radius * sin(currentAngle) + arc.centerPoint.y
        )
    }
}

class Path internal constructor(val steps: List<PathStep>, val closed: Boolean) {

    /**
     * Retorna uma lista com pontos que formam o [Path] a partir de segmentos de reta
     */
    fun linearPoints(minAngleDiscretization: Double, minNSegments: Int): List<Vector> {
        require(minAngleDiscretization > 0.0)
        require(minNSegments > 0)
        if (steps.size <= 1) return emptyList()
        val list = mutableListOf<Vector>(steps.first().endPoint)
        for (index in 1..steps.lastIndex) {
            val currentStep = steps[index]
            if (currentStep is ArcTo) {
                list += linearPointsArc(
                    startPoint = steps[index - 1].endPoint as Vector2D, endPoint = currentStep.endPoint as Vector2D,
                    bulge = currentStep.bulge,
                    minAngleDiscretization = minAngleDiscretization, minNSegments = minNSegments
                )
            } else {
                list += currentStep.endPoint
            }
        }
        return list
    }

    companion object {
        fun withLines(points: List<Vector2D>, closed: Boolean = false): Path {
            if (points.size <= 1) return Path(steps = emptyList(), closed = false)
            val steps = listOf(MoveTo(points.first())) + points.subList(fromIndex = 1, toIndex = points.lastIndex)
                .map { point -> LineTo(point = point) }
            return Path(steps = steps, closed = closed)
        }

        fun initBuilder(startPoint: Vector2D): PathBuilder = PathBuilder(startPoint = startPoint)

        fun initBuilder(x: Double, y: Double) = initBuilder(Vector2D(x = x, y = y))
    }
}

sealed class PathStep {
    abstract val endPoint: Vector
}

class MoveTo(val point: Vector2D) : PathStep() {
    override val endPoint: Vector get() = point
}

class LineTo(val point: Vector2D) : PathStep() {
    override val endPoint: Vector get() = point
}

class ArcTo(val point: Vector2D, val bulge: Double) : PathStep() {
    override val endPoint: Vector get() = point
}

class PathBuilder internal constructor(startPoint: Vector2D) {
    private val pathSteps = mutableListOf<PathStep>(
        MoveTo(point = startPoint)
    )
    private var lastPoint: Vector2D = startPoint

    fun lineTo(point: Vector2D): PathBuilder {
        lastPoint = point
        pathSteps += LineTo(point = point)
        return this
    }

    fun lineTo(x: Double, y: Double): PathBuilder = lineTo(point = Vector2D(x = x, y = y))

    fun deltaLineTo(deltaX: Double = 0.0, deltaY: Double = 0.0): PathBuilder =
        lineTo(lastPoint.plus(deltaX = deltaX, deltaY = deltaY))

    fun arcTo(point: Vector2D, bulge: Double): PathBuilder {
        lastPoint = point
        pathSteps += ArcTo(point = point, bulge = bulge)
        return this
    }

    fun arcTo(x: Double, y: Double, bulge: Double): PathBuilder = arcTo(point = Vector2D(x = x, y = y), bulge = bulge)
    fun deltaArcTo(deltaX: Double = 0.0, deltaY: Double = 0.0, bulge: Double): PathBuilder =
        arcTo(point = lastPoint.plus(deltaX = deltaX, deltaY = deltaY), bulge = bulge)

    fun build(): Path = Path(steps = pathSteps, closed = false)
    fun closeAndBuild(): Path = Path(steps = pathSteps, closed = true)
}