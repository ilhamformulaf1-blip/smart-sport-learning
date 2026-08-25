package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.FavoriteType
import com.example.model.LearningStatus
import com.example.model.SportCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY totalScore DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserByIdFlow(userId: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUserById(userId: Long)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>
}

@Dao
interface MaterialDao {
    @Query("SELECT * FROM materials ORDER BY orderIndex ASC, id ASC")
    fun getAllMaterials(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materials WHERE category = :category ORDER BY orderIndex ASC")
    fun getMaterialsByCategory(category: SportCategory): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materials WHERE isPopular = 1")
    fun getPopularMaterials(): Flow<List<MaterialEntity>>

    @Query("SELECT * FROM materials WHERE id = :id LIMIT 1")
    fun getMaterialById(id: Long): Flow<MaterialEntity?>

    @Query("SELECT * FROM materials WHERE id = :id LIMIT 1")
    suspend fun getMaterialByIdDirect(id: Long): MaterialEntity?

    @Query("SELECT * FROM materials WHERE title LIKE '%' || :query || '%' OR contentBody LIKE '%' || :query || '%' OR shortDescription LIKE '%' || :query || '%'")
    fun searchMaterials(query: String): Flow<List<MaterialEntity>>

    @Query("SELECT COUNT(*) FROM materials")
    fun getMaterialCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: MaterialEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<MaterialEntity>)

    @Update
    suspend fun updateMaterial(material: MaterialEntity)

    @Query("DELETE FROM materials WHERE id = :id")
    suspend fun deleteMaterialById(id: Long)
}

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY orderIndex ASC, id ASC")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE category = :category ORDER BY orderIndex ASC")
    fun getVideosByCategory(category: SportCategory): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    fun getVideoById(id: Long): Flow<VideoEntity?>

    @Query("SELECT * FROM videos WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchVideos(query: String): Flow<List<VideoEntity>>

    @Query("SELECT COUNT(*) FROM videos")
    fun getVideoCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Update
    suspend fun updateVideo(video: VideoEntity)

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteVideoById(id: Long)
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quizzes WHERE materialId = :materialId")
    fun getQuizzesByMaterial(materialId: Long): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quizzes WHERE materialId = :materialId")
    suspend fun getQuizzesByMaterialDirect(materialId: Long): List<QuizQuestionEntity>

    @Query("SELECT * FROM quizzes WHERE category = :category")
    fun getQuizzesByCategory(category: SportCategory): Flow<List<QuizQuestionEntity>>

    @Query("SELECT COUNT(*) FROM quizzes")
    fun getQuizCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizQuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<QuizQuestionEntity>)

    @Update
    suspend fun updateQuiz(quiz: QuizQuestionEntity)

    @Query("DELETE FROM quizzes WHERE id = :id")
    suspend fun deleteQuizById(id: Long)
}

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY timestamp DESC")
    fun getResultsByUser(userId: Long): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestResultByUser(userId: Long): Flow<QuizResultEntity?>

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<QuizResultEntity>>

    @Query("SELECT AVG(score) FROM quiz_results")
    fun getAverageScore(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity): Long
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM learning_progress WHERE userId = :userId")
    fun getProgressByUser(userId: Long): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM learning_progress WHERE userId = :userId AND materialId = :materialId LIMIT 1")
    fun getProgressByMaterial(userId: Long, materialId: Long): Flow<ProgressEntity?>

    @Query("SELECT * FROM learning_progress WHERE userId = :userId AND materialId = :materialId LIMIT 1")
    suspend fun getProgressByMaterialDirect(userId: Long, materialId: Long): ProgressEntity?

    @Query("SELECT * FROM learning_progress WHERE userId = :userId AND status = :status ORDER BY lastAccessedTimestamp DESC LIMIT 1")
    fun getLastActiveProgress(userId: Long, status: LearningStatus = LearningStatus.IN_PROGRESS): Flow<ProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: ProgressEntity): Long

    @Query("SELECT COUNT(*) FROM learning_progress WHERE userId = :userId AND status = 'COMPLETED'")
    fun getCompletedCount(userId: Long): Flow<Int>
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM badges")
    fun getAllBadges(): Flow<List<BadgeEntity>>

    @Query("SELECT * FROM user_badges WHERE userId = :userId")
    fun getUserBadges(userId: Long): Flow<List<UserBadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadge(badge: BadgeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockBadge(userBadge: UserBadgeEntity): Long
}

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY timestamp DESC")
    fun getFavoritesByUser(userId: Long): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND itemType = :type AND itemId = :itemId)")
    fun isFavorite(userId: Long, type: FavoriteType, itemId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND itemType = :type AND itemId = :itemId")
    suspend fun removeFavorite(userId: Long, type: FavoriteType, itemId: Long)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsByUser(userId: Long): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadCount(userId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: Long)
}
