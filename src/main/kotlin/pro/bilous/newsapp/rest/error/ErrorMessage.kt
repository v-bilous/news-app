package pro.bilous.newsapp.rest.error

data class ErrorMessage(
    var status: Int? = null,
    var message: String? = null
)