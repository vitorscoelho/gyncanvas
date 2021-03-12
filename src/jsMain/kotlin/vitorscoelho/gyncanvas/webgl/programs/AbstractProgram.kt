package vitorscoelho.gyncanvas.webgl.programs

import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext

abstract class AbstractProgram(
    protected val gl: WebGLRenderingContext,
    vertexShaderSource: String,
    fragmentShaderSource: String
) {
    protected val webGLProgram: WebGLProgram

    init {
        val vertexShader = createVertexShader(gl = gl, source = vertexShaderSource)
        val fragmentShader = createFragmentShader(gl = gl, source = fragmentShaderSource)
        this.webGLProgram = createProgram(gl = gl, vertexShader = vertexShader, fragmentShader = fragmentShader)
    }

    fun use() {
        gl.useProgram(webGLProgram)
//        gl.enableVertexAttribArray(positionLocation)//TODO conferir se fica aqui ou no init
//        gl.enableVertexAttribArray(colorLocation)//TODO conferir se fica aqui ou no init
    }
}