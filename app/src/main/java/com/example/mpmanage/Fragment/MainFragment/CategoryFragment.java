package com.example.mpmanage.Fragment.MainFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Adapter.CategoryAdapter;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.R;

import java.util.ArrayList;
import java.util.Locale;


public class CategoryFragment extends Fragment {

    View view;
    RecyclerView rvChuDe, rvTheLoai;
    CategoryAdapter adapterChuDe, adapterTheLoai;
    ArrayList<ChuDeTheLoai> arrayListChuDe, arrayListTheLoai;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chu_de, container, false);
        AnhXa();
        SetDataChuDe();
        SetDataTheLoai();
        return view;
    }

    private void AnhXa() {
        rvChuDe = view.findViewById(R.id.rv_chude);
        rvTheLoai = view.findViewById(R.id.rv_theloai);
    }

    private void SetDataChuDe() {
        if(arrayListChuDe != null){
            SetRvChuDe();
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if(MainActivity.chuDeArrayList != null){
                    arrayListChuDe = MainActivity.chuDeArrayList;
                    SetRvChuDe();
                    handler.removeCallbacks(this);
                }
            }
        }, 120);
    }

    private void SetRvChuDe() {
        adapterChuDe = new CategoryAdapter(getContext(), arrayListChuDe, "chude");
        rvChuDe.setAdapter(adapterChuDe);
        rvChuDe.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    private void SetDataTheLoai() {
        if(arrayListTheLoai != null){
            SetRvTheLoai();
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if(MainActivity.theLoaiArrayList != null){
                    arrayListTheLoai = MainActivity.theLoaiArrayList;
                    SetRvTheLoai();
                    handler.removeCallbacks(this);
                }
            }
        }, 120);
    }

    private void SetRvTheLoai() {
        adapterTheLoai = new CategoryAdapter(getContext(), arrayListTheLoai, "chude");
        rvTheLoai.setAdapter(adapterTheLoai);
        rvTheLoai.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

    }


}