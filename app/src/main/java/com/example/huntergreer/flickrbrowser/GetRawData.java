package com.example.huntergreer.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    private DownloadStatus mDownloadStatus;
    private final OnDownloadComplete mCallBack;

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callback) {
        mDownloadStatus = DownloadStatus.IDLE;
        mCallBack = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        } else {
            try {
                StringBuilder res = new StringBuilder();
                mDownloadStatus = DownloadStatus.PROCESSING;
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int response = connection.getResponseCode();
                Log.d(TAG, "doInBackground: response code was " + response);

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    res.append(line).append("\n");
                }
                mDownloadStatus = DownloadStatus.OK;
                return res.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "doInBackground: Invalid URL... \n" + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "doInBackground: IO Exception... \n " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "doInBackground: Security exception. Needs permission? " + e.getMessage());
            } finally {
                if (connection != null) connection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "doInBackground: Error closing stream... \n " + e.getMessage());
                    }
                }
            }
            mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter is " + s);

        if (mCallBack != null) mCallBack.onDownloadComplete(s, mDownloadStatus);

        Log.d(TAG, "onPostExecute: ends");
    }
}
