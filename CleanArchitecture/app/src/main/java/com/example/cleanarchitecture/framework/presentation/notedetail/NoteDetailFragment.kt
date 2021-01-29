package com.example.cleanarchitecture.framework.presentation.notedetail

import android.os.Bundle
import android.view.View
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.framework.presentation.common.BaseNoteFragment

class NoteDetailFragment : BaseNoteFragment(R.layout.fragment_note_detail) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun inject() {
        TODO("prepare dagger")
    }


}