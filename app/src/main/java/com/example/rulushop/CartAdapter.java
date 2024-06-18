package com.example.rulushop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTxt, priceTxt, totalEachItem, quantityTxt;
        private ImageView pic;
        private TextView plusCartBtn, minusCartBtn, remove_btn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.textView8);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            pic = itemView.findViewById(R.id.pic);
            plusCartBtn = itemView.findViewById(R.id.PlusCartBtn);
            minusCartBtn = itemView.findViewById(R.id.minusCartBtn);
            remove_btn = itemView.findViewById(R.id.remove_txt);

            remove_btn.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ((CartActivity) itemView.getContext()).removeCartItem(cartItems.get(position).getId());
                }
            });

            plusCartBtn.setOnClickListener(v -> {
                CartItem item = cartItems.get(getAdapterPosition());
                item.setQuantity(item.getQuantity() + 1);
                notifyItemChanged(getAdapterPosition());
                // Call updateTotals in CartActivity
                ((CartActivity) itemView.getContext()).updateTotals(cartItems);
            });

            minusCartBtn.setOnClickListener(v -> {
                CartItem item = cartItems.get(getAdapterPosition());
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    notifyItemChanged(getAdapterPosition());
                    // Call updateTotals in CartActivity
                    ((CartActivity) itemView.getContext()).updateTotals(cartItems);
                }
            });
        }

        public void bind(CartItem cartItem) {
            Glide.with(itemView.getContext()).load(cartItem.getImageUrl()).into(pic);
            titleTxt.setText(cartItem.getTitle());
            priceTxt.setText(String.format("F CFA %.2f", cartItem.getPrice()));
            totalEachItem.setText(String.format("F CFA %.2f", cartItem.getPrice() * cartItem.getQuantity()));
            quantityTxt.setText(String.valueOf(cartItem.getQuantity()));
            // Load image using your preferred image loading library
        }

    }

}
