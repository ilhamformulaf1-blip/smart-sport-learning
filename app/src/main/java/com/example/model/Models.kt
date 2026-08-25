package com.example.model

enum class UserRole {
    SISWA,
    GURU,
    ADMIN
}

enum class SportCategory(val displayName: String, val iconCode: String, val badgeTitle: String) {
    SEPAK_BOLA("Sepak Bola", "sports_soccer", "Football Expert"),
    BOLA_BASKET("Bola Basket", "sports_basketball", "Basketball Player"),
    BOLA_VOLI("Bola Voli", "sports_volleyball", "Volleyball Expert"),
    ATLETIK("Atletik", "directions_run", "Athletics Master"),
    PENANGANAN_CEDERA("Penanganan Cedera", "medical_services", "First Aid Hero");

    companion object {
        fun fromString(value: String): SportCategory {
            return entries.find { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) } ?: SEPAK_BOLA
        }
    }
}

enum class LearningStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

enum class FavoriteType {
    MATERIAL,
    VIDEO
}

enum class NotificationType {
    INFO,
    BADGE_UNLOCKED,
    QUIZ_AVAILABLE,
    PROGRESS_MILESTONE,
    REMINDER
}
