package hemendra.books.model.listeners

import hemendra.books.presenter.listeners.IDataSourceListener
import hemendra.books.presenter.listeners.IImageSourceListener

/**
 * Provides abstraction for the actual source classes.
 */
interface ISourceFactory {

    /**
     * Get the instance of the data source.
     * @param listener The callback for data source.
     */
    fun getDataSource(listener: IDataSourceListener)

    /**
     * Get the instance of the image source.
     * @param listener The callback for image source.
     */
    fun getImageSource(listener: IImageSourceListener)

}