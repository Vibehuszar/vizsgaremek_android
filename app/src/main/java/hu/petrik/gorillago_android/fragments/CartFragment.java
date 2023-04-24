package hu.petrik.gorillago_android.fragments;
import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.petrik.gorillago_android.R;
import hu.petrik.gorillago_android.classes.CartItem;
import hu.petrik.gorillago_android.classes.MenuItem;

public class CartFragment extends Fragment {
    private TextView textViewCart, textViewTotalPrice;
    private ListView listViewCart;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("CartFragment", "onAttach called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        View footerView = inflater.inflate(R.layout.cart_footer, null);
        init(view);
        textViewTotalPrice = footerView.findViewById(R.id.textViewTotalPrice);
        initListView(); // initialize listViewCart before creating the adapter
        CartAdapter adapter = new CartAdapter();
        listViewCart.setAdapter(adapter);
        listViewCart.addFooterView(footerView);
        return view;
    }

    private void init(View view) {
        textViewCart = view.findViewById(R.id.textViewCart);
        listViewCart = view.findViewById(R.id.listViewCart);
    }
    private void initListView() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shopping_cart", MODE_PRIVATE);
        int totalprice = sharedPreferences.getInt("total_price", 0);
        textViewTotalPrice.setText("Összesen: " + String.valueOf(totalprice) + "Ft");
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("total_price")) {
                totalprice = (int) value;
            } else {
                String[] itemArray = ((String) value).split(",");
                int quantity = Integer.parseInt(itemArray[0]);
                int price = Integer.parseInt(itemArray[1]);
                String url = itemArray[2];
                CartItem cartItem = new CartItem(url, key, price, quantity);
                cartItems.add(cartItem);
            }
        }
    }
    private class CartAdapter extends ArrayAdapter<CartItem> {
        private HashMap<MenuItem, Integer> quantityMap = new HashMap<>();

        public CartAdapter() {
            super(listViewCart.getContext(), R.layout.cartadapter, cartItems);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.cartadapter, parent, false);
            }

            CartItem actualCart = cartItems.get(position);
            ImageView imageViewItem = convertView.findViewById(R.id.cart_item_image);
            TextView textViewItemName = convertView.findViewById(R.id.cart_item_name);
            TextView textViewItemPrice = convertView.findViewById(R.id.cart_item_price);
            TextView textViewItemQuantity = convertView.findViewById(R.id.cart_item_quantity);
            Button buttonPlus = convertView.findViewById(R.id.cart_buttonPlus);
            Button buttonNegative = convertView.findViewById(R.id.cart_buttonNegative);
            Picasso.get().load(actualCart.getUrl()).into(imageViewItem);
            textViewItemName.setText(actualCart.getName());
            textViewItemPrice.setText(String.valueOf(actualCart.getPricePerItem()));
            textViewItemQuantity.setText(String.valueOf(actualCart.getQuantity()));

            return convertView;
        }
    }
}
