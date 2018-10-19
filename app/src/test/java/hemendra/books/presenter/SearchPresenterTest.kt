package hemendra.books.presenter

import hemendra.books.model.listeners.IDataSource
import hemendra.books.view.listeners.IBooksView
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory

/**
 * Some behavioral tests
 */
class SearchPresenterTest {

    private val bookView = mock(IBooksView::class.java)
    private val dataSource = mock(IDataSource::class.java)

    private val searchPresenter = SearchPresenter.getInstance(bookView)
    init {
        searchPresenter.dataSource = dataSource
    }

    @Test
    fun performSearchTest() {
        searchPresenter.performSearch("Harry Potter", "12345", 1)

        // should call the corresponding method of data source
        verify(dataSource, VerificationModeFactory.atLeastOnce())
                .searchBooks("Harry Potter", "12345", 1)
    }

    @Test
    fun isSearchingTest() {
        searchPresenter.isSearching()

        // should call the corresponding method of data source
        verify(dataSource, VerificationModeFactory.atLeastOnce()).isSearching()
    }

    @Test
    fun abortTest() {
        searchPresenter.abort()

        // should call the corresponding method of data source
        verify(dataSource, VerificationModeFactory.atLeastOnce()).abort()
    }

    @Test
    fun destroyTest() {
        searchPresenter.destroy()

        // should call the corresponding method of data source
        verify(dataSource, VerificationModeFactory.atLeastOnce()).destroy()

        // should release references
        assertNull(searchPresenter.dataSource)
        assertNull(searchPresenter.activity)
    }
}