package com.meowbeos.studentsupport.fragment.schedule;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.activity.account.LoginActivity;
import com.meowbeos.studentsupport.adapter.marks.SemesterAdapter;
import com.meowbeos.studentsupport.adapter.news.SectionNewsAdapter;
import com.meowbeos.studentsupport.adapter.news.SliderNewsAdapter;
import com.meowbeos.studentsupport.adapter.schedule.ScheduleAdapter;
import com.meowbeos.studentsupport.constants.STConstant;
import com.meowbeos.studentsupport.model.CollectNews;
import com.meowbeos.studentsupport.model.GeneralNews;
import com.meowbeos.studentsupport.model.NewsPins;
import com.meowbeos.studentsupport.model.WeeklySchedule;
import com.meowbeos.studentsupport.service.ServiceAPI;
import com.meowbeos.studentsupport.utils.SessionManager;
import com.meowbeos.studentsupport.utils.StringUtil;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ScheduleFragment extends Fragment {

    private static final String TAG = "News";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerView;
    private final Calendar myCalendar = Calendar.getInstance();

    private View view;
    private TextView textViewDate, emptyView;

    private HashMap<String, String> _hmUser;
    private ScheduleAdapter scheduleAdapter;
    private SessionManager mSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        initComponent();

        mSession = new SessionManager(getContext());

        Date dateNow = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat formatterShow = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(dateNow);
        textViewDate.setText("TKB Ngày: " + formatterShow.format(dateNow));
        if (mSession.isLoggedIn()) { // đã đăng nhập
            _hmUser = mSession.getUserDetails();

            getListSchedule(StringUtil.generateAPIKey(), _hmUser.get(SessionManager.ID), strDate);
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        textViewDate.setText("TKB Ngày: " + sdf.format(myCalendar.getTime()));

        if (mSession.isLoggedIn()) { // đã đăng nhập
            _hmUser = mSession.getUserDetails();

            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            getListSchedule(StringUtil.generateAPIKey(), _hmUser.get(SessionManager.ID), formatter.format(myCalendar.getTime()));
        }
    }

    private void initComponent() {
        recyclerView = view.findViewById(R.id.rc_layout_schedule);
        textViewDate = view.findViewById(R.id.tv_date_request);
        emptyView = view.findViewById(R.id.empty_view);
    }

    private void getListSchedule(String apiKey, String idStudent, String strDate) {

        ServiceAPI serviceAPI = new Retrofit.Builder()
                .baseUrl(STConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceAPI.class);

        compositeDisposable.add(serviceAPI.getListSchedule(apiKey, idStudent, strDate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleResponse(WeeklySchedule weeklySchedule) {
        if (!weeklySchedule.getResultCode().equals("00")) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            scheduleAdapter = new ScheduleAdapter(getContext(), weeklySchedule.getListSchedule());
            recyclerView.setAdapter(scheduleAdapter);

        }
    }

    private void handleError(Throwable throwable) {
        Log.e(TAG, "handleError: Error", throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}