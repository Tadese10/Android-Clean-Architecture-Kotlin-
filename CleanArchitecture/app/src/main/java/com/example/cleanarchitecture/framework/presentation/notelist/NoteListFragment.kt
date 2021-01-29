package com.example.cleanarchitecture.framework.presentation.notelist

import android.os.Bundle
import android.view.View
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.framework.presentation.common.BaseNoteFragment

class NoteListFragment: BaseNoteFragment(R.layout.fragment_note_list)
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun inject() {
        TODO("prepare dagger")
    }

}