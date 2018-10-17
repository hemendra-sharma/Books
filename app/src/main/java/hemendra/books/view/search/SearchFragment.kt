package hemendra.books.view.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import hemendra.books.view.showMessage
import hemendra.books.R
import hemendra.books.data.Book
import hemendra.books.presenter.listeners.IImagePresenter
import hemendra.books.presenter.listeners.ISearchPresenter
import hemendra.books.view.listeners.OnBookItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    private var searchPresenter: ISearchPresenter? = null
    fun setSearchPresenter(presenter: ISearchPresenter) {
        this.searchPresenter = presenter
    }

    private var imagePresenter: IImagePresenter? = null
    fun setImagePresenter(presenter: IImagePresenter) {
        this.imagePresenter = presenter
    }

    private var onBookItemClickListener: OnBookItemClickListener? = null
    fun setOnBookItemClickListener(listener: OnBookItemClickListener) {
        this.onBookItemClickListener = listener
    }

    private var searchMenuItem: MenuItem? = null
    private var searchView: SearchView? = null

    private var savedView: View? = null

    private var lastSearched = ""
    private var lastPageNumber = 1

    private var loadingFirstTime = true

    private var adapter: BooksListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater ?: return
        context ?: return
        activity ?: return
        menu ?: return

        menuInflater.inflate(R.menu.main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchMenuItem = menu.findItem(R.id.action_search)
        searchView = searchMenuItem?.actionView as SearchView?
        //val settingsItem = menu.findItem(R.id.action_settings)
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView?.clearFocus()
                lastSearched = query
                lastPageNumber = 1
                performSearch(query, lastPageNumber)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean  = false
        })

        searchMenuItem?.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if(lastSearched.isNotEmpty()) {
                    lastSearched = ""
                    lastPageNumber = 1
                    performSearch(lastSearched, lastPageNumber)
                }
                return true
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        savedView?.let { return it }
        savedView = inflater.inflate(R.layout.fragment_search, container, false)
        return savedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar?.let { (activity as AppCompatActivity).setSupportActionBar(it) }

        recycler?.let {
            it.addOnScrollListener(object :
                    ContinuousScrollListener(it.layoutManager as LinearLayoutManager) {
                override fun onLoadMore() {
                    if (searchPresenter?.isSearching() == false) {
                        lastPageNumber++
                        performSearch(lastSearched, lastPageNumber)
                    }
                }
            })
        }

        if(loadingFirstTime) {
            lastSearched = ""
            lastPageNumber = 1
            performSearch(lastSearched, lastPageNumber)
        }
    }

    override fun onDestroyView() {
        context?.let { searchPresenter?.abort() }
        (activity as AppCompatActivity?)?.setSupportActionBar(null)
        (savedView?.parent as ViewGroup?)?.removeAllViews()
        super.onDestroyView()
    }

    fun destroy() {
        savedView = null
    }

    private fun performSearch(query: String, pageNumber: Int) {
        if(query.isEmpty()) {
            searchPresenter?.performSearch("*", getString(R.string.API_KEY), pageNumber)
            if(pageNumber == 1) showProgress("Listing All Books")
        } else {
            searchPresenter?.performSearch(lastSearched, getString(R.string.API_KEY), pageNumber)
            if(pageNumber == 1) showProgress("Searching for\n\n\"$query\"")
        }
    }

    fun onSearchResults(results: ArrayList<Book>) {
        hideProgress()

        if(lastPageNumber == 1) {
            adapter = BooksListAdapter(results, onBookItemClickListener, imagePresenter)
            recycler?.adapter = adapter
            Handler().postDelayed({ checkLoadMore() }, 1000)
        } else {
            if(results.size == 0)
                adapter?.endReached()
            else
                adapter?.appendData(results)
        }
    }

    fun onSearchFailed(reason: String) {
        hideProgress()
        context?.let { showMessage(it, "Search Failed. Reason: $reason") }
    }

    private fun checkLoadMore() {
        val mLayoutManager = recycler?.layoutManager as LinearLayoutManager?
        val visibleItemCount = mLayoutManager?.childCount ?: 0
        val totalItemCount = mLayoutManager?.itemCount ?: 0
        val pastVisibleItemsCount = mLayoutManager?.findFirstVisibleItemPosition() ?: -1

        if ((visibleItemCount + pastVisibleItemsCount) >= totalItemCount) {
            lastPageNumber = 2
            performSearch(lastSearched, lastPageNumber)
        }
    }

    fun onBackPressed(): Boolean {
        return if(isProgressOrErrorVisible()) {
            searchPresenter?.abort()
            hideProgress()
            true
        } else false
    }

    private fun isProgressOrErrorVisible(): Boolean {
        return rlProgress?. let { it.visibility == View.VISIBLE } ?: false
    }

    private fun showProgress(msg: String) {
        rlProgress?.visibility = View.VISIBLE
        tvProgress?.text = msg
    }

    private fun hideProgress() {
        rlProgress?.visibility = View.GONE
    }

}