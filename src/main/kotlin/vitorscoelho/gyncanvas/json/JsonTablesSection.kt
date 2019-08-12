package vitorscoelho.gyncanvas.json

interface JsonTableRecord {
    val name: String
}

class JsonLayer(
    override val name: String,
    val colorNumber: Short
) : JsonTableRecord