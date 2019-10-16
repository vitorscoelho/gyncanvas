package vitorscoelho.gyncanvas.core.dxf.tables

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.blocks.Block
import vitorscoelho.gyncanvas.utils.NumberFormatter

class DimStyle(
    override val name: String,

    //LINES PROP
    //Propriedade: Dimensions lines
    override val dimensionLinesColor: Color = Color.BY_BLOCK,//DIMCLRD - 176
    override val dimensionLinesSuppressDimLine1: Boolean = false,//DIMSD1 - 281
    override val dimensionLinesSuppressDimLine2: Boolean = false,//DIMSD1 - 282

    //Propriedade: Extension lines
    override val extensionLinesColor: Color = Color.BY_BLOCK,//DIMCLRE - 177
    override val extensionLinesSuppressExtLine1: Boolean = false,//DIMSE1 - 75
    override val extensionLinesSuppressExtLine2: Boolean = false,//DIMSE2 - 76
    override val extensionLinesExtendBeyondDimLines: Double = 0.18,//DIMEXE - 44
    override val extensionLinesOffsetFromOrigin: Double = 0.0625,//DIMEXO - 42

    //SYMBOLS AND ARROWS PROP
    //Propriedade: Arrowheads
    override val firstArrowHead: Block = Block.NONE,//DIMBLK1
    override val secondArrowHead: Block = Block.NONE,//DIMBLK2
    override val leaderArrowHead: Block = Block.NONE,//DIMLDRBLK
    override val arrowSize: Double = 0.18,//DIMASZ

    //TEXT PROP
    //Propriedade: TextAppearance
    override val textStyle: TextStyle,//DIMTXSTY - 340
    override val textColor: Color = Color.BY_BLOCK,//DIMCLRT - 178
    override val textHeight: Double = 0.18,//DIMTXT - 140
    //Propriedade: TextPlacement
    override val textOffsetFromDimLine: Double = 0.09,//DIMGAP - 147 (O VALOR FICA NEGATIVO SE A VARIAVEL textoEmCaixa DA CLASSE TextAppearance for true) //CORRIGIR ISTO

    //FIT PROP
    //Propriedade: ScaleForDimensionFeatures
    override val overallScale: Double = 1.0,//DIMSCALE - 40

    //PRIMARY UNITS PROP
    //Propriedade: LinearDimensions
    override val linearDimensionPrecision: Int = 2,//DIMDEC - 271
    override val decimalSeparator: Char = '.',//DIMDSEP - 278
    override val unitRound: Double = 0.0,//DIMRND - 45
    override val prefix: String = "",//DIMPOST - 3 NÃO SEI
    override val suffix: String = "",//DIMPOST - 3
    override val scaleFactor: Double = 1.0,//DIMLFAC - 144
    override val linearDimensionSuppressLeadingZeros: Boolean = false,
    override val linearDimensionSuppressTrailingZeros: Boolean = true,
    //Propriedade: AngularDimensions
    override val angularDimensionPrecision: Int = 0//DIMADEC - 179
) : Table,DimStyleProperties {
    private val linearDimensionFormatter = NumberFormatter(
        suppressLeadingZeros = linearDimensionSuppressLeadingZeros,
        suppressTrailingZeros = linearDimensionSuppressTrailingZeros,
        precision = linearDimensionPrecision,
        decimalSeparator = decimalSeparator,
        prefix = prefix,
        suffix = suffix,
        roundoff = unitRound
    )

    fun linearDimensionFormat(valor: Double): String = linearDimensionFormatter.format(valor = valor * this.scaleFactor)
}