package com.cjayme.movielistapplication.presentations.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.model.Result
import com.cjayme.movielistapplication.databinding.FragmentDetailBinding
import com.cjayme.movielistapplication.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val trackId = args.trackId
        val detailViewModel =
            ViewModelProvider(this)[DetailViewModel::class.java]

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            detailViewModel.movies.observe(viewLifecycleOwner) { result ->
                val list = result.data?.results
                val res = getDetails(list, trackId)

                populateDetails(res)

                toolbar.setNavigationOnClickListener {
                    val action = DetailFragmentDirections.actionNavigationDetailToNavigationHome()
                    Navigation.findNavController(root).navigate(action)
                }

                btnAddToFavorite.setOnClickListener {
                    updateFavoriteButton(true, res)
                }

                btnAddedToFavorite.setOnClickListener {
                    updateFavoriteButton(false, res)
                }
            }
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackId = args.trackId
        setInitialFavoriteButton(trackId.toString())
    }

    private fun updateFavoriteButton(isAdded: Boolean, res: Result?) {
        when (isAdded) {
            true -> {
                Utils.saveFavoriteToList(
                    requireContext(),
                    "track_id",
                    res!!.trackId.toString()
                )
                binding.btnAddToFavorite.visibility = View.GONE
                binding.btnAddedToFavorite.visibility = View.VISIBLE
            }
            false -> {
                Utils.removeFavoriteFromList(
                    requireContext(),
                    "track_id",
                    res!!.trackId.toString()
                )
                binding.btnAddToFavorite.visibility = View.VISIBLE
                binding.btnAddedToFavorite.visibility = View.GONE
            }
        }
    }

    private fun setInitialFavoriteButton(trackId: String) {
        when (Utils.isDataExistsInFavoriteList(
            requireContext(),
            "track_id",
            trackId
        )) {
            true -> {
                binding.btnAddToFavorite.visibility = View.GONE
                binding.btnAddedToFavorite.visibility = View.VISIBLE
            }
            false -> {
                binding.btnAddToFavorite.visibility = View.VISIBLE
                binding.btnAddedToFavorite.visibility = View.GONE
            }
        }
    }

    private fun populateDetails(details: Result?) {
        details.let {
            binding.tvFullDescription.text = it?.longDescription
            binding.tvGenre.text = it?.primaryGenreName
            binding.tvArtist.text = it?.artistName
            binding.tvPrice.text = buildString {
                append(it?.trackPrice)
                append(" • ")
                append(it?.currency)
            }
            binding.tvItemTitle.text = it?.trackName
            binding.tvReleaseDate.text = it?.releaseDate?.let { it1 -> Utils.convertDate(it1) }

        }
        Glide.with(requireContext())
            .load(details?.artworkUrl100)
            .placeholder(R.drawable.woman)
            .into(binding.ivItemImage)
    }

    private fun getDetails(res: List<Result>?, trackId: Int): Result? {
        return res?.find { it.trackId == trackId }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}