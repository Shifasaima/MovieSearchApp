package com.example.myapplication.ui.theme

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.internal.MovieRepository
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MovieDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    fun searchMovies(query: String) {
        viewModelScope.launch {
            val results = repository.searchMovies(
                query = query,
                page = _currentPage.value
            )

            Log.d("results: ", "$results")

            _searchResults.value = results
        }
    }

    fun loadNextPage(query: String) {
        viewModelScope.launch {
            _currentPage.value += 1
            val results = repository.searchMovies(
                query = query,
                page = _currentPage.value
            )
            _searchResults.value = results
        }
    }

    fun getMovieDetails(imdbID: String) {
        viewModelScope.launch {
            val detail = repository.getMovieDetails(imdbId = imdbID)
            _movieDetail.value = detail
        }
    }
}