package hemendra.books.model

import hemendra.books.model.listeners.BooksLoaderListener
import hemendra.books.model.listeners.IGetBooksFactory

class GetBooksFactory: IGetBooksFactory {
    override fun newInstance(listener: BooksLoaderListener): GetBooks {
        return GetBooks(listener)
    }
}