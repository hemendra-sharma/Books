package hemendra.books.view.listeners

import hemendra.books.data.Book

@FunctionalInterface
interface OnBookItemClickListener {
    fun onItemClick(book: Book)
}