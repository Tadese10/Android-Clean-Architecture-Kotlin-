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
import com.example.cleanarchitecture.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class RestoreDeletedNote(
        private val noteCacheDataSource: NoteCacheDataSource,
        private val noteNetworkDataSource: NoteNetworkDataSource
){

    fun restoreDeletedNote(
            note: Note,
            stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.insertNote(note)
        }

        val response = object: CacheResponseHandler<NoteListViewState, Long>(
                response = cacheResult,
                stateEvent = stateEvent
        ){
            override fun handleSuccess(resultObject: Long): DataState<NoteListViewState> {
                return if(resultObject > 0){
                    val viewState =
                            NoteListViewState(
                                    notePendingDelete = NoteListViewState.NotePendingDelete(
                                            note = note
                                    )
                            )
                    DataState.data(
                            response = Response(
                                    message = RESTORE_NOTE_SUCCESS,
                                    uiComponentType = UIComponentType.Toast(),
                                    messageType = MessageType.Success()
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                    )
                }
                else{
                    DataState.data(
                            response = Response(
                                    message = RESTORE_NOTE_FAILED,
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

        updateNetwork(response?.stateMessage?.response?.message, note)
    }

    private suspend fun updateNetwork(response: String?, note: Note) {
        if(response.equals(RESTORE_NOTE_SUCCESS)){

            // insert into "notes" node
            safeApiCall(IO){
                noteNetworkDataSource.insertOrUpdateNote(note)
            }

            // remove from "deleted" node
            safeApiCall(IO){
                noteNetworkDataSource.deleteDeletedNote(note)
            }
        }
    }

    companion object{

        val RESTORE_NOTE_SUCCESS = "Successfully restored the deleted note."
        val RESTORE_NOTE_FAILED = "Failed to restore the deleted note."

    }
}