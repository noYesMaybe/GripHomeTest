package com.example.griphometest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.griphometest.adapters.PokemonListAdapter
import com.example.griphometest.helper.ImageLoader
import com.example.griphometest.helper.PokemonParser
import com.example.griphometest.helper.Utils
import com.example.griphometest.model.Pokemon
import com.example.griphometest.repository.PokemonRepository
import com.example.griphometest.service.RetrofitClient
import com.example.griphometest.viewmodel.PokemonViewModel

class PokemonListFragmentKotlin : Fragment() {

    private var pokemonViewModel: PokemonViewModel? = null
    private var pokemonListAdapter: PokemonListAdapter? = null
    private var fragmentNavigation: IFragmentNavigation? = null
    private var recyclerView: RecyclerView? = null
    private var scrollView: NestedScrollView? = null
    private var loadingBar: ProgressBar? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootFragmentView = inflater.inflate(R.layout.pokemon_list_fragment, container, false)

        recyclerView = rootFragmentView.findViewById<View>(R.id.listView) as RecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(requireActivity(), 2)
        scrollView = rootFragmentView.findViewById(R.id.idNestedScrollView)
        loadingBar = rootFragmentView.findViewById(R.id.idLoadingBar)
        setScroll()

        return rootFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init dependencies
        val repository = PokemonRepository(RetrofitClient.getInstance().pokemonService, null,
                PokemonParser.getInstance(), ImageLoader.getInstance(),
                PokemonApplication.getInstance().executorService)
        val factory = PokemonViewModel.Factory(repository)
        pokemonViewModel = ViewModelProvider(requireActivity(), factory).get(PokemonViewModel::class.java)
        pokemonViewModel!!.pokemonList.observe(viewLifecycleOwner, { pokemonList: List<Pokemon?> ->
            Log.d(Utils.LOG_TAG, "View notified, list size: " + pokemonList.size)
            if (pokemonList.isEmpty()) {
                return@observe
            }
            loadingBar!!.visibility = View.INVISIBLE
            pokemonListAdapter = PokemonListAdapter(pokemonList)
            recyclerView!!.adapter = pokemonListAdapter

            // set listener
            pokemonListAdapter!!.setItemClickListener { pokemon: Pokemon ->
                Log.d(Utils.LOG_TAG, String.format("Clicked on pokemon: %s (Id: %s)",
                        pokemon.name, pokemon.id))
                pokemonViewModel!!.selectPokemon(pokemon)
                goToPokemonDetails()
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        fragmentNavigation = try {
            context as IFragmentNavigation
        } catch (ex: ClassCastException) {
            throw ClassCastException("$context must implement IFragmentNavigation!")
        }
    }

    private fun setScroll() {
        // adding on scroll change listener method for our nested scroll view.
        scrollView!!.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener {
                v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll to bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                loadingBar!!.visibility = View.VISIBLE
                pokemonViewModel!!.loadPokemons()
            }
        })
    }

    private fun goToPokemonDetails() {
        if (fragmentNavigation != null) {
            fragmentNavigation!!.navigateToFragment(FragmentType.Detail)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PokemonListFragmentKotlin()
    }
}