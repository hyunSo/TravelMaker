package com.travelMaker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddUpdateProduct extends TravelActivity {
    EditText add_name, add_weight;
    Button add_save_btn, add_view_all, update_btn, update_view_all;
    LinearLayout add_view, update_view;
    String valid_weight = null, valid_name = null,
            Toast_msg = null, valid_user_id = "";
    int album_id;
    String album_name;
    int PRODUCT_ID;
    ProductDatabaseHandler dbHandler = new ProductDatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_product);

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
            PRODUCT_ID = DataCenter.getProductId();

            Product c = dbHandler.Get_Product(PRODUCT_ID);

            add_name.setText(c.getName());
            add_weight.setText(c.getWeight());
            // dbHandler.close();
        }
        album_id = DataCenter.getAlbumId();
        album_name = DataCenter.getAlbumName();

        add_weight.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Is_Valid_Email(add_weight);
            }
        });

        add_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Is_Valid_Product_Name(add_name);
            }
        });

        add_save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // check the value state is null or not
                if (valid_name != null
                        && valid_weight != null && valid_name.length() != 0
                        && valid_weight.length() != 0) {

                    dbHandler.Add_Product(new Product(album_id, valid_name,
                            "/TravelMaker/"+album_name+"/"+valid_name, valid_weight, 0));
                    Toast_msg = "Data inserted successfully";
                    Show_Toast(Toast_msg);
                    Reset_Text();

                }

            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                valid_name = add_name.getText().toString();
                valid_weight = add_weight.getText().toString();
                // check the value state is null or not
                if (valid_name != null
                        && valid_weight != null && valid_name.length() != 0
                        && valid_weight.length() != 0) {

                    dbHandler.Update_Product(new Product(PRODUCT_ID, album_id, valid_name,
                            "/TravelMaker/"+album_name+"/"+valid_name, valid_weight, 0));
                    dbHandler.close();
                    Toast_msg = "Data Update successfully";
                    Show_Toast(Toast_msg);
                    Reset_Text();
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
                Intent view_product = new Intent(AddUpdateProduct.this,
                        ProductList.class);
                view_product.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(view_product);
                finish();
            }
        });

        add_view_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent view_product = new Intent(AddUpdateProduct.this,
                        ProductList.class);
                view_product.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);

                Log.e("ALBUM_ID in product a/u", Integer.toString(album_id));

                startActivity(view_product);
                finish();
            }
        });

    }

    public void Set_Add_Update_Screen() {

        add_name = (EditText) findViewById(R.id.product_add_name);
        add_weight = (EditText) findViewById(R.id.product_add_weight);

        add_save_btn = (Button) findViewById(R.id.product_add_save_btn);
        update_btn = (Button) findViewById(R.id.product_update_btn);
        add_view_all = (Button) findViewById(R.id.product_add_view_all);
        update_view_all = (Button) findViewById(R.id.product_update_view_all);

        add_view = (LinearLayout) findViewById(R.id.product_add_view);
        update_view = (LinearLayout) findViewById(R.id.product_update_view);

        add_view.setVisibility(View.GONE);
        update_view.setVisibility(View.GONE);

    }

    public void Is_Valid_Email(EditText edt) {
//        if (edt.getText().toString() == null) {
//            edt.setError("Invalid Email Address");
//            valid_weight = null;
//        } else {
            valid_weight = edt.getText().toString();
        //}
    }

//    boolean isEmailValid(CharSequence weight) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(weight).matches();
//    } // end of weight matcher

    public void Is_Valid_Product_Name(EditText edt) throws NumberFormatException {
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
        add_weight.getText().clear();

    }

}
