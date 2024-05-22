package com.febrian.simpletodolistapp.utils

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun Context.showDialog(message: String, onConfirm: () -> Unit, onCancel: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
        .setPositiveButton("Yes") { dialog, id ->
            onConfirm()
        }
        .setNegativeButton("No") { dialog, id ->
            onCancel()
            dialog.dismiss()
        }
    val dialog = builder.create()
    dialog.show()
}