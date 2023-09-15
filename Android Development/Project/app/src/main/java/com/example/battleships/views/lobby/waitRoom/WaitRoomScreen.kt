package com.example.battleships.views.lobby.waitRoom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleships.R
import com.example.battleships.model.GameImage
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.ui.theme.TypoBattle
import com.example.battleships.views.composables.BackgroundImage
import com.example.battleships.views.composables.WaitingBox
import kotlinx.coroutines.delay

@Composable
fun WaitRoomScreen(
    userPref:UserInfoRepository?,
    joined: Boolean,
    proceedToSetUp: () -> Unit,
    waitForOpponent: () -> Unit,
    game: GameImage?,
    quitQueue: () -> Unit,
    getOpponent: () -> Unit
) {

    var inPage by remember {
        mutableStateOf(true)
    }

    var gameImage by remember {
        mutableStateOf(game)
    }

    var hasOpponentName by remember {
        mutableStateOf(userPref?.gameInfo?.opponent)
    }

    LaunchedEffect(Unit){
        if (joined){
            while (hasOpponentName==null && inPage){
                getOpponent()
                hasOpponentName = userPref?.gameInfo?.opponent
                delay(500)
            }
        }else{
            while((gameImage!!.gameState == "WP" ||  hasOpponentName == null) && inPage) {
                waitForOpponent()
                gameImage = WaitRoomActivity.game
                hasOpponentName = userPref?.gameInfo?.opponent
                delay(1000)
            }
        }
        waitForSetUp(inPage, quitQueue, proceedToSetUp)
    }


    BattleshipsTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("WaitRoomScreen"),
        ){
            BackgroundImage()
        }
        Column(
            verticalArrangement = Arrangement.Top,
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
                        inPage = false
                    },
                    enabled = hasOpponentName == null,
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
            }
            Spacer(modifier = Modifier.height(150.dp))
            if (!joined && hasOpponentName == null){
                WaitingBox(text = "Waiting for other player to join...")
            }
            else if (hasOpponentName != null){
                Card(
                    border = BorderStroke(4.dp, Color.Black),
                    shape = RoundedCornerShape(10),
                    backgroundColor = Color(0x5CFFFFFF),
                    modifier = Modifier.requiredSize(300.dp,150.dp)

                ){
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${userPref?.userInfo?.nick}\nvs\n$hasOpponentName!",
                            style = TypoBattle.h3,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun WaitRoomPreviewScreen(){
    WaitRoomScreen(
        userPref = null,
        joined = false,
        proceedToSetUp = {  },
        waitForOpponent = {  },
        game = null,
        quitQueue = {},
        getOpponent = {}
    )
}

suspend fun waitForSetUp(inPage: Boolean, onQuit: ()->Unit, proceedToSetUp: () -> Unit){
    if (inPage){
        for (i  in 0 until 5) {
            delay(1000)
        }
        proceedToSetUp()
    }else{
        onQuit()
    }
}

