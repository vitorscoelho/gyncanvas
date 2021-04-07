package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.tables.Layer
import vitorscoelho.gyncanvas.core.dxf.tables.TextStyle
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPointBaseline.*
import vitorscoelho.gyncanvas.core.dxf.entities.AttachmentPointAlign.*
import vitorscoelho.gyncanvas.core.primitives.Primitive
import vitorscoelho.gyncanvas.math.Vector

data class MText(
    override val layer: Layer,
    override val color: Color = Color.BY_LAYER,
    val style: TextStyle,
    val size: Double,
    val justify: AttachmentPoint = AttachmentPoint.BOTTOM_LEFT,
    val rotation: Double = 0.0,
    val position: Vector,
    val content: String
) : Entity {
//    override val primitives: List<Primitive>
//        get() = TODO("Not yet implemented")

    override fun forEachPrimitive(action: (Primitive) -> Unit) {
        TODO("Not yet implemented")
    }

//    override fun draw(drawer: Drawer) {
//        applyLineWidth(drawer = drawer)
//        drawer.setFont(fontName = style.fontFileName)
//        drawer.textJustify = justify
//        drawer.fillText(text = content, size = size, x = position.x, y = position.y, angle = rotation)
//    }
}

enum class AttachmentPoint(val value: Byte, val baseline: AttachmentPointBaseline, val align: AttachmentPointAlign) {
    TOP_LEFT(value = 1, baseline = TOP, align = LEFT),
    TOP_CENTER(value = 2, baseline = TOP, align = CENTER),
    TOP_RIGHT(value = 3, baseline = TOP, align = RIGHT),
    MIDDLE_LEFT(value = 4, baseline = MIDDLE, align = LEFT),
    MIDDLE_CENTER(value = 5, baseline = MIDDLE, align = CENTER),
    MIDDLE_RIGHT(value = 6, baseline = MIDDLE, align = RIGHT),
    BOTTOM_LEFT(value = 7, baseline = BOTTOM, align = LEFT),
    BOTTOM_CENTER(value = 8, baseline = BOTTOM, align = CENTER),
    BOTTOM_RIGHT(value = 9, baseline = BOTTOM, align = RIGHT);
}

enum class AttachmentPointBaseline { TOP, MIDDLE, BOTTOM }
enum class AttachmentPointAlign { LEFT, CENTER, RIGHT }