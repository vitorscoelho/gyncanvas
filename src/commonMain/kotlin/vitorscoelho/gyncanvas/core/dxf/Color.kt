package vitorscoelho.gyncanvas.core.dxf

import vitorscoelho.gyncanvas.core.dxf.entities.Entity

abstract class Color private constructor() : vitorscoelho.gyncanvas.core.primitives.Color {
    abstract val colorIndex: Int
    abstract val dxfRed: Short
    abstract val dxfGreen: Short
    abstract val dxfBlue: Short
    internal open fun effectiveColor(entity: Entity): Color = this //TODO mudar o método para não aceitar nulo. Mudar owner para tipo Block e usar Block.MODEL como padrão

    override val red: Float get() = dxfRed.toFloat() / 255f
    override val green: Float get() = dxfGreen.toFloat() / 255f
    override val blue: Float get() = dxfBlue.toFloat() / 255f

    private class ColorImplementation(
        override val colorIndex: Int,
        override val dxfRed: Short,
        override val dxfGreen: Short,
        override val dxfBlue: Short
    ) : Color()

    companion object {
        val BY_BLOCK: Color by lazy {
            object : Color() {
                override val colorIndex: Int = 0
                override val dxfRed: Short = 0
                override val dxfGreen: Short = 0
                override val dxfBlue: Short = 0
                override fun effectiveColor(entity: Entity): Color {
                    //Chama a função várias vezes até que a resposta seja diferente de BY_BLOCK
//                    return owner.color.effectiveColor(layerColor=owner.layer,owner =
//                    if(owner.color!=BY_BLOCK) return owner
                    //Corrigir este método para ir buscando o owner até chegar ao MODEL
                    //Estudar o Decorator para ver se ele atende isto
                    //colocando uma nova variavel no objeto ('val owner, por exemplo)
                    return entity.layer.color//TODO ver o que fazer
                }
            }
        }
        val BY_LAYER: Color by lazy {
            object : Color() {
                override val colorIndex: Int = 256
                override val dxfRed: Short = 0
                override val dxfGreen: Short = 0
                override val dxfBlue: Short = 0
                override fun effectiveColor(entity: Entity): Color = entity.layer.color
            }
        }

        fun fromIndex(colorIndex: Int): Color {
            return when (colorIndex) {
                0 -> BY_BLOCK
                1 -> INDEX_1
                2 -> INDEX_2
                3 -> INDEX_3
                4 -> INDEX_4
                5 -> INDEX_5
                6 -> INDEX_6
                7 -> INDEX_7
                8 -> INDEX_8
                9 -> INDEX_9
                10 -> INDEX_10
                11 -> INDEX_11
                12 -> INDEX_12
                13 -> INDEX_13
                14 -> INDEX_14
                15 -> INDEX_15
                16 -> INDEX_16
                17 -> INDEX_17
                18 -> INDEX_18
                19 -> INDEX_19
                20 -> INDEX_20
                21 -> INDEX_21
                22 -> INDEX_22
                23 -> INDEX_23
                24 -> INDEX_24
                25 -> INDEX_25
                26 -> INDEX_26
                27 -> INDEX_27
                28 -> INDEX_28
                29 -> INDEX_29
                30 -> INDEX_30
                31 -> INDEX_31
                32 -> INDEX_32
                33 -> INDEX_33
                34 -> INDEX_34
                35 -> INDEX_35
                36 -> INDEX_36
                37 -> INDEX_37
                38 -> INDEX_38
                39 -> INDEX_39
                40 -> INDEX_40
                41 -> INDEX_41
                42 -> INDEX_42
                43 -> INDEX_43
                44 -> INDEX_44
                45 -> INDEX_45
                46 -> INDEX_46
                47 -> INDEX_47
                48 -> INDEX_48
                49 -> INDEX_49
                50 -> INDEX_50
                51 -> INDEX_51
                52 -> INDEX_52
                53 -> INDEX_53
                54 -> INDEX_54
                55 -> INDEX_55
                56 -> INDEX_56
                57 -> INDEX_57
                58 -> INDEX_58
                59 -> INDEX_59
                60 -> INDEX_60
                61 -> INDEX_61
                62 -> INDEX_62
                63 -> INDEX_63
                64 -> INDEX_64
                65 -> INDEX_65
                66 -> INDEX_66
                67 -> INDEX_67
                68 -> INDEX_68
                69 -> INDEX_69
                70 -> INDEX_70
                71 -> INDEX_71
                72 -> INDEX_72
                73 -> INDEX_73
                74 -> INDEX_74
                75 -> INDEX_75
                76 -> INDEX_76
                77 -> INDEX_77
                78 -> INDEX_78
                79 -> INDEX_79
                80 -> INDEX_80
                81 -> INDEX_81
                82 -> INDEX_82
                83 -> INDEX_83
                84 -> INDEX_84
                85 -> INDEX_85
                86 -> INDEX_86
                87 -> INDEX_87
                88 -> INDEX_88
                89 -> INDEX_89
                90 -> INDEX_90
                91 -> INDEX_91
                92 -> INDEX_92
                93 -> INDEX_93
                94 -> INDEX_94
                95 -> INDEX_95
                96 -> INDEX_96
                97 -> INDEX_97
                98 -> INDEX_98
                99 -> INDEX_99
                100 -> INDEX_100
                101 -> INDEX_101
                102 -> INDEX_102
                103 -> INDEX_103
                104 -> INDEX_104
                105 -> INDEX_105
                106 -> INDEX_106
                107 -> INDEX_107
                108 -> INDEX_108
                109 -> INDEX_109
                110 -> INDEX_110
                111 -> INDEX_111
                112 -> INDEX_112
                113 -> INDEX_113
                114 -> INDEX_114
                115 -> INDEX_115
                116 -> INDEX_116
                117 -> INDEX_117
                118 -> INDEX_118
                119 -> INDEX_119
                120 -> INDEX_120
                121 -> INDEX_121
                122 -> INDEX_122
                123 -> INDEX_123
                124 -> INDEX_124
                125 -> INDEX_125
                126 -> INDEX_126
                127 -> INDEX_127
                128 -> INDEX_128
                129 -> INDEX_129
                130 -> INDEX_130
                131 -> INDEX_131
                132 -> INDEX_132
                133 -> INDEX_133
                134 -> INDEX_134
                135 -> INDEX_135
                136 -> INDEX_136
                137 -> INDEX_137
                138 -> INDEX_138
                139 -> INDEX_139
                140 -> INDEX_140
                141 -> INDEX_141
                142 -> INDEX_142
                143 -> INDEX_143
                144 -> INDEX_144
                145 -> INDEX_145
                146 -> INDEX_146
                147 -> INDEX_147
                148 -> INDEX_148
                149 -> INDEX_149
                150 -> INDEX_150
                151 -> INDEX_151
                152 -> INDEX_152
                153 -> INDEX_153
                154 -> INDEX_154
                155 -> INDEX_155
                156 -> INDEX_156
                157 -> INDEX_157
                158 -> INDEX_158
                159 -> INDEX_159
                160 -> INDEX_160
                161 -> INDEX_161
                162 -> INDEX_162
                163 -> INDEX_163
                164 -> INDEX_164
                165 -> INDEX_165
                166 -> INDEX_166
                167 -> INDEX_167
                168 -> INDEX_168
                169 -> INDEX_169
                170 -> INDEX_170
                171 -> INDEX_171
                172 -> INDEX_172
                173 -> INDEX_173
                174 -> INDEX_174
                175 -> INDEX_175
                176 -> INDEX_176
                177 -> INDEX_177
                178 -> INDEX_178
                179 -> INDEX_179
                180 -> INDEX_180
                181 -> INDEX_181
                182 -> INDEX_182
                183 -> INDEX_183
                184 -> INDEX_184
                185 -> INDEX_185
                186 -> INDEX_186
                187 -> INDEX_187
                188 -> INDEX_188
                189 -> INDEX_189
                190 -> INDEX_190
                191 -> INDEX_191
                192 -> INDEX_192
                193 -> INDEX_193
                194 -> INDEX_194
                195 -> INDEX_195
                196 -> INDEX_196
                197 -> INDEX_197
                198 -> INDEX_198
                199 -> INDEX_199
                200 -> INDEX_200
                201 -> INDEX_201
                202 -> INDEX_202
                203 -> INDEX_203
                204 -> INDEX_204
                205 -> INDEX_205
                206 -> INDEX_206
                207 -> INDEX_207
                208 -> INDEX_208
                209 -> INDEX_209
                210 -> INDEX_210
                211 -> INDEX_211
                212 -> INDEX_212
                213 -> INDEX_213
                214 -> INDEX_214
                215 -> INDEX_215
                216 -> INDEX_216
                217 -> INDEX_217
                218 -> INDEX_218
                219 -> INDEX_219
                220 -> INDEX_220
                221 -> INDEX_221
                222 -> INDEX_222
                223 -> INDEX_223
                224 -> INDEX_224
                225 -> INDEX_225
                226 -> INDEX_226
                227 -> INDEX_227
                228 -> INDEX_228
                229 -> INDEX_229
                230 -> INDEX_230
                231 -> INDEX_231
                232 -> INDEX_232
                233 -> INDEX_233
                234 -> INDEX_234
                235 -> INDEX_235
                236 -> INDEX_236
                237 -> INDEX_237
                238 -> INDEX_238
                239 -> INDEX_239
                240 -> INDEX_240
                241 -> INDEX_241
                242 -> INDEX_242
                243 -> INDEX_243
                244 -> INDEX_244
                245 -> INDEX_245
                246 -> INDEX_246
                247 -> INDEX_247
                248 -> INDEX_248
                249 -> INDEX_249
                250 -> INDEX_250
                251 -> INDEX_251
                252 -> INDEX_252
                253 -> INDEX_253
                254 -> INDEX_254
                256 -> BY_LAYER
                else -> throw IllegalArgumentException("|colorIndex|=$colorIndex, mas deveria estar no intervalo 0<=|colorIndex|<=256.")//TODO Traduzir
            }
        }

        val INDEX_1: Color = ColorImplementation(colorIndex = 1, dxfRed = 255, dxfGreen = 0, dxfBlue = 0)
        val INDEX_2: Color = ColorImplementation(colorIndex = 2, dxfRed = 255, dxfGreen = 255, dxfBlue = 0)
        val INDEX_3: Color = ColorImplementation(colorIndex = 3, dxfRed = 0, dxfGreen = 255, dxfBlue = 0)
        val INDEX_4: Color = ColorImplementation(colorIndex = 4, dxfRed = 0, dxfGreen = 255, dxfBlue = 255)
        val INDEX_5: Color = ColorImplementation(colorIndex = 5, dxfRed = 0, dxfGreen = 0, dxfBlue = 255)
        val INDEX_6: Color = ColorImplementation(colorIndex = 6, dxfRed = 255, dxfGreen = 0, dxfBlue = 255)
        val INDEX_7: Color = ColorImplementation(colorIndex = 7, dxfRed = 255, dxfGreen = 255, dxfBlue = 255)
        val INDEX_8: Color = ColorImplementation(colorIndex = 8, dxfRed = 65, dxfGreen = 65, dxfBlue = 65)
        val INDEX_9: Color = ColorImplementation(colorIndex = 9, dxfRed = 128, dxfGreen = 128, dxfBlue = 128)
        val INDEX_10: Color = ColorImplementation(colorIndex = 10, dxfRed = 255, dxfGreen = 0, dxfBlue = 0)
        val INDEX_11: Color = ColorImplementation(colorIndex = 11, dxfRed = 255, dxfGreen = 170, dxfBlue = 170)
        val INDEX_12: Color = ColorImplementation(colorIndex = 12, dxfRed = 189, dxfGreen = 0, dxfBlue = 0)
        val INDEX_13: Color = ColorImplementation(colorIndex = 13, dxfRed = 189, dxfGreen = 126, dxfBlue = 126)
        val INDEX_14: Color = ColorImplementation(colorIndex = 14, dxfRed = 129, dxfGreen = 0, dxfBlue = 0)
        val INDEX_15: Color = ColorImplementation(colorIndex = 15, dxfRed = 129, dxfGreen = 86, dxfBlue = 86)
        val INDEX_16: Color = ColorImplementation(colorIndex = 16, dxfRed = 104, dxfGreen = 0, dxfBlue = 0)
        val INDEX_17: Color = ColorImplementation(colorIndex = 17, dxfRed = 104, dxfGreen = 69, dxfBlue = 69)
        val INDEX_18: Color = ColorImplementation(colorIndex = 18, dxfRed = 79, dxfGreen = 0, dxfBlue = 0)
        val INDEX_19: Color = ColorImplementation(colorIndex = 19, dxfRed = 79, dxfGreen = 53, dxfBlue = 53)
        val INDEX_20: Color = ColorImplementation(colorIndex = 20, dxfRed = 255, dxfGreen = 63, dxfBlue = 0)
        val INDEX_21: Color = ColorImplementation(colorIndex = 21, dxfRed = 255, dxfGreen = 191, dxfBlue = 170)
        val INDEX_22: Color = ColorImplementation(colorIndex = 22, dxfRed = 189, dxfGreen = 46, dxfBlue = 0)
        val INDEX_23: Color = ColorImplementation(colorIndex = 23, dxfRed = 189, dxfGreen = 141, dxfBlue = 126)
        val INDEX_24: Color = ColorImplementation(colorIndex = 24, dxfRed = 129, dxfGreen = 31, dxfBlue = 0)
        val INDEX_25: Color = ColorImplementation(colorIndex = 25, dxfRed = 129, dxfGreen = 96, dxfBlue = 86)
        val INDEX_26: Color = ColorImplementation(colorIndex = 26, dxfRed = 104, dxfGreen = 25, dxfBlue = 0)
        val INDEX_27: Color = ColorImplementation(colorIndex = 27, dxfRed = 104, dxfGreen = 78, dxfBlue = 69)
        val INDEX_28: Color = ColorImplementation(colorIndex = 28, dxfRed = 79, dxfGreen = 19, dxfBlue = 0)
        val INDEX_29: Color = ColorImplementation(colorIndex = 29, dxfRed = 79, dxfGreen = 59, dxfBlue = 53)
        val INDEX_30: Color = ColorImplementation(colorIndex = 30, dxfRed = 255, dxfGreen = 127, dxfBlue = 0)
        val INDEX_31: Color = ColorImplementation(colorIndex = 31, dxfRed = 255, dxfGreen = 212, dxfBlue = 170)
        val INDEX_32: Color = ColorImplementation(colorIndex = 32, dxfRed = 189, dxfGreen = 94, dxfBlue = 0)
        val INDEX_33: Color = ColorImplementation(colorIndex = 33, dxfRed = 189, dxfGreen = 157, dxfBlue = 126)
        val INDEX_34: Color = ColorImplementation(colorIndex = 34, dxfRed = 129, dxfGreen = 64, dxfBlue = 0)
        val INDEX_35: Color = ColorImplementation(colorIndex = 35, dxfRed = 129, dxfGreen = 107, dxfBlue = 86)
        val INDEX_36: Color = ColorImplementation(colorIndex = 36, dxfRed = 104, dxfGreen = 52, dxfBlue = 0)
        val INDEX_37: Color = ColorImplementation(colorIndex = 37, dxfRed = 104, dxfGreen = 86, dxfBlue = 69)
        val INDEX_38: Color = ColorImplementation(colorIndex = 38, dxfRed = 79, dxfGreen = 39, dxfBlue = 0)
        val INDEX_39: Color = ColorImplementation(colorIndex = 39, dxfRed = 79, dxfGreen = 66, dxfBlue = 53)
        val INDEX_40: Color = ColorImplementation(colorIndex = 40, dxfRed = 255, dxfGreen = 191, dxfBlue = 0)
        val INDEX_41: Color = ColorImplementation(colorIndex = 41, dxfRed = 255, dxfGreen = 234, dxfBlue = 170)
        val INDEX_42: Color = ColorImplementation(colorIndex = 42, dxfRed = 189, dxfGreen = 141, dxfBlue = 0)
        val INDEX_43: Color = ColorImplementation(colorIndex = 43, dxfRed = 189, dxfGreen = 173, dxfBlue = 126)
        val INDEX_44: Color = ColorImplementation(colorIndex = 44, dxfRed = 129, dxfGreen = 96, dxfBlue = 0)
        val INDEX_45: Color = ColorImplementation(colorIndex = 45, dxfRed = 129, dxfGreen = 118, dxfBlue = 86)
        val INDEX_46: Color = ColorImplementation(colorIndex = 46, dxfRed = 104, dxfGreen = 78, dxfBlue = 0)
        val INDEX_47: Color = ColorImplementation(colorIndex = 47, dxfRed = 104, dxfGreen = 95, dxfBlue = 69)
        val INDEX_48: Color = ColorImplementation(colorIndex = 48, dxfRed = 79, dxfGreen = 59, dxfBlue = 0)
        val INDEX_49: Color = ColorImplementation(colorIndex = 49, dxfRed = 79, dxfGreen = 73, dxfBlue = 53)
        val INDEX_50: Color = ColorImplementation(colorIndex = 50, dxfRed = 255, dxfGreen = 255, dxfBlue = 0)
        val INDEX_51: Color = ColorImplementation(colorIndex = 51, dxfRed = 255, dxfGreen = 255, dxfBlue = 170)
        val INDEX_52: Color = ColorImplementation(colorIndex = 52, dxfRed = 189, dxfGreen = 189, dxfBlue = 0)
        val INDEX_53: Color = ColorImplementation(colorIndex = 53, dxfRed = 189, dxfGreen = 189, dxfBlue = 126)
        val INDEX_54: Color = ColorImplementation(colorIndex = 54, dxfRed = 129, dxfGreen = 129, dxfBlue = 0)
        val INDEX_55: Color = ColorImplementation(colorIndex = 55, dxfRed = 129, dxfGreen = 129, dxfBlue = 86)
        val INDEX_56: Color = ColorImplementation(colorIndex = 56, dxfRed = 104, dxfGreen = 104, dxfBlue = 0)
        val INDEX_57: Color = ColorImplementation(colorIndex = 57, dxfRed = 104, dxfGreen = 104, dxfBlue = 69)
        val INDEX_58: Color = ColorImplementation(colorIndex = 58, dxfRed = 79, dxfGreen = 79, dxfBlue = 0)
        val INDEX_59: Color = ColorImplementation(colorIndex = 59, dxfRed = 79, dxfGreen = 79, dxfBlue = 53)
        val INDEX_60: Color = ColorImplementation(colorIndex = 60, dxfRed = 191, dxfGreen = 255, dxfBlue = 0)
        val INDEX_61: Color = ColorImplementation(colorIndex = 61, dxfRed = 234, dxfGreen = 255, dxfBlue = 170)
        val INDEX_62: Color = ColorImplementation(colorIndex = 62, dxfRed = 141, dxfGreen = 189, dxfBlue = 0)
        val INDEX_63: Color = ColorImplementation(colorIndex = 63, dxfRed = 173, dxfGreen = 189, dxfBlue = 126)
        val INDEX_64: Color = ColorImplementation(colorIndex = 64, dxfRed = 96, dxfGreen = 129, dxfBlue = 0)
        val INDEX_65: Color = ColorImplementation(colorIndex = 65, dxfRed = 118, dxfGreen = 129, dxfBlue = 86)
        val INDEX_66: Color = ColorImplementation(colorIndex = 66, dxfRed = 78, dxfGreen = 104, dxfBlue = 0)
        val INDEX_67: Color = ColorImplementation(colorIndex = 67, dxfRed = 95, dxfGreen = 104, dxfBlue = 69)
        val INDEX_68: Color = ColorImplementation(colorIndex = 68, dxfRed = 59, dxfGreen = 79, dxfBlue = 0)
        val INDEX_69: Color = ColorImplementation(colorIndex = 69, dxfRed = 73, dxfGreen = 79, dxfBlue = 53)
        val INDEX_70: Color = ColorImplementation(colorIndex = 70, dxfRed = 127, dxfGreen = 255, dxfBlue = 0)
        val INDEX_71: Color = ColorImplementation(colorIndex = 71, dxfRed = 212, dxfGreen = 255, dxfBlue = 170)
        val INDEX_72: Color = ColorImplementation(colorIndex = 72, dxfRed = 94, dxfGreen = 189, dxfBlue = 0)
        val INDEX_73: Color = ColorImplementation(colorIndex = 73, dxfRed = 157, dxfGreen = 189, dxfBlue = 126)
        val INDEX_74: Color = ColorImplementation(colorIndex = 74, dxfRed = 64, dxfGreen = 129, dxfBlue = 0)
        val INDEX_75: Color = ColorImplementation(colorIndex = 75, dxfRed = 107, dxfGreen = 129, dxfBlue = 86)
        val INDEX_76: Color = ColorImplementation(colorIndex = 76, dxfRed = 52, dxfGreen = 104, dxfBlue = 0)
        val INDEX_77: Color = ColorImplementation(colorIndex = 77, dxfRed = 86, dxfGreen = 104, dxfBlue = 69)
        val INDEX_78: Color = ColorImplementation(colorIndex = 78, dxfRed = 39, dxfGreen = 79, dxfBlue = 0)
        val INDEX_79: Color = ColorImplementation(colorIndex = 79, dxfRed = 66, dxfGreen = 79, dxfBlue = 53)
        val INDEX_80: Color = ColorImplementation(colorIndex = 80, dxfRed = 63, dxfGreen = 255, dxfBlue = 0)
        val INDEX_81: Color = ColorImplementation(colorIndex = 81, dxfRed = 191, dxfGreen = 255, dxfBlue = 170)
        val INDEX_82: Color = ColorImplementation(colorIndex = 82, dxfRed = 46, dxfGreen = 189, dxfBlue = 0)
        val INDEX_83: Color = ColorImplementation(colorIndex = 83, dxfRed = 141, dxfGreen = 189, dxfBlue = 126)
        val INDEX_84: Color = ColorImplementation(colorIndex = 84, dxfRed = 31, dxfGreen = 129, dxfBlue = 0)
        val INDEX_85: Color = ColorImplementation(colorIndex = 85, dxfRed = 96, dxfGreen = 129, dxfBlue = 86)
        val INDEX_86: Color = ColorImplementation(colorIndex = 86, dxfRed = 25, dxfGreen = 104, dxfBlue = 0)
        val INDEX_87: Color = ColorImplementation(colorIndex = 87, dxfRed = 78, dxfGreen = 104, dxfBlue = 69)
        val INDEX_88: Color = ColorImplementation(colorIndex = 88, dxfRed = 19, dxfGreen = 79, dxfBlue = 0)
        val INDEX_89: Color = ColorImplementation(colorIndex = 89, dxfRed = 59, dxfGreen = 79, dxfBlue = 53)
        val INDEX_90: Color = ColorImplementation(colorIndex = 90, dxfRed = 0, dxfGreen = 255, dxfBlue = 0)
        val INDEX_91: Color = ColorImplementation(colorIndex = 91, dxfRed = 170, dxfGreen = 255, dxfBlue = 170)
        val INDEX_92: Color = ColorImplementation(colorIndex = 92, dxfRed = 0, dxfGreen = 189, dxfBlue = 0)
        val INDEX_93: Color = ColorImplementation(colorIndex = 93, dxfRed = 126, dxfGreen = 189, dxfBlue = 126)
        val INDEX_94: Color = ColorImplementation(colorIndex = 94, dxfRed = 0, dxfGreen = 129, dxfBlue = 0)
        val INDEX_95: Color = ColorImplementation(colorIndex = 95, dxfRed = 86, dxfGreen = 129, dxfBlue = 86)
        val INDEX_96: Color = ColorImplementation(colorIndex = 96, dxfRed = 0, dxfGreen = 104, dxfBlue = 0)
        val INDEX_97: Color = ColorImplementation(colorIndex = 97, dxfRed = 69, dxfGreen = 104, dxfBlue = 69)
        val INDEX_98: Color = ColorImplementation(colorIndex = 98, dxfRed = 0, dxfGreen = 79, dxfBlue = 0)
        val INDEX_99: Color = ColorImplementation(colorIndex = 99, dxfRed = 53, dxfGreen = 79, dxfBlue = 53)
        val INDEX_100: Color = ColorImplementation(colorIndex = 100, dxfRed = 0, dxfGreen = 255, dxfBlue = 63)
        val INDEX_101: Color = ColorImplementation(colorIndex = 101, dxfRed = 170, dxfGreen = 255, dxfBlue = 191)
        val INDEX_102: Color = ColorImplementation(colorIndex = 102, dxfRed = 0, dxfGreen = 189, dxfBlue = 46)
        val INDEX_103: Color = ColorImplementation(colorIndex = 103, dxfRed = 126, dxfGreen = 189, dxfBlue = 141)
        val INDEX_104: Color = ColorImplementation(colorIndex = 104, dxfRed = 0, dxfGreen = 129, dxfBlue = 31)
        val INDEX_105: Color = ColorImplementation(colorIndex = 105, dxfRed = 86, dxfGreen = 129, dxfBlue = 96)
        val INDEX_106: Color = ColorImplementation(colorIndex = 106, dxfRed = 0, dxfGreen = 104, dxfBlue = 25)
        val INDEX_107: Color = ColorImplementation(colorIndex = 107, dxfRed = 69, dxfGreen = 104, dxfBlue = 78)
        val INDEX_108: Color = ColorImplementation(colorIndex = 108, dxfRed = 0, dxfGreen = 79, dxfBlue = 19)
        val INDEX_109: Color = ColorImplementation(colorIndex = 109, dxfRed = 53, dxfGreen = 79, dxfBlue = 59)
        val INDEX_110: Color = ColorImplementation(colorIndex = 110, dxfRed = 0, dxfGreen = 255, dxfBlue = 127)
        val INDEX_111: Color = ColorImplementation(colorIndex = 111, dxfRed = 170, dxfGreen = 255, dxfBlue = 212)
        val INDEX_112: Color = ColorImplementation(colorIndex = 112, dxfRed = 0, dxfGreen = 189, dxfBlue = 94)
        val INDEX_113: Color = ColorImplementation(colorIndex = 113, dxfRed = 126, dxfGreen = 189, dxfBlue = 157)
        val INDEX_114: Color = ColorImplementation(colorIndex = 114, dxfRed = 0, dxfGreen = 129, dxfBlue = 64)
        val INDEX_115: Color = ColorImplementation(colorIndex = 115, dxfRed = 86, dxfGreen = 129, dxfBlue = 107)
        val INDEX_116: Color = ColorImplementation(colorIndex = 116, dxfRed = 0, dxfGreen = 104, dxfBlue = 52)
        val INDEX_117: Color = ColorImplementation(colorIndex = 117, dxfRed = 69, dxfGreen = 104, dxfBlue = 86)
        val INDEX_118: Color = ColorImplementation(colorIndex = 118, dxfRed = 0, dxfGreen = 79, dxfBlue = 39)
        val INDEX_119: Color = ColorImplementation(colorIndex = 119, dxfRed = 53, dxfGreen = 79, dxfBlue = 66)
        val INDEX_120: Color = ColorImplementation(colorIndex = 120, dxfRed = 0, dxfGreen = 255, dxfBlue = 191)
        val INDEX_121: Color = ColorImplementation(colorIndex = 121, dxfRed = 170, dxfGreen = 255, dxfBlue = 234)
        val INDEX_122: Color = ColorImplementation(colorIndex = 122, dxfRed = 0, dxfGreen = 189, dxfBlue = 141)
        val INDEX_123: Color = ColorImplementation(colorIndex = 123, dxfRed = 126, dxfGreen = 189, dxfBlue = 173)
        val INDEX_124: Color = ColorImplementation(colorIndex = 124, dxfRed = 0, dxfGreen = 129, dxfBlue = 96)
        val INDEX_125: Color = ColorImplementation(colorIndex = 125, dxfRed = 86, dxfGreen = 129, dxfBlue = 118)
        val INDEX_126: Color = ColorImplementation(colorIndex = 126, dxfRed = 0, dxfGreen = 104, dxfBlue = 78)
        val INDEX_127: Color = ColorImplementation(colorIndex = 127, dxfRed = 69, dxfGreen = 104, dxfBlue = 95)
        val INDEX_128: Color = ColorImplementation(colorIndex = 128, dxfRed = 0, dxfGreen = 79, dxfBlue = 59)
        val INDEX_129: Color = ColorImplementation(colorIndex = 129, dxfRed = 53, dxfGreen = 79, dxfBlue = 73)
        val INDEX_130: Color = ColorImplementation(colorIndex = 130, dxfRed = 0, dxfGreen = 255, dxfBlue = 255)
        val INDEX_131: Color = ColorImplementation(colorIndex = 131, dxfRed = 170, dxfGreen = 255, dxfBlue = 255)
        val INDEX_132: Color = ColorImplementation(colorIndex = 132, dxfRed = 0, dxfGreen = 189, dxfBlue = 189)
        val INDEX_133: Color = ColorImplementation(colorIndex = 133, dxfRed = 126, dxfGreen = 189, dxfBlue = 189)
        val INDEX_134: Color = ColorImplementation(colorIndex = 134, dxfRed = 0, dxfGreen = 129, dxfBlue = 129)
        val INDEX_135: Color = ColorImplementation(colorIndex = 135, dxfRed = 86, dxfGreen = 129, dxfBlue = 129)
        val INDEX_136: Color = ColorImplementation(colorIndex = 136, dxfRed = 0, dxfGreen = 104, dxfBlue = 104)
        val INDEX_137: Color = ColorImplementation(colorIndex = 137, dxfRed = 69, dxfGreen = 104, dxfBlue = 104)
        val INDEX_138: Color = ColorImplementation(colorIndex = 138, dxfRed = 0, dxfGreen = 79, dxfBlue = 79)
        val INDEX_139: Color = ColorImplementation(colorIndex = 139, dxfRed = 53, dxfGreen = 79, dxfBlue = 79)
        val INDEX_140: Color = ColorImplementation(colorIndex = 140, dxfRed = 0, dxfGreen = 191, dxfBlue = 255)
        val INDEX_141: Color = ColorImplementation(colorIndex = 141, dxfRed = 170, dxfGreen = 234, dxfBlue = 255)
        val INDEX_142: Color = ColorImplementation(colorIndex = 142, dxfRed = 0, dxfGreen = 141, dxfBlue = 189)
        val INDEX_143: Color = ColorImplementation(colorIndex = 143, dxfRed = 126, dxfGreen = 173, dxfBlue = 189)
        val INDEX_144: Color = ColorImplementation(colorIndex = 144, dxfRed = 0, dxfGreen = 96, dxfBlue = 129)
        val INDEX_145: Color = ColorImplementation(colorIndex = 145, dxfRed = 86, dxfGreen = 118, dxfBlue = 129)
        val INDEX_146: Color = ColorImplementation(colorIndex = 146, dxfRed = 0, dxfGreen = 78, dxfBlue = 104)
        val INDEX_147: Color = ColorImplementation(colorIndex = 147, dxfRed = 69, dxfGreen = 95, dxfBlue = 104)
        val INDEX_148: Color = ColorImplementation(colorIndex = 148, dxfRed = 0, dxfGreen = 59, dxfBlue = 79)
        val INDEX_149: Color = ColorImplementation(colorIndex = 149, dxfRed = 53, dxfGreen = 73, dxfBlue = 79)
        val INDEX_150: Color = ColorImplementation(colorIndex = 150, dxfRed = 0, dxfGreen = 127, dxfBlue = 255)
        val INDEX_151: Color = ColorImplementation(colorIndex = 151, dxfRed = 170, dxfGreen = 212, dxfBlue = 255)
        val INDEX_152: Color = ColorImplementation(colorIndex = 152, dxfRed = 0, dxfGreen = 94, dxfBlue = 189)
        val INDEX_153: Color = ColorImplementation(colorIndex = 153, dxfRed = 126, dxfGreen = 157, dxfBlue = 189)
        val INDEX_154: Color = ColorImplementation(colorIndex = 154, dxfRed = 0, dxfGreen = 64, dxfBlue = 129)
        val INDEX_155: Color = ColorImplementation(colorIndex = 155, dxfRed = 86, dxfGreen = 107, dxfBlue = 129)
        val INDEX_156: Color = ColorImplementation(colorIndex = 156, dxfRed = 0, dxfGreen = 52, dxfBlue = 104)
        val INDEX_157: Color = ColorImplementation(colorIndex = 157, dxfRed = 69, dxfGreen = 86, dxfBlue = 104)
        val INDEX_158: Color = ColorImplementation(colorIndex = 158, dxfRed = 0, dxfGreen = 39, dxfBlue = 79)
        val INDEX_159: Color = ColorImplementation(colorIndex = 159, dxfRed = 53, dxfGreen = 66, dxfBlue = 79)
        val INDEX_160: Color = ColorImplementation(colorIndex = 160, dxfRed = 0, dxfGreen = 63, dxfBlue = 255)
        val INDEX_161: Color = ColorImplementation(colorIndex = 161, dxfRed = 170, dxfGreen = 191, dxfBlue = 255)
        val INDEX_162: Color = ColorImplementation(colorIndex = 162, dxfRed = 0, dxfGreen = 46, dxfBlue = 189)
        val INDEX_163: Color = ColorImplementation(colorIndex = 163, dxfRed = 126, dxfGreen = 141, dxfBlue = 189)
        val INDEX_164: Color = ColorImplementation(colorIndex = 164, dxfRed = 0, dxfGreen = 31, dxfBlue = 129)
        val INDEX_165: Color = ColorImplementation(colorIndex = 165, dxfRed = 86, dxfGreen = 96, dxfBlue = 129)
        val INDEX_166: Color = ColorImplementation(colorIndex = 166, dxfRed = 0, dxfGreen = 25, dxfBlue = 104)
        val INDEX_167: Color = ColorImplementation(colorIndex = 167, dxfRed = 69, dxfGreen = 78, dxfBlue = 104)
        val INDEX_168: Color = ColorImplementation(colorIndex = 168, dxfRed = 0, dxfGreen = 19, dxfBlue = 79)
        val INDEX_169: Color = ColorImplementation(colorIndex = 169, dxfRed = 53, dxfGreen = 59, dxfBlue = 79)
        val INDEX_170: Color = ColorImplementation(colorIndex = 170, dxfRed = 0, dxfGreen = 0, dxfBlue = 255)
        val INDEX_171: Color = ColorImplementation(colorIndex = 171, dxfRed = 170, dxfGreen = 170, dxfBlue = 255)
        val INDEX_172: Color = ColorImplementation(colorIndex = 172, dxfRed = 0, dxfGreen = 0, dxfBlue = 189)
        val INDEX_173: Color = ColorImplementation(colorIndex = 173, dxfRed = 126, dxfGreen = 126, dxfBlue = 189)
        val INDEX_174: Color = ColorImplementation(colorIndex = 174, dxfRed = 0, dxfGreen = 0, dxfBlue = 129)
        val INDEX_175: Color = ColorImplementation(colorIndex = 175, dxfRed = 86, dxfGreen = 86, dxfBlue = 129)
        val INDEX_176: Color = ColorImplementation(colorIndex = 176, dxfRed = 0, dxfGreen = 0, dxfBlue = 104)
        val INDEX_177: Color = ColorImplementation(colorIndex = 177, dxfRed = 69, dxfGreen = 69, dxfBlue = 104)
        val INDEX_178: Color = ColorImplementation(colorIndex = 178, dxfRed = 0, dxfGreen = 0, dxfBlue = 79)
        val INDEX_179: Color = ColorImplementation(colorIndex = 179, dxfRed = 53, dxfGreen = 53, dxfBlue = 79)
        val INDEX_180: Color = ColorImplementation(colorIndex = 180, dxfRed = 63, dxfGreen = 0, dxfBlue = 255)
        val INDEX_181: Color = ColorImplementation(colorIndex = 181, dxfRed = 191, dxfGreen = 170, dxfBlue = 255)
        val INDEX_182: Color = ColorImplementation(colorIndex = 182, dxfRed = 46, dxfGreen = 0, dxfBlue = 189)
        val INDEX_183: Color = ColorImplementation(colorIndex = 183, dxfRed = 141, dxfGreen = 126, dxfBlue = 189)
        val INDEX_184: Color = ColorImplementation(colorIndex = 184, dxfRed = 31, dxfGreen = 0, dxfBlue = 129)
        val INDEX_185: Color = ColorImplementation(colorIndex = 185, dxfRed = 96, dxfGreen = 86, dxfBlue = 129)
        val INDEX_186: Color = ColorImplementation(colorIndex = 186, dxfRed = 25, dxfGreen = 0, dxfBlue = 104)
        val INDEX_187: Color = ColorImplementation(colorIndex = 187, dxfRed = 78, dxfGreen = 69, dxfBlue = 104)
        val INDEX_188: Color = ColorImplementation(colorIndex = 188, dxfRed = 19, dxfGreen = 0, dxfBlue = 79)
        val INDEX_189: Color = ColorImplementation(colorIndex = 189, dxfRed = 59, dxfGreen = 53, dxfBlue = 79)
        val INDEX_190: Color = ColorImplementation(colorIndex = 190, dxfRed = 127, dxfGreen = 0, dxfBlue = 255)
        val INDEX_191: Color = ColorImplementation(colorIndex = 191, dxfRed = 212, dxfGreen = 170, dxfBlue = 255)
        val INDEX_192: Color = ColorImplementation(colorIndex = 192, dxfRed = 94, dxfGreen = 0, dxfBlue = 189)
        val INDEX_193: Color = ColorImplementation(colorIndex = 193, dxfRed = 157, dxfGreen = 126, dxfBlue = 189)
        val INDEX_194: Color = ColorImplementation(colorIndex = 194, dxfRed = 64, dxfGreen = 0, dxfBlue = 129)
        val INDEX_195: Color = ColorImplementation(colorIndex = 195, dxfRed = 107, dxfGreen = 86, dxfBlue = 129)
        val INDEX_196: Color = ColorImplementation(colorIndex = 196, dxfRed = 52, dxfGreen = 0, dxfBlue = 104)
        val INDEX_197: Color = ColorImplementation(colorIndex = 197, dxfRed = 86, dxfGreen = 69, dxfBlue = 104)
        val INDEX_198: Color = ColorImplementation(colorIndex = 198, dxfRed = 39, dxfGreen = 0, dxfBlue = 79)
        val INDEX_199: Color = ColorImplementation(colorIndex = 199, dxfRed = 66, dxfGreen = 53, dxfBlue = 79)
        val INDEX_200: Color = ColorImplementation(colorIndex = 200, dxfRed = 191, dxfGreen = 0, dxfBlue = 255)
        val INDEX_201: Color = ColorImplementation(colorIndex = 201, dxfRed = 234, dxfGreen = 170, dxfBlue = 255)
        val INDEX_202: Color = ColorImplementation(colorIndex = 202, dxfRed = 141, dxfGreen = 0, dxfBlue = 189)
        val INDEX_203: Color = ColorImplementation(colorIndex = 203, dxfRed = 173, dxfGreen = 126, dxfBlue = 189)
        val INDEX_204: Color = ColorImplementation(colorIndex = 204, dxfRed = 96, dxfGreen = 0, dxfBlue = 129)
        val INDEX_205: Color = ColorImplementation(colorIndex = 205, dxfRed = 118, dxfGreen = 86, dxfBlue = 129)
        val INDEX_206: Color = ColorImplementation(colorIndex = 206, dxfRed = 78, dxfGreen = 0, dxfBlue = 104)
        val INDEX_207: Color = ColorImplementation(colorIndex = 207, dxfRed = 95, dxfGreen = 69, dxfBlue = 104)
        val INDEX_208: Color = ColorImplementation(colorIndex = 208, dxfRed = 59, dxfGreen = 0, dxfBlue = 79)
        val INDEX_209: Color = ColorImplementation(colorIndex = 209, dxfRed = 73, dxfGreen = 53, dxfBlue = 79)
        val INDEX_210: Color = ColorImplementation(colorIndex = 210, dxfRed = 255, dxfGreen = 0, dxfBlue = 255)
        val INDEX_211: Color = ColorImplementation(colorIndex = 211, dxfRed = 255, dxfGreen = 170, dxfBlue = 255)
        val INDEX_212: Color = ColorImplementation(colorIndex = 212, dxfRed = 189, dxfGreen = 0, dxfBlue = 189)
        val INDEX_213: Color = ColorImplementation(colorIndex = 213, dxfRed = 189, dxfGreen = 126, dxfBlue = 189)
        val INDEX_214: Color = ColorImplementation(colorIndex = 214, dxfRed = 129, dxfGreen = 0, dxfBlue = 129)
        val INDEX_215: Color = ColorImplementation(colorIndex = 215, dxfRed = 129, dxfGreen = 86, dxfBlue = 129)
        val INDEX_216: Color = ColorImplementation(colorIndex = 216, dxfRed = 104, dxfGreen = 0, dxfBlue = 104)
        val INDEX_217: Color = ColorImplementation(colorIndex = 217, dxfRed = 104, dxfGreen = 69, dxfBlue = 104)
        val INDEX_218: Color = ColorImplementation(colorIndex = 218, dxfRed = 79, dxfGreen = 0, dxfBlue = 79)
        val INDEX_219: Color = ColorImplementation(colorIndex = 219, dxfRed = 79, dxfGreen = 53, dxfBlue = 79)
        val INDEX_220: Color = ColorImplementation(colorIndex = 220, dxfRed = 255, dxfGreen = 0, dxfBlue = 191)
        val INDEX_221: Color = ColorImplementation(colorIndex = 221, dxfRed = 255, dxfGreen = 170, dxfBlue = 234)
        val INDEX_222: Color = ColorImplementation(colorIndex = 222, dxfRed = 189, dxfGreen = 0, dxfBlue = 141)
        val INDEX_223: Color = ColorImplementation(colorIndex = 223, dxfRed = 189, dxfGreen = 126, dxfBlue = 173)
        val INDEX_224: Color = ColorImplementation(colorIndex = 224, dxfRed = 129, dxfGreen = 0, dxfBlue = 96)
        val INDEX_225: Color = ColorImplementation(colorIndex = 225, dxfRed = 129, dxfGreen = 86, dxfBlue = 118)
        val INDEX_226: Color = ColorImplementation(colorIndex = 226, dxfRed = 104, dxfGreen = 0, dxfBlue = 78)
        val INDEX_227: Color = ColorImplementation(colorIndex = 227, dxfRed = 104, dxfGreen = 69, dxfBlue = 95)
        val INDEX_228: Color = ColorImplementation(colorIndex = 228, dxfRed = 79, dxfGreen = 0, dxfBlue = 59)
        val INDEX_229: Color = ColorImplementation(colorIndex = 229, dxfRed = 79, dxfGreen = 53, dxfBlue = 73)
        val INDEX_230: Color = ColorImplementation(colorIndex = 230, dxfRed = 255, dxfGreen = 0, dxfBlue = 127)
        val INDEX_231: Color = ColorImplementation(colorIndex = 231, dxfRed = 255, dxfGreen = 170, dxfBlue = 212)
        val INDEX_232: Color = ColorImplementation(colorIndex = 232, dxfRed = 189, dxfGreen = 0, dxfBlue = 94)
        val INDEX_233: Color = ColorImplementation(colorIndex = 233, dxfRed = 189, dxfGreen = 126, dxfBlue = 157)
        val INDEX_234: Color = ColorImplementation(colorIndex = 234, dxfRed = 129, dxfGreen = 0, dxfBlue = 64)
        val INDEX_235: Color = ColorImplementation(colorIndex = 235, dxfRed = 129, dxfGreen = 86, dxfBlue = 107)
        val INDEX_236: Color = ColorImplementation(colorIndex = 236, dxfRed = 104, dxfGreen = 0, dxfBlue = 52)
        val INDEX_237: Color = ColorImplementation(colorIndex = 237, dxfRed = 104, dxfGreen = 69, dxfBlue = 86)
        val INDEX_238: Color = ColorImplementation(colorIndex = 238, dxfRed = 79, dxfGreen = 0, dxfBlue = 39)
        val INDEX_239: Color = ColorImplementation(colorIndex = 239, dxfRed = 79, dxfGreen = 53, dxfBlue = 66)
        val INDEX_240: Color = ColorImplementation(colorIndex = 240, dxfRed = 255, dxfGreen = 0, dxfBlue = 63)
        val INDEX_241: Color = ColorImplementation(colorIndex = 241, dxfRed = 255, dxfGreen = 170, dxfBlue = 191)
        val INDEX_242: Color = ColorImplementation(colorIndex = 242, dxfRed = 189, dxfGreen = 0, dxfBlue = 46)
        val INDEX_243: Color = ColorImplementation(colorIndex = 243, dxfRed = 189, dxfGreen = 126, dxfBlue = 141)
        val INDEX_244: Color = ColorImplementation(colorIndex = 244, dxfRed = 129, dxfGreen = 0, dxfBlue = 31)
        val INDEX_245: Color = ColorImplementation(colorIndex = 245, dxfRed = 129, dxfGreen = 86, dxfBlue = 96)
        val INDEX_246: Color = ColorImplementation(colorIndex = 246, dxfRed = 104, dxfGreen = 0, dxfBlue = 25)
        val INDEX_247: Color = ColorImplementation(colorIndex = 247, dxfRed = 104, dxfGreen = 69, dxfBlue = 78)
        val INDEX_248: Color = ColorImplementation(colorIndex = 248, dxfRed = 79, dxfGreen = 0, dxfBlue = 19)
        val INDEX_249: Color = ColorImplementation(colorIndex = 249, dxfRed = 79, dxfGreen = 53, dxfBlue = 59)
        val INDEX_250: Color = ColorImplementation(colorIndex = 250, dxfRed = 51, dxfGreen = 51, dxfBlue = 51)
        val INDEX_251: Color = ColorImplementation(colorIndex = 251, dxfRed = 80, dxfGreen = 80, dxfBlue = 80)
        val INDEX_252: Color = ColorImplementation(colorIndex = 252, dxfRed = 105, dxfGreen = 105, dxfBlue = 105)
        val INDEX_253: Color = ColorImplementation(colorIndex = 253, dxfRed = 130, dxfGreen = 130, dxfBlue = 130)
        val INDEX_254: Color = ColorImplementation(colorIndex = 254, dxfRed = 190, dxfGreen = 190, dxfBlue = 190)
        val INDEX_255: Color = ColorImplementation(colorIndex = 255, dxfRed = 255, dxfGreen = 255, dxfBlue = 255)
    }
}