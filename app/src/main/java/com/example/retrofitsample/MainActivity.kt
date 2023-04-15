package com.example.retrofitsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.retrofitsample.Model.Holiday
import com.example.retrofitsample.Retrofit.HolidaysAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private val network = HolidaysAPI.retrofitService
    val arrayList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter =
            ArrayAdapter<String>(
                this@MainActivity,
                android.R.layout.simple_list_item_1,
                arrayList
            )
        list.adapter = adapter

        get.setOnClickListener {
            callToGetHolidays()
            Log.i("Logs", arrayList.toString())
        }

        delete.setOnClickListener {

            arrayList.clear()
            adapter.notifyDataSetChanged()

            if (name.text.isEmpty()) {
                Toast.makeText(this@MainActivity, "Enter a holiday to delete", Toast.LENGTH_SHORT)
                    .show()
            } else {
                callToDeleteHoliday(name.text.toString())
            }

            if (!date.text.isEmpty()) {
                Toast.makeText(this@MainActivity, "Date is not needed for delete", Toast.LENGTH_SHORT)
                    .show()

            }

        }

        post.setOnClickListener {

            arrayList.clear()
            adapter.notifyDataSetChanged()

        }

        patch.setOnClickListener {

            arrayList.clear()
            adapter.notifyDataSetChanged()

        }

    }

    private fun callToDeleteHoliday(name: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val reponse = network.deleteHoliday(name)

            withContext(Dispatchers.Main) {
                Log.i("Logs", reponse.toString())
                when (reponse.code()) {
                    404 -> Toast.makeText(this@MainActivity, "Record Not Found", Toast.LENGTH_SHORT)
                        .show()
                    204 -> Toast.makeText(
                        this@MainActivity,
                        "Record Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    else ->
                        Toast.makeText(this@MainActivity, "Internal Error", Toast.LENGTH_SHORT)
                            .show()

                }
            }
        }


    }


    private fun callToGetHolidays() {

        arrayList.clear()

        CoroutineScope(Dispatchers.IO).launch {
            val list = network.getHolidays()

            withContext(Dispatchers.Main) {

                for (i in list.indices) {
                    arrayList.add("${list[i].name} - ${list[i].date}")
                    adapter.notifyDataSetChanged()
                }

            }
        }


    }


}

