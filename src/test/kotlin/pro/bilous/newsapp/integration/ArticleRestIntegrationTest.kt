package pro.bilous.newsapp.integration

import createNewTestArticle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import pro.bilous.newsapp.domain.Article
import pro.bilous.newsapp.repository.ArticleRepository
import pro.bilous.newsapp.rest.error.ErrorMessage
import java.lang.IllegalStateException
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ArticleRestIntegrationTest(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val repository: ArticleRepository) {

    @Test
    fun `create one article`() {
        val testArticle = createNewTestArticle()

        val result = restTemplate.postForEntity("/articles", testArticle, Article::class.java)
        val createdArticle = result.body ?: throw IllegalStateException("response is null")

        assertEquals(testArticle.header, createdArticle.header)
        assertEquals(testArticle.textContent, createdArticle.textContent)
    }

    @Test
    fun `get one article`() {
        val savedArticle = repository.save(createNewTestArticle())
        val result = restTemplate.getForEntity("/articles/${savedArticle.id}", Article::class.java)
        val article = result.body ?: throw IllegalStateException("response body is null")
        assertEquals(savedArticle.id, article.id)
    }

    @Test
    fun `find articles by period`() {
        val firstArticle = repository.save(createNewTestArticle().apply {
            publishDate = LocalDateTime.of(2023, 10, 23, 23, 23,)
        })
        repository.save(createNewTestArticle().apply {
            publishDate = LocalDateTime.of(2023, 10, 27, 23, 23, )
        })
        val startEndSearch = "start=2023-10-22T23:23:23&end=2023-10-25T23:23:23"
        val result = restTemplate.getForEntity("/articles/byPeriod?$startEndSearch", Array<Article>::class.java)
        val articles = result.body ?: throw IllegalStateException("response body is null")
        assertEquals(1, articles.size)
        assertEquals(firstArticle.publishDate, articles.first().publishDate)
    }

    @Test
    fun `fail when find articles by period start date is after end`() {
        val startEndSearch = "start=2023-10-25T23:23:23&end=2023-10-22T23:23:23"
        val result = restTemplate.getForEntity("/articles/byPeriod?$startEndSearch", ErrorMessage::class.java)
        assertTrue(result.statusCode.is4xxClientError)
    }
}
