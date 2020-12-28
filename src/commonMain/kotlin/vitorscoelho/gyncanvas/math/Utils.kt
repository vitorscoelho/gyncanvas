package vitorscoelho.gyncanvas.math

import com.soywiz.korma.geom.Angle

/**
 * Converts an angle measured in degrees to an approximately
 * equivalent angle measured in radians.  The conversion from
 * degrees to radians is generally inexact.
 * @param   angdeg   an angle, in degrees
 * @return  the measurement of the angle [angdeg] in radians.
 */
fun degreesToRadians(angdeg: Double) = Angle.degreesToRadians(angdeg)

/**
 * Converts an angle measured in radians to an approximately
 * equivalent angle measured in degrees.  The conversion from
 * radians to degrees is generally inexact; users should
 * *not* expect **`cos(toRadians(90.0))`** to exactly
 * equal **`0.0`**.
 * @param   angrad   an angle, in radians
 * @return  the measurement of the angle [angrad] in degrees.
 */
fun radiansToDegrees(angrad: Double) = Angle.radiansToDegrees(angrad)