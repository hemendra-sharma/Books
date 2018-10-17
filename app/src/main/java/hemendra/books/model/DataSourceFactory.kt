package hemendra.books.model

import hemendra.books.model.listeners.IDataSource
import hemendra.books.model.listeners.IImageSource
import hemendra.books.model.listeners.ISourceFactory
import hemendra.books.presenter.listeners.IDataSourceListener

class DataSourceFactory : ISourceFactory {

    override fun getDataSource(listener: IDataSourceListener): IDataSource {
        return BooksSource.getInstance(listener)
    }

    override fun getImageSource(): IImageSource {
        return ImageSource.getInstance()
    }

}