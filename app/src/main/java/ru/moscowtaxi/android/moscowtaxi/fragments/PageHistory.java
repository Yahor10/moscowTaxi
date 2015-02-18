package ru.moscowtaxi.android.moscowtaxi.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.activity.FromHistoryActivity;
import ru.moscowtaxi.android.moscowtaxi.activity.MainActivity;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.HistoryListViewAdapter;
import ru.moscowtaxi.android.moscowtaxi.loaders.HistoryLoader;
import ru.moscowtaxi.android.moscowtaxi.orm.OrderHistoryORM;


/**
 * Created by alex-pers on 12/10/14.
 */
public class PageHistory extends Fragment implements LoaderManager.LoaderCallbacks<List<OrderHistoryORM>> {

    public static int LOADER_ID = 100;
    ListView listView;
    HistoryListViewAdapter adapter;

    public PageHistory() {

    }

    public static Fragment newInstance() {
        PageHistory fragment = new PageHistory();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_history);

        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Activity activity = getActivity();
                if (activity instanceof FromHistoryActivity) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MainActivity.KEY_ACTIVITY_RESULT_FROM_HISTORY_data, adapter.getItem(i));
                    intent.putExtras(bundle);
                    activity.setResult(MainActivity.KEY_ACTIVITY_RESULT_FROM_HISTORY, intent);
                    activity.finish();
                }
            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public Loader<List<OrderHistoryORM>> onCreateLoader(int i, Bundle bundle) {
        return new HistoryLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<OrderHistoryORM>> listLoader, List<OrderHistoryORM> orderHistoryORMs) {
        adapter = new HistoryListViewAdapter(getActivity().getLayoutInflater(), orderHistoryORMs);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<OrderHistoryORM>> listLoader) {

    }
}
