package com.example.rulushop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_pop_list, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getTitle());
        holder.ratingBar.setRating(product.getRating());
        holder.price.setText(String.format("%.2f F CFA", product.getPrice()));

        // Utiliser Glide pour charger l'image
        Glide.with(context)
                .load(product.getImg())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("image", product.getImg());
            intent.putExtra("title", product.getTitle());
            intent.putExtra("price", String.format("%.2f F CFA", product.getPrice()));
            intent.putExtra("description", product.getDescription());
            intent.putExtra("rating", product.getRating());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView imageView;
        RatingBar ratingBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.oldPricetxt);
            imageView = itemView.findViewById(R.id.imageView3);
            ratingBar = itemView.findViewById(R.id.ratingBar2);
        }

    }
    public void filterList(List<Product> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }
}
