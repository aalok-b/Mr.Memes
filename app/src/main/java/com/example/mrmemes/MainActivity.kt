package com.example.mrmemes

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.lang.Math.abs


class MainActivity : AppCompatActivity(),GestureDetector.OnGestureListener {

    var currentImageUrl: String? = null

    lateinit var gestureDetector: GestureDetector
    var x2:Float = 0.0f
    var x1:Float = 0.0f
    var y2:Float = 0.0f
    var y1:Float = 0.0f

    companion object{
        const val MIN_DISTANCE = 150
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestureDetector = GestureDetector(this, this)

        loadMeme()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        gestureDetector.onTouchEvent(event)

        when(event?.action)
        {

            // when we start to swipe
            0 ->
            {
                x1 = event.x
                y1 = event.y
            }
            // when we end to swipe
            1 ->
            {
                x2 = event.x
                y2 = event.y

                val valueX: Float = x2 - x1
                val valueY: Float = y2 - y1

                if (abs(valueX) > MIN_DISTANCE) {
                    // detect right side swipe
                    if (x2 > x1) {
                        Toast.makeText(this, "Right Swipe", Toast.LENGTH_SHORT).show()

                    }
                    // detect left side swipe
                   else  {

                        loadMeme()
                    }

                }
                else if ( abs(valueY) > MIN_DISTANCE)
                {
                    // detect top to bottom swipe
                    if(y2 > y1){
                        Toast.makeText(this, "Downward Swipe", Toast.LENGTH_SHORT).show()
                    }
                    // detect bottom to top swipe
                    else{
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, "$currentImageUrl")
                        val chooser = Intent.createChooser(intent,"Share this using....")
                        startActivity(chooser)
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun loadMeme(){

        val load = findViewById<ProgressBar>(R.id.load)
        load.visibility = View.VISIBLE
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

         // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener{ response ->
                currentImageUrl = response.getString("url")


                val imageView = findViewById<ImageView>(R.id.imageView)
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        load.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        load.visibility = View.GONE
                        return false
                    }
                }).into(imageView )

            },
            {  })
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }




    fun shareMeme(view: View) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "$currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this using....")
        startActivity(chooser)
    }



    fun nextMeme(view: View) {
        loadMeme()
    }




    // we dont need this just have to implement

    override fun onDown(e: MotionEvent?): Boolean {
        //TODO("Not yet implemented")
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
       // TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
       // TODO("Not yet implemented")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
       // TODO("Not yet implemented")
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
       // TODO("Not yet implemented")
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
       // TODO("Not yet implemented")
        return false
    }


}


