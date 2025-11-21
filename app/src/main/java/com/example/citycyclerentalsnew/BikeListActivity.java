package com.example.citycyclerentalsnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BikeListActivity extends AppCompatActivity {
    private EditText stationFilterEditText;
    private RecyclerView bikeRecyclerView;
    private DatabaseHelper db;
    private SharedPreferences prefs;
    private ArrayList<Bike> bikeList;
    private BikeAdapter bikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike_list);

        stationFilterEditText = findViewById(R.id.stationFilterEditText);
        bikeRecyclerView = findViewById(R.id.bikeRecyclerView);
        Button profileButton = findViewById(R.id.profileButton);
        Button historyButton = findViewById(R.id.historyButton);
        Button pricingButton = findViewById(R.id.pricingButton);
        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        bikeList = new ArrayList<>();

        bikeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bikeAdapter = new BikeAdapter(bikeList, bike -> {
            Intent intent = new Intent(this, RentalActivity.class);
            intent.putExtra("bike_id", bike.getId());
            startActivity(intent);
        });
        bikeRecyclerView.setAdapter(bikeAdapter);

        loadBikes("");

        stationFilterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                bikeList.clear();
                loadBikes(s.toString());
                bikeAdapter.notifyDataSetChanged();
            }
        });

        profileButton.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        historyButton.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        pricingButton.setOnClickListener(v -> startActivity(new Intent(this, PricingActivity.class)));
    }

    private void loadBikes(String stationFilter) {
        Cursor cursor = db.getAvailableBikes(stationFilter);
        if (cursor.moveToFirst()) {
            do {
                Bike bike = new Bike(
                        cursor.getInt(0), // bike_id
                        cursor.getString(1), // type
                        cursor.getString(5), // station name
                        cursor.getString(4) // icon_url
                );
                bikeList.add(bike);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}

class Bike {
    private int id;
    private String type, station, iconUrl;

    public Bike(int id, String type, String station, String iconUrl) {
        this.id = id;
        this.type = type;
        this.station = station;
        this.iconUrl = iconUrl;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getStation() { return station; }
    public String getIconUrl() { return iconUrl; }
}

class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.BikeViewHolder> {
    private ArrayList<Bike> bikes;
    private OnBikeClickListener listener;

    public BikeAdapter(ArrayList<Bike> bikes, OnBikeClickListener listener) {
        this.bikes = bikes;
        this.listener = listener;
    }

    @Override
    public BikeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bike, parent, false);
        return new BikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BikeViewHolder holder, int position) {
        Bike bike = bikes.get(position);
        holder.typeTextView.setText(bike.getType());
        holder.stationTextView.setText(bike.getStation());
        Picasso.get().load(bike.getIconUrl()).into(holder.iconImageView);
        holder.itemView.setOnClickListener(v -> listener.onBikeClick(bike));
    }

    @Override
    public int getItemCount() { return bikes.size(); }

    static class BikeViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView typeTextView, stationTextView;

        public BikeViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.bikeIconImageView);
            typeTextView = itemView.findViewById(R.id.bikeTypeTextView);
            stationTextView = itemView.findViewById(R.id.bikeStationTextView);
        }
    }

    interface OnBikeClickListener {
        void onBikeClick(Bike bike);
    }
}