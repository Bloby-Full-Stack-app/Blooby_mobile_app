package tn.esprit.looby.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.looby.R
import tn.esprit.looby.models.Music

class MusicActivity : AppCompatActivity() {

    var backgroundIV: ImageView? = null;
    var albumartIV: ImageView? = null;
    var playBtn: Button? = null;
    var backBtn: Button? = null;
    var nextBtn: Button? = null;
    var titleTV: TextView? = null;
    var artistTV: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        backgroundIV = findViewById(R.id.backgroundIV)
        albumartIV = findViewById(R.id.albumartIV)
        playBtn = findViewById(R.id.playBtn)
        backBtn = findViewById(R.id.backBtn)
        nextBtn = findViewById(R.id.nextBtn)
        titleTV = findViewById(R.id.titleTV)
        artistTV = findViewById(R.id.artistTV)

        val music = intent.getSerializableExtra("music") as? Music

        titleTV!!.text = music!!.title
        artistTV!!.text = music.title
    }
}