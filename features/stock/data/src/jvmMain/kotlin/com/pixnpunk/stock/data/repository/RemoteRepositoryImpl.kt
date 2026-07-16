package com.pixnpunk.stock.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.useWriterConnection
import com.pixnpunk.database.AppDatabase
import com.pixnpunk.database.dao.AuthorDao
import com.pixnpunk.database.dao.TranslatorDao
import com.pixnpunk.dto.DownloadStatus
import com.pixnpunk.dto.GameEntry
import com.pixnpunk.dto.ProgressState
import com.pixnpunk.stock.data.mapper.toAuthorEntity
import com.pixnpunk.stock.data.mapper.toDomain
import com.pixnpunk.stock.data.mapper.toTranslatorEntity
import com.pixnpunk.stock.data.model.RepoSettings.API_QUERY_PAGE
import com.pixnpunk.stock.data.model.RepoSettings.API_QUERY_PER_PAGE
import com.pixnpunk.stock.data.model.RepoSettings.PAGE_SIZE
import com.pixnpunk.stock.data.model.RepoSettings.REMOTE_AUTHORS_URL
import com.pixnpunk.stock.data.model.RepoSettings.REMOTE_TRANSLATORS_URL
import com.pixnpunk.stock.domain.model.AuthorEntry
import com.pixnpunk.stock.domain.model.GenericApiResponse
import com.pixnpunk.stock.domain.model.NameCountItemRemote
import com.pixnpunk.stock.domain.model.SearchState
import com.pixnpunk.stock.domain.model.TranslatorEntry
import com.pixnpunk.stock.domain.repository.RemoteRepository
import com.pixnpunk.utils.FileUtil.unpackFileToDir
import com.pixnpunk.utils.FileUtil.writeFileToDir
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes

class RemoteRepositoryImpl(
    private val ktorClient: HttpClient,
    private val appDatabase: AppDatabase,
    private val authorDao: AuthorDao,
    private val translatorDao: TranslatorDao
) : RemoteRepository {

    override fun getGameEntriesPager(
        searchState: SearchState
    ): Flow<PagingData<GameEntry>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RemotePagingSource(
                    ktorClient,
                    searchState.query,
                    searchState.sortBy,
                    searchState.sortOrder,
                    searchState.selectedLang,
                    searchState.activeTokens
                )
            }
        ).flow


    override fun getAuthorSuggestions(query: String): Flow<List<AuthorEntry>> =
        if (query.isEmpty()) {
            authorDao.getRandomAuthors().map { list -> list.map { it.toDomain() } }
        } else {
            authorDao.searchAuthorsLocal(query).map { list -> list.map { it.toDomain() } }
        }

    override fun getTranslatorSuggestions(query: String): Flow<List<TranslatorEntry>> =
        if (query.isEmpty()) {
            translatorDao.getRandomTranslators().map { list -> list.map { it.toDomain() } }
        } else {
            translatorDao.searchTranslatorLocal(query).map { list -> list.map { it.toDomain() } }
        }

    override suspend fun downloadGame(downloadEntry: GameEntry) {
//        ktorClient.prepareGet(
//            urlString = downloadEntry.fileUrl,
//            block = {
//                val timeout = 30.minutes.inWholeMilliseconds
//                timeout {
//                    requestTimeoutMillis = timeout
//                    connectTimeoutMillis = timeout
//                    socketTimeoutMillis = timeout
//                }
//
//                onDownload { bytesSentTotal, contentLength ->
//                    if (contentLength != null) {
//                        _progressDownloadFlow.emit(
//                            ProgressState(
//                                progress = bytesSentTotal.toFloat() / contentLength.toFloat(),
//                                status = DownloadStatus.DOWNLOAD_FILE,
//                                time = "NaN"
//                            )
//                        )
//                    }
//                }
//            }
//        ).execute { response ->
//            if (response.status.isSuccess()) {
//                val contentDisposition = response.headers[HttpHeaders.ContentDisposition]
//                val headerValues = parseHeaderValue(contentDisposition)
//                val fileName = headerValues.firstOrNull()?.params?.firstOrNull()?.value
//                    ?: "#${(0..Int.MAX_VALUE).random()}Game"
//
//                writeFileToDir(
//                    fileChannel = response.bodyAsChannel(),
//                    fileName = fileName,
//                    destDirPath = destDirPath,
//                    progress = _progressDownloadFlow
//                ).onSuccess {
//                    unpackFileToDir(
//                        filePath = it,
//                        destDirPath = destDirPath,
//                        progress = _progressDownloadFlow
//                    ).onSuccess { return@execute Result.success(destDirPath) }
//                }.onFailure {
//                    _progressDownloadFlow.emit(ProgressState(progress = 1.0F))
//                }
//            } else {
//                return@execute Result.failure(RuntimeException("Failed to download file. HTTP Status: ${response.status.value}"))
//            }
//        }
    }

    override suspend fun syncDictionaries() = withContext(Dispatchers.IO) {
        runCatching {
            coroutineScope {
                val allAuthors = async { fetchAllPages(REMOTE_AUTHORS_URL) }.await()
                val allTranslators = async { fetchAllPages(REMOTE_TRANSLATORS_URL) }.await()
                appDatabase.useWriterConnection {
                    authorDao.clearAll()
                    authorDao.insertAll(allAuthors.map { it.toAuthorEntity() })

                    translatorDao.clearAll()
                    translatorDao.insertAll(allTranslators.map { it.toTranslatorEntity() })
                }
            }
        }
    }

    private suspend fun fetchAllPages(url: String): List<NameCountItemRemote> {
        val allItems = mutableListOf<NameCountItemRemote>()
        var currentPage = 1
        var hasNextPage = true

        while (hasNextPage) {
            val response = ktorClient.get(url) {
                parameter(API_QUERY_PAGE, currentPage)
                parameter(API_QUERY_PER_PAGE, 100)
            }.body<GenericApiResponse<NameCountItemRemote>>()

            allItems.addAll(response.data)

            if (response.meta.currentPage >= response.meta.lastPage) {
                hasNextPage = false
            } else {
                currentPage++
            }
        }

        return allItems
    }
}