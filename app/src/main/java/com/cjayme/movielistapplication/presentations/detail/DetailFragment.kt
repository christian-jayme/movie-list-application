package com.cjayme.movielistapplication.presentations.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.cjayme.movielistapplication.data.Result
import com.cjayme.movielistapplication.databinding.FragmentDetailBinding
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

        val textView: TextView = binding.textDetail

        detailViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.apply {
            detailViewModel.movies.observe(viewLifecycleOwner) { result ->
                val res = result.data?.results
                populateDetails(getDetails(res))
            }
        }
        return root
    }

    private fun populateDetails(details: Result?) {
        binding.textDetail.text = details?.trackId.toString()
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