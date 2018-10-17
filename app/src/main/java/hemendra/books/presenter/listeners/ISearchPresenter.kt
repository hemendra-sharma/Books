package hemendra.books.presenter.listeners

interface ISearchPresenter {

    /**
     * Start searching for the book volumes.
     * @param query The keyword to search for.
     */
    fun performSearch(query: String)

    /**
     * Abort the ongoing search process.
     */
    fun abort()

    /**
     * Destroy all the references and release memory.
     */
    fun destroy()

}