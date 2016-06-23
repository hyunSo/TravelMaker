package com.travelMaker;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by yoon on 2016. 6. 23..
 */
public class MainActivity extends TravelActivity {
    Button album_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        album_btn = (Button)findViewById(R.id.goto_album_list);

        album_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent goto_album = new Intent(MainActivity.this,
                        AlbumList.class);
                goto_album.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goto_album);
                finish();
            }
        });

    }

}
