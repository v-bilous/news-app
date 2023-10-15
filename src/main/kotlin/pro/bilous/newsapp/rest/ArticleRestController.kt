package pro.bilous.newsapp.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pro.bilous.newsapp.domain.Article
import pro.bilous.newsapp.service.ArticleService
import java.time.LocalDateTime

@RestController
class ArticleRestController(val service: ArticleService) {

    @PostMapping("/articles")
    fun createArticle(@RequestBody article: Article): ResponseEntity<Article> {
        val createdArticle = service.create(article)
        return ResponseEntity(createdArticle, HttpStatus.CREATED)
    }

    @GetMapping("/articles/{id}")
    fun getArticleById(@PathVariable id: Long): ResponseEntity<Article> {
        return ResponseEntity(service.getById(id), HttpStatus.OK)
    }

    @PutMapping("/articles/{id}")
    fun updateArticle(@PathVariable id: Long, @RequestBody article: Article): ResponseEntity<Void> {
        service.update(id, article)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/articles/{id}")
    fun deleteArticle(@PathVariable id: Long): ResponseEntity<Void> {
        service.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @GetMapping("/articles/byPeriod")
    fun findArticlesByAuthor(@RequestParam start: LocalDateTime, @RequestParam end: LocalDateTime): List<Article> {
        return service.findByPeriod(start, end)
    }

    @GetMapping("/articles/byKeyword")
    fun findArticlesByKeyword(@RequestParam searchValue: String): List<Article> {
        return service.findByKeyword(searchValue)
    }

    @GetMapping("/articles/byAuthor")
    fun findArticlesByAuthor(@RequestParam searchValue: String): List<Article> {
        return service.findByAuthor(searchValue)
    }
}