package vitorscoelho.gyncanvas.core.dxf.tables

import vitorscoelho.gyncanvas.core.dxf.Color
import vitorscoelho.gyncanvas.core.dxf.blocks.Block

interface DimStyleProperties {
    //LINES PROP
    //Propriedade: Dimensions lines
    val dimensionLinesColor: Color?//DIMCLRD - 176
    //    val dimensionLinesLType: LType = LType.BY_BLOCK,//DIMLTYPE
//    val dimensionLinesLineWeight: LineWeight = LineWeight.BY_BLOCK,//DIMLWD
//    val dimensionLinesBaselineSpacing: Double = 0.38,//DIMDLI
    val dimensionLinesSuppressDimLine1: Boolean?//DIMSD1 - 281
    val dimensionLinesSuppressDimLine2: Boolean?//DIMSD1 - 282

    //Propriedade: Extension lines
    val extensionLinesColor: Color?//DIMCLRE - 177
    //    val extensionLinesLTypeExtLine1: LType = LType.BY_BLOCK,//DIMLTEX1
//    val extensionLinesLTypeExtLine2: LType = LType.BY_BLOCK,//DIMLTEX2
//    val extensionLinesLineWeight: LineWeight = LineWeight.BY_BLOCK,//DIMLWE - 372
    val extensionLinesSuppressExtLine1: Boolean?//DIMSE1 - 75
    val extensionLinesSuppressExtLine2: Boolean?//DIMSE2 - 76
    val extensionLinesExtendBeyondDimLines: Double?//DIMEXE - 44
    val extensionLinesOffsetFromOrigin: Double? //DIMEXO - 42
    //val fixedLengthExtensionLines:Double//DIMFXLON. PARECE QUE NÃO EXISTE PARA DXF2000


    //SYMBOLS AND ARROWS PROP
    //Propriedade: Arrowheads
    val firstArrowHead: Block? //DIMBLK1
    val secondArrowHead: Block? //DIMBLK2
    val leaderArrowHead: Block? //DIMLDRBLK
    val arrowSize: Double? //DIMASZ

    //Propriedade: Center marks
//    val centerMarkType: CenterMarkType = CenterMarkType.MARK,//DIMCEN - 141
//    val centerMarkSize: Double=0.09,//DIMCEN - 141

    //Propriedade: Dimension Break
//    val breakSize: Double=0.125//NÃO ACHEI NA REFERÊNCIA DO AUTOCAD2000


    //TEXT PROP
    //Propriedade: TextAppearance
    val textStyle: TextStyle?//DIMTXSTY - 340
    val textColor: Color? //DIMCLRT - 178
    //private fillColor:Color,//DIMTFILL e DIMTFILLCLR. NÃO ENCONTREI NAS REFERÊNCIAS
    val textHeight: Double?//DIMTXT - 140
    //    val textInBox: Boolean = false,//DIMGAP - 147
    //Propriedade: TextPlacement
//    val  textPlacementVertical: TextPlacementVertical = TextPlacementVertical.ABOVE,//DIMTAD - 77
//    val  textPlacementHorizontal: TextPlacementHorizontal = TextPlacementHorizontal.CENTERED,//DIMJUST - 280
    //private  textViewDirection:TextViewDirection,//DIMTXTDIRECTION NÃO ENCONTREI NA REFERÊNCIA DA AUTODESK
    val textOffsetFromDimLine: Double?//DIMGAP - 147 (O VALOR FICA NEGATIVO SE A VARIAVEL textoEmCaixa DA CLASSE TextAppearance for true) //CORRIGIR ISTO
    //Propriedade: TextAlignment
//    val textAlignment: TextAlignment = TextAlignment.ALIGNED_WITH_DIMENSION_LINE


    //FIT PROP
    //Propriedade: FitOptions
//    val fitType: FitType = FitType.EITHER_TEXT_OR_ARROWS,//DIMATFIT - 289 Altera posição do texto se ele não couber dentro das extension line
//    val alwaysKeepTextBetweenExtLine: Boolean = false,//DIMTIX - 174 O texto sempre vai ficar no centro das extension line
//    val suppressArrowsIfDontFit: Boolean = false,//DIMSOXD - 175 As setas são suprimidas se não couberem nas extension line
    //Propriedade: TextPlacement
//    val textPlacementType: TextPlacementType = TextPlacementType.BESIDE_THE_DIM_LINE,//DIMTMOVE - 279
    //Propriedade: ScaleForDimensionFeatures
    val overallScale: Double?//DIMSCALE - 40
    //Propriedade: FineTuning
//    val placeTextManually: Boolean = false,//DIMUPT - 288
//    val drawDimLineBetweenExtLines: Boolean = false//DIMTOFL - 172


    //PRIMARY UNITS PROP
    //Propriedade: LinearDimensions
//    val linearDimensionUnitFormat: LinearDimensionUnitFormat = LinearDimensionUnitFormat.Decimal,//DIMLUNIT - 277
    val linearDimensionPrecision: Int?//DIMDEC - 271
    //DIMFRAC - NÃO SEI. NÃO ACHEI
    val decimalSeparator: Char?//DIMDSEP - 278
    val unitRound: Double? //DIMRND - 45
    val prefix: String? //DIMPOST - 3 NÃO SEI
    val suffix: String? //DIMPOST - 3
    val scaleFactor: Double? //DIMLFAC - 144
    //    val linearDimensionZeroSuppress;
    val linearDimensionSuppressLeadingZeros: Boolean?
    val linearDimensionSuppressTrailingZeros: Boolean?
    //Propriedade: AngularDimensions
//    val angularDimensionUnitFormat: AngularDimensionUnitFormat = AngularDimensionUnitFormat.DECIMAL_DEGREES,//DIMAUNIT - 275
    val angularDimensionPrecision: Int?//DIMADEC - 179
//    val angularDimensionZeroSuppress: AngularDimensionZeroSuppress = AngularDimensionZeroSuppress.NaoSuprimir//DIMAZIN - 79
}