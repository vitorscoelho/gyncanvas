package vitorscoelho.gyncanvas.core.dxf.tables

class TextStyle(
    override val name: String,
    val fontName: String,
    val fontFileName: String
) : Table {
}

/*
override val name: String,
        val textFont: TextFont,
        val fixedTextHeight: Double = 0.0,
        val widthFactor:Double=1.0,
        val obliqueAngle:Double=0.0,
        val textGeneration: TextGeneration = TextGeneration.NORMAL,
        val standardFlagValue: StandardFlagValue = StandardFlagValue.NORMAL
        ) : TableItem(){
 */