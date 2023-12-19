package com.example.minorproject;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker driverMarker1;
    private Marker driverMarker2;
    private Marker studentMarker;
    private float minDistance;
    private float busSpeed;
    private LocationRequest locationRequest;
    // Declare variables for bus stop distances at the class level
    private float distanceToBusStopLaw;
    private float distanceToBusStopGuest;
    private float distanceToBusStopHindi;
    private float distanceToBusStopScCluster;
    private float distanceToBusStopPostOffice;
    private float distanceToBusStopAdminBlock;
    private float distanceToBusStopGate;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    // Add a class-level variable to store the previous location
    private Location previousLocation;
    private Circle studentLocationCircle;
    public LatLng myLocation;
    float zoomLevel = 16.0f;
    private float previousBearingBus1 = 0.0f;
    private float previousBearingBus2 = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FirebaseApp.initializeApp(this);
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize locationCallback here
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
//                Log.e("location", "student Location");
                if (locationResult.getLastLocation() != null) {
                    // Get the new location
                    Location newLocation = locationResult.getLastLocation();

                    // Check if there is a significant change in location
                    if (isSignificantLocationChange(newLocation)) {
                        // Update the student's location marker
                        LatLng studentLocation = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
                        if (studentMarker != null) {
                            studentMarker.remove();
                        }
                        studentMarker = mMap.addMarker(new MarkerOptions()
                                .position(studentLocation)
                                .title("Student")
                                .snippet("Student's Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.student)));

                        // Move the camera to the new student location
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(studentLocation, zoomLevel);
                        mMap.moveCamera(cameraUpdate);

                        // Remove the previous circle, if it exists
                        if (studentLocationCircle != null) {
                            studentLocationCircle.remove();
                        }

                        // Add a new circle around the student's location
                        studentLocationCircle = mMap.addCircle(new CircleOptions()
                                .center(studentLocation)
                                .radius(30) // 30 meters
                                .strokeColor(Color.DKGRAY)
                                .fillColor(Color.LTGRAY));
                    }
                }
            }

            private boolean isSignificantLocationChange(Location newLocation) {
                if (previousLocation == null) {
                    // If the previous location is null, consider it a significant change
                    return true;
                }

                // Calculate the distance between the new and previous locations
                float distance = newLocation.distanceTo(previousLocation);

                // Set a threshold for what is considered a significant change (adjust as needed)
                float distanceThreshold = 10; // Adjust this value based on your requirements

                // Check if the distance is greater than the threshold
                return distance > distanceThreshold;
            }


        };

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        studentLocationCircle = null;

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult.getLastLocation() != null) {
                    // Update the student's location marker
                    LatLng studentLocation = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    if (studentMarker != null) {
                        studentMarker.remove();
                    }
                    studentMarker = mMap.addMarker(new MarkerOptions()
                            .position(studentLocation)
                            .title("Student")
                            .snippet("Student's Location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.student)));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(studentLocation, zoomLevel);
                    mMap.moveCamera(cameraUpdate);
                    // Remove the previous circle, if it exists
                    if (studentLocationCircle != null) {
                        studentLocationCircle.remove();
                    }

                    // Add a new circle around the student's location
                    studentLocationCircle = googleMap.addCircle(new CircleOptions()
                            .center(studentLocation)
                            .radius(30) // 30 meters
                            .strokeColor(Color.DKGRAY)
                            .fillColor(Color.LTGRAY));
                }
            }
        };

        // Start receiving location updates
        startLocationUpdates();

        // Add a marker in your initial location and move the camera
        myLocation = new LatLng(25.609713, 91.890228);

        mMap.addMarker(new MarkerOptions().position(myLocation).title("Student Dhaba").snippet("Bus Stop Student's Dhaba"));

        // Set up a ValueEventListener to listen for changes in the driver's location
        ValueEventListener locationListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if the "driver" node exists in the database
                String idno = "2001020050";
                String idno2 = "2005020050";
                if (dataSnapshot.child(idno).child("location").exists() && dataSnapshot.child(idno2).child("location").exists()) {

                    trackingbus1(dataSnapshot, googleMap);
                    Log.d("BUS2", "bus");
                    trackingbus2(dataSnapshot, googleMap);
                } else if (dataSnapshot.child(idno2).child("location").exists()) {
                    trackingbus2(dataSnapshot, googleMap);
                } else {
                    // Handle the case where the "driver" node does not exist
                    Log.e("Location", "Driver node does not exist.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
                Log.e("Firebase", "Error: " + databaseError.getMessage());
            }
        };

        // Add the ValueEventListener to the database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("driver");
        databaseReference.addValueEventListener(locationListener);

        // Create a custom InfoWindowAdapter
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                // Return null to use the default InfoWindow frame
                return null;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public View getInfoContents(Marker marker) {
                // Check if the marker is the bus marker
                if (marker.getTitle().equals("Bus Polo") || marker.getTitle().equals("Bus Mawiong")) {
                    // Use the custom layout for the InfoWindow
                    View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);

                    // Set the title, snippet, and other details in the custom InfoWindow
                    TextView title = infoWindow.findViewById(R.id.title);
                    TextView snippet = infoWindow.findViewById(R.id.snippet);
                    TextView distanceTime = infoWindow.findViewById(R.id.distance_time);

                    title.setText(marker.getTitle());

                    // Set the snippet to the nearest bus stop based on your previous calculations
                    String nearestBusStop = "";
                    if (minDistance == distanceToBusStopLaw) {
                        nearestBusStop = "Law Department Bus Stop";
                    } else if (minDistance == distanceToBusStopHindi) {
                        nearestBusStop = "Hindi Department Bus Stop";
                    } else if (minDistance == distanceToBusStopGuest) {
                        nearestBusStop = "New Guest House Bus Stop";
                    } else if (minDistance == distanceToBusStopScCluster) {
                        nearestBusStop = "Science Cluster Bus Stop";
                    } else if (minDistance == distanceToBusStopGate) {
                        nearestBusStop = "Gate 2 Bus Stop";
                    } else if (minDistance == distanceToBusStopPostOffice) {
                        nearestBusStop = "Post Office Bus Stop";
                    } else if (minDistance == distanceToBusStopAdminBlock) {
                        nearestBusStop = "Admin Block Bus Stop";
                    }
                    snippet.setText("Nearest Bus Stop: " + nearestBusStop);

                    String formattedDistance = String.format("%.2f", minDistance);

                    if (busSpeed == 0) {
                        distanceTime.setText("Distance: " + formattedDistance + " m | Estimated Time: BUS NOT MOVING");
                    } else {
                        distanceTime.setText("Distance: " + formattedDistance + " m | Estimated Time: " + (minDistance / busSpeed) + " minutes");
                    }
                    return infoWindow;
                } else {
                    return null;
                }
            }
        });
    }

    // Method to calculate bus speed based on change in location and time
    private float calculateBusSpeed(double currentLatitude, double currentLongitude) {
        float speed = 0.0f;
        if (previousLocation != null) {
            Location currentLocation = new Location("Current Location");
            currentLocation.setLatitude(currentLatitude);
            currentLocation.setLongitude(currentLongitude);

            float distance = previousLocation.distanceTo(currentLocation); // in meters
            long timeDifference = (currentLocation.getTime() - previousLocation.getTime()) / 1000; // in seconds

            if (timeDifference > 0) {
                // Calculate speed in meters per second
                speed = distance / timeDifference;
                // Convert to kilometers per hour (1 m/s = 3.6 km/h)
                speed *= 3.6f;
            }
        }
        // Update the previous location
        previousLocation = new Location("Previous Location");
        previousLocation.setLatitude(currentLatitude);
        previousLocation.setLongitude(currentLongitude);
        return speed;
    }

    // Method to request and display directions
    private void requestDirections(LatLng origin, LatLng destination) {
        // Create a DirectionsApiRequest
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyAL0DtmffBMo-Ol7s7ANvhblcR8-f4_DE8").build(); // Replace with your API key

        DirectionsApiRequest directionsRequest = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .mode(TravelMode.DRIVING); // You can change the travel mode as needed

        // Execute the request asynchronously
        directionsRequest.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                // Handle the directions result here
                if (result.routes != null && result.routes.length > 0) {
                    // Get the first route from the result
                    DirectionsRoute route = result.routes[0];

                    // Extract route information, such as distance and duration
                    String distance = route.legs[0].distance.humanReadable;
                    String duration = route.legs[0].duration.humanReadable;

                    // Draw the route on the map
                    List<com.google.maps.model.LatLng> path = route.overviewPolyline.decodePath();
                    List<LatLng> polyline = new ArrayList<>();
                    for (com.google.maps.model.LatLng latLng : path) {
                        polyline.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    // Create a PolylineOptions to display the route
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(polyline).width(10).color(Color.BLUE);

                    // Add the polyline to the map
                    runOnUiThread(() -> {
                        mMap.addPolyline(polylineOptions);
                    });
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle any errors
                Log.e("Directions", "Error: " + e.getMessage());
            }
        });
    }

    private void requestDirections(LatLng origin, LatLng destination, LatLng waypoint) {
        // Create a DirectionsApiRequest
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyAL0DtmffBMo-Ol7s7ANvhblcR8-f4_DE8").build(); // Replace with your API key

        DirectionsApiRequest directionsRequest = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .waypoints(new com.google.maps.model.LatLng(waypoint.latitude, waypoint.longitude))
                .mode(TravelMode.DRIVING); // You can change the travel mode as needed

        // Execute the request asynchronously
        directionsRequest.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                // Handle the directions result here
                if (result.routes != null && result.routes.length > 0) {
                    // Get the first route from the result
                    DirectionsRoute route = result.routes[0];

                    // Extract route information, such as distance and duration
                    String distance = route.legs[0].distance.humanReadable;
                    String duration = route.legs[0].duration.humanReadable;

                    // Draw the route on the map
                    List<com.google.maps.model.LatLng> path = route.overviewPolyline.decodePath();
                    List<LatLng> polyline = new ArrayList<>();
                    for (com.google.maps.model.LatLng latLng : path) {
                        polyline.add(new LatLng(latLng.lat, latLng.lng));
                    }

                    // Create a PolylineOptions to display the route
                    PolylineOptions polylineOptions = new PolylineOptions().addAll(polyline).width(10).color(Color.BLACK);

                    // Add the polyline to the map
                    runOnUiThread(() -> {
                        mMap.addPolyline(polylineOptions);
                    });
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle any errors
                Log.e("Directions", "Error: " + e.getMessage());
            }
        });
    }

    // Start receiving location updates
    private void startLocationUpdates() {
        if (fusedLocationClient != null) {
            try {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    // Stop receiving location updates
    private void stopLocationUpdates() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void trackingbus2(DataSnapshot dataSnapshot, GoogleMap googleMap) {
        String idno = "2005020050";

        DataSnapshot driverSnapshot = dataSnapshot.child(idno).child("location");
        // Check if the "location" node exists within the "driver" node
        Double latitude = driverSnapshot.child("latitude").getValue(Double.class);
        Double longitude = driverSnapshot.child("longitude").getValue(Double.class);

        if (latitude != null && longitude != null) {
            // Update the map with the latest location
            LatLng driverLocation = new LatLng(latitude, longitude);
            float bearing = calculateBearing(previousLocation, latitude, longitude);

            // Update the driver marker with the new position and rotation
            updateDriverMarker(googleMap, driverLocation, bearing, driverMarker2);

            // Update the previous location and bearing
            previousLocation = new Location("Previous Location") {{
                setLatitude(latitude);
                setLongitude(longitude);
            }};
            previousBearingBus2 = bearing;


            mMap = googleMap;
//            driverMarker2 = googleMap.addMarker(new MarkerOptions()
//                    .position(driverLocation)
//                    .title("Driver")
//                    .snippet("Bus Driver's Location")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bustop)));

            // Declare and initialize bus stop locations
            LatLng busStopLaw = new LatLng(25.610671, 91.891290); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopLaw)
                    .title("Bus Stop")
                    .snippet("Bus Stop Law Department"));

            LatLng busStopGuest = new LatLng(25.610640, 91.893836); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopGuest)
                    .title("Bus Stop")
                    .snippet("Bus Stop New Guest House"));

            LatLng busStopScCluster = new LatLng(25.612615, 91.898084); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopScCluster)
                    .title("Bus Stop")
                    .snippet("Bus Stop Science Cluster"));

            LatLng busStopPostOffice = new LatLng(25.613406, 91.901624); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopPostOffice)
                    .title("Bus Stop")
                    .snippet("Bus Stop Post Office"));

            LatLng busStopAdminBlock = new LatLng(25.609898, 91.900556); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopAdminBlock)
                    .title("Bus Stop")
                    .snippet("Bus Stop Admin Block"));

            LatLng busStopGate = new LatLng(25.608250, 91.898719); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopGate)
                    .title("Bus Stop")
                    .snippet("Bus Stop Gate 2"));

            // Calculate the distance to the nearest bus stop
            float[] results = new float[1];
            Location.distanceBetween(latitude, longitude, busStopLaw.latitude, busStopLaw.longitude, results);
            distanceToBusStopLaw = results[0];

            Location.distanceBetween(latitude, longitude, busStopGuest.latitude, busStopGuest.longitude, results);
            distanceToBusStopGuest = results[0];

            Location.distanceBetween(latitude, longitude, busStopScCluster.latitude, busStopScCluster.longitude, results);
            distanceToBusStopScCluster = results[0];

            Location.distanceBetween(latitude, longitude, busStopPostOffice.latitude, busStopPostOffice.longitude, results);
            distanceToBusStopPostOffice = results[0];

            Location.distanceBetween(latitude, longitude, busStopAdminBlock.latitude, busStopAdminBlock.longitude, results);
            distanceToBusStopAdminBlock = results[0];

            Location.distanceBetween(latitude, longitude, busStopGate.latitude, busStopGate.longitude, results);
            distanceToBusStopGate = results[0];

            // Calculate bus speed based on change in location and time
            float currentSpeed = calculateBusSpeed(latitude, longitude);
            if (currentSpeed > 0) {
                busSpeed = currentSpeed;
            }

            // Find the nearest bus stop
            minDistance = Math.min(Math.min(distanceToBusStopLaw, distanceToBusStopGuest), Math.min(distanceToBusStopScCluster, distanceToBusStopGate));
            String nearestBusStop = "";
            if (minDistance == distanceToBusStopLaw) {
                nearestBusStop = "Law Department Bus Stop";
            } else if (minDistance == distanceToBusStopGuest) {
                nearestBusStop = "New Guest House Bus Stop";
            } else if (minDistance == distanceToBusStopScCluster) {
                nearestBusStop = "Science Cluster Bus Stop";
            } else if (minDistance == distanceToBusStopGate) {
                nearestBusStop = "Gate 2 Bus Stop";
            } else if (minDistance == distanceToBusStopPostOffice) {
                nearestBusStop = "Post Office Bus Stop";
            } else if (minDistance == distanceToBusStopAdminBlock) {
                nearestBusStop = "Admin Block Bus Stop";
            }


            // Request and display directions from driver's location to the nearest bus stop
            LatLng SBIATM = new LatLng(25.610620, 91.901788); // Replace latitude and longitude with actual values
            LatLng TaxiStand = new LatLng(25.609063, 91.900165); // Replace latitude and longitude with actual values

            requestDirections(myLocation, busStopPostOffice, busStopScCluster);
            requestDirections(busStopPostOffice, busStopAdminBlock, SBIATM);
            requestDirections(busStopAdminBlock, busStopGate, TaxiStand);
        } else {
            // Handle the case where latitude or longitude is null
            Log.e("Location", "Latitude or longitude is null.");
        }
    }

    private void trackingbus1(DataSnapshot dataSnapshot, GoogleMap googleMap) {
        String idno = "2001020050";

        DataSnapshot driverSnapshot = dataSnapshot.child(idno).child("location");
        // Check if the "location" node exists within the "driver" node
        Double latitude = driverSnapshot.child("latitude").getValue(Double.class);
        Double longitude = driverSnapshot.child("longitude").getValue(Double.class);

        if (latitude != null && longitude != null) {
            // Update the map with the latest location
            LatLng driverLocation = new LatLng(latitude, longitude);

            float bearing = calculateBearing(previousLocation, latitude, longitude);

            // Update the driver marker with the new position and rotation
            updateDriverMarker(googleMap, driverLocation, bearing, driverMarker1);

            // Update the previous location and bearing
            previousLocation = new Location("Previous Location") {{
                setLatitude(latitude);
                setLongitude(longitude);
            }};
            previousBearingBus1 = bearing;

            mMap = googleMap;
//            driverMarker1 = googleMap.addMarker(new MarkerOptions()
//                    .position(driverLocation)
//                    .title("Driver")
//                    .snippet("Bus Driver's Location")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busblue)));

            // Declare and initialize bus stop locations
            LatLng busStopLaw = new LatLng(25.610671, 91.891290); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopLaw)
                    .title("Bus Stop")
                    .snippet("Bus Stop Law Department"));

            LatLng busStopGuest = new LatLng(25.610640, 91.893836); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopGuest)
                    .title("Bus Stop")
                    .snippet("Bus Stop New Guest House"));

            LatLng busStopHindi = new LatLng(25.609315, 91.895313); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopHindi)
                    .title("Bus Stop")
                    .snippet("Bus Stop Hindi Department"));

            LatLng busStopGate = new LatLng(25.608250, 91.898719); // Replace latitude and longitude with actual values
            mMap.addMarker(new MarkerOptions()
                    .position(busStopGate)
                    .title("Bus Stop")
                    .snippet("Bus Stop Gate 2"));

            // Calculate the distance to the nearest bus stop
            float[] results = new float[1];
            Location.distanceBetween(latitude, longitude, busStopLaw.latitude, busStopLaw.longitude, results);
            distanceToBusStopLaw = results[0];

            Location.distanceBetween(latitude, longitude, busStopGuest.latitude, busStopGuest.longitude, results);
            distanceToBusStopGuest = results[0];

            Location.distanceBetween(latitude, longitude, busStopHindi.latitude, busStopHindi.longitude, results);
            distanceToBusStopHindi = results[0];

            Location.distanceBetween(latitude, longitude, busStopGate.latitude, busStopGate.longitude, results);
            distanceToBusStopGate = results[0];

            // Calculate bus speed based on change in location and time
            float currentSpeed = calculateBusSpeed(latitude, longitude);
            if (currentSpeed > 0) {
                busSpeed = currentSpeed;
            }

            // Find the nearest bus stop
            minDistance = Math.min(Math.min(distanceToBusStopLaw, distanceToBusStopGuest), Math.min(distanceToBusStopHindi, distanceToBusStopGate));
            String nearestBusStop = "";
            if (minDistance == distanceToBusStopLaw) {
                nearestBusStop = "Law Department Bus Stop";
            } else if (minDistance == distanceToBusStopGuest) {
                nearestBusStop = "New Guest House Bus Stop";
            } else if (minDistance == distanceToBusStopHindi) {
                nearestBusStop = "Hindi Department Bus Stop";
            } else if (minDistance == distanceToBusStopGate) {
                nearestBusStop = "Gate 2 Bus Stop";
            }
//            Log.e("Zoom", "Zooming to the bus.");
            requestDirections(myLocation, busStopGate);
        } else {
            // Handle the case where latitude or longitude is null
            Log.e("Location", "Latitude or longitude is null.");
        }
    }

    private void updateDriverMarker(GoogleMap googleMap, LatLng driverLocation, float bearing, Marker driverMarker) {
        if (driverMarker != null) {
            driverMarker.remove();
        }
        mMap = googleMap;

        // Update the appropriate class-level marker based on the driver's location
        if (driverMarker == driverMarker1) {
            driverMarker1 = googleMap.addMarker(new MarkerOptions()
                    .position(driverLocation)
                    .title("Bus Polo")
                    .snippet("Bus Driver's Location")
                    .rotation(bearing)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busblue)));
        } else if (driverMarker == driverMarker2) {
            driverMarker2 = googleMap.addMarker(new MarkerOptions()
                    .position(driverLocation)
                    .title("Bus Mawiong")
                    .snippet("Bus Driver's Location")
                    .rotation(bearing)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bustop)));
        }

        // Other marker-related logic...

        // Move the camera to the new driver's location
        // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(driverLocation, zoomLevel);
        // mMap.moveCamera(cameraUpdate);
    }

    private float calculateBearing(Location fromLocation, double toLatitude, double toLongitude) {
        if (fromLocation != null) {
            Location toLocation = new Location("To Location");
            toLocation.setLatitude(toLatitude);
            toLocation.setLongitude(toLongitude);
            return fromLocation.bearingTo(toLocation);
        }
        return 0.0f;
    }
}