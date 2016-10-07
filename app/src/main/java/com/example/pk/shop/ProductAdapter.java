package com.example.pk.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends BaseAdapter {
    @BindView(R.id.pli_product_name)
    TextView name;
    @BindView(R.id.pli_product_number)
    TextView number;

    private Context context;
    private ArrayList<Product> products;

    public ProductAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return products.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root = layoutInflater.inflate(R.layout.product_list_item, viewGroup, false);

        ButterKnife.bind(this, root);

        name.setText(products.get(i).getName());
        number.setText(String.valueOf(products.get(i).getCount()));

        return root;
    }
}
