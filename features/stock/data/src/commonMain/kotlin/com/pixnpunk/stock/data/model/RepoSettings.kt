package com.pixnpunk.stock.data.model

internal object RepoSettings {
    const val INITIAL_PAGE_INDEX = 1
    const val PAGE_SIZE = 15
    const val API_QUERY_PAGE = "page"
    const val API_QUERY_PER_PAGE = "per_page"
    const val API_SEARCH_AUTHORS = "search-authors"
    const val API_SEARCH_TRANSLATORS = "search-translators"
    const val API_SEARCH_DESC = "search-description"
    const val API_SEARCH_NAME = "search-name"
    const val API_QUERY_SORT = "sort"
    const val API_QUERY_ORDER = "order"
    const val API_QUERY_LANG = "language"

    const val REMOTE_GAMES_URL = "https://qsp.org/api/v1/games"
    const val REMOTE_AUTHORS_URL = "$REMOTE_GAMES_URL/authors"
    const val REMOTE_TRANSLATORS_URL = "$REMOTE_GAMES_URL/translators"
}
