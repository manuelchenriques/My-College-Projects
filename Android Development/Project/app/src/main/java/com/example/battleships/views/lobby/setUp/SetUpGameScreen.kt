package com.example.battleships.views.lobby.setUp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleships.R
import com.example.battleships.gameLogic.*
import com.example.battleships.model.GameImage
import com.example.battleships.model.gameDomain.Orientation
import com.example.battleships.model.gameDomain.Position
import com.example.battleships.model.gameDomain.ShipType
import com.example.battleships.model.gameDomain.getShipTypeOrNull
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.ui.theme.TypoBattle
import com.example.battleships.views.composables.BackgroundImage
import com.example.battleships.views.composables.WaitingBox
import kotlinx.coroutines.delay
import java.util.*

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetUpGameScreen(
    game: GameImage?,
    user: String?,
    onQuit: (String) -> Unit,
    endSetUp: (Map<ShipType, Pair<Orientation, Position>>) -> Unit,
    checkGameState: () -> Unit,
    isPlayer1: Boolean,
    startPlaying: () -> Unit,
    opponent: String?
) {
    var gameImage by remember {
        mutableStateOf(game)
    }
    var gameState by remember {
        mutableStateOf("SP")
    }

    var isSetUp by remember {
        mutableStateOf(false)
    }

    var hasQuited by remember {
        mutableStateOf(false)
    }

    var inPage by remember {
        mutableStateOf(true)
    }

    var ticks by remember { mutableStateOf(300) }
    LaunchedEffect(Unit) {
        while(ticks>0 && gameState != "PT1" && inPage) {
            checkGameState()
            gameImage = SetUpGameActivity.game
            gameState = gameImage!!.gameState
            if ((isPlayer1 && gameState == "R1") || (!isPlayer1 && gameState == "R2") && !isSetUp)
                isSetUp = true
            if (gameState == "P1W" || gameState == "P2W"){
                inPage = false
            }
            delay(1000)
            ticks--
        }
        if (!inPage){
            if (!hasQuited) {
                delay(3000)
            }
            onQuit(gameState)
        }
        if (gameState == "PT1"){
            startPlaying()
        }
    }

    var counter by remember {
        mutableStateOf(
            mapOf(
                "Carrier" to false,
                "Battleship" to false,
                "Cruiser" to false,
                "Submarine" to false,
                "Destroyer" to false
            )
        )
    }
    val options = listOf(
        "Carrier",
        "Battleship",
        "Cruiser",
        "Submarine",
        "Destroyer"
    )
    var placedShips by remember {
        mutableStateOf(
            mapOf<ShipType,List<Position>>()
        )
    }
    
    val orientationOptions = listOf(Orientation.Down,Orientation.Right)
    var selectedOrientation by remember {
        mutableStateOf(orientationOptions[0])
    }

    var selectedOption by remember { mutableStateOf("") }

    val onSelectionChange = { text: String ->
        selectedOption = text
        Log.v("Change", text)
    }

    BattleshipsTheme{
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("SetUpGameScreen"),
        ) {
            BackgroundImage()
            if (ticks > 0 && inPage){
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
                            onClick = {
                                hasQuited = true
                                inPage = false
                            },
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
                            text = "${ticks/60}:${String.format("%02d",ticks%60)}",
                            style = TypoBattle.h1,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){
                        Card(
                            border = BorderStroke(4.dp, Color.Black),
                            shape = CircleShape,
                            backgroundColor = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        ) {
                            Text(
                                text = user.toString(),
                                style = TypoBattle.h3,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                            )
                        }
                    }
                    if (isSetUp){
                        WaitingBox(text = "Waiting for $opponent to ready up...")
                    }
                    else{
                        options.forEach { text ->
                            Row(
                                modifier = Modifier
                                    .padding(
                                        all = 2.dp,
                                    ),
                            ) {
                                Text(
                                    text = text,
                                    textAlign = TextAlign.Center,
                                    style = TypoBattle.h3,
                                    color = Color.Black,
                                    modifier = Modifier
                                        .clip(
                                            shape = RoundedCornerShape(
                                                size = 12.dp,
                                            ),
                                        )
                                        .selectable(
                                            selected = selectedOption == text && !counter[text]!!,
                                            enabled = !counter[text]!!,
                                            onClick = {
                                                onSelectionChange(text)
                                            }
                                        )
                                        .background(
                                            if (counter[text]!!) {
                                                Color.LightGray
                                            } else {
                                                if (text == selectedOption) {
                                                    Color(
                                                        red = 175,
                                                        green = 121,
                                                        blue = 101,
                                                        alpha = 255
                                                    )
                                                } else {
                                                    Color.White
                                                }
                                            }
                                        )
                                        .size(
                                            width = 200.dp,
                                            height = 40.dp
                                        )
                                        .padding(
                                            vertical = 3.dp,
                                            horizontal = 4.dp,
                                        ),
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .selectableGroup()
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            orientationOptions.forEach { orientation ->
                                Column(modifier = Modifier
                                    .selectable(
                                        selected = (selectedOrientation == orientation),
                                        onClick = { selectedOrientation = orientation },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                ) {
                                    Text(
                                        text = orientation.name,
                                        textAlign = TextAlign.Center,
                                        style = TypoBattle.h3,
                                        color = Color.Black,
                                        modifier = Modifier
                                            .clip(
                                                shape = RoundedCornerShape(
                                                    size = 12.dp,
                                                ),
                                            )
                                            .background(
                                                if (orientation == selectedOrientation) {
                                                    Color(
                                                        red = 123,
                                                        green = 139,
                                                        blue = 141,
                                                        alpha = 255
                                                    )
                                                } else {
                                                    Color.White
                                                }
                                            )
                                            .size(
                                                width = 90.dp,
                                                height = 40.dp
                                            )
                                            .padding(
                                                vertical = 3.dp,
                                                horizontal = 4.dp,
                                            ),
                                    )
                                }
                            }
                        }
                        LazyVerticalGrid(
                            cells = GridCells.Fixed(10),
                            contentPadding = PaddingValues(1.dp),
                            modifier = Modifier
                                .requiredSize(345.dp)
                                .border(color = Color.Black, width = 5.dp, shape = RectangleShape),
                        ){
                            items(Position.list){ pos ->
                                val isIn:ShipType? = isPosIn(pos,placedShips)
                                Card(
                                    backgroundColor = Color(red = 255, green = 255, blue = 255, alpha = 72),
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .requiredSize(32.dp)
                                        .clickable(
                                            enabled = isIn != null || (!counter.all { it.value } && selectedOption != ""),
                                        ) {
                                            if (isIn == null) {
                                                val pair = canBePut(
                                                    pos,
                                                    selectedOption.getShipTypeOrNull()!!,
                                                    selectedOrientation,
                                                    placedShips
                                                )
                                                if (pair.first) {
                                                    placedShips = buildMap(placedShips.size + 1) {
                                                        putAll(placedShips)
                                                        put(
                                                            selectedOption.getShipTypeOrNull()!!,
                                                            pair.second
                                                        )
                                                    }
                                                    counter = buildMap {
                                                        putAll(counter)
                                                        this[selectedOption] = true
                                                    }
                                                    selectedOption = ""
                                                }
                                            } else {
                                                placedShips = buildMap {
                                                    putAll(placedShips)
                                                    remove(isIn)
                                                }
                                                counter = buildMap {
                                                    putAll(counter)
                                                    this[isIn.name] = false
                                                }
                                            }
                                        }
                                ){
                                    if (isIn != null){
                                        Canvas(modifier = Modifier.requiredSize(40.dp)){
                                            this.drawRoundRect(color = Color(red = 48, green = 51, blue = 54, alpha = 255))
                                        }
                                    }
                                }
                            }
                        }
                        Button(
                            onClick = {
                                Log.v("End",placedShips.toString())
                                Log.v("Status",isSetUp.toString())
                                endSetUp(placedShips.positionListToPlacement())
                            },
                            enabled = counter.all { count ->
                                count.value
                            } && ticks > 0
                        ) {
                            Image(
                                painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.end_setup_white_logo else R.drawable.end_setup_logo),
                                contentDescription = "go_back_logo",
                                modifier = Modifier.requiredSize(
                                    50.dp, 30.dp
                                )
                            )
                        }
                    }

                }
            }
            else{
                if (!hasQuited) {
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
                    ) {
                        Text(
                            text = if (inPage) "Time's up!" else "Opponent quited",
                            style = TypoBattle.h3,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary,
                        )
                        Button(
                            onClick = {
                                inPage = false
                            },
                            modifier = Modifier.requiredSize(100.dp, 50.dp)
                        ) {
                            Text(text = "Exit")
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun SetUpPreviewScreen(){
    SetUpGameScreen(
        game = GameImage(
            id = 0,
            player1 = UUID.randomUUID(),
            player2 = null,
            gameState = "SP",
            myBoard = hashMapOf(),
            myFleet = arrayListOf(),
            opponentBoard = hashMapOf(),
            opponentFleet = mutableListOf(),
            remainingTime = 1000
        ),
        user = "Juan",
        onQuit = {},
        endSetUp = {},
        checkGameState = {},
        isPlayer1 = SetUpGameActivity.player1,
        startPlaying = {},
        opponent = "Yeetus"
    )
}


