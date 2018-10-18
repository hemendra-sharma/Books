package hemendra.books.presenter

import hemendra.books.data.Book
import hemendra.books.model.DataSourceFactory
import hemendra.books.model.listeners.IDataSource
import hemendra.books.presenter.listeners.IDataSourceListener
import hemendra.books.presenter.listeners.ISearchPresenter
import hemendra.books.view.listeners.IBooksView

class SearchPresenter private constructor(private var activity: IBooksView?) :
        ISearchPresenter, IDataSourceListener {

    companion object {

        var searchPresenter: SearchPresenter? = null

        fun getInstance(activity: IBooksView): SearchPresenter {
            if(searchPresenter == null) searchPresenter = SearchPresenter(activity)
            return searchPresenter!!
        }

    }

    private val dataSourceFactory = DataSourceFactory()

    private var dataSource: IDataSource? = null

    init {
        dataSource = dataSourceFactory.getDataSource(this)
    }

    override fun performSearch(query: String, apiKey: String, pageNumber: Int) {
        dataSource?.searchBooks(query, apiKey, pageNumber)
    }

    override fun isSearching(): Boolean {
        return dataSource?.isSearching() ?: false
    }

    override fun onSearchResults(results: ArrayList<Book>) {
        activity?.onSearchResults(results)
    }

    override fun onSearchFailed(reason: String) {
        activity?.onSearchFailed(reason)
    }

    override fun abort() {
        dataSource?.abort()
    }

    override fun destroy() {
        abort()
        dataSource?.destroy()

        dataSource = null
        searchPresenter = null
        activity = null
    }
}