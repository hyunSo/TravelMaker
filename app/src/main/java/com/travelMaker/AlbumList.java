package com.travelMaker;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumList extends TravelActivity {
    Button add_btn;
    ListView Album_listview;
    ArrayList<Album> album_data = new ArrayList<Album>();
    Album_Adapter cAdapter;
    AlbumDatabaseHandler db;
    String Toast_msg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);
        try {
            Album_listview = (ListView) findViewById(R.id.album_list);
            Album_listview.setItemsCanFocus(false);
            add_btn = (Button) findViewById(R.id.album_add_btn);

            Set_Referash_Data();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("some error", "" + e);
        }
        add_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent add_user = new Intent(AlbumList.this,
                        AddUpdateAlbum.class);
                add_user.putExtra("called", "add");
                add_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(add_user);
                finish();
            }
        });

    }

//    public void Call_My_Blog(View v) {
//        Intent intent = new Intent(AlbumList.this, My_Blog.class);
//        startActivity(intent);
//
//    }

    public void Set_Referash_Data() {
        album_data.clear();
        db = new AlbumDatabaseHandler(this);
        ArrayList<Album> album_array_from_db = db.Get_Albums();

        for (int i = 0; i < album_array_from_db.size(); i++) {

            int tidno = album_array_from_db.get(i).getID();
            String name = album_array_from_db.get(i).getName();
            String mobile = album_array_from_db.get(i).getPath();
            String plane = album_array_from_db.get(i).getPlane();
            String maxWeight = album_array_from_db.get(i).get_maxWeight();
            String currWeight = album_array_from_db.get(i).get_currWeight();
            Album cnt = new Album();
            cnt.setID(tidno);
            cnt.setName(name);
            cnt.setPlane(plane);
            cnt.setPath(mobile);
            cnt.set_maxWeight(maxWeight);
            cnt.set_currWeight(currWeight);

            album_data.add(cnt);
        }
        db.close();
        cAdapter = new Album_Adapter(AlbumList.this, R.layout.album_listview_row,
                album_data);
        Album_listview.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();
    }

    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Set_Referash_Data();

    }

    public class Album_Adapter extends ArrayAdapter<Album> {
        Activity activity;
        int layoutResourceId;
        Album album;
        ArrayList<Album> data = new ArrayList<Album>();

        public Album_Adapter(Activity act, int layoutResourceId,
                               ArrayList<Album> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new UserHolder();
                holder.name = (TextView) row.findViewById(R.id.album_name_txt);
                holder.plane = (TextView) row.findViewById(R.id.album_plane_txt);
                holder.path = (TextView) row.findViewById(R.id.album_path_txt);
                holder.weight = (TextView) row.findViewById(R.id.current_weight_txt);
                holder.show = (Button) row.findViewById(R.id.album_btn_show);
                holder.edit = (Button) row.findViewById(R.id.album_btn_update);
                holder.delete = (Button) row.findViewById(R.id.album_btn_delete);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            album = data.get(position);
            holder.show.setTag(album.getID());
            holder.edit.setTag(album.getID());
            holder.delete.setTag(album.getID());
            holder.name.setText(album.getName());
            holder.plane.setText(album.getPlane());
            holder.path.setText(album.getPath());
            holder.weight.setText(album.get_currWeight() + "kg (maximum " + album.get_maxWeight() + "kg)");

            holder.show.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Show Button Clicked", "**********");

                    Intent show_album = new Intent(activity,
                            ProductList.class);
                    String album_id = v.getTag().toString();
                    String album_name = db.Get_Album(Integer.parseInt(album_id)).getName();

                    show_album.putExtra("ALBUM_ID", album_id);
                    show_album.putExtra("ALBUM_NAME", album_name);

                    activity.startActivity(show_album);
                }
            });

            holder.edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Edit Button Clicked", "**********");

                    Intent update_user = new Intent(activity,
                            AddUpdateAlbum.class);
                    update_user.putExtra("called", "update");
                    update_user.putExtra("ALBUM_ID", v.getTag().toString());
                    activity.startActivity(update_user);

                }
            });
            holder.delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub

                    // show a message while loader is loading

                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete ");
                    final int user_id = Integer.parseInt(v.getTag().toString());
                    adb.setNegativeButton("Cancel", null);
                    adb.setPositiveButton("Ok",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // MyDataObject.remove(positionToRemove);
                                    AlbumDatabaseHandler dBHandler = new AlbumDatabaseHandler(
                                            activity.getApplicationContext());
                                    dBHandler.Delete_Album(user_id);
                                    AlbumList.this.onResume();

                                }
                            });
                    adb.show();
                }

            });
            return row;

        }

        class UserHolder {
            TextView name;
            TextView plane;
            TextView path;
            TextView weight;
            Button show;
            Button edit;
            Button delete;
        }

    }

}
