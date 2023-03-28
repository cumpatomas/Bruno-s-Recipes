package com.cumpatomas.brunosrecipes.ui.recipeslist

import android.graphics.Typeface.NORMAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.font.Typeface
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.core.ex.hideKeyboard
import com.cumpatomas.brunosrecipes.databinding.RecipesListFragmentBinding
import com.cumpatomas.brunosrecipes.domain.model.RecipesModel
import com.cumpatomas.brunosrecipes.manualdi.ApplicationModule.applicationContext
import com.cumpatomas.brunosrecipes.ui.adapter.RecipeListAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecipesListFragment : Fragment() {

    private var _binding: RecipesListFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipesListViewModel by viewModels()
    private val adapter = RecipeListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipesListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        initCollectors()
        initListeners()
        setLottieAnimations()
    }

    private fun setLottieAnimations() {
        binding.progressBar.setAnimation(R.raw.loading)
        binding.progressBar.repeatMode = LottieDrawable.RESTART
        binding.progressBar.bringToFront()
    }

    private fun initListeners() {

        viewModel.clearCategoriesSelected() // We clear the chips filter

        //listener para la barra de busqueda
        binding.etSearchRecipe.addTextChangedListener { input ->
            // destildamos los Chips cuando buscamos con la barra
            viewModel.searchList(input.toString())
        }

        adapter.onItemClickListener = { id ->
            val action = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(id)
            findNavController().navigate(action)
        }

        binding.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            hideKeyboard() // escondemos teclado
            binding.etSearchRecipe.clearFocus()// sacamos focus del test input
            viewModel.clearCategoriesSelected()
            (0 until group.childCount).forEach { index ->
                val chip = group.getChildAt(index) as Chip
                if (chip.isChecked) {
                    viewModel.addCategoriesSelected(chip.text.toString())
                }
            } //clear the Input Text when we filter by chips
            viewModel.searchList()
        }
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recipesList.collectLatest { list ->
                        updateList(list)
                    }
                }
                launch {
                    viewModel.categoryList.collectLatest { categories ->
                        createChips(categories)
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
        binding.progressBar.isVisible = viewState.loading
    }

    private fun updateList(list: List<RecipesModel>) {
        adapter.setList(list)
    }

    private fun initRecyclerView() {
        val recyclerView =
            binding.recipesRecyclerView// encontramos el Recycler del xml relacionado al fragment
        recyclerView.layoutManager =
            LinearLayoutManager(context) // aca en contexto como no es el Main ponemos context
        recyclerView.adapter = this.adapter
    }


    private fun createChips(categories: List<String>) {
        binding.chipGroup.removeAllViews()
        categories.forEach { category ->
            val chipDrawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                R.style.Colors_Widget_MaterialComponents_Chip_Choice
            )
            val chip = Chip(requireContext())
            chip.setChipDrawable(chipDrawable)
            chip.textSize = 16.0f
            chip.text = category
            chip.typeface = android.graphics.Typeface.create(ResourcesCompat.getFont(applicationContext,R.font.marlin_sans),
                NORMAL)
            binding.chipGroup.addView(chip)
        }
    }
}