package com.example.battleships.views.lobby.playGame

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleships.R
import com.example.battleships.model.GameImage
import com.example.battleships.model.PlayInputModel
import com.example.battleships.model.User
import com.example.battleships.model.gameDomain.Position
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.ui.theme.TypoBattle
import com.example.battleships.views.composables.BackgroundImage
import com.example.battleships.views.composables.GridCell
import com.example.battleships.views.composables.UserCard
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayGameScreen (
    game: GameImage?,
    user: String?,
    onQuit: () -> Unit,
    checkGameState: () -> Unit,
    isPlayer1: Boolean,
    makeShot: (PlayInputModel) -> Unit,
    goBack: () -> Unit,
    opponent: String?
){
    var gameImage by remember {
        mutableStateOf(game)
    }

    var gameState by remember {
        mutableStateOf("PT1")
    }

    var isTurn by remember {
        mutableStateOf((isPlayer1 && gameState == "PT1") || (!isPlayer1 && gameState == "PT2"))
    }

    var shotToMake by remember {
        mutableStateOf<Position?>(null)
    }

    var ticks by remember { mutableStateOf(180) }

    LaunchedEffect(Unit){
        while (ticks > 0 && gameState != "P1W" && gameState != "P2W"){
            delay(1000)
            ticks--
            checkGameState()
            gameImage = PlayGameActivity.game
            gameState = gameImage!!.gameState
            if (isTurn){
                if((isPlayer1 && gameState == "PT2") || (!isPlayer1 && gameState == "PT1")){
                    isTurn = false
                    ticks = 180
                }
            }
            else{
                if((isPlayer1 && gameState == "PT1") || (!isPlayer1 && gameState == "PT2")) {
                    isTurn = true
                    ticks = 180
                }
            }
        }
    }

    BattleshipsTheme{
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("PlayGameScreen"),
        ) {
            BackgroundImage()
            if (gameState == "P1W" || gameState == "P2W"){
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .requiredSize(250.dp, 250.dp)
                        .border(5.dp, Color.Black, RoundedCornerShape(10))
                        .background(
                            Color(red = 255, green = 255, blue = 255, alpha = 89),
                            RoundedCornerShape(10)
                        ),
                ){
                    Text(
                        text = if ((isPlayer1 && gameState == "P1W") || (!isPlayer1 && gameState == "P2W")) "You Won!" else "You Lost...",
                        style = TypoBattle.h3,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                    )
                    Button(
                        onClick = goBack,
                        modifier = Modifier.requiredSize(100.dp,50.dp)
                    ) {
                        Text(text = "Return")
                    }
                }
            }
            else{
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onQuit ,
                            modifier = Modifier
                                .testTag("QuitButton")
                                .size(height = 60.dp, width = 60.dp)
                                .padding(1.dp)
                        ) {
                            Image(
                                painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.back_arrow_white else R.drawable.back_arrow),
                                contentDescription = "go_back_logo",
                                modifier = Modifier.size(
                                    50.dp, 50.dp
                                )
                            )
                        }
                        Text(
                            text = if (isTurn) "$user's turn" else "$opponent's turn",
                            style = TypoBattle.h3,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.requiredWidth(200.dp)
                        )
                        Text(
                            text = "${ticks / 60}:${String.format("%02d", ticks % 60)}",
                            style = TypoBattle.h1,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                    }
                    UserCard(user = user)
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(10),
                        contentPadding = PaddingValues(1.dp),
                        modifier = Modifier
                            .requiredSize(185.dp)
                            .border(color = Color.Black, width = 5.dp, shape = RectangleShape),
                    ){
                        items(Position.list){ pos ->
                            Card(
                                backgroundColor = Color(red = 255, green = 255, blue = 255, alpha = 72),
                                modifier = Modifier
                                    .padding(1.dp)
                                    .requiredSize(16.dp)
                            ){
                                GridCell(gameImage!!.myBoard[pos],10.dp,16.dp)
                            }
                        }
                    }
                    UserCard(user = opponent)
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(10),
                        contentPadding = PaddingValues(1.dp),
                        modifier = Modifier
                            .requiredSize(345.dp)
                            .border(color = Color.Black, width = 5.dp, shape = RectangleShape),
                    ){
                        items(Position.list){ pos ->
                            Card(
                                backgroundColor = Color(red = 255, green = 255, blue = 255, alpha = 72),
                                modifier = Modifier
                                    .padding(1.dp)
                                    .requiredSize(32.dp)
                                    .clickable(
                                        enabled = isTurn && gameImage!!.opponentBoard[pos] == null,
                                    ) {
                                        shotToMake = if (shotToMake == pos) {
                                            null
                                        } else {
                                            pos
                                        }
                                    }
                            ){
                                GridCell(gameImage!!.opponentBoard[pos],25.dp,32.dp)
                                if (shotToMake == pos){
                                    Canvas(modifier = Modifier.requiredSize(25.dp)){
                                        this.drawCircle(color = Color(
                                            red = 0,
                                            green = 0,
                                            blue = 0,
                                            alpha = 255
                                        )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Button(
                        onClick = {
                            makeShot(PlayInputModel(listOf(shotToMake!!)))
                            shotToMake = null
                        },
                        enabled = shotToMake != null
                    ) {
                        Image(
                            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.shot_white_logo else R.drawable.shot_logo),
                            contentDescription = "shot_logo",
                            modifier = Modifier.requiredSize(
                                50.dp, 30.dp
                            )
                        )
                    }
                }

            }

        }
    }
}


@Preview
@Composable
fun PlayPreviewScreen(){
    PlayGameScreen(
        game = GameImage(
            id = 0,
            player1 = UUID.randomUUID(),
            player2 = null,
            gameState = "PT1",
            myBoard = hashMapOf(
                Position(0,0) to "Miss",
                Position(1,1) to "Hit",
                Position(2,2) to "Sunk",
                Position(3,3) to "Ship",
                Position(4,4) to "Sunk"
            ),
            myFleet = arrayListOf(),
            opponentBoard = hashMapOf(
                Position(0,0) to "Miss",
                Position(1,1) to "Hit",
                Position(2,2) to "Sunk",
                Position(4,4) to "Sunk"
            ),
            opponentFleet = mutableListOf(),
            remainingTime = 1000,
        ),
        user = "Juan",
        onQuit = {},
        checkGameState = {},
        isPlayer1 = true,
        makeShot = {},
        goBack = {},
        opponent = "Yeetus"
    )
}


