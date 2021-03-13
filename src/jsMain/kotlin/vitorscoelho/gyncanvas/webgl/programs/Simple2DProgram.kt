package vitorscoelho.gyncanvas.webgl.programs

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import org.khronos.webgl.WebGLRenderingContext.Companion as GL

internal class Simple2DProgram(gl: WebGLRenderingContext) :
    AbstractProgram(gl = gl, vertexShaderSource = vertexShaderSource, fragmentShaderSource = fragmentShaderSource) {
    //Localização dos attributes
    private val positionLocation: Int = gl.getAttribLocation(webGLProgram, "position")
    private val colorLocation: Int = gl.getAttribLocation(webGLProgram, "color")

    /**Localização do uniform transformMatrix do shader*/
    private val transformMatrixLocation: WebGLUniformLocation =
        gl.getUniformLocation(webGLProgram, "transformMatrix")
            ?: throw IllegalStateException("Não foi possível carregar a localização do uniform")

    init {
        //Ativando os atributos dos shaders. Ficam desativados por padrão
        gl.enableVertexAttribArray(positionLocation)//TODO conferir se fica aqui msm ou na função use()
        gl.enableVertexAttribArray(colorLocation)//TODO conferir se fica aqui msm ou na função use()
    }

    fun setAttributes(positionBuffer: WebGLBuffer, colorBuffer: WebGLBuffer) {
        //TODO ver se tem como passar o positionBuffer e o colorBuffer em um único buffer. Como a ideia do SimpleProgram é renderizar elemento com uma cor só, o índice da cor pode ser o primeiro elemento do buffer
        //Position buffer
        gl.bindBuffer(GL.ARRAY_BUFFER, positionBuffer)
        gl.vertexAttribPointer(positionLocation, positionVertexSize, GL.FLOAT, false, 0, 0)
        //Color buffer
        gl.bindBuffer(GL.ARRAY_BUFFER, colorBuffer)
        gl.vertexAttribPointer(colorLocation, colorVertexSize, GL.FLOAT, false, 0, 0)
    }

    fun setCamera(transformMatrix: Float32Array) {
        gl.uniformMatrix3fv(transformMatrixLocation, false, transformMatrix)
    }

    companion object {
        private const val positionVertexSize = 2//Porque é 2D
        private const val colorVertexSize = 3//Porque é RGB com alpha sempre igual a 1

        private val vertexShaderSource: String
            get() = """
            precision mediump float;
                    
            attribute vec2 position;
            attribute vec3 color;
            
            varying vec3 v_color;
            
            uniform mat3 transformMatrix;
            
            void main(){
                v_color = color;
                vec3 transformedVector = transformMatrix * vec3(position.x, position.y, 1);
                gl_Position = vec4(transformedVector.x, transformedVector.y, 0, 1);
            }
        """.trimIndent()

        private val fragmentShaderSource: String
            get() = """
            precision mediump float;
            
            varying vec3 v_color;
            
            void main(){
                gl_FragColor = vec4(v_color, 1);
            }
        """.trimIndent()
    }
}