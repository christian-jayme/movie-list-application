package com.cjayme.movielistapplication.presentations.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.Result
import com.cjayme.movielistapplication.databinding.FragmentHomeBinding
import com.cjayme.movielistapplication.presentations.adapter.CarouselAdapter
import com.cjayme.movielistapplication.presentations.adapter.GenreListAdapter
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.HeroCarouselStrategy
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), GenreListAdapter.OnClickListener  {

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

        binding.apply {
            homeViewModel.movies.observe(viewLifecycleOwner) { result ->
                val res = result.data?.results
                setupCarousel(res)
                setupGenreList(res)
            }
        }


        return root
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
            requireContext(),
            this
        )

        val rvGenre = binding.rvGenreGroup
        rvGenre.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        rvGenre.adapter = genreListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGenreClick(position: String, itemView: View) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationGenre(position)
        Navigation.findNavController(itemView).navigate(action)
    }
}
