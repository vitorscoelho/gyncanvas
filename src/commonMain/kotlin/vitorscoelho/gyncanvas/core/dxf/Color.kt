package vitorscoelho.gyncanvas.core.dxf

import vitorscoelho.gyncanvas.core.dxf.entities.Entity

abstract class Color private constructor() : vitorscoelho.gyncanvas.core.primitives.Color {
    abstract val colorIndex: Int
    internal open fun effectiveColor(entity: Entity): Color = this //TODO mudar o método para não aceitar nulo. Mudar owner para tipo Block e usar Block.MODEL como padrão

    private class ColorImplementation(
        override val colorIndex: Int,
        override val red: Short,
        override val green: Short,
        override val blue: Short
    ) : Color()

    companion object {
        val BY_BLOCK: Color by lazy {
            object : Color() {
                override val colorIndex: Int = 0
                override val red: Short = 0
                override val green: Short = 0
                override val blue: Short = 0
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
                override val red: Short = 0
                override val green: Short = 0
                override val blue: Short = 0
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

        val INDEX_1: Color = ColorImplementation(colorIndex = 1, red = 255, green = 0, blue = 0)
        val INDEX_2: Color = ColorImplementation(colorIndex = 2, red = 255, green = 255, blue = 0)
        val INDEX_3: Color = ColorImplementation(colorIndex = 3, red = 0, green = 255, blue = 0)
        val INDEX_4: Color = ColorImplementation(colorIndex = 4, red = 0, green = 255, blue = 255)
        val INDEX_5: Color = ColorImplementation(colorIndex = 5, red = 0, green = 0, blue = 255)
        val INDEX_6: Color = ColorImplementation(colorIndex = 6, red = 255, green = 0, blue = 255)
        val INDEX_7: Color = ColorImplementation(colorIndex = 7, red = 255, green = 255, blue = 255)
        val INDEX_8: Color = ColorImplementation(colorIndex = 8, red = 65, green = 65, blue = 65)
        val INDEX_9: Color = ColorImplementation(colorIndex = 9, red = 128, green = 128, blue = 128)
        val INDEX_10: Color = ColorImplementation(colorIndex = 10, red = 255, green = 0, blue = 0)
        val INDEX_11: Color = ColorImplementation(colorIndex = 11, red = 255, green = 170, blue = 170)
        val INDEX_12: Color = ColorImplementation(colorIndex = 12, red = 189, green = 0, blue = 0)
        val INDEX_13: Color = ColorImplementation(colorIndex = 13, red = 189, green = 126, blue = 126)
        val INDEX_14: Color = ColorImplementation(colorIndex = 14, red = 129, green = 0, blue = 0)
        val INDEX_15: Color = ColorImplementation(colorIndex = 15, red = 129, green = 86, blue = 86)
        val INDEX_16: Color = ColorImplementation(colorIndex = 16, red = 104, green = 0, blue = 0)
        val INDEX_17: Color = ColorImplementation(colorIndex = 17, red = 104, green = 69, blue = 69)
        val INDEX_18: Color = ColorImplementation(colorIndex = 18, red = 79, green = 0, blue = 0)
        val INDEX_19: Color = ColorImplementation(colorIndex = 19, red = 79, green = 53, blue = 53)
        val INDEX_20: Color = ColorImplementation(colorIndex = 20, red = 255, green = 63, blue = 0)
        val INDEX_21: Color = ColorImplementation(colorIndex = 21, red = 255, green = 191, blue = 170)
        val INDEX_22: Color = ColorImplementation(colorIndex = 22, red = 189, green = 46, blue = 0)
        val INDEX_23: Color = ColorImplementation(colorIndex = 23, red = 189, green = 141, blue = 126)
        val INDEX_24: Color = ColorImplementation(colorIndex = 24, red = 129, green = 31, blue = 0)
        val INDEX_25: Color = ColorImplementation(colorIndex = 25, red = 129, green = 96, blue = 86)
        val INDEX_26: Color = ColorImplementation(colorIndex = 26, red = 104, green = 25, blue = 0)
        val INDEX_27: Color = ColorImplementation(colorIndex = 27, red = 104, green = 78, blue = 69)
        val INDEX_28: Color = ColorImplementation(colorIndex = 28, red = 79, green = 19, blue = 0)
        val INDEX_29: Color = ColorImplementation(colorIndex = 29, red = 79, green = 59, blue = 53)
        val INDEX_30: Color = ColorImplementation(colorIndex = 30, red = 255, green = 127, blue = 0)
        val INDEX_31: Color = ColorImplementation(colorIndex = 31, red = 255, green = 212, blue = 170)
        val INDEX_32: Color = ColorImplementation(colorIndex = 32, red = 189, green = 94, blue = 0)
        val INDEX_33: Color = ColorImplementation(colorIndex = 33, red = 189, green = 157, blue = 126)
        val INDEX_34: Color = ColorImplementation(colorIndex = 34, red = 129, green = 64, blue = 0)
        val INDEX_35: Color = ColorImplementation(colorIndex = 35, red = 129, green = 107, blue = 86)
        val INDEX_36: Color = ColorImplementation(colorIndex = 36, red = 104, green = 52, blue = 0)
        val INDEX_37: Color = ColorImplementation(colorIndex = 37, red = 104, green = 86, blue = 69)
        val INDEX_38: Color = ColorImplementation(colorIndex = 38, red = 79, green = 39, blue = 0)
        val INDEX_39: Color = ColorImplementation(colorIndex = 39, red = 79, green = 66, blue = 53)
        val INDEX_40: Color = ColorImplementation(colorIndex = 40, red = 255, green = 191, blue = 0)
        val INDEX_41: Color = ColorImplementation(colorIndex = 41, red = 255, green = 234, blue = 170)
        val INDEX_42: Color = ColorImplementation(colorIndex = 42, red = 189, green = 141, blue = 0)
        val INDEX_43: Color = ColorImplementation(colorIndex = 43, red = 189, green = 173, blue = 126)
        val INDEX_44: Color = ColorImplementation(colorIndex = 44, red = 129, green = 96, blue = 0)
        val INDEX_45: Color = ColorImplementation(colorIndex = 45, red = 129, green = 118, blue = 86)
        val INDEX_46: Color = ColorImplementation(colorIndex = 46, red = 104, green = 78, blue = 0)
        val INDEX_47: Color = ColorImplementation(colorIndex = 47, red = 104, green = 95, blue = 69)
        val INDEX_48: Color = ColorImplementation(colorIndex = 48, red = 79, green = 59, blue = 0)
        val INDEX_49: Color = ColorImplementation(colorIndex = 49, red = 79, green = 73, blue = 53)
        val INDEX_50: Color = ColorImplementation(colorIndex = 50, red = 255, green = 255, blue = 0)
        val INDEX_51: Color = ColorImplementation(colorIndex = 51, red = 255, green = 255, blue = 170)
        val INDEX_52: Color = ColorImplementation(colorIndex = 52, red = 189, green = 189, blue = 0)
        val INDEX_53: Color = ColorImplementation(colorIndex = 53, red = 189, green = 189, blue = 126)
        val INDEX_54: Color = ColorImplementation(colorIndex = 54, red = 129, green = 129, blue = 0)
        val INDEX_55: Color = ColorImplementation(colorIndex = 55, red = 129, green = 129, blue = 86)
        val INDEX_56: Color = ColorImplementation(colorIndex = 56, red = 104, green = 104, blue = 0)
        val INDEX_57: Color = ColorImplementation(colorIndex = 57, red = 104, green = 104, blue = 69)
        val INDEX_58: Color = ColorImplementation(colorIndex = 58, red = 79, green = 79, blue = 0)
        val INDEX_59: Color = ColorImplementation(colorIndex = 59, red = 79, green = 79, blue = 53)
        val INDEX_60: Color = ColorImplementation(colorIndex = 60, red = 191, green = 255, blue = 0)
        val INDEX_61: Color = ColorImplementation(colorIndex = 61, red = 234, green = 255, blue = 170)
        val INDEX_62: Color = ColorImplementation(colorIndex = 62, red = 141, green = 189, blue = 0)
        val INDEX_63: Color = ColorImplementation(colorIndex = 63, red = 173, green = 189, blue = 126)
        val INDEX_64: Color = ColorImplementation(colorIndex = 64, red = 96, green = 129, blue = 0)
        val INDEX_65: Color = ColorImplementation(colorIndex = 65, red = 118, green = 129, blue = 86)
        val INDEX_66: Color = ColorImplementation(colorIndex = 66, red = 78, green = 104, blue = 0)
        val INDEX_67: Color = ColorImplementation(colorIndex = 67, red = 95, green = 104, blue = 69)
        val INDEX_68: Color = ColorImplementation(colorIndex = 68, red = 59, green = 79, blue = 0)
        val INDEX_69: Color = ColorImplementation(colorIndex = 69, red = 73, green = 79, blue = 53)
        val INDEX_70: Color = ColorImplementation(colorIndex = 70, red = 127, green = 255, blue = 0)
        val INDEX_71: Color = ColorImplementation(colorIndex = 71, red = 212, green = 255, blue = 170)
        val INDEX_72: Color = ColorImplementation(colorIndex = 72, red = 94, green = 189, blue = 0)
        val INDEX_73: Color = ColorImplementation(colorIndex = 73, red = 157, green = 189, blue = 126)
        val INDEX_74: Color = ColorImplementation(colorIndex = 74, red = 64, green = 129, blue = 0)
        val INDEX_75: Color = ColorImplementation(colorIndex = 75, red = 107, green = 129, blue = 86)
        val INDEX_76: Color = ColorImplementation(colorIndex = 76, red = 52, green = 104, blue = 0)
        val INDEX_77: Color = ColorImplementation(colorIndex = 77, red = 86, green = 104, blue = 69)
        val INDEX_78: Color = ColorImplementation(colorIndex = 78, red = 39, green = 79, blue = 0)
        val INDEX_79: Color = ColorImplementation(colorIndex = 79, red = 66, green = 79, blue = 53)
        val INDEX_80: Color = ColorImplementation(colorIndex = 80, red = 63, green = 255, blue = 0)
        val INDEX_81: Color = ColorImplementation(colorIndex = 81, red = 191, green = 255, blue = 170)
        val INDEX_82: Color = ColorImplementation(colorIndex = 82, red = 46, green = 189, blue = 0)
        val INDEX_83: Color = ColorImplementation(colorIndex = 83, red = 141, green = 189, blue = 126)
        val INDEX_84: Color = ColorImplementation(colorIndex = 84, red = 31, green = 129, blue = 0)
        val INDEX_85: Color = ColorImplementation(colorIndex = 85, red = 96, green = 129, blue = 86)
        val INDEX_86: Color = ColorImplementation(colorIndex = 86, red = 25, green = 104, blue = 0)
        val INDEX_87: Color = ColorImplementation(colorIndex = 87, red = 78, green = 104, blue = 69)
        val INDEX_88: Color = ColorImplementation(colorIndex = 88, red = 19, green = 79, blue = 0)
        val INDEX_89: Color = ColorImplementation(colorIndex = 89, red = 59, green = 79, blue = 53)
        val INDEX_90: Color = ColorImplementation(colorIndex = 90, red = 0, green = 255, blue = 0)
        val INDEX_91: Color = ColorImplementation(colorIndex = 91, red = 170, green = 255, blue = 170)
        val INDEX_92: Color = ColorImplementation(colorIndex = 92, red = 0, green = 189, blue = 0)
        val INDEX_93: Color = ColorImplementation(colorIndex = 93, red = 126, green = 189, blue = 126)
        val INDEX_94: Color = ColorImplementation(colorIndex = 94, red = 0, green = 129, blue = 0)
        val INDEX_95: Color = ColorImplementation(colorIndex = 95, red = 86, green = 129, blue = 86)
        val INDEX_96: Color = ColorImplementation(colorIndex = 96, red = 0, green = 104, blue = 0)
        val INDEX_97: Color = ColorImplementation(colorIndex = 97, red = 69, green = 104, blue = 69)
        val INDEX_98: Color = ColorImplementation(colorIndex = 98, red = 0, green = 79, blue = 0)
        val INDEX_99: Color = ColorImplementation(colorIndex = 99, red = 53, green = 79, blue = 53)
        val INDEX_100: Color = ColorImplementation(colorIndex = 100, red = 0, green = 255, blue = 63)
        val INDEX_101: Color = ColorImplementation(colorIndex = 101, red = 170, green = 255, blue = 191)
        val INDEX_102: Color = ColorImplementation(colorIndex = 102, red = 0, green = 189, blue = 46)
        val INDEX_103: Color = ColorImplementation(colorIndex = 103, red = 126, green = 189, blue = 141)
        val INDEX_104: Color = ColorImplementation(colorIndex = 104, red = 0, green = 129, blue = 31)
        val INDEX_105: Color = ColorImplementation(colorIndex = 105, red = 86, green = 129, blue = 96)
        val INDEX_106: Color = ColorImplementation(colorIndex = 106, red = 0, green = 104, blue = 25)
        val INDEX_107: Color = ColorImplementation(colorIndex = 107, red = 69, green = 104, blue = 78)
        val INDEX_108: Color = ColorImplementation(colorIndex = 108, red = 0, green = 79, blue = 19)
        val INDEX_109: Color = ColorImplementation(colorIndex = 109, red = 53, green = 79, blue = 59)
        val INDEX_110: Color = ColorImplementation(colorIndex = 110, red = 0, green = 255, blue = 127)
        val INDEX_111: Color = ColorImplementation(colorIndex = 111, red = 170, green = 255, blue = 212)
        val INDEX_112: Color = ColorImplementation(colorIndex = 112, red = 0, green = 189, blue = 94)
        val INDEX_113: Color = ColorImplementation(colorIndex = 113, red = 126, green = 189, blue = 157)
        val INDEX_114: Color = ColorImplementation(colorIndex = 114, red = 0, green = 129, blue = 64)
        val INDEX_115: Color = ColorImplementation(colorIndex = 115, red = 86, green = 129, blue = 107)
        val INDEX_116: Color = ColorImplementation(colorIndex = 116, red = 0, green = 104, blue = 52)
        val INDEX_117: Color = ColorImplementation(colorIndex = 117, red = 69, green = 104, blue = 86)
        val INDEX_118: Color = ColorImplementation(colorIndex = 118, red = 0, green = 79, blue = 39)
        val INDEX_119: Color = ColorImplementation(colorIndex = 119, red = 53, green = 79, blue = 66)
        val INDEX_120: Color = ColorImplementation(colorIndex = 120, red = 0, green = 255, blue = 191)
        val INDEX_121: Color = ColorImplementation(colorIndex = 121, red = 170, green = 255, blue = 234)
        val INDEX_122: Color = ColorImplementation(colorIndex = 122, red = 0, green = 189, blue = 141)
        val INDEX_123: Color = ColorImplementation(colorIndex = 123, red = 126, green = 189, blue = 173)
        val INDEX_124: Color = ColorImplementation(colorIndex = 124, red = 0, green = 129, blue = 96)
        val INDEX_125: Color = ColorImplementation(colorIndex = 125, red = 86, green = 129, blue = 118)
        val INDEX_126: Color = ColorImplementation(colorIndex = 126, red = 0, green = 104, blue = 78)
        val INDEX_127: Color = ColorImplementation(colorIndex = 127, red = 69, green = 104, blue = 95)
        val INDEX_128: Color = ColorImplementation(colorIndex = 128, red = 0, green = 79, blue = 59)
        val INDEX_129: Color = ColorImplementation(colorIndex = 129, red = 53, green = 79, blue = 73)
        val INDEX_130: Color = ColorImplementation(colorIndex = 130, red = 0, green = 255, blue = 255)
        val INDEX_131: Color = ColorImplementation(colorIndex = 131, red = 170, green = 255, blue = 255)
        val INDEX_132: Color = ColorImplementation(colorIndex = 132, red = 0, green = 189, blue = 189)
        val INDEX_133: Color = ColorImplementation(colorIndex = 133, red = 126, green = 189, blue = 189)
        val INDEX_134: Color = ColorImplementation(colorIndex = 134, red = 0, green = 129, blue = 129)
        val INDEX_135: Color = ColorImplementation(colorIndex = 135, red = 86, green = 129, blue = 129)
        val INDEX_136: Color = ColorImplementation(colorIndex = 136, red = 0, green = 104, blue = 104)
        val INDEX_137: Color = ColorImplementation(colorIndex = 137, red = 69, green = 104, blue = 104)
        val INDEX_138: Color = ColorImplementation(colorIndex = 138, red = 0, green = 79, blue = 79)
        val INDEX_139: Color = ColorImplementation(colorIndex = 139, red = 53, green = 79, blue = 79)
        val INDEX_140: Color = ColorImplementation(colorIndex = 140, red = 0, green = 191, blue = 255)
        val INDEX_141: Color = ColorImplementation(colorIndex = 141, red = 170, green = 234, blue = 255)
        val INDEX_142: Color = ColorImplementation(colorIndex = 142, red = 0, green = 141, blue = 189)
        val INDEX_143: Color = ColorImplementation(colorIndex = 143, red = 126, green = 173, blue = 189)
        val INDEX_144: Color = ColorImplementation(colorIndex = 144, red = 0, green = 96, blue = 129)
        val INDEX_145: Color = ColorImplementation(colorIndex = 145, red = 86, green = 118, blue = 129)
        val INDEX_146: Color = ColorImplementation(colorIndex = 146, red = 0, green = 78, blue = 104)
        val INDEX_147: Color = ColorImplementation(colorIndex = 147, red = 69, green = 95, blue = 104)
        val INDEX_148: Color = ColorImplementation(colorIndex = 148, red = 0, green = 59, blue = 79)
        val INDEX_149: Color = ColorImplementation(colorIndex = 149, red = 53, green = 73, blue = 79)
        val INDEX_150: Color = ColorImplementation(colorIndex = 150, red = 0, green = 127, blue = 255)
        val INDEX_151: Color = ColorImplementation(colorIndex = 151, red = 170, green = 212, blue = 255)
        val INDEX_152: Color = ColorImplementation(colorIndex = 152, red = 0, green = 94, blue = 189)
        val INDEX_153: Color = ColorImplementation(colorIndex = 153, red = 126, green = 157, blue = 189)
        val INDEX_154: Color = ColorImplementation(colorIndex = 154, red = 0, green = 64, blue = 129)
        val INDEX_155: Color = ColorImplementation(colorIndex = 155, red = 86, green = 107, blue = 129)
        val INDEX_156: Color = ColorImplementation(colorIndex = 156, red = 0, green = 52, blue = 104)
        val INDEX_157: Color = ColorImplementation(colorIndex = 157, red = 69, green = 86, blue = 104)
        val INDEX_158: Color = ColorImplementation(colorIndex = 158, red = 0, green = 39, blue = 79)
        val INDEX_159: Color = ColorImplementation(colorIndex = 159, red = 53, green = 66, blue = 79)
        val INDEX_160: Color = ColorImplementation(colorIndex = 160, red = 0, green = 63, blue = 255)
        val INDEX_161: Color = ColorImplementation(colorIndex = 161, red = 170, green = 191, blue = 255)
        val INDEX_162: Color = ColorImplementation(colorIndex = 162, red = 0, green = 46, blue = 189)
        val INDEX_163: Color = ColorImplementation(colorIndex = 163, red = 126, green = 141, blue = 189)
        val INDEX_164: Color = ColorImplementation(colorIndex = 164, red = 0, green = 31, blue = 129)
        val INDEX_165: Color = ColorImplementation(colorIndex = 165, red = 86, green = 96, blue = 129)
        val INDEX_166: Color = ColorImplementation(colorIndex = 166, red = 0, green = 25, blue = 104)
        val INDEX_167: Color = ColorImplementation(colorIndex = 167, red = 69, green = 78, blue = 104)
        val INDEX_168: Color = ColorImplementation(colorIndex = 168, red = 0, green = 19, blue = 79)
        val INDEX_169: Color = ColorImplementation(colorIndex = 169, red = 53, green = 59, blue = 79)
        val INDEX_170: Color = ColorImplementation(colorIndex = 170, red = 0, green = 0, blue = 255)
        val INDEX_171: Color = ColorImplementation(colorIndex = 171, red = 170, green = 170, blue = 255)
        val INDEX_172: Color = ColorImplementation(colorIndex = 172, red = 0, green = 0, blue = 189)
        val INDEX_173: Color = ColorImplementation(colorIndex = 173, red = 126, green = 126, blue = 189)
        val INDEX_174: Color = ColorImplementation(colorIndex = 174, red = 0, green = 0, blue = 129)
        val INDEX_175: Color = ColorImplementation(colorIndex = 175, red = 86, green = 86, blue = 129)
        val INDEX_176: Color = ColorImplementation(colorIndex = 176, red = 0, green = 0, blue = 104)
        val INDEX_177: Color = ColorImplementation(colorIndex = 177, red = 69, green = 69, blue = 104)
        val INDEX_178: Color = ColorImplementation(colorIndex = 178, red = 0, green = 0, blue = 79)
        val INDEX_179: Color = ColorImplementation(colorIndex = 179, red = 53, green = 53, blue = 79)
        val INDEX_180: Color = ColorImplementation(colorIndex = 180, red = 63, green = 0, blue = 255)
        val INDEX_181: Color = ColorImplementation(colorIndex = 181, red = 191, green = 170, blue = 255)
        val INDEX_182: Color = ColorImplementation(colorIndex = 182, red = 46, green = 0, blue = 189)
        val INDEX_183: Color = ColorImplementation(colorIndex = 183, red = 141, green = 126, blue = 189)
        val INDEX_184: Color = ColorImplementation(colorIndex = 184, red = 31, green = 0, blue = 129)
        val INDEX_185: Color = ColorImplementation(colorIndex = 185, red = 96, green = 86, blue = 129)
        val INDEX_186: Color = ColorImplementation(colorIndex = 186, red = 25, green = 0, blue = 104)
        val INDEX_187: Color = ColorImplementation(colorIndex = 187, red = 78, green = 69, blue = 104)
        val INDEX_188: Color = ColorImplementation(colorIndex = 188, red = 19, green = 0, blue = 79)
        val INDEX_189: Color = ColorImplementation(colorIndex = 189, red = 59, green = 53, blue = 79)
        val INDEX_190: Color = ColorImplementation(colorIndex = 190, red = 127, green = 0, blue = 255)
        val INDEX_191: Color = ColorImplementation(colorIndex = 191, red = 212, green = 170, blue = 255)
        val INDEX_192: Color = ColorImplementation(colorIndex = 192, red = 94, green = 0, blue = 189)
        val INDEX_193: Color = ColorImplementation(colorIndex = 193, red = 157, green = 126, blue = 189)
        val INDEX_194: Color = ColorImplementation(colorIndex = 194, red = 64, green = 0, blue = 129)
        val INDEX_195: Color = ColorImplementation(colorIndex = 195, red = 107, green = 86, blue = 129)
        val INDEX_196: Color = ColorImplementation(colorIndex = 196, red = 52, green = 0, blue = 104)
        val INDEX_197: Color = ColorImplementation(colorIndex = 197, red = 86, green = 69, blue = 104)
        val INDEX_198: Color = ColorImplementation(colorIndex = 198, red = 39, green = 0, blue = 79)
        val INDEX_199: Color = ColorImplementation(colorIndex = 199, red = 66, green = 53, blue = 79)
        val INDEX_200: Color = ColorImplementation(colorIndex = 200, red = 191, green = 0, blue = 255)
        val INDEX_201: Color = ColorImplementation(colorIndex = 201, red = 234, green = 170, blue = 255)
        val INDEX_202: Color = ColorImplementation(colorIndex = 202, red = 141, green = 0, blue = 189)
        val INDEX_203: Color = ColorImplementation(colorIndex = 203, red = 173, green = 126, blue = 189)
        val INDEX_204: Color = ColorImplementation(colorIndex = 204, red = 96, green = 0, blue = 129)
        val INDEX_205: Color = ColorImplementation(colorIndex = 205, red = 118, green = 86, blue = 129)
        val INDEX_206: Color = ColorImplementation(colorIndex = 206, red = 78, green = 0, blue = 104)
        val INDEX_207: Color = ColorImplementation(colorIndex = 207, red = 95, green = 69, blue = 104)
        val INDEX_208: Color = ColorImplementation(colorIndex = 208, red = 59, green = 0, blue = 79)
        val INDEX_209: Color = ColorImplementation(colorIndex = 209, red = 73, green = 53, blue = 79)
        val INDEX_210: Color = ColorImplementation(colorIndex = 210, red = 255, green = 0, blue = 255)
        val INDEX_211: Color = ColorImplementation(colorIndex = 211, red = 255, green = 170, blue = 255)
        val INDEX_212: Color = ColorImplementation(colorIndex = 212, red = 189, green = 0, blue = 189)
        val INDEX_213: Color = ColorImplementation(colorIndex = 213, red = 189, green = 126, blue = 189)
        val INDEX_214: Color = ColorImplementation(colorIndex = 214, red = 129, green = 0, blue = 129)
        val INDEX_215: Color = ColorImplementation(colorIndex = 215, red = 129, green = 86, blue = 129)
        val INDEX_216: Color = ColorImplementation(colorIndex = 216, red = 104, green = 0, blue = 104)
        val INDEX_217: Color = ColorImplementation(colorIndex = 217, red = 104, green = 69, blue = 104)
        val INDEX_218: Color = ColorImplementation(colorIndex = 218, red = 79, green = 0, blue = 79)
        val INDEX_219: Color = ColorImplementation(colorIndex = 219, red = 79, green = 53, blue = 79)
        val INDEX_220: Color = ColorImplementation(colorIndex = 220, red = 255, green = 0, blue = 191)
        val INDEX_221: Color = ColorImplementation(colorIndex = 221, red = 255, green = 170, blue = 234)
        val INDEX_222: Color = ColorImplementation(colorIndex = 222, red = 189, green = 0, blue = 141)
        val INDEX_223: Color = ColorImplementation(colorIndex = 223, red = 189, green = 126, blue = 173)
        val INDEX_224: Color = ColorImplementation(colorIndex = 224, red = 129, green = 0, blue = 96)
        val INDEX_225: Color = ColorImplementation(colorIndex = 225, red = 129, green = 86, blue = 118)
        val INDEX_226: Color = ColorImplementation(colorIndex = 226, red = 104, green = 0, blue = 78)
        val INDEX_227: Color = ColorImplementation(colorIndex = 227, red = 104, green = 69, blue = 95)
        val INDEX_228: Color = ColorImplementation(colorIndex = 228, red = 79, green = 0, blue = 59)
        val INDEX_229: Color = ColorImplementation(colorIndex = 229, red = 79, green = 53, blue = 73)
        val INDEX_230: Color = ColorImplementation(colorIndex = 230, red = 255, green = 0, blue = 127)
        val INDEX_231: Color = ColorImplementation(colorIndex = 231, red = 255, green = 170, blue = 212)
        val INDEX_232: Color = ColorImplementation(colorIndex = 232, red = 189, green = 0, blue = 94)
        val INDEX_233: Color = ColorImplementation(colorIndex = 233, red = 189, green = 126, blue = 157)
        val INDEX_234: Color = ColorImplementation(colorIndex = 234, red = 129, green = 0, blue = 64)
        val INDEX_235: Color = ColorImplementation(colorIndex = 235, red = 129, green = 86, blue = 107)
        val INDEX_236: Color = ColorImplementation(colorIndex = 236, red = 104, green = 0, blue = 52)
        val INDEX_237: Color = ColorImplementation(colorIndex = 237, red = 104, green = 69, blue = 86)
        val INDEX_238: Color = ColorImplementation(colorIndex = 238, red = 79, green = 0, blue = 39)
        val INDEX_239: Color = ColorImplementation(colorIndex = 239, red = 79, green = 53, blue = 66)
        val INDEX_240: Color = ColorImplementation(colorIndex = 240, red = 255, green = 0, blue = 63)
        val INDEX_241: Color = ColorImplementation(colorIndex = 241, red = 255, green = 170, blue = 191)
        val INDEX_242: Color = ColorImplementation(colorIndex = 242, red = 189, green = 0, blue = 46)
        val INDEX_243: Color = ColorImplementation(colorIndex = 243, red = 189, green = 126, blue = 141)
        val INDEX_244: Color = ColorImplementation(colorIndex = 244, red = 129, green = 0, blue = 31)
        val INDEX_245: Color = ColorImplementation(colorIndex = 245, red = 129, green = 86, blue = 96)
        val INDEX_246: Color = ColorImplementation(colorIndex = 246, red = 104, green = 0, blue = 25)
        val INDEX_247: Color = ColorImplementation(colorIndex = 247, red = 104, green = 69, blue = 78)
        val INDEX_248: Color = ColorImplementation(colorIndex = 248, red = 79, green = 0, blue = 19)
        val INDEX_249: Color = ColorImplementation(colorIndex = 249, red = 79, green = 53, blue = 59)
        val INDEX_250: Color = ColorImplementation(colorIndex = 250, red = 51, green = 51, blue = 51)
        val INDEX_251: Color = ColorImplementation(colorIndex = 251, red = 80, green = 80, blue = 80)
        val INDEX_252: Color = ColorImplementation(colorIndex = 252, red = 105, green = 105, blue = 105)
        val INDEX_253: Color = ColorImplementation(colorIndex = 253, red = 130, green = 130, blue = 130)
        val INDEX_254: Color = ColorImplementation(colorIndex = 254, red = 190, green = 190, blue = 190)
        val INDEX_255: Color = ColorImplementation(colorIndex = 255, red = 255, green = 255, blue = 255)
    }
}