package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle
import vitorscoelho.gyncanvas.core.dxf.transformation.ImmutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.MutableTransformationMatrix
import vitorscoelho.gyncanvas.core.dxf.transformation.TransformationMatrix
import vitorscoelho.gyncanvas.math.Vetor2D

data class MText(
    override val properties: EntityProperties,
    val style: TextStyle,
    val size: Double,
    val justify: AttachmentPoint = AttachmentPoint.BOTTOM_LEFT,
    val rotation: Double = 0.0,
    val position: Vetor2D,
    val content: String
) : Entity {
    private val tipoTexto: TipoTexto = TipoTexto.getTextType(
        rotation = rotation,
        fixedSize = false
    )//TODO Criar outro tipo de entidade que permite um texto com tamanho fixo

    override fun applyProperties(drawer: Drawer) {
        applyLineWidth(drawer = drawer)
        applyColor(drawer = drawer, layer = layer, color = color)
        drawer.setFont(fontName = style.fontFileName, fontSize = size)
        drawer.textJustify = justify
        /*
        override fun aplicar(gc: GraphicsContext, transformacoes: Transformacoes) {
        fillAtributtes.aplicar(gc = gc, transformacoes = transformacoes)
        gc.font = font
        gc.textAlign = textAlign
        gc.textBaseline = textBaselinte
        gc.fontSmoothingType = fontSmoothing
    }
         */
    }

    override fun draw(drawer: Drawer) {
        tipoTexto.draw(mText = this, drawer = drawer)
    }

    override fun transform(transformationMatrix: TransformationMatrix): MText =
        copy(
            size = size * transformationMatrix.scale,
            //justify = TODO fazer uma maneira de identificar o reflect
            //rotation = TODO
            position = position.transform(transformationMatrix)
        )
    /*
    override val properties: EntityProperties,
    val style: TextStyle,
    val size: Double,
    val justify: AttachmentPoint = AttachmentPoint.BOTTOM_LEFT,
    val rotation: Double = 0.0,
    val position: Vetor2D,
    val content: String
     */
}

private enum class TipoTexto {
    HORIZONTAL {
        override fun draw(mText: MText, drawer: Drawer) {
            drawer.fillText(text = mText.content, x = mText.position.x, y = mText.position.y)
        }
    },
    ROTATED {
        override fun draw(mText: MText, drawer: Drawer) {
            val matrizOriginal = ImmutableTransformationMatrix(otherMatrix = drawer.transform)
            drawer.copyToTransform(
                transformationMatrix = MutableTransformationMatrix(otherMatrix = matrizOriginal)
                    .translate(xOffset = mText.position.x, yOffset = mText.position.y)
                    .rotate(angle = -mText.rotation)
            )
            drawer.fillText(text = mText.content, x = mText.position.x, y = mText.position.y)
            drawer.copyToTransform(transformationMatrix = matrizOriginal)
        }
    },
    HORIZONTAL_FIXED_SIZE {
        override fun draw(mText: MText, drawer: Drawer) {
            val matrizOriginal = ImmutableTransformationMatrix(otherMatrix = drawer.transform)
            drawer.copyToTransform(
                transformationMatrix = MutableTransformationMatrix(otherMatrix = matrizOriginal)
                    .translate(xOffset = mText.position.x, yOffset = -mText.position.y)
                    .scale(factor = 1.0 / drawer.transform.scale, xOrigin = 0.0, yOrigin = 0.0)
            )
            drawer.fillText(text = mText.content, x = 0.0, y = 0.0)
            drawer.copyToTransform(transformationMatrix = matrizOriginal)
        }
    },
    ROTATED_FIXED_SIZE {
        override fun draw(mText: MText, drawer: Drawer) {
            val matrizOriginal = ImmutableTransformationMatrix(otherMatrix = drawer.transform)
            drawer.copyToTransform(
                transformationMatrix = MutableTransformationMatrix(otherMatrix = matrizOriginal)
                    .translate(xOffset = mText.position.x, yOffset = -mText.position.y)
                    .rotate(angle = -mText.rotation)
                    .scale(factor = 1.0 / drawer.transform.scale, xOrigin = 0.0, yOrigin = 0.0)
            )
            drawer.fillText(text = mText.content, x = 0.0, y = 0.0)
            drawer.copyToTransform(transformationMatrix = matrizOriginal)
        }
    };

    abstract fun draw(mText: MText, drawer: Drawer)

    companion object {
        fun getTextType(rotation: Double, fixedSize: Boolean): TipoTexto {
            if (fixedSize) {
                return when (rotation == 0.0) {
                    true -> HORIZONTAL_FIXED_SIZE
                    false -> ROTATED_FIXED_SIZE
                }
            } else {
                return when (rotation == 0.0) {
                    true -> HORIZONTAL
                    false -> ROTATED
                }
            }
        }
    }
}

enum class AttachmentPoint(val value: Byte) {
    TOP_LEFT(value = 1), TOP_CENTER(value = 2), TOP_RIGHT(value = 3), MIDDLE_LEFT(value = 4), MIDDLE_CENTER(value = 5),
    MIDDLE_RIGHT(value = 6), BOTTOM_LEFT(value = 7), BOTTOM_CENTER(value = 8), BOTTOM_RIGHT(value = 9)
}