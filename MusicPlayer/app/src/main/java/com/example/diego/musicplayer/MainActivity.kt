package com.example.diego.musicplayer

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {

    var listSong = ArrayList<SongInfo>()
    var myAdapter:MySongAdapter? = null
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playSongs()
        myAdapter = MySongAdapter(listSong)
        lvSongs.adapter = myAdapter

        var myTracking = mySongTrack()
        myTracking.start()
    }

    fun playSongs()
    {
        listSong.add(SongInfo("A Dios sea la gloria", "Cristiana", "http://server.firefighters.org/alabanza/A_Dios_Sea_La_Gloria.mp3"))
        listSong.add(SongInfo("Abre mis ojos señor", "Crisitana", "http://server.firefighters.org/alabanza/Abre_Mis_Ojos_Senor.mp3"))
        listSong.add(SongInfo("Llena tu copa", "Cristiana", "http://server.firefighters.org/alabanza/Llena_Tu_Copa.mp3"))
        listSong.add(SongInfo("Señor te doy mi ser", "Cristiana", "http://server.firefighters.org/alabanza/Senor_Te_Doy_Mi_Ser.mp3"))
    }


    //with "INNER" i can get any funciton or element of the Main Activity class, with out the inner
    //it can not be possible
    inner class MySongAdapter: BaseAdapter
    {
        var myListSong = ArrayList<SongInfo>()

        constructor(myListSong:ArrayList<SongInfo>):super()
        {
            this.myListSong = myListSong
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.song_ticket, null)
            val song = this.myListSong[position]
            myView.tvSong.text = song.song
            myView.tvAutor.text = song.author

            myView.bPlay.setOnClickListener(View.OnClickListener {


                if(myView.bPlay.text.equals("X"))
                {
                    mp!!.stop()
                    //myView.bPlay.setBackgroundResource(R.drawable.playb)
                    myView.bPlay.text = "O"
                }
                else
                {
                    mp = MediaPlayer()
                    try
                    {
                        mp!!.setDataSource(song.songURL)
                        mp!!.prepare()
                        mp!!.start()
                       // myView.bPlay.setBackgroundResource(R.drawable.close)
                        myView.bPlay.text = "X"
                        sbProgress.max = mp!!.duration
                    }
                    catch(ex:Exception){

                    }
                }


            })

            return myView
        }

        override fun getItem(item: Int): Any {
            return this.myListSong[item]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return this.myListSong.size
        }

    }

    inner class mySongTrack():Thread()
    {


        override fun run()
        {
            while(true)
            {
                try
                {
                    Thread.sleep(1000)
                }
                catch (ex:Exception)
                {
                }

                runOnUiThread {
                    if(mp!=null)
                    {
                        sbProgress.progress = mp!!.currentPosition
                    }
                }
            }
        }
    }

}





