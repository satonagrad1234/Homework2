package com.example.homework

import Picture
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.homework.ui.theme.GalleryTheme
import createNewPicture
import generateSamplePictures

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GalleryTheme {
                PictureGalleryApp()
            }
        }
    }
}
@Composable
fun PictureGalleryApp() {
    var searchTxt by remember { mutableStateOf("") }

    val gallery = remember { mutableStateListOf<Picture>().apply { addAll(generateSamplePictures()) } }

    var isGridMode by remember { mutableStateOf(false) }

    val filteredGallery = gallery.filter {
        it.author.contains(searchTxt, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                OutlinedTextField(
                    value = searchTxt,
                    onValueChange = { searchTxt = it },
                    label = { Text("Поиск по автору") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Поиск") },
                    trailingIcon = {
                        if (searchTxt.isNotEmpty()) {
                            IconButton(onClick = { searchTxt = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Очистить поиск")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 21.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { isGridMode = !isGridMode }) {
                        val icon = if (isGridMode) Icons.AutoMirrored.Filled.List else Icons.Default.GridView
                        val text = if (isGridMode) "Список" else "Сетка"
                        Icon(icon, contentDescription = text)
                        Spacer(Modifier.width(11.dp))
                        Text(text)
                    }
                    Button(
                        onClick = { gallery.clear() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Очистить всё")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                addNewPicture(gallery)
            },
                Modifier.shadow(5.dp, RectangleShape)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Добавить изображение" )
            }
        }
    ) { paddingValues ->
        if (isGridMode) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
            ) {
                items(filteredGallery, key = { it.id }) { picture ->
                    PictureCard(picture = picture) {
                        // Удаление при нажатии на картинку
                        gallery.remove(picture)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
            ) {
                items(filteredGallery, key = { it.id }) { picture ->
                    PictureCard(picture = picture) {
                        gallery.remove(picture)
                    }
                }
            }
        }
    }
}

fun addNewPicture(gallery: SnapshotStateList<Picture>) {
    val newPicture = createNewPicture()

    val isExists = gallery.any { it.url == newPicture.url || it.id == newPicture.id }

    if (!isExists) {
        gallery.add(newPicture)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureCard(picture: Picture, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GlideImage(
                model = picture.url,
                contentDescription = picture.author,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) //
            )

            Text(
                text = picture.author,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(),
        content = content
    )
}

@Preview(showBackground = true, name = "Галерея - Сетка")
@Composable
fun GridPreview() {
    AppTheme {
        GridModePreview()
    }
}

@Preview(showBackground = true, name = "Галерея - Список")
@Composable
fun ListPreview() {
    AppTheme {
        PictureGalleryApp()
    }
}

@Composable
fun GridModePreview() {
    val gallery = remember { mutableStateListOf<Picture>().apply { addAll(generateSamplePictures()) } }
    var searchText by remember { mutableStateOf("") }

    val isGridMode = true

    val filteredGallery = gallery.filter {
        it.author.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        topBar = {},
        floatingActionButton = {}
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
        ) {
            items(filteredGallery, key = { it.id }) { picture ->
                PictureCard(picture = picture) {
                }
            }
        }
    }
}