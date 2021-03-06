package ru.bsuirhelper.android.ui.schedule;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import org.joda.time.DateTime;
import ru.bsuirhelper.android.core.StudentCalendar;

import java.util.Calendar;

import static android.app.DatePickerDialog.OnDateSetListener;

/**
 * Created by Влад on 07.11.13.
 */
public abstract class DialogDatePicker extends DialogFragment implements OnDateSetListener {
    private static final String SCHOOL_WEEK = "Учебная неделя ";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        CustomDatePicker datePickerDialog = new CustomDatePicker(getActivity(), this, year, month, day);
        if (Build.VERSION.SDK_INT > 10) {
            datePickerDialog.getDatePicker().setMinDate(StudentCalendar.getStartStudentYear());
            datePickerDialog.getDatePicker().setMaxDate(StudentCalendar.getEndStudentYear());
        }
        return datePickerDialog;
    }

    class CustomDatePicker extends DatePickerDialog {

        public CustomDatePicker(Context context, OnDateSetListener callBack, int year, int month, int day) {
            super(context, callBack, year, month, day);
            setTitle(SCHOOL_WEEK + StudentCalendar.getWorkWeek(new DateTime(year, month + 1, day, 1, 1)));
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            setTitle(SCHOOL_WEEK + StudentCalendar.getWorkWeek(new DateTime(year, month + 1, day, 1, 1)));
        }
    }
}
