package com.example.lab3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private TextView detailsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        detailsTextView = view.findViewById(R.id.details_text_view);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        // Load reservation details from arguments if available
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("reservationId")) {
            long reservationId = bundle.getLong("reservationId", -1);
            Log.d(TAG, "Received reservationId: " + reservationId);

            if (reservationId != -1) {
                loadReservationDetails(reservationId);
            } else {
                showError("Invalid reservation ID.");
            }
        }

        // Confirm button navigates back to ListReservationsFragment
        confirmButton.setOnClickListener(v -> {
            ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager);
            if (viewPager != null) {
                viewPager.setCurrentItem(2, true); // Navigate back to ListReservationsFragment
                Log.d(TAG, "Navigating back to ListReservationsFragment.");
            } else {
                Log.e(TAG, "ViewPager2 not found in activity.");
            }
        });

        return view;
    }

    private void loadReservationDetails(long reservationId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        Reservation reservation = databaseHelper.getReservationById(reservationId);

        if (reservation != null) {
            Log.d(TAG, "Reservation details: " + reservation.toString());
            String details = "ID: " + reservation.getId() +
                    "\nName: " + reservation.getName() +
                    "\nEmail: " + reservation.getEmail() +
                    "\nNumber of People: " + reservation.getPeopleCount() +
                    "\nStart Date: " + reservation.getStartDate() +
                    "\nEnd Date: " + reservation.getEndDate() +
                    "\nTotal Days: " + reservation.getTotalDays() +
                    "\nTotal Price: $" + reservation.getTotalPrice() +
                    "\nServices:\n" + reservation.getServices();
            detailsTextView.setText(details);
        } else {
            showError("No reservation found for ID: " + reservationId);
        }
    }

    private void showError(String errorMessage) {
        if (detailsTextView != null) {
            detailsTextView.setText(errorMessage);
        }
        Log.e(TAG, errorMessage);
    }

    // Method to dynamically update reservation details
    public void updateReservationDetails(long reservationId) {
        Log.d(TAG, "Updating reservation details with ID: " + reservationId);
        loadReservationDetails(reservationId); // Reload details for the new reservation
    }

}
