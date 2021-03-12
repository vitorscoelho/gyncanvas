package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLContextAttributes
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import org.w3c.dom.RenderingContext
import vitorscoelho.gyncanvas.core.primitives.Color

internal val DEFAULT_WEBGL_CONTEXT_ATTRIBUTES: WebGLContextAttributes
    get() = object : WebGLContextAttributes {
        override var alpha: Boolean? = true
        override var depth: Boolean? = true
        override var stencil: Boolean? = false
        override var antialias: Boolean? = true
        override var premultipliedAlpha: Boolean? = true
        override var preserveDrawingBuffer: Boolean? = true
        override var preferLowPowerToHighPerformance: Boolean? = undefined
        override var failIfMajorPerformanceCaveat: Boolean? = false
    }

internal fun getWebGLContext(
    drawingArea: JSDrawingArea,
    webGLAttributes: WebGLContextAttributes = DEFAULT_WEBGL_CONTEXT_ATTRIBUTES
): WebGLRenderingContext {
    val context: RenderingContext = drawingArea.canvas.getContext("webgl", webGLAttributes)
        ?: throw IllegalStateException("WebGL is not supported")
    return context as WebGLRenderingContext
}

internal fun WebGLRenderingContext.adjustViewportAndClear(backgroundColor: Color) {
    //Ajustando a viewport ao tamanho do canvas
    viewport(0, 0, drawingBufferWidth, drawingBufferHeight)
    //Limpar a tela com a cor do background
    clearColor(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 1f)
    clear(GL.COLOR_BUFFER_BIT)
}

internal fun createArrayBufferStaticDraw(gl: WebGLRenderingContext, dataArray: Float32Array): WebGLBuffer {
    val buffer = gl.createBuffer()
    gl.bindBuffer(GL.ARRAY_BUFFER, buffer)
    gl.bufferData(GL.ARRAY_BUFFER, dataArray, GL.STATIC_DRAW)
    return buffer ?: throw IllegalStateException()//TODO colocar mensagem na Exception
}

internal fun createEmptyArrayBufferStaticDraw(gl: WebGLRenderingContext): WebGLBuffer = createArrayBufferStaticDraw(
    gl = gl, dataArray = Float32Array(emptyArray())
)

internal fun OrthographicCamera2D.toWebGLMat3() = Float32Array(
    arrayOf(
        mxx, myx, 0f,
        mxy, myy, 0f,
        tx, ty, 1f
    )
)

internal fun OrthographicCamera2D.toWebGLMat4() = Float32Array(
    arrayOf(
        mxx, myx, mzx, 0f,
        mxy, myy, mzy, 0f,
        mxz, myz, mzz, 0f,
        tx, ty, tz, 1f
    )
)