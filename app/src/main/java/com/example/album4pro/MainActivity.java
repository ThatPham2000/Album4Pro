package com.example.album4pro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.example.album4pro.fragments.MyFragmentAdapter;
import com.example.album4pro.fragments.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 menuViewPager2;
    private BottomNavigationView menuBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuViewPager2 = (ViewPager2) findViewById(R.id.view_pager_2);
        menuBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Khởi tạo adapter
        MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(this);
        menuViewPager2.setAdapter(myFragmentAdapter);

        // Set hiệu ứng chuyển trang Zoom Out Page
        menuViewPager2.setPageTransformer(new ZoomOutPageTransformer());

        // Set sự kiện khi LƯỚT trên trang
        menuViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_library).setChecked(true);
                        break;
                    case 1:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_albums).setChecked(true);
                        break;
                    case 2:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_private).setChecked(true);
                        break;
                    case 3:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                    case 4:
                        menuBottomNavigationView.getMenu().findItem(R.id.menu_setting).setChecked(true);
                        break;
                }
            }
        });

        // Set sự kiện khi chọn trong thanh menu
        menuBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_library:
                        menuViewPager2.setCurrentItem(0);
                        break;
                    case R.id.menu_albums:
                        menuViewPager2.setCurrentItem(1);
                        break;
                    case R.id.menu_private:
                        menuViewPager2.setCurrentItem(2);
                        break;
                    case R.id.menu_search:
                        menuViewPager2.setCurrentItem(3);
                        break;
                    case R.id.menu_setting:
                        menuViewPager2.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
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