package com.cjayme.movielistapplication.presentations.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cjayme.movielistapplication.data.model.Result
import com.cjayme.movielistapplication.databinding.FragmentFavoriteBinding
import com.cjayme.movielistapplication.presentations.adapter.FavoriteAdapter
import com.cjayme.movielistapplication.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteAdapter.OnClickListener {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favoriteViewModel =
            ViewModelProvider(this)[FavoriteViewModel::class.java]

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.apply {
            favoriteViewModel.movies.observe(viewLifecycleOwner) { result ->
                val list = result.data?.results
                val res = Utils.getFavoriteMovie(requireContext(),list)
                setupFavoriteList(res)
            }
        }

        return root
    }

    private fun setupFavoriteList(movies: List<Result>?) {
        val recycler = binding.rvFavoriteItems
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = FavoriteAdapter(requireContext(), movies, this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, itemView: View) {
        val action = FavoriteFragmentDirections.actionNavigationFavoriteToNavigationDetail(position)
        Navigation.findNavController(itemView).navigate(action)
    }
}