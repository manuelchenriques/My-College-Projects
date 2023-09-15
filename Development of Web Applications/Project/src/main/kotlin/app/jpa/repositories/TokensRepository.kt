package app.jpa.repositories

import app.jpa.entities.Token
import app.jpa.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TokensRepository: JpaRepository<Token,String>{

    fun findTokenById(id: String): Token?
    fun findTopByUseridOrderByCreatedAtAsc(user: User): Token?
    fun countTokenByUserid(userid: User): Int
    fun removeTokenById(id: String)
}