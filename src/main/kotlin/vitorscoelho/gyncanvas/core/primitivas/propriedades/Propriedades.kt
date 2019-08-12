package vitorscoelho.gyncanvas.core.primitivas.propriedades

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.StrokeLineCap
import javafx.scene.text.Font
import javafx.scene.text.FontSmoothingType
import javafx.scene.text.TextAlignment
import vitorscoelho.gyncanvas.core.Transformacoes

interface DrawAttributes {
    fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes)
}

class FillAttributes(
    val fillPaint: Paint = Color.BLACK
) : DrawAttributes {
    override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.fill = fillPaint
    }
}

class StrokeAttributes(
    val strokePaint: Paint = Color.BLACK,
    val lineWidth: Double = 1.0,
    val lineCap: StrokeLineCap = StrokeLineCap.SQUARE,
    val miterLimit: Double = 10.0,
    //val dashes: DoubleArray? = null,
    val dashOffset: Double = 0.0,
    val fixedWidth: Boolean = true
) : DrawAttributes {
    override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
        gc.stroke = strokePaint
        gc.lineWidth = if (fixedWidth) lineWidth / transformacoes.escala else lineWidth
        gc.lineCap = lineCap
        gc.miterLimit = miterLimit
        gc.lineDashOffset = dashOffset
    }
}

class FillTextAttributes(
    val font: Font = Font.getDefault(),
    val textAlign: TextAlignment = TextAlignment.LEFT,
    val textBaselinte: VPos = VPos.BASELINE,
    val fontSmoothing: FontSmoothingType = FontSmoothingType.GRAY,
    val fillAtributtes: FillAttributes

) : DrawAttributes {
    override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
        fillAtributtes.aplicar(gc = gc, transformacoes = transformacoes)
        gc.font = font
        gc.textAlign = textAlign
        gc.textBaseline = textBaselinte
        gc.fontSmoothingType = fontSmoothing
    }
}