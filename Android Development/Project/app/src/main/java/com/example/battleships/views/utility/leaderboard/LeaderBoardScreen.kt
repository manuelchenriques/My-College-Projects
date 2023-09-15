package com.example.battleships.views.utility.leaderboard

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleships.R
import com.example.battleships.mock.mockRanking
import com.example.battleships.model.Stat
import com.example.battleships.services.UtilityServices
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.ui.theme.TypoBattle
import com.example.battleships.views.composables.BackgroundImage
import com.example.battleships.views.composables.RankingList
import com.example.battleships.views.composables.WaitingBox
import kotlinx.coroutines.delay


@Composable
fun LeaderBoardScreen(
    onReturn: () -> Unit,
    leaderBoard: List<Stat>?,
    refreshLeaderBoard: (UtilityServices.OrderBy, Int, Int, String?) -> Unit,
    next: Boolean,
    previous: Boolean
) {
    var username by remember { mutableStateOf("") }
    var pageIdx by remember { mutableStateOf(0) }
    var orderBy by remember { mutableStateOf(UtilityServices.OrderBy.Wins) }
    var hasNext by remember { mutableStateOf(next) }
    var hasPrevious by remember { mutableStateOf(previous) }

    var inPage by remember {
        mutableStateOf(true)
    }
    var ranking by remember {
        mutableStateOf(leaderBoard)
    }

    LaunchedEffect(Unit) {
        refreshLeaderBoard(UtilityServices.OrderBy.Wins,10,0,null)
        while (inPage) {
            delay(500)
            hasNext = LeaderboardActivity.hasNext
            hasPrevious = LeaderboardActivity.hasPrevious
            ranking = LeaderboardActivity.ranks
        }
        onReturn()
    }

    BattleshipsTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("LeaderBoardScreen"),
        ) {
            BackgroundImage()
            Column(modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Start,
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
                }
                Spacer(modifier = Modifier.requiredHeight(60.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .requiredHeight(60.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = { Text("Username...") },
                        singleLine = true,

                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        modifier = Modifier
                            .background(Color(0x51FFFFFF))
                            .requiredHeight(60.dp)
                    )
                    Button(onClick = {
                        refreshLeaderBoard(orderBy,10,pageIdx,username)
                        ranking = LeaderboardActivity.ranks
                        hasNext = LeaderboardActivity.hasNext
                        hasPrevious = LeaderboardActivity.hasPrevious
                        pageIdx = 0
                        },
                        modifier = Modifier.requiredHeight(60.dp)
                    ) {
                        Text(text = "search", modifier = Modifier.requiredHeight(20.dp))
                    }
                }
                Spacer(modifier = Modifier.requiredHeight(100.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Card(
                        border = BorderStroke(4.dp, Color.Black),
                        shape = RoundedCornerShape(10),
                        backgroundColor = Color(0x5CFFFFFF),
                        modifier = Modifier
                            .requiredHeight(40.dp)
                            .fillMaxWidth()
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "By Games Played",
                                style = TypoBattle.h4,
                                color = MaterialTheme.colors.primary,
                                softWrap = true
                            )
                            Switch(
                                checked = orderBy == UtilityServices.OrderBy.Wins,
                                onCheckedChange = {
                                    orderBy = if (it) {
                                        UtilityServices.OrderBy.Wins
                                    } else {
                                        UtilityServices.OrderBy.GamesPlayed
                                    }
                                    Log.e("Checked",orderBy.name)
                                })
                            Text(
                                text = "By Wins",
                                style = TypoBattle.h4,
                                color = MaterialTheme.colors.primary,
                                softWrap = true
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.requiredHeight(20.dp))
                if (ranking != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RankingList(ranking = ranking!!)
                    }
                    Spacer(modifier = Modifier.requiredHeight(60.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = {
                            refreshLeaderBoard(orderBy,10,pageIdx-1,username)
                            pageIdx -=1
                            ranking = LeaderboardActivity.ranks
                            hasNext = LeaderboardActivity.hasNext
                            hasPrevious = LeaderboardActivity.hasPrevious
                        },
                            modifier = Modifier.requiredHeight(40.dp),
                            enabled = hasPrevious
                        ) {
                            Text(text = "<")
                        }
                        Card(
                            border = BorderStroke(4.dp, Color.Black),
                            shape = RoundedCornerShape(10),
                            backgroundColor = Color(0x5CFFFFFF),
                            modifier = Modifier
                                .requiredHeight(40.dp)
                                .requiredWidth(100.dp)
                                .align(Alignment.CenterVertically)
                        ){
                            Text(text ="Page ${pageIdx+1}", textAlign = TextAlign.Center)
                        }

                        Button(onClick = {
                            refreshLeaderBoard(orderBy,10,pageIdx+1,username)
                            pageIdx +=1
                            ranking = LeaderboardActivity.ranks
                            hasNext = LeaderboardActivity.hasNext
                            hasPrevious = LeaderboardActivity.hasPrevious
                        },
                            modifier = Modifier.requiredHeight(40.dp),
                            enabled = hasNext
                        ) {
                            Text(text = ">")
                        }
                    }
                } else {
                    WaitingBox(text = "Waiting for ranking!")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderBoardPreview() {
    LeaderBoardScreen(
        onReturn = {},
        leaderBoard = mockRanking,
        refreshLeaderBoard = {p1, p2 ,p3,p4 -> println(p1.toString() + p2 + p3 + p4)},
        next = false,
        previous = false
    )
}


