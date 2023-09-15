package app.jpa.entities

import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "users", schema = "dbo"
)
open class User(username: String, encodedPassword: String){
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @Column(name = "username", nullable = false, length = 64)
    open var username: String? = username

    @Column(name = "passwordinfo", nullable = false, length = 256)
    open var passwordinfo: String? = encodedPassword

    @OneToMany(mappedBy = "userid")
    open var tokens: MutableSet<Token> = mutableSetOf()

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "stats", referencedColumnName = "id")
    open var stats: UserStat? = UserStat()

    init {
        this.id = UUID.randomUUID()
    }
}