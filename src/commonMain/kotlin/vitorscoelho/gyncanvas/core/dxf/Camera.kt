package vitorscoelho.gyncanvas.core.dxf

import vitorscoelho.gyncanvas.math.Vector2D

abstract class Camera {
    abstract var xCenter: Double
        protected set
    abstract var yCenter: Double
        protected set
    abstract var zoom: Double
        protected set

    abstract fun setPosition(x: Double, y: Double)
    abstract fun setPosition(x: Double, y: Double, zoom: Double)
    abstract fun translate(deltaX: Double = 0.0, deltaY: Double = 0.0)

    abstract fun appendZoom(factor: Double, xTarget: Double, yTarget: Double)
    abstract fun zoomWindow(x1: Double, y1: Double, x2: Double, y2: Double)
    //    abstract fun coordinates(): Vector2D
    abstract fun worldCoordinates(xCamera: Double, yCamera: Double): Vector2D

    abstract fun worldDistance(distCamera: Double): Double
}