package com.example.alert;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

public class MyRingtoneManager {
	public static MediaPlayer player;
    public static void SoundPlayer(AssetFileDescriptor afd) {
    	player = new MediaPlayer();
    	long start = afd.getStartOffset();
    	long end = afd.getLength();
    	try {
			player.setDataSource(afd.getFileDescriptor(), start, end);
		} catch (IllegalArgumentException | IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	player.setOnPreparedListener(new OnPreparedListener() {
    		public void onPrepared(MediaPlayer p) {
    		    player.start();
    		}
    	});
    	//player = MediaPlayer.create(context, raw_id);
        player.setLooping(true); // Set looping
        //player.setVolume(100, 100);
    	player.prepareAsync();
    	
        //player.release();
        //player.start();
    }
}
