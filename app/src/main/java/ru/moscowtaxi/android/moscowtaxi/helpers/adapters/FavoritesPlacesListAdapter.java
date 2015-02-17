package ru.moscowtaxi.android.moscowtaxi.helpers.adapters;

import android.content.Context;
import android.location.Location;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.moscowtaxi.android.moscowtaxi.R;
import ru.moscowtaxi.android.moscowtaxi.activity.MainActivity;
import ru.moscowtaxi.android.moscowtaxi.orm.FavoritePlaceORM;
import ru.moscowtaxi.android.moscowtaxi.preferences.PreferenceUtils;

/**
 * Created by alex-pers on 12/15/14.
 */
public class FavoritesPlacesListAdapter extends BaseAdapter {

    List<FavoritePlaceORM> items;
    LayoutInflater inflater;

    public FavoritesPlacesListAdapter(LayoutInflater inflater, List<FavoritePlaceORM> data) {
        this.inflater = inflater;
        items = data;
    }


    public List<FavoritePlaceORM> getItems() {
        return items;
    }

    public void setItems(List<FavoritePlaceORM> items) {
        this.items = items;
    }


    @Override
    public int getCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        if (items == null)
            return null;
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (items == null)
            return -1;
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater
                    .inflate(R.layout.list_item_favorite_place, null);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.linStateNormal = (LinearLayout) convertView.findViewById(R.id.lay_state_normal);
        holder.linStateEdited = (LinearLayout) convertView.findViewById(R.id.lay_state_edited);
        holder.txtName = (TextView) convertView.findViewById(R.id.name);
        holder.txtAdress = (TextView) convertView.findViewById(R.id.address);
        holder.edtName = (EditText) convertView.findViewById(R.id.nameEdit);
        holder.edtAdress = (EditText) convertView.findViewById(R.id.addressEdit);
        holder.butChange = (LinearLayout) convertView.findViewById(R.id.editLayout);
        holder.butDelete = (LinearLayout) convertView.findViewById(R.id.removeLayout);
        holder.butDetectAdress = (LinearLayout) convertView.findViewById(R.id.detectLayout);
        holder.butSave = (LinearLayout) convertView.findViewById(R.id.saveLayout);
        holder.txtAboveAdress = (TextView) convertView.findViewById(R.id.txt_above_edt_adress);
        holder.txtAboveName = (TextView) convertView.findViewById(R.id.txt_above_edt_name);

        holder.setData(items.get(i));

        return convertView;
    }

    public class Holder {
        LinearLayout linStateNormal;
        LinearLayout linStateEdited;

        TextView txtName;
        TextView txtAdress;

        EditText edtName;
        EditText edtAdress;

        TextView txtAboveName;
        TextView txtAboveAdress;


        LinearLayout butChange;
        LinearLayout butDelete;
        LinearLayout butDetectAdress;
        LinearLayout butSave;


        public void setData(final FavoritePlaceORM data) {

            if (data.is_edited_now) {
                linStateEdited.setVisibility(View.VISIBLE);
                linStateNormal.setVisibility(View.GONE);
            } else {
                linStateEdited.setVisibility(View.GONE);
                linStateNormal.setVisibility(View.VISIBLE);
            }


//            View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        InputMethodManager imm = (InputMethodManager) edtName.getContext().getSystemService(
//                                Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
//                    }
//                }
//            };
//            edtName.setOnFocusChangeListener(focusChangeListener);
//
//            edtAdress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        InputMethodManager imm = (InputMethodManager) edtName.getContext().getSystemService(
//                                Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(edtAdress.getWindowToken(), 0);
//                    }
//                }
//            });
            txtAboveAdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edtName.setVisibility(View.GONE);

                    txtAboveName.setVisibility(View.VISIBLE);
                    edtAdress.setVisibility(View.VISIBLE);
                    if ("".equals(edtName.getText().toString())) {
                        txtAboveName.setText("Имя");
                    } else {
                        txtAboveName.setText(edtName.getText());
                    }

                    txtAboveAdress.setVisibility(View.GONE);

                    edtAdress.requestFocus();
                }
            });

            txtAboveName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    edtAdress.setVisibility(View.GONE);
                    txtAboveAdress.setVisibility(View.VISIBLE);
                    edtName.setVisibility(View.VISIBLE);
                    if ("".equals(edtAdress.getText().toString())) {
                        txtAboveAdress.setText("Адресс");
                    } else {
                        txtAboveAdress.setText(edtAdress.getText());
                    }

                    txtAboveName.setVisibility(View.GONE);
                    edtName.requestFocus();
                }
            });

            txtName.setText(data.name);
            txtAdress.setText(data.address);
            butChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (FavoritePlaceORM item : items) {
                        item.is_edited_now = false;
                    }

                    data.is_edited_now = true;
                    FavoritesPlacesListAdapter.this.notifyDataSetChanged();
                }
            });

            butSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Editable text = edtName.getText();
                    Editable text1 = edtAdress.getText();
                    Context context = view.getContext();
                    if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text1)) {
                        Toast.makeText(context, context.getString(R.string.empty_edit_text_error), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    data.is_edited_now = false;
                    data.userName = PreferenceUtils.getCurrentUser(context);
                    data.name = text.toString();
                    data.address = text1.toString();

                    FavoritesPlacesListAdapter.this.notifyDataSetChanged();
                    FavoritePlaceORM.insertOrUpdateFavoritePlace(context, data);


                }
            });

            butDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    getItems().remove(data);
                    notifyDataSetChanged();
                    FavoritePlaceORM.deleteFavoritePlaceByID(context, data.getId());
                }
            });

            butDetectAdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) v.getContext();
                    Location lastLocation = activity.getLastLocation();
                    if (lastLocation != null) {
                        new MainActivity.GetAddressTask(activity, edtAdress).execute(lastLocation);
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.failed_determinate_location), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
