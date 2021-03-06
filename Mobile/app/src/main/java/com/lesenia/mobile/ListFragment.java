package com.lesenia.mobile;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private PharmacyAdapter adapter;

    public ListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(Objects.requireNonNull(getView()));
        registerNetworkMonitoring();
        loadPharmacy();
    }

    private void registerNetworkMonitoring() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(linearLayout);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
    }

    private void initViews(View root) {
        recyclerView = root.findViewById(R.id.list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linearLayout = root.findViewById(R.id.linearLayout);
        swipeRefreshLayout = root.findViewById(R.id.list_swipe_refresh);
        setupSwipeToRefresh();
    }

    private void setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadPharmacy();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void loadPharmacy() {
        swipeRefreshLayout.setRefreshing(true);
        final PharmacyApi pharmacyApi = getRetrofitEx().getPharmacyApi();
        final Call<List<Pharmacy>> call = pharmacyApi.getPharmacy();

        call.enqueue(new Callback<List<Pharmacy>>() {
            @Override
            public void onResponse(final Call<List<Pharmacy>> call,
                                   final Response<List<Pharmacy>> response) {
                adapter = new PharmacyAdapter(getActivity(), response.body());
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
                Snackbar.make(linearLayout, R.string.update_failure, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private RetrofitEx getRetrofitEx() {
        return ((RetrofitEx) Objects.requireNonNull(getActivity()).getApplication());
    }
}
