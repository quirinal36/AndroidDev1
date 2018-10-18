package kr.coder.ba.bacoderdevproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import kr.coder.ba.bacoderdevproject.list.PatientListFragment;
import kr.coder.ba.bacoderdevproject.sign.SignupFragment;
import kr.coder.ba.bacoderdevproject.view.MyInfoFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;

    @BindView(R.id.imageView)
    ImageView _profileImageView;
    @BindView(R.id.user_name)
    TextView _userNameView;

    private int stackNum=1;
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences pref = getSharedPreferences(getString(R.string.sharedpreference_name), MODE_PRIVATE);
        int userId = pref.getInt(getString(R.string.user_id), 0);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new PatientListFragment();
        fragmentTransaction.replace(R.id.content_main , fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.d(TAG, "count: "+getSupportFragmentManager().getBackStackEntryCount());
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "count: " + getSupportFragmentManager().getBackStackEntryCount());
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                showFinishDialog();
                return;
            }
            super.onBackPressed();
        }
    }
    private void showFinishDialog(){
        new MaterialDialog.Builder(this)
                .title("종료")
                .content("앱을 종료할까요?")
                .positiveText("확인")
                .negativeText("취소")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_camera) {
            fragment = new PatientListFragment();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            fragment = new MyInfoFragment();
            SharedPreferences pref = getSharedPreferences(getString(R.string.sharedpreference_name), MODE_PRIVATE);
            Bundle args = new Bundle();
            args.putString("param1", pref.getString(getString(R.string.device_phone_num), ""));
            args.putString("param2", pref.getString(getString(R.string.device_uuid), ""));
            fragment.setArguments(args);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            fragment = new SignupFragment();
        }
        if(fragment != null){
            fragmentTransaction.replace(R.id.content_main , fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedScreen(int id){
        Fragment fragment = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
        fragment.onActivityResult(requestCode, resultCode, data);
    }


}
