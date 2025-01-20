package com.example.lab3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.viewpager2.widget.ViewPager2;
import java.util.List;
import java.util.ArrayList;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class StartFragment extends Fragment {

    private Button startDateButton, endDateButton, startReservationButton, plusButton, minusButton;
    private TextView peopleCountDisplay;
    private ImageButton menuButton;
    private Calendar calendar;
    private int peopleCount = 1; // Minimum value
    private String selectedRoomType = null; // "Simple", "Princesse", or "Royale"
    private int selectedRoomQuantity = 0;   // Number of rooms selected

    private int selectedSaunaQuantity = 0;   // Number of sauna services selected
    private int selectedMassageQuantity = 0; // Number of massages selected
    private int selectedBuffetQuantity = 0;  // Number of buffet services selected

  // Selected beauty services (e.g., "Manicure et Pédicure")
    private int selectedBeautyQuantity = 0;

    private EditText nameField, emailField;
    private List<String> selectedBeautyServices = new ArrayList<>();





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        nameField = view.findViewById(R.id.name_field);
        emailField = view.findViewById(R.id.email_field);
        // Initialize views
        menuButton = view.findViewById(R.id.menu_button);
        startDateButton = view.findViewById(R.id.start_date_button);
        endDateButton = view.findViewById(R.id.end_date_button);
        startReservationButton = view.findViewById(R.id.start_reservation_button);
        plusButton = view.findViewById(R.id.plus_button);
        minusButton = view.findViewById(R.id.minus_button);
        peopleCountDisplay = view.findViewById(R.id.people_count_display);
        startReservationButton.setOnClickListener(v -> calculateTotalReservationPriceAndNavigate());

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Set listener for the menu button
        menuButton.setOnClickListener(v -> showPopupMenu(v));

        // Set listeners for date picker buttons
        startDateButton.setOnClickListener(v -> showDatePickerDialog(true));
        endDateButton.setOnClickListener(v -> showDatePickerDialog(false));

        // Increment and decrement number of people
        plusButton.setOnClickListener(v -> {
            peopleCount++;
            updatePeopleCountDisplay();
        });

        minusButton.setOnClickListener(v -> {
            if (peopleCount > 1) {
                peopleCount--;
                updatePeopleCountDisplay();
            } else {
                Toast.makeText(getContext(), "Le nombre de personnes ne peut pas être inférieur à 1.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private String startDate = null; // Holds the start date selected by the user
    private String endDate = null;   // Holds the end date selected by the user

    private void showDatePickerDialog(boolean isStartDate) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    if (isStartDate) {
                        startDate = selectedDate; // Save the start date
                        startDateButton.setText("Date de début: " + selectedDate);
                    } else {
                        endDate = selectedDate; // Save the end date
                        endDateButton.setText("Date de fin: " + selectedDate);
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private boolean isDateInHighSeason() {
        // Define high season periods
        Calendar startHighSeason1 = Calendar.getInstance();
        startHighSeason1.set(Calendar.MONTH, Calendar.JUNE);
        startHighSeason1.set(Calendar.DAY_OF_MONTH, 21);

        Calendar endHighSeason1 = Calendar.getInstance();
        endHighSeason1.set(Calendar.MONTH, Calendar.AUGUST);
        endHighSeason1.set(Calendar.DAY_OF_MONTH, 20);

        Calendar startHighSeason2 = Calendar.getInstance();
        startHighSeason2.set(Calendar.MONTH, Calendar.DECEMBER);
        startHighSeason2.set(Calendar.DAY_OF_MONTH, 18);

        Calendar endHighSeason2 = Calendar.getInstance();
        endHighSeason2.set(Calendar.MONTH, Calendar.JANUARY);
        endHighSeason2.set(Calendar.DAY_OF_MONTH, 7);

        Calendar startDateCalendar = parseDate(startDate);
        Calendar endDateCalendar = parseDate(endDate);

        // Check if any part of the date range overlaps with high season
        return (startDateCalendar != null && endDateCalendar != null) &&
                ((startDateCalendar.compareTo(startHighSeason1) >= 0 && startDateCalendar.compareTo(endHighSeason1) <= 0) ||
                        (endDateCalendar.compareTo(startHighSeason2) >= 0 && endDateCalendar.compareTo(endHighSeason2) <= 0));
    }

    /**
     * Helper function to parse a date string in the format "dd/MM/yyyy" into a Calendar object.
     */
    private Calendar parseDate(String dateStr) {
        try {
            String[] parts = dateStr.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1; // Calendar months are zero-based
            int year = Integer.parseInt(parts[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);

            return calendar;
        } catch (Exception e) {
            return null;
        }
    }



    private void updatePeopleCountDisplay() {
        peopleCountDisplay.setText(String.valueOf(peopleCount));
    }

    private void showPopupMenu(View anchor) {
        // Create and show the PopupMenu
        PopupMenu popupMenu = new PopupMenu(requireContext(), anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popupMenu.getMenu());

        // Handle menu item clicks
        popupMenu.setOnMenuItemClickListener(item -> handleMenuItemClick(item));
        popupMenu.show();
    }

    private boolean handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_chambre) {
            showChambreDialog();
            return true;
        } else if (id == R.id.menu_sauna) {
            showServiceDialog("Sauna impérial", 25, 35);
            return true;
        } else if (id == R.id.menu_massage) {
            showServiceDialog("Massage", 40, 52);
            return true;
        } else if (id == R.id.menu_buffet) {
            showServiceDialog("Buffet des rois", 32, 39);
            return true;
        } else if (id == R.id.menu_soins_de_beaute) {
            showSoinsDeBeauteDialog();
            return true;
        }

        return false;
    }

    private void showChambreDialog() {
        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_chambre, null);

        // Initialize dialog views
        RadioGroup roomTypeGroup = dialogView.findViewById(R.id.room_type_group);
        TextView quantityText = dialogView.findViewById(R.id.room_quantity);
        Button plusButton = dialogView.findViewById(R.id.plus_button);
        Button minusButton = dialogView.findViewById(R.id.minus_button);

        final int[] quantity = {1}; // Default quantity
        final int[] selectedPrice = {78}; // Default price for Chambre Simple
        final boolean[] isWeekend = {false}; // Default: Weekday price

        // Determine if the selected date range is during the high season
        boolean isHighSeason = isDateInHighSeason();

        // Set up quantity increment/decrement with limit based on `peopleCount`
        plusButton.setOnClickListener(v -> {
            if (quantity[0] < peopleCount) {
                quantity[0]++;
                quantityText.setText(String.format("%02d", quantity[0]));
            } else {
                Toast.makeText(getContext(), "Le nombre de chambres ne peut pas dépasser le nombre de personnes.", Toast.LENGTH_SHORT).show();
            }
        });

        minusButton.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityText.setText(String.format("%02d", quantity[0]));
            }
        });

        // Update price based on selected room type
        roomTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_simple) {
                selectedPrice[0] = calculateRoomPrice(78, 80, 85, 92, isWeekend[0], isHighSeason);
            } else if (checkedId == R.id.radio_princesse) {
                selectedPrice[0] = calculateRoomPrice(150, 165, 170, 185, isWeekend[0], isHighSeason);
            } else if (checkedId == R.id.radio_royale) {
                selectedPrice[0] = calculateRoomPrice(180, 195, 205, 223, isWeekend[0], isHighSeason);
            }
        });

        // Show the dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Sélectionnez un type de chambre")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    selectedRoomQuantity = quantity[0]; // Persist quantity
                    int selectedId = roomTypeGroup.getCheckedRadioButtonId();


                    if (selectedId == R.id.radio_simple) {
                        selectedRoomType = "Simple";
                    } else if (selectedId == R.id.radio_princesse) {
                        selectedRoomType = "Princesse";
                    } else if (selectedId == R.id.radio_royale) {
                        selectedRoomType = "Royale";
                    } else {
                        selectedRoomType = "Aucun";
                    }
                })
                .setNegativeButton("Annuler", null)
                .create()
                .show();
    }

    /**
     * Helper function to calculate the room price based on the type, season, and day.
     */
    private int calculateRoomPrice(int weekdayPrice, int weekendPrice, int highSeasonWeekdayPrice, int highSeasonWeekendPrice, boolean isWeekend, boolean isHighSeason) {
        if (isHighSeason) {
            return isWeekend ? highSeasonWeekendPrice : highSeasonWeekdayPrice;
        } else {
            return isWeekend ? weekendPrice : weekdayPrice;
        }
    }

    /**
     * Helper function to check if the selected date range is within the high season.
     */

    private void showServiceDialog(String title, int weekdayPrice, int weekendPrice) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_service, null);

        RadioGroup serviceTypeGroup = dialogView.findViewById(R.id.service_type_group);
        TextView quantityText = dialogView.findViewById(R.id.service_quantity);
        Button plusButton = dialogView.findViewById(R.id.plus_button);
        Button minusButton = dialogView.findViewById(R.id.minus_button);

        final int[] quantity = {1};
        final int[] selectedPrice = {weekdayPrice};

        // Handle quantity increment and decrement
        plusButton.setOnClickListener(v -> {
            if (quantity[0] < peopleCount) {
                quantity[0]++;
                quantityText.setText(String.format("%02d", quantity[0]));
            } else {
                Toast.makeText(getContext(), "Le maximum de services est limité au nombre de personnes.", Toast.LENGTH_SHORT).show();
            }
        });

        minusButton.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityText.setText(String.format("%02d", quantity[0]));
            }
        });

        // Update price based on selected type
        serviceTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_weekday) {
                selectedPrice[0] = weekdayPrice;
            } else if (checkedId == R.id.radio_weekend) {
                selectedPrice[0] = weekendPrice;
            }
        });

        // Show the dialog
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Save the selected quantity based on the title
                    switch (title) {
                        case "Sauna impérial":
                            selectedSaunaQuantity = quantity[0];
                            break;
                        case "Massage":
                            selectedMassageQuantity = quantity[0];
                            break;
                        case "Buffet des rois":
                            selectedBuffetQuantity = quantity[0];
                            break;
                    }

                    int totalPrice = quantity[0] * selectedPrice[0];
                    Toast.makeText(getContext(), "Prix total pour " + title + ": $" + totalPrice, Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Non", null)
                .create()
                .show();
    }


    private void showSoinsDeBeauteDialog() {
        // Initialize the list to store selected beauty services
        selectedBeautyServices = new ArrayList<>();

        // Define the options for beauty services
        String[] soinsOptions = {"Manicure et Pédicure", "Soins de visage", "Coiffure"};

        // Initialize an array to keep track of checked items
        boolean[] checkedItems = new boolean[soinsOptions.length];

        // Create and show the AlertDialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Sélectionnez des soins de beauté")
                .setMultiChoiceItems(soinsOptions, checkedItems, (dialog, which, isChecked) -> {
                    // Update the list of selected beauty services based on user input
                    if (isChecked) {
                        selectedBeautyServices.add(soinsOptions[which]);
                    } else {
                        selectedBeautyServices.remove(soinsOptions[which]);
                    }
                })
                .setPositiveButton("Confirmer", (dialog, which) -> {
                    // Handle confirmation if necessary
                    if (selectedBeautyServices.isEmpty()) {
                        Toast.makeText(requireContext(), "Aucun soin sélectionné.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(),
                                "Soins sélectionnés: " + String.join(", ", selectedBeautyServices),
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .create()
                .show();
    }

    private void calculateTotalReservationPriceAndNavigate() {
        if (startDate == null || endDate == null) {
            Toast.makeText(getContext(), "Veuillez sélectionner les dates de début et de fin.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the selected dates
        Calendar startDateCalendar = parseDate(startDate);
        Calendar endDateCalendar = parseDate(endDate);

        if (startDateCalendar == null || endDateCalendar == null) {
            Toast.makeText(getContext(), "Dates invalides, veuillez réessayer.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate total days, weekends, and weekdays
        int totalDays = calculateTotalDays(startDateCalendar, endDateCalendar);
        int weekendDays = calculateWeekendDays(startDateCalendar, endDateCalendar);
        int weekdayDays = totalDays - weekendDays;

        boolean isHighSeason = isDateInHighSeason();

        // Calculate room cost
        int roomCost = 0;
        if (selectedRoomType != null && selectedRoomQuantity > 0) {
            switch (selectedRoomType) {
                case "Simple":
                    roomCost = calculateRoomCost(78, 80, 85, 92, weekdayDays, weekendDays, isHighSeason, selectedRoomQuantity);
                    break;
                case "Princesse":
                    roomCost = calculateRoomCost(150, 165, 170, 185, weekdayDays, weekendDays, isHighSeason, selectedRoomQuantity);
                    break;
                case "Royale":
                    roomCost = calculateRoomCost(180, 195, 205, 223, weekdayDays, weekendDays, isHighSeason, selectedRoomQuantity);
                    break;
            }
        }

        // Calculate service costs
        int serviceCost = 0;
        if (selectedSaunaQuantity > 0) {
            serviceCost += calculateServiceCost(25, 35, 45, 53, weekdayDays, weekendDays, isHighSeason, selectedSaunaQuantity);
        }
        if (selectedMassageQuantity > 0) {
            serviceCost += calculateServiceCost(40, 52, 40, 52, weekdayDays, weekendDays, false, selectedMassageQuantity);
        }
        if (selectedBuffetQuantity > 0) {
            serviceCost += calculateServiceCost(32, 39, 32, 39, weekdayDays, weekendDays, false, selectedBuffetQuantity);
        }
        if (selectedBeautyServices != null && !selectedBeautyServices.isEmpty()) {
            for (String service : selectedBeautyServices) {
                switch (service) {
                    case "Manicure et Pédicure":
                        serviceCost += calculateServiceCost(22, 30, 22, 30, weekdayDays, weekendDays, false, selectedBeautyQuantity);
                        break;
                    case "Soins de visage":
                        serviceCost += calculateServiceCost(25, 35, 25, 35, weekdayDays, weekendDays, false, selectedBeautyQuantity);
                        break;
                    case "Coiffure":
                        serviceCost += calculateServiceCost(32, 40, 32, 40, weekdayDays, weekendDays, false, selectedBeautyQuantity);
                        break;
                }
            }
        }

        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Veuillez entrer votre nom et votre email.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate the grand total
        int grandTotal = roomCost + serviceCost;

        // Save the data to the database
        // Save the data to the database
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        long reservationId = databaseHelper.insertReservation(
                name,
                email,
                peopleCount,
                startDate,
                endDate,
                totalDays,
                grandTotal,
                getSelectedServicesString()
        );

        if (reservationId != -1) {
            Toast.makeText(getContext(), "Réservation enregistrée avec succès!", Toast.LENGTH_SHORT).show();

            Bundle bundle = new Bundle();
            bundle.putLong("reservationId", reservationId);

            ViewPager2 viewPager = requireActivity().findViewById(R.id.view_pager);
            if (viewPager != null) {
                // Get the adapter and access DetailsFragment
                ViewPagerAdapter adapter = (ViewPagerAdapter) viewPager.getAdapter();
                if (adapter != null) {
                    DetailsFragment detailsFragment = (DetailsFragment) adapter.getFragmentAt(1);
                    if (detailsFragment != null) {
                        // Pass the reservation ID to DetailsFragment
                        detailsFragment.setArguments(bundle);
                        detailsFragment.updateReservationDetails(reservationId);
                    } else {
                        Log.e("StartFragment", "DetailsFragment is null.");
                    }
                }

                // Navigate to DetailsFragment
                viewPager.setCurrentItem(1, true);
            } else {
                Toast.makeText(getContext(), "Échec de la navigation.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Échec de l'enregistrement de la réservation.", Toast.LENGTH_SHORT).show();
        }

    }





    private String getSelectedServicesString() {
        StringBuilder selectedServices = new StringBuilder();

        if (selectedRoomType != null) {
            selectedServices.append("Room Type: ").append(selectedRoomType).append("\n");
        }
        if (selectedSaunaQuantity > 0) {
            selectedServices.append("Sauna: ").append(selectedSaunaQuantity).append("\n");
        }
        if (selectedMassageQuantity > 0) {
            selectedServices.append("Massage: ").append(selectedMassageQuantity).append("\n");
        }
        if (selectedBuffetQuantity > 0) {
            selectedServices.append("Buffet: ").append(selectedBuffetQuantity).append("\n");
        }
        if (selectedBeautyServices != null && !selectedBeautyServices.isEmpty())
        {
            selectedServices.append("Beauty Services: ");
            for (String service : selectedBeautyServices) {
                selectedServices.append(service).append(", ");
            }
            selectedServices.setLength(selectedServices.length() - 2); // Remove trailing comma
        }

        return selectedServices.toString();
    }
    private int calculateServiceCost(int weekdayPrice, int weekendPrice, int highSeasonWeekdayPrice, int highSeasonWeekendPrice,
                                     int weekdayDays, int weekendDays, boolean isHighSeason, int quantity) {
        if (isHighSeason) {
            return (highSeasonWeekdayPrice * weekdayDays + highSeasonWeekendPrice * weekendDays) * quantity;
        } else {
            return (weekdayPrice * weekdayDays + weekendPrice * weekendDays) * quantity;
        }
    }
    private int calculateRoomCost(int weekdayPrice, int weekendPrice, int highSeasonWeekdayPrice, int highSeasonWeekendPrice,
                                  int weekdayDays, int weekendDays, boolean isHighSeason, int quantity) {
        if (isHighSeason) {
            return (highSeasonWeekdayPrice * weekdayDays + highSeasonWeekendPrice * weekendDays) * quantity;
        } else {
            return (weekdayPrice * weekdayDays + weekendPrice * weekendDays) * quantity;
        }
    }
    private int calculateWeekendDays(Calendar start, Calendar end) {
        int weekendDays = 0;
        Calendar temp = (Calendar) start.clone();

        while (!temp.after(end)) {
            int dayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                weekendDays++;
            }
            temp.add(Calendar.DAY_OF_MONTH, 1);
        }

        return weekendDays;
    }
    private int calculateTotalDays(Calendar start, Calendar end) {
        long differenceInMillis = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (differenceInMillis / (1000 * 60 * 60 * 24)) + 1; // Include start date
    }




}
