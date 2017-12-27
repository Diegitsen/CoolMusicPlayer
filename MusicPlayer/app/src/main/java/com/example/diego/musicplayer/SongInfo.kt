package com.example.diego.musicplayer

/**
 * Created by diego on 27/12/17.
 */

class SongInfo
{
    var song:String? = null
    var author:String? = null
    var songURL:String? = null

    constructor(song:String, author:String, songURL:String)
    {
        this.song = song
        this.author = author
        this.songURL = songURL
    }
}
