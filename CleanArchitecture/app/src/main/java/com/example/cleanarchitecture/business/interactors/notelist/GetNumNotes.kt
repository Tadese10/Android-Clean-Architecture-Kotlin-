package com.example.cleanarchitecture.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.domain.state.DataState
import com.codingwithmitch.cleannotes.business.domain.state.StateEvent
import com.example.cleanarchitecture.business.data.cache.CacheResponseHandler
import com.example.cleanarchitecture.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanarchitecture.business.data.util.safeCacheCall
import com.example.cleanarchitecture.business.domain.state.MessageType
import com.example.cleanarchitecture.business.domain.state.Response
import com.example.cleanarchitecture.business.domain.state.UIComponentType
import com.example.cleanarchitecture.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetNumNotes(
    private val noteCacheDataSource: NoteCacheDataSource
) {

    fun getNumNotes(
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            noteCacheDataSource.getNumNotes()
        }
        val response = object : CacheResponseHandler<NoteListViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override fun handleSuccess(resultObject: Int): DataState<NoteListViewState> {
                val viewState = NoteListViewState(
                    numNotesInCache = resultObject
                )
                return DataState.data(
                    response = Response(
                        message = GET_NUM_NOTES_SUCCESS,
                        uiComponentType = UIComponentType.None(),
                        messageType = MessageType.Success()
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        val GET_NUM_NOTES_SUCCESS = "Successfully retrieved the number of notes from the cache."
        val GET_NUM_NOTES_FAILED = "Failed to get the number of notes from the cache."
    }

}