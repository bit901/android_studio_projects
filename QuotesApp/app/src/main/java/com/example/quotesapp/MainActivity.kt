package com.example.quotesapp

import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {
    var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RequestManager(this@MainActivity).GetAllQuotes(listener)
        dialog = ProgressDialog(this@MainActivity)
        dialog?.setTitle("Loading this bitch")
        dialog?.show()

    }

    private val listener: QuotesResponseListener = object : QuotesResponseListener {
        override fun didFetch(response: List<QuotesResponse>, message: String) {
            dialog?.dismiss()
            var rv_home: RecyclerView = findViewById(R.id.rv_home)
            rv_home.setHasFixedSize(true)
            rv_home.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            val adapter = QuotesListAdapter(this@MainActivity, response, copyListener)
            rv_home.adapter = adapter
        }

        override fun didError(message: String) {
            dialog?.dismiss()
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }

    }

    private val copyListener: CopyListener = object : CopyListener {
        override fun onCopyClicked(text: String) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("copied_data", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@MainActivity, "Quote copied from clipboard", Toast.LENGTH_LONG).show()
        }

    }

}