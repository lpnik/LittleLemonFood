package com.example.littlelemonfood.composables

import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.littlelemonfood.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController, userDataViewModel: UserDataViewModel) {
    val firstName by userDataViewModel.firstName.collectAsState()
    val lastName by userDataViewModel.lastName.collectAsState()
    val email by userDataViewModel.email.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header()
        Spacer(modifier = Modifier
            .height(20.dp))
        Box(
            modifier = Modifier
                .background(color = "#495E57".color)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Text("Let's get to know you",
                color = "#EDEFEE".color,
                fontSize = 28.sp,
                modifier = Modifier
                    .padding(26.dp)
            )

        }
        Spacer(modifier = Modifier
            .height(16.dp))
        UserInputFields(
            firstName = firstName,
            lastName = lastName,
            email = email,
            onFirstNameChange = { userDataViewModel.updateFirstName(it) },
            onLastNameChange = { userDataViewModel.updateLastName(it) },
            onEmailChange = { userDataViewModel.updateEmail(it) }
        )
        Spacer(modifier = Modifier
            .height(16.dp))
        Button(
            onClick = { navController.navigate(Destinations.Home) },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14))
        ) {
            Text(
                text = "Register",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }
    }
}

val String.color
    get() = Color(parseColor(this))

@Composable
fun UserInputFields(
    firstName: String,
    lastName: String,
    email: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Personal information",
            fontSize = 22.sp,
            modifier = Modifier
                .padding(16.dp)
        )
        Text("First Name",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(6.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.White)
                .border(1.dp, Color.Black)
        ) {
            BasicTextField(
                value = firstName,
                onValueChange = { onFirstNameChange(it) },
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .padding(8.dp)
            )
        }

        Text("Last Name",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(6.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.White)
                .border(1.dp, Color.Black)
        ) {
            BasicTextField(
                value = lastName,
                onValueChange = { onLastNameChange(it) },
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        Text("Email",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(6.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.White)
                .border(1.dp, Color.Black)
        ) {
            BasicTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .wrapContentHeight()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
        )
    }
}

class UserDataViewModel : ViewModel() {
    private var _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName

    private var _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName

    private var _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    fun updateFirstName(data: String) {
        viewModelScope.launch {
            _firstName.emit(data)
        }
    }

    fun updateLastName(data: String) {
        viewModelScope.launch {
            _lastName.emit(data)
        }
    }

    fun updateEmail(data: String) {
        viewModelScope.launch {
            _email.emit(data)
        }
    }

    fun clearUserData() {
        viewModelScope.launch {
            _firstName.emit("")
            _lastName.emit("")
            _email.emit("")
        }
    }
}
