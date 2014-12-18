
package ru.moscowtaxi.android.moscowtaxi.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.EntityAdapter;
import ru.moscowtaxi.android.moscowtaxi.helpers.adapters.FavoritePlacesAdapter;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;

/**
 * Created by ychabatarou on 04.11.2014.
 */
public class FavoritesListView extends EntityListView<FavoritePlaceORM> implements View.OnClickListener {


    public FavoritesListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Button address = new Button(context);
        address.setOnClickListener(this);
        address.setId(R.id.add_favorite_address);

        Resources resources = getResources();
        address.setBackgroundColor(resources.getColor(R.color.orange_color));
        address.setTextColor(resources.getColor(android.R.color.white));
        address.setGravity(Gravity.CENTER_HORIZONTAL);

        String addFavorite = context.getString(R.string.add_favorites_place);
        address.setText(addFavorite);

        LinearLayout footer = new LinearLayout(context);
        LayoutParams linLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        footer.setLayoutParams(linLayoutParam);
        footer.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 35, 0, 0);
        address.setLayoutParams(layoutParams);

        footer.addView(address);
        addFooterView(footer);
    }

    @Override
    protected EntityAdapter createAdapter() {
        return new FavoritePlacesAdapter(getContext()) {

            @Override
            public List<FavoritePlaceORM> loadData() {
                Context context = getContext();
                Log.v(null,"PLACES1 ");
                List<FavoritePlaceORM> favoritesPlaces = FavoritePlaceORM.getFavoritesPlaces(context);
                if (favoritesPlaces == null) {
                    favoritesPlaces = new ArrayList<FavoritePlaceORM>();
                }
//                favoritesPlaces.add(new FavoritePlaceORM());
//                favoritesPlaces.add(new FavoritePlaceORM());


                return favoritesPlaces;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item_favorite_place, parent, false);
                } // TODO create list item layout
                TextView name = (TextView) convertView.findViewById(R.id.name);
                name.setText("TEST");

                View addressSummary = convertView.findViewById(R.id.addressSummary);
                View editLayout = convertView.findViewById(R.id.editLayout);
                View removeLayout = convertView.findViewById(R.id.removeLayout);

                View detectLayout = convertView.findViewById(R.id.detectLayout);
                View saveLayout = convertView.findViewById(R.id.saveLayout);
                View addressSummaryEdit = convertView.findViewById(R.id.addressEditSummary);

                final FavoritePlaceORM item = getItem(position);

                if (item == null) {
                    addressSummary.setVisibility(View.GONE);
                    editLayout.setVisibility(View.GONE);
                    removeLayout.setVisibility(View.GONE);

                    addressSummaryEdit.setVisibility(View.VISIBLE);
                    detectLayout.setVisibility(View.VISIBLE);
                    saveLayout.setVisibility(View.VISIBLE);
                } else {
                    addressSummary.setVisibility(View.VISIBLE);
                    editLayout.setVisibility(View.VISIBLE);
                    removeLayout.setVisibility(View.VISIBLE);

                    addressSummaryEdit.setVisibility(View.GONE);
                    detectLayout.setVisibility(View.GONE);
                    saveLayout.setVisibility(View.GONE);
                }
                return convertView;
            }
        };
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.add_favorite_address:
                getAdapter().add(null);
                break;
        }

    }

}
