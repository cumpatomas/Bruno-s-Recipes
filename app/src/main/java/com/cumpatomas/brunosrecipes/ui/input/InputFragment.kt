package com.cumpatomas.brunosrecipes.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.databinding.InputFragmentBinding
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.ui.adapter.FilteredListAdapter
import com.cumpatomas.brunosrecipes.ui.recipeslist.RecipesListViewState
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class InputFragment : Fragment() {

    private val viewModel: InputFragmentViewModel by viewModels()
    private var _binding: InputFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = FilteredListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InputFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initCollectors()
        initListeners()
        setLottieAnimations()
    }

    private fun initListeners() {

        binding.inputChipGroup.setOnCheckedStateChangeListener { group, _ ->

            (0 until group.childCount).forEach { index ->
                val chip = group.getChildAt(index) as Chip
                if (chip.isChecked)
                    viewModel.addIngredientSelected(chip.text.toString())
                else
                    viewModel.clearIngredient(chip.text.toString())
            }
            viewModel.searchList()
        }

        adapter.onItemClickListener = { id ->
            /*val action = InputFragmentDirections.actionInputFragmentToRecipeFragment(id)
            findNavController().navigate(action)*/
        }

        binding.chipEraseFilters.setOnClickListener {

            (0 until binding.inputChipGroup.childCount).forEach { index ->
                val chip = binding.inputChipGroup.getChildAt(index) as Chip
                if (chip.isChecked) {
                    chip.isChecked = false
                }
            }
            viewModel.clearFilteredRecipesList()
        }
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.ingredientsList.collectLatest { allIngredients ->
                        launch {
                            viewModel.selIngredientsFlow.collectLatest { selectedIngredients ->
                                createChips(allIngredients, selectedIngredients)
                            }
                        }
                    }
                }
                launch {
                    viewModel.inputRecipesList.collectLatest { filteredRecipesList ->

                        updateList(filteredRecipesList)
                    }
                }

                launch {
                    viewModel.viewState.collectLatest { viewState ->
                        updateUI(viewState)
                    }
                }
            }
        }
    }

    private fun updateUI(viewState: RecipesListViewState) {
        binding.progressBar2.isVisible = viewState.loading
        binding.chipEraseFilters.isGone = viewState.loading
    }

    private fun updateList(inputList: List<RecipesModel>) {
        adapter.setList(inputList)

    }

    private fun createChips(ingredients: List<String>, selectedIngredients: Set<String>) {
        binding.inputChipGroup.removeAllViews()

        ingredients.forEach { ingredients ->
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                R.style.Colors_Widget_MaterialComponents_Chip_Choice
            )
            val chip = Chip(requireContext())
            chip.setChipDrawable(chipDrawable)
            chip.text = ingredients
            if (chip.text in selectedIngredients) {
                chip.isChecked = true
            }
            binding.inputChipGroup.addView(chip)
        }
    }

    private fun initRecyclerView() {
        val recyclerView =
            binding.filteredListRecycler// encontramos el Recycler del xml relacionado al fragment
        recyclerView.layoutManager =
            LinearLayoutManager(context) // aca en contexto como no es el Main ponemos context
        recyclerView.adapter = this.adapter
    }

    private fun setLottieAnimations() {
        binding.progressBar2.setAnimation(R.raw.loading_white)
        binding.progressBar2.repeatMode = LottieDrawable.RESTART
        binding.progressBar2.bringToFront()
    }
}
