package com.example.ui.screens.admin

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.window.Dialog
import com.example.data.local.MaterialEntity
import com.example.data.local.QuizQuestionEntity
import com.example.data.local.UserEntity
import com.example.data.local.VideoEntity
import com.example.model.SportCategory
import com.example.model.UserRole
import com.example.ui.components.StatCard
import com.example.ui.components.getCategoryColor
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.CautionOrange
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.SafetyRed
import com.example.ui.theme.SkyBlueLight
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SportGreen
import com.example.viewmodel.SportViewModel

@Composable
fun TeacherAdminDashboard(
    viewModel: SportViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val allMaterials by viewModel.allMaterials.collectAsState()
    val allVideos by viewModel.allVideos.collectAsState()
    val allQuizResults by viewModel.allQuizResults.collectAsState()
    val totalQuizzes by viewModel.totalQuizzesCount.collectAsState()
    val averageScore by viewModel.averageQuizScore.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddMaterialDialog by remember { mutableStateOf(false) }
    var editingMaterial by remember { mutableStateOf<MaterialEntity?>(null) }

    var showAddVideoDialog by remember { mutableStateOf(false) }
    var editingVideo by remember { mutableStateOf<VideoEntity?>(null) }

    val isTeacher = currentUser?.role == UserRole.GURU
    val isAdmin = currentUser?.role == UserRole.ADMIN

    // Analytics calculations
    val students = allUsers.filter { it.role == UserRole.SISWA }
    val needingGuidance = allQuizResults.filter { !it.isPassed }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("teacher_admin_dashboard")
    ) {
        // Header Banner
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = if (isAdmin) Color(0xFF991B1B) else Color(0xFF5B21B6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.School,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAdmin) "PANEL KELOLA ADMIN" else "DASHBOARD GURU PJOK",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Monitoring nilai siswa, kelola materi pembelajaran, video tutorial, dan bank soal",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 12.dp
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Nilai Siswa") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Kelola Materi (${allMaterials.size})") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Kelola Video (${allVideos.size})") }
            )
            if (isAdmin) {
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("Pengguna (${allUsers.size})") }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Tab 0: Student Scores & Monitoring
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Summary metrics
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCard(
                                count = students.size.toString(),
                                title = "Total Siswa",
                                icon = Icons.Default.Group,
                                color = SkyBluePrimary,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                count = String.format("%.1f", averageScore ?: 84.5),
                                title = "Rata-rata Skor",
                                icon = Icons.Default.TrendingUp,
                                color = SportGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Needing Guidance Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CautionOrange.copy(alpha = 0.1f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = CautionOrange,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Analisis Perlu Bimbingan Remedial",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = CautionOrange
                                    )
                                    Text(
                                        text = "Terdapat ${needingGuidance.size} riwayat kuis siswa dengan nilai di bawah KKM (75).",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Rekapitulasi Hasil Kuis Siswa",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (allQuizResults.isEmpty()) {
                        item {
                            Text(
                                text = "Belum ada hasil kuis yang tercatat.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(allQuizResults) { result ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        val studentName = allUsers.find { it.id == result.userId }?.name ?: "Siswa #${result.userId}"
                                        Text(
                                            text = studentName,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${result.category.displayName} • ${result.correctCount}/${result.totalQuestions} Soal Benar",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (result.isPassed) SportGreen.copy(alpha = 0.15f) else SafetyRed.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "${result.score} pts",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Black,
                                            color = if (result.isPassed) SportGreen else SafetyRed,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Tab 1: Manage Materials
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Daftar Materi PJOK",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Button(
                                    onClick = {
                                        editingMaterial = null
                                        showAddMaterialDialog = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Tambah Materi")
                                }
                            }
                        }

                        items(allMaterials, key = { it.id }) { mat ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = mat.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${mat.category.displayName} • ${mat.readTimeMinutes} mnt",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            editingMaterial = mat
                                            showAddMaterialDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = SkyBluePrimary
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteMaterial(mat.id) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            tint = SafetyRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Tab 2: Manage Videos
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Daftar Video Pembelajaran",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Button(
                                    onClick = {
                                        editingVideo = null
                                        showAddVideoDialog = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Tambah Video")
                                }
                            }
                        }

                        items(allVideos, key = { it.id }) { vid ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = vid.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "${vid.category.displayName} • Durasi ${vid.duration}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            editingVideo = vid
                                            showAddVideoDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = SkyBluePrimary
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteVideo(vid.id) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus",
                                            tint = SafetyRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Tab 3: Admin User Management
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "Daftar Seluruh Pengguna Sistem",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    items(allUsers, key = { it.id }) { user ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (user.role) {
                                                UserRole.ADMIN -> SafetyRed.copy(alpha = 0.15f)
                                                UserRole.GURU -> Color(0xFF6D28D9).copy(alpha = 0.15f)
                                                UserRole.SISWA -> SkyBluePrimary.copy(alpha = 0.15f)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when (user.role) {
                                            UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                            UserRole.GURU -> Icons.Default.School
                                            UserRole.SISWA -> Icons.Default.Person
                                        },
                                        contentDescription = null,
                                        tint = when (user.role) {
                                            UserRole.ADMIN -> SafetyRed
                                            UserRole.GURU -> Color(0xFF6D28D9)
                                            UserRole.SISWA -> SkyBluePrimary
                                        },
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = user.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${user.email} • ${user.kelas} • Peran: ${user.role.name}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (user.id != (currentUser?.id ?: 0L)) {
                                    IconButton(onClick = { viewModel.deleteUser(user.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Hapus Pengguna",
                                            tint = SafetyRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Material Form Dialog
        if (showAddMaterialDialog) {
            MaterialFormDialog(
                initial = editingMaterial,
                onDismiss = { showAddMaterialDialog = false },
                onSave = { mat ->
                    viewModel.saveMaterial(mat) {
                        showAddMaterialDialog = false
                    }
                }
            )
        }

        // Video Form Dialog
        if (showAddVideoDialog) {
            VideoFormDialog(
                initial = editingVideo,
                onDismiss = { showAddVideoDialog = false },
                onSave = { vid ->
                    viewModel.saveVideo(vid) {
                        showAddVideoDialog = false
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialFormDialog(
    initial: MaterialEntity?,
    onDismiss: () -> Unit,
    onSave: (MaterialEntity) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(initial?.category ?: SportCategory.SEPAK_BOLA) }
    var shortDesc by remember { mutableStateOf(initial?.shortDescription ?: "") }
    var learningObjectives by remember { mutableStateOf(initial?.learningObjectives ?: "") }
    var contentBody by remember { mutableStateOf(initial?.contentBody ?: "") }
    var basicTechniques by remember { mutableStateOf(initial?.basicTechniques ?: "") }
    var commonMistakes by remember { mutableStateOf(initial?.commonMistakes ?: "") }
    var tips by remember { mutableStateOf(initial?.tips ?: "") }
    var practiceExercises by remember { mutableStateOf(initial?.practiceExercises ?: "") }
    var safetyWarning by remember { mutableStateOf(initial?.safetyWarning ?: "") }
    var readTimeMinutes by remember { mutableIntStateOf(initial?.readTimeMinutes ?: 10) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = if (initial == null) "Tambah Materi PJOK Baru" else "Edit Materi PJOK",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Materi") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cabang Olahraga") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        SportCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = shortDesc,
                    onValueChange = { shortDesc = it },
                    label = { Text("Deskripsi Singkat") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = learningObjectives,
                    onValueChange = { learningObjectives = it },
                    label = { Text("Tujuan Pembelajaran") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = contentBody,
                    onValueChange = { contentBody = it },
                    label = { Text("Pembahasan Materi Lengkap") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = basicTechniques,
                    onValueChange = { basicTechniques = it },
                    label = { Text("Teknik Dasar & Tahapan Gerak") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = commonMistakes,
                    onValueChange = { commonMistakes = it },
                    label = { Text("Kesalahan Umum") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = tips,
                    onValueChange = { tips = it },
                    label = { Text("Tips Pembelajaran") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = practiceExercises,
                    onValueChange = { practiceExercises = it },
                    label = { Text("Latihan Mandiri") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val entity = (initial ?: MaterialEntity(
                                title = "",
                                category = selectedCategory,
                                shortDescription = "",
                                learningObjectives = "",
                                contentBody = "",
                                basicTechniques = "",
                                commonMistakes = "",
                                tips = "",
                                practiceExercises = ""
                            )).copy(
                                title = title,
                                category = selectedCategory,
                                shortDescription = shortDesc,
                                learningObjectives = learningObjectives,
                                contentBody = contentBody,
                                basicTechniques = basicTechniques,
                                commonMistakes = commonMistakes,
                                tips = tips,
                                practiceExercises = practiceExercises,
                                safetyWarning = safetyWarning,
                                readTimeMinutes = readTimeMinutes
                            )
                            onSave(entity)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                    ) {
                        Text("Simpan Materi")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFormDialog(
    initial: VideoEntity?,
    onDismiss: () -> Unit,
    onSave: (VideoEntity) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var selectedCategory by remember { mutableStateOf(initial?.category ?: SportCategory.SEPAK_BOLA) }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var videoUrl by remember { mutableStateOf(initial?.videoUrl ?: "https://www.youtube.com/watch?v=dQw4w9WgXcQ") }
    var duration by remember { mutableStateOf(initial?.duration ?: "08:30") }
    var categoryExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = if (initial == null) "Tambah Video Baru" else "Edit Video Pembelajaran",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Video") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory.displayName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cabang Olahraga") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        SportCategory.entries.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.displayName) },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi Video") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = videoUrl,
                    onValueChange = { videoUrl = it },
                    label = { Text("URL Video (YouTube)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Durasi (contoh: 08:30)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val entity = (initial ?: VideoEntity(
                                title = "",
                                category = selectedCategory,
                                description = "",
                                videoUrl = videoUrl,
                                duration = duration
                            )).copy(
                                title = title,
                                category = selectedCategory,
                                description = description,
                                videoUrl = videoUrl,
                                duration = duration
                            )
                            onSave(entity)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                    ) {
                        Text("Simpan Video")
                    }
                }
            }
        }
    }
}
