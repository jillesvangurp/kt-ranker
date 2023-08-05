package com.jilesvangurp.ktranker

import kotlinx.serialization.Serializable
import kotlin.time.Duration


@Serializable
data class SearchResults(val total: Long, val responseTime: Duration, val searchResultList: List<SearchResult>) {
    @Serializable
    data class SearchResult(val id: String, val label: String? = null)
}



/**
 * The job of a SearchPlugin implementation is to fetch results from any kind of search implementation that
 * given the search context (search parameters) and a number of desired results. It returns a Results
 * instance that specifies the list of results in the order that they appear with their unique Ids and an
 * optional label (for human readbility)
 *
 * A typical implementation would use either ktor client or some kind of client for whatever search client you are
 * using and extract the results.
 *
 */
interface SearchPlugin {
    suspend fun fetch(searchContext: Map<String, String>, numberOfItemsToFetch: Int): Result<SearchResults>
}

/**
 * Use this to quickly convert a search to a RatedSearch. You can edit the ratings later.
 */
suspend fun SearchPlugin.initializeRatedSearch(searchContext: Map<String, String>, numberOfItemsToFetch: Int): Result<RatedSearch> {
    return fetch(searchContext, numberOfItemsToFetch).map { results ->
        var rating = results.searchResultList.size
        RatedSearch("", searchContext, results.searchResultList.map {
            SearchResultRating(it.id, rating--, it.label)
        })
    }
}



