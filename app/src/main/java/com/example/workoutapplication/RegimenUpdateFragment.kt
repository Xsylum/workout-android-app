package com.example.workoutapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class RegimenUpdateFragment(private val name: String = "",
                            private val description: String = ""): DialogFragment() {

    private lateinit var listener: RegimenUpdateListener

    //TODO: don't allow empty name/description
    interface RegimenUpdateListener {
        fun onRegimenUpdatePositiveClick(name: String, description: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as RegimenUpdateListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement RegimenRenameListener!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val layoutView = inflater.inflate(R.layout.fragment_regimen_update_info, null)
            builder.setView(layoutView)

            val nameView = layoutView.findViewById<EditText>(R.id.et_RegimenName)
            nameView.setText(name)
            val descView = layoutView.findViewById<EditText>(R.id.et_RegimenDescription)
            descView.setText(description)

            builder.setTitle("Regimen Info Details")
                .setPositiveButton("Confirm") {dialog, id ->
                    listener.onRegimenUpdatePositiveClick(nameView.text.toString(),
                        descView.text.toString())
                }
                .setNegativeButton("Cancel") {dialog, id -> }

            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}