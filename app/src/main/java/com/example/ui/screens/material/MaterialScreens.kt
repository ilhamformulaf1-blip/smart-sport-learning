package com.example.ui.screens.material

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MaterialEntity
import com.example.model.FavoriteType
import com.example.model.SportCategory
import com.example.ui.components.MaterialCard
import com.example.ui.components.SportSearchBar
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.CautionOrange
import com.example.ui.theme.CautionOrangeContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.SafetyRed
import com.example.ui.theme.SafetyRedContainer
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SportGreen
import com.example.viewmodel.SportViewModel

@Composable
fun MaterialListScreen(
    viewModel: SportViewModel,
    onMaterialClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val allMaterials by viewModel.allMaterials.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val userFavorites by viewModel.getUserFavorites()?.collectAsState(initial = emptyList()) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList()) }
    val userProgressList by viewModel.getUserProgress()?.collectAsState(initial = emptyList()) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList()) }

    val filteredMaterials = allMaterials.filter { material ->
        val matchesCategory = selectedCategory == null || material.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                material.title.contains(searchQuery, ignoreCase = true) ||
                material.shortDescription.contains(searchQuery, ignoreCase = true) ||
                material.category.displayName.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("material_list_screen")
    ) {
        // Search Bar & Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            Text(
                text = "Materi Pembelajaran PJOK",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Pilih cabang olahraga dan pelajari teknik dasarnya",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            SportSearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.selectCategory(null) },
                        label = { Text("Semua (${allMaterials.size})") },
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

        // Materials List
        if (filteredMaterials.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Materi tidak ditemukan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Coba gunakan kata kunci lain atau ubah kategori filter.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredMaterials, key = { it.id }) { material ->
                    val isFav = userFavorites.any { it.itemType == FavoriteType.MATERIAL && it.itemId == material.id }
                    val prog = userProgressList.find { it.materialId == material.id }?.percentage
                    MaterialCard(
                        material = material,
                        isFavorite = isFav,
                        progressPercentage = prog,
                        onFavoriteToggle = { viewModel.toggleFavorite(FavoriteType.MATERIAL, material.id) },
                        onClick = { onMaterialClick(material.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MaterialDetailScreen(
    materialId: Long,
    viewModel: SportViewModel,
    onBackClick: () -> Unit,
    onStartQuiz: (MaterialEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val allMaterials by viewModel.allMaterials.collectAsState()
    val material = allMaterials.find { it.id == materialId }
    val userFavorites by viewModel.getUserFavorites()?.collectAsState(initial = emptyList()) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList()) }
    val userProgressList by viewModel.getUserProgress()?.collectAsState(initial = emptyList()) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList()) }
    val isFav = userFavorites.any { it.itemType == FavoriteType.MATERIAL && it.itemId == materialId }
    val currentProgress = userProgressList.find { it.materialId == materialId }?.percentage ?: 0

    if (material == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Memuat materi...")
        }
        return
    }

    val categoryColor = getCategoryColor(material.category)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("material_detail_screen")
    ) {
        // App Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = material.category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = categoryColor
                )

                IconButton(onClick = { viewModel.toggleFavorite(FavoriteType.MATERIAL, material.id) }) {
                    Icon(
                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorit",
                        tint = if (isFav) SafetyRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Content Body Scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Hero Title Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    NavyDeep,
                                    categoryColor.copy(alpha = 0.8f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = getCategoryIcon(material.category),
                                contentDescription = null,
                                tint = SkyBlueLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = material.category.displayName.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SkyBlueLight
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = material.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Estimasi Waktu Belajar: ${material.readTimeMinutes} Menit",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Safety Warning Banner (Crucial for Penanganan Cedera)
            if (material.safetyWarning.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SafetyRedContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Peringatan Keselamatan",
                            tint = SafetyRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PERINGATAN KESELAMATAN MEDIS",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                color = SafetyRed
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = material.safetyWarning,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 1. Tujuan Pembelajaran
            DetailSectionCard(
                title = "🎯 Tujuan Pembelajaran",
                content = material.learningObjectives,
                iconColor = SkyBluePrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Isi Materi Pembelajaran
            DetailSectionCard(
                title = "📖 Pembahasan Materi",
                content = material.contentBody,
                iconColor = categoryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Teknik Dasar & Tahapan Gerakan
            DetailSectionCard(
                title = "⚡ Tahapan & Teknik Dasar",
                content = material.basicTechniques,
                iconColor = SportGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Kesalahan Umum yang Sering Dilakukan
            DetailSectionCard(
                title = "⚠️ Kesalahan Umum Siswa",
                content = material.commonMistakes,
                iconColor = CautionOrange
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Tips Pembelajaran
            DetailSectionCard(
                title = "💡 Tips Pelaksanaan",
                content = material.tips,
                iconColor = GoldAccent
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 6. Latihan Mandiri
            DetailSectionCard(
                title = "🏃 Latihan Mandiri / Kelompok",
                content = material.practiceExercises,
                iconColor = SkyBluePrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Completion and Quiz CTA Action Bar
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Evaluasi & Penguasaan Materi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Uji pemahamanmu dengan kuis interaktif (nilai kelulusan >= 75)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.markMaterialCompleted(material) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (currentProgress >= 100) SportGreen else SkyBluePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (currentProgress >= 100) "Sudah Selesai" else "Tandai Selesai")
                        }

                        Button(
                            onClick = { onStartQuiz(material) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                            modifier = Modifier.weight(1f).testTag("start_quiz_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mulai Kuis", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DetailSectionCard(
    title: String,
    content: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                lineHeight = 22.sp
            )
        }
    }
}
