package app.http.model

class UserTokenCreateOutputModel(
    val token: String
)

class UserHomeOutputModel(
    val id: String,
    val username: String,
    val stats: Int
)


class LeaderboardOutputModel(
    val stats : List<UserStatOutputModel>
)

class UserStatOutputModel(
    val id: Int,
    val username: String,
    val gamesPlayed: Int,
    val gamesWon: Int
)