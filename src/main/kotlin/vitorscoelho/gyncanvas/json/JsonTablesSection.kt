package vitorscoelho.gyncanvas.json

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder(NOME_VARIAVEL_TYPE)
interface JsonTableRecord {
    val name: String
}

class JsonLayer(
    override val name: String,
    val colorNumber: Short
) : JsonTableRecord {
    @JsonProperty(NOME_VARIAVEL_TYPE)
    val tipoParaExportacao: String = tipoJson(classeMae = "Tables", subclasse = "JsonLayer")
}