package com.prime.awitd.contactbook.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prime.awitd.contactbook.R;
import com.prime.awitd.contactbook.activity.MainFrameActivity;

/**
 * Created by SantaClaus on 23/01/2017.
 */

public class AboutFragment extends Fragment {

    TextView tvUrl,tvDevTwitter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View aboutView = inflater.inflate(R.layout.activity_about,container,false);

        ((MainFrameActivity)getActivity()).setActionBarTitle(R.string.menu_about);

        tvUrl = (TextView)aboutView.findViewById(R.id.tv_url);
        tvDevTwitter = (TextView)aboutView.findViewById(R.id.dev_twitter);

        tvUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.awitd.com/"));
                String title = (String) getResources().getText(R.string.chooser_title);
                Intent chooser = Intent.createChooser(intent, title);
                startActivity(chooser);
            }
        });

        tvDevTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                try {
                    // get the Twitter app if possible
                    getContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=USERID"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/yepyaesonetun"));
                }
                startActivity(intent);
            }
        });
        return aboutView;


    }
}
