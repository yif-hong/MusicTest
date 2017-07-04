package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rhong.musictest.R;

/**
 * Created by rhong on 2017/7/3.
 */

public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    private View view;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu: set ok");
        menu.clear();
        Log.d(TAG, "onCreateOptionsMenu: menu clear ok");
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_off:
                Toast.makeText(getActivity(), "back off", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: drawer layout start");
                mDrawerLayout.openDrawer(GravityCompat.START);

            default:
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container);
        Log.d(TAG, "onCreateView: start ok");
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        Log.d(TAG, "onCreate: fvb ok");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.list_topbar_icon_arrows_1_n);
        }
        Log.d(TAG, "onCreate: actionbar home set ok");

        return view;
    }


}
