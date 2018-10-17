package hemendra.books.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import hemendra.books.R
import hemendra.books.data.Book
import hemendra.books.presenter.PresenterFactory
import hemendra.books.view.search.SearchFragment
import hemendra.books.view.listeners.IBooksView
import hemendra.books.view.listeners.OnBookItemClickListener

class BooksActivity : AppCompatActivity(), IBooksView {

    companion object {
        const val SEARCH_FRAGMENT = "search"
        const val DETAILS_FRAGMENT = "details"
    }

    private val presenterFactory = PresenterFactory()

    private val searchPresenter = presenterFactory.getSearchPresenter(this)
    private val imagePresenter = presenterFactory.getImagePresenter()

    private val searchFragment = SearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        searchFragment.setSearchPresenter(searchPresenter)
        searchFragment.setImagePresenter(imagePresenter)

        searchFragment.setOnBookItemClickListener(onBookItemClickListener)

        showSearchFragment()
    }

    /**
     * Take the action bar from fragment and attach it to this activity
     */
    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        invalidateOptionsMenu()
    }

    /**
     * refresh the action bar
     * @param menu The menu to inflate
     * @return Return FALSE because we want to handle search-view implementation on
     * fragment itself.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        setupActionBar()
        return false
    }

    /**
     * Add logo to action bar
     */
    private fun setupActionBar() {
        supportActionBar?.setLogo(R.drawable.logo_40)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 1)
            supportFragmentManager.popBackStackImmediate()
        else if(!searchFragment.onBackPressed())
            finish()
    }

    override fun onDestroy() {
        searchPresenter.destroy()
        imagePresenter.destroy()
        searchFragment.destroy()
        super.onDestroy()
    }

    private fun showSearchFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder, searchFragment, SEARCH_FRAGMENT)
        transaction.addToBackStack(SEARCH_FRAGMENT)
        transaction.commitAllowingStateLoss()
    }

    private val onBookItemClickListener = object : OnBookItemClickListener {
        override fun onItemClick(book: Book) {

        }
    }

    override fun onSearchResults(results: ArrayList<Book>) {
        searchFragment.onSearchResults(results)
    }

    override fun onSearchFailed(reason: String) {
        searchFragment.onSearchFailed(reason)
    }
}
