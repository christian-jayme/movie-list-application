package com.cjayme.movielistapplication.presentations.genre

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.controllers.MovieController
import com.cjayme.movielistapplication.databinding.FragmentGenreBinding
import com.cjayme.movielistapplication.listeners.OnMovieResultResponse
import com.cjayme.movielistapplication.models.Result
import com.cjayme.movielistapplication.presentations.adapter.GenreListAdapter
import com.cjayme.movielistapplication.presentations.adapter.MovieListAdapter
import com.cjayme.movielistapplication.presentations.home.HomeFragmentDirections
import com.cjayme.movielistapplication.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class GenreFragment : Fragment(), OnMovieResultResponse {

    @Inject
    lateinit var movieController: MovieController

    private var _binding: FragmentGenreBinding? = null

    private val binding get() = _binding!!

    private val args: GenreFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val genreViewModel =
            ViewModelProvider(this)[GenreViewModel::class.java]

        _binding = FragmentGenreBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textGenre
//        favoriteViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tv: TextView = _binding!!.tvBack
        val genre = args.genre

        CoroutineScope(Dispatchers.Main).launch {
            setupListeners()
        }

        tv.text = genre
        tv.setOnClickListener {
            val action = GenreFragmentDirections.actionNavigationGenreToNavigationHome()
            Navigation.findNavController(view).navigate(action)

        }
    }

    private suspend fun setupListeners() {
        // Set the listener to receive movie results
        movieController.setMovieResultListener(this)
        // Call function to fetch movies
        movieController.getMovies("star", "au", "movie")
    }

    private fun setupGenreList(movies: List<Result>?) {
        val recycler = binding.rvMovies
        recycler.layoutManager = GridLayoutManager(requireContext(), 3)
        recycler.adapter = MovieListAdapter(movies, requireContext())
    }
    private fun getMoviesByGenre(
        movies: List<Result>?,
    ) = movies!!.filter {
        it.primaryGenreName.contains(args.genre, ignoreCase = true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSuccess(movies: List<Result>?) {
        setupGenreList(getMoviesByGenre(movies))
    }

    override fun onError(errorMessage: Int) {
        Utils.showErrorDialog(requireContext(), errorMessage)
    }
}