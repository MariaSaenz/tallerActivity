package my.jviracocha.talleractivity;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton instanceVolley;
    private RequestQueue requestQueue;
    // private ImageLoader imageLoader;
    private static Context contexto;

    private VolleySingleton(Context context) {
        contexto= context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(contexto.getApplicationContext());
        }

        return  requestQueue;
    }

    public static  synchronized VolleySingleton getInstanceVolley(Context context) {
        if(instanceVolley == null){
            instanceVolley=new VolleySingleton(context);
        }
        return instanceVolley;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
