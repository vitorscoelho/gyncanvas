package vitorscoelho.gyncanvas.core

import javafx.geometry.VPos
import javafx.scene.canvas.Canvas
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Affine
import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPoint
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPointAlign
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPointBaseline
import vitorscoelho.gyncanvas.math.TransformationMatrix
import java.lang.Math.toDegrees

private val mapTextBaselineAutocadToJFX: Map<AttachmentPointBaseline, VPos> by lazy {
    mapOf(
        AttachmentPointBaseline.BOTTOM to VPos.BOTTOM,
        AttachmentPointBaseline.MIDDLE to VPos.CENTER,
        AttachmentPointBaseline.TOP to VPos.TOP
    )
}

private val mapTextAlignAutocadToJFX: Map<AttachmentPointAlign, TextAlignment> by lazy {
    mapOf(
        AttachmentPointAlign.LEFT to TextAlignment.LEFT,
        AttachmentPointAlign.CENTER to TextAlignment.CENTER,
        AttachmentPointAlign.RIGHT to TextAlignment.RIGHT
    )
}

private fun getTextBaseline(attachmentPointBaseline: AttachmentPointBaseline): VPos =
    mapTextBaselineAutocadToJFX[attachmentPointBaseline]!!

private fun getTextAlign(attachmentPointAlign: AttachmentPointAlign): TextAlignment =
    mapTextAlignAutocadToJFX[attachmentPointAlign]!!

class FXDrawer(val canvas: Canvas) : Drawer() {
    private val gc = canvas.graphicsContext2D

    override var canvasWidth: Double
        get() = this.canvas.width
        set(value) {}
    override var canvasHeight: Double
        get() = this.canvas.height
        set(value) {}

    /**Necessário fixar um tamanho de fonte para contornar bug do JavaFX com fontes muito pequenas*/
    private val fontSize = 10.0

    /**Fator utilizado para que o tamanho dos textos no Canvas sejam iguais aos dos textos do AutoCAD*/
    private val factorAutoCADFontSize = 1.445
    private val factorFontSize = factorAutoCADFontSize / fontSize

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

    private val fontsInCache = hashMapOf<String, Font>()
    private fun getFXFont(fontName: String): Font {
        return fontsInCache.getOrPut(
            key = fontName,
            defaultValue = { Font.font(fontName, fontSize) }
        )
    }

    override var fontName: String = gc.font.name

    override fun setFont(fontName: String) {
        this.fontName = fontName
        gc.font = getFXFont(fontName = fontName)
    }

    override var textJustify: AttachmentPoint = AttachmentPoint.BOTTOM_LEFT
        set(value) {
            field = value
            gc.textAlign = getTextAlign(value.align)
            gc.textBaseline = getTextBaseline(value.baseline)
        }

    override fun fillBackground() {
        val currentTransform = transform
        transform = TransformationMatrix.IDENTITY
        gc.fillRect(0.0, 0.0, this.canvas.width, this.canvas.height)
        transform = currentTransform
    }

    override fun strokeLine(x1: Double, y1: Double, x2: Double, y2: Double) =
        gc.strokeLine(x1, -y1, x2, -y2)

    override fun strokeCircle(xCenter: Double, yCenter: Double, diameter: Double) {
        val radius = diameter / 2.0
        gc.strokeOval(
            xCenter - radius, -yCenter - radius,
            diameter, diameter
        )
    }

    override fun fillText(text: String, size: Double, x: Double, y: Double, angle: Double) {
        gc.save()
        gc.translate(x, -y)
        val scale =
            size * factorFontSize //Existe um bug no Canvas que impede fontes muito pequenas. Por isso o ajuste do tamanho teve que ser feito escalando a matriz de transformação
        gc.scale(scale, scale)
        if (angle != 0.0) gc.rotate(toDegrees(-angle))
        gc.fillText(text, 0.0, 0.0)
        gc.restore()
    }

    override fun beginPath() = gc.beginPath()
    override fun closePath() = gc.closePath()
    override fun fill() = gc.fill()
    override fun stroke() = gc.stroke()
    override fun moveTo(x: Double, y: Double) = gc.moveTo(x, -y)
    override fun lineTo(x: Double, y: Double) = gc.lineTo(x, -y)
    override fun arcTo(xTangent1: Double, yTangent1: Double, xTangent2: Double, yTangent2: Double, radius: Double) =
        gc.arcTo(xTangent1, -yTangent1, xTangent2, -yTangent2, radius)

    protected override var transform: TransformationMatrix = TransformationMatrix.IDENTITY
        set(value) {
            field = value
            gc.transform = with(value) {
                Affine(
                    mxx, mxy, tx,
                    myx, myy, ty
                )
            }
        }
}