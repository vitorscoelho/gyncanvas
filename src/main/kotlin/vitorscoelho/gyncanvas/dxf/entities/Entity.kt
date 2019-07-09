package vitorscoelho.gyncanvas.dxf.entities

import com.beust.klaxon.Json

class Point2d(
    var x: Double = 0.0,
    var y: Double = 0.0
)

class Point3d(
    var x: Double = 0.0,
    var y: Double = 0.0
)

interface Entity {
    var Layer: String?
}
/*
Line
Polyline
Circle
Text
Dimension
 */

class Line(
    var StartPoint: Point3d? = null,
    var EndPoint: Point3d? = null,
    override var Layer: String? = null
) : Entity

/*
int index, Point2d pt, double bulge, double startWidth, double endWidth
 */
class Polyline(
    var vertexes: MutableList<PolylineVertex> = mutableListOf(),
    var Closed: Boolean? = null,
    override var Layer: String? = null
) : Entity

class PolylineVertex(var point: Point2d, var bulge: Double, var startWidth: Double, val endWidth: Double)

class Circle(
    var Center: Point3d? = null,
    var Radius: Double? = null,
    override var Layer: String? = null
) : Entity {
    @Json(ignored = true)
    var Diameter: Double?
        get() {
            return when (Radius) {
                null -> null
                else -> 2.0 * Radius!!
            }
        }
        set(value) {
            Radius = when (value) {
                null -> null
                else -> Radius!! / 2.0
            }
        }
}

class MText(
    var Location: Point3d? = null,
//    var Text: String? = null,
    var Contents: String? = null,
    var Rotation: Double? = null,
    override var Layer: String? = null
) : Entity
