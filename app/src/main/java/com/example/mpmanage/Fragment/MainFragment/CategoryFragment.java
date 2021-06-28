package com.example.mpmanage.Fragment.MainFragment;

import android.annotation.SuppressLint;
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
    @SuppressLint("StaticFieldLeak")
    static CategoryAdapter adapterChuDe, adapterTheLoai;
    static ArrayList<ChuDeTheLoai> arrayListChuDe, arrayListTheLoai;
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
        adapterTheLoai = new CategoryAdapter(getContext(), arrayListTheLoai, "theloai");
        rvTheLoai.setAdapter(adapterTheLoai);
        rvTheLoai.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    public static void AddCategory(String loai, ChuDeTheLoai category){
        if(loai.equals("chude")){
            arrayListChuDe.add(category);
            adapterChuDe.notifyDataSetChanged();
        }else{
            arrayListTheLoai.add(category);
            adapterTheLoai.notifyDataSetChanged();
        }
    }

    public static void UpdateCategoty(String loai, ChuDeTheLoai category){
        if(loai.equals("chude")){
            int index = arrayListChuDe.indexOf(category);
            if(index == -1)
                return;
            arrayListChuDe.get(index).setTen(category.getTen());
            arrayListChuDe.get(index).setHinh(category.getHinh());
            adapterChuDe.notifyItemChanged(index);
        }else{
            int index = arrayListTheLoai.indexOf(category);
            if(index == -1)
                return;
            arrayListTheLoai.get(index).setTen(category.getTen());
            arrayListTheLoai.get(index).setHinh(category.getHinh());
            adapterTheLoai.notifyItemChanged(index);
        }
    }

    public static void DeleteCategory(String loai, ChuDeTheLoai category){
        if(loai.equals("chude")){
            int index = arrayListChuDe.indexOf(category);
            if(index == -1)
                return;
            arrayListChuDe.remove(index);
            adapterChuDe.notifyItemRemoved(index);
            adapterChuDe.notifyItemRangeChanged(index, arrayListChuDe.size());
        }else{
            int index = arrayListTheLoai.indexOf(category);
            if(index == -1)
                return;
            arrayListTheLoai.remove(index);
            adapterTheLoai.notifyItemRemoved(index);
            adapterTheLoai.notifyItemRangeChanged(index, arrayListTheLoai.size());
        }
    }


}