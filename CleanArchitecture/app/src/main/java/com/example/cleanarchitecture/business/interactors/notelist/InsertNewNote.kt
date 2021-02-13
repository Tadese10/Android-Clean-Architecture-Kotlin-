package com.example.cleanarchitecture.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.domain.state.DataState
import com.codingwithmitch.cleannotes.business.domain.state.StateEvent
import com.example.cleanarchitecture.business.data.cache.CacheResponseHandler
import com.example.cleanarchitecture.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanarchitecture.business.data.network.abstraction.NoteNetworkDataSource
import com.example.cleanarchitecture.business.data.util.safeApiCall
import com.example.cleanarchitecture.business.data.util.safeCacheCall
import com.example.cleanarchitecture.business.domain.model.Note
import com.example.cleanarchitecture.business.domain.state.MessageType
import com.example.cleanarchitecture.business.domain.state.Response
import com.example.cleanarchitecture.business.domain.state.UIComponentType
import com.example.cleanarchitecture.business.domain.model.NoteFactory
import com.example.cleanarchitecture.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class InsertNewNote(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource,
    private val noteFactory: NoteFactory
)
{

    fun insertNewNote(
        id : String? =null,
        title: String,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {//Flow is Caroutine data structure

        val newNote = noteFactory.createSingleNote(
            id = id ?: UUID.randomUUID().toString(),
            title = title
        )

        val cacheResult = safeCacheCall<Long?>(IO, cacheCall = suspend { noteCacheDataSource.insertNote(newNote) })

        val cacheResponse = object: CacheResponseHandler<NoteListViewState, Long>(
            response =  cacheResult,
            stateEvent = stateEvent
        ){
            override fun handleSuccess(resultObject: Long): DataState<NoteListViewState> {
               return if(resultObject > 0){
                    val viewState = NoteListViewState(
                        newNote = newNote
                    )

                     DataState.data(
                        response = Response(
                            message = INSERT_NOTE_SUCESS,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                }else{
                     DataState.data(
                        Response(
                            message = INSERT_NOTE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(cacheResponse)
        updateNetwork(cacheResponse?.stateMessage?.response!!.message, newNote)
    }

    private suspend fun updateNetwork(cacheResponse: String?, newNote: Note) {
        if(cacheResponse.equals(INSERT_NOTE_SUCESS)){
            safeApiCall(IO, suspend { noteNetworkDataSource.insertOrUpdateNote(newNote) })
        }
    }

    companion object{
        const val INSERT_NOTE_SUCESS = "Successfully inserted new note."
        const val INSERT_NOTE_FAILED = "Failed to insert new note."
    }
}