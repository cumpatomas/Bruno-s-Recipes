package com.cumpatomas.brunosrecipes.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.databinding.HistoryListFragmentBinding
import com.cumpatomas.brunosrecipes.domain.SearchRecipesUseCase
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.ui.adapter.HistoryListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {
    private var _binding: HistoryListFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = HistoryListAdapter()
    private val viewModel: HistoryViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initCollectors()
        initListeners()

    }


    private fun initListeners() {
        adapter.onItemClickListener = { id ->
            val action = HistoryFragmentDirections.actionHistorialFragmentToRecipeFragment(id)
            findNavController().navigate(action)
        }
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recipesCooked.collectLatest { recipesList ->
                        setAdapterData(recipesList)
                    }
                }
            }
        }
    }

    private fun setAdapterData(recipesList: List<RecipesModel>) {
        adapter.setList(recipesList)
        if (recipesList.size == 1)
            binding.tvHistoryTitle.text = resources.getString(R.string.history_title, recipesList.size.toString())
        else
            binding.tvHistoryTitle.text = resources.getString(R.string.history_title2, recipesList.size.toString())
    }


    private fun initRecyclerView() {
        val recyclerView = binding.rvHistoryList// encontramos el Recycler del Main LAYOUT xml
        recyclerView.layoutManager =
            LinearLayoutManager(context) // si cambiamos el Manager aqui podriamos hacer listados de GRID u otro tipo! Investigar!
        recyclerView.adapter = this.adapter
    }


}