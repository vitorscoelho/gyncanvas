package vitorscoelho.gyncanvas.math

import kotlin.math.PI

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