package ru.bsuirhelper.android.appwidget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import org.joda.time.DateTime;
import ru.bsuirhelper.android.ApplicationSettings;
import ru.bsuirhelper.android.R;
import ru.bsuirhelper.android.core.schedule.Lesson;
import ru.bsuirhelper.android.core.schedule.ScheduleManager;

/**
 * Created by Влад on 15.10.13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class ScheduleFactoryViews implements RemoteViewsService.RemoteViewsFactory {
    private final ScheduleManager mScheduleManager;
    private Lesson[] mLessons;
    private final Context mContext;
    private final Intent mIntent;
    private final int mWidgetId;
    private int mLessonCount;
    private final ApplicationSettings mSettings;

    public ScheduleFactoryViews(Context context, Intent intent) {
        mScheduleManager = ScheduleManager.getInstance(context);
        mIntent = intent;
        mWidgetId = mIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        mContext = context;
        mSettings = ApplicationSettings.getInstance(context);
    }

    @Override
    public void onCreate() {
        updateLessons();
    }

    @Override
    public void onDataSetChanged() {
        updateLessons();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mLessonCount;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_view_lesson);
        Lesson lesson = mLessons[position];
        rView.setTextViewText(R.id.widget_lesson_name, lesson.fields.get("subject"));
        rView.setTextViewText(R.id.widget_lesson_time, lesson.fields.get("timePeriod"));
        rView.setTextViewText(R.id.widget_lesson_teacher, lesson.fields.get("teacher"));

        if (!lesson.fields.get("auditorium").equals("")) {
            rView.setTextViewText(R.id.widget_lesson_auditorium, lesson.fields.get("auditorium"));
        }

        String lessonType = lesson.fields.get("subjectType");

        if (lessonType.equals("лр")) {
            rView.setInt(R.id.widget_lesson_type_color, "setBackgroundColor", (Color.parseColor("#FF4444")));
        } else if (lessonType.equals("пз")) {
            rView.setInt(R.id.widget_lesson_type_color, "setBackgroundColor", (Color.parseColor("#FFBB33")));
        } else if (lessonType.equals("лк")) {
            rView.setInt(R.id.widget_lesson_type_color, "setBackgroundColor", (Color.parseColor("#99CC00")));
        } else {
            rView.setInt(R.id.widget_lesson_type_color, "setBackgroundColor", (Color.WHITE));
        }

        rView.setInt(R.id.widget_separateline, "setBackgroundColor", Color.WHITE);

        return rView;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void updateLessons() {
        String groupId = mSettings.getString("defaultgroup", null);
        int subgroup = mSettings.getInt(groupId, 1);
        if (!mScheduleManager.isLessonsEndToday(groupId, subgroup)) {
            mLessons = mScheduleManager.getLessonsOfDay(groupId, DateTime.now(), subgroup);
        } else {
            mLessons = mScheduleManager.getLessonsOfDay(groupId, new DateTime().plusDays(1), subgroup);
        }
        mLessonCount = mLessons.length;
    }
}
