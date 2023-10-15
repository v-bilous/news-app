package pro.bilous.newsapp.rest

import createTestArticle
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import pro.bilous.newsapp.service.ArticleService
import articleId
import toJsonPost
import toJsonPut

@WebMvcTest
class ArticlesRestControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var articleService: ArticleService

    @Test
    fun `get by id articles`() {
        val article = createTestArticle()
        val articleId = article.articleId()
        whenever(articleService.getById(articleId)).thenReturn(article)

        mockMvc.perform(get("/articles/{id}", articleId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(article.id))
            .andExpect(jsonPath("\$.header").value(article.header))
    }

    @Test
    fun `should return 404 response code`() {
        val articleId = 1L
        whenever(articleService.getById(articleId)).thenThrow(EntityNotFoundException())

        mockMvc.perform(get("/articles/{id}", articleId).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `create article`() {
        val article = createTestArticle()
        whenever(articleService.create(any())).thenReturn(article)
        val jsonBody = article.toJsonPost()

        mockMvc.perform(post("/articles").content(jsonBody).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(article.id))
    }

    @Test
    fun `update article`() {
        val article = createTestArticle()
        val jsonBody = article.toJsonPut()

        mockMvc.perform(put("/articles/{id}", article.id).content(jsonBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andExpect(jsonPath("\$").doesNotExist())

    }

    @Test
    fun `delete article successfully`() {
        val articleId = 1
        mockMvc.perform(delete("/articles/{id}", articleId).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent)
            .andExpect(jsonPath("\$").doesNotExist())
    }

    @Test
    fun `find articles by period`() {
        val firstArticle = createTestArticle(1)
        val start = "2023-10-23T23:23:23"
        val end = "2023-10-24T23:23:23"
        whenever(articleService.findByPeriod(any(), any())).thenReturn(listOf(firstArticle))

        mockMvc.perform(get("/articles/byPeriod")
            .param("start", start)
            .param("end", end)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(firstArticle.id))
            .andExpect(jsonPath("\$.[0].header").value(firstArticle.header))
    }

    @Test
    fun `find articles by keyword`() {
        val firstArticle = createTestArticle(1)
        val secondArticle = createTestArticle(2)
        val searchKeyword = "keywordToSearch"
        whenever(articleService.findByKeyword(searchKeyword)).thenReturn(listOf(firstArticle, secondArticle))

        mockMvc.perform(get("/articles/byKeyword")
            .param("searchValue", searchKeyword)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(firstArticle.id))
            .andExpect(jsonPath("\$.[0].header").value(firstArticle.header))
            .andExpect(jsonPath("\$.[1].id").value(secondArticle.id))
            .andExpect(jsonPath("\$.[1].header").value(secondArticle.header))
    }

    @Test
    fun `find articles by author`() {
        val firstArticle = createTestArticle(1)
        val searchAuthor = "authorToSearch"
        whenever(articleService.findByAuthor(searchAuthor)).thenReturn(listOf(firstArticle))

        mockMvc.perform(get("/articles/byAuthor")
            .param("searchValue", searchAuthor)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(firstArticle.id))
            .andExpect(jsonPath("\$.[0].header").value(firstArticle.header))
    }
}