package com.example.cleanarchitecture.business.interactors.common

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class DeleteNote<ViewState>(
    private val noteCacheDataSource: NoteCacheDataSource,
    private val noteNetworkDataSource: NoteNetworkDataSource
){

    fun deleteNote(
        note: Note,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            noteCacheDataSource.deleteNote(note.id)
        }

        val response = object: CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override fun handleSuccess(resultObject: Int): DataState<ViewState> {
                return if(resultObject > 0){
                    DataState.data(
                        response = Response(
                            message = DELETE_NOTE_SUCCESS,
                            uiComponentType = UIComponentType.None(),
                            messageType = MessageType.Success()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
                else{
                    DataState.data(
                        response = Response(
                            message = DELETE_NOTE_FAILED,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Error()
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(response)

        // update network
        updateNetwork(response, note)
    }

    private suspend fun updateNetwork(
        response: DataState<ViewState>?,
        note: Note
    ) {
        if (response?.stateMessage?.response?.message.equals(DELETE_NOTE_SUCCESS)) {

            // delete from 'notes' node
            safeApiCall(Dispatchers.IO) {
                noteNetworkDataSource.deleteNote(note.id)
            }

            // insert into 'deletes' node
            safeApiCall(Dispatchers.IO) {
                noteNetworkDataSource.insertDeletedNote(note)
            }

        }
    }

    companion object{
        val DELETE_NOTE_SUCCESS = "Successfully deleted note."
        val DELETE_NOTE_PENDING = "Delete pending..."
        val DELETE_NOTE_FAILED = "Failed to delete note."
        val DELETE_ARE_YOU_SURE = "Are you sure you want to delete this?"
    }
}