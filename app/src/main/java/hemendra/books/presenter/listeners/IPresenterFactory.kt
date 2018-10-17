package hemendra.books.presenter.listeners

import hemendra.books.view.listeners.IBooksView
import hemendra.books.view.listeners.IImageHolder

/**
 * Provides abstraction for actual presenter classes
 */
interface IPresenterFactory {

    /**
     * Get a new instance of the search presenter.
     * @param booksView Instance of the books list view.
     * @return A new instance of the actual search presenter class.
     */
    fun getSearchPresenter(booksView: IBooksView): ISearchPresenter

    /**
     * Get a new instance of the image presenter.
     * @param imageHolder Instance of the image holder view.
     * @return A new instance of the actual image holder view class.
     */
    fun getImagePresenter(imageHolder: IImageHolder): IImagePresenter

}