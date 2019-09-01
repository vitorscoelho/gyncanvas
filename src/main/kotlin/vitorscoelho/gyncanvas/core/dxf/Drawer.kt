package vitorscoelho.gyncanvas.core.dxf

import javafx.scene.canvas.Canvas
import javafx.scene.transform.Affine
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix

abstract class Drawer {
    abstract var fill: Color
    abstract var stroke: Color
    abstract var lineWidht: Double
    abstract fun fillBackground()
    abstract fun strokeLine(x1: Double, y1: Double, x2: Double, y2: Double)
    abstract fun strokeCircle(xCenter: Double, yCenter: Double, diameter: Double)
    abstract fun beginPath()
    abstract fun closePath()
    abstract fun fill()
    abstract fun stroke()
    abstract fun moveTo(x: Double, y: Double)
    abstract fun lineTo(x: Double, y: Double)
    abstract fun arcTo(xTangent1: Double, yTangent1: Double, xTangent2: Double, yTangent2: Double, radius: Double)
    abstract val transform: TransformationMatrix
    abstract fun copyToTransform(transformationMatrix: TransformationMatrix)
}

class FXDrawer(val canvas: Canvas) : Drawer() {
    private val gc = canvas.graphicsContext2D

    /**
     * Array que contém uma relação dos índices das cores e as cores do JavaFX.
     */
    private var colorIndexInCache = Array<javafx.scene.paint.Color?>(size = 256) { null }

    private fun getFXColor(color: Color): javafx.scene.paint.Color {
        if (colorIndexInCache[color.colorIndex] == null) {
            colorIndexInCache[color.colorIndex] =
                javafx.scene.paint.Color.rgb(color.red.toInt(), color.green.toInt(), color.blue.toInt())
        }
        return colorIndexInCache[color.colorIndex]!!//TODO mudar no futuro para aceitar cores que não sejam dadas pelo índice
    }

    override var fill: Color = Color.INDEX_250
        set(value) {
            field = value
            gc.fill = getFXColor(color = value)
        }

    override var stroke: Color = Color.INDEX_250
        set(value) {
            field = value
            gc.stroke = getFXColor(color = value)
        }

    override var lineWidht: Double
        set(value) {
            gc.lineWidth = value
        }
        get() = gc.lineWidth

    override fun fillBackground() =
        gc.fillRect(0.0, 0.0, this.canvas.width, this.canvas.height)

    override fun strokeLine(x1: Double, y1: Double, x2: Double, y2: Double) =
        gc.strokeLine(x1, -y1, x2, -y2)


    override fun strokeCircle(xCenter: Double, yCenter: Double, diameter: Double) {
        val radius = diameter / 2.0
        gc.strokeOval(
            xCenter - radius, -yCenter - radius,
            diameter, diameter
        )
    }

    override fun beginPath() = gc.beginPath()
    override fun closePath() = gc.closePath()
    override fun fill() = gc.fill()
    override fun stroke() = gc.stroke()
    override fun moveTo(x: Double, y: Double) = gc.moveTo(x, -y)
    override fun lineTo(x: Double, y: Double) = gc.lineTo(x, -y)
    override fun arcTo(xTangent1: Double, yTangent1: Double, xTangent2: Double, yTangent2: Double, radius: Double) =
        gc.arcTo(xTangent1, -yTangent1, xTangent2, -yTangent2, radius)

    override val transform: TransformationMatrix
        get() = mutableTransform

    private val mutableTransform = MutableTransformationMatrix()

    override fun copyToTransform(transformationMatrix: TransformationMatrix) {
        mutableTransform.set(otherMatrix = transformationMatrix)
        gc.transform = mutableTransform.toAffine()
    }

    private fun TransformationMatrix.toAffine(): Affine =
        Affine(mxx, mxy, mxz, tx, myx, myy, myz, ty, mzx, mzy, mzz, tz)
}