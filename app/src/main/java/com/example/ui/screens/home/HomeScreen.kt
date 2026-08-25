package com.example.ui.screens.home

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.MaterialEntity
import com.example.data.local.UserEntity
import com.example.model.FavoriteType
import com.example.model.SportCategory
import com.example.model.UserRole
import com.example.ui.components.MaterialCard
import com.example.ui.components.SportCategoryCard
import com.example.ui.components.SportSearchBar
import com.example.ui.components.StatCard
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavySurface
import com.example.ui.theme.SkyBlueDark
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SportGreen
import com.example.viewmodel.SportViewModel

@Composable
fun HomeScreen(
    viewModel: SportViewModel,
    onNavigateToMaterials: () -> Unit,
    onNavigateToMaterialDetail: (Long) -> Unit,
    onNavigateToVideos: () -> Unit,
    onNavigateToQuiz: (Long?) -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToBadges: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val popularMaterials by viewModel.popularMaterials.collectAsState()
    val totalMaterials by viewModel.totalMaterialsCount.collectAsState()
    val totalVideos by viewModel.totalVideosCount.collectAsState()
    val totalQuizzes by viewModel.totalQuizzesCount.collectAsState()
    val userProgressList by viewModel.getUserProgress()?.collectAsState(initial = emptyList()) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList()) }
    val userFavorites by viewModel.getUserFavorites()?.collectAsState(initial = emptyList()) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList()) }
    val latestQuizResult by viewModel.getLatestQuizResult()?.collectAsState(initial = null) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(null) }
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Calculate student overall percentage
    val completedCount = userProgressList.count { it.percentage >= 100 }
    val overallPercentage = if (totalMaterials > 0) ((completedCount.toFloat() / totalMaterials.toFloat()) * 100).toInt() else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Hero Banner
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("hero_banner")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    NavyDeep,
                                    Color(0xFF075985),
                                    Color(0xFF0F172A)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Badge Tag
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = SkyBluePrimary.copy(alpha = 0.25f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = SkyBlueLight,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Platform Pembelajaran PJOK Digital",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = SkyBlueLight
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "SMART SPORT LEARNING",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "“Belajar Olahraga Lebih Mudah, Interaktif, dan Menyenangkan”",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onNavigateToMaterials,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                                modifier = Modifier.testTag("mulai_belajar_btn")
                            ) {
                                Text(
                                    text = "Mulai Belajar",
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            FilledTonalButton(
                                onClick = { onNavigateToQuiz(null) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.15f),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Latihan Kuis", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // 2. Student Dashboard Card (If Logged in as Siswa)
        if (currentUser != null && currentUser?.role == UserRole.SISWA) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("student_welcome_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(SkyBluePrimary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "Foto Siswa",
                                        tint = SkyBluePrimary,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Selamat Datang,",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = currentUser?.name ?: "Siswa",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = currentUser?.kelas ?: "Kelas X",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SkyBluePrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            // Circular Progress Indicator
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(54.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = { overallPercentage / 100f },
                                    color = SportGreen,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    strokeWidth = 5.dp,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Text(
                                    text = "$overallPercentage%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick info row: Latest Quiz score & Total Points
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Total Poin Belajar",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${currentUser?.totalScore ?: 0} Pts",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = SkyBluePrimary
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Nilai Kuis Terakhir",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (latestQuizResult != null) "${latestQuizResult?.score}/100" else "Belum Ada",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = if (latestQuizResult?.isPassed == true) SportGreen else GoldAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Quick Action Buttons Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Menu Pembelajaran",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionButton(
                        title = "Materi",
                        subtitle = "5 Kategori",
                        icon = Icons.Default.MenuBook,
                        color = SkyBluePrimary,
                        onClick = onNavigateToMaterials,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Video",
                        subtitle = "Tutorial",
                        icon = Icons.Default.VideoLibrary,
                        color = Color(0xFFEA580C),
                        onClick = onNavigateToVideos,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Kuis",
                        subtitle = "Interaktif",
                        icon = Icons.Default.Quiz,
                        color = Color(0xFF8B5CF6),
                        onClick = { onNavigateToQuiz(null) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        title = "Progress",
                        subtitle = "Tracking",
                        icon = Icons.Default.CheckCircle,
                        color = SportGreen,
                        onClick = onNavigateToProgress,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. Learning Statistics Grid (Materi, Video, Soal, Progress)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Statistik Pembelajaran",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        count = totalMaterials.toString(),
                        title = "Materi PJOK",
                        icon = Icons.Default.MenuBook,
                        color = SkyBluePrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = totalVideos.toString(),
                        title = "Video HD",
                        icon = Icons.Default.VideoLibrary,
                        color = Color(0xFFEA580C),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        count = totalQuizzes.toString(),
                        title = "Bank Soal",
                        icon = Icons.Default.Quiz,
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        count = "$overallPercentage%",
                        title = "Progress Siswa",
                        icon = Icons.Default.LocalFireDepartment,
                        color = SportGreen,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 5. Sport Categories Carousel
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cabang Olahraga & Materi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = onNavigateToMaterials) {
                        Text("Lihat Semua", color = SkyBluePrimary, fontWeight = FontWeight.SemiBold)
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(SportCategory.entries) { cat ->
                        SportCategoryCard(
                            category = cat,
                            isSelected = false,
                            onClick = {
                                viewModel.selectCategory(cat)
                                onNavigateToMaterials()
                            },
                            modifier = Modifier.width(115.dp)
                        )
                    }
                }
            }
        }

        // 6. Popular Materials Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Materi Populer",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    TextButton(onClick = onNavigateToMaterials) {
                        Text("Semua Materi", color = SkyBluePrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        items(popularMaterials.take(5)) { material ->
            val isFav = userFavorites.any { it.itemType == FavoriteType.MATERIAL && it.itemId == material.id }
            val prog = userProgressList.find { it.materialId == material.id }?.percentage
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                MaterialCard(
                    material = material,
                    isFavorite = isFav,
                    progressPercentage = prog,
                    onFavoriteToggle = { viewModel.toggleFavorite(FavoriteType.MATERIAL, material.id) },
                    onClick = { onNavigateToMaterialDetail(material.id) }
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}
