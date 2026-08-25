package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.SportsVolleyball
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BadgeEntity
import com.example.data.local.MaterialEntity
import com.example.data.local.UserEntity
import com.example.data.local.VideoEntity
import com.example.model.LearningStatus
import com.example.model.SportCategory
import com.example.model.UserRole
import com.example.ui.theme.BronzeAccent
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldContainer
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavySurface
import com.example.ui.theme.SafetyRed
import com.example.ui.theme.SilverAccent
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SportGreen
import com.example.ui.theme.SportGreenContainer

fun getCategoryIcon(category: SportCategory): ImageVector {
    return when (category) {
        SportCategory.SEPAK_BOLA -> Icons.Default.SportsSoccer
        SportCategory.BOLA_BASKET -> Icons.Default.SportsBasketball
        SportCategory.BOLA_VOLI -> Icons.Default.SportsVolleyball
        SportCategory.ATLETIK -> Icons.Default.FitnessCenter
        SportCategory.PENANGANAN_CEDERA -> Icons.Default.MedicalServices
    }
}

fun getCategoryColor(category: SportCategory): Color {
    return when (category) {
        SportCategory.SEPAK_BOLA -> Color(0xFF0284C7)
        SportCategory.BOLA_BASKET -> Color(0xFFEA580C)
        SportCategory.BOLA_VOLI -> Color(0xFF8B5CF6)
        SportCategory.ATLETIK -> Color(0xFF10B981)
        SportCategory.PENANGANAN_CEDERA -> Color(0xFFEF4444)
    }
}

@Composable
fun AppHeader(
    currentUser: UserEntity?,
    unreadNotifications: Int,
    onNotificationsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onRoleBadgeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(listOf(SkyBluePrimary, Color(0xFF0369A1)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Sports,
                        contentDescription = "Smart Sport Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "SMART SPORT",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = SkyBluePrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "PJOK SMP/SMA",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Quick Role Switch Chip & Icons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (currentUser != null) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = when (currentUser.role) {
                            UserRole.GURU -> Color(0xFFEDE9FE)
                            UserRole.ADMIN -> Color(0xFFFEE2E2)
                            UserRole.SISWA -> Color(0xFFE0F2FE)
                        },
                        modifier = Modifier
                            .clickable { onRoleBadgeClick() }
                            .testTag("role_switch_badge")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = when (currentUser.role) {
                                    UserRole.GURU -> Icons.Default.School
                                    UserRole.ADMIN -> Icons.Default.Verified
                                    UserRole.SISWA -> Icons.Default.Psychology
                                },
                                contentDescription = null,
                                tint = when (currentUser.role) {
                                    UserRole.GURU -> Color(0xFF6D28D9)
                                    UserRole.ADMIN -> Color(0xFFDC2626)
                                    UserRole.SISWA -> SkyBluePrimary
                                },
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentUser.role.name,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = when (currentUser.role) {
                                    UserRole.GURU -> Color(0xFF6D28D9)
                                    UserRole.ADMIN -> Color(0xFFDC2626)
                                    UserRole.SISWA -> SkyBluePrimary
                                }
                            )
                        }
                    }
                }

                // Favorites Icon
                IconButton(
                    onClick = onFavoritesClick,
                    modifier = Modifier.size(38.dp).testTag("favorites_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Materi Tersimpan",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Notifications Icon with Badge
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier.size(38.dp).testTag("notifications_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifications > 0) {
                                Badge(containerColor = SafetyRed) {
                                    Text(unreadNotifications.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SportSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Cari materi olahraga, video, atau kuis...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = SkyBluePrimary
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SkyBluePrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("search_input")
    )
}

@Composable
fun SportCategoryCard(
    category: SportCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(category)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) categoryColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(categoryColor, categoryColor)))
        } else {
            CardDefaults.outlinedCardBorder()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
        modifier = modifier.testTag("category_card_${category.name}")
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(categoryColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIcon(category),
                    contentDescription = category.displayName,
                    tint = categoryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isSelected) categoryColor else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun MaterialCard(
    material: MaterialEntity,
    isFavorite: Boolean,
    progressPercentage: Int?,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(material.category)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("material_card_${material.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = categoryColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(material.category),
                            contentDescription = null,
                            tint = categoryColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = material.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Read time badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${material.readTimeMinutes} mnt",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Favorite Button
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier.size(32.dp).testTag("fav_btn_${material.id}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorit",
                            tint = if (isFavorite) SafetyRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = material.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = material.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            val pct = progressPercentage ?: 0
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when {
                                pct >= 100 -> "Selesai"
                                pct > 0 -> "Sedang Dipelajari"
                                else -> "Belum Dimulai"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = when {
                                pct >= 100 -> SportGreen
                                pct > 0 -> SkyBluePrimary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                        Text(
                            text = "$pct%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { pct / 100f },
                        color = if (pct >= 100) SportGreen else SkyBluePrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }

                if (pct >= 100) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Completed",
                        tint = SportGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VideoCard(
    video: VideoEntity,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onWatchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(video.category)
    Card(
        onClick = onWatchClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("video_card_${video.id}")
    ) {
        Column {
            // Video Thumbnail Simulation / Hero Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                categoryColor.copy(alpha = 0.85f),
                                NavyDeep
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Play Icon Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Video",
                        tint = categoryColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Duration badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = video.duration,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = video.category.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = categoryColor
                    )
                    IconButton(
                        onClick = onFavoriteToggle,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Simpan Video",
                            tint = if (isFavorite) SafetyRed else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun StatCard(
    count: String,
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
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
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = count,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun BadgeCard(
    badge: BadgeEntity,
    isUnlocked: Boolean,
    unlockedDate: String? = null,
    modifier: Modifier = Modifier
) {
    val badgeIcon = when (badge.badgeKey) {
        "sport_learner" -> Icons.Default.EmojiEvents
        "football_expert" -> Icons.Default.SportsSoccer
        "basketball_player" -> Icons.Default.SportsBasketball
        "volleyball_expert" -> Icons.Default.SportsVolleyball
        "athletics_master" -> Icons.Default.FitnessCenter
        "first_aid_hero" -> Icons.Default.MedicalServices
        "quiz_champion" -> Icons.Default.LocalFireDepartment
        else -> Icons.Default.School
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        border = if (isUnlocked) CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(GoldAccent, SkyBluePrimary))) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 3.dp else 0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) Brush.radialGradient(listOf(GoldContainer, GoldAccent.copy(alpha = 0.2f)))
                        else Brush.radialGradient(listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) badgeIcon else Icons.Default.Lock,
                    contentDescription = badge.name,
                    tint = if (isUnlocked) GoldAccent else Color(0xFF94A3B8),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = badge.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isUnlocked) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Unlocked",
                            tint = SportGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Syarat: ${badge.requirement}",
                    style = MaterialTheme.typography.labelSmall,
                    color = SkyBluePrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun LeaderboardPodiumCard(
    user: UserEntity,
    rank: Int,
    modifier: Modifier = Modifier
) {
    val medalColor = when (rank) {
        1 -> GoldAccent
        2 -> SilverAccent
        3 -> BronzeAccent
        else -> SkyBluePrimary
    }

    val rankEmoji = when (rank) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "#$rank"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (rank <= 3) 3.dp else 1.dp),
        border = if (rank <= 3) CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(medalColor, medalColor.copy(alpha = 0.4f)))) else null,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Number
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(medalColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rankEmoji,
                    fontSize = if (rank <= 3) 18.sp else 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = medalColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${user.kelas} • ${user.completedCount} Materi Selesai",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Score Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SkyBluePrimary.copy(alpha = 0.1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Poin",
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${user.totalScore} Poin",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = SkyBluePrimary
                    )
                }
            }
        }
    }
}
