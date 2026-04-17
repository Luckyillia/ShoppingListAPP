package com.example.shoppinglistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ContactViewHolder> {

    private List<Product> productList;
    private OnContactDeleteListener deleteListener;

    public interface OnContactDeleteListener {
        void onContactDelete(long id);
    }

    public ProductAdapter(List<Product> productList, OnContactDeleteListener deleteListener) {
        this.productList = productList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        holder.tvProductName.setText(currentProduct.getName());
        holder.tvQuantity.setText(currentProduct.getQuantity());
        holder.tvCategory.setText("Kategoria: " + currentProduct.getCategory());

        holder.itemView.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onContactDelete(currentProduct.getId());
                Toast.makeText(v.getContext(),
                        "Usunieto: " + currentProduct.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName;
        public TextView tvQuantity;
        public TextView tvCategory;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}