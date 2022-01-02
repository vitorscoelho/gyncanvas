package vitorscoelho.gyncanvas

import kotlin.math.abs
import kotlin.math.min

/**
 * mxx, mxy, tx,
 * myx, myy, ty,
 *  0 ,  0 ,  1
 */
class OrthographicCamera2D(val drawingArea: DrawingArea) {
    var mxx: Float = 1f
        private set
    val mxy = 0f
    var tx = 0f
        private set
    val myx = 0f
    var myy = 1f
        private set
    var ty = 0f
        private set

    var left = -1f
        private set
    var right = 1f
        private set
    var bottom = -1f
        private set
    var top = 1f
        private set

    var xCenter: Float = 0f
        private set
        get() = 0.5f * (right + left)
    var yCenter: Float = 0f
        private set
        get() = 0.5f * (top + bottom)

    var zoom = -drawingArea.width / (left - right)
        private set

    init {
        setPosition(xCenter = 0f, yCenter = 0f, zoom = 1f)
    }

    private fun set(left: Float, right: Float, bottom: Float, top: Float) {
        require(right > left && top > bottom)
        mxx = 2f / (right - left)
        myy = 2f / (top - bottom)
        tx = -(right + left) / (right - left)
        ty = -(top + bottom) / (top - bottom)
        zoom = -drawingArea.width / (left - right)
        this.left = left
        this.right = right
        this.bottom = bottom
        this.top = top
    }

    fun setPosition(xCenter: Float, yCenter: Float, zoom: Float) {
        require(zoom > 0)
        val halfWidth = 0.5f * drawingArea.width / zoom
        val halfHeight = 0.5f * drawingArea.height / zoom
        set(
            left = xCenter - halfWidth,
            right = xCenter + halfWidth,
            bottom = yCenter - halfHeight,
            top = yCenter + halfHeight,
        )
    }

    fun translate(tx: Float = 0f, ty: Float = 0f) {
        setPosition(
            xCenter = xCenter + tx,
            yCenter = yCenter + ty,
            zoom = zoom
        )
    }

    fun appendZoom(pivotX: Float, pivotY: Float, zoomFactor: Float) {
        require(zoomFactor > 0f)
        val deltaLeft = (pivotX - left) / zoomFactor
        val deltaRight = (right - pivotX) / zoomFactor
        val deltaBottom = (pivotY - bottom) / zoomFactor
        val deltaTop = (top - pivotY) / zoomFactor
        set(
            left = pivotX - deltaLeft,
            right = pivotX + deltaRight,
            bottom = pivotY - deltaBottom,
            top = pivotY + deltaTop,
        )
    }

    fun zoomWindow(x1: Float, y1: Float, x2: Float, y2: Float) {
        val zoomX = drawingArea.width / abs(x2 - x1)
        val zoomY = drawingArea.height / abs(y2 - y1)
        setPosition(xCenter = (x1 + x2) / 2f, yCenter = (y1 + y2) / 2f, zoom = min(zoomX, zoomY))
    }

    fun xWorld(xCanvas: Float): Float {
        //TODO analisar o possível risco de a largura do canvas não estar atualizado na câmera
        val proporcao = xCanvas / drawingArea.width
        return (right - left) * proporcao + left
    }

    fun yWorld(yCanvas: Float): Float {
        //TODO analisar o possível risco de a altura do canvas não estar atualizado na câmera
        val proporcao = 1f - yCanvas / drawingArea.height
        return (top - bottom) * proporcao + bottom
    }

    fun worldDistance(canvasDistance: Float) = canvasDistance / zoom
}