package vitorscoelho.gyncanvas.webgl

/**
 * mxx, mxy, mxz, tx,
 * myx, myy, myz, ty,
 * mzx, mzy, mzz, tz,
 *  0 ,  0 ,  0 ,  1
 */
class OrthographicCamera(val drawingArea: DrawingArea) {
    var mxx: Float = 1f
        private set
    val mxy = 0f
    val mxz = 0f
    var tx = 0f
        private set
    val myx = 0f
    var myy = 1f
        private set
    val myz = 0f
    var ty = 0f
        private set
    val mzx = 0f
    val mzy = 0f
    var mzz = 1f
        private set
    var tz = 0f
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

    private fun set(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        require(right > left && top > bottom && far > near)
        mxx = 2f / (right - left)
        myy = 2f / (top - bottom)
        mzz = -2f / (far - near)
        tx = -(right + left) / (right - left)
        ty = -(top + bottom) / (top - bottom)
        tz = -(far + near) / (far - near)
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
            near = -1f, far = 1f//TODO mudar para possibilitar 3D no futuro
        )
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