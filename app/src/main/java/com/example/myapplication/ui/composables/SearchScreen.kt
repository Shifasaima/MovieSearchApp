package com.example.myapplication.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplication.model.Movie
import com.example.myapplication.ui.theme.MovieViewModel
import java.lang.reflect.Modifier

@Composable
fun SearchScreen(viewModel: MovieViewModel, navController: NavController) {
    val searchQuery = remember {
        mutableStateOf("")
    }
    val searchResults by viewModel.searchResults.collectAsState()
    val listState = rememberLazyListState()

    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                viewModel.searchMovies(it)
                Log.d("results:", searchQuery.value)
            },
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            label = { Text(text = "Search")}
        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
        LazyColumn(state = listState) {
            items(searchResults.size) {idx->
                MovieItem(searchResults[idx]) {
                    navController.navigate(
                        "details/${searchResults[idx].imdbId}"
                    )
                }
            }
            item {
                if(searchResults.isNotEmpty()) {
                    LaunchedEffect(key1 = searchResults) {
                        listState.layoutInfo.visibleItemsInfo.
                        lastOrNull()?.let { visibleItem ->
                            if (visibleItem.index == searchResults.size -1) {
                                viewModel.loadNextPage(searchQuery.value)
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Row (
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        AsyncImage(model = movie.poster, contentDescription = null,
            modifier = androidx.compose.ui.Modifier.size(64.dp))
        Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
        Column {
            Text(text = movie.title, style = MaterialTheme.typography.h6)
            Text(text = movie.year, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun DetailsScreen(viewModel: MovieViewModel) {
    val movieDetail by viewModel.movieDetail.collectAsState()

    movieDetail?.let {
        detail ->
        Column(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AsyncImage(model = detail.poster,
                contentDescription = null,
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .height(400.dp))
        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
        Text(text = detail.title, style = MaterialTheme.typography.h4)
        Text(text = "Year: ${detail.year}", style = MaterialTheme.typography.body1)
        Text(text = "Rated: ${detail.rated}", style = MaterialTheme.typography.body1)
        }
    }
}