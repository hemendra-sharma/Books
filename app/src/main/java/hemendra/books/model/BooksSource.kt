package hemendra.books.model

import android.support.annotation.VisibleForTesting
import hemendra.books.data.Book
import hemendra.books.model.listeners.BooksLoaderListener
import hemendra.books.model.listeners.IDataSource
import hemendra.books.model.listeners.IGetBooksFactory
import hemendra.books.presenter.listeners.IDataSourceListener

class BooksSource private constructor(
        @VisibleForTesting
        internal val listener: IDataSourceListener) : IDataSource, BooksLoaderListener {

    companion object {
        private var booksSource: BooksSource? = null
        fun getInstance(listener: IDataSourceListener) : BooksSource {
            if(booksSource == null) booksSource = BooksSource(listener)
            return booksSource!!
        }
    }

    @VisibleForTesting
    internal var factory: IGetBooksFactory = GetBooksFactory()

    private var getBooks : GetBooks? = null

    override fun searchBooks(query: String, apiKey: String, pageNumber: Int) {
        if(isSearching())
            return

        getBooks = factory.newInstance(this)
        getBooks?.execute(query, apiKey, pageNumber)
    }

    override fun isSearching(): Boolean {
        return getBooks?.isExecuting() ?: false
    }

    override fun onSearchResults(results: ArrayList<Book>) {
        listener.onSearchResults(results)
    }

    override fun onSearchFailed(reason: String) {
        listener.onSearchFailed(reason)
    }

    override fun abort() {
        getBooks?.cancel(true)
    }

    override fun destroy() {
        abort()
        booksSource = null
    }

}