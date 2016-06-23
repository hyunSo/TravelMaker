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

public class AddUpdateAlbum extends TravelActivity{
    EditText add_name;//, add_plane;
    Spinner add_flight;
    Button add_save_btn, add_view_all, update_btn, update_view_all;
    LinearLayout add_view, update_view;
    String valid_plane = null, valid_name = null,
            Toast_msg = null, valid_user_id = "", currentWeight = "";//, valid_flight = null;
    String valid_weight = "";
    int ALBUM_ID;
    int flight_id = -1;
    int lv_start = -1;
    ArrayList<plane> plane_data1;
    plane_Adapter cAdapter;
    AlbumDatabaseHandler dbHandler = new AlbumDatabaseHandler(this);

    VerticalSeekBar_Reverse verticalSeekBar_Reverse=null;
    TextView vs_reverseProgress=null;

    ListView planeListView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_album);

        final int max = 40;
        final int min = 10;

        planeListView1 = (ListView) findViewById(R.id.lv1);

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
        add_flight.setOnItemSelectedListener(new MyOnItemSelectedListener());
        createSpinnerDropDown();



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
    private void createSpinnerDropDown(){
        Spinner spinner = (Spinner) findViewById(R.id.album_add_subflight);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, constants.subdaehan);
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter2);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

    }

    private void sub_flight(int position, long id) {
        plane_data1 = new ArrayList<plane>();
        //plane_data2 = new ArrayList<plane>();
        //plane_data3 = new ArrayList<plane>();

        switch (flight_id) {
            case 1://아시아나
            case 2://대한항공
                if(position>0) {
                    lv_start =  constants.SUBDAEHAN_STPOINT[position - 1];
                    for (int i = constants.SUBDAEHAN_STPOINT[position - 1]; i < constants.SUBDAEHAN_STPOINT[position]; i++) {
                        plane p = new plane();
                        p._details = constants.SUBDAEHAN[i];
                        plane_data1.add(p);
                    }
                }

        }
        cAdapter = new plane_Adapter(this, R.layout.plane_listview_row, plane_data1);
        planeListView1.setAdapter(cAdapter);
        planeListView1.setOnItemClickListener( new ListViewItemClickListener());
        cAdapter.notifyDataSetChanged();


    }
    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {

            valid_weight = Integer.toString(constants.SUBDAEHAN_W[lv_start+position]);

            for(int i=0;i<parent.getCount();i++)
                if(i!=position) parent.getChildAt(i).setBackgroundColor(Color.WHITE);
            view.setBackgroundColor(Color.GRAY);



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
    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            String selectedItem = parent.getItemAtPosition(pos).toString();

            //check which spinner triggered the listener
            switch (parent.getId()) {
                //country spinner
                case R.id.album_add_flight:
                    flight_id = pos;
                    //make sure the country was already selected during the onCreate
               /*     if(selectedCountry != null){
                        Toast.makeText(parent.getContext(), "Country you selected is " + selectedItem,
                                Toast.LENGTH_LONG).show();
                    }
                    selectedCountry = selectedItem;*/
                    break;
                //animal spinner
                case R.id.album_add_subflight:
                    sub_flight(pos, id);
                    //make sure the animal was already selected during the onCreate
               /*     if(selectedAnimal != null){
                        Toast.makeText(parent.getContext(), "Animal selected is " + selectedItem,
                                Toast.LENGTH_LONG).show();
                    }
                    selectedAnimal = selectedItem;*/

                    break;
            }


        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing.
        }
    }
}
