package com.example.littlelemonfood.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.littlelemonfood.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.font.FontWeight
import com.example.littlelemonfood.AppDatabase
import com.example.littlelemonfood.MenuItemRoom
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController, database: AppDatabase) {
    val databaseMenuItems by database.menuItemDao().getAll().observeAsState(emptyList())
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HeaderHome(navController)

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .background(color = Color(73, 94, 87))
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(placeable.width, placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                            .weight(0.6f)
                    ) {
                        Text(
                            "Little Lemon",
                            color = Color(244, 206, 20),
                            fontSize = 44.sp,
                        )
                        Text(
                            "Chicago",
                            color = Color(237, 239, 238),
                            fontSize = 28.sp,
                        )
                        Text(
                            "We are a family owned Mediterranean restaurant, focused on traditional recipes served with a modern twist.",
                            color = Color(237, 239, 238),
                            fontSize = 19.sp,
                            modifier = Modifier.padding(top = 18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.hero_image),
                        contentDescription = "Hero image",
                        modifier = Modifier
                            .size(100.dp)
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(placeable.width, placeable.height) {
                                    placeable.placeRelative(0, 0)
                                }
                            }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(color = Color.White)
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "ORDER FOR DELIVERY!",
            color = Color(51, 51, 51),
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = listOf("starters", "mains", "desserts", "drinks", "All"),
                itemContent = { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDEFEE))
                    ){
                        Text(
                            text = category,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    }
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            val filteredMenuItems = databaseMenuItems.filter { menuItem ->
                (selectedCategory == "All" || menuItem.category == selectedCategory) &&
                        (searchQuery.isBlank() || menuItem.title.contains(searchQuery, ignoreCase = true))
            }
            items(filteredMenuItems) { menuItem ->
                MenuItemCard(menuItem)
            }
        }
    }
}

@Composable
fun HeaderHome(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp, 75.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .clickable {
                    navController.navigate(Destinations.Profile)
                }
        )
    }
}

@Composable
fun MenuItems(database: AppDatabase) {
    val menuItems by database.menuItemDao().getAll().observeAsState(emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
    ) {
        items(menuItems) { menuItem ->
            MenuItemCard(menuItem)
        }
    }
}

@Composable
fun MenuItemCard(menuItem: MenuItemRoom) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = menuItem.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = menuItem.description,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = "$${menuItem.price}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = menuItem.category,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Little Lemon Logo",
                modifier = Modifier.size(150.dp, 75.dp)
            )
        }
    }
}