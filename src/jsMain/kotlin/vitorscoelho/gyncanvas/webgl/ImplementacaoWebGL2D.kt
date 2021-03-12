package vitorscoelho.gyncanvas.webgl

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext.Companion as GL
import vitorscoelho.gyncanvas.core.primitives.Color
import vitorscoelho.gyncanvas.webgl.programs.Simple2DProgram

class WebGLStaticDrawer2D(val drawingArea: JSDrawingArea) {
    private val gl = getWebGLContext(drawingArea = drawingArea)

    private val program = Simple2DProgram(gl = gl)

    fun draw(backgroundColor: Color, camera: OrthographicCamera2D) {
        gl.adjustViewportAndClear(backgroundColor = backgroundColor)

        program.use()

        val positionBuffer: WebGLBuffer = createArrayBufferStaticDraw(
            gl = gl,
            dataArray = Float32Array(
                arrayOf(
                    -1f, 0f,
                    0f, 1f,
                    1f, 0f
                )
            )
        )
        program.setAttributes(
            positionBuffer = positionBuffer
        )
        program.setUniforms(
            transformMatrix = camera.toWebGLMat3()
        )
        gl.drawArrays(
            mode = GL.TRIANGLES,
            first = 0,
            count = 3
        )
    }
}

