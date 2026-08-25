package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.model.UserRole
import com.example.ui.components.AppHeader
import com.example.ui.components.VideoPlayerDialog
import com.example.ui.screens.admin.TeacherAdminDashboard
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.material.MaterialDetailScreen
import com.example.ui.screens.material.MaterialListScreen
import com.example.ui.screens.progress.BadgeScreen
import com.example.ui.screens.progress.FavoritesScreen
import com.example.ui.screens.progress.LeaderboardScreen
import com.example.ui.screens.progress.NotificationScreen
import com.example.ui.screens.progress.ProfileScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.quiz.ActiveQuizScreen
import com.example.ui.screens.quiz.QuizHubScreen
import com.example.ui.screens.video.VideoListScreen
import com.example.ui.theme.SkyBluePrimary
import com.example.viewmodel.SportViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Beranda", Icons.Default.Home)
    object Materials : Screen("materials", "Materi", Icons.Default.MenuBook)
    object MaterialDetail : Screen("material_detail/{materialId}", "Detail Materi", Icons.Default.MenuBook) {
        fun createRoute(materialId: Long) = "material_detail/$materialId"
    }
    object Videos : Screen("videos", "Video", Icons.Default.VideoLibrary)
    object QuizHub : Screen("quiz_hub", "Kuis", Icons.Default.Quiz)
    object ActiveQuiz : Screen("active_quiz", "Kuis Aktif", Icons.Default.Quiz)
    object Progress : Screen("progress", "Progress", Icons.Default.CheckCircle)
    object Badges : Screen("badges", "Lencana", Icons.Default.EmojiEvents)
    object Leaderboard : Screen("leaderboard", "Peringkat", Icons.Default.Leaderboard)
    object Favorites : Screen("favorites", "Favorit", Icons.Default.Bookmark)
    object Notifications : Screen("notifications", "Notifikasi", Icons.Default.Home)
    object Profile : Screen("profile", "Profil", Icons.Default.Person)
    object TeacherAdmin : Screen("teacher_admin", "Kelola", Icons.Default.School)
    object Auth : Screen("auth", "Masuk", Icons.Default.Person)
}

@Composable
fun AppNavigation(
    viewModel: SportViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val activeVideo by viewModel.activeVideo.collectAsState()
    val isVideoPlaying by viewModel.isVideoPlaying.collectAsState()
    val unreadNotifs by viewModel.getUnreadNotificationsCount()?.collectAsState(initial = 0) ?: androidx.compose.runtime.remember { androidx.compose.runtime.mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = if (currentUser?.role == UserRole.GURU || currentUser?.role == UserRole.ADMIN) {
        listOf(
            Screen.Home,
            Screen.Materials,
            Screen.Videos,
            Screen.TeacherAdmin,
            Screen.Profile
        )
    } else {
        listOf(
            Screen.Home,
            Screen.Materials,
            Screen.Videos,
            Screen.QuizHub,
            Screen.Profile
        )
    }

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Materials.route,
        Screen.Videos.route,
        Screen.QuizHub.route,
        Screen.Progress.route,
        Screen.Badges.route,
        Screen.Leaderboard.route,
        Screen.Profile.route,
        Screen.TeacherAdmin.route
    )

    val showHeader = currentRoute in listOf(
        Screen.Home.route,
        Screen.Materials.route,
        Screen.Videos.route,
        Screen.QuizHub.route,
        Screen.Progress.route,
        Screen.Badges.route,
        Screen.Leaderboard.route,
        Screen.Profile.route,
        Screen.TeacherAdmin.route
    )

    Scaffold(
        topBar = {
            if (showHeader) {
                AppHeader(
                    currentUser = currentUser,
                    unreadNotifications = unreadNotifs,
                    onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
                    onFavoritesClick = { navController.navigate(Screen.Favorites.route) },
                    onRoleBadgeClick = { navController.navigate(Screen.Profile.route) }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    bottomNavItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = { Text(screen.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SkyBluePrimary,
                                selectedTextColor = SkyBluePrimary,
                                indicatorColor = SkyBluePrimary.copy(alpha = 0.15f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToMaterials = { navController.navigate(Screen.Materials.route) },
                        onNavigateToMaterialDetail = { id -> navController.navigate(Screen.MaterialDetail.createRoute(id)) },
                        onNavigateToVideos = { navController.navigate(Screen.Videos.route) },
                        onNavigateToQuiz = { matId ->
                            if (matId != null) {
                                val mat = viewModel.allMaterials.value.find { it.id == matId }
                                if (mat != null) viewModel.startQuizForMaterial(mat)
                                navController.navigate(Screen.ActiveQuiz.route)
                            } else {
                                navController.navigate(Screen.QuizHub.route)
                            }
                        },
                        onNavigateToProgress = { navController.navigate(Screen.Progress.route) },
                        onNavigateToBadges = { navController.navigate(Screen.Badges.route) },
                        onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) }
                    )
                }

                composable(Screen.Materials.route) {
                    MaterialListScreen(
                        viewModel = viewModel,
                        onMaterialClick = { id -> navController.navigate(Screen.MaterialDetail.createRoute(id)) }
                    )
                }

                composable(
                    route = Screen.MaterialDetail.route,
                    arguments = listOf(navArgument("materialId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val matId = backStackEntry.arguments?.getLong("materialId") ?: 0L
                    MaterialDetailScreen(
                        materialId = matId,
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() },
                        onStartQuiz = { mat ->
                            viewModel.startQuizForMaterial(mat)
                            navController.navigate(Screen.ActiveQuiz.route)
                        }
                    )
                }

                composable(Screen.Videos.route) {
                    VideoListScreen(viewModel = viewModel)
                }

                composable(Screen.QuizHub.route) {
                    QuizHubScreen(
                        viewModel = viewModel,
                        onStartCategoryQuiz = { cat ->
                            viewModel.startQuizForCategory(cat)
                            navController.navigate(Screen.ActiveQuiz.route)
                        }
                    )
                }

                composable(Screen.ActiveQuiz.route) {
                    ActiveQuizScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Screen.Progress.route) {
                    ProgressScreen(
                        viewModel = viewModel,
                        onNavigateToMaterial = { id -> navController.navigate(Screen.MaterialDetail.createRoute(id)) }
                    )
                }

                composable(Screen.Badges.route) {
                    BadgeScreen(viewModel = viewModel)
                }

                composable(Screen.Leaderboard.route) {
                    LeaderboardScreen(viewModel = viewModel)
                }

                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = viewModel,
                        onMaterialClick = { id -> navController.navigate(Screen.MaterialDetail.createRoute(id)) }
                    )
                }

                composable(Screen.Notifications.route) {
                    NotificationScreen(viewModel = viewModel)
                }

                composable(Screen.Profile.route) {
                    ProfileScreen(
                        viewModel = viewModel,
                        onRoleSwitched = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }

                composable(Screen.TeacherAdmin.route) {
                    TeacherAdminDashboard(viewModel = viewModel)
                }

                composable(Screen.Auth.route) {
                    AuthScreen(
                        viewModel = viewModel,
                        onAuthSuccess = { navController.navigate(Screen.Home.route) { popUpTo(0) } }
                    )
                }
            }

            // Global Video Player Dialog
            activeVideo?.let { vid ->
                VideoPlayerDialog(
                    video = vid,
                    isPlaying = isVideoPlaying,
                    onTogglePlay = { viewModel.toggleVideoPlay() },
                    onNext = { viewModel.nextVideo() },
                    onPrev = { viewModel.prevVideo() },
                    onClose = { viewModel.closeVideoPlayer() }
                )
            }
        }
    }
}
