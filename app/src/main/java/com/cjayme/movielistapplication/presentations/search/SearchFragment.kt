package com.cjayme.movielistapplication.presentations.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cjayme.movielistapplication.data.model.Result
import com.cjayme.movielistapplication.databinding.FragmentSearchBinding
import com.cjayme.movielistapplication.presentations.adapter.SearchAdapter
import com.cjayme.movielistapplication.presentations.adapter.SearchAdapter.OnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), OnClickListener {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchViewModel =
            ViewModelProvider(this)[SearchViewModel::class.java]

        binding.apply {
            searchViewModel.movies.observe(viewLifecycleOwner) { result ->
                val res = result.data?.results
                setupSearchedList(res)
            }
        }
    }

    private fun setupSearchedList(movies: List<Result>?) {
        val recycler = binding.rvSearchItems
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = SearchAdapter(requireContext(), movies, this)
        val adapter = recycler.adapter as SearchAdapter
        binding.searchInput.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.filter(s.toString())
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, itemView: View) {
        val action = SearchFragmentDirections.actionNavigationSearchToNavigationDetail(position)
        Navigation.findNavController(itemView).navigate(action)
    }
}