package com.example.battleships.views.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleships.R
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.ui.theme.TypoBattle
import com.example.battleships.views.composables.BackgroundImage

@Composable
fun MainScreen(
    onGetLeaderboard: () -> Unit,
    onGetAbout: () -> Unit,
    onSigIn: () -> Unit,
    onPlayGames: () -> Unit,
    isUserLogged: Boolean,
    username: String?
){
    BattleshipsTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("MainScreen"),
        ) {//Header for about and sign in
            BackgroundImage()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Button(
                    onClick = onGetAbout,
                    modifier = Modifier
                        .testTag("AboutButton")
                        .size(height = 60.dp, width = 60.dp)
                        .padding(1.dp)
                ) {
                    Image(painter = painterResource(id = if(isSystemInDarkTheme())R.drawable.link_white_logo else R.drawable.link_logo),
                        contentDescription = "about_logo",
                        modifier = Modifier.size(
                            50.dp, 50.dp
                        )
                    )
                }
                Button(
                    onClick = onSigIn,
                    modifier = Modifier
                        .testTag("SignInButton")
                        .size(height = 60.dp, width = 60.dp)
                        .padding(1.dp)
                ) {
                    Image(painter = painterResource(id = if(isSystemInDarkTheme())R.drawable.signin_logo else R.drawable.signin_white_logo),
                        contentDescription = "signin_logo",
                        modifier = Modifier.size(
                            50.dp, 50.dp
                        )
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = TypoBattle.h1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary
                )
                Image(
                    painter = painterResource(id = if(isSystemInDarkTheme())R.drawable.battleships_logo else R.drawable.battleships_white_logo),
                    contentDescription = "app_logo",
                    modifier = Modifier.size(
                        200.dp, 200.dp
                    )
                )
                if (username != null)
                    Text(
                        text = "Welcome, $username!",
                        style = TypoBattle.h3,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        softWrap = true)
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val height = 50.dp
                    val width = 140.dp
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ){
                        Button(
                            onClick = onPlayGames,
                            modifier = Modifier
                                .testTag("PlayButton")
                                .size(height = height, width = width)
                                .padding(1.dp),
                            enabled = isUserLogged
                        ) {
                            Text(
                                text = stringResource(id = R.string.play_games_button_text)
                            )
                        }
                        Button(
                            onClick = onGetLeaderboard,
                            modifier = Modifier
                                .testTag("LeaderboardButton")
                                .size(height = height, width = width)
                                .padding(1.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.leaderboard_button_text)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onGetLeaderboard = {},
        onGetAbout = {},
        onSigIn = {},
        onPlayGames = {},
        isUserLogged = true,
        username = "Yeetus_Deletus"
    )
}