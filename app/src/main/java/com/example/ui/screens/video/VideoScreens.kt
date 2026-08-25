package com.example.ui.screens.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.VideoEntity
import com.example.model.FavoriteType
import com.example.model.SportCategory
import com.example.ui.components.SportSearchBar
import com.example.ui.components.VideoCard
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.SkyBluePrimary
import com.example.viewmodel.SportViewModel

@Composable
fun VideoListScreen(
    viewModel: SportViewModel,
    modifier: Modifier = Modifier
) {
    val allVideos by viewModel.allVideos.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val userFavorites by viewModel.getUserFavorites()?.collectAsState(initial = emptyList()) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList()) }

    val filteredVideos = allVideos.filter { video ->
        val matchesCategory = selectedCategory == null || video.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                video.title.contains(searchQuery, ignoreCase = true) ||
                video.description.contains(searchQuery, ignoreCase = true) ||
                video.category.displayName.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("video_list_screen")
    ) {
        // Search & Category Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Video Pembelajaran PJOK",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Tonton demonstrasi visual gerakan dan teknik olahraga",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            SportSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                placeholder = "Cari video teknik dasar..."
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.selectCategory(null) },
                        label = { Text("Semua (${allVideos.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SkyBluePrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(SportCategory.entries) { cat ->
                    val catColor = getCategoryColor(cat)
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = {
                            viewModel.selectCategory(if (selectedCategory == cat) null else cat)
                        },
                        label = { Text(cat.displayName) },
                        leadingIcon = {
                            Icon(
                                imageVector = getCategoryIcon(cat),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedCategory == cat) Color.White else catColor
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = catColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Video Items List
        if (filteredVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Video tidak ditemukan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Coba ubah kata kunci pencarian atau kategori.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredVideos, key = { it.id }) { video ->
                    val isFav = userFavorites.any { it.itemType == FavoriteType.VIDEO && it.itemId == video.id }
                    VideoCard(
                        video = video,
                        isFavorite = isFav,
                        onFavoriteToggle = { viewModel.toggleFavorite(FavoriteType.VIDEO, video.id) },
                        onWatchClick = { viewModel.openVideoPlayer(video) }
                    )
                }
            }
        }
    }
}
