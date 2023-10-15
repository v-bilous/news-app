package pro.bilous.newsapp.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pro.bilous.newsapp.domain.Article
import java.time.LocalDateTime

interface ArticleRepository :  JpaRepository<Article, Long> {

    fun findAllByPublishDateBetween(start: LocalDateTime, end: LocalDateTime): List<Article>

    @Query("select a from Article a join a.keywords k where k = :keyword")
    fun findByKeyword(keyword: String): List<Article>

    @Query("select a from Article a join a.authors k where k = :author")
    fun findByAuthor(author: String): List<Article>
}