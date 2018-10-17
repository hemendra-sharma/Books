package hemendra.books.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import hemendra.books.R
import hemendra.books.data.Book
import hemendra.books.presenter.listeners.IImagePresenter
import hemendra.books.view.listeners.OnBookItemClickListener

class BooksListAdapter(private val books: ArrayList<Book>,
                       private val listener: OnBookItemClickListener?,
                       private val imagePresenter: IImagePresenter?):
        RecyclerView.Adapter<BookViewHolder>() {


    private var endReached = false

    fun appendData(movies: ArrayList<Book>) {
        this.books.addAll(movies)
        notifyDataSetChanged()
    }

    fun endReached() {
        endReached = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(vg: ViewGroup, position: Int): BookViewHolder {
        val view = LayoutInflater.from(vg.context)
                .inflate(R.layout.book_list_item, vg, false)
        return BookViewHolder(view, listener, imagePresenter)
    }

    override fun getItemCount(): Int =
            if(endReached) books.size
            else books.size + 1

    override fun onBindViewHolder(holder: BookViewHolder, i: Int) {
        if(i < books.size) {
            holder.book = books[i]
            holder.fillDetails(books[i])
        } else  holder.showProgress()
    }

}