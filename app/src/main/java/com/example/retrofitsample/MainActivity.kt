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

            name.text.clear()
            date.text.clear()

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
                Toast.makeText(
                    this@MainActivity,
                    "Date is not needed for delete",
                    Toast.LENGTH_SHORT
                )
                    .show()

            }

        }

        post.setOnClickListener {

            arrayList.clear()
            adapter.notifyDataSetChanged()

            if (name.text.isEmpty() || date.text.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Enter a holiday and date to post",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                callToPostHoliday(Holiday(name.text.toString(), date.text.toString()))
            }

        }

        patch.setOnClickListener {

            arrayList.clear()
            adapter.notifyDataSetChanged()

            if (name.text.isEmpty() || date.text.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Enter a valid holiday and date to update",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                callToGetAndPatchHoliday(name.text.toString())
            }

        }

    }

    private fun callToPatchHoliday(holiday: Holiday) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = network.patchHoliday(holiday)

            withContext(Dispatchers.Main) {
                Log.i("Logs", response.toString())
                when (response.code()) {
                    200 -> Toast.makeText(
                        this@MainActivity,
                        "Record Updated Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    400 -> Toast.makeText(
                        this@MainActivity,
                        "Date should be in yyyy-MM-dd Format",
                        Toast.LENGTH_SHORT
                    ).show()
                    404 -> Toast.makeText(
                        this@MainActivity,
                        "Record not found to update",
                        Toast.LENGTH_SHORT
                    ).show()
                    else ->
                        Toast.makeText(this@MainActivity, "Internal Error", Toast.LENGTH_SHORT)
                            .show()

                }

            }

        }


    }

    private fun callToGetAndPatchHoliday(holidayName: String){

        CoroutineScope(Dispatchers.IO).launch {
            val response = network.getHolidayByName(holidayName)

            withContext(Dispatchers.Main) {
                Log.i("Logs", response.toString())
                if (response.code() == 200) {
                    val id = response.body()?.id ?: 0
                    callToPatchHoliday(Holiday(name = name.text.toString(), date.text.toString(), id))
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Record Not found To update",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun callToPostHoliday(holiday: Holiday) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = network.postHoliday(holiday)

            withContext(Dispatchers.Main) {
                Log.i("Logs", response.toString())
                when (response.code()) {
                    201 -> Toast.makeText(
                        this@MainActivity,
                        "Record Added Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    400 -> Toast.makeText(
                        this@MainActivity,
                        "Date should be in yyyy-MM-dd Format",
                        Toast.LENGTH_SHORT
                    ).show()
                    409 -> Toast.makeText(
                        this@MainActivity,
                        "Record already added",
                        Toast.LENGTH_SHORT
                    ).show()
                    else ->
                        Toast.makeText(this@MainActivity, "Internal Error", Toast.LENGTH_SHORT)
                            .show()

                }

            }

        }

    }

    private fun callToDeleteHoliday(name: String) {

        CoroutineScope(Dispatchers.IO).launch {
            val response = network.deleteHoliday(name)

            withContext(Dispatchers.Main) {
                Log.i("Logs", response.toString())
                when (response.code()) {
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

