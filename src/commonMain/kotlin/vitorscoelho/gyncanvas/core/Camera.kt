package vitorscoelho.gyncanvas.core

import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix

class Camera {
    var xCenter: Double = 0.0
        private set
    var yCenter: Double = 0.0
        private set
    var zoom: Double = 0.0
        private set

    private val _transformationMatrix = MutableTransformationMatrix()
    val transformationMatrix: TransformationMatrix = _transformationMatrix

    fun setPosition(x: Double, y: Double) {
        _transformationMatrix
            .identity()
            .translate(xOffset = x, yOffset = y)

    }
//    abstract fun setPosition(x: Double, y: Double, zoom: Double)
//    abstract fun translate(deltaX: Double = 0.0, deltaY: Double = 0.0)
//
//    abstract fun appendZoom(factor: Double, xTarget: Double, yTarget: Double)
//    abstract fun zoomWindow(x1: Double, y1: Double, x2: Double, y2: Double)
//
//    //    abstract fun coordinates(): Vector2D
//    abstract fun worldCoordinates(xCamera: Double, yCamera: Double): Vector2D
//
//    abstract fun worldDistance(distCamera: Double): Double
}