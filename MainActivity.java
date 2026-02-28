import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            configureWifi();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configureWifi();
            } else {
                Toast.makeText(this, "Location permission is required to use WifiNetworkSuggestion.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void configureWifi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WifiNetworkSuggestion suggestion = new WifiNetworkSuggestion.Builder()
                    .setSsid("ExampleSSID")
                    .setWpa2Passphrase("ExamplePassword")
                    .build();

            List<WifiNetworkSuggestion> suggestionsList = new ArrayList<>();
            suggestionsList.add(suggestion);

            int status = WifiManager.addNetworkSuggestions(suggestionsList);
            if (status == WifiManager.STATUS_SUCCESS) {
                Toast.makeText(this, "Wifi suggestion added successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add wifi suggestion.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        }
    }
}