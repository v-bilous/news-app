package pro.bilous.newsapp.service

import org.springframework.stereotype.Service
import pro.bilous.newsapp.domain.Article
import pro.bilous.newsapp.repository.ArticleRepository
import java.time.LocalDateTime

@Service
class ArticleService(private val repository: ArticleRepository) {

    fun getById(id: Long): Article {
        return repository.getReferenceById(id)
    }

    fun update(articleId: Long, article: Article) {
        val existingArticle = repository.getReferenceById(articleId)
        if (article.id == null) {
            article.id = existingArticle.id
        } else if (existingArticle.id != article.id) {
            throw IllegalArgumentException("Article id in json body has invalid value")
        }
        repository.save(article)
    }

    fun create(article: Article): Article {
        if (article.id != null) {
            throw IllegalArgumentException("Article id should be null")
        }
        return repository.save(article)
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    fun findByPeriod(start: LocalDateTime, end: LocalDateTime): List<Article> {
        if (start.isAfter(end)) {
            throw IllegalArgumentException("start datetime is after end")
        }
        return repository.findAllByPublishDateBetween(start, end)
    }

    fun findByKeyword(keyword: String): List<Article> {
        return repository.findByKeyword(keyword)
    }

    fun findByAuthor(author: String): List<Article> {
        return repository.findByAuthor(author)
    }
}