package com.travelMaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProductList extends TravelActivity {
    static final int MAXPRODUCTS = 100;

    ArrayList<Product> product_array_from_db;//유진

    Button add_btn;
    Button goto_album_list;
    Button ex_btn;

    TextView album_name_txt;
    TextView album_weight_txt;
    ListView Product_listview;
    ArrayList<Product> product_data = new ArrayList<Product>();
    Product_Adapter cAdapter;
    ProductDatabaseHandler db;
    AlbumDatabaseHandler AlbumdbHandler = new AlbumDatabaseHandler(this);
    String Toast_msg;

    static int ALBUM_ID;
    static String album_name;
    static Album c;

    ProductDatabaseHandler dbHandler = new ProductDatabaseHandler(this);

    //  int highlight_top = 0;
    ArrayList<Product> productarr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        try {
            productarr = new ArrayList<Product>();

            Product_listview = (ListView) findViewById(R.id.product_list);
            Product_listview.setItemsCanFocus(false);
            add_btn = (Button) findViewById(R.id.product_add_btn);
            goto_album_list = (Button) findViewById(R.id.product_goto_album_list);
            album_name_txt = (TextView) findViewById(R.id.product_album_name);
            album_weight_txt = (TextView) findViewById(R.id.textWeight);
            ex_btn = (Button) findViewById(R.id.product_ex_btn);

            ALBUM_ID = DataCenter.getAlbumId();
            album_name = DataCenter.getAlbumName();
            c = AlbumdbHandler.Get_Album(ALBUM_ID);

            Log.e("ALBUM ID", Integer.toString(ALBUM_ID));

            Set_Refresh_Data();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("some error", "" + e);
        }
        add_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent add_user = new Intent(ProductList.this,
                        ArduinoControllerActivity.class);
                add_user.putExtra("called", "add");

                add_user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(add_user);
                finish();
            }
        });

        ex_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // arraylist<producst> = knapsack();
                int max_weight = (int)Double.parseDouble(c.get_maxWeight());
                if(product_array_from_db.size()>0) {
                    KnapSack kn = new KnapSack(product_array_from_db, max_weight);
                    int[] ex_productIdx = kn.getExProductListIdx();
                    product_highlight(ex_productIdx);
                }

            }
        });

        goto_album_list.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent goto_album_list = new Intent(ProductList.this,
                        AlbumList.class);
                goto_album_list.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goto_album_list);
                finish();
            }
        });

    }

//    public void Call_My_Blog(View v) {
//        Intent intent = new Intent(ProductList.this, My_Blog.class);
//        startActivity(intent);
//
//    }

    private void product_highlight(int[] ex_productIdx) {

        for(int i = 0;i<ex_productIdx.length;i++) {
            Product tmp = product_data.get(ex_productIdx[i]);
            productarr.add(tmp);
        }
        for(int i = 0;i<ex_productIdx.length;i++) {
            product_data.remove(ex_productIdx[i]);
        }
        for(int i = 0;i<ex_productIdx.length;i++) {
            product_data.add(0,productarr.get(i));
        }

        cAdapter = new Product_Adapter(ProductList.this, R.layout.product_listview_row,
                product_data);
        Product_listview.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();

        // update album name


    }

    public void Set_Refresh_Data() {
        product_data.clear();
        db = new ProductDatabaseHandler(this);
        product_array_from_db = db.Get_Products_by_Album(ALBUM_ID);
        Log.e("REFRESH DATA", Integer.toString(product_array_from_db.size()));

        for (int i = 0; i < product_array_from_db.size(); i++) {

            int tidno = product_array_from_db.get(i).getID();
            int album_id = product_array_from_db.get(i).getAlbumID();
            String name = product_array_from_db.get(i).getName();
            String path = product_array_from_db.get(i).getPath();
            String weight = product_array_from_db.get(i).getWeight();
            Product cnt = new Product();
            cnt.setID(tidno);
            cnt.setAlbumID(album_id);
            cnt.setName(name);
            cnt.setPath(path);
            cnt.setWeight(weight);

            product_data.add(cnt);
        }
        db.close();
        cAdapter = new Product_Adapter(ProductList.this, R.layout.product_listview_row,
                product_data);
        Product_listview.setAdapter(cAdapter);
        cAdapter.notifyDataSetChanged();

        // update album name
        album_name_txt.setText(album_name);
        album_weight_txt.setText(c.get_currWeight() + "kg (maximum " + c.get_maxWeight() + "kg)");
    }

    public void Show_Toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        ALBUM_ID = Integer.parseInt(getIntent().getStringExtra("ALBUM_ID"));

        Log.e("ALBUM ID in Resume", Integer.toString(ALBUM_ID));

        Set_Refresh_Data();

    }

    public class Product_Adapter extends ArrayAdapter<Product> {
        Activity activity;
        int layoutResourceId;
        Product product;
        ArrayList<Product> data = new ArrayList<Product>();

        public Product_Adapter(Activity act, int layoutResourceId,
                               ArrayList<Product> data) {
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
                holder.name = (TextView) row.findViewById(R.id.product_name_txt);
                holder.image = (ImageView)row.findViewById(R.id.product_list_image);
                holder.weight = (TextView) row.findViewById(R.id.product_weight_txt);
                holder.path = (TextView) row.findViewById(R.id.product_path_txt);
                holder.show = (Button) row.findViewById(R.id.product_btn_show);
                holder.edit = (Button) row.findViewById(R.id.product_btn_update);
                holder.delete = (Button) row.findViewById(R.id.product_btn_delete);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }
            product = data.get(position);
            holder.show.setTag(product.getID());
            holder.edit.setTag(product.getID());
            holder.delete.setTag(product.getID());
            holder.name.setText(product.getName());
            holder.weight.setText(product.getWeight());
            holder.path.setText(product.getPath());
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inSampleSize = 2;

            Bitmap bm = BitmapFactory.decodeFile(product.getPath(), bfo);
            Bitmap resized;
            if(bm!=null)
            {  }
            else
            {

                bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            }
            resized = Bitmap.createScaledBitmap(bm, 320, 372, true);
            holder.image.setImageBitmap(resized);

            holder.show.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Show Button Clicked", "**********");

                    Intent update_user = new Intent(activity,
                            ProductShow.class);
                    DataCenter.setProductId(Integer.parseInt(v.getTag().toString()));

                    activity.startActivity(update_user);

                }
            });

            holder.edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Edit Button Clicked", "**********");

                    Intent update_user = new Intent(activity,
                            AddUpdateProduct.class);
                    update_user.putExtra("called", "update");
                    DataCenter.setProductId(Integer.parseInt(v.getTag().toString()));

                    activity.startActivity(update_user);

                }
            });
            holder.delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub

                    // show a message while loader is loading

                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                    adb.setTitle("제거");
                    adb.setMessage("물품을 리스트에서 제거하시겠습니까?");
                    final int user_id = Integer.parseInt(v.getTag().toString());
                    adb.setNegativeButton("취소", null);
                    adb.setPositiveButton("네",
                            new AlertDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // MyDataObject.remove(positionToRemove);

                                    ProductDatabaseHandler dBHandler = new ProductDatabaseHandler(
                                            activity.getApplicationContext());
                                    Product tmp = dBHandler.Get_Product(user_id);
                                    AlbumdbHandler.Update_Album_Weight(c, "-" + tmp.getWeight());
                                    AlbumdbHandler.close();

                                    Log.e("DELETE", "productArr size: " + Integer.toString(productarr.size()));
                                    Log.e("DELETE", "productData index: " + Integer.toString(findIndex(product_data, user_id)));

                                    if( findIndex(product_data, user_id) < productarr.size())
                                    {
                                        productarr.remove(findIndex(productarr, user_id));
                                    }
                                    dBHandler.Delete_Product(user_id);
                                    product_data.remove(findIndex(product_data, user_id));
                                    product_array_from_db.remove(findIndex(product_array_from_db, user_id));
                                    //ProductList.this.onResume();
                                    cAdapter = new Product_Adapter(ProductList.this, R.layout.product_listview_row,
                                            product_data);
                                    Product_listview.setAdapter(cAdapter);
                                    cAdapter.notifyDataSetChanged();

                                    // update album name
                                    album_name_txt.setText(album_name);
                                    album_weight_txt.setText(c.get_currWeight() + "kg (maximum " + c.get_maxWeight() + "kg)");
                                }
                            });
                    adb.show();
                }

            });
            if (position < productarr.size()) {
                row.setBackgroundColor(Color.parseColor("#BCF7F0"));
            } else {
                row.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            return row;

        }

        class UserHolder {
            ImageView image;
            TextView name;
            TextView weight;
            TextView path;
            Button show;
            Button edit;
            Button delete;
        }

        private int findIndex(ArrayList<Product> arr, int id) {
            int i;

            for(i = 0;i<arr.size();i++) {
                if( arr.get(i).getID() == id ) break;
            }
            return i;
        }

    }

}
