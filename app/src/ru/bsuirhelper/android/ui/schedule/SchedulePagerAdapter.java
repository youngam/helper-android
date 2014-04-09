package ru.bsuirhelper.android.ui.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.joda.time.DateTime;
import ru.bsuirhelper.android.core.StudentCalendar;

/**
 * Created by Влад on 10.10.13.
 */
class SchedulePagerAdapter extends FragmentStatePagerAdapter {
    private final StudentCalendar mStudentCalendar;
    private final String mGroupId;
    private final int mSubgroup;

    public SchedulePagerAdapter(FragmentManager fm, String groupId, int subgroup) {
        super(fm);
        mStudentCalendar = new StudentCalendar();
        mGroupId = groupId;
        mSubgroup = subgroup;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle args = new Bundle();
        args.putInt("day", i + 1);
        args.putString("groupId", mGroupId);
        args.putInt("subgroup", mSubgroup);
        Fragment fragment = new FragmentScheduleOfDay();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String dateTime = "";
        DateTime day = StudentCalendar.convertToDefaultDateTime(position + 1);
        String nameDayOfWeek;
        if (day.getDayOfYear() == DateTime.now().getDayOfYear()) {
            nameDayOfWeek = "Сегодня";
        } else {
            nameDayOfWeek = day.dayOfWeek().getAsText();
            char firstCharacter = Character.toUpperCase(nameDayOfWeek.charAt(0));
            nameDayOfWeek = firstCharacter + nameDayOfWeek.substring(1);

        }
        dateTime += nameDayOfWeek;
        return dateTime;
    }

    @Override
    public int getCount() {
        return mStudentCalendar.getDaysOfYear();
    }
}
