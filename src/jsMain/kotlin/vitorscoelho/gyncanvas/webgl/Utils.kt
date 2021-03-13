package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.*
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
    with(backgroundColor) {
        fun toWebGL(color: Short) = color.toFloat() / 255f
        clearColor(toWebGL(red), toWebGL(green), toWebGL(blue), alpha)
    }
    clear(GL.COLOR_BUFFER_BIT)
}

internal fun WebGLRenderingContext.createEmptyBuffer(): WebGLBuffer =
    createBuffer() ?: throw IllegalStateException()//TODO colocar mensagem na Exception

internal fun WebGLRenderingContext.createArrayBufferData(data: BufferDataSource, usage: Int): WebGLBuffer {
    val buffer = createEmptyBuffer()
    changeArrayBufferData(buffer, data, usage)
    return buffer
}

internal fun WebGLRenderingContext.createArrayBufferStaticData(data: BufferDataSource): WebGLBuffer =
    createArrayBufferData(data, GL.STATIC_DRAW)

internal fun WebGLRenderingContext.createArrayBufferDynamicData(data: BufferDataSource): WebGLBuffer =
    createArrayBufferData(data, GL.DYNAMIC_DRAW)

internal fun WebGLRenderingContext.createArrayBufferStaticData(data: Array<Short>): WebGLBuffer =
    createArrayBufferStaticData(Int16Array(data))

internal fun WebGLRenderingContext.createArrayBufferDynamicData(data: ShortArray): WebGLBuffer =
    createArrayBufferDynamicData(Int16Array(data.toTypedArray()))

internal fun WebGLRenderingContext.createArrayBufferStaticData(data: Array<Int>): WebGLBuffer =
    createArrayBufferStaticData(Int32Array(data))

internal fun WebGLRenderingContext.createArrayBufferDynamicData(data: IntArray): WebGLBuffer =
    createArrayBufferDynamicData(Int32Array(data.toTypedArray()))

internal fun WebGLRenderingContext.createArrayBufferStaticData(data: Array<Float>): WebGLBuffer =
    createArrayBufferStaticData(Float32Array(data))

internal fun WebGLRenderingContext.createArrayBufferDynamicData(data: FloatArray): WebGLBuffer =
    createArrayBufferDynamicData(Float32Array(data.toTypedArray()))

internal fun WebGLRenderingContext.changeArrayBufferData(buffer: WebGLBuffer, newData: BufferDataSource, usage: Int) {
    bindBuffer(GL.ARRAY_BUFFER, buffer)
    bufferData(GL.ARRAY_BUFFER, newData, usage)
}

internal fun WebGLRenderingContext.changeArrayBufferStaticData(buffer: WebGLBuffer, newData: BufferDataSource) =
    changeArrayBufferData(buffer, newData, GL.STATIC_DRAW)

internal fun WebGLRenderingContext.changeArrayBufferDynamicData(buffer: WebGLBuffer, newData: BufferDataSource) =
    changeArrayBufferData(buffer, newData, GL.DYNAMIC_DRAW)

internal fun WebGLRenderingContext.changeArrayBufferStaticData(buffer: WebGLBuffer, newData: Array<Short>) =
    changeArrayBufferStaticData(buffer, Int16Array(newData))

internal fun WebGLRenderingContext.changeArrayBufferDynamicData(buffer: WebGLBuffer, newData: ShortArray) =
    changeArrayBufferData(buffer, Int16Array(newData.toTypedArray()), GL.DYNAMIC_DRAW)

internal fun WebGLRenderingContext.changeArrayBufferStaticData(buffer: WebGLBuffer, newData: Array<Int>) =
    changeArrayBufferStaticData(buffer, Int32Array(newData))

internal fun WebGLRenderingContext.changeArrayBufferDynamicData(buffer: WebGLBuffer, newData: IntArray) =
    changeArrayBufferDynamicData(buffer, Int32Array(newData.toTypedArray()))

internal fun WebGLRenderingContext.changeArrayBufferStaticData(buffer: WebGLBuffer, newData: Array<Float>) =
    changeArrayBufferStaticData(buffer, Float32Array(newData))

internal fun WebGLRenderingContext.changeArrayBufferDynamicData(buffer: WebGLBuffer, newData: FloatArray) =
    changeArrayBufferDynamicData(buffer, Float32Array(newData.toTypedArray()))

internal fun OrthographicCamera2D.toWebGLMat3() = Float32Array(
    arrayOf(
        mxx, myx, 0f,
        mxy, myy, 0f,
        tx, ty, 1f
    )
)