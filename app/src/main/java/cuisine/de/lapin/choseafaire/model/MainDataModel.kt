package cuisine.de.lapin.choseafaire.model

data class MainDataModel (
    val background: String,
    val quote: Quote
)

data class Quote (
    val quote: String,
    val author: String
)