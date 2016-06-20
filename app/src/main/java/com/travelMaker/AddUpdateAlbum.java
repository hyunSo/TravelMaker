package com.travelMaker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class AddUpdateAlbum extends TravelActivity implements AdapterView.OnItemSelectedListener{
    EditText add_name;//, add_plane;
    Spinner add_flight;
    Button add_save_btn, add_view_all, update_btn, update_view_all;
    LinearLayout add_view, update_view;
    String valid_plane = null, valid_name = null,
            Toast_msg = null, valid_user_id = "";//, valid_flight = null;
    int ALBUM_ID;
    int flight_id = -1;
    AlbumDatabaseHandler dbHandler = new AlbumDatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_album);

        // set screen
        Set_Add_Update_Screen();

        // set visibility of view as per calling activity
        String called_from = getIntent().getStringExtra("called");

        if (called_from.equalsIgnoreCase("add")) {
            add_view.setVisibility(View.VISIBLE);
            update_view.setVisibility(View.GONE);
        } else {

            update_view.setVisibility(View.VISIBLE);
            add_view.setVisibility(View.GONE);
            ALBUM_ID = Integer.parseInt(getIntent().getStringExtra("ALBUM_ID"));

            Album c = dbHandler.Get_Album(ALBUM_ID);

            add_name.setText(c.getName());
//            add_plane.setText(c.getPlane());
            // dbHandler.close();
        }

        add_flight = (Spinner) findViewById(R.id.album_add_flight);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.airlines, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        add_flight.setAdapter(adapter);
        add_flight.setOnItemSelectedListener(this);

/*      add_plane.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                Is_Valid_Email(add_plane);
            }
        });
*/
        add_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Is_Valid_Album_Name(add_name);
            }
        });

        add_save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // check the value state is null or not
                if (valid_name != null && flight_id >= 0
                        && valid_plane != null && valid_name.length() != 0
                        && valid_plane.length() != 0) {

                    dbHandler.Add_Album(new Album(valid_name,
                            "/TravelMaker/"+valid_name, valid_plane));
                    Toast_msg = "Data inserted successfully";
                    Show_Toast(Toast_msg);
                    Reset_Text();

                    Intent view_user = new Intent(AddUpdateAlbum.this,
                            AlbumList.class);
                    view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(view_user);
                    finish();
                }

            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                valid_name = add_name.getText().toString();
//                valid_plane = add_plane.getText().toString();

                // check the value state is null or not
                if (valid_name != null && flight_id >= 0
                        && valid_plane != null && valid_name.length() != 0
                        && valid_plane.length() != 0) {

                    dbHandler.Update_Album(new Album(ALBUM_ID, valid_name,
                            "/TravelMaker/"+valid_name, valid_plane));
                    dbHandler.close();
                    Toast_msg = "Data Updated successfully";
                    Show_Toast(Toast_msg);
                    Reset_Text();

                    Intent view_user = new Intent(AddUpdateAlbum.this,
                            AlbumList.class);
                    view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(view_user);
                    finish();
                } else {
                    Toast_msg = "Sorry Some Fields are missing.\nPlease Fill up all.";
                    Show_Toast(Toast_msg);
                }

            }
        });
        update_view_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent view_user = new Intent(AddUpdateAlbum.this,
                        AlbumList.class);
                view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(view_user);
                finish();
            }
        });

        add_view_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent view_user = new Intent(AddUpdateAlbum.this,
                        AlbumList.class);
                view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(view_user);
                finish();
            }
        });

    }

    public  void  onItemSelected(
            AdapterView<?> parent, View view, int  position, long  id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        //
        flight_id = position;
        switch (position) {
            case 0:
                valid_plane = null;
                break;
            case 1:
                valid_plane = new String("아시아나항공");
                Toast.makeText(parent.getContext(), "아시아나항공", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                valid_plane = new String("대한항공");
                Toast.makeText(parent.getContext(), "대한항공", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                valid_plane = new String("제주항공");
                Toast.makeText(parent.getContext(), "제주항공", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                valid_plane = new String("진에어");
                Toast.makeText(parent.getContext(), "진에어", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                valid_plane = new String("이스타항공");
                Toast.makeText(parent.getContext(), "이스타항공", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                valid_plane = new String("티웨이");
                Toast.makeText(parent.getContext(), "티웨이", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                valid_plane = new String("에어부산");
                Toast.makeText(parent.getContext(), "에어부산", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                valid_plane = new String("에어아시아");
                Toast.makeText(parent.getContext(), "에어아시아", Toast.LENGTH_SHORT).show();
                break;
            case 9:
                valid_plane = new String("델타항공");
                Toast.makeText(parent.getContext(), "델타항공", Toast.LENGTH_SHORT).show();
                break;
            case 10:
                valid_plane = new String("피치항공");
                Toast.makeText(parent.getContext(), "피치항공", Toast.LENGTH_SHORT).show();
                break;
            default:
                valid_plane = null;
                flight_id = -1;
                Toast.makeText(parent.getContext(), "Something is wrong", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public  void  onNothingSelected(AdapterView<?> parent) {
    }

    public void Set_Add_Update_Screen() {

        add_name = (EditText) findViewById(R.id.album_add_name);
//        add_plane = (EditText) findViewById(R.id.album_add_plane);

        add_save_btn = (Button) findViewById(R.id.album_add_save_btn);
        update_btn = (Button) findViewById(R.id.album_update_btn);

        add_view_all = (Button) findViewById(R.id.album_add_view_all);
        update_view_all = (Button) findViewById(R.id.album_update_view_all);

        add_view = (LinearLayout) findViewById(R.id.album_add_view);
        update_view = (LinearLayout) findViewById(R.id.album_update_view);

        add_view.setVisibility(View.GONE);
        update_view.setVisibility(View.GONE);

    }

    public void Is_Valid_Email(EditText edt) {
//            valid_plane = edt.getText().toString();
    }

    public void Is_Valid_Album_Name(EditText edt) throws NumberFormatException {
        if (edt.getText().toString().length() < 3 || edt.getText().toString().length() > 15) {
            edt.setError("3-15자로 입력하세요.");
            valid_name = null;
        } else {
            valid_name = edt.getText().toString();
        }

    }

    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void Reset_Text() {
        add_name.getText().clear();
 //       add_plane.getText().clear();
    }

}
