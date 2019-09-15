package vitorscoelho.gyncanvas.core.dxf.tables

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.blocks.Block

class DimStyle(
    override val name: String,

    //LINES PROP
    //Propriedade: Dimensions lines
    val dimensionLinesColor: Color = Color.BY_BLOCK,//DIMCLRD - 176
//    val dimensionLinesLType: LType = LType.BY_BLOCK,//DIMLTYPE
//    val dimensionLinesLineWeight: LineWeight = LineWeight.BY_BLOCK,//DIMLWD
    val dimensionLinesBaselineSpacing: Double = 0.38,//DIMDLI
    val dimensionLinesSupressDimLine1: Boolean = false,//DIMSD1 - 281
    val dimensionLinesSupressDimLine2: Boolean = false,//DIMSD1 - 282

    //Propriedade: Extension lines
    val extensionLinesColor: Color = Color.BY_BLOCK,//DIMCLRE - 177
//    val extensionLinesLTypeExtLine1: LType = LType.BY_BLOCK,//DIMLTEX1
//    val extensionLinesLTypeExtLine2: LType = LType.BY_BLOCK,//DIMLTEX2
//    val extensionLinesLineWeight: LineWeight = LineWeight.BY_BLOCK,//DIMLWE - 372
    val extensionLinesSupressExtLine1: Boolean = false,//DIMSE1 - 75
    val extensionLinesSupressExtLine2: Boolean = false,//DIMSE2 - 76
    val extensionLinesExtendBeyondDimLines: Double = 0.18,//DIMEXE - 44
    val extensionLinesOffsetFromOrigin: Double = 0.0625,//DIMEXO - 42
    //val fixedLengthExtensionLines:Double//DIMFXLON. PARECE QUE NÃO EXISTE PARA DXF2000


    //SYMBOLS AND ARROWS PROP
    //Propriedade: Arrowheads
    val firstArrowHead: Block,//DIMBLK1
    val secondArrowHead: Block,//DIMBLK2
    val leaderArrowHead: Block,//DIMLDRBLK
    val arrowSize: Double = 0.18,//DIMASZ

    //Propriedade: Center marks
//    val centerMarkType: CenterMarkType = CenterMarkType.MARK,//DIMCEN - 141
//    val centerMarkSize: Double=0.09,//DIMCEN - 141

    //Propriedade: Dimension Break
//    val breakSize: Double=0.125//NÃO ACHEI NA REFERÊNCIA DO AUTOCAD2000


    //TEXT PROP
    //Propriedade: TextAppearance
    val textStyle: TextStyle,//DIMTXSTY - 340
    val textColor: Color = Color.BY_BLOCK,//DIMCLRT - 178
    //private fillColor:Color,//DIMTFILL e DIMTFILLCLR. NÃO ENCONTREI NAS REFERÊNCIAS
    val textHeight: Double = 0.18,//DIMTXT - 140
//    val textInBox: Boolean = false,//DIMGAP - 147
    //Propriedade: TextPlacement
//    val  textPlacementVertical: TextPlacementVertical = TextPlacementVertical.ABOVE,//DIMTAD - 77
//    val  textPlacementHorizontal: TextPlacementHorizontal = TextPlacementHorizontal.CENTERED,//DIMJUST - 280
    //private  textViewDirection:TextViewDirection,//DIMTXTDIRECTION NÃO ENCONTREI NA REFERÊNCIA DA AUTODESK
    val offsetFromDimLine: Double = 0.09,//DIMGAP - 147 (O VALOR FICA NEGATIVO SE A VARIAVEL textoEmCaixa DA CLASSE TextAppearance for true) //CORRIGIR ISTO
    //Propriedade: TextAlignment
//    val textAlignment: TextAlignment = TextAlignment.ALIGNED_WITH_DIMENSION_LINE


    //FIT PROP
    //Propriedade: FitOptions
//    val fitType: FitType = FitType.EITHER_TEXT_OR_ARROWS,//DIMATFIT - 289 Altera posição do texto se ele não couber dentro das extension line
//    val alwaysKeepTextBetweenExtLine: Boolean = false,//DIMTIX - 174 O texto sempre vai ficar no centro das extension line
    val supressArrowsIfDontFit: Boolean = false,//DIMSOXD - 175 As setas são suprimidas se não couberem nas extension line
    //Propriedade: TextPlacement
//    val textPlacementType: TextPlacementType = TextPlacementType.BESIDE_THE_DIM_LINE,//DIMTMOVE - 279
    //Propriedade: ScaleForDimensionFeatures
    val overallScale: Double = 1.0,//DIMSCALE - 40
    //Propriedade: FineTuning
//    val placeTextManually: Boolean = false,//DIMUPT - 288
//    val drawDimLineBetweenExtLines: Boolean = false//DIMTOFL - 172


    //PRIMARY UNITS PROP
    //Propriedade: LinearDimensions
//    val linearDimensionUnitFormat: LinearDimensionUnitFormat = LinearDimensionUnitFormat.Decimal,//DIMLUNIT - 277
    val linearDimensionPrecision: Int = 2,//DIMDEC - 271
    //DIMFRAC - NÃO SEI. NÃO ACHEI
    val decimalSeparator: DecimalSeparator = DecimalSeparator.PERIOD,//DIMDSEP - 278
    val unitRound: Double = 0.0,//DIMRND - 45
    //val prefix:String,//DIMPOST - 3 NÃO SEI
    val suffix: String = "",//DIMPOST - 3
    val scaleFactor: Double = 1.0,//DIMLFAC - 144
//    val linearDimensionZeroSupress: LinearDimensionZeroSupress = LinearDimensionZeroSupress.NaoSuprimir,//DIMZIN - 78

    //Propriedade: AngularDimensions
//    val angularDimensionUnitFormat: AngularDimensionUnitFormat = AngularDimensionUnitFormat.DECIMAL_DEGREES,//DIMAUNIT - 275
    val angularDimensionPrecision: Int = 0//DIMADEC - 179
//    val angularDimensionZeroSupress: AngularDimensionZeroSupress = AngularDimensionZeroSupress.NaoSuprimir//DIMAZIN - 79
):Table {
}

enum class DecimalSeparator(val character: Char) {
    COMMA(character = ','),
    PERIOD(character = '.'),
    SPACE(character = ' ')
}