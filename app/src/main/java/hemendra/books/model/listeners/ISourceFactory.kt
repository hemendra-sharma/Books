package hemendra.books.model.listeners

import android.content.Context
import hemendra.books.presenter.listeners.IDataSourceListener

/**
 * Provides abstraction for the actual source classes.
 */
interface ISourceFactory {

    /**
     * Get the instance of the data source.
     * @param listener The callback for data source.
     */
    fun getDataSource(listener: IDataSourceListener): IDataSource

    /**
     * Get the instance of the image source.
     * @param context The application context.
     */
    fun getImageSource(context: Context): IImageSource

}