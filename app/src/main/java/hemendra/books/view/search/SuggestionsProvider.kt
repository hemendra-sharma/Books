package hemendra.books.view.search

import android.content.SearchRecentSuggestionsProvider

class SuggestionsProvider : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "hemendra.books.view.search.SuggestionsProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

}