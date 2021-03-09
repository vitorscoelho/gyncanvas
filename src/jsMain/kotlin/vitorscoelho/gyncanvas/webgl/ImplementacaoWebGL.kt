package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.*
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.RenderingContext
import vitorscoelho.gyncanvas.math.TransformationMatrix
import vitorscoelho.gyncanvas.webgl.primitives.Color
import vitorscoelho.gyncanvas.webgl.primitives.Drawable

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
    gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, buffer)
    gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, dataArray, WebGLRenderingContext.STATIC_DRAW)
    return buffer ?: throw IllegalStateException()//TODO colocar mensagem na Exception
}

/**
 * Drawer com as seguintes características:
 * - Otimizado para casos onde não se acrescenta ou remove elementos com frequência. Ou seja, ideal para casos de desenhos fixos onde só se deseja fazer uma navegação pelo desenho
 * - Sempre que um elemento [Drawable] for adicionado ou removido, toda a lógica de criação de buffer vai acontecer para cada um dos elementos, mesmo os já existentes
 */
class WebGLStaticDrawer(val canvas: HTMLCanvasElement) {
    private val gl: WebGLRenderingContext = run {
        val context: RenderingContext = canvas.getContext("webgl", webGLContextAttributes)
            ?: throw IllegalStateException("WebGL is not supported")
        context as WebGLRenderingContext
    }

    private val simpleProgram = Simple3DProgram(gl = gl)

    /**Seta a lista de elementos que serão desenhados*/
    fun setElements(elements: List<Drawable>) {

    }

    fun draw(backgroundColor: Color, transform: TransformationMatrix) {
        //Ajustando a viewport ao tamanho do canvas
        gl.viewport(0, 0, canvas.clientWidth, canvas.clientHeight)
        //Limpar a tela com a cor do background
        gl.clearColor(backgroundColor.red, backgroundColor.green, backgroundColor.blue, 1f)
        gl.clear(WebGLRenderingContext.COLOR_BUFFER_BIT)

        //TODO Atribuir os parâmetros de cada elemento para os shaders
        simpleProgram.use()
        simpleProgram.setUniforms(
            Float32Array(
                arrayOf(
                    1f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 1f
                )
            )
        )
//        val linha = Line2D(
//            startPoint = Vector2D(x = -1.0, y = -1.0),
//            endPoint = Vector2D(x = 1.0, y = 1.0),
//            color = COLOR_GREEN
//        )
//        simpleProgram.setAttributes(
//            positionBuffer = linha.createPositionBuffer(gl),
//            colorBuffer = linha.createColorBuffer(gl)
//        )
        val positionBuffer = createArrayBufferStaticDraw(
            gl = gl,
            dataArray = Float32Array(
                arrayOf(
                    -1f, -1f, 0f,
                    1f, 1f, 0f
                )
            )
        )
        val colorBuffer = createArrayBufferStaticDraw(
            gl = gl,
            dataArray = Float32Array(
                arrayOf(
                    1f, 1f, 1f,
                    1f, 1f, 1f
                )
            )
        )
        simpleProgram.setAttributes(
            positionBuffer = positionBuffer,
            colorBuffer = colorBuffer
        )

        //TODO Invocar gl.drawArrays para cada elemento
        //TODO ideia de uma implementação específica:
        ///Criar um buffer com as informações de vértices de todos os elementos.
        ///Mapear, para cada elemento, o tipo (GL.LINES, GL.TRIANGLES,..) o índice do primeiro vértice no buffer e a qtd de vértices no buffer
        ///Assim, bastaria ficar chamando o gl.drawArrays várias vezes
        gl.drawArrays(GL.LINES, 0, 2)
    }
}