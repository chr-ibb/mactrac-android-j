package chribb.mactrac;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import chribb.mactrac.ui.add.AddViewModel;


public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private AddViewModel addViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_day, R.id.nav_week, R.id.nav_month)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        addViewModel =  new ViewModelProvider(this).get(AddViewModel.class);
        addViewModel.loadFavoriteTrie();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_select_today:
//                return false;
//            case R.id.action_search_day:
//                return false;
//            case R.id.action_edit_macros:
//                return false;
//            case R.id.action_delete_all:
//                return false;
//            case R.id.action_test_10000:
//                return false;
//            case R.id.action_test_today:
//                return false;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}

//TODO notes:
// Using Room to store data on the device is really limiting the way I approach the code.
// I'm not using the data structures I want. I'm not saving a table of all the days with macros
// and linking those days to tables with all of the macros for that day...
// etc. Thinking about it though, you always have to save the data to some database and then load
// it back to your Linked List or Trie or whatever you want it to be...
// Still my current philosophy, being my first app, is to get it working first.
