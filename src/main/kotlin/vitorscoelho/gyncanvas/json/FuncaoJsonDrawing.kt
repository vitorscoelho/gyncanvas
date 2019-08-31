package vitorscoelho.gyncanvas.json

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = FuncaoLinha::class, name = "FuncaoLinha")
)
interface FuncaoJsonDrawing {
    fun json(): JsonDrawing
}

class FuncaoLinha(
    val ponto1: JsonPoint3d,
    val ponto2: JsonPoint3d
) : FuncaoJsonDrawing {
    override fun json(): JsonDrawing {
        val linha = JsonLine(
            layerName = "0",
            colorNumber = 7,
            startPoint = ponto1,
            endPoint = ponto2
        )
        return JsonDrawing(
            tablesSection = emptyList(),
            entitiesSection = listOf(linha)
        )
    }
}