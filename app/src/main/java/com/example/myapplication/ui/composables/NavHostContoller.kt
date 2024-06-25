package com.example.myapplication.ui.composables

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.theme.MovieViewModel

@Composable
fun NavHostController() {
    val navController = rememberNavController()
   NavHost(navController = navController, startDestination = "search") {
       composable("search") {
           val movieViewModel: MovieViewModel = hiltViewModel()
           SearchScreen(movieViewModel, navController)
       }

       composable(
           "details/{imdbID}",
           arguments = listOf(navArgument("imdbID")
           {type = NavType.StringType})
       ) {backStackEntry ->
           val imdbID = backStackEntry.arguments?.getString(
               "imdbID"
           ) ?: ""
           val movieViewModel: MovieViewModel = hiltViewModel()
           movieViewModel.getMovieDetails(imdbID)
           DetailsScreen(movieViewModel)
       }
   }
}
