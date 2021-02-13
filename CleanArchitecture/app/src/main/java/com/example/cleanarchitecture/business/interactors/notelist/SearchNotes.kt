package com.example.cleanarchitecture.business.interactors.notelist

import com.codingwithmitch.cleannotes.business.domain.state.DataState
import com.codingwithmitch.cleannotes.business.domain.state.StateEvent
import com.example.cleanarchitecture.business.data.cache.CacheResponseHandler
import com.example.cleanarchitecture.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanarchitecture.business.data.util.safeCacheCall
import com.example.cleanarchitecture.business.domain.model.Note
import com.example.cleanarchitecture.business.domain.state.MessageType
import com.example.cleanarchitecture.business.domain.state.Response
import com.example.cleanarchitecture.business.domain.state.UIComponentType
import com.example.cleanarchitecture.framework.presentation.notelist.state.NoteListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.ByteOrder

class SearchNotes(
    private val noteCacheDataSource:  NoteCacheDataSource //It doesnt need to talk to network cox this will always be in sync with firestore data
) {
     fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<NoteListViewState>?> = flow{
        var updatePage = page
        if(page <= 0){
            updatePage = 1
        }

        val cacheResult = safeCacheCall(IO){
            noteCacheDataSource.searchNotes(
                query = query,
                filterAndOrder = filterAndOrder,
                page = updatePage)
        }

         val response = object : CacheResponseHandler<NoteListViewState, List<Note>>(
             response = cacheResult,
             stateEvent = stateEvent
         ){
             override fun handleSuccess(resultObject: List<Note>): DataState<NoteListViewState> {
                 var message : String? = SEARCH_NOTES_SUCCESS
                 var uiComponentType: UIComponentType = UIComponentType.None()
                 if(resultObject.isEmpty()){
                     message = SEARCH_NOTES_NO_MATCHING_RESULTS
                     uiComponentType = UIComponentType.Toast()
                 }
                 return DataState.data(
                     response = Response(
                         message = message,
                         uiComponentType = uiComponentType,
                         messageType = MessageType.Success()
                     ),
                     data = NoteListViewState(
                         noteList = ArrayList(resultObject)
                     ),
                     stateEvent = stateEvent
                 )

             }
         }.getResult()

         emit(response)
    }

    companion object{
        val SEARCH_NOTES_SUCCESS = "Successfully retrieved list of notes."
        val SEARCH_NOTES_NO_MATCHING_RESULTS = "There are no notes that match that query."
        val SEARCH_NOTES_FAILED = "Failed to retrieve the list of notes."

    }
}