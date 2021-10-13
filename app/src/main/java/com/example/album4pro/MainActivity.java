package com.example.album4pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resource, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(this, "Show Camera", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_option1:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Toast.makeText(this, "Show option 1", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(this, "Show Default", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);

        }
    }
}