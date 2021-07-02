package com.example.mpmanage.Fragment.MainFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Activity.UpdateCategoryActivity;
import com.example.mpmanage.Adapter.CategoryAdapter;
import com.example.mpmanage.Model.ChuDeTheLoai;
import com.example.mpmanage.R;

import java.util.ArrayList;


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
        setHasOptionsMenu(true);
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
        LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvChuDe.setLayoutAnimation(animlayout);
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
        LayoutAnimationController animlayout = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_left_to_right);
        rvTheLoai.setLayoutAnimation(animlayout);
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
            int i = 0;
            for(i = 0; i < arrayListChuDe.size(); i++)
                if(arrayListChuDe.get(i).getId().equals(category.getId())){
                    break;
                }
            if(i >= arrayListChuDe.size())
                return;
            Log.e("BBB", "Cap nhat chu de: " + category.getTen());
            arrayListChuDe.get(i).setTen(category.getTen());
            arrayListChuDe.get(i).setHinh(category.getHinh());
            adapterChuDe.notifyItemChanged(i);
        }else{
            int i = 0;
            for(i = 0; i < arrayListTheLoai.size(); i++)
                if(arrayListTheLoai.get(i).getId().equals(category.getId())){
                    break;
                }
            if(i >= arrayListTheLoai.size())
                return;
            arrayListTheLoai.get(i).setTen(category.getTen());
            arrayListTheLoai.get(i).setHinh(category.getHinh());
            adapterTheLoai.notifyItemChanged(i);
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

    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_category, menu);

        MenuItem addchude = menu.findItem(R.id.add_chu_de);
        addchude.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), UpdateCategoryActivity.class);
            intent.putExtra("loai", "chude");
            startActivity(intent);
            return true;
        });

        MenuItem addtheloai = menu.findItem(R.id.add_the_loai);
        addtheloai.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), UpdateCategoryActivity.class);
            intent.putExtra("loai", "theloai");
            startActivity(intent);
            return true;
        });


        super.onCreateOptionsMenu(menu, inflater);
    }



}