package vitorscoelho.gyncanvas.core.dxf

import javafx.scene.canvas.Canvas
import javafx.scene.transform.Affine
import org.joml.Matrix4d
import org.joml.Vector3d
import tornadofx.onChange
import vitorscoelho.gyncanvas.math.Vector2D
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.max

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

class FXCamera internal constructor(private val canvas: Canvas) : Camera() {
    private val jomlMatrix = Matrix4d()
    private val jomlScaleVector = Vector3d()

    override var xCenter: Double = 0.0
        protected set
        get() = (xMax - xMin) / 2.0
    override var yCenter: Double = 0.0
        protected set
        get() = (yMax - yMin) / 2.0
    override var zoom: Double = 1.0
        protected set
        get() {
            jomlMatrix.getScale(jomlScaleVector)
            return jomlScaleVector.z
        }

    var xMin: Double = 0.0
        private set
    var yMin: Double = 0.0
        private set
    var xMax: Double = 0.0
        private set
    var yMax: Double = 0.0
        private set

    init {
        canvas.widthProperty().onChange { updateMinMaxCoordinates() }
        canvas.heightProperty().onChange { updateMinMaxCoordinates() }
    }

    private fun updateMinMaxCoordinates() {
        val bottomLeft = worldCoordinates(xCamera = 0.0, yCamera = canvas.height)
        val upperRight = worldCoordinates(xCamera = canvas.width, yCamera = 0.0)
        xMin = bottomLeft.x
        yMin = bottomLeft.y
        xMax = upperRight.x
        yMax = upperRight.y
    }

    override fun setPosition(x: Double, y: Double) {
        jomlMatrix.identity()
        updateMinMaxCoordinates() //É necessário atualizar os valores para se pegar o centro correto
        jomlMatrix.translate(xCenter - x, yCenter - y, 0.0)
        updateMinMaxCoordinates()
    }

    override fun setPosition(x: Double, y: Double, zoom: Double) {
        jomlMatrix.identity()
        updateMinMaxCoordinates() //É necessário atualizar os valores para se pegar o centro correto
        jomlMatrix.translate(xCenter - x, yCenter - y, 0.0)
        jomlMatrix.scaleAround(zoom, x, -y, 0.0)
        updateMinMaxCoordinates()
    }

    override fun zoomWindow(x1: Double, y1: Double, x2: Double, y2: Double) {
        if (x1 == x2 || y1 == y2) return
        val midPointZoom = Vector2D(x = (x1 + x2) / 2.0, y = (y1 + y2) / 2.0)
        val deltaZoomWindow = Vector2D(x = (x1 - x2).absoluteValue, y = (y1 - y2).absoluteValue)
        val deltaZoomAtual = Vector2D(x = this.xMax - this.xMin, y = this.yMax - this.yMin)
        val appendZoom = min(deltaZoomAtual.x / deltaZoomWindow.x, deltaZoomAtual.y / deltaZoomWindow.y)
        setPosition(x = midPointZoom.x, y = midPointZoom.y, zoom = this.zoom * appendZoom)
        translate()
    }

    override fun translate(deltaX: Double, deltaY: Double) {
        jomlMatrix.translate(-deltaX, deltaY, 0.0)
        updateMinMaxCoordinates()
    }

    override fun appendZoom(factor: Double, xTarget: Double, yTarget: Double) {
        jomlMatrix.scaleAround(factor, xTarget, -yTarget, 0.0)
        updateMinMaxCoordinates()
    }

    internal fun toAffine(): Affine = with(jomlMatrix) {
        Affine(
            m00(), m10(), m20(), m30(),
            m01(), m11(), m21(), m31(),
            m02(), m12(), m22(), m32()
        )
    }

    /**
     * @param xCamera abscissa do ponto que se deseja analisar as distâncias
     * @param yCamera ordenada do ponto que se deseja analisar as distâncias
     * @param maxDistance distância máxima de captura, em pixel
     * @param points pontos que terão a distância analisada
     */
    fun nearestPoint(xCamera: Double, yCamera: Double, maxDistance: Double, points: List<Vector2D>): Vector2D? {
        val maxDistanceWorld = maxDistance / zoom
        val detectionPoint = worldCoordinates(xCamera = xCamera, yCamera = yCamera)
        return points.filter {
            it.x >= xMin && it.x <= xMax
                    && it.y >= yMin && yCenter <= yMax
                    && Vector2D.distance(it, detectionPoint) <= maxDistanceWorld
        }.minBy { Vector2D.distance(it, detectionPoint) }
    }

    override fun worldCoordinates(xCamera: Double, yCamera: Double): Vector2D {
        val vetorJOML = Vector3d(xCamera, yCamera, 0.0)
        val matrizInversa = Matrix4d(jomlMatrix).invert()
        matrizInversa.transformPosition(vetorJOML)
        return Vector2D(x = vetorJOML.x, y = -vetorJOML.y)
    }

    override fun worldDistance(distCamera: Double) = distCamera / zoom
}