package com.cjayme.movielistapplication.presentations.home

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.controllers.MovieController
import com.cjayme.movielistapplication.databinding.FragmentFavoriteBinding
import com.cjayme.movielistapplication.databinding.FragmentHomeBinding
import com.cjayme.movielistapplication.listeners.OnMovieResultResponse
import com.cjayme.movielistapplication.models.Result
import com.cjayme.movielistapplication.presentations.adapter.CarouselAdapter
import com.cjayme.movielistapplication.presentations.adapter.GenreListAdapter
import com.cjayme.movielistapplication.presentations.adapter.MovieListAdapter
import com.cjayme.movielistapplication.utils.Utils
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.CarouselStrategy
import com.google.android.material.carousel.HeroCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMovieResultResponse  {

    @Inject
    lateinit var movieController: MovieController

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            setupListeners()
        }
    }

    private suspend fun setupListeners() {
        // Set the listener to receive movie results
        movieController.setMovieResultListener(this)
        // Call function to fetch movies
        movieController.getMovies("star", "au", "movie")
    }


    private fun setupCarousel(movies: List<Result>?) {
        val images = listOf(R.drawable._5, R.drawable._5, R.drawable._5, R.drawable._5)
        val rvCarousel = binding.rvCarousel
        rvCarousel.layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        rvCarousel.layoutManager
        rvCarousel.adapter = CarouselAdapter(images)
    }



    private fun setupGenreList(movies: List<Result>?) {

        val uniqueGenres: List<String> = movies?.map {
            it.primaryGenreName
        }?.distinct() ?: emptyList()

        val genreListAdapter = GenreListAdapter(
            movies,
            uniqueGenres,
            requireContext()
        )

        val rvGenre = binding.rvGenreGroup
        rvGenre.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false)
        rvGenre.adapter = genreListAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(movies: List<Result>?) {
        setupCarousel(movies)
        setupGenreList(movies)
    }

    override fun onError(errorMessage: Int) {
        Utils.showErrorDialog(requireContext(), errorMessage)
    }
}
