package com.example.mpmanage.Fragment.LoginFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.mpmanage.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ConfirmCodeFragment extends Fragment {

    View view;
    TextInputEditText edtCode;
    MaterialButton btnCf;
    TextView btnLogin;
    int Code;
    String IdUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm_code, container, false);
        AnhXa();
        GetCode();
        EventClick();
        return view;
    }


    private void AnhXa() {
        edtCode = view.findViewById(R.id.edt_code_cf);
        btnCf = view.findViewById(R.id.btn_cf_code);
        btnLogin = view.findViewById(R.id.txt_cf_login);
    }

    private void GetCode() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Code = bundle.getInt("code");
            IdUser = bundle.getString("idadmin");
        }
    }

    private void EventClick() {
        btnCf.setOnClickListener(v -> {
            String Vcode = edtCode.getText().toString();
            if (Vcode.equals(Code + "")) {
                Bundle bundle = new Bundle();
                bundle.putString("idadmin", IdUser);
                Navigation.findNavController(view).navigate(R.id.action_confirmCodeFragment_to_changePasswordFragment, bundle);
            } else {
                if (Vcode.equals("")) {
                    edtCode.setError("Vui Lòng Nhập Mã Xác Nhận");
                    Toast.makeText(getContext(), "Vui Lòng Nhập Mã Xác Nhận", Toast.LENGTH_SHORT).show();
                } else {
                    edtCode.setError("Mã Xác Nhận Không Chính Xác");
                    Toast.makeText(getContext(), "Mã Xác Nhận Không Chính Xác", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_confirmCodeFragment_to_loginFragment));

    }
}