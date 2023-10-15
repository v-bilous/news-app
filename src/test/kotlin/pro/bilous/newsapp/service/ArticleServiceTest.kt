package pro.bilous.newsapp.service

import articleId
import createTestArticle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import pro.bilous.newsapp.domain.Article
import pro.bilous.newsapp.repository.ArticleRepository
import java.time.LocalDateTime
import kotlin.IllegalArgumentException

class ArticleServiceTest {

    private val repository: ArticleRepository = mock()
    private val service = ArticleService(repository)

    @Test
    fun `get article by id`() {
        val article = createTestArticle()
        val id = article.articleId()
        whenever(repository.getReferenceById(id)).thenReturn(article)

        val result = service.getById(id)

        assertEquals(id, result.id)
        assertEquals(article.header, result.header)
        assertEquals(article.shortDescription, result.shortDescription)
        assertEquals(article.keywords, result.keywords)
        assertEquals(article.authors, result.authors)
    }

    @Test
    fun `should create an article`() {
        val article = createTestArticle()
        article.id = null
        whenever(repository.save(article)).thenReturn(article)

        val result = service.create(article)
        assertEquals(article.header, result.header)
    }

    @Test
    fun `should validate id is null when create`() {
        val article = createTestArticle()
        article.id = null
        whenever(repository.save(article)).thenReturn(article)

        service.create(article)

        argumentCaptor<Article>().apply {
            verify(repository, times(1)).save(capture())
            assertNull(firstValue.id)
        }
    }

    @Test
    fun `when create should throw exception if id is not null`() {
        val article = createTestArticle()
        whenever(repository.save(article)).thenReturn(article)
        assertThrows<IllegalArgumentException> {
            service.create(article)
        }
    }

    @Test
    fun `should update an article`() {
        val article = createTestArticle()
        val articleId = article.articleId()
        whenever(repository.getReferenceById(articleId)).thenReturn(article)
        whenever(repository.save(article)).thenReturn(article)

        service.update(articleId, article)

        argumentCaptor<Article>().apply {
            verify(repository, times(1)).save(capture())
            assertEquals(article.id, firstValue.id)
        }
    }

    @Test
    fun `when update should throw exception if id has invalid value in json body `() {
        val firstArticle = createTestArticle(1)
        val secondArticle = createTestArticle(2)
        val otherId = secondArticle.articleId()
        whenever(repository.getReferenceById(otherId)).thenReturn(secondArticle)

        assertThrows<IllegalArgumentException> {
            service.update(otherId, firstArticle)
        }
        argumentCaptor<Article>().apply {
            verify(repository, times(0)).save(capture())
        }
    }

    @Test
    fun `should update when id is null in json body`() {
        val firstArticle = createTestArticle(1)
        val secondArticle = createTestArticle(2)
        val otherId = secondArticle.articleId()
        whenever(repository.getReferenceById(otherId)).thenReturn(secondArticle)

        firstArticle.id = null
        service.update(otherId, firstArticle)

        argumentCaptor<Article>().apply {
            verify(repository, times(1)).save(capture())
            assertEquals(otherId, firstValue.id)
        }
    }

    @Test
    fun `should delete an article`() {
        val articleId = 1L
        service.deleteById(articleId)

        argumentCaptor<Long>().apply {
            verify(repository, times(1)).deleteById(capture())
            assertEquals(articleId, firstValue)
        }
    }

    @Test
    fun `should find articles by period`() {
        val start = LocalDateTime.of(2023, 10, 23, 23, 23)
        val end = LocalDateTime.of(2023, 10, 24, 23, 23)

        whenever(repository.findAllByPublishDateBetween(start, end))
            .thenReturn(listOf(createTestArticle(1), createTestArticle(2)))

        val result = service.findByPeriod(start, end)
        assertEquals(2, result.size)
    }

    @Test
    fun `should fail when find articles by period because date range is invalid`() {
        val start = LocalDateTime.of(2023, 10, 24, 23, 23)
        val end = LocalDateTime.of(2023, 10, 23, 23, 23)

        assertThrows<IllegalArgumentException> {
            service.findByPeriod(start, end)
        }
    }

    @Test
    fun `should find articles by keyword`() {
        val searchKeyword = "keywordToSearch"

        whenever(repository.findByKeyword(searchKeyword))
            .thenReturn(listOf(createTestArticle(1), createTestArticle(2)))

        val result = service.findByKeyword(searchKeyword)
        assertEquals(2, result.size)
    }

    @Test
    fun `should find articles by author`() {
        val searchAuthor = "authorToSearch"

        whenever(repository.findByAuthor(searchAuthor))
            .thenReturn(listOf(createTestArticle(1), createTestArticle(2)))

        val result = service.findByAuthor(searchAuthor)
        assertEquals(2, result.size)
    }
}
