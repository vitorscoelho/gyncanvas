package vitorscoelho.gyncanvas.webgl.primitives

import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLBuffer
import org.khronos.webgl.WebGLRenderingContext
import vitorscoelho.gyncanvas.math.Vector2D

interface Drawable {
    val primitives: List<Primitive>
}

sealed class Primitive : Drawable {
    override val primitives: List<Primitive> get() = listOf(this)
}

class Line2D(val startPoint: Vector2D, val endPoint: Vector2D, val color: Color) : Primitive()

interface Color {
    val red: Float
    val green: Float
    val blue: Float
}

val COLOR_GREEN = object : Color {
    override val red = 0f
    override val green = 1f
    override val blue = 0f
}


private fun vectorsToFloat32Array(vararg vectors: Vector2D) = Float32Array(
    vectors.flatMap { listOf(it.x.toFloat(), it.y.toFloat()) }.toTypedArray()
)//TODO Ver se não é melhor fazer sem flatmap. Fica mais feio, mas, talvez, fica mais otimizado


//class Line2D(val startPoint: Vector2D, val endPoint: Vector2D, val color: Color) : Primitive {
//
//    fun createPositionBuffer(gl: WebGLRenderingContext): WebGLBuffer {
//        val vertexData = vectorsToFloat32Array(startPoint, endPoint)
//        return createArrayBufferStaticDraw(gl, vertexData)
//    }
//
//    fun createColorBuffer(gl: WebGLRenderingContext): WebGLBuffer {
//        //TODO criar maneira do fragment shader sempre buscar uma cor por um índice para não precisar criar buffer com três floats atoa. O ideal é o índice seja até mesmo só um valor Int16
//        val colorData = Float32Array(arrayOf(color.blue, color.green, color.red))
//        return createArrayBufferStaticDraw(gl, colorData)
//    }
//
//    fun draw(gl: WebGLRenderingContext) {
//        //TODO Set buffers and attributes
//
//        //Draw
//        gl.drawArrays(WebGLRenderingContext.LINES, 0, drawArraysCount)
//    }
//
//    companion object {
//        private const val drawArraysCount = 2
//
//        ////TODO traduzir para o inglês
//        private const val messageErrorBuffer = "Não foi possível criar o buffer do elemento"
//    }
//}