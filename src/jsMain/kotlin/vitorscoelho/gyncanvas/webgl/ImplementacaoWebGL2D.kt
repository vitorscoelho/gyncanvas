package vitorscoelho.gyncanvas.webgl

import kotlinx.browser.document
import org.khronos.webgl.*
import org.w3c.dom.*
import vitorscoelho.gyncanvas.Drawer
import vitorscoelho.gyncanvas.OrthographicCamera2D
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import vitorscoelho.gyncanvas.core.primitives.Color
import vitorscoelho.gyncanvas.core.primitives.Drawable
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.core.primitives.PrimitiveType
import vitorscoelho.gyncanvas.webgl.programs.AbstractProgram
import vitorscoelho.gyncanvas.webgl.programs.Simple2DProgram


//TODO ler isto sobre fontes e suavização de curvas: https://medium.com/@evanwallace/easy-scalable-text-rendering-on-the-gpu-c3f4d782c5ac


private class TextureProgram(gl: WebGLRenderingContext) :
    AbstractProgram(gl = gl, vertexShaderSource = vertexShaderSource, fragmentShaderSource = fragmentShaderSource) {

    //Localização dos attributes
    private val positionLocation: Int = gl.getAttribLocation(webGLProgram, "position")
    private val texCoordLocation: Int = gl.getAttribLocation(webGLProgram, "texCoord")

    /**Localização do uniform transformMatrix do shader*/
    private val transformMatrixLocation: WebGLUniformLocation =
        gl.getUniformLocation(webGLProgram, "transformMatrix")
            ?: throw IllegalStateException("Não foi possível carregar a localização do uniform")

    private val textureLocation: WebGLUniformLocation =
        gl.getUniformLocation(webGLProgram, "u_texture")
            ?: throw IllegalStateException("Não foi possível carregar a localização do uniform")

    init {
        //Ativando os atributos dos shaders. Ficam desativados por padrão
        gl.enableVertexAttribArray(positionLocation)//TODO conferir se fica aqui msm ou na função use()
        gl.enableVertexAttribArray(texCoordLocation)//TODO conferir se fica aqui msm ou na função use()
    }

    fun setAttributes(positionBuffer: WebGLBuffer, texCoordBuffer: WebGLBuffer) {
        //Position buffer
        gl.bindBuffer(GL.ARRAY_BUFFER, positionBuffer)
        gl.vertexAttribPointer(positionLocation, positionVertexSize, GL.FLOAT, false, 0, 0)
        //Texture coordinates buffer
        gl.bindBuffer(GL.ARRAY_BUFFER, texCoordBuffer)
        gl.vertexAttribPointer(texCoordLocation, 2, GL.FLOAT, false, 0, 0)
    }

    fun setCamera(transformMatrix: Float32Array) {
        gl.uniformMatrix3fv(transformMatrixLocation, false, transformMatrix)
    }

    fun setTexture(texture: WebGLTexture) {
        gl.uniform1i(textureLocation, 0)
    }

    companion object {
        private const val positionVertexSize = 2//Porque é 2D
        private val vertexShaderSource: String
            get() = """
            precision mediump float;
                    
            attribute vec2 position;
            attribute vec2 texCoord;
            
            varying vec2 v_texCoord;
            
            uniform mat3 transformMatrix;
            
            void main(){
                v_texCoord = texCoord;
                vec3 transformedVector = transformMatrix * vec3(position.x, position.y, 1);
                gl_Position = vec4(transformedVector.x, transformedVector.y, 0, 1);
            }
        """.trimIndent()

        private val fragmentShaderSource: String
            get() = """
            precision mediump float;
            
            varying vec2 v_texCoord;
            
            uniform sampler2D u_texture;
            
            void main(){
                gl_FragColor = texture2D(u_texture, v_texCoord);
            }
        """.trimIndent()
    }
}

private fun createTextTexture(gl: WebGLRenderingContext): WebGLTexture {
    val canvasElement = document.createElement("canvas") as HTMLCanvasElement
    val gc = canvasElement.getContext("2d")!! as CanvasRenderingContext2D

    val fontSize = 20
    gc.font = "${fontSize}px monospace"
    gc.textAlign = CanvasTextAlign.CENTER
    gc.textBaseline = CanvasTextBaseline.MIDDLE
    val textContent = "Texto teste"
    val textMetrics: TextMetrics = gc.measureText(textContent)
    val width = textMetrics.width.toInt() + 1
    val height = (fontSize * 1.5).toInt()
    gc.canvas.width = width
    gc.canvas.height = height
    gc.fillStyle = "black"
    gc.clearRect(0.0, 0.0, gc.canvas.width.toDouble(), gc.canvas.height.toDouble())
    gc.fillText(textContent, width / 2.0, height / 2.0)

    //TODO testar sempre criar um texto com fontSize relativamente alto pra permitir um zoom razoável no texto sem perda de qualidade

    val textureText = gl.createTexture()
    gl.bindTexture(GL.TEXTURE_2D, textureText)
    gl.pixelStorei(GL.UNPACK_FLIP_Y_WEBGL, 1)
    gl.texImage2D(GL.TEXTURE_2D, 0, GL.RGBA, GL.RGBA, GL.UNSIGNED_BYTE, canvasElement)
    gl.generateMipmap(GL.TEXTURE_2D)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_S, GL.CLAMP_TO_EDGE)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_T, GL.CLAMP_TO_EDGE)

    return textureText!!
}

private val mapGlTypes: Map<PrimitiveType, Int> = mapOf(
    PrimitiveType.POINTS to GL.POINTS,
    PrimitiveType.LINES to GL.LINES,
    PrimitiveType.LINE_STRIP to GL.LINE_STRIP,
    PrimitiveType.LINE_LOOP to GL.LINE_LOOP,
    PrimitiveType.TRIANGLES to GL.TRIANGLES,
    PrimitiveType.TRIANGLE_STRIP to GL.TRIANGLE_STRIP,
    PrimitiveType.TRIANGLE_FAN to GL.TRIANGLE_FAN,
)

private val Primitive.glType: Int get() = mapGlTypes[this.type]!!

private fun rgbToFloat(decimal: Short) = decimal.toFloat() / 255f

class WebGLStaticDrawer2D internal constructor(drawingArea: JSDrawingArea) : Drawer() {
    private val gl = getWebGLContext(drawingArea = drawingArea)

    private val program = Simple2DProgram(gl = gl)
    private val textureProgram = TextureProgram(gl = gl)
    private val positionTexBuffer = gl.createArrayBufferStaticData(
        data = arrayOf(
            30f, 30f,
            70f, 30f,
            100f, 70f,
            60f, 70f,
            //
            30f, -50f,
            70f, -50f,
            70f, -20f,
            30f, -20f
        )
    )
    private val texCoordBuffer = gl.createArrayBufferStaticData(
        data = arrayOf(
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
            //
            0f, 0f,
            1f, 0f,
            1f, 1f,
            0f, 1f
        )
    )

    val texture: WebGLTexture = run {
        val tex = gl.createTexture()
        gl.bindTexture(GL.TEXTURE_2D, tex)

        //Preenche a textura com azul enquanto a imagem não é completamente carregada
        gl.texImage2D(
            GL.TEXTURE_2D,
            0,
            GL.RGBA,
            1,
            1,
            0,
            GL.RGBA,
            GL.UNSIGNED_BYTE,
            Uint8Array(arrayOf<Byte>(0, 0, 255.toByte(), 255.toByte()))
        )
        //Carregando uma imagem pré-existente
//        val imageSource = Image().apply { src = "f-texture.png" }
//        imageSource.onload = {
//            gl.bindTexture(GL.TEXTURE_2D, texture)
//            gl.texImage2D(GL.TEXTURE_2D, 0, GL.RGBA, GL.RGBA, GL.UNSIGNED_BYTE, imageSource)
//            gl.generateMipmap(GL.TEXTURE_2D)
//        }
        //Criando uma imagem a partir do canvas do HTML
        val canvasText = createCanvasText("Olá, texto", 200)
        //Colocando o canvas em um objeto Image apenas para ter a possibilidade de usar o método 'onload'
        val image = Image().apply { src = canvasText.toDataURL() }
        //TODO fazer renderizar nas mesmas proporções do canvas para o texto não ficar esticado
        //TODO colocar transparência no fundo do Canvas
        //TODO testar a situação de colocar um canvas sobre o WebGL para renderizar o texto. Acho que vai ser a melhor solução mesmo
        image.onload = {
            gl.pixelStorei(GL.UNPACK_FLIP_Y_WEBGL, 1)
            gl.bindTexture(GL.TEXTURE_2D, texture)
            gl.texImage2D(GL.TEXTURE_2D, 0, GL.RGBA, GL.RGBA, GL.UNSIGNED_BYTE, canvasText)
            gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MAG_FILTER, GL.LINEAR)
            gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MIN_FILTER, GL.LINEAR_MIPMAP_NEAREST)
            gl.generateMipmap(GL.TEXTURE_2D)
        }
        tex!!
    }

    private fun getPowerOfTwo(value: Double): Int {
        var pow = 1
        while (pow <= value) {
            pow *= 2
        }
        return pow
    }

    private fun createCanvasText(
        content: String, fontSize: Int
    ): HTMLCanvasElement {
        //Baseado em: https://delphic.me.uk/tutorials/webgl-text
        val canvas = document.createElement("canvas") as HTMLCanvasElement
        val gc = canvas.getContext("2d")!! as CanvasRenderingContext2D
        gc.font = "${fontSize}px monospace"
        canvas.width = getPowerOfTwo(gc.measureText(content).width)
        canvas.height = getPowerOfTwo(2.0 * fontSize)

        gc.fillStyle = "red"
        gc.textAlign = CanvasTextAlign.CENTER
        gc.textBaseline = CanvasTextBaseline.MIDDLE
        gc.font = "${fontSize}px monospace"
        gc.fillText(content, canvas.width / 2.0, canvas.height / 2.0)

        return canvas
    }

    private var elementsTypes: IntArray = intArrayOf()
    private var elementsVerticesCount: IntArray = intArrayOf()

    private val positionBuffer: WebGLBuffer = gl.createEmptyBuffer()
    private val colorBuffer: WebGLBuffer = gl.createEmptyBuffer()

    override fun setElements(elements: List<Drawable>) {
        val lista = mutableListOf<Primitive>()
        elements.forEach { element ->
            element.forEachPrimitive { primitive ->
                lista += primitive
            }
        }
        setElements(lista)
    }

    private fun setElements(elements: List<Primitive>) {
        elementsTypes = IntArray(size = elements.size)
        elementsVerticesCount = IntArray(size = elements.size)

        val vertexCount = elements.sumBy { it.verticesCount }

        val vertexPosition = Array<Float>(size = vertexCount * 2) { 0f }
        val vertexColor = Array<Float>(size = vertexCount * 3) { 0f }

        var positionIndex = 0
        var colorIndex = 0
        elements.forEachIndexed { elementIndex, element ->
            elementsTypes[elementIndex] = element.glType
            elementsVerticesCount[elementIndex] = element.verticesCount
            element.forEachVertex { _, x, y, _, red, green, blue, _ ->
                vertexPosition[positionIndex++] = x
                vertexPosition[positionIndex++] = y
                vertexColor[colorIndex++] = rgbToFloat(red)
                vertexColor[colorIndex++] = rgbToFloat(green)
                vertexColor[colorIndex++] = rgbToFloat(blue)
            }
        }
        gl.changeArrayBufferStaticData(positionBuffer, vertexPosition)
        gl.changeArrayBufferStaticData(colorBuffer, vertexColor)
    }

    override fun draw(backgroundColor: Color, camera: OrthographicCamera2D) {
        gl.adjustViewportAndClear(backgroundColor = backgroundColor)
        program.use()
        program.setAttributes(positionBuffer = positionBuffer, colorBuffer = colorBuffer)
        program.setCamera(transformMatrix = camera.toWebGLMat3())
        var first = 0
        for (index in 0..elementsTypes.size) {
            gl.drawArrays(mode = elementsTypes[index], first = first, count = elementsVerticesCount[index])
            first += elementsVerticesCount[index]
        }
        textureProgram.use()
        textureProgram.setCamera(transformMatrix = camera.toWebGLMat3())
        textureProgram.setAttributes(
            positionBuffer = positionTexBuffer, texCoordBuffer = texCoordBuffer
        )
        textureProgram.setTexture(texture = texture)
        gl.drawArrays(GL.TRIANGLE_FAN, 0, 4)
        gl.drawArrays(GL.TRIANGLE_FAN, 4, 4)
    }
}

