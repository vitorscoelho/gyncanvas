package vitorscoelho.gyncanvas.core.primitivas

import com.sun.javafx.geom.BaseBounds
import com.sun.javafx.geom.transform.BaseTransform
import com.sun.javafx.jmx.MXNodeAlgorithm
import com.sun.javafx.jmx.MXNodeAlgorithmContext
import com.sun.javafx.sg.prism.NGNode
import javafx.scene.Node
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.transform.Affine
import org.joml.Matrix4d

val Canvas.caneta: Caneta
    get() = Caneta(this)

class Caneta(private val canvasJFX: Canvas) : Node() {
    private val gc: GraphicsContext = canvasJFX.graphicsContext2D

    private val processos = mutableListOf<() -> Unit>()

    override fun impl_computeContains(localX: Double, localY: Double): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun impl_processMXNode(alg: MXNodeAlgorithm?, ctx: MXNodeAlgorithmContext?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun impl_createPeer(): NGNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun impl_computeGeomBounds(bounds: BaseBounds?, tx: BaseTransform?): BaseBounds {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun fillArc(x: Double, y: Double, w: Double, h: Double, startAngle: Double, arcExtent: Double, closure: ArcType) {
        gc.fillArc(x, -y, w, h, startAngle, arcExtent, closure)
    }

    fun fillOval(x: Double, y: Double, w: Double, h: Double): () -> Unit {
        val processo = { gc.fillOval(x, -y, w, h) }
        processos += processo
        return processo
    }

    fun fillPolygon(xPoints: DoubleArray, yPoints: DoubleArray, nPoints: Int) {
        val realYPoints = DoubleArray(size = nPoints) { index -> -yPoints[index] }
        gc.fillPolygon(xPoints, realYPoints, nPoints)
    }

    fun fillRect(x: Double, y: Double, w: Double, h: Double) {
        gc.fillRect(x, -y, w, h)
    }

    fun fillRoundRect(x: Double, y: Double, w: Double, h: Double, arcWidth: Double, arcHeight: Double) {
        gc.fillRoundRect(x, -y, w, h, arcWidth, arcHeight)
    }

    fun fillText(text: String, x: Double, y: Double) {
        gc.fillText(text, x, -y)
    }

    fun fillText(text: String, x: Double, y: Double, maxWidth: Double) {
        gc.fillText(text, x, -y, maxWidth)
    }

    fun strokeArc(x: Double, y: Double, w: Double, h: Double, startAngle: Double, arcExtent: Double, closure: ArcType) {
        gc.strokeArc(x, -y, w, h, startAngle, arcExtent, closure)
    }

    fun strokeLine(x1: Double, y1: Double, x2: Double, y2: Double) {
        gc.strokeLine(x1, -y1, x2, -y2)
    }

    fun strokeOval(x: Double, y: Double, w: Double, h: Double) {
        gc.strokeOval(x, -y, w, h)
    }

    fun strokePolygon(xPoints: DoubleArray, yPoints: DoubleArray, nPoints: Int) {
        val realYPoints = DoubleArray(size = nPoints) { index -> -yPoints[index] }
        gc.strokePolygon(xPoints, realYPoints, nPoints)
    }

    fun strokePolyline(xPoints: DoubleArray, yPoints: DoubleArray, nPoints: Int) {
        val realYPoints = DoubleArray(size = nPoints) { index -> -yPoints[index] }
        gc.strokePolyline(xPoints, realYPoints, nPoints)
    }

    fun strokeRect(x: Double, y: Double, w: Double, h: Double) {
        gc.strokeRect(x, -y, w, h)
    }

    fun strokeRoundRect(x: Double, y: Double, w: Double, h: Double, arcWidth: Double, arcHeight: Double) {
        gc.strokeRoundRect(x, -y, w, h, arcWidth, arcHeight)
    }

    fun strokeText(text: String, x: Double, y: Double) {
        gc.strokeText(text, x, -y)
    }

    fun strokeText(text: String, x: Double, y: Double, maxWidth: Double) {
        gc.strokeText(text, x, -y, maxWidth)
    }

    private val transformationMatrix = TransformationMatrixImplementation(gc)
    fun initTransform(): TransformationMatrix = transformationMatrix.resetPendingOperations()

    private val affineIdentidade = Affine()
    private fun resetarAffine() {
        gc.transform = affineIdentidade
    }

    private val corFundo = Color.BLACK

    fun draw() {
        resetarAffine()
        gc.fill = corFundo
        gc.fillRect(0.0, 0.0, this.canvasJFX.width, this.canvasJFX.height)
        gc.transform = transformationMatrix.toAffine()
        processos.forEach { it.invoke() }
    }
}

private class TransformationMatrixImplementation(val gc: GraphicsContext) : TransformationMatrix {
    private val jomlMatrix = Matrix4d()
    private val pendingOperations = mutableListOf<() -> Unit>()

    fun resetPendingOperations(): TransformationMatrix {
        pendingOperations.clear()
        return this
    }

    override fun scale(scale: Double, xPivot: Double, yPivot: Double): TransformationMatrix {
        pendingOperations += { jomlMatrix.scaleAround(scale, xPivot, yPivot, 0.0) }
        return this
    }

    override fun translate(translacaoX: Double, translacaoY: Double): TransformationMatrix {
        pendingOperations += { jomlMatrix.translate(translacaoX, translacaoY, 0.0) }
        return this
    }

    override fun rotate(angle: Double/*, xPivot: Double, yPivot: Double*/): TransformationMatrix {
//        val quaterniond = Quaterniond(0.0, 0.0, 2.0, 0.0)
//        matrizJOML.rotate(rotacao,xPivot,yPivot,0.0)
//        matrizJOML.rotateAroundLocal(quaterniond, xPivot, yPivot, 0.0)
//        matrizJOML.rotate(3.14, 0.0, 0.0, 1.0)
//        matrizJOML.rotate(quaterniond)
        pendingOperations += { jomlMatrix.rotateZ(angle) }
        return this
    }

    override fun reflect(): TransformationMatrix {
//        pendingOperations += { jomlMatrix.ref }
        TODO()
    }

    override fun apply() {
        pendingOperations.forEach { it.invoke() }
        resetPendingOperations()
    }

    fun toAffine(): Affine = with(jomlMatrix) {
        javafx.scene.transform.Affine(
            m00(), m10(), m20(), m30(),
            m01(), m11(), m21(), m31(),
            m02(), m12(), m22(), m32()
        )
    }
}

interface TransformationMatrix {
    fun scale(scale: Double, xPivot: Double, yPivot: Double): TransformationMatrix
    fun translate(translacaoX: Double, translacaoY: Double): TransformationMatrix
    fun rotate(angle: Double/*, xPivot: Double, yPivot: Double*/): TransformationMatrix
    fun reflect(): TransformationMatrix
    fun apply()
}

