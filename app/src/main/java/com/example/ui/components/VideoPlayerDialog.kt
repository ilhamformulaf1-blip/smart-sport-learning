package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.VideoEntity
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavySurface
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SportGreen
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerDialog(
    video: VideoEntity,
    isPlaying: Boolean,
    onTogglePlay: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var sliderProgress by remember { mutableFloatStateOf(0.15f) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var isFullscreen by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(500)
            sliderProgress = (sliderProgress + 0.008f * playbackSpeed).coerceIn(0f, 1f)
            if (sliderProgress >= 1f) {
                sliderProgress = 0f
            }
        }
    }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .padding(if (isFullscreen) 0.dp else 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(if (isFullscreen) 0.dp else 24.dp),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("video_player_dialog")
            ) {
                Column {
                    // Video Header Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavyDeep)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = video.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 1
                            )
                            Text(
                                text = "Kategori: ${video.category.displayName} • Durasi ${video.duration}",
                                style = MaterialTheme.typography.labelSmall,
                                color = SkyBlueLight
                            )
                        }

                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup",
                                tint = Color.White
                            )
                        }
                    }

                    // Simulated Video Canvas Screen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isFullscreen) 360.dp else 220.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        NavyDeep,
                                        Color(0xFF0369A1),
                                        NavyDeep
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        // Animated Sport Graphic Overlay
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(SkyBluePrimary.copy(alpha = 0.3f))
                                    .clickable { onTogglePlay() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isPlaying) "Memutar Video Pembelajaran..." else "Video Dijeda",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Watermark / Source Tag
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Smart Sport Video Hub",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Video Scrubbing & Control Bar
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavySurface)
                            .padding(16.dp)
                    ) {
                        // Time Slider
                        Slider(
                            value = sliderProgress,
                            onValueChange = { sliderProgress = it },
                            colors = SliderDefaults.colors(
                                thumbColor = SkyBluePrimary,
                                activeTrackColor = SkyBluePrimary,
                                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val currentSec = (sliderProgress * 480).toInt()
                            val min = currentSec / 60
                            val sec = currentSec % 60
                            Text(
                                text = String.format("%02d:%02d", min, sec),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = video.duration,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Controls Row: Prev, Rewind, Play/Pause, Next, Speed, Fullscreen, Open in YouTube
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onPrev) {
                                Icon(
                                    imageVector = Icons.Default.SkipPrevious,
                                    contentDescription = "Video Sebelumnya",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = { sliderProgress = (sliderProgress - 0.05f).coerceAtLeast(0f) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay10,
                                    contentDescription = "Mundur 10 Detik",
                                    tint = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(SkyBluePrimary)
                                    .clickable { onTogglePlay() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            IconButton(onClick = onNext) {
                                Icon(
                                    imageVector = Icons.Default.SkipNext,
                                    contentDescription = "Video Berikutnya",
                                    tint = Color.White
                                )
                            }

                            // Speed Toggler (1.0x, 1.25x, 1.5x)
                            TextButton(
                                onClick = {
                                    playbackSpeed = when (playbackSpeed) {
                                        1.0f -> 1.25f
                                        1.25f -> 1.5f
                                        else -> 1.0f
                                    }
                                }
                            ) {
                                Text(
                                    text = "${playbackSpeed}x",
                                    color = SkyBlueLight,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Fullscreen Toggle
                            IconButton(onClick = { isFullscreen = !isFullscreen }) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "Fullscreen",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // External Link Button (YouTube)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFDC2626).copy(alpha = 0.15f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.videoUrl))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // fallback
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Buka di Aplikasi YouTube / Browser Eksternal",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
