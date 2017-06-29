package server;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Terminator on 17/01/2017.
 */

public interface Url {
     static final String URL = "http://167.205.7.226:3030/api/tracker/";

     static final String VIEW_DATA = URL + "getalltracker/";

     /*static AsyncHttpClient client = new AsyncHttpClient();

    public static void postAll(RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler){
            client.get(POST_DATA,params,asyncHttpResponseHandler);
    }*/
}
