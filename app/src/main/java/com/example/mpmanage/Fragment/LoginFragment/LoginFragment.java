package com.example.mpmanage.Fragment.LoginFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.mpmanage.Model.Admin;
import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    public static SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        AnhXa();
        AutoLogin();
        EventClick();
        return view;
    }


    private void AnhXa() {
        edtEmail = view.findViewById(R.id.edt_email_login);
        edtPassword = view.findViewById(R.id.edt_password_login);
        txtForget = view.findViewById(R.id.forget_password);
        btnLogin = view.findViewById(R.id.btn_facebook_login);
        passwordinput = view.findViewById(R.id.edt_input_password_login);
        sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
    }

    private void AutoLogin() {
        edtEmail.setText(sharedPreferences.getString("username", ""));
        edtPassword.setText(sharedPreferences.getString("password", ""));
        view.setVisibility(View.INVISIBLE);
        if (!edtPassword.getText().toString().equals("") && !edtEmail.getText().toString().equals("")) {
            {
                Login(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        } else
            view.setVisibility(View.VISIBLE);
    }

    private void EventClick() {


        //Đăng Nhập

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setClickable(false);
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email.equals(""))
                    edtEmail.setError("Email trống");
                else {
                    if (password.equals(""))
                        edtPassword.setError("Mật Khẩu Trống");
                    else {
                        Login(email, password);
                    }
                }
            }
        });

        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });


    }

    private void Login(String email, String password) {
        mProgressDialog = ProgressDialog.show(getContext(), "Đang Đăng Nhập", "Vui Lòng Chờ...", false, false);
        DataService dataService = APIService.getService();
        Call<Admin> callback = dataService.Login(email, password);
        callback.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                admin = (Admin) response.body();
                btnLogin.setClickable(true);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (admin.getIdUser().toString().equals("-1")) {
                    mProgressDialog.dismiss();
                    edtPassword.setError("Tài khoản hoặc mật khẩu không đúng");
                    Toast.makeText(getContext(), "Tài Khoản Hoặc Mật Khẩu Không Đúng", Toast.LENGTH_SHORT).show();
                    edtPassword.setText("");
                    view.setVisibility(View.VISIBLE);
                    editor.remove("username");
                    editor.remove("password");
                    editor.commit();

                } else {
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    editor.putString("username", email);
                    editor.putString("password", password);
                    editor.commit();
                    Toast.makeText(getContext(), "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                view.setVisibility(View.VISIBLE);
            }
        });
    }


}