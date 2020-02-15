package my.jviracocha.talleractivity.FragmentsCliente;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
//implements DatePickerDialog.OnDateSetListener
public class DatePickerFragment extends DialogFragment {


    private DatePickerDialog.OnDateSetListener listener;

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        //=Calendar.getInstance(TimeZone.getDefault());
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 3);
       // c.add(Calendar.YEAR,2);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),listener, year ,month,day );

        //c.set(Calendar.YEAR,year -1);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        // Create a new instance of DatePickerDialog and return it
        return datePickerDialog;//new DatePickerDialog(getActivity(), listener, year, month, day);

        //valor minimo y maximo a mostrar en el data

    }
    //@Override
    //public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    //}
}
