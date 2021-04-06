package vitorscoelho.gyncanvas.math

import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Converts an angle measured in degrees to an approximately
 * equivalent angle measured in radians.  The conversion from
 * degrees to radians is generally inexact.
 * @param   angdeg   an angle, in degrees
 * @return  the measurement of the angle [angdeg] in radians.
 */
fun toRadians(angdeg: Double) = angdeg * PI / 180.0

/**
 * Converts an angle measured in radians to an approximately
 * equivalent angle measured in degrees.  The conversion from
 * radians to degrees is generally inexact; users should
 * *not* expect **`cos(toRadians(90.0))`** to exactly
 * equal **`0.0`**.
 * @param   angrad   an angle, in radians
 * @return  the measurement of the angle [angrad] in degrees.
 */
fun toDegrees(angrad: Double) = angrad * 180.0 / PI

/**
 * @property bulge é a tangente de um quarto do ângulo para o segmento de arco. Negativo se a orientação do arco é no sentido horário do [startPoint] para o [endPoint]. Um bulge igual a zero indica uma reta e bulge igual a 1 indica um semicírculo
 */
class Arc private constructor(
    val startPoint: Vector2D,
    val endPoint: Vector2D,
    val bulge: Double,
    val centerPoint: Vector2D,
    val radius: Double,
    val startAngle: Double,
    val endAngle: Double
) {
    /**
     * Distância angular percorrida pelo arco, em radianos.
     * Não é, necessariamente, a menor distância angular entre [startPoint] e [endPoint]
     */
    val angularDistance: Double
        get() {
//            val minDist = (endAngle - startAngle).absoluteValue
//            if (endAngle > startAngle && bulge > 0.0) {
//                return endAngle - startAngle
//            }
//            if (endAngle > startAngle && bulge < 0.0) {
//                return 2.0 * PI - (endAngle - startAngle)
//            }
//            if (endAngle < startAngle && bulge > 0.0) {
//                return 2.0 * PI - (startAngle - endAngle)
//            }
//            if (endAngle < startAngle && bulge > 0.0) {
//                return startAngle - endAngle
//            }
            if (bulge == 0.0) return Double.POSITIVE_INFINITY
            val minDist = (endAngle - startAngle).absoluteValue
            val isClockWise = bulge < 0.0
            if (
                (startAngle > endAngle && isClockWise) ||
                (endAngle > startAngle && !isClockWise)
            ) {
                return minDist
            }
            return 2.0 * PI - minDist
        }

    val length: Double get() = radius * angularDistance
    /*
    TODO criar testes unitários com situações como estas:
        val c1 = centerPointArc(
            startPoint = Vector2D.ZERO,
            endPoint = Vector2D(30.0, 0.0),
            bulge = 200.0,
        )
        val c2 = centerPointArc(
            startPoint = Vector2D(-8.0, 4.5),
            endPoint = Vector2D(30.0, -20.0),
            bulge = -0.47
        )
     */

    companion object {
        /**
         * Solução adaptada de:
         * https://math.stackexchange.com/questions/1337344/get-third-point-from-an-arc-constructed-by-start-point-end-point-and-bulge
         */
        fun create(startPoint: Vector2D, endPoint: Vector2D, bulge: Double): Arc {
            //1
            val startToChordMiddleVec: Vector2D = endPoint - startPoint
            val chordLength: Double = startToChordMiddleVec.norm
            //2
            val sagittaLength: Double = bulge.absoluteValue * chordLength / 2.0
            val radius: Double = chordLength * (bulge * bulge + 1.0) / (4.0 * bulge.absoluteValue)
            val deltaLengthRadiusSagitta = sagittaLength - radius
            //3
            //3.1
            val chordMiddleToArcMiddleVec = startToChordMiddleVec.rotate(bulge.sign * PI / 2.0)
            //4
            val centerPoint =
                Vector2D.mid(startPoint, endPoint) - chordMiddleToArcMiddleVec.normalized(deltaLengthRadiusSagitta)

            /**Posições dos pontos em relação ao centro*/
            val deltaStartPoint = startPoint - centerPoint
            val deltaEndPoint = endPoint - centerPoint
            val xAxis = Vector2D.X_AXIS
            val startAngle = xAxis.angle(deltaStartPoint)
            val endAngle = xAxis.angle(deltaEndPoint)
            return Arc(
                startPoint = startPoint, endPoint = endPoint,
                bulge = bulge,
                centerPoint = centerPoint,
                radius = radius,
                startAngle = startAngle, endAngle = endAngle
            )
        }
    }
}
