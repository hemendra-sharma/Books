package hemendra.books.view.listeners

import hemendra.books.data.Book

interface IBooksView {

    /**
     * Gets called when search results are downloaded.
     *
     */
    fun onSearchResults(results: ArrayList<Book>)

    /**
     * Gets called when the search fails.
     * @param reason The failure reason.
     */
    fun onSearchFailed(reason: String)

}