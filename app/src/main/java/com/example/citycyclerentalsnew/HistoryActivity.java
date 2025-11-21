package com.example.citycyclerentalsnew;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private DatabaseHelper db;
    private SharedPreferences prefs;
    private ArrayList<Rental> historyList;
    private RentalAdapter rentalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        historyList = new ArrayList<>();

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rentalAdapter = new RentalAdapter(historyList);
        historyRecyclerView.setAdapter(rentalAdapter);

        int userId = prefs.getInt("userId", -1);
        loadHistory(userId);
    }

    private void loadHistory(int userId) {
        Cursor cursor = db.getRentalHistory(userId);
        if (cursor.moveToFirst()) {
            do {
                Rental rental = new Rental(
                        cursor.getInt(0), // rental_id
                        cursor.getString(6), // type
                        cursor.getString(7), // station
                        cursor.getString(3), // start_time
                        cursor.getString(4), // end_time
                        cursor.getInt(5), // duration
                        cursor.getDouble(8) // amount
                );
                historyList.add(rental);
            } while (cursor.moveToNext());
        }
        cursor.close();
        rentalAdapter.notifyDataSetChanged();
    }
}

class Rental {
    private int id, duration;
    private String type, station, startTime, endTime;
    private double amount;

    public Rental(int id, String type, String station, String startTime, String endTime, int duration, double amount) {
        this.id = id;
        this.type = type;
        this.station = station;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.amount = amount;
    }

    public String getType() { return type; }
    public String getStation() { return station; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public int getDuration() { return duration; }
    public double getAmount() { return amount; }
}

class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.RentalViewHolder> {
    private ArrayList<Rental> rentals;

    public RentalAdapter(ArrayList<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public RentalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rental, parent, false);
        return new RentalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RentalViewHolder holder, int position) {
        Rental rental = rentals.get(position);
        holder.typeTextView.setText(rental.getType());
        holder.stationTextView.setText(rental.getStation());
        holder.startTextView.setText("Start: " + rental.getStartTime());
        holder.endTextView.setText("End: " + (rental.getEndTime() != null ? rental.getEndTime() : "Ongoing"));
        holder.durationTextView.setText("Duration: " + rental.getDuration() + " mins");
        holder.amountTextView.setText("Cost: $" + rental.getAmount());
    }

    @Override
    public int getItemCount() { return rentals.size(); }

    static class RentalViewHolder extends RecyclerView.ViewHolder {
        TextView typeTextView, stationTextView, startTextView, endTextView, durationTextView, amountTextView;

        public RentalViewHolder(View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.rentalTypeTextView);
            stationTextView = itemView.findViewById(R.id.rentalStationTextView);
            startTextView = itemView.findViewById(R.id.rentalStartTextView);
            endTextView = itemView.findViewById(R.id.rentalEndTextView);
            durationTextView = itemView.findViewById(R.id.rentalDurationTextView);
            amountTextView = itemView.findViewById(R.id.rentalAmountTextView);
        }
    }
}