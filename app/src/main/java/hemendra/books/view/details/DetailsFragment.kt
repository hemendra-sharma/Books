package hemendra.books.view.details

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import hemendra.books.R
import hemendra.books.data.Book
import hemendra.books.presenter.listeners.IImagePresenter
import hemendra.books.view.listeners.IImageHolder
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.IllegalArgumentException
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment: Fragment(), IImageHolder {

    private var imagePresenter: IImagePresenter? = null
    fun setImagePresenter(presenter: IImagePresenter) {
        this.imagePresenter = presenter
    }

    private var book: Book? = null
    fun setBook(book: Book) {
        this.book = book
    }

    private var savedView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        savedView?.let { return it }
        savedView = inflater.inflate(R.layout.fragment_details, container, false)
        return savedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        book?.let { fillDetails(it) }
    }

    @SuppressLint("SetTextI18n")
    private fun fillDetails(book: Book) {
        this.book = book

        ivCover?.setImageBitmap(null)
        ivCover?.setBackgroundColor(Color.parseColor("#eeeeee"))

        val loadingImageURL = when {
            book.smallThumbnail.isNotEmpty() -> book.smallThumbnail
            book.thumbnail.isNotEmpty() -> book.thumbnail
            else -> ""
        }

        if(loadingImageURL.isNotEmpty())
            imagePresenter?.getImage(loadingImageURL, this)

        tvTitle?.text = book.title
        if(book.subtitle.isNotEmpty()) {
            tvSubtitle?.text = book.subtitle
            tvSubtitle?.visibility = View.VISIBLE
        } else {
            tvSubtitle?.visibility = View.GONE
        }

        if(book.averageRating >= 0 && book.ratingsCount >= 0) {
            tvRating?.text = String.format(Locale.getDefault(), "%.1f/5 (%d)",
                    book.averageRating, book.ratingsCount)
            tvRating?.visibility = View.VISIBLE
            ivStar?.visibility = View.VISIBLE
        } else {
            tvRating?.visibility = View.GONE
            ivStar?.visibility = View.GONE
        }

        if(book.authors.size == 1) {
            tvAuthors?.text = "By: ${book.authors[0]}"
            tvAuthors?.visibility = View.VISIBLE
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
            tvAuthors?.text = sb.toString()
            tvAuthors?.visibility = View.VISIBLE
        } else {
            tvAuthors?.visibility = View.GONE
        }

        if(book.categories.size == 1) {
            tvCategories?.text = "Categories: ${book.categories[0]}"
            tvCategories?.visibility = View.VISIBLE
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
            tvCategories?.text = sb.toString()
            tvCategories?.visibility = View.VISIBLE
        } else {
            tvCategories?.visibility = View.GONE
        }

        if(book.publishedDate.isNotEmpty()) {
            try {
                if(book.publishedDate.length == 4) { // has only year. e.g. 2018
                    tvDate?.text = "Published On: " + book.publishedDate
                } else if(book.publishedDate.length == 7) { // has year and month. e.g. 2018-10
                    val date = SimpleDateFormat("yyyy-MM", Locale.getDefault()).parse(book.publishedDate)
                    tvDate?.text = "Published On: " + SimpleDateFormat("MMM, yyyy", Locale.getDefault()).format(date)
                } else { // may have full date
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(book.publishedDate)
                    tvDate?.text = "Published On: " + SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
                }
                tvDate?.visibility = View.VISIBLE
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                tvDate?.text = "Published On: "+book.publishedDate
                tvDate?.visibility = View.VISIBLE
            } catch (e: ParseException) {
                e.printStackTrace()
                tvDate?.text = "Published On: "+book.publishedDate
                tvDate?.visibility = View.VISIBLE
            }
        } else {
            tvDate?.visibility = View.GONE
        }

        tvDescription?.text = book.description

        if(book.buyLink.isNotEmpty())
            btnBuy.visibility = View.VISIBLE
        else
            btnBuy.visibility = View.GONE

        if(book.webReaderLink.isNotEmpty())
            btnRead.visibility = View.VISIBLE
        else
            btnRead.visibility = View.GONE

        btnBuy.transformationMethod = null
        btnRead.transformationMethod = null

        btnBuy.setOnClickListener(onBuyClicked)
        btnRead.setOnClickListener(onReadClicked)
    }

    private val onBuyClicked = View.OnClickListener {
        book?.let { b ->
            activity?.let { act ->
                if(b.buyLink.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(b.buyLink)
                    if(intent.resolveActivity(act.packageManager) != null)
                        startActivity(Intent.createChooser(intent, "Select an App"))
                    else
                        Toast.makeText(act, "No App Found to Buy This!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val onReadClicked = View.OnClickListener {
        book?.let { b ->
            activity?.let { act ->
                if(b.webReaderLink.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(b.webReaderLink)
                    if(intent.resolveActivity(act.packageManager) != null)
                        startActivity(Intent.createChooser(intent, "Select an App"))
                    else
                        Toast.makeText(act, "No App Found to Read This!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun gotImage(bitmap: Bitmap, url: String) {
        ivCover?.setImageBitmap(bitmap)
        ivCover?.setBackgroundColor(Color.TRANSPARENT)
    }

    fun destroy() {
        savedView = null
        book = null
    }

}