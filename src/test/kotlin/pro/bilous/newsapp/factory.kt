import pro.bilous.newsapp.domain.Article
import java.time.LocalDateTime

fun createTestArticle(number: Int = 1): Article {
    return Article(
        id = number.toLong(),
        header = "header-$number",
        shortDescription = "short description $number",
        textContent = "text content $number",
        keywords = listOf("keywordFirst-$number", "keywordSecond-$number"),
        authors = listOf("authorFirst-$number", "authorSecond-$number")
    )
}

fun createNewTestArticle(): Article {
    return createTestArticle().apply {
        id = null
    }
}

fun Article.articleId(): Long = id ?: throw IllegalStateException("Article id is null")

fun Article.toJsonPost(): String {
    return """
           {
                "header": "$header",
                "shortDescription" : "$shortDescription",
                "textContent": "$textContent",
                "publishDate": "2023-10-23T23:23:23"
            }
            """.trimIndent()
}

fun Article.toJsonPut(): String {
    return """
           {
                "id": $id,
                "header": "$header",
                "shortDescription" : "$shortDescription",
                "textContent": "$textContent",
                "publishDate": "2023-10-23T23:23:23"
            }
            """.trimIndent()
}
