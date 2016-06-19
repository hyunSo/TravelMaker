package com.hardcopy.travelMaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductShow extends TravelActivity {

    static int PRODUCT_ID;
    static int album_id;
    static String album_name;

    TextView detail_text;
    Button go_back;

    String name, path, weight;

    ProductDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_show);

        PRODUCT_ID = Integer.parseInt(getIntent().getStringExtra("PRODUCT_ID"));
        album_id = Integer.parseInt(getIntent().getStringExtra("ALBUM_ID"));
        album_name = getIntent().getStringExtra("ALBUM_NAME");

        db = new ProductDatabaseHandler(this);

        Product product = db.Get_Product(PRODUCT_ID);

        name = product.getName();
        path = product.getPath();
        weight = product.getWeight();

        detail_text = (TextView)findViewById(R.id.product_detail);
        go_back = (Button)findViewById(R.id.product_back);

        detail_text.setText(album_name + " > " + name + "\n무게 : " + weight);

        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        ImageView iv = (ImageView)findViewById(R.id.product_image);
        Bitmap bm = BitmapFactory.decodeFile(path, bfo);
        Bitmap resized = Bitmap.createScaledBitmap(bm, 320, 372, true);
        iv.setImageBitmap(resized);

        go_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent product_show = new Intent(ProductShow.this,
                        ProductList.class);
                product_show.putExtra("ALBUM_ID", Integer.toString(album_id));
                product_show.putExtra("ALBUM_NAME", album_name);

                startActivity(product_show);

            }
        });


    }

}
