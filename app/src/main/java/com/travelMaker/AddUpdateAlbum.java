package com.travelMaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VerticalSeekBar_Reverse;
import android.widget.constants;

import java.util.ArrayList;

public class AddUpdateAlbum extends TravelActivity implements AdapterView.OnItemSelectedListener{
    EditText add_name;//, add_plane;
    Spinner add_flight;
    Button add_save_btn, add_view_all, update_btn, update_view_all;
    LinearLayout add_view, update_view;
    String valid_plane = null, valid_name = null,
            Toast_msg = null, valid_user_id = "", currentWeight = "";//, valid_flight = null;
    String valid_weight = "";
    int ALBUM_ID;
    int flight_id = -1;
    ArrayList<plane> plane_data1,plane_data2, plane_data3;
    plane_Adapter cAdapter;
    AlbumDatabaseHandler dbHandler = new AlbumDatabaseHandler(this);

    VerticalSeekBar_Reverse verticalSeekBar_Reverse=null;
    TextView vs_reverseProgress=null;

    ListView planeListView1;
    ListView planeListView2;
    ListView planeListView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_album);

        final int max = 40;
        final int min = 10;

        verticalSeekBar_Reverse=(VerticalSeekBar_Reverse)findViewById(R.id.seekbar_reverse);
        vs_reverseProgress=(TextView)findViewById(R.id.reverse_sb_progresstext);
        planeListView1 = (ListView) findViewById(R.id.lv1);
        planeListView2 = (ListView) findViewById(R.id.lv2);
        planeListView3 = (ListView) findViewById(R.id.lv3);
        add_flight = (Spinner) findViewById(R.id.album_add_flight);

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
            ALBUM_ID = DataCenter.getAlbumId();

            Album c = dbHandler.Get_Album(ALBUM_ID);

            add_name.setText(c.getName());
            currentWeight = c.get_currWeight();
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

        //ArrayAdapter<String> sAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, constants.PLANES);

        plane_data1 = new ArrayList<plane>();
        plane_data2 = new ArrayList<plane>();
        plane_data3 = new ArrayList<plane>();
        for(int i = 0;i< constants.PLANES.length;i++) {
            plane p = new plane();
            p._details = constants.PLANES[i];
            if(i<8) plane_data1.add(p);
            else if(i<14) plane_data2.add(p);
            else plane_data3.add(p);
        }
        cAdapter = new plane_Adapter(this, R.layout.plane_listview_row, plane_data1);
        planeListView1.setAdapter(cAdapter);
        cAdapter = new plane_Adapter(this, R.layout.plane_listview_row, plane_data2);
        planeListView2.setAdapter(cAdapter);
        cAdapter = new plane_Adapter(this, R.layout.plane_listview_row, plane_data3);
        planeListView3.setAdapter(cAdapter);

        cAdapter.notifyDataSetChanged();

        //   verticalSeekBar_Reverse.setMax( (max - min) / step );
        verticalSeekBar_Reverse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                double value = min + (progress*(max-min)/100);
                vs_reverseProgress.setText(value+" kg");
                valid_weight = Double.toString(value);
                int c1, c2, c3;
                if(value<16) {
                    c1 = Color.WHITE;
                    c2 = c3 = Color.GRAY;
                }
                else if(value<30)
                {
                    c2 = Color.WHITE;
                    c1 = c3 = Color.GRAY;
                }
                else
                {
                    c3 = Color.WHITE;
                    c1 = c2 = Color.GRAY;
                }
                planeListView1.setBackgroundColor(c1);
                planeListView2.setBackgroundColor(c2);
                planeListView3.setBackgroundColor(c3);

            }
        });


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
                        && valid_name.length() != 0
                        && valid_weight.length() != 0 ) {

                    //TODO change 32 into exact value
                    dbHandler.Add_Album(new Album(valid_name,
                            "/TravelMaker/"+valid_name, valid_weight));
                    //Toast_msg = "Data inserted successfully"+valid_weight;
                    //Show_Toast(Toast_msg);
                    //Reset_Text();

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
                        && valid_weight.length() != 0 && valid_name.length() != 0) {

                    Album c = dbHandler.Get_Album(ALBUM_ID);
                    currentWeight = c.get_currWeight();

                    //TODO change 32 into exact value
                    dbHandler.Update_Album(new Album(ALBUM_ID, valid_name,
                            "/TravelMaker/"+valid_name, valid_weight, currentWeight));
                    dbHandler.close();
                    //Toast_msg = "Data Updated successfully"+valid_weight;
                    //Show_Toast(Toast_msg);
                    //Reset_Text();

                    Intent view_user = new Intent(AddUpdateAlbum.this,
                            AlbumList.class);
                    view_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(view_user);
                    finish();
                } else {
                    //Toast_msg = "Sorry Some Fields are missing.\nPlease Fill up all.";
                    //Show_Toast(Toast_msg);
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
        /*switch (position) {
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
        }*/
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

    public class plane_Adapter extends ArrayAdapter<plane> {
        //  Activity activity;
        // int layoutResourceId;
        // plane plane;
        ArrayList<plane> data = new ArrayList<plane>();

        public plane_Adapter(Activity act, int layoutResourceId,
                             ArrayList<plane> data) {
            super(act, layoutResourceId, data);
            //   this.layoutResourceId = layoutResourceId;
            //  this.activity = act;
            this.data = data;
            // notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            // UserHolder holder = null;

            if (row == null) {
                //  LayoutInflater inflater = LayoutInflater.from(activity);
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = vi.inflate(R.layout.plane_listview_row, null);
                // row = inflater.inflate(layoutResourceId, parent, false);
                // holder = new UserHolder();
                //holder.planedetails = (TextView) row.findViewById(R.id.planeDetail_tv);
                //row.setTag(holder);
            } else {
                //holder = (UserHolder) row.getTag();
            }
            plane p = data.get(position);
            if(p!=null)
            {
                TextView tt = (TextView) row.findViewById(R.id.planeDetail_tv);
                if(tt != null)
                {
                    tt.setText(p.getDet());
                }
            }
            //    plane = data.get(position);
            //   holder.planedetails.setTag(plane.getDet());
           /* holder.show.setTag(product.getID());
            holder.edit.setTag(product.getID());
            holder.delete.setTag(product.getID());
            holder.name.setText(product.getName());
            holder.weight.setText(product.getWeight());
            holder.path.setText(product.getPath());*/


            return row;

        }
    }
}
