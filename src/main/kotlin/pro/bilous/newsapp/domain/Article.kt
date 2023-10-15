package pro.bilous.newsapp.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "article")
data class Article(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "header")
    var header: String? = null,

    @Column(name = "short_description")
    var shortDescription: String? = null,

    @Column(name = "text_content")
    var textContent: String? = null,

    @Column(name = "publish_date")
    var publishDate: LocalDateTime? = null,

    @ElementCollection
    @CollectionTable(name = "article_keyword", joinColumns = [JoinColumn(name = "article_id")])
    @Column(name = "keyword", nullable = false)
    var keywords: List<String>? = null,

    @ElementCollection
    @CollectionTable(name = "article_author", joinColumns = [JoinColumn(name = "article_id")])
    @Column(name = "author", nullable = false)
    var authors: List<String>? = null
)
