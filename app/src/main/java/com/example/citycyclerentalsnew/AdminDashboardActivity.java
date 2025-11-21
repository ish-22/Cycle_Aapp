package com.example.citycyclerentalsnew;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {
    private RecyclerView adminRecyclerView;
    private Button viewUsersButton, manageBikesButton, viewRentalsButton, viewPaymentsButton;
    private DatabaseHelper db;
    private ArrayList<String> adminList;
    private AdminAdapter adminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        adminRecyclerView = findViewById(R.id.adminRecyclerView);
        viewUsersButton = findViewById(R.id.viewUsersButton);
        manageBikesButton = findViewById(R.id.manageBikesButton);
        viewRentalsButton = findViewById(R.id.viewRentalsButton);
        viewPaymentsButton = findViewById(R.id.viewPaymentsButton);
        db = new DatabaseHelper(this);
        adminList = new ArrayList<>();

        adminRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminAdapter = new AdminAdapter(adminList);
        adminRecyclerView.setAdapter(adminAdapter);

        viewUsersButton.setOnClickListener(v -> displayAllUsers());
        manageBikesButton.setOnClickListener(v -> displayAllBikes());
        viewRentalsButton.setOnClickListener(v -> displayAllRentals());
        viewPaymentsButton.setOnClickListener(v -> displayAllPayments());
    }

    private void displayAllUsers() {
        adminList.clear();
        Cursor cursor = db.getAllUsers();
        if (cursor.moveToFirst()) {
            do {
                adminList.add("ID: " + cursor.getInt(0) + ", Name: " + cursor.getString(3) + ", Email: " + cursor.getString(1) + ", Phone: " + cursor.getString(4) + ", Role: " + cursor.getString(6));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adminAdapter.notifyDataSetChanged();
    }

    private void displayAllBikes() {
        adminList.clear();
        Cursor cursor = db.getAllBikes();
        if (cursor.moveToFirst()) {
            do {
                adminList.add("ID: " + cursor.getInt(0) + ", Type: " + cursor.getString(1) + ", Station: " + cursor.getString(5) + ", Available: " + cursor.getInt(3) + ", Icon: " + cursor.getString(4));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adminList.add("Example Actions: Add Bike - db.addBike('Hybrid', 1, 'url'); Update - db.updateBike(1, 'Mountain', 1, 1, 'url'); Delete - db.deleteBike(1)");
        adminAdapter.notifyDataSetChanged();
    }

    private void displayAllRentals() {
        adminList.clear();
        Cursor cursor = db.getAllRentals();
        if (cursor.moveToFirst()) {
            do {
                adminList.add("ID: " + cursor.getInt(0) + ", User: " + cursor.getString(8) + ", Bike: " + cursor.getString(6) + ", Station: " + cursor.getString(7) + ", Start: " + cursor.getString(3) + ", End: " + (cursor.getString(4) != null ? cursor.getString(4) : "Ongoing") + ", Status: " + cursor.getString(6));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adminAdapter.notifyDataSetChanged();
    }

    private void displayAllPayments() {
        adminList.clear();
        Cursor cursor = db.getAllPayments();
        if (cursor.moveToFirst()) {
            do {
                adminList.add("ID: " + cursor.getInt(0) + ", User: " + cursor.getString(6) + ", Bike: " + cursor.getString(4) + ", Station: " + cursor.getString(5) + ", Amount: $" + cursor.getDouble(2) + ", Status: " + cursor.getString(4));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adminAdapter.notifyDataSetChanged();
    }
}

class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder> {
    private ArrayList<String> items;

    public AdminAdapter(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public AdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminViewHolder holder, int position) {
        holder.textView.setText(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public AdminViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}