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
import com.cjayme.movielistapplication.controllers.MovieController
import com.cjayme.movielistapplication.databinding.FragmentGenreBinding
import com.cjayme.movielistapplication.data.Result
import com.cjayme.movielistapplication.presentations.adapter.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GenreFragment : Fragment() {

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genreViewModel =
            ViewModelProvider(this)[GenreViewModel::class.java]

        val tv: TextView = _binding!!.tvBack
        val genre = args.genre

        tv.text = genre
        tv.setOnClickListener {
            val action = GenreFragmentDirections.actionNavigationGenreToNavigationHome()
            Navigation.findNavController(view).navigate(action)

        }

        binding.apply {
            genreViewModel.movies.observe(viewLifecycleOwner) { result ->
                val res = result.data?.results
                setupGenreList(getMoviesByGenre(res))
            }
        }

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
}