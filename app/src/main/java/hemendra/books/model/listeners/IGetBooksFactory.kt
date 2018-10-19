package hemendra.books.model.listeners

import hemendra.books.model.GetBooks

interface IGetBooksFactory {
    fun newInstance(listener: BooksLoaderListener): GetBooks
}