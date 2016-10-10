package com.example.pk.shop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pk.shop.R;
import com.example.pk.shop.models.ProductModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProductModel> productModels;

    public RecyclerViewAdapter(Context context, ArrayList<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(R.layout.product_recycler_view_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(productModels.get(position).getName());
        holder.number.setText(String.valueOf(productModels.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.prvi_product_name)
        TextView name;
        @BindView(R.id.prvi_product_number)
        TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
