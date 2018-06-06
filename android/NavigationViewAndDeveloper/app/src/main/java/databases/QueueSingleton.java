package databases;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Houbenz on 26/05/2018.
 */

public class QueueSingleton {

private static QueueSingleton mInstance;
private RequestQueue mrequestQueue;
private static Context mContext;


        private QueueSingleton(Context context){
            mContext=context;
            mrequestQueue=getRequestQueue();
        }

    public static synchronized QueueSingleton getInstance(Context context) {
            if(mInstance==null){
                mInstance=new QueueSingleton(context);
            }
            return mInstance;

    }

    public RequestQueue getRequestQueue(){
        if(mrequestQueue==null){
            mrequestQueue=Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mrequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
    getRequestQueue().add(request);
    }
}
