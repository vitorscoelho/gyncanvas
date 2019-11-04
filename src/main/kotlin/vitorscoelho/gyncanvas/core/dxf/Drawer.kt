package vitorscoelho.gyncanvas.core.dxf

import javafx.geometry.VPos
import javafx.scene.canvas.Canvas
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.scene.transform.Affine
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPoint
import vitorscoelho.gyncanvas.core.dxf.transformation.ImmutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import java.lang.Math.toDegrees

abstract class Drawer {
    abstract val camera: Camera
    abstract var fill: Color
    abstract var stroke: Color
    abstract var lineWidht: Double
    abstract var textJustify: AttachmentPoint
    abstract var fontName: String
    abstract fun setFont(fontName: String)
    abstract fun fillBackground()
    abstract fun strokeLine(x1: Double, y1: Double, x2: Double, y2: Double)
    abstract fun strokeCircle(xCenter: Double, yCenter: Double, diameter: Double)
    abstract fun fillText(text: String, size: Double, x: Double, y: Double, angle: Double)
    abstract fun beginPath()
    abstract fun closePath()
    abstract fun fill()
    abstract fun stroke()
    abstract fun moveTo(x: Double, y: Double)
    abstract fun lineTo(x: Double, y: Double)
    abstract fun arcTo(xTangent1: Double, yTangent1: Double, xTangent2: Double, yTangent2: Double, radius: Double)
    abstract fun apllyCameraTransform()
}

class FXDrawer(val canvas: Canvas) : Drawer() {
    private val gc = canvas.graphicsContext2D
    override val camera = FXCamera(canvas = canvas)
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

    private val mapTextJustifyAutocadToJFX = mapOf(
        AttachmentPoint.BOTTOM_LEFT to { gc.textBaseline = VPos.BOTTOM;gc.textAlign = TextAlignment.LEFT },
        AttachmentPoint.BOTTOM_CENTER to { gc.textBaseline = VPos.BOTTOM;gc.textAlign = TextAlignment.CENTER },
        AttachmentPoint.BOTTOM_RIGHT to { gc.textBaseline = VPos.BOTTOM;gc.textAlign = TextAlignment.RIGHT },
        AttachmentPoint.MIDDLE_LEFT to { gc.textBaseline = VPos.CENTER;gc.textAlign = TextAlignment.LEFT },
        AttachmentPoint.MIDDLE_CENTER to { gc.textBaseline = VPos.CENTER;gc.textAlign = TextAlignment.CENTER },
        AttachmentPoint.MIDDLE_RIGHT to { gc.textBaseline = VPos.CENTER;gc.textAlign = TextAlignment.RIGHT },
        AttachmentPoint.TOP_LEFT to { gc.textBaseline = VPos.TOP;gc.textAlign = TextAlignment.LEFT },
        AttachmentPoint.TOP_CENTER to { gc.textBaseline = VPos.TOP;gc.textAlign = TextAlignment.CENTER },
        AttachmentPoint.TOP_RIGHT to { gc.textBaseline = VPos.TOP;gc.textAlign = TextAlignment.RIGHT }
    )
    private val mapTextJustifyJFXToAutocad = mapOf<VPos, Map<TextAlignment, AttachmentPoint>>(
        VPos.BOTTOM to mapOf(
            TextAlignment.LEFT to AttachmentPoint.BOTTOM_LEFT,
            TextAlignment.CENTER to AttachmentPoint.BOTTOM_CENTER,
            TextAlignment.RIGHT to AttachmentPoint.BOTTOM_RIGHT
        ),
        VPos.CENTER to mapOf(
            TextAlignment.LEFT to AttachmentPoint.MIDDLE_LEFT,
            TextAlignment.CENTER to AttachmentPoint.MIDDLE_CENTER,
            TextAlignment.RIGHT to AttachmentPoint.MIDDLE_RIGHT
        ),
        VPos.TOP to mapOf(
            TextAlignment.LEFT to AttachmentPoint.TOP_LEFT,
            TextAlignment.CENTER to AttachmentPoint.TOP_CENTER,
            TextAlignment.RIGHT to AttachmentPoint.TOP_RIGHT
        )
    )
    override var textJustify: AttachmentPoint
        set(value) {
            mapTextJustifyAutocadToJFX[value]!!.invoke()
        }
        get() = mapTextJustifyJFXToAutocad[gc.textBaseline]!![gc.textAlign]!!

    override fun fillBackground() = with(camera) {
        val deltaX = xMax - xMin
        val deltaY = yMax - yMin
        gc.fillRect(xMin, -yMax, deltaX, deltaY)
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

    override fun apllyCameraTransform() {
        gc.transform = camera.toAffine()
    }
}