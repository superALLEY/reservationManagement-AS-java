package com.example.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;
import java.util.List;

public class ReservationAdapter extends BaseAdapter {

    private final Context context;
    private final List<Reservation> reservations;

    public ReservationAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reservations.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false);
        }

        Reservation reservation = reservations.get(position);

        TextView idView = convertView.findViewById(R.id.reservation_id);
        TextView nameView = convertView.findViewById(R.id.reservation_name);
        TextView emailView = convertView.findViewById(R.id.reservation_email);
        TextView datesView = convertView.findViewById(R.id.reservation_dates);

        idView.setText("ID: " + reservation.getId());
        nameView.setText("Name: " + reservation.getName());
        emailView.setText("Email: " + reservation.getEmail());
        datesView.setText("Dates: " + reservation.getStartDate() + " - " + reservation.getEndDate());

        return convertView;
    }
}
