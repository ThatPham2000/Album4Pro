package com.example.album4pro.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;
import com.example.album4pro.privates.CreatePasswordActivity;
import com.example.album4pro.privates.EnterPasswordActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrivateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivateFragment extends Fragment {
    private MainActivity mMainActivity;
    private boolean PASSWORD_ENTERED = false;

    String password_Prefs;
    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrivateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivateFragment newInstance(String param1, String param2) {
        PrivateFragment fragment = new PrivateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_private, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Chưa nhập mật khẩu
        if (PASSWORD_ENTERED == false){
            sharedPreferences = getActivity().getSharedPreferences("PASSPREF", Context.MODE_PRIVATE);
            password_Prefs = sharedPreferences.getString("password_tag", "");
            if(password_Prefs.equals("")){
                // there is no password
                Intent intent = new Intent(getActivity(), CreatePasswordActivity.class);
                startActivity(intent);
            } else {
                // there is a password
                Intent intent = new Intent(getActivity(), EnterPasswordActivity.class);
                startActivity(intent);
            }
        }
        // Sau khi nhập mật khẩu
        PASSWORD_ENTERED = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Đã nhập mật khẩu, khi Fragment bị stop bởi 1 Activity khác chiếm giữ, sẽ không phải nhập lại mật khẩu lần nữa
        PASSWORD_ENTERED = true;
    }

}