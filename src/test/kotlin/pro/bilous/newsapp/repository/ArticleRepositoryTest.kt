package pro.bilous.newsapp.repository

import createTestArticle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import pro.bilous.newsapp.domain.Article
import java.time.LocalDateTime

@DataJpaTest
class ArticleRepositoryTest(@Autowired private val repository: ArticleRepository) {

    @Test
    fun `should find three articles between two dates`() {
        addArticlesToDbForRange(25, 29)
        val expectedHeaders = listOf("header-26", "header-27", "header-28")

        val result = repository.findAllByPublishDateBetween(
            LocalDateTime.of(2023, 10, 26, 23, 23),
            LocalDateTime.of(2023, 10, 28, 23, 23),
        )
        assertEquals(3, result.size)
        assertTrue(result.map { it.header }.all { expectedHeaders.contains(it) })
    }

    @Test
    fun `should find one article between two dates`() {
        addArticlesToDbForRange(24, 26)
        val expectedHeaders = listOf("header-26")

        val result = repository.findAllByPublishDateBetween(
            LocalDateTime.of(2023, 10, 26, 23, 23),
            LocalDateTime.of(2023, 10, 26, 23, 23),
        )
        assertEquals(1, result.size)
        assertTrue(result.map { it.header }.all { expectedHeaders.contains(it) })
    }

    @Test
    fun `should find one article by keyword`() {
        addArticlesToDbForRange(24, 26)
        val searchKeyword = "keywordFirst-25"

        val result = repository.findByKeyword(searchKeyword)
        assertEquals(1, result.size)
        assertTrue(result.all { it.keywords?.contains(searchKeyword) ?: false })
    }

    @Test
    fun `should find four articles by keyword`() {
        val searchKeyword = "keywordToSearch"
        addArticlesToDbForRange(24, 27, keyword = searchKeyword)

        val result = repository.findByKeyword(searchKeyword)
        assertEquals(4, result.size)
        assertTrue(result.all { it.keywords?.contains(searchKeyword) ?: false })
    }

    @Test
    fun `should not find any articles by keyword`() {
        val searchKeyword = "keywordToSearch"
        addArticlesToDbForRange(24, 27)

        val result = repository.findByKeyword(searchKeyword)
        assertEquals(0, result.size)
    }

    @Test
    fun `should find one article by author`() {
        addArticlesToDbForRange(24, 26)
        val searchAuthor = "authorFirst-25"

        val result = repository.findByAuthor(searchAuthor)
        assertEquals(1, result.size)
        assertTrue(result.all { it.authors?.contains(searchAuthor) ?: false })
    }

    @Test
    fun `should find four articles by author`() {
        val searchAuthor = "authorToSearch"
        addArticlesToDbForRange(24, 27, author = searchAuthor)

        val result = repository.findByAuthor(searchAuthor)
        assertEquals(4, result.size)
        assertTrue(result.all { it.authors?.contains(searchAuthor) ?: false })
    }

    @Test
    fun `should not find any articles by author`() {
        val searchAuthor = "authorToSearch"
        addArticlesToDbForRange(24, 27)

        val result = repository.findByAuthor(searchAuthor)
        assertEquals(0, result.size)
    }

    private fun addArticlesToDbForRange(first: Int, last: Int, keyword: String? = null, author: String? = null) {
        val savedArticles = mutableListOf<Article>()
        for (i in first..last) {
            val testArticle = createTestArticle(i)
            if (keyword != null) {
                testArticle.keywords = listOf(keyword)
            }
            if (author != null) {
                testArticle.authors = listOf(author)
            }
            testArticle.publishDate = LocalDateTime.of(2023, 10, i, 23, 23)
            savedArticles.add(repository.save(testArticle))
        }
    }
}