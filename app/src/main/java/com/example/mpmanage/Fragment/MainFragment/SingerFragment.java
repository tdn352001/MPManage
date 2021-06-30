package com.example.mpmanage.Fragment.MainFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpmanage.Activity.AddSongActivity;
import com.example.mpmanage.Activity.MainActivity;
import com.example.mpmanage.Activity.UpdateCaSiActivity;
import com.example.mpmanage.Adapter.SingerAdapter;
import com.example.mpmanage.Model.CaSi;
import com.example.mpmanage.R;

import java.util.ArrayList;

import static android.view.View.GONE;

public class SingerFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    TextView title;
    static ArrayList<CaSi> arrayList;
    static SingerAdapter adapter;
    SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_singer, container, false);
        AnhXa();
        GetDanhSachCaSi();
        setHasOptionsMenu(true);
        return view;
    }

    private void AnhXa() {
        title = view.findViewById(R.id.txt_casi);
        relativeLayout = view.findViewById(R.id.txt_noinfo_add_online);
        recyclerView = view.findViewById(R.id.rv_casi);
    }

    private void GetDanhSachCaSi() {
        if(arrayList != null){
            SetRV();
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 120);
                if(MainActivity.caSiArrayList != null){
                    arrayList = MainActivity.caSiArrayList;
                    SetRV();
                    handler.removeCallbacks(this);
                }
            }
        }, 120);
    }

    private void SetRV() {
        adapter = new SingerAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    public static void UpdateCaSi(CaSi caSi){
        int i = 0;
        for(i =0; i <arrayList.size(); i++)
            if(arrayList.get(i).getIdCaSi().equals(caSi.getIdCaSi()))
            {
                arrayList.get(i).setTenCaSi(caSi.getTenCaSi());
                arrayList.get(i).setHinhCaSi(caSi.getHinhCaSi());
                adapter.notifyItemChanged(i);
                break;
            }
    }
    public static void AddCaSi(CaSi caSi){
        arrayList.add(caSi);
        adapter.notifyDataSetChanged();
    }
    public static void DeleteCaSi(CaSi caSi){
        int i = arrayList.indexOf(caSi);
        if(i != -1){
            arrayList.remove(i);
            adapter.notifyItemRemoved(i);
            adapter.notifyItemRangeChanged(i, arrayList.size());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_view);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Tìm Kiếm Ca Sĩ");
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    title.setText(R.string.list_singer);
                    relativeLayout.setVisibility(GONE);
                } else {
                    title.setText("Tìm Kiếm Từ Khóa: " + newText);
                }
                adapter.getFilter().filter(newText);


                //Kiểm tra kết quả
                final int[] i = {0};
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.postDelayed(this, 100);
                        if (i[0] >= 3)
                            handler.removeCallbacks(this);

                        if (adapter.getItemCount() == 0) {
                            relativeLayout.setVisibility(View.VISIBLE);
                        } else
                            relativeLayout.setVisibility(View.GONE);
                        i[0]++;
                    }
                }, 200);


                return true;
            }
        });

        MenuItem addItem = menu.findItem(R.id.add_item);
        addItem.setTitle(R.string.add_singer);
        addItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), UpdateCaSiActivity.class);
            startActivity(intent);
            return true;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}