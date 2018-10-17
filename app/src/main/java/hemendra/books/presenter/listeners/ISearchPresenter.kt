package hemendra.books.presenter.listeners

interface ISearchPresenter {

    /**
     * Start searching for the book volumes.
     * @param query The keyword to search for.
     * @param apiKey The API Key for interacting with Google Books API.
     * @param pageNumber The page number for paged search.
     */
    fun performSearch(query: String, apiKey: String, pageNumber: Int)

    /**
     * Check if the search is already ongoing.
     */
    fun isSearching(): Boolean

    /**
     * Abort the ongoing search process.
     */
    fun abort()

    /**
     * Destroy all the references and release memory.
     */
    fun destroy()

}