package com.example.battleships.views.users.userProfile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.battleships.model.Stat
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.ui.theme.TypoBattle
import com.example.battleships.views.composables.BackgroundImage
import com.example.battleships.views.composables.WaitingBox
import kotlinx.coroutines.delay


@Composable
fun UserProfileScreen(
    onReturn: () -> Unit,
    onSignOut: () -> Unit,
    onRefreshStats: () -> Unit,
    userPref: UserInfoRepository?
) {
    var stat by remember {
        mutableStateOf(userPref?.statInfo)
    }
    var inPage by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        while (inPage) {
            onRefreshStats()
            stat = userPref?.statInfo
            delay(1000)
        }
        onReturn()
    }

    BattleshipsTheme{
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("UserProfileScreen"),
        ) {
            BackgroundImage()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Button(
                    onClick = {
                        inPage = false
                    },
                    modifier = Modifier
                        .testTag("GoBackButton")
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
                Button(
                    onClick = {
                        inPage = false
                        onSignOut()
                    },
                    modifier = Modifier
                        .testTag("SignOutButton")
                        .size(height = 60.dp, width = 60.dp)
                        .padding(1.dp)
                    ) {
                        Image(painter = painterResource(id = if(isSystemInDarkTheme()) R.drawable.sign_out_white else R.drawable.sign_out),
                            contentDescription = "sign_out_logo",
                            modifier = Modifier.size(
                                50.dp, 50.dp
                            )
                        )
                    }
            }
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(150.dp))
                Text(text = "${userPref?.userInfo?.nick}\'s profile",
                    style = TypoBattle.h3,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(150.dp))
                if (stat != null) {
                    Card(
                        border = BorderStroke(4.dp, Color.Black),
                        shape = CircleShape,
                        backgroundColor = Color(0x5CFFFFFF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.End)

                    ) {
                        Column(
                            modifier = Modifier.padding(25.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Text(
                                text = "Played: ${stat?.gamesPlayed.toString()}",
                                style = TypoBattle.h4,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary
                            )
                            Text(
                                text = "Won: ${stat?.gamesWon.toString()}",
                                style = TypoBattle.h4,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary
                            )
                            if (stat != null) {
                                Text(
                                    text = "Win Percentage: ${
                                        div(
                                            stat!!.gamesWon,
                                            stat!!.gamesPlayed
                                        )
                                    }%",
                                    style = TypoBattle.h4,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.primary,
                                )
                            }
                        }
                    }
                }else{
                    WaitingBox(text = "Waiting for scores!")
                }
            }
        }
    }
}

fun div(won:Int,played:Int):String {
    return if (played == 0) "0.00"
    else
        String.format("%.2f",(won.toDouble()/played.toDouble()).times(100))
}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview(){
    UserProfileScreen(
        onReturn = {},
        onSignOut = {},
        onRefreshStats = {},
        userPref = null
    )
}
