package com.ho8278.marble.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ho8278.core.error.stable
import com.ho8278.core.flowbinding.textChanges
import com.ho8278.marble.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()

    private val adapter by lazy {
        MarbleCharacterAdapter { viewModel.onSelectCard(it.characterId) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            binding.editText.textChanges()
                .debounce(300L)
                .stable()
                .collect { viewModel.onTextChanges(it) }
        }

        lifecycleScope.launch {
            viewModel.itemList
                .stable()
                .collect { adapter.submitList(it) }
        }

        lifecycleScope.launch {
            viewModel.isLoading
                .stable()
                .collect {
                    binding.progress.visibility = if (it) View.VISIBLE else View.GONE
                }
        }
    }

    companion object {
        const val TAG = "search"

        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}