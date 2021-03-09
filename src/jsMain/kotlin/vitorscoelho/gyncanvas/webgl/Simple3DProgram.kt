package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.*

private const val preMessageShaderError = "Não foi possível criar o shader."//TODO colocar em inglês

private fun createShader(gl: WebGLRenderingContext, type: Int, source: String): WebGLShader {
    val shader = gl.createShader(type)
    gl.shaderSource(shader, source)
    gl.compileShader(shader)
    val success = gl.getShaderParameter(shader, WebGLRenderingContext.COMPILE_STATUS) as Boolean
    if (success) return shader ?: throw IllegalStateException(preMessageShaderError)
    val messageError = gl.getShaderInfoLog(shader)
    gl.deleteShader(shader)
    throw IllegalStateException("$preMessageShaderError\r\n$messageError")
}

private fun createVertexShader(gl: WebGLRenderingContext, source: String) =
    createShader(gl = gl, type = WebGLRenderingContext.VERTEX_SHADER, source = source)

private fun createFragmentShader(gl: WebGLRenderingContext, source: String) =
    createShader(gl = gl, type = WebGLRenderingContext.FRAGMENT_SHADER, source = source)

private const val preMessageProgramError = "Não foi possível criar o program."//TODO colocar em inglês
private fun createProgram(
    gl: WebGLRenderingContext,
    vertexShader: WebGLShader,
    fragmentShader: WebGLShader
): WebGLProgram {
    val program = gl.createProgram()
    gl.attachShader(program, vertexShader)
    gl.attachShader(program, fragmentShader)
    gl.linkProgram(program)
    val success = gl.getProgramParameter(program, WebGLRenderingContext.LINK_STATUS) as Boolean
    if (success) return program ?: throw IllegalStateException(preMessageProgramError)//TODO colocar em inglês
    val messageError = gl.getProgramInfoLog(program)
    throw IllegalStateException("$preMessageProgramError\r\n$messageError")//TODO colocar em inglês
}

/**
 * Program mais simples possível
 */
class Simple3DProgram(private val gl: WebGLRenderingContext) {
    private val webGLProgram: WebGLProgram
    private val positionLocation: Int
    private val colorLocation: Int
    private val transformMatrixLocation: WebGLUniformLocation

    init {
        //Criação do program mais simples possível
        // TODO ver depois a questão das cores para usar índices de cores ao invés de ficar enviando RGB para cada vértice
        val vertexShader = createVertexShader(gl = gl, source = simpleVertexShaderSource())
        val fragmentShader = createFragmentShader(gl = gl, source = simpleFragmentShaderSourceCode())
        this.webGLProgram = createProgram(gl = gl, vertexShader = vertexShader, fragmentShader = fragmentShader)
        //Pegando a localização dos attributes dos shaders
        this.positionLocation = gl.getAttribLocation(webGLProgram, attNameVSPosition)
        this.colorLocation = gl.getAttribLocation(webGLProgram, attNameVSColor)
        //Pegando a localização dos uniforms dos shaders
        this.transformMatrixLocation = gl.getUniformLocation(webGLProgram, unNameVSTransformMatrix)
            ?: throw IllegalStateException("Não foi possível carregar a localização do uniform $unNameVSTransformMatrix") //TODO traduzir para o inglês
        //Ativando os atributos dos shaders. Ficam desativados por padrão
        gl.enableVertexAttribArray(positionLocation)//TODO conferir se fica aqui msm ou na função use()
        gl.enableVertexAttribArray(colorLocation)//TODO conferir se fica aqui msm ou na função use()
    }

    fun use() {
        gl.useProgram(webGLProgram)
//        gl.enableVertexAttribArray(positionLocation)//TODO conferir se fica aqui ou no init
//        gl.enableVertexAttribArray(colorLocation)//TODO conferir se fica aqui ou no init
    }

    fun setUniforms(transformMatrix: Float32Array) {
        gl.uniformMatrix4fv(transformMatrixLocation, false, transformMatrix)
    }

    fun setAttributes(positionBuffer: WebGLBuffer, colorBuffer: WebGLBuffer) {
        //TODO ver se tem como passar o positionBuffer e o colorBuffer em um único buffer. Como a ideia do SimpleProgram é renderizar elemento com uma cor só, o índice da cor pode ser o primeiro elemento do buffer
        //Position buffer
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, positionBuffer)
        gl.vertexAttribPointer(positionLocation, positionVertexSize, WebGLRenderingContext.FLOAT, false, 0, 0)
        //Color buffer
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, colorBuffer)
        //TODO ver de mudar para passar apenas um índice da cor na invocação de função abaixo
        gl.vertexAttribPointer(colorLocation, colorVertexSize, WebGLRenderingContext.FLOAT, false, 0, 0)
    }

    companion object {
        private const val positionVertexSize = 3 //Porque é 3D
        private const val colorVertexSize = 3 //Porque é RGB com alpha sempre igual a 1

        private const val attNameVSPosition = "position"
        private const val attNameVSColor = "color"
        private const val unNameVSTransformMatrix = "transformMatrix"
        private const val varNameFSColor = "vColor"

        private fun simpleVertexShaderSource(): String = """
            precision mediump float;
                    
            attribute vec3 $attNameVSPosition;
            attribute vec3 $attNameVSColor;
            varying vec3 $varNameFSColor;
                    
            uniform mat4 $unNameVSTransformMatrix;
            
            void main(){
                $varNameFSColor = $attNameVSColor;
                gl_Position = $unNameVSTransformMatrix * vec4($attNameVSPosition, 1);
            }
        """.trimIndent()

        private fun simpleFragmentShaderSourceCode() = """
            precision mediump float;
            varying vec3 $varNameFSColor;
            void main(){
                gl_FragColor = vec4($varNameFSColor, 1);
            }
        """.trimIndent()
    }
}