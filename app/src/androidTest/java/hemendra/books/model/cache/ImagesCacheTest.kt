package hemendra.books.model.cache

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImagesCacheTest {

    private var context: Context? = null
    private var db: ImagesDB? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getTargetContext()
        db = ImagesDB(context!!)
    }

    @Test
    fun duplicateImageTest() {
        val url = "http://test.com/image_1.jpg"
        val imageData = ByteArray(10)

        db?.insertImage(url, imageData)
        db?.insertImage(url, imageData)
        db?.insertImage(url, imageData)

        assertEquals(1, db?.getRecordsCountForUrl(url) ?: 0)
    }

    @Test
    fun maxSizeTest() {
        val imageData = ByteArray(10)
        for (i in 1..ImagesDB.MAX_CACHED_IMAGES*2) {
            val url = "http://test.com/image_$i.jpg"
            db?.insertImage(url, imageData)
        }

        // the cache should not have more than 'MAX_CACHED_IMAGES' records in it
        assertEquals(ImagesDB.MAX_CACHED_IMAGES, db?.getTotalRecordsCount() ?: 0)
    }

    @Test
    fun missingDataTest() {
        // should return NULL if the image does not exist in cache
        assertNull(db?.getImage("http://some_weird_image_url.com/1.jpg"))
    }

    @After
    fun tearDown() {
        // close the database connection
        db?.close()
        // delete database so it is again a fresh start in next test run
        context?.deleteDatabase(ImagesDB.DATABASE_NAME)
    }

}