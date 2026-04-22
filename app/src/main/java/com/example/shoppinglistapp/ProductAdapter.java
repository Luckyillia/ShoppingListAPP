package com.example.shoppinglistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product>    productList;
    private final OnDeleteListener deleteListener;

    public interface OnDeleteListener {
        void onDelete(long id);
    }

    public ProductAdapter(List<Product> productList, OnDeleteListener deleteListener) {
        this.productList    = productList;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = productList.get(position);

        holder.tvProductName.setText(p.getName());
        holder.tvQuantity.setText(p.getQuantityString()); // NIE p.getQuantity() – crash!
        holder.tvCategory.setText("Kategoria: " + p.getCategory());

        // Klik tylko na przycisku Usuń – nie na całym wierszu
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(p.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvProductName;
        public final TextView tvQuantity;
        public final TextView tvCategory;
        public final Button   btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity    = itemView.findViewById(R.id.tvQuantity);
            tvCategory    = itemView.findViewById(R.id.tvCategory);
            btnDelete     = itemView.findViewById(R.id.btnDelete);
        }
    }
}
