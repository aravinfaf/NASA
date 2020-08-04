package com.juno.nasaphotooftheday.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.juno.nasaphotooftheday.R
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener


class ViewActivity : AppCompatActivity() {

    lateinit var youtubePlayerView: YouTubePlayerView
    lateinit var full_image: ImageView
    lateinit var media_type: String
    lateinit var data: String
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var toolbar_title:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        toolbar=findViewById(R.id.toolbar)
        toolbar_title=findViewById(R.id.toolbar_title)
        full_image = findViewById(R.id.full_IV)
        youtubePlayerView = findViewById(R.id.youtube_player_view)

        toolbar_title.text="Fullscreen"

        media_type = intent.getStringExtra("media_type")
        data = intent.getStringExtra("data")

        Log.e("DDD", media_type + "/" + data)

        if (media_type.equals("image",true)){
            youtubePlayerView.visibility=View.GONE
            full_image.visibility=View.VISIBLE
            showImage(data)
        }else{
            youtubePlayerView.visibility=View.VISIBLE
            full_image.visibility=View.GONE
            youTubePlayer(data)
        }

    }

    private fun showImage(data: String?) {

        Glide
            .with(this)
            .load(this.data)
            .centerCrop()
            .placeholder(R.drawable.ic_image_placeholder)
            .into(full_image);

    }

    private fun youTubePlayer(data: String?) {

        getLifecycle().addObserver(youtubePlayerView);

        youtubePlayerView.initialize({ initializedYouTubePlayer ->
            initializedYouTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                override fun onReady() {
                    val videoId = this@ViewActivity.data
                    initializedYouTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }, true)
    }
}
