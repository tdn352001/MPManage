package com.example.mpmanage.Fragment.LoginFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mpmanage.Model.Md5;
import com.example.mpmanage.R;
import com.example.mpmanage.Service.APIService;
import com.example.mpmanage.Service.DataService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {


    View view;
    TextInputEditText edtPW, edtCF;
    MaterialButton btnCf;
    TextView btnLogin;
    String IdAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        AnhXa();
        GetUser();
        EventClick();
        return view;
    }

    private void AnhXa() {
        edtPW = view.findViewById(R.id.edt_change_password);
        edtCF = view.findViewById(R.id.edt_password_confirm);
        btnCf = view.findViewById(R.id.btn_changelogin);
        btnLogin = view.findViewById(R.id.txt_change_login);
    }

    private void GetUser() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            IdAdmin = bundle.getString("idadmin");

        }
    }

    private void EventClick() {
        btnLogin.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_loginFragment));

        btnCf.setOnClickListener(v -> {
            String password = edtPW.getText().toString();
            String cpassword = edtCF.getText().toString();
            if (password.equals("")) {
                {
                    edtPW.setError("Vui L??ng Nh???p M???t Kh???u M???i!");
                    Toast.makeText(getContext(), "Vui L??ng Nh???p M???t Kh???u M???i!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (password.length() < 6) {
                    edtPW.setError("M???t Kh???u C?? T???i Thi???u 6 K?? T???!");
                    Toast.makeText(getContext(), "M???t Kh???u C?? T???i Thi???u 6 K?? T???!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(cpassword)) {
                        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "??ang Th???c Hi???n", "Vui L??ng Ch???....!!!", false, false);
                        DataService dataService = APIService.getService();
                        Call<String> callback = dataService.ChangePassword(IdAdmin, Md5.endcode(password));
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = (String) response.body();
                                if (result.equals("Thanh Cong"))
                                    Toast.makeText(getContext(), "Thay ?????i M???t Kh???u Th??nh C??ng", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_loginFragment);
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progressDialog.dismiss();
                                Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_loginFragment);
                                Toast.makeText(getContext(), "L???i M???ng! Vui L??ng Th??? L???i", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        edtCF.setText("");
                        edtCF.setError("M???t Kh???u Kh??ng Tr??ng Kh???p");
                        Toast.makeText(getContext(), "M???t Kh???u Kh??ng Tr??ng Kh???p", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}