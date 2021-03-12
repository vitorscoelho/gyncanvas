package vitorscoelho.gyncanvas.webgl.programs

import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLShader

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

internal fun createVertexShader(gl: WebGLRenderingContext, source: String) =
    createShader(gl = gl, type = WebGLRenderingContext.VERTEX_SHADER, source = source)

internal fun createFragmentShader(gl: WebGLRenderingContext, source: String) =
    createShader(gl = gl, type = WebGLRenderingContext.FRAGMENT_SHADER, source = source)

private const val preMessageProgramError = "Não foi possível criar o program."//TODO colocar em inglês
internal fun createProgram(
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