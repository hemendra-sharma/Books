package hemendra.books.model.listeners

interface IDataSource {

    /**
     * Start searching for the book volumes.
     * @param query The keyword to search for.
     * @param apiKey The API Key for interacting with Google Books API.
     */
    fun searchBooks(query: String, apiKey: String)

    /**
     * Abort the ongoing search process.
     */
    fun abort()

    /**
     * Destroy all the references and release memory.
     */
    fun destroy()

}