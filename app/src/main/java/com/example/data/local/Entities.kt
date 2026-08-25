package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.FavoriteType
import com.example.model.LearningStatus
import com.example.model.NotificationType
import com.example.model.SportCategory
import com.example.model.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
    val kelas: String,
    val photoUrl: String = "",
    val totalScore: Int = 0,
    val completedCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "materials")
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: SportCategory,
    val title: String,
    val shortDescription: String,
    val learningObjectives: String, // newline-separated bullet points
    val contentBody: String,
    val basicTechniques: String, // structured steps
    val commonMistakes: String,
    val tips: String,
    val practiceExercises: String,
    val safetyWarning: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val readTimeMinutes: Int = 5,
    val orderIndex: Int = 0,
    val isPopular: Boolean = false
)

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: SportCategory,
    val title: String,
    val duration: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String = "",
    val orderIndex: Int = 0
)

@Entity(tableName = "quizzes")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val materialId: Long,
    val category: SportCategory,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: Int, // 0 = A, 1 = B, 2 = C, 3 = D
    val explanation: String = ""
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val materialId: Long,
    val category: SportCategory,
    val materialTitle: String,
    val score: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val totalQuestions: Int,
    val timeSpentSeconds: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isPassed: Boolean
)

@Entity(tableName = "learning_progress")
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val materialId: Long,
    val category: SportCategory,
    val percentage: Int,
    val status: LearningStatus,
    val lastAccessedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "badges")
data class BadgeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val badgeKey: String,
    val name: String,
    val description: String,
    val icon: String,
    val requirement: String,
    val category: String = ""
)

@Entity(tableName = "user_badges")
data class UserBadgeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val badgeKey: String,
    val unlockedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val itemType: FavoriteType,
    val itemId: Long,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
