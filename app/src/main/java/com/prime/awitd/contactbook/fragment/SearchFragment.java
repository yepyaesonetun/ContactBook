package com.prime.awitd.contactbook.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.prime.awitd.contactbook.R;
import com.prime.awitd.contactbook.activity.MainFrameActivity;
import com.prime.awitd.contactbook.database.DatabaseHelper;

import java.lang.reflect.Field;

/**
 * Created by SantaClaus on 23/01/2017.
 */

public class SearchFragment extends Fragment {

    public static String[] vName;
    static int doing;
    static String id = "";
    static String colTeam = "";
    static String colMobile = "";
    static String colHome = "";
    android.widget.SearchView search;

    ArrayAdapter<String> dataAdapter = null;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View searchView = inflater.inflate(R.layout.search_name_list_activity, container, false);
        final ListView listView = (ListView) searchView.findViewById(R.id.list_view);

        ((MainFrameActivity) getActivity()).setActionBarTitle(R.string.menu_search);

        final DatabaseHelper dbHandler = new DatabaseHelper(getContext());

        Cursor cursor = dbHandler.getAllData();
        Log.i("Length...", cursor.getCount() + "");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int i = 0;
            vName = new String[cursor.getCount()];
            // fetch all data one by one
            do {
                vName[i] = cursor.getString(cursor.getColumnIndex("NAME"));
                i++;
            } while (cursor.moveToNext());
            Log.i("length", vName[0] + "");
        }

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, vName);

        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String itemValue = (String) listView.getItemAtPosition(position);
                itemValue.trim();
                //dbHandler.itemvalue =itemValue;

                // Toast.makeText(SearchActivity.this, "Position "+position+" ListItem "+itemValue, Toast.LENGTH_SHORT).show();

                //getMemberDetail(itemValue);

                DatabaseHelper dbHandaler = new DatabaseHelper(getContext());
                Cursor cursor = dbHandaler.getDetail(itemValue);
                Log.i("name", itemValue);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    doing = 1;

                    id = cursor.getString(cursor.getColumnIndex("ID"));
                    colTeam = cursor.getString(cursor.getColumnIndex("TEAM"));
                    colMobile = cursor.getString(cursor.getColumnIndex("MOBILE"));
                    colHome = cursor.getString(cursor.getColumnIndex("HOME"));

                    // Toast.makeText(SearchActivity.this, "Detail "+id+"\n"+colTeam+"\n"+colMobile+"\n"+colHome, Toast.LENGTH_SHORT).show();

                    final AppCompatDialog appCompatDialog = new AppCompatDialog(getContext(), android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
                    appCompatDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    appCompatDialog.setContentView(R.layout.dialog_profile);

                    final TextView tvMobileNo = (TextView) appCompatDialog.findViewById(R.id.textViewPhone);
                    final TextView tvHomeNo = (TextView) appCompatDialog.findViewById(R.id.textViewHome);
                    TextView tvName = (TextView) appCompatDialog.findViewById(R.id.textViewName);
                    TextView tvID = (TextView) appCompatDialog.findViewById(R.id.textViewID);
                    TextView tvPosition = (TextView) appCompatDialog.findViewById(R.id.textViewTeam);
                    FloatingActionButton fabMobile = (FloatingActionButton) appCompatDialog.findViewById(R.id.fab_phone);
                    FloatingActionButton fabHome = (FloatingActionButton) appCompatDialog.findViewById(R.id.fab_home);

                    tvName.setText(itemValue);
                    tvID.setText(id);
                    tvPosition.setText(colTeam);
                    tvMobileNo.setText(colMobile);
                    tvHomeNo.setText(colHome);

                    fabMobile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            funPhoneCalling((String) tvMobileNo.getText());
                        }
                    });
                    fabHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            funPhoneCalling((String) tvHomeNo.getText());
                        }
                    });

                    appCompatDialog.setCanceledOnTouchOutside(true);
                    appCompatDialog.show();
                } else
                    doing = 0;
            }
        });

        search = (android.widget.SearchView) searchView.findViewById(R.id.search);
        int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) search.findViewById(id);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        textView.setHintTextColor(getResources().getColor(R.color.colorAccent));
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }
        search.setQueryHint("Type Name");

        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

//                Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
//                        Toast.LENGTH_SHORT).show();
            }
        });
        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

//                Toast.makeText(getBaseContext(), query,
//                        Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                if (newText == "") {
                    String[] arr = new String[vName.length];
                    String[] name;
                    int j = 0;
                    for (int i = 0; i < vName.length; i++) {
                        if (vName[i].startsWith("ေ")) {
                            arr[j++] = vName[i];
                        }

                    }
                    name = new String[j];
                    for (int a = 0; a < j; a++) {
                        name[a] = arr[a];
                    }
                    dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, vName);
                    // final EditText det = (EditText) findViewById(R.id.editText);

                    listView.setAdapter(dataAdapter);
                } else {
                    SearchFragment.this.dataAdapter.getFilter().filter(newText.toString());
                }
                return false;
            }
        });

        return searchView;
    }

    private void funPhoneCalling(String number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.action_call_prefit_text) + number));
        startActivity(callIntent);
    }

}
