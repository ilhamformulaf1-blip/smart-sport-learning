package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.BadgeEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.MaterialEntity
import com.example.data.local.NotificationEntity
import com.example.data.local.ProgressEntity
import com.example.data.local.QuizQuestionEntity
import com.example.data.local.QuizResultEntity
import com.example.data.local.UserBadgeEntity
import com.example.data.local.UserEntity
import com.example.data.local.VideoEntity
import com.example.data.repository.SportRepository
import com.example.data.seed.DatabaseSeeder
import com.example.model.FavoriteType
import com.example.model.LearningStatus
import com.example.model.NotificationType
import com.example.model.SportCategory
import com.example.model.UserRole
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthUiState(
    val currentUser: UserEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class QuizSessionState(
    val materialId: Long = 0,
    val category: SportCategory = SportCategory.SEPAK_BOLA,
    val materialTitle: String = "",
    val questions: List<QuizQuestionEntity> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> selectedOptionIndex (0-3)
    val isSubmitted: Boolean = false,
    val timeRemainingSeconds: Int = 300, // 5 minutes timer
    val isTimerRunning: Boolean = false,
    val result: QuizResultEntity? = null
)

data class CategoryProgressSummary(
    val category: SportCategory,
    val totalMaterials: Int,
    val completedMaterials: Int,
    val percentage: Int
)

class SportViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = SportRepository(db)

    // Current User State
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Selected Category Filter for Material/Video Tabs
    private val _selectedCategory = MutableStateFlow<SportCategory?>(null)
    val selectedCategory: StateFlow<SportCategory?> = _selectedCategory.asStateFlow()

    // Active Quiz Session
    private val _quizState = MutableStateFlow(QuizSessionState())
    val quizState: StateFlow<QuizSessionState> = _quizState.asStateFlow()
    private var quizTimerJob: Job? = null

    // Video Player State
    private val _activeVideo = MutableStateFlow<VideoEntity?>(null)
    val activeVideo: StateFlow<VideoEntity?> = _activeVideo.asStateFlow()
    private val _isVideoPlaying = MutableStateFlow(false)
    val isVideoPlaying: StateFlow<Boolean> = _isVideoPlaying.asStateFlow()
    private val _videoProgress = MutableStateFlow(0f)
    val videoProgress: StateFlow<Float> = _videoProgress.asStateFlow()

    // Data Flows
    val allMaterials: StateFlow<List<MaterialEntity>> = repository.allMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val popularMaterials: StateFlow<List<MaterialEntity>> = repository.popularMaterials
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVideos: StateFlow<List<VideoEntity>> = repository.allVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBadges: StateFlow<List<BadgeEntity>> = repository.allBadges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalMaterialsCount: StateFlow<Int> = repository.totalMaterialsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalVideosCount: StateFlow<Int> = repository.totalVideosCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalQuizzesCount: StateFlow<Int> = repository.totalQuizzesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val averageQuizScore: StateFlow<Double?> = repository.averageScore
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allQuizResults: StateFlow<List<QuizResultEntity>> = repository.allQuizResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            DatabaseSeeder.seedDatabaseIfEmpty(db)
            // Default login as first student (Ilham) for immediate interactive experience
            val defaultStudent = repository.getUserById(1) ?: repository.allUsers.first().firstOrNull()
            _currentUser.value = defaultStudent
        }
    }

    // --- Authentication & User Switch ---
    fun login(email: String, password: String,onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _authError.value = null
            val user = repository.getUserByEmail(email.trim())
            if (user != null && user.passwordHash == password.trim()) {
                _currentUser.value = user
                onSuccess()
            } else {
                _authError.value = "Email atau kata sandi tidak sesuai. Silakan coba lagi."
            }
        }
    }

    fun switchRoleQuick(role: UserRole) {
        viewModelScope.launch {
            val user = allUsers.value.find { it.role == role }
            if (user != null) {
                _currentUser.value = user
            }
        }
    }

    fun register(name: String, email: String, password: String, role: UserRole, kelas: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _authError.value = null
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                _authError.value = "Mohon lengkapi seluruh isian data."
                return@launch
            }
            val existing = repository.getUserByEmail(email.trim())
            if (existing != null) {
                _authError.value = "Email sudah terdaftar. Gunakan email lain."
                return@launch
            }
            val newId = repository.registerUser(name.trim(), email.trim(), password.trim(), role, kelas.trim())
            val createdUser = repository.getUserById(newId)
            _currentUser.value = createdUser
            onSuccess()
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun clearAuthError() {
        _authError.value = null
    }

    // --- Search & Category Filtering ---
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: SportCategory?) {
        _selectedCategory.value = category
    }

    // --- User Specific Progress & Badges ---
    fun getUserProgress() = _currentUser.value?.let { user ->
        repository.getProgressByUser(user.id)
    }

    fun getUserBadges() = _currentUser.value?.let { user ->
        repository.getUserBadges(user.id)
    }

    fun getUserFavorites() = _currentUser.value?.let { user ->
        repository.getFavoritesByUser(user.id)
    }

    fun getUserNotifications() = _currentUser.value?.let { user ->
        repository.getNotificationsByUser(user.id)
    }

    fun getUnreadNotificationsCount() = _currentUser.value?.let { user ->
        repository.getUnreadNotificationCount(user.id)
    }

    fun getLatestQuizResult() = _currentUser.value?.let { user ->
        repository.getLatestQuizResultByUser(user.id)
    }

    fun toggleFavorite(type: FavoriteType, itemId: Long) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(user.id, type, itemId)
        }
    }

    fun markNotificationsRead() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.markAllNotificationsAsRead(user.id)
        }
    }

    fun markMaterialCompleted(material: MaterialEntity) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateProgress(user.id, material.id, material.category, 100)
        }
    }

    // --- Video Player Controls ---
    fun openVideoPlayer(video: VideoEntity) {
        _activeVideo.value = video
        _isVideoPlaying.value = true
        _videoProgress.value = 0f
    }

    fun closeVideoPlayer() {
        _activeVideo.value = null
        _isVideoPlaying.value = false
    }

    fun toggleVideoPlay() {
        _isVideoPlaying.value = !_isVideoPlaying.value
    }

    fun setVideoProgress(progress: Float) {
        _videoProgress.value = progress.coerceIn(0f, 1f)
    }

    fun nextVideo() {
        val current = _activeVideo.value ?: return
        val list = allVideos.value
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex in 0 until list.size - 1) {
            openVideoPlayer(list[currentIndex + 1])
        }
    }

    fun prevVideo() {
        val current = _activeVideo.value ?: return
        val list = allVideos.value
        val currentIndex = list.indexOfFirst { it.id == current.id }
        if (currentIndex > 0) {
            openVideoPlayer(list[currentIndex - 1])
        }
    }

    // --- Quiz Management & Play Session ---
    fun startQuizForMaterial(material: MaterialEntity) {
        viewModelScope.launch {
            val questions = repository.getQuizzesByMaterialDirect(material.id)
            val fallbackQuestions = if (questions.isEmpty()) {
                repository.getQuizzesByCategory(material.category).first().take(10)
            } else {
                questions
            }

            quizTimerJob?.cancel()
            _quizState.value = QuizSessionState(
                materialId = material.id,
                category = material.category,
                materialTitle = material.title,
                questions = fallbackQuestions,
                currentQuestionIndex = 0,
                selectedAnswers = emptyMap(),
                isSubmitted = false,
                timeRemainingSeconds = 300,
                isTimerRunning = true,
                result = null
            )

            // Start countdown timer
            quizTimerJob = viewModelScope.launch {
                while (_quizState.value.timeRemainingSeconds > 0 && !_quizState.value.isSubmitted) {
                    delay(1000)
                    _quizState.value = _quizState.value.copy(
                        timeRemainingSeconds = _quizState.value.timeRemainingSeconds - 1
                    )
                }
                if (!_quizState.value.isSubmitted) {
                    submitQuiz()
                }
            }
        }
    }

    fun startQuizForCategory(category: SportCategory) {
        viewModelScope.launch {
            val questions = repository.getQuizzesByCategory(category).first().take(10)
            quizTimerJob?.cancel()
            _quizState.value = QuizSessionState(
                materialId = 0,
                category = category,
                materialTitle = "Kuis Cabang Olahraga: ${category.displayName}",
                questions = questions,
                currentQuestionIndex = 0,
                selectedAnswers = emptyMap(),
                isSubmitted = false,
                timeRemainingSeconds = 300,
                isTimerRunning = true,
                result = null
            )

            quizTimerJob = viewModelScope.launch {
                while (_quizState.value.timeRemainingSeconds > 0 && !_quizState.value.isSubmitted) {
                    delay(1000)
                    _quizState.value = _quizState.value.copy(
                        timeRemainingSeconds = _quizState.value.timeRemainingSeconds - 1
                    )
                }
                if (!_quizState.value.isSubmitted) {
                    submitQuiz()
                }
            }
        }
    }

    fun selectQuizAnswer(questionIndex: Int, optionIndex: Int) {
        if (_quizState.value.isSubmitted) return
        val currentMap = _quizState.value.selectedAnswers.toMutableMap()
        currentMap[questionIndex] = optionIndex
        _quizState.value = _quizState.value.copy(selectedAnswers = currentMap)
    }

    fun goToNextQuestion() {
        if (_quizState.value.currentQuestionIndex < _quizState.value.questions.size - 1) {
            _quizState.value = _quizState.value.copy(
                currentQuestionIndex = _quizState.value.currentQuestionIndex + 1
            )
        }
    }

    fun goToPrevQuestion() {
        if (_quizState.value.currentQuestionIndex > 0) {
            _quizState.value = _quizState.value.copy(
                currentQuestionIndex = _quizState.value.currentQuestionIndex - 1
            )
        }
    }

    fun jumpToQuestion(index: Int) {
        if (index in _quizState.value.questions.indices) {
            _quizState.value = _quizState.value.copy(currentQuestionIndex = index)
        }
    }

    fun submitQuiz() {
        quizTimerJob?.cancel()
        val state = _quizState.value
        val questions = state.questions
        if (questions.isEmpty()) return

        var correctCount = 0
        questions.forEachIndexed { index, q ->
            val userSelected = state.selectedAnswers[index]
            if (userSelected != null && userSelected == q.correctOption) {
                correctCount++
            }
        }
        val wrongCount = questions.size - correctCount
        val score = ((correctCount.toDouble() / questions.size.toDouble()) * 100).toInt()
        val isPassed = score >= 75
        val timeSpent = 300 - state.timeRemainingSeconds

        val user = _currentUser.value
        val userId = user?.id ?: 1L

        val resultEntity = QuizResultEntity(
            id = 0,
            userId = userId,
            materialId = state.materialId,
            category = state.category,
            materialTitle = state.materialTitle,
            score = score,
            correctCount = correctCount,
            wrongCount = wrongCount,
            totalQuestions = questions.size,
            timeSpentSeconds = timeSpent,
            timestamp = System.currentTimeMillis(),
            isPassed = isPassed
        )

        viewModelScope.launch {
            repository.submitQuizResult(resultEntity)
            if (isPassed && state.materialId > 0) {
                repository.updateProgress(userId, state.materialId, state.category, 100)
            }
            _quizState.value = _quizState.value.copy(
                isSubmitted = true,
                isTimerRunning = false,
                result = resultEntity
            )
        }
    }

    // --- Teacher & Admin CRUD Methods ---
    fun saveMaterial(material: MaterialEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.insertOrUpdateMaterial(material)
            onComplete()
        }
    }

    fun deleteMaterial(id: Long, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteMaterial(id)
            onComplete()
        }
    }

    fun saveVideo(video: VideoEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.insertOrUpdateVideo(video)
            onComplete()
        }
    }

    fun deleteVideo(id: Long, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteVideo(id)
            onComplete()
        }
    }

    fun saveQuiz(quiz: QuizQuestionEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.insertOrUpdateQuiz(quiz)
            onComplete()
        }
    }

    fun deleteQuiz(id: Long, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteQuiz(id)
            onComplete()
        }
    }

    fun deleteUser(userId: Long) {
        viewModelScope.launch {
            repository.deleteUser(userId)
        }
    }
}
