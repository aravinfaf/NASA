package com.juno.nasaphotooftheday.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.juno.nasaphotooftheday.R
import com.juno.nasaphotooftheday.viewmodel.ApiViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class HomeActivity : AppCompatActivity() {

    lateinit var progressbar:ProgressBar
    var viewModel: ApiViewModel? = null
    lateinit var youtube_thumbnail:String
    lateinit var youtube_videoID:String

    lateinit var title_TV:TextView
    lateinit var descriptionTV:TextView
    lateinit var calendar_TV:TextView
    lateinit var image_IV:ImageView
    lateinit var action_IV:ImageView

    lateinit var media_type:String
    lateinit var filtered_data:String
    private var errorSnackbar : Snackbar?=null
    lateinit var relativeLL:RelativeLayout
    lateinit var toolbar: Toolbar
    lateinit var toolbar_title:TextView

    var cal = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.toolbar)
        toolbar_title=findViewById(R.id.toolbar_title)
        relativeLL=findViewById(R.id.relativeLL)
        progressbar=findViewById(R.id.progressbar)
        title_TV=findViewById(R.id.title_TV)
        descriptionTV=findViewById(R.id.description_TV)
        calendar_TV=findViewById(R.id.calendar_TV)
        action_IV=findViewById(R.id.action_IV)
        image_IV=findViewById(R.id.image_IV)

        toolbar_title.text= "Home"

        if(isOnline(this)) {

            viewModel = ViewModelProviders.of(this).get(ApiViewModel::class.java)
            viewModel!!.getDatafromApi("DEMO_KEY", "")
            progressbar.visibility = View.VISIBLE
        }else{
            showError("No Internet Connection!!!")
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }


        calendar_TV.setOnClickListener(View.OnClickListener {

            if (isOnline(this)) {

                DatePickerDialog(
                    this@HomeActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }else{
                showError("No Internet Connection!!!")
            }
        })


        if (isOnline(this)){
            getValue()
        }else{
            showError("No Internet Connection!!!")
        }

        action_IV.setOnClickListener(View.OnClickListener {

            if(isOnline(this)) {

                val intent: Intent = Intent(applicationContext, ViewActivity::class.java)
                intent.putExtra("media_type", media_type)
                intent.putExtra("data", filtered_data)
                startActivity(intent)
            }else{
                showError("No Internet Connection!!!")
            }
        })
    }

    private fun getValue(){
        viewModel!!.getLiveData().observe(this, Observer {

            progressbar.visibility=View.INVISIBLE


            title_TV.text=it.title
            descriptionTV.text=it.explanation

            if (it.media_type.equals("image",true)){

                action_IV.setImageResource(R.drawable.ic_zoom_in)

                Glide
                    .with(this)
                    .load(it.url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(image_IV);

                media_type=it.media_type
                filtered_data=it.url

            }else if(it.media_type.equals("video",true)){

                action_IV.setImageResource(R.drawable.ic_baseline_play_arrow_24)

                val url: String? =getYouTubeId("https://www.youtube.com/embed/I_88S8DWbcU?rel=0")

                youtube_videoID=getYouTubeId(it.url)

                youtube_thumbnail="https://img.youtube.com/vi/"+youtube_videoID+"/default.jpg".trim()


                Glide
                    .with(this)
                    .load(youtube_thumbnail)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .into(image_IV);

                media_type=it.media_type
                filtered_data=youtube_videoID
            }
        })
        viewModel!!.errorMessage.observe(this, Observer {
            error_message->if(error_message!=null){
                showError("No Internet Connection!!!")
        }
        })

    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        calendar_TV!!.text = sdf.format(cal.getTime())

        viewModel?.getDatafromApi("DEMO_KEY",calendar_TV.text.toString().trim())
        progressbar.visibility=View.VISIBLE

    }

    private fun getYouTubeId(youTubeUrl: String): String {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun showError( erroMessage: String){
        errorSnackbar = Snackbar.make(relativeLL,erroMessage, Snackbar.LENGTH_SHORT)
        errorSnackbar!!.show()
    }

}