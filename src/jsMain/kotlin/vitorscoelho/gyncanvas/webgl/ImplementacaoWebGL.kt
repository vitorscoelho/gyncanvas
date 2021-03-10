package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.*
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import org.w3c.dom.RenderingContext
import vitorscoelho.gyncanvas.webgl.primitives.Color
import vitorscoelho.gyncanvas.webgl.primitives.Drawable
import vitorscoelho.gyncanvas.webgl.primitives.Line2D

private val webGLContextAttributes = object : WebGLContextAttributes {
    override var alpha: Boolean? = true
    override var depth: Boolean? = true
    override var stencil: Boolean? = false
    override var antialias: Boolean? = true
    override var premultipliedAlpha: Boolean? = true
    override var preserveDrawingBuffer: Boolean? = true
    override var preferLowPowerToHighPerformance: Boolean? = undefined
    override var failIfMajorPerformanceCaveat: Boolean? = false
}

private fun createArrayBufferStaticDraw(gl: WebGLRenderingContext, dataArray: Float32Array): WebGLBuffer {
    val buffer = gl.createBuffer()
    gl.bindBuffer(GL.ARRAY_BUFFER, buffer)
    gl.bufferData(GL.ARRAY_BUFFER, dataArray, GL.STATIC_DRAW)
    return buffer ?: throw IllegalStateException()//TODO colocar mensagem na Exception
}

private fun createEmptyArrayBufferStaticDraw(gl: WebGLRenderingContext): WebGLBuffer = createArrayBufferStaticDraw(
    gl = gl, dataArray = Float32Array(emptyArray())
)

private fun OrthographicCamera.toWebGL() = Float32Array(
    arrayOf(
        mxx, myx, mzx, 0f,
        mxy, myy, mzy, 0f,
        mxz, myz, mzz, 0f,
        tx, ty, tz, 1f
    )
)

/**
 * Drawer com as seguintes características:
 * - Otimizado para casos onde não se acrescenta ou remove elementos com frequência. Ou seja, ideal para casos de desenhos fixos onde só se deseja fazer uma navegação pelo desenho
 * - Sempre que um elemento [Drawable] for adicionado ou removido, toda a lógica de criação de buffer vai acontecer para cada um dos elementos, mesmo os já existentes
 */
class WebGLStaticDrawer(val drawingArea: JSDrawingArea) {
    private val gl: WebGLRenderingContext = run {
        val context: RenderingContext = drawingArea.canvas.getContext("webgl", webGLContextAttributes)
            ?: throw IllegalStateException("WebGL is not supported")
        context as WebGLRenderingContext
    }

    private val simpleProgram = Simple3DProgram(gl = gl)

    private var verticesCount: Int = 0
    private val positionsBuffer: WebGLBuffer = createEmptyArrayBufferStaticDraw(gl = gl)
    private val colorsBuffer: WebGLBuffer = createEmptyArrayBufferStaticDraw(gl = gl)

    /**Seta a lista de elementos que serão desenhados*/
    fun setElements(elements: List<Drawable>) {
        val positionsData: Array<Float> =
            elements
                .filterIsInstance<Line2D>()
                .flatMap { it.vertices }
                .flatMap { listOf(it.x, it.y, 0) }
                .map { it.toFloat() }
                .toTypedArray()
        gl.bindBuffer(GL.ARRAY_BUFFER, positionsBuffer)
        gl.bufferData(GL.ARRAY_BUFFER, Float32Array(positionsData), GL.STATIC_DRAW)
        val colorsData: Array<Float> =
            elements
                .filterIsInstance<Line2D>()
                .map { it.color }
                .flatMap { listOf(it, it) }
                .flatMap { listOf(it.red, it.green, it.blue) }
                .toTypedArray()
        println(positionsData)
        println(colorsData)
        gl.bindBuffer(GL.ARRAY_BUFFER, colorsBuffer)
        gl.bufferData(GL.ARRAY_BUFFER, Float32Array(colorsData), GL.STATIC_DRAW)
        this.verticesCount = positionsData.size / 3
    }

    fun draw(backgroundColor: Color, camera: OrthographicCamera) {
        //Ajustando a viewport ao tamanho do canvas
        gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight)
        //Limpar a tela com a cor do background
        gl.clearColor(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 1f)
        gl.clear(GL.COLOR_BUFFER_BIT)

        //TODO Atribuir os parâmetros de cada elemento para os shaders
        simpleProgram.use()
        simpleProgram.setUniforms(transformMatrix = camera.toWebGL())
        simpleProgram.setAttributes(
            positionBuffer = positionsBuffer,
            colorBuffer = colorsBuffer
        )

        //TODO Invocar gl.drawArrays para cada elemento
        //TODO ideia de uma implementação específica:
        ///Criar um buffer com as informações de vértices de todos os elementos.
        ///Mapear, para cada elemento, o tipo (GL.LINES, GL.TRIANGLES,..) o índice do primeiro vértice no buffer e a qtd de vértices no buffer
        ///Assim, bastaria ficar chamando o gl.drawArrays várias vezes
        gl.drawArrays(GL.LINES, 0, this.verticesCount)
    }
}