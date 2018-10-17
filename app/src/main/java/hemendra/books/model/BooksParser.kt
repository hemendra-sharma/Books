package hemendra.books.model

import hemendra.books.data.Book
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener

class BooksParser {
    companion object {

        fun parseBooks(json: String): ArrayList<Book>? {
            try {
                if(JSONTokener(json).nextValue() is JSONObject) {
                    val jsonObject = JSONObject(json)
                    if(jsonObject.has("items")) {
                        // "items" ->
                        val itemsArray = jsonObject.getJSONArray("items")
                        val count = itemsArray.length()
                        val books = ArrayList<Book>()
                        for(i in 0 until count) {
                            val bookObject = itemsArray.getJSONObject(i)
                            bookObject?.let { obj ->
                                parseBook(obj)?.let { book ->
                                    books.add(book)
                                }
                            }
                        }
                        return books
                    }
                }
            } catch (e : JSONException) {
                e.printStackTrace()
            }
            return null
        }

        private fun parseBook(obj: JSONObject): Book? {
            try {
                if(obj.has("volumeInfo")) {
                    val volumeInfo = obj.getJSONObject("volumeInfo")

                    val book = Book()

                    book.title = volumeInfo.optString("title")
                    book.subtitle = volumeInfo.optString("subtitle")
                    book.publisher = volumeInfo.optString("publisher")
                    book.publishedDate = volumeInfo.optString("publishedDate")
                    book.description = volumeInfo.optString("description")
                    book.averageRating = volumeInfo.optDouble("averageRating", -1.0)
                    book.ratingsCount = volumeInfo.optLong("ratingsCount", -1)
                    book.smallThumbnail = volumeInfo.optString("smallThumbnail")
                    book.thumbnail = volumeInfo.optString("thumbnail")

                    if(volumeInfo.has("authors")) {
                        val authors = volumeInfo.getJSONArray("authors")
                        for(i in 0 until authors.length()) {
                            book.authors.add(authors.getString(i))
                        }
                    }

                    if(volumeInfo.has("categories")) {
                        val categories = volumeInfo.getJSONArray("categories")
                        for(i in 0 until categories.length()) {
                            book.categories.add(categories.getString(i))
                        }
                    }

                    return book
                }
            } catch (e : JSONException) {
                e.printStackTrace()
            }
            return null
        }

    }
}