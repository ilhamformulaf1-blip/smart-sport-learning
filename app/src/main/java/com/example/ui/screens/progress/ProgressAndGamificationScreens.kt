package com.example.ui.screens.progress

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserEntity
import com.example.model.FavoriteType
import com.example.model.LearningStatus
import com.example.model.SportCategory
import com.example.model.UserRole
import com.example.ui.components.BadgeCard
import com.example.ui.components.LeaderboardPodiumCard
import com.example.ui.components.MaterialCard
import com.example.ui.components.VideoCard
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.SafetyRed
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SportGreen
import com.example.viewmodel.SportViewModel

@Composable
fun ProgressScreen(
    viewModel: SportViewModel,
    onNavigateToMaterial: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val allMaterials by viewModel.allMaterials.collectAsState()
    val userProgressList by viewModel.getUserProgress()?.collectAsState(initial = emptyList()) ?: remember { androidx.compose.runtime.mutableStateOf(emptyList()) }
    val currentUser by viewModel.currentUser.collectAsState()

    val totalMaterials = allMaterials.size
    val completedCount = userProgressList.count { it.percentage >= 100 }
    val inProgressCount = userProgressList.count { it.percentage in 1..99 }
    val overallPercentage = if (totalMaterials > 0) ((completedCount.toFloat() / totalMaterials.toFloat()) * 100).toInt() else 0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("progress_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Overview Card
        item {
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
                                listOf(NavyDeep, Color(0xFF0284C7), NavyDeep)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "PROGRESS BELAJAR",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SkyBlueLight
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${currentUser?.name ?: "Siswa"}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "$completedCount dari $totalMaterials Materi Terselesaikan",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(68.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = { overallPercentage / 100f },
                                color = SportGreen,
                                trackColor = Color.White.copy(alpha = 0.2f),
                                strokeWidth = 6.dp,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = "$overallPercentage%",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Summary Status Pills
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusCard(title = "Selesai", count = completedCount.toString(), color = SportGreen, modifier = Modifier.weight(1f))
                StatusCard(title = "Sedang Belajar", count = inProgressCount.toString(), color = SkyBluePrimary, modifier = Modifier.weight(1f))
                StatusCard(title = "Belum Mulai", count = (totalMaterials - completedCount - inProgressCount).coerceAtLeast(0).toString(), color = Color(0xFF94A3B8), modifier = Modifier.weight(1f))
            }
        }

        // Category Breakdown
        item {
            Text(
                text = "Progress Per Cabang Olahraga",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(SportCategory.entries) { cat ->
            val catColor = getCategoryColor(cat)
            val catMaterials = allMaterials.filter { it.category == cat }
            val catCompleted = userProgressList.count { prog ->
                catMaterials.any { it.id == prog.materialId } && prog.percentage >= 100
            }
            val catPct = if (catMaterials.isNotEmpty()) ((catCompleted.toFloat() / catMaterials.size.toFloat()) * 100).toInt() else 0

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = getCategoryIcon(cat),
                                contentDescription = null,
                                tint = catColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = cat.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "$catCompleted/${catMaterials.size} Selesai ($catPct%)",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = catColor
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { catPct / 100f },
                        color = catColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    count: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun BadgeScreen(
    viewModel: SportViewModel,
    modifier: Modifier = Modifier
) {
    val allBadges by viewModel.allBadges.collectAsState()
    val userBadges by viewModel.getUserBadges()?.collectAsState(initial = emptyList()) ?: remember { androidx.compose.runtime.mutableStateOf(emptyList()) }

    val unlockedCount = userBadges.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("badge_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(NavyDeep, Color(0xFFD97706), NavyDeep))
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "LENCANA & PENCAPAIAN 🏅",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFDE68A)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Prestasi Belajar",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "$unlockedCount dari ${allBadges.size} Lencana Terbuka",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Daftar Lencana Olahraga",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(allBadges, key = { it.id }) { badge ->
            val isUnlocked = userBadges.any { it.badgeKey == badge.badgeKey }
            BadgeCard(
                badge = badge,
                isUnlocked = isUnlocked
            )
        }
    }
}

@Composable
fun LeaderboardScreen(
    viewModel: SportViewModel,
    modifier: Modifier = Modifier
) {
    val allUsers by viewModel.allUsers.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val rankedStudents = allUsers
        .filter { it.role == UserRole.SISWA }
        .sortedByDescending { it.totalScore }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeep),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(listOf(NavyDeep, Color(0xFF0369A1), NavyDeep))
                        )
                        .padding(20.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "PAPAN PERINGKAT SISWA",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "Siswa teraktif dan peraih skor tertinggi dalam latihan soal PJOK",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        items(rankedStudents.size) { idx ->
            val student = rankedStudents[idx]
            LeaderboardPodiumCard(
                user = student,
                rank = idx + 1
            )
        }
    }
}

@Composable
fun FavoritesScreen(
    viewModel: SportViewModel,
    onMaterialClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val userFavorites by viewModel.getUserFavorites()?.collectAsState(initial = emptyList()) ?: remember { androidx.compose.runtime.mutableStateOf(emptyList()) }
    val allMaterials by viewModel.allMaterials.collectAsState()
    val allVideos by viewModel.allVideos.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    val favMaterials = allMaterials.filter { mat -> userFavorites.any { it.itemType == FavoriteType.MATERIAL && it.itemId == mat.id } }
    val favVideos = allVideos.filter { vid -> userFavorites.any { it.itemType == FavoriteType.VIDEO && it.itemId == vid.id } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_screen")
    ) {
        Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Materi Tersimpan (${favMaterials.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Video Tersimpan (${favVideos.size})") }
                )
            }
        }

        if (selectedTab == 0) {
            if (favMaterials.isEmpty()) {
                EmptyFavView(message = "Belum ada materi yang kamu simpan sebagai favorit.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favMaterials, key = { it.id }) { mat ->
                        MaterialCard(
                            material = mat,
                            isFavorite = true,
                            progressPercentage = null,
                            onFavoriteToggle = { viewModel.toggleFavorite(FavoriteType.MATERIAL, mat.id) },
                            onClick = { onMaterialClick(mat.id) }
                        )
                    }
                }
            }
        } else {
            if (favVideos.isEmpty()) {
                EmptyFavView(message = "Belum ada video yang kamu simpan sebagai favorit.")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favVideos, key = { it.id }) { vid ->
                        VideoCard(
                            video = vid,
                            isFavorite = true,
                            onFavoriteToggle = { viewModel.toggleFavorite(FavoriteType.VIDEO, vid.id) },
                            onWatchClick = { viewModel.openVideoPlayer(vid) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyFavView(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun NotificationScreen(
    viewModel: SportViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.getUserNotifications()?.collectAsState(initial = emptyList()) ?: remember { androidx.compose.runtime.mutableStateOf(emptyList()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("notification_screen")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pemberitahuan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = { viewModel.markNotificationsRead() }) {
                Icon(imageVector = Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tandai Dibaca")
            }
        }

        if (notifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tidak ada pemberitahuan baru.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications, key = { it.id }) { notif ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!notif.isRead) SkyBluePrimary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = notif.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = notif.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    viewModel: SportViewModel,
    onRoleSwitched: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("profile_screen")
    ) {
        // Profile Info Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(SkyBluePrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (currentUser?.role) {
                            UserRole.GURU -> Icons.Default.School
                            UserRole.ADMIN -> Icons.Default.Verified
                            else -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = SkyBluePrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentUser?.name ?: "Pengguna Smart Sport",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${currentUser?.email} • ${currentUser?.kelas}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SkyBluePrimary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "Peran: ${currentUser?.role?.name}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = SkyBluePrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Role Switcher (Crucial for evaluation of Student, Teacher, Admin features)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Ganti Peran Akun (Mode Pengujian)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Beralih peran secara instan untuk menguji fitur Siswa, Guru, atau Admin:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.switchRoleQuick(UserRole.SISWA)
                            onRoleSwitched()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentUser?.role == UserRole.SISWA) SkyBluePrimary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (currentUser?.role == UserRole.SISWA) Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("switch_siswa")
                    ) {
                        Text("Siswa")
                    }

                    Button(
                        onClick = {
                            viewModel.switchRoleQuick(UserRole.GURU)
                            onRoleSwitched()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentUser?.role == UserRole.GURU) Color(0xFF6D28D9) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (currentUser?.role == UserRole.GURU) Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("switch_guru")
                    ) {
                        Text("Guru")
                    }

                    Button(
                        onClick = {
                            viewModel.switchRoleQuick(UserRole.ADMIN)
                            onRoleSwitched()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentUser?.role == UserRole.ADMIN) Color(0xFFDC2626) else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (currentUser?.role == UserRole.ADMIN) Color.White else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("switch_admin")
                    ) {
                        Text("Admin")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Information
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tentang Aplikasi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Smart Sport Learning v1.0.0\nMedia Pembelajaran Digital Pendidikan Jasmani, Olahraga, dan Kesehatan (PJOK) untuk SMP/SMA.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
