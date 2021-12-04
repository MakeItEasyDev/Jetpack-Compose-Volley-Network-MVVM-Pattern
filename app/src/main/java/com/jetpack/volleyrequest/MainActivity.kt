package com.jetpack.volleyrequest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.volleyrequest.model.UserResponse
import com.jetpack.volleyrequest.repository.UserRepository
import com.jetpack.volleyrequest.ui.theme.VolleyRequestTheme
import com.jetpack.volleyrequest.ui.theme.red
import com.jetpack.volleyrequest.ui.theme.yellow
import com.jetpack.volleyrequest.utils.Resource
import com.jetpack.volleyrequest.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolleyRequestTheme {
                Surface(color = MaterialTheme.colors.background) {
                    VolleyRequestApi()
                }
            }
        }
    }
}

@Composable
fun VolleyRequestApi() {
    val viewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository = UserRepository())
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val getAllUserData = viewModel.getUserData.observeAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    yellow,
                                    red
                                )
                            )
                        )
                        .padding(15.dp)
                ) {
                    Text(
                        text = "User Live Data",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                scope.launch {
                    val result = viewModel.getUserData(context)

                    if (result is Resource.Success) {
                        Toast.makeText(context, "Fetching data Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }

                if (!viewModel.isLoading.value) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }

                if (viewModel.isLoading.value) {
                    if (viewModel.getUserData.value!!.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)
                        ) {
                            items(getAllUserData.value!!.size) { index ->
                                UserListItem(getAllUserData.value!![index], index)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(userResponse: UserResponse, index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clip(
                if (index % 2 == 1) {
                    RoundedCornerShape(topStart = 20.dp, bottomEnd = 20.dp)
                } else {
                    RoundedCornerShape(topEnd = 20.dp, bottomStart = 20.dp)
                }
            ),
        elevation = 10.dp,
        backgroundColor = Color.Transparent.copy(alpha = 0.5f)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            yellow,
                            red
                        )
                    )
                )
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = userResponse.id.toString(),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )

                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = "Title: ${userResponse.title}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )

                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = "Status: ${userResponse.completed}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    color = Color.White
                )
            }
        }
    }
}

class UserViewModelFactory(
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}















