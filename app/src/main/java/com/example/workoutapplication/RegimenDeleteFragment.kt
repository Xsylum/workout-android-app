package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class RegimenDeleteFragment(private val name: String): DialogFragment() {

    private lateinit var listener: RegimenDeleteListener

    interface RegimenDeleteListener {
        fun onRegimenDeletePositiveClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as RegimenDeleteListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement RegimenDeleteListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle("Are you sure you want to delete regimen $name?")
                .setPositiveButton("Confirm Deletion") { dialog, id ->
                    listener.onRegimenDeletePositiveClick()
                }
                .setNegativeButton("Cancel") { dialog, id ->

                }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}