package com.sanderson82.picarcontroller;

import android.os.AsyncTask;

import com.camera.simplemjpeg.MjpegInputStream;
import com.camera.simplemjpeg.MjpegView;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Casa on 12/3/2014.
 */
public enum WebCamController {
    INSTANCE;

    private int port;
    private String host;
    private MjpegView mv = null;

    public void setHostPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect(MjpegView mv) {
        this.mv = mv;
        new DoRead().execute(host, Integer.toString(port));
    }

    public void resumePlayback() {
        if (mv == null) return;
        mv.resumePlayback();
    }

    public void stopPlayback() {
        if (mv == null) return;
        mv.stopPlayback();
    }

    public void freeMemory() {
        if (mv == null) return;
        mv.freeCameraMemory();
    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... params) {
            Socket socket = null;
            try {
                socket = new Socket(params[0], Integer.valueOf(params[1]));
                return (new MjpegInputStream(socket.getInputStream()));
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            if (result != null) result.setSkip(1);
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(true);
        }
    }
}
