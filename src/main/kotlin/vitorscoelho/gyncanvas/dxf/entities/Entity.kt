package vitorscoelho.gyncanvas.dxf.entities

import com.beust.klaxon.Json

class Point2d(
    var x: Double,
    var y: Double
)

class Point3d(
    var x: Double,
    var y: Double
)

interface Entity {
    var layer: String
}
/*
Line
Polyline
Circle
Text
Dimension
 */

class Line(
    var startPoint: Point3d,
    var endPoint: Point3d,
    override var layer: String
) : Entity

/*
int index, Point2d pt, double bulge, double startWidth, double endWidth
 */
class Polyline(
    var vertices: Array<Point2d>,
    var bulges: DoubleArray,
    var closed: Boolean,
    override var layer: String
) : Entity

class PolylineVertex(var point: Point2d, var bulge: Double, var startWidth: Double, val endWidth: Double)

class Circle(
    var center: Point3d,
    var radius: Double,
    override var layer: String
) : Entity {
    @Json(ignored = true)
    var diameter: Double
        get() = 2.0 * radius
        set(value) {
            radius = value / 2.0
        }
}

class MText(
    var location: Point3d,
//    var Text: String,
    var contents: String,
    var rotation: Double,
    override var layer: String
) : Entity
