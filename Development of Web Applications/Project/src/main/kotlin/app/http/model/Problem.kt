package app.http.model

import org.springframework.http.ResponseEntity
import java.net.URI

class Problem(
    typeUri: URI
) {
    val type: String = typeUri.toASCIIString()

    companion object {
        private const val MEDIA_TYPE = "application/problem+json"
        private const val DOC_LINK = "https://github.com/isel-leic-daw/2022-daw-leic52d-2022-daw-leic52d-g01/tree/main/docs"

        fun response(status: Int, problem: Problem) = ResponseEntity
            .status(status)
            .header("Content-Type", MEDIA_TYPE)
            .body(problem)

        val placementInvalid = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/placement-invalid"
            )
        )
        val missingShips = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/missing-ships"
            )
        )

        val insecurePassword = Problem(
            URI(
                    DOC_LINK +
                        "docs/problems/insecure-password"
            )
        )

        val userAlreadyExists = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/user-already-exists"
            )
        )

        val authenticationFailed = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/failed-authentication"
            )
        )

        val userNotFound = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/user-not-found"
            )
        )

        val orderByInvalid = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/order-by-invalid"
            )
        )

        val notAPlayer = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/not-player"
            )
        )

        val actionNotPermitted = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/action-not-permitted"
            )
        )

        val invalidSize = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/invalid-ship-size"
            )
        )

        val invalidShipType = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/invalid-ship-type"
            )
        )

        val invalidShipCoordinate = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/invalid-ship-position"
            )
        )

        val alreadyPlaced = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/ship-already-placed"
            )
        )

        val timeout = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/timeout"
            )
        )

        val invalidShotNumber = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/invalid-shot-number"
            )
        )

        val invalidShotPosition = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/invalid-shot-position"
            )
        )

        val failedToJoin = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/failed-to-join"
            )
        )

        val alreadyEnded = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/game-ended"
            )
        )

        val gameNotFound = Problem(
            URI(
                DOC_LINK +
                        "docs/problems/game-notFound"
            )
        )









    }
}