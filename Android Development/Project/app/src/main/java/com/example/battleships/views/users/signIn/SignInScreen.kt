package com.example.battleships.views.users.signIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleships.model.AuthResponses
import com.example.battleships.preferences.UserInfoRepository
import com.example.battleships.ui.theme.BattleshipsTheme
import com.example.battleships.views.composables.BackgroundImage
import com.example.battleships.views.composables.WaitingBox
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    onRegister: (String, String) -> Unit,
    onReturn: () -> Unit,
    onSignIn: (String, String) -> Unit,
    userPref: UserInfoRepository?
) {
    var userState by remember {
        mutableStateOf<String?>(null)
    }
    var inPage by remember {
        mutableStateOf(true)
    }
    var isSubmitting by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        while (inPage) {
            delay(1000)
            val auth = SignInActivity.authRes

            userState = if(auth != null){
                isSubmitting = false
                when(auth){
                    AuthResponses.SignInFailed, AuthResponses.RegisterFailed -> "Authentication failed"
                    AuthResponses.BadCredentials -> "Invalid Credentials"
                    AuthResponses.RegisterSuccessful -> "User created successfully"
                    else -> null
                }
            } else null

            if (userPref!!.userInfo != null)
                inPage = false
        }
        onReturn()
    }

    BattleshipsTheme{
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("SignInScreen"),
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
                    .testTag("AboutButton")
                    .size(height = 60.dp, width = 60.dp)
                    .padding(1.dp)
            ) {
                Image(
                    painter = painterResource(id = if (isSystemInDarkTheme()) com.example.battleships.R.drawable.back_arrow_white else com.example.battleships.R.drawable.back_arrow),
                    contentDescription = "about_logo",
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
                if (isSubmitting) {
                    WaitingBox(text = "Loading...")
                } else {
                    Image(
                        painter = painterResource(id = if (isSystemInDarkTheme()) com.example.battleships.R.drawable.signin_logo else com.example.battleships.R.drawable.signin_white_logo),
                        contentDescription = "app_logo",
                        modifier = Modifier.size(
                            200.dp, 200.dp
                        )
                    )
                    var username by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("Username...") },
                            singleLine = true,

                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                            modifier = Modifier.background(Color(0x51FFFFFF))
                        )
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Password...") },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            trailingIcon = {
                                val image = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff
                                val description =
                                    if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, description)
                                }
                            },
                            modifier = Modifier.background(Color(0x51FFFFFF))
                        )
                        if (userState != null) {
                            Text(text = userState!!)
                        }


                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Button(
                            onClick = {
                                onSignIn(username, password)
                            },
                            modifier = Modifier
                                .testTag("SignInButton")
                                .size(height = 50.dp, width = 130.dp)
                                .padding(1.dp)
                        ) {
                            Text(
                                text = stringResource(id = com.example.battleships.R.string.sign_in_button_text)
                            )
                        }
                        Button(
                            onClick = {
                                isSubmitting = true
                                onRegister(username, password)
                            },
                            modifier = Modifier
                                .testTag("RegisterButton")
                                .size(height = 50.dp, width = 130.dp)
                                .padding(1.dp)
                        ) {
                            Text(
                                text = stringResource(id = com.example.battleships.R.string.register_button_text)
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
fun SignInPreview(){
    SignInScreen(
        onRegister = { username, password -> println(username + password) },
        onReturn = {},
        onSignIn = { username, password -> println(username + password) },
        userPref = null
    )
}




