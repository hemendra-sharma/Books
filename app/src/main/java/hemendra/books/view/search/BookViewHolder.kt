package hemendra.books.view.search

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import hemendra.books.R
import hemendra.books.data.Book
import hemendra.books.presenter.listeners.IImagePresenter
import hemendra.books.view.listeners.IImageHolder
import hemendra.books.view.listeners.OnBookItemClickListener
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BookViewHolder(val view: View,
                     private val listener: OnBookItemClickListener?,
                     private val imagesPresenter: IImagePresenter?):
        RecyclerView.ViewHolder(view), IImageHolder {

    private val cardView = view as CardView
    private val ivCover = view.findViewById<ImageView>(R.id.ivCover)
    private val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
    private val tvSubtitle = view.findViewById<TextView>(R.id.tvSubtitle)
    private val ivStar = view.findViewById<ImageView>(R.id.ivStar)
    private val tvRating = view.findViewById<TextView>(R.id.tvRating)
    private val tvAuthors = view.findViewById<TextView>(R.id.tvAuthors)
    private val tvCategories = view.findViewById<TextView>(R.id.tvCategories)
    private val tvDate = view.findViewById<TextView>(R.id.tvDate)
    private val pb = view.findViewById<ProgressBar>(R.id.pb)

    var book: Book? = null

    init {
        view.setOnClickListener { _ ->
            book?.let { listener?.onItemClick(it) }
        }
    }

    private var loadingImageURL = ""

    @SuppressLint("SetTextI18n")
    fun fillDetails(book: Book) {
        this.book = book

        hideProgress()

        ivCover.setImageBitmap(null)
        imagesPresenter?.getImage(book.smallThumbnail, this)

        tvTitle.text = book.title
        if(book.subtitle.isNotEmpty()) {
            tvSubtitle.text = book.subtitle
            tvSubtitle.visibility = View.VISIBLE
        } else {
            tvSubtitle.visibility = View.GONE
        }

        if(book.averageRating >= 0 && book.ratingsCount >= 0) {
            tvRating.text = String.format(Locale.getDefault(), "%.1f/10 (%d)",
                    book.averageRating, book.ratingsCount)
            tvRating.visibility = View.VISIBLE
            ivStar.visibility = View.VISIBLE
        } else {
            tvRating.visibility = View.GONE
            ivStar.visibility = View.GONE
        }

        if(book.authors.size == 1) {
            tvAuthors.text = "By: ${book.authors[0]}"
            tvAuthors.visibility = View.VISIBLE
        } else if(book.authors.size > 1) {
            val sb = StringBuilder()
            sb.append("By: ")
            for(i in 0 until book.authors.size) {
                if(i >= 2) {
                    sb.append("...${book.authors.size-2} more")
                    break
                }

                sb.append(book.authors[i])
                if(i < book.authors.size-1)
                    sb.append(", ")
            }
            tvAuthors.text = sb.toString()
            tvAuthors.visibility = View.VISIBLE
        } else {
            tvAuthors.visibility = View.GONE
        }

        if(book.categories.size == 1) {
            tvCategories.text = "Categories: ${book.categories[0]}"
            tvCategories.visibility = View.VISIBLE
        } else if(book.categories.size > 1) {
            val sb = StringBuilder()
            sb.append("Categories: ")
            for(i in 0 until book.categories.size) {
                if(i >= 2) {
                    sb.append("...${book.categories.size-2} more")
                    break
                }

                sb.append(book.categories[i])
                if(i < book.categories.size-1)
                    sb.append(", ")
            }
            tvCategories.text = sb.toString()
            tvCategories.visibility = View.VISIBLE
        } else {
            tvCategories.visibility = View.GONE
        }

        if(book.publishedDate.isNotEmpty()) {
            try {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(book.publishedDate)
                tvDate.text = "Published On: "+SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                tvDate.visibility = View.VISIBLE
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                tvDate.text = "Published On: "+book.publishedDate
                tvDate.visibility = View.VISIBLE
            } catch (e: ParseException) {
                e.printStackTrace()
                tvDate.text = "Published On: "+book.publishedDate
                tvDate.visibility = View.VISIBLE
            }
        } else {
            tvDate.visibility = View.GONE
        }
    }

    fun showProgress() {
        ivCover.visibility = View.GONE
        tvTitle.visibility = View.GONE
        tvSubtitle.visibility = View.GONE
        ivStar.visibility = View.GONE
        tvRating.visibility = View.GONE
        tvAuthors.visibility = View.GONE
        tvCategories.visibility = View.GONE
        tvDate.visibility = View.GONE
        pb.visibility = View.VISIBLE
        cardView.cardElevation = 0f
    }

    private fun hideProgress() {
        ivCover.visibility = View.VISIBLE
        tvTitle.visibility = View.VISIBLE
        tvSubtitle.visibility = View.VISIBLE
        ivStar.visibility = View.VISIBLE
        tvRating.visibility = View.VISIBLE
        tvAuthors.visibility = View.VISIBLE
        tvCategories.visibility = View.VISIBLE
        tvDate.visibility = View.VISIBLE
        pb.visibility = View.GONE
        cardView.cardElevation = 5f
    }

    override fun gotImage(bitmap: Bitmap, url: String) {
        if(url == loadingImageURL) {
            ivCover.setImageBitmap(bitmap)
        }
    }
}