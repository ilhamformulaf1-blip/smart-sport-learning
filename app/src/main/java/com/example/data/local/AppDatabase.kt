package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.model.FavoriteType
import com.example.model.LearningStatus
import com.example.model.NotificationType
import com.example.model.SportCategory
import com.example.model.UserRole

class Converters {
    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromSportCategory(value: SportCategory): String = value.name

    @TypeConverter
    fun toSportCategory(value: String): SportCategory = SportCategory.valueOf(value)

    @TypeConverter
    fun fromLearningStatus(value: LearningStatus): String = value.name

    @TypeConverter
    fun toLearningStatus(value: String): LearningStatus = LearningStatus.valueOf(value)

    @TypeConverter
    fun fromFavoriteType(value: FavoriteType): String = value.name

    @TypeConverter
    fun toFavoriteType(value: String): FavoriteType = FavoriteType.valueOf(value)

    @TypeConverter
    fun fromNotificationType(value: NotificationType): String = value.name

    @TypeConverter
    fun toNotificationType(value: String): NotificationType = NotificationType.valueOf(value)
}

@Database(
    entities = [
        UserEntity::class,
        MaterialEntity::class,
        VideoEntity::class,
        QuizQuestionEntity::class,
        QuizResultEntity::class,
        ProgressEntity::class,
        BadgeEntity::class,
        UserBadgeEntity::class,
        FavoriteEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun materialDao(): MaterialDao
    abstract fun videoDao(): VideoDao
    abstract fun quizDao(): QuizDao
    abstract fun quizResultDao(): QuizResultDao
    abstract fun progressDao(): ProgressDao
    abstract fun badgeDao(): BadgeDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_sport_learning.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
