package com.example.littlelemonfood.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController, userDataViewModel: UserDataViewModel) {
    val firstName by userDataViewModel.firstName.collectAsState()
    val lastName by userDataViewModel.lastName.collectAsState()
    val email by userDataViewModel.email.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Header()
            Spacer(modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier.height(16.dp))
            UserInputFields(
                firstName = firstName,
                lastName = lastName,
                email = email,
                onFirstNameChange = { userDataViewModel.updateFirstName(it) },
                onLastNameChange = { userDataViewModel.updateLastName(it) },
                onEmailChange = { userDataViewModel.updateEmail(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(
            onClick = {
                userDataViewModel.clearUserData()
                navController.navigate(Destinations.Onboarding)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14))
        ) {
            Text(
                text = "Log Out",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }
    }
}


