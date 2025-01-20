# Android Reservation Management System

## Overview
This project is an Android application designed to manage reservations for a hotel. Users can input their details, choose services, view a list of reservations, and see detailed information for each reservation. The application is structured using fragments and utilizes a SQLite database for data storage.

## Features
- **Reservation Creation**: Users can create reservations by selecting services, dates, and entering personal details.
- **List of Reservations**: Displays all reservations stored in the SQLite database.
- **Reservation Details**: Provides detailed information about a selected reservation.
- **Interactive UI**: Includes room type selection, service selection, and dynamic price calculation.

## Project Structure

### Fragments
- **StartFragment**
  - Allows users to start a reservation.
  - Features date pickers, people count management, and service selection.
  - Dynamically calculates the total price of reservations.
- **DetailsFragment**
  - Displays detailed information for a selected reservation.
  - Enables navigation back to the reservation list.
- **ListReservationsFragment**
  - Lists all reservations stored in the database.
  - Users can select a reservation to view its details.

### Adapters
- **ReservationAdapter**: A custom adapter for displaying reservations in a ListView.
- **ViewPagerAdapter**: Manages navigation between fragments using ViewPager2.

### Database
- **DatabaseHelper**
  - Manages the SQLite database.
  - Handles CRUD operations for reservations.

### Model
- **Reservation**: Represents a reservation with fields such as name, email, number of people, dates, total price, and selected services.

## Technologies Used
- **Android Studio**
- **Java**
- **SQLite**
- **ViewPager2**

## Screenshots
Add screenshots of your app's UI here.

## How to Run
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/android-reservation-app.git
   ```
2. Open the project in Android Studio.
3. Build the project to ensure all dependencies are resolved.
4. Run the app on an emulator or a physical Android device.

## Dependencies
- Minimum SDK: 21 (Android 5.0 Lollipop)
- Target SDK: 33

## Code Explanation

### StartFragment
Manages the initial reservation process, including:
- Date selection using `DatePickerDialog`.
- People count increment and decrement.
- Service and room selection.

### DetailsFragment
Displays detailed information about a selected reservation, including:
- Reservation ID
- User details (name, email)
- Selected services and total price

### ListReservationsFragment
Fetches and displays all reservations stored in the SQLite database using `ReservationAdapter`.

### DatabaseHelper
Handles database creation, data insertion, updates, deletions, and retrieval.
- Tables: `reservations`
- Columns: `id`, `name`, `email`, `people_count`, `start_date`, `end_date`, `total_days`, `total_price`, `services`

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

Feel free to contribute to this project by submitting pull requests or issues.
