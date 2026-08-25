package com.example.data.repository

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
import com.example.model.FavoriteType
import com.example.model.LearningStatus
import com.example.model.NotificationType
import com.example.model.SportCategory
import com.example.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SportRepository(private val db: AppDatabase) {

    // --- Users & Auth ---
    val allUsers: Flow<List<UserEntity>> = db.userDao().getAllUsers()
    val totalUsersCount: Flow<Int> = db.userDao().getUserCount()

    suspend fun getUserByEmail(email: String): UserEntity? {
        return db.userDao().getUserByEmail(email)
    }

    suspend fun getUserById(userId: Long): UserEntity? {
        return db.userDao().getUserById(userId)
    }

    fun getUserFlow(userId: Long): Flow<UserEntity?> {
        return db.userDao().getUserByIdFlow(userId)
    }

    suspend fun registerUser(name: String, email: String, password: String, role: UserRole, kelas: String): Long {
        val user = UserEntity(
            name = name,
            email = email,
            passwordHash = password,
            role = role,
            kelas = kelas,
            photoUrl = if (role == UserRole.GURU) "avatar_teacher" else "avatar_student_1"
        )
        return db.userDao().insertUser(user)
    }

    suspend fun updateUser(user: UserEntity) {
        db.userDao().updateUser(user)
    }

    suspend fun deleteUser(userId: Long) {
        db.userDao().deleteUserById(userId)
    }

    // --- Materials ---
    val allMaterials: Flow<List<MaterialEntity>> = db.materialDao().getAllMaterials()
    val popularMaterials: Flow<List<MaterialEntity>> = db.materialDao().getPopularMaterials()
    val totalMaterialsCount: Flow<Int> = db.materialDao().getMaterialCount()

    fun getMaterialsByCategory(category: SportCategory): Flow<List<MaterialEntity>> {
        return db.materialDao().getMaterialsByCategory(category)
    }

    fun getMaterialById(id: Long): Flow<MaterialEntity?> {
        return db.materialDao().getMaterialById(id)
    }

    suspend fun getMaterialByIdDirect(id: Long): MaterialEntity? {
        return db.materialDao().getMaterialByIdDirect(id)
    }

    fun searchMaterials(query: String): Flow<List<MaterialEntity>> {
        return db.materialDao().searchMaterials(query)
    }

    suspend fun insertOrUpdateMaterial(material: MaterialEntity): Long {
        return if (material.id == 0L) {
            db.materialDao().insertMaterial(material)
        } else {
            db.materialDao().updateMaterial(material)
            material.id
        }
    }

    suspend fun deleteMaterial(id: Long) {
        db.materialDao().deleteMaterialById(id)
    }

    // --- Videos ---
    val allVideos: Flow<List<VideoEntity>> = db.videoDao().getAllVideos()
    val totalVideosCount: Flow<Int> = db.videoDao().getVideoCount()

    fun getVideosByCategory(category: SportCategory): Flow<List<VideoEntity>> {
        return db.videoDao().getVideosByCategory(category)
    }

    fun getVideoById(id: Long): Flow<VideoEntity?> {
        return db.videoDao().getVideoById(id)
    }

    fun searchVideos(query: String): Flow<List<VideoEntity>> {
        return db.videoDao().searchVideos(query)
    }

    suspend fun insertOrUpdateVideo(video: VideoEntity): Long {
        return if (video.id == 0L) {
            db.videoDao().insertVideo(video)
        } else {
            db.videoDao().updateVideo(video)
            video.id
        }
    }

    suspend fun deleteVideo(id: Long) {
        db.videoDao().deleteVideoById(id)
    }

    // --- Quizzes ---
    val totalQuizzesCount: Flow<Int> = db.quizDao().getQuizCount()
    val averageScore: Flow<Double?> = db.quizResultDao().getAverageScore()
    val allQuizResults: Flow<List<QuizResultEntity>> = db.quizResultDao().getAllResults()

    fun getQuizzesByMaterial(materialId: Long): Flow<List<QuizQuestionEntity>> {
        return db.quizDao().getQuizzesByMaterial(materialId)
    }

    suspend fun getQuizzesByMaterialDirect(materialId: Long): List<QuizQuestionEntity> {
        return db.quizDao().getQuizzesByMaterialDirect(materialId)
    }

    fun getQuizzesByCategory(category: SportCategory): Flow<List<QuizQuestionEntity>> {
        return db.quizDao().getQuizzesByCategory(category)
    }

    suspend fun insertOrUpdateQuiz(quiz: QuizQuestionEntity): Long {
        return if (quiz.id == 0L) {
            db.quizDao().insertQuiz(quiz)
        } else {
            db.quizDao().updateQuiz(quiz)
            quiz.id
        }
    }

    suspend fun deleteQuiz(id: Long) {
        db.quizDao().deleteQuizById(id)
    }

    fun getQuizResultsByUser(userId: Long): Flow<List<QuizResultEntity>> {
        return db.quizResultDao().getResultsByUser(userId)
    }

    fun getLatestQuizResultByUser(userId: Long): Flow<QuizResultEntity?> {
        return db.quizResultDao().getLatestResultByUser(userId)
    }

    suspend fun submitQuizResult(result: QuizResultEntity): Long {
        val id = db.quizResultDao().insertResult(result)
        // Check if user unlocked Quiz Champion badge (score >= 90)
        if (result.score >= 90) {
            db.badgeDao().unlockBadge(UserBadgeEntity(0, result.userId, "quiz_champion", System.currentTimeMillis()))
            db.notificationDao().insertNotification(
                NotificationEntity(
                    0, result.userId, "Luar Biasa! Badge Quiz Champion Diraih",
                    "Kamu meraih nilai ${result.score} pada kuis ${result.materialTitle}!",
                    NotificationType.BADGE_UNLOCKED
                )
            )
        }

        // Update user's total score in UserEntity
        val user = db.userDao().getUserById(result.userId)
        if (user != null) {
            val updatedUser = user.copy(totalScore = user.totalScore + result.score)
            db.userDao().updateUser(updatedUser)
        }
        return id
    }

    // --- Learning Progress ---
    fun getProgressByUser(userId: Long): Flow<List<ProgressEntity>> {
        return db.progressDao().getProgressByUser(userId)
    }

    fun getProgressByMaterial(userId: Long, materialId: Long): Flow<ProgressEntity?> {
        return db.progressDao().getProgressByMaterial(userId, materialId)
    }

    fun getLastActiveProgress(userId: Long): Flow<ProgressEntity?> {
        return db.progressDao().getLastActiveProgress(userId)
    }

    suspend fun updateProgress(userId: Long, materialId: Long, category: SportCategory, percentage: Int) {
        val existing = db.progressDao().getProgressByMaterialDirect(userId, materialId)
        val status = when {
            percentage >= 100 -> LearningStatus.COMPLETED
            percentage > 0 -> LearningStatus.IN_PROGRESS
            else -> LearningStatus.NOT_STARTED
        }
        val entity = if (existing != null) {
            val newPercentage = maxOf(existing.percentage, percentage)
            existing.copy(
                percentage = newPercentage,
                status = if (newPercentage >= 100) LearningStatus.COMPLETED else LearningStatus.IN_PROGRESS,
                lastAccessedTimestamp = System.currentTimeMillis()
            )
        } else {
            ProgressEntity(
                userId = userId,
                materialId = materialId,
                category = category,
                percentage = percentage,
                status = status,
                lastAccessedTimestamp = System.currentTimeMillis()
            )
        }
        db.progressDao().insertOrUpdateProgress(entity)

        // If completed, trigger badges
        if (percentage >= 100) {
            db.badgeDao().unlockBadge(UserBadgeEntity(0, userId, "sport_learner", System.currentTimeMillis()))

            // Count completed by category
            when (category) {
                SportCategory.SEPAK_BOLA -> db.badgeDao().unlockBadge(UserBadgeEntity(0, userId, "football_expert", System.currentTimeMillis()))
                SportCategory.BOLA_BASKET -> db.badgeDao().unlockBadge(UserBadgeEntity(0, userId, "basketball_player", System.currentTimeMillis()))
                SportCategory.BOLA_VOLI -> db.badgeDao().unlockBadge(UserBadgeEntity(0, userId, "volleyball_expert", System.currentTimeMillis()))
                SportCategory.ATLETIK -> db.badgeDao().unlockBadge(UserBadgeEntity(0, userId, "athletics_master", System.currentTimeMillis()))
                SportCategory.PENANGANAN_CEDERA -> db.badgeDao().unlockBadge(UserBadgeEntity(0, userId, "first_aid_hero", System.currentTimeMillis()))
            }

            // Update user completedCount
            val user = db.userDao().getUserById(userId)
            if (user != null) {
                db.userDao().updateUser(user.copy(completedCount = user.completedCount + 1))
            }
        }
    }

    // --- Badges ---
    val allBadges: Flow<List<BadgeEntity>> = db.badgeDao().getAllBadges()

    fun getUserBadges(userId: Long): Flow<List<UserBadgeEntity>> {
        return db.badgeDao().getUserBadges(userId)
    }

    // --- Favorites ---
    fun getFavoritesByUser(userId: Long): Flow<List<FavoriteEntity>> {
        return db.favoriteDao().getFavoritesByUser(userId)
    }

    fun isFavorite(userId: Long, type: FavoriteType, itemId: Long): Flow<Boolean> {
        return db.favoriteDao().isFavorite(userId, type, itemId)
    }

    suspend fun toggleFavorite(userId: Long, type: FavoriteType, itemId: Long) {
        val isFav = db.favoriteDao().isFavorite(userId, type, itemId).first()
        if (isFav) {
            db.favoriteDao().removeFavorite(userId, type, itemId)
        } else {
            db.favoriteDao().addFavorite(FavoriteEntity(0, userId, type, itemId))
        }
    }

    // --- Notifications ---
    fun getNotificationsByUser(userId: Long): Flow<List<NotificationEntity>> {
        return db.notificationDao().getNotificationsByUser(userId)
    }

    fun getUnreadNotificationCount(userId: Long): Flow<Int> {
        return db.notificationDao().getUnreadCount(userId)
    }

    suspend fun sendNotification(userId: Long, title: String, message: String, type: NotificationType) {
        db.notificationDao().insertNotification(NotificationEntity(0, userId, title, message, type))
    }

    suspend fun markAllNotificationsAsRead(userId: Long) {
        db.notificationDao().markAllAsRead(userId)
    }
}
