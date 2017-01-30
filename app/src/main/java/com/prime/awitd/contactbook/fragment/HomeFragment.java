package com.prime.awitd.contactbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prime.awitd.contactbook.R;
import com.prime.awitd.contactbook.activity.MainFrameActivity;
import com.prime.awitd.contactbook.database.DatabaseHelper;
import com.prime.awitd.contactbook.mflib.PrimeMMTextView;
import com.prime.awitd.contactbook.model.Member;
import com.prime.awitd.contactbook.model.PrimeRoundedImageView;
import com.prime.awitd.contactbook.model.RecyclerItemClickListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SantaClaus on 23/01/2017.
 */

public class HomeFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected DatabaseReference myRef;
    String MVname, MVTeam;
    public int homeNo, phoneNo;
    List<String> arrayList = new ArrayList<>();
    DatabaseHelper myDb;
    private FirebaseRecyclerAdapter<Member, HomeFragment.MyViewHolder> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.activity_main, container, false);

        ((MainFrameActivity) getActivity()).setActionBarTitle(R.string.main_frame_activity_title);

        recyclerView = (RecyclerView) mainView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myDb = new DatabaseHelper(getContext());
        checkNetwork();

        //DataBase raf
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.keepSynced(true);

        adapter = new FirebaseRecyclerAdapter<Member, HomeFragment.MyViewHolder>(
                Member.class, R.layout.list_item, HomeFragment.MyViewHolder.class, myRef
        ) {
            @Override
            protected void populateViewHolder(final HomeFragment.MyViewHolder viewHolder, final Member model, final int position) {

                viewHolder.textViewName.setText(model.getName());
                viewHolder.textViewID.setText(model.getId());
                viewHolder.textViewTeam.setText(model.getTeam());

                viewHolder.textViewTeam.setTextColor(getResources().getColor(R.color.colorPrimary));

                viewHolder.textViewPhone.setText(String.valueOf(model.getPhone()));
                viewHolder.textViewHome.setText(String.valueOf(model.getHome()));
//
//                PicassoCache.getPicassoInstance(MainActivity.this).load(model.getImgUrl()), MemoryPolicy.NO_STORE)
//                        .into(viewHolder.imageView);

                boolean isInserted = myDb.insertData(model.getId(), model.getName(), model.getTeam(), String.valueOf(model.getPhone()), String.valueOf(model.getHome()));
//                if (isInserted == true){
//                    Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(MainActivity.this, "Data is not inserted", Toast.LENGTH_SHORT).show();
//                }

                boolean isUpdate = myDb.updateData(model.getId(), model.getName(), model.getTeam(), String.valueOf(model.getPhone()), String.valueOf(model.getHome()));
//                if (isUpdate == true) {
//                    Toast.makeText(MainActivity.this, "Data Update", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    Toast.makeText(MainActivity.this, "Data is not Updated", Toast.LENGTH_LONG).show();
//                }

                //ViewAll();
                //getNameFromDB();

                Picasso.with(getContext()).load(model.getImgUrl()).networkPolicy(NetworkPolicy.OFFLINE)

                        .into(viewHolder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                // success ..
                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Log.v("Picasso", getString(R.string.cannot_fetch_in_first_time_exception));
                                Picasso.with(getContext()).load(model.getImgUrl()).networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).error(R.mipmap.awitd_water_logo)
                                        .into(viewHolder.imageView, new Callback() {

                                            @Override
                                            public void onSuccess() {
                                                Log.v("Picasso", "fetch image success in try again.");
                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso", "Could not fetch image again...");
                                            }

                                        });
                            }
                        });
                arrayList.add(model.getName().toString());
            }
        };
        recyclerView.setAdapter(adapter);
        System.out.println("List" + arrayList);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

                        HomeFragment.MyViewHolder mv = new MyViewHolder(view);
                        MVname = mv.textViewName.getText().toString();
                        MVTeam = mv.textViewTeam.getText().toString();
                        homeNo = Integer.parseInt(mv.textViewHome.getText().toString());
                        phoneNo = Integer.parseInt(mv.textViewPhone.getText().toString());

                        final AppCompatDialog appCompatDialog = new AppCompatDialog(getContext(), R.style.Theme_CustomDialog);
                        appCompatDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        appCompatDialog.setContentView(R.layout.profile_screen);

                        ImageView headerImgView = (ImageView) appCompatDialog.findViewById(R.id.header_imageview);
                        TextView tvMobileNo = (TextView) appCompatDialog.findViewById(R.id.dialog_mobile_textView);
                        TextView tvHomeNo = (TextView) appCompatDialog.findViewById(R.id.dialog_home_textView);
                        TextView tvName = (TextView) appCompatDialog.findViewById(R.id.dialog_name_textView);
                        TextView tvPosition = (TextView) appCompatDialog.findViewById(R.id.dialog_team_textView);

                        ImageView btn_mobile_phone = (ImageView) appCompatDialog.findViewById(R.id.button_phone);
                        ImageView btn_home_phone = (ImageView) appCompatDialog.findViewById(R.id.button_home_phone);

//                        Configuration config = getResources().getConfiguration();
//                        if (MVTeam.toString().equalsIgnoreCase("Android")||MVTeam.toString().equalsIgnoreCase("Android Team")||MVTeam.toString().equalsIgnoreCase("Android team")){
//
//                            if(config.smallestScreenWidthDp >= 600){
//                                // tablet size
//                                headerImgView.setBackgroundResource(R.drawable.android_team_cover_photo);
//                            }else {
//                                headerImgView.setBackgroundResource((R.drawable.awitd_android_team_cover_photo_small));
//                            }

                        tvName.setText(MVname);
                        tvPosition.setText(MVTeam);
                        tvMobileNo.setText(phoneNo + "");
                        tvHomeNo.setText(homeNo + "");
                        com.prime.awitd.contactbook.model.PrimeRoundedImageView img_profile_picture = (PrimeRoundedImageView) appCompatDialog.findViewById(R.id.profile_picture);
                        try {
                            img_profile_picture.setImageDrawable(mv.imageView.getDrawable());
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        btn_home_phone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                funPhoneCalling(homeNo);
                            }
                        });

                        btn_mobile_phone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                funPhoneCalling(phoneNo);
                            }
                        });
                        appCompatDialog.setCanceledOnTouchOutside(true);
                        appCompatDialog.show();
                    }
                })
        );


        return mainView;
    }

    private void checkNetwork() {

        ConnectivityManager connectivitymanager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = connectivitymanager.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    Toast.makeText(getContext(), R.string.connected_wifi, Toast.LENGTH_SHORT).show();

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    Toast.makeText(getContext(), R.string.connected_mobile_data, Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(getContext(), R.string.check_your_connection, Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void funPhoneCalling(Integer number) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string.action_call_prefit_text) + number));
        startActivity(callIntent);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTeam, textViewPhone, textViewHome, textViewID;
        PrimeMMTextView textViewName;
        com.prime.awitd.contactbook.model.PrimeRoundedImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            textViewName = (PrimeMMTextView) itemView.findViewById(R.id.textViewName);
            textViewTeam = (TextView) itemView.findViewById(R.id.textViewTeam);
            textViewPhone = (TextView) itemView.findViewById(R.id.textViewPhone);
            textViewHome = (TextView) itemView.findViewById(R.id.textViewHome);
            textViewID = (TextView) itemView.findViewById(R.id.textViewID);
            imageView = (PrimeRoundedImageView) itemView.findViewById(R.id.imgView_user_photo);

        }
    }


}
