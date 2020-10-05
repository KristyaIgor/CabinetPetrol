package edi.md.petrolcabinet.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import edi.md.petrolcabinet.BaseApp;
import edi.md.petrolcabinet.R;
import edi.md.petrolcabinet.remote.petrolStation.PetrolStation;

public class FragmentMaps extends Fragment {
//    private BottomSheetBehavior bottomSheetBehavior;
//    private View bottomSheet;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            SupportMapFragment mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            LatLng chisnau = new LatLng(47.003670, 28.907089);

            List<PetrolStation> listStation = BaseApp.getAppInstance().getPetrolStations();

            if(listStation != null) {
                for(PetrolStation station: listStation){
                    if(station.getLatitude() != 0 && station.getLongitude() != 0){
                        LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(station.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                    }
                }
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chisnau, 7));
            googleMap.getUiSettings().setCompassEnabled(true);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AskForPermissions();
            }
            googleMap.setMyLocationEnabled(true);

            if (mapView != null && mapView.getView().findViewById(Integer.parseInt("1")) != null) {
                // Get the button view
                ImageView locationButton = ((ImageView) mapView.getView().findViewById(Integer.parseInt("2")));
                // and next place it, on bottom right (as Google Maps app)
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                // position on right bottom
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 75, 230);
                locationButton.setImageResource(R.drawable.ic_location_green);
                locationButton.setBackground(getResources().getDrawable(R.drawable.location_png_green));
//                locationButton.setPadding(15,15,15,15);
                locationButton.setElevation(10);
            }
//            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    updateBottomSheetContent(marker);
//                    return true;
//                }
//            });
//            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                }
//            });
        }
    };

//    private void updateBottomSheetContent(Marker marker) {
//        if(bottomSheetBehavior.isDraggable())
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        TextView name = (TextView) bottomSheet.findViewById(R.id.detail_name);
//        name.setText(marker.getTitle());
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_maps, container, false);
//        bottomSheet = root.findViewById(R.id.bottom_sheet_map);
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//        bottomSheetBehavior.setPeekHeight(400);
//        bottomSheetBehavior.setHideable(true);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void AskForPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        int READ_PHONEpermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (READ_PHONEpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }
}