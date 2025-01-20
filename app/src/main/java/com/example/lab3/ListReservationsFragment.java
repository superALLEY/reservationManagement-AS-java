package com.example.lab3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.List;

public class ListReservationsFragment extends Fragment {

    private static final String TAG = "ListReservationsFragment";
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reservations, container, false);

        ListView reservationsListView = view.findViewById(R.id.reservations_list_view);

        databaseHelper = new DatabaseHelper(getContext());
        List<Reservation> reservations = databaseHelper.getAllReservationsList();

        if (reservations == null || reservations.isEmpty()) {
            Log.e(TAG, "Reservations list is empty or null");
        } else {
            Log.d(TAG, "Reservations fetched: " + reservations.size());
            ReservationAdapter adapter = new ReservationAdapter(getContext(), reservations);
            reservationsListView.setAdapter(adapter);
        }


        // Use the custom ReservationAdapter
        ReservationAdapter adapter = new ReservationAdapter(getContext(), reservations);
        reservationsListView.setAdapter(adapter);

        // Set a click listener to navigate to DetailsFragment
        reservationsListView.setOnItemClickListener((parent, view1, position, id) -> {
            Reservation selectedReservation = reservations.get(position);
            if (selectedReservation != null) {
                Log.d(TAG, "Selected reservation: " + selectedReservation);

                ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager);
                if (viewPager != null) {
                    ViewPagerAdapter adapter1 = (ViewPagerAdapter) viewPager.getAdapter();
                    if (adapter1 != null) {
                        DetailsFragment detailsFragment = (DetailsFragment) adapter1.getFragmentAt(1);
                        if (detailsFragment != null) {
                            // Dynamically update the DetailsFragment
                            detailsFragment.updateReservationDetails(selectedReservation.getId());
                            viewPager.setCurrentItem(1, true); // Navigate to DetailsFragment
                            Log.d(TAG, "Navigating to DetailsFragment with ID: " + selectedReservation.getId());
                        } else {
                            Log.e(TAG, "DetailsFragment not found.");
                        }
                    } else {
                        Log.e(TAG, "ViewPagerAdapter is null.");
                    }
                } else {
                    Log.e(TAG, "ViewPager2 not found in activity.");
                }
            } else {
                Log.e(TAG, "Selected reservation is null.");
            }
        });

        return view;
    }
}
