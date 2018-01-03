package com.example.diego.musicplayer

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {

    var listSong = ArrayList<SongInfo>()
    var myAdapter:MySongAdapter? = null
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //playSongs()
        checkUserPermsions()
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


                if(myView.bPlay.background.equals(R.drawable.close))
                {
                    mp!!.stop()
                    myView.bPlay.setBackgroundResource(R.drawable.playb)
                    //myView.bPlay.text = "O"
                }
                else
                {
                    mp = MediaPlayer()
                    try
                    {
                        mp!!.setDataSource(song.songURL)
                        mp!!.prepare()
                        mp!!.start()
                        myView.bPlay.setBackgroundResource(R.drawable.close)
                        //myView.bPlay.text = "X"
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

    /*fun checkUserPermission()
    {
        if(Build.VERSION.SDK_INT>=23)
        {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        ,REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }

        loadSong()
    }*/

    fun checkUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }

        loadSong()

    }

    //get access to location permission
    private val REQUEST_CODE_ASK_PERMISSIONS = 123

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode)
        {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                                                {loadSong()}
                                            else
                                            {
                                                //Permission Denied
                                                Toast.makeText(this, "denail", Toast.LENGTH_SHORT).show()
                                            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }



    }

    fun loadSong()
    {
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allSongsURI, null, selection, null, null)
        if(cursor != null)
        {
            if(cursor!!.moveToFirst())
            {
                do
                {
                    val songURL = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    listSong.add(SongInfo(songName, songAuthor, songURL))
                }while(cursor!!.moveToNext())
            }

            cursor!!.close()

            myAdapter = MySongAdapter(listSong)
            lvSongs.adapter = myAdapter

        }
    }

}





