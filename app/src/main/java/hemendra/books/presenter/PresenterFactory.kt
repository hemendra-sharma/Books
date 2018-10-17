package hemendra.books.presenter

import android.content.Context
import hemendra.books.presenter.listeners.IImagePresenter
import hemendra.books.presenter.listeners.IPresenterFactory
import hemendra.books.presenter.listeners.ISearchPresenter
import hemendra.books.view.listeners.IBooksView
import hemendra.books.view.listeners.IImageHolder

class PresenterFactory : IPresenterFactory {

    override fun getSearchPresenter(booksView: IBooksView): ISearchPresenter {
        return SearchPresenter.getInstance(booksView)
    }

    override fun getImagePresenter(context: Context): IImagePresenter {
        return ImagePresenter.getInstance(context)
    }
}