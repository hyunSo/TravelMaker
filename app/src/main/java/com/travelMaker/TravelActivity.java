package com.travelMaker;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by yoon on 2016. 5. 29..
 */
public class TravelActivity extends Activity {
    private BackPressCloseSystem backPressCloseSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backPressCloseSystem = new BackPressCloseSystem(this);
        // 뒤로 가기 버튼 이벤트
    }

    @Override
    public void onBackPressed() {
        backPressCloseSystem.onBackPressed();
    }
}
