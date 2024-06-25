package com.example.myapplication.internal

import android.util.Log
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MovieDetail
import javax.inject.Inject

interface MovieRepository {

    suspend fun searchMovies(query: String, page: Int): List<Movie>
    suspend fun getMovieDetails(imdbId: String): MovieDetail
}

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
): MovieRepository {

    private  val apiKey = "bb66c938"

    override suspend fun searchMovies(query: String, page: Int): List<Movie> {
        Log.d("searchMovie", query + "" + page)
        val response = apiService.searchMovies(
            apiKey = apiKey,
            searchString = query,
            page = page
        )
        return if (response.response == "True") {
            response.search
        } else {
            emptyList()
        }
    }

    override suspend fun getMovieDetails(imdbId: String): MovieDetail {
        return apiService.getMovieDetails(
            apiKey = apiKey,
            imdbID = imdbId
        )
    }

    //https://www.omdbapi.com/?i=tt3896198&apikey=bb66c938
    //https://www.omdbapi.com/?apikey=bb66c938&s=avenger&page=1

}