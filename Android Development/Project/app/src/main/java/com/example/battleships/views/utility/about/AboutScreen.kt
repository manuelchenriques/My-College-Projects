package com.example.battleships.views.utility.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleships.R
import com.example.battleships.model.AboutInfo
import com.example.battleships.model.Developers
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.ui.theme.TypoBattle
import com.example.battleships.views.composables.BackgroundImage
import com.example.battleships.views.composables.WaitingBox
import kotlinx.coroutines.delay


@Composable
fun AboutScreen(
    onReturn: () -> Unit,
    aboutInfo: AboutInfo?,
    getAbout: () -> Unit
) {

    var info by remember {
        mutableStateOf(aboutInfo)
    }

    LaunchedEffect(Unit) {
        while (info == null) {
            delay(1000)
            getAbout()
            info = AboutActivity.info?.getOrNull()
        }
    }

    BattleshipsTheme{
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("AboutScreen"),
        ) {
            BackgroundImage()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Button(
                    onClick = onReturn,
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
            if (info != null) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Card(
                        border = BorderStroke(4.dp, Color.Black),
                        shape = RectangleShape,
                        backgroundColor = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(
                                alignment = Alignment.End
                            )

                    ) {
                        Column(
                            modifier = Modifier.padding(25.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Developed at:\n ${info?.from}",
                                style = TypoBattle.h3,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                softWrap = true
                            )
                            Text(
                                text = "\nDeveloped by:\n",
                                style = TypoBattle.h3,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary,
                                softWrap = true
                            )
                            info?.developers?.forEach { dev ->
                                Card(
                                    border = BorderStroke(2.dp, Color.Black),
                                    shape = RectangleShape,
                                    backgroundColor = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(alignment = Alignment.End)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(5.dp),
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceEvenly,
                                        ) {
                                            Text(
                                                text = dev.name,
                                                style = TypoBattle.h4,
                                                color = MaterialTheme.colors.primary,
                                                softWrap = true
                                            )
                                            Text(
                                                text = "ID: ${dev.number}",
                                                style = TypoBattle.h4,
                                                color = MaterialTheme.colors.primary,
                                                softWrap = true
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            Image(
                                                painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.contact_black_logo else R.drawable.contact_white_logo),
                                                contentDescription = "go_back_logo",
                                                modifier = Modifier.size(
                                                    20.dp, 20.dp
                                                )
                                            )
                                            Text(
                                                text = ": ${dev.publicMail}",
                                                style = TypoBattle.h4,
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colors.primary,
                                                softWrap = true
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center,
                                        ) {
                                            LinkButton(link = dev.gitPage)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                WaitingBox(text = "Loading...")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview(){
    AboutScreen(
        onReturn = {},
        aboutInfo = AboutInfo(
            developers = arrayListOf(
                Developers("Miguel Neves",47230,"A47230@alunos.isel.pt","https://github.com/yeetus-deletus-neves"),
                Developers("Pedro Batista",47246,"A47246@alunos.isel.pt","https://github.com/bigskydiver"),
                Developers("Manuel Henriques",47202,"A47202@alunos.isel.pt","https://github.com/manuelchenriques")
            ),
            from = "Around Lisbon",
            course = "Life's Course of Coding",
            professor = "Failed Build and RunTime Errors"
        ),
        getAbout = { }
    )
}

@Composable
fun LinkButton(link:String){
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse(link)) }

    Button(onClick = { context.startActivity(intent)}, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.github_white_logo else R.drawable.github_black_logo),
            contentDescription = "go_back_logo",
            modifier = Modifier.requiredSize(
                20.dp, 20.dp
            )
        )
    }
}

