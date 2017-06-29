package laravel.test.com.gpstracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import server.AppController;
import server.Manager;
import server.Url;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    String myJSON;
    JSONArray peoples = null;

    MapView osm;
    MapController mc;
    LocationManager locationManager;
    TextView text;
    Marker marker;
    GeoPoint center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getData();
    }


    private void mapController(double Latitude, double Longitut, String Mark){
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        center = new GeoPoint(Latitude, Longitut);
        osm = (MapView) findViewById(R.id.map);
        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc = (MapController) osm.getController();
        mc.setZoom(15);
        mc.animateTo(center);
        marker = new Marker(osm);
        marker.setPosition(center);

        //marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.marker_angkot));
        marker.setTitle(Mark);
        //rabbit();
        osm.getOverlays().clear();
        osm.getOverlays().add(marker);
        osm.invalidate();

        //marker.setOnMarkerClickListener((Marker.OnMarkerClickListener) this);
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }


    /*protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray("result");

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String mac = c.getString("MAC");
                String speed = c.getString("SPEED");
                String jam = c.getString("JAM");
                String tgl = c.getString("TGL");
                String lati = c.getString("LAT");
                String longit = c.getString("LONGI");
                double latparse = Double.parseDouble(lati);
                double longitparse = Double.parseDouble(longit);
                HashMap<String,String> persons = new HashMap<String,String>();

                persons.put("Mac",mac);
                persons.put("Date",tgl);
                persons.put("Time",jam);
                persons.put("Speed",speed);
                persons.put("data", lati);
                persons.put("data", longit);

                String calculate = "-Speed: "+speed+"\n"+
                        "-Tgl      : "+tgl+"\n"+
                        "-Jam    : "+jam;
                mapController(latparse, longitparse, calculate);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.43.244:8012/GpsTracker/ShowMarker.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }*/

   public void getData() {

        //Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Please Wait..... \n" +
                            "Connection is not stable");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Url.VIEW_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(MainActivity.class.getSimpleName(), "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        JSONArray array = jObj.getJSONArray("tracker");

                        //for(int i=0; i < array.length(); i++){
                        int i=0;
                        while (i<array.length()) {
                            JSONObject data = array.getJSONObject(i);
                            String mac = data.getString("Mac");
                            String speed = data.getString("Speed");
                            String tgl = data.getString("Date");
                            String jam = data.getString("Time");
                            String lokasi = data.getString("Lokasi");
                            JSONArray latlong = data.getJSONArray("Data");
                            double lat = latlong.getDouble(0);
                            double longi = latlong.getDouble(1);
                            String calculate = "->Speed : " + speed + "\n" +
                                    "->Tgl       : " + tgl + "\n" +
                                    "->Jam     : " + jam + "\n" +
                                    "->Lokasi : " + lokasi;

                            //if(mac.equals("60: 1:94: 0:51:C8")){

                            mapController(lat, longi, calculate);
                            i++;
                        }


                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_SHORT).show();

                    } else {
                        String error_msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                error_msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(MainActivity.class.getSimpleName(), "Login Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //Toast.makeText(LoginActivity.this, "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("SessionID", "309dc715c393f672728824ec8e4edea7");

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}