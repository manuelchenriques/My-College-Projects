package app.jpa.entities

import java.time.Duration
import java.time.Instant
import javax.persistence.*

@Entity
@Table(name = "tokens", schema = "dbo")
open class Token(token: String, user: User, created: Instant, lastUsed: Instant){
    @Id
    @Column(name = "tokenvalidationinfo", nullable = false, length = 256)
    open var id: String? = token

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid")
    open var userid: User? = user

    @Column(name = "created_at", nullable = false)
    open var createdAt: Long? = null

    @Column(name = "last_used_at", nullable = false)
    open var lastUsedAt: Long? = null

    init {
        this.createdAt = created.toEpochMilli()
        this.lastUsedAt = lastUsed.toEpochMilli()
    }

    companion object{
        val MAX_TOKENS: Int = 3
        val TOKENS_TTL: Duration = Duration.ofDays(1)
    }
}