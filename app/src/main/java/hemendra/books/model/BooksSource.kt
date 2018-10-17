package hemendra.books.model

import hemendra.books.model.listeners.IDataSource
import hemendra.books.presenter.listeners.IDataSourceListener

class BooksSource(private val listener: IDataSourceListener) : IDataSource {

    companion object {

        private var booksSource: BooksSource? = null

        fun getInstance(listener: IDataSourceListener) : BooksSource {
            if(booksSource == null) booksSource = BooksSource(listener)
            return booksSource!!
        }

    }

    override fun searchBooks(query: String, apiKey: String) {

    }

    override fun abort() {

    }

    override fun destroy() {
        abort()
        booksSource = null
    }

}