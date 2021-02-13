package com.example.cleanarchitecture.framework.presentation.notedetail.state

import android.os.Parcelable
import com.codingwithmitch.cleannotes.business.domain.state.ViewState
import com.example.cleanarchitecture.business.domain.model.Note
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteDetailViewState(

    var note: Note? = null,

    var isUpdatePending: Boolean? = null

) : Parcelable, ViewState
