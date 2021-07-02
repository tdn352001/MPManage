package com.example.mpmanage.Fragment.LoginFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.mpmanage.Model.Admin;
import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Model.BaiHat;
import com.example.mpmanage.Model.Md5;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    ProgressDialog mProgressDialog;
    View view;
    TextInputEditText edtEmail, edtPassword;
    TextInputLayout passwordinput;
    TextView txtForget;
    MaterialButton btnLogin;
    Admin admin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        AnhXa();
        EventClick();
        return view;
    }


    private void AnhXa() {
        edtEmail = view.findViewById(R.id.edt_email_login);
        edtPassword = view.findViewById(R.id.edt_password_login);
        txtForget = view.findViewById(R.id.forget_password);
        btnLogin = view.findViewById(R.id.btn_facebook_login);
        passwordinput = view.findViewById(R.id.edt_input_password_login);
    }

    private void EventClick() {

        //Đăng Nhập
        btnLogin.setOnClickListener(v -> {
            btnLogin.setClickable(false);
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            if (email.equals(""))
                edtEmail.setError("Email trống");
            else {
                if (password.equals(""))
                    edtPassword.setError("Mật Khẩu Trống");
                else {
                    Login(email, password);
                }
            }
        });

        txtForget.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment));

    }

    private void Login(String email, String password) {
        mProgressDialog = ProgressDialog.show(getContext(), "Đang Đăng Nhập", "Vui Lòng Chờ...", true, true);
        DataService dataService = APIService.getService();
        Call<Admin> callback = dataService.Login(email, Md5.endcode(password));
        callback.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                admin = (Admin) response.body();
                btnLogin.setClickable(true);
                if (admin == null) {
                    mProgressDialog.dismiss();
                    edtPassword.setError("Tài khoản hoặc mật khẩu không đúng");
                    Toast.makeText(getContext(), "Tài Khoản Hoặc Mật Khẩu Không Đúng", Toast.LENGTH_SHORT).show();
                    edtPassword.setText("");
                    view.setVisibility(View.VISIBLE);
                } else {
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    Toast.makeText(getContext(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                view.setVisibility(View.VISIBLE);
                mProgressDialog.dismiss();
                btnLogin.setClickable(true);
                Toast.makeText(getContext(), "Đăng Nhập Không Thành Công! Vui Lòng Thử Lại", Toast.LENGTH_SHORT).show();
            }
        });
    }





}