package vitorscoelho.gyncanvas.webgl.programs

import org.khronos.webgl.*
import org.khronos.webgl.WebGLRenderingContext.Companion as GL

/**
 * Program mais simples possível
 */
internal class Simple3DProgram(gl: WebGLRenderingContext) :
    AbstractProgram(gl = gl, vertexShaderSource = vertexShaderSource, fragmentShaderSource = fragmentShaderSource) {
    /**Localização do attribute position do shader*/
    private val positionLocation: Int = gl.getAttribLocation(webGLProgram, attNameVSPosition)

    /**Localização do attribute color do shader*/
    private val colorLocation: Int = gl.getAttribLocation(webGLProgram, attNameVSColor)
    //TODO ver depois a questão das cores para usar índices de cores ao invés de ficar enviando RGB para cada vértice

    /**Localização do uniform transformMatrix do shader*/
    private val transformMatrixLocation: WebGLUniformLocation =
        gl.getUniformLocation(webGLProgram, unNameVSTransformMatrix)
            ?: throw IllegalStateException("Não foi possível carregar a localização do uniform $unNameVSTransformMatrix")

    init {
        //Criação do program mais simples possível
        //Ativando os atributos dos shaders. Ficam desativados por padrão
        gl.enableVertexAttribArray(positionLocation)//TODO conferir se fica aqui msm ou na função use()
        gl.enableVertexAttribArray(colorLocation)//TODO conferir se fica aqui msm ou na função use()
    }

    fun setUniforms(transformMatrix: Float32Array) {
        gl.uniformMatrix4fv(transformMatrixLocation, false, transformMatrix)
    }

    fun setAttributes(positionBuffer: WebGLBuffer, colorBuffer: WebGLBuffer) {
        //TODO ver se tem como passar o positionBuffer e o colorBuffer em um único buffer. Como a ideia do SimpleProgram é renderizar elemento com uma cor só, o índice da cor pode ser o primeiro elemento do buffer
        //Position buffer
        gl.bindBuffer(GL.ARRAY_BUFFER, positionBuffer)
        gl.vertexAttribPointer(positionLocation, positionVertexSize, GL.FLOAT, false, 0, 0)
        //Color buffer
        gl.bindBuffer(GL.ARRAY_BUFFER, colorBuffer)
        //TODO ver de mudar para passar apenas um índice da cor na invocação de função abaixo
        gl.vertexAttribPointer(colorLocation, colorVertexSize, GL.FLOAT, false, 0, 0)
    }

    companion object {
        private const val positionVertexSize = 3 //Porque é 3D
        private const val colorVertexSize = 3 //Porque é RGB com alpha sempre igual a 1

        private const val attNameVSPosition = "position"
        private const val attNameVSColor = "color"
        private const val unNameVSTransformMatrix = "transformMatrix"
        private const val varNameFSColor = "vColor"

        private val vertexShaderSource: String
            get() = """
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

        private val fragmentShaderSource: String
            get() = """
            precision mediump float;
            varying vec3 $varNameFSColor;
            void main(){
                gl_FragColor = vec4($varNameFSColor, 1);
            }
        """.trimIndent()
    }
}