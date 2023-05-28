package ru.mirea.allik.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.allik.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void ipOnClick(View view) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadPageTask().execute("https://ipinfo.io/json"); // запуск нового потока
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    public void weatherClick(View view) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {

            new Weather().execute("https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&current_weather=true");

        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            binding.textView.setText("Загружаем...");
            binding.ipText.setText("Загружаем...");
            binding.hostText.setText("Загружаем...");
            binding.cityText.setText("Загружаем...");
            binding.regText.setText("Загружаем...");
            binding.countryText.setText("Загружаем...");
            binding.locText.setText("Загружаем...");
            binding.orgText.setText("Загружаем...");
            binding.postalText.setText("Загружаем...");
            binding.tZoneText.setText("Загружаем...");
            binding.readmeText.setText("Загружаем...");
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
//            binding.textView.setText(result);
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);
                String ip = responseJson.getString("ip");
                Log.d(MainActivity.class.getSimpleName(), "IP: " + ip);
                binding.ipText.setText("ip: " + responseJson.getString("ip"));
                binding.hostText.setText("Host Name: " + responseJson.getString("hostname"));
                binding.cityText.setText("City: " + responseJson.getString("city"));
                binding.regText.setText("Region: " +responseJson.getString("region"));
                binding.countryText.setText("Country: " + responseJson.getString("country"));
                binding.locText.setText("Location: " + responseJson.getString("loc"));
                binding.orgText.setText("Org: " + responseJson.getString("org"));
                binding.postalText.setText("Postal: " +responseJson.getString("postal"));
                binding.tZoneText.setText("Time Zone: " + responseJson.getString("timezone"));
                binding.readmeText.setText("Read Me: " + responseJson.getString("readme"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read); }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage()+". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }

    private class Weather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            binding.textView.setText("Загружаем...");
            binding.tZoneAbbrText.setText("Загружаем...");
            binding.timeText.setText("Загружаем...");
            binding.tempText.setText("Загружаем...");
            binding.elevationText.setText("Загружаем...");
            binding.wSpeedText.setText("Загружаем...");
            binding.wDirText.setText("Загружаем...");
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
//            binding.textView.setText(result);
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);
                JSONObject weather = responseJson.getJSONObject("current_weather");
//                String ip = responseJson.getString("ip");
//                Log.d(MainActivity.class.getSimpleName(), "IP: " + ip);
                binding.tZoneAbbrText.setText("Time Zone Abbreviation: " + responseJson.getString("timezone_abbreviation"));
                binding.timeText.setText("Time: " + weather.getString("time"));
                binding.tempText.setText("Temperature: " + weather.getString("temperature"));
                binding.elevationText.setText("Elevation: " + responseJson.getString("elevation"));
                binding.wSpeedText.setText("Wind Speed: " + weather.getString("windspeed"));
                binding.wDirText.setText("Wind Direction: " + weather.getString("winddirection"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
}


