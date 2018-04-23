package com.combii.instagram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class ImagesActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    String activeUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);



        Intent intent = getIntent();
        activeUsername = intent.getStringExtra("activeUsername");

        setTitle(activeUsername + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<>("Image");

        query.whereEqualTo("username", activeUsername);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){
                    if(objects.size() > 0){

                        for(ParseObject object : objects){

                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if(e == null && data != null){

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

                                        ImageView imageView = new ImageView(getApplicationContext());

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        ));

                                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.instragram));


                                         imageView.setImageBitmap(bitmap);


                                        linearLayout.addView(imageView);

                                    }

                                }
                            });

                        }

                    }
                }
            }
        });
    }




}
