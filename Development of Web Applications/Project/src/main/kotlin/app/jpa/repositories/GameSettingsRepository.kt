package app.jpa.repositories

import app.jpa.entities.GameSetting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameSettingsRepository : JpaRepository<GameSetting, Int> {
}