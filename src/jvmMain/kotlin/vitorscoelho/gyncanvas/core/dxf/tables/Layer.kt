package vitorscoelho.gyncanvas.core.dxf.tables

import vitorscoelho.gyncanvas.core.dxf.Color

class Layer(
    override val name: String,
    val color: Color
) : Table {
    init {
        require(color != Color.BY_BLOCK && color != Color.BY_LAYER) { "|color| cannot be Color.BY_BLOCK or Color.BY_LAYER." }
    }
}