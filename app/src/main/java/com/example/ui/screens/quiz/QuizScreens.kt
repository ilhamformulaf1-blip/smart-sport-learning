package com.example.ui.screens.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MaterialEntity
import com.example.model.SportCategory
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.CautionOrange
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavySurface
import com.example.ui.theme.SafetyRed
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SportGreen
import com.example.viewmodel.SportViewModel

@Composable
fun QuizHubScreen(
    viewModel: SportViewModel,
    onStartCategoryQuiz: (SportCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val allMaterials by viewModel.allMaterials.collectAsState()
    val allQuizResults by viewModel.allQuizResults.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    val userResults = allQuizResults.filter { it.userId == (currentUser?.id ?: 1L) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("quiz_hub_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                            Brush.linearGradient(
                                listOf(
                                    NavyDeep,
                                    Color(0xFF6D28D9),
                                    NavyDeep
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                tint = SkyBlueLight,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "KUIS & EVALUASI PJOK",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = SkyBlueLight
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Uji Pengetahuan Olahragamu!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pilih cabang olahraga di bawah untuk mengerjakan simulasi latihan soal pilihan ganda interaktif.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Pilih Kategori Kuis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        itemsIndexed(SportCategory.entries.toTypedArray()) { _, cat ->
            val catColor = getCategoryColor(cat)
            val pastResult = userResults.find { it.category == cat }

            Card(
                onClick = { onStartCategoryQuiz(cat) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("quiz_cat_${cat.name}")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(catColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(cat),
                            contentDescription = cat.displayName,
                            tint = catColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cat.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "10 Soal Pilihan Ganda • 5 Menit",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (pastResult != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Skor Terakhir: ${pastResult.score}/100 (${if (pastResult.isPassed) "Lulus" else "Belum Lulus"})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (pastResult.isPassed) SportGreen else SafetyRed
                            )
                        }
                    }

                    Button(
                        onClick = { onStartCategoryQuiz(cat) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = catColor)
                    ) {
                        Text("Mulai", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActiveQuizScreen(
    viewModel: SportViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quizState by viewModel.quizState.collectAsState()
    val questions = quizState.questions

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Tidak ada pertanyaan untuk kuis ini.")
        }
        return
    }

    if (quizState.isSubmitted && quizState.result != null) {
        // Render Result Screen
        QuizResultView(
            quizState = quizState,
            onRetry = {
                if (quizState.materialId > 0) {
                    val mat = viewModel.allMaterials.value.find { it.id == quizState.materialId }
                    if (mat != null) viewModel.startQuizForMaterial(mat)
                    else viewModel.startQuizForCategory(quizState.category)
                } else {
                    viewModel.startQuizForCategory(quizState.category)
                }
            },
            onBackClick = onBackClick
        )
        return
    }

    val currentQ = questions.getOrNull(quizState.currentQuestionIndex) ?: questions[0]
    val selectedOption = quizState.selectedAnswers[quizState.currentQuestionIndex]
    val timeMin = quizState.timeRemainingSeconds / 60
    val timeSec = quizState.timeRemainingSeconds % 60
    val isTimeLow = quizState.timeRemainingSeconds <= 60

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("active_quiz_screen")
    ) {
        // Quiz Top Bar with Timer
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Batal",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "Soal ${quizState.currentQuestionIndex + 1} dari ${questions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Timer Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isTimeLow) SafetyRed.copy(alpha = 0.15f) else SkyBluePrimary.copy(alpha = 0.15f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = if (isTimeLow) SafetyRed else SkyBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%02d:%02d", timeMin, timeSec),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = if (isTimeLow) SafetyRed else SkyBluePrimary
                        )
                    }
                }
            }
        }

        // Main Question & Options Scrollable Area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Number Jumping Row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                questions.indices.forEach { idx ->
                    val isAnswered = quizState.selectedAnswers.containsKey(idx)
                    val isCurrent = quizState.currentQuestionIndex == idx
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when {
                                    isCurrent -> SkyBluePrimary
                                    isAnswered -> SportGreen.copy(alpha = 0.2f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                            .border(
                                width = if (isCurrent) 2.dp else 1.dp,
                                color = if (isCurrent) SkyBluePrimary else if (isAnswered) SportGreen else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.jumpToQuestion(idx) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (idx + 1).toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = currentQ.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options List (A, B, C, D)
            val optionLetters = listOf("A", "B", "C", "D")
            listOf(currentQ.optionA, currentQ.optionB, currentQ.optionC, currentQ.optionD).forEachIndexed { optIndex, optionText ->
                val isSelected = selectedOption == optIndex

                Card(
                    onClick = { viewModel.selectQuizAnswer(quizState.currentQuestionIndex, optIndex) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) SkyBluePrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(SkyBluePrimary, SkyBluePrimary))) else CardDefaults.outlinedCardBorder(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .testTag("option_${quizState.currentQuestionIndex}_$optIndex")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) SkyBluePrimary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = optionLetters[optIndex],
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) SkyBluePrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Bottom Navigation Buttons
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.goToPrevQuestion() },
                    enabled = quizState.currentQuestionIndex > 0,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sebelumnya")
                }

                if (quizState.currentQuestionIndex < questions.size - 1) {
                    Button(
                        onClick = { viewModel.goToNextQuestion() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                    ) {
                        Text("Berikutnya")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.submitQuiz() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SportGreen),
                        modifier = Modifier.testTag("submit_quiz_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Selesai & Kumpulkan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun QuizResultView(
    quizState: com.example.viewmodel.QuizSessionState,
    onRetry: () -> Unit,
    onBackClick: () -> Unit
) {
    val result = quizState.result ?: return
    val isPassed = result.isPassed
    val questions = quizState.questions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("quiz_result_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Result Banner
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = if (isPassed) SportGreen else SafetyRed),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPassed) Icons.Default.EmojiEvents else Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = if (isPassed) SportGreen else SafetyRed,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isPassed) "SELAMAT! KAMU LULUS 🎉" else "BELUM LULUS, TETAP SEMANGAT! 💪",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${result.score}",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Nilai Akhir (Passing Grade: 75)",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats breakdown
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${result.correctCount}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = SportGreen
                    )
                    Text(
                        text = "Benar",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${result.wrongCount}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = SafetyRed
                    )
                    Text(
                        text = "Salah",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Divider(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val min = result.timeSpentSeconds / 60
                    val sec = result.timeSpentSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", min, sec),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = SkyBluePrimary
                    )
                    Text(
                        text = "Durasi",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Ulangi Kuis")
            }

            Button(
                onClick = onBackClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                modifier = Modifier.weight(1f)
            ) {
                Text("Kembali ke Menu", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Pembahasan Soal
        Text(
            text = "Pembahasan Soal & Kunci Jawaban",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        val optionLetters = listOf("A", "B", "C", "D")

        questions.forEachIndexed { index, q ->
            val userAns = quizState.selectedAnswers[index]
            val isCorrect = userAns == q.correctOption

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nomor ${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = SkyBluePrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isCorrect) SportGreen.copy(alpha = 0.15f) else SafetyRed.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (isCorrect) "Benar" else "Salah",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) SportGreen else SafetyRed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Jawaban Kamu: ${if (userAns != null) optionLetters[userAns] else "Tidak dijawab"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isCorrect) SportGreen else SafetyRed
                    )

                    Text(
                        text = "Kunci Jawaban: ${optionLetters[q.correctOption]}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = SportGreen
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Penjelasan:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = q.explanation,
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
