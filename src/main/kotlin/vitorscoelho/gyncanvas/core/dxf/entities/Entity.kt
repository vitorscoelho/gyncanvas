package vitorscoelho.gyncanvas.core.dxf.entities

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.Drawer
import vitorscoelho.gyncanvas.core.dxf.tables.Layer

interface Entity {
    val layer: Layer
    val color: Color

    fun draw(drawer: Drawer)

    fun applyProperties(drawer: Drawer) {
        drawer.lineWidht = 1.0 / drawer.transform.scale
        when (color) {
            Color.BY_LAYER -> {
                drawer.stroke = layer.color
                drawer.fill = layer.color
            }
            else -> {
                drawer.stroke = color
                drawer.fill = color
            }
        }
    }
}