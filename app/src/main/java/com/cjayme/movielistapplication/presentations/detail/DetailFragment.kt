package com.cjayme.movielistapplication.presentations.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.cjayme.movielistapplication.R
import com.cjayme.movielistapplication.data.Result
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

        val detailViewModel =
            ViewModelProvider(this)[DetailViewModel::class.java]

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            detailViewModel.movies.observe(viewLifecycleOwner) { result ->
                val res = result.data?.results
                populateDetails(getDetails(res))
            }
        }
        return root
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

    private fun getDetails(res: List<Result>?): Result? {
        val trackId = args.trackId

        return res?.find { it.trackId == trackId }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}