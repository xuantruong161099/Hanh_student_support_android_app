package com.meowbeos.studentsupport.fragment.support;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.activity.MainActivity;
import com.meowbeos.studentsupport.activity.account.LoginActivity;
import com.meowbeos.studentsupport.utils.SessionManager;

import java.util.HashMap;


public class SupportFragment extends Fragment {

    private TextView tv_student_name;
    private View view;
    private HashMap<String, String> _hmUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_support, container, false);

        tv_student_name = view.findViewById(R.id.tv_student_name);

        SessionManager mSession = new SessionManager(getContext());

        if (mSession.isLoggedIn()) { // đã đăng nhập
            _hmUser = mSession.getUserDetails();
            tv_student_name.setText(_hmUser.get(SessionManager.DISPLAY_NAME));
        }

        return view;
    }
}