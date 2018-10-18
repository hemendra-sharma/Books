package hemendra.books.data

class Book {

    var title = ""
    var subtitle = ""
    val authors = ArrayList<String>()
    var publisher = ""
    var publishedDate = ""
    var description = ""
    val categories = ArrayList<String>()
    var averageRating: Double = 0.0
    var ratingsCount: Long = 0L
    var smallThumbnail = ""
    var thumbnail = ""
    var buyLink = ""
    var webReaderLink = ""

}