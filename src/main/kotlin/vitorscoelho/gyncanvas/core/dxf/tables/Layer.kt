package vitorscoelho.gyncanvas.core.dxf.tables

import vitorscoelho.gyncanvas.core.dxf.Color

class Layer(
    override val name: String,
    val color: Color
) : Table