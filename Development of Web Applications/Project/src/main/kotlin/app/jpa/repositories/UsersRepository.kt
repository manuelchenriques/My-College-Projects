package app.jpa.repositories

import app.jpa.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID
@Repository
interface UsersRepository:JpaRepository<User, UUID> {
    fun findUserByUsername(username: String) : User?
    fun findUserById(id: UUID) : User?
    fun existsUserByUsername(username: String) : Boolean

}