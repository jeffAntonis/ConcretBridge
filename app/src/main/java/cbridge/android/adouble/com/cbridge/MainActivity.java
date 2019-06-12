package cbridge.android.adouble.com.cbridge;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView nav_bottom;
    private FrameLayout container;

    private ChatFragment chatFragment;
    private ProjectsFragment projectsFragment;
    private PublishFragment publishFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout) findViewById(R.id.container);
        nav_bottom = (BottomNavigationView) findViewById(R.id.bottom_navgation);

        chatFragment = new ChatFragment();
        projectsFragment = new ProjectsFragment();
        publishFragment = new PublishFragment();
        searchFragment = new SearchFragment();
        profileFragment = new ProfileFragment();

        nav_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_account :
                        setFragment(profileFragment);
                        return true;

                    case R.id.nav_project :
                        setFragment(projectsFragment);
                        return true;

                    case R.id.nav_add :
                        setFragment(publishFragment);
                        return true;

                    case R.id.nav_search :
                        setFragment(searchFragment);
                        return true;

                    case R.id.nav_message :
                        setFragment(chatFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });

        nav_bottom.setSelectedItemId(R.id.nav_message);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
