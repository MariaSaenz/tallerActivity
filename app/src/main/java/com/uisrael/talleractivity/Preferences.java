package my.jviracocha.talleractivity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String STRING_PREFERENCES = "jviracocha.talleractivity";
    public static final String PREFERENCE_ESTADO_BUTTON_SESION = "estado.button.sesion";
    public static final String PREFERENCE_USUARIO_LOGIN = "usuario.login";//<-----------lo que se haga con esto se hace con todo

    public static  final  String USUARIO_CORREO="usuario_correo";
    public static  final  String USUARIO_ROL="usuario_rol";
    public static final  String USUARIO_ID="usuaio_id";

    public static void savePreferenceBoolean(Context c, boolean b,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        preferences.edit().putBoolean(key,b).apply();
    }

    public static void savePreferenceString(Context c, String b, String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        preferences.edit().putString(key,b).apply();

    }

    public static boolean obtenerPreferenceBoolean(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        return preferences.getBoolean(key,false);//Si es que nunca se ha guardado nada en esta key pues retornara false
    }

    public static String obtenerPreferenceString(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        return preferences.getString(key,"");//Si es que nunca se ha guardado nada en esta key pues retornara una cadena vacia
    }
    public  static void clearPreference(Context c,String key){
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES,c.MODE_PRIVATE);
        preferences.edit().remove(key).apply();

    }

}
