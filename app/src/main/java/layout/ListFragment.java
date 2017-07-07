package layout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rhong.musictest.R;
import com.example.rhong.musictest.adapter.AllSongListAdapter;
import com.example.rhong.musictest.entity.Song;

import java.util.ArrayList;
import java.util.List;

import static com.example.rhong.musictest.util.MusicUtil.selectedImageView;

/**
 * Created by rhong on 2017/7/3.
 */

public class ListFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ListFragment";
    private View view;
    private DrawerLayout mDrawerLayout;
    private ImageView searchImageView, sideBarListIcon, sideBarAlbumIcon, sideBarSignerIcon, sideBarCollectIcon, backImageView, squareList, squareAlbum, squareSigner, squareCollect;
    private LinearLayout listSidebarLayout, albumSidebarLayout, signerSidebarLayout, collectSidebarLayout;
    private TextView listTextView, albumTextView, signerTextView, collectTextView;

    private ListView listView;
    private AllSongListAdapter myAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        Log.d(TAG, "onCreateOptionsMenu: set ok");
//        menu.clear();
//        Log.d(TAG, "onCreateOptionsMenu: menu clear ok");
//        inflater.inflate(R.menu.toolbar, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.back_off:
//                Toast.makeText(getActivity(), "back off", Toast.LENGTH_SHORT).show();
//                break;
//            case android.R.id.home:
//                Log.d(TAG, "onOptionsItemSelected: drawer layout start");
//                mDrawerLayout.openDrawer(GravityCompat.START);
//
//            default:
//                break;
//        }
//        return true;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        Log.d(TAG, "onCreateView: start ok");
        initView();
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
//
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.list_topbar_icon_arrows_1_n);
//        }
//        Log.d(TAG, "onCreate: actionbar home set ok");
        return view;
    }

    private void initView() {
        backImageView = (ImageView) view.findViewById(R.id.button_open_sidebar);
        listView = (ListView) view.findViewById(R.id.list_music);
        List<Song> songList = new ArrayList<>();

        myAdapter = new AllSongListAdapter(songList, getActivity());
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                int i = 1;
                ImageView itemMusic = (ImageView) view.findViewById(R.id.item_list_tag);
                TextView tv_name = (TextView) view.findViewById(R.id.item_list_song_name);
                ImageView itemLike = (ImageView) view.findViewById(R.id.item_list_collected_all_song);
                SeekBar seekbar = (SeekBar) view.findViewById(R.id.item_list_seekBar);
                if (i == 1) {
                    itemLike.setBackgroundResource(R.drawable.list_icon_collect_1);
                    tv_name.setTextSize(26);
                    tv_name.setTextColor(Color.parseColor("#000000"));
                    itemMusic.setBackgroundResource(R.drawable.list_icon_play);
                    seekbar.setVisibility(View.VISIBLE);
                    seekbar.setProgress(50);
                } else {
                    itemLike.setBackgroundResource(R.drawable.list_icon_collect_2);
                    tv_name.setTextSize(21);
                    tv_name.setTextColor(Color.parseColor("#646464"));
                    itemMusic.setBackgroundResource(R.drawable.list_icon_music);
                    seekbar.setVisibility(View.GONE);
                }
            }
        });

        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        searchImageView = view.findViewById(R.id.sidebar_search);
        listSidebarLayout = view.findViewById(R.id.sidebar_list);
        albumSidebarLayout = view.findViewById(R.id.sidebar_album);
        signerSidebarLayout = view.findViewById(R.id.sidebar_signer);
        collectSidebarLayout = view.findViewById(R.id.sidebar_collect);

        sideBarListIcon = view.findViewById(R.id.sidebar_list_icon);
        sideBarAlbumIcon = view.findViewById(R.id.sidebar_album_icon);
        sideBarSignerIcon = view.findViewById(R.id.sidebar_signer_icon);
        sideBarCollectIcon = view.findViewById(R.id.sidebar_collect_icon);
        listTextView = view.findViewById(R.id.sidebar_list_text);
        albumTextView = view.findViewById(R.id.sidebar_album_text);
        signerTextView = view.findViewById(R.id.sidebar_signer_text);
        collectTextView = view.findViewById(R.id.sidebar_collect_text);
        squareList = view.findViewById(R.id.sidebar_square_list);
        squareAlbum = view.findViewById(R.id.sidebar_square_album);
        squareSigner = view.findViewById(R.id.sidebar_square_signer);
        squareCollect = view.findViewById(R.id.sidebar_square_collect);

        backImageView.setOnClickListener(this);
        searchImageView.setOnClickListener(this);
        listSidebarLayout.setOnClickListener(this);
        albumSidebarLayout.setOnClickListener(this);
        signerSidebarLayout.setOnClickListener(this);
        collectSidebarLayout.setOnClickListener(this);

        setSquareVisible(1);

        //设置DrawerLayout禁止手势开侧滑开启菜单，可以选择手势滑动关闭侧滑菜单
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    private void setSquareVisible(int id) {
        switch (id) {
            case 0:
                squareList.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareSigner.setVisibility(View.INVISIBLE);
                squareCollect.setVisibility(View.INVISIBLE);
            case 1:
                squareList.setVisibility(View.VISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareSigner.setVisibility(View.INVISIBLE);
                squareCollect.setVisibility(View.INVISIBLE);
                break;
            case 2:
                squareList.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.VISIBLE);
                squareSigner.setVisibility(View.INVISIBLE);
                squareCollect.setVisibility(View.INVISIBLE);
                break;
            case 3:
                squareSigner.setVisibility(View.VISIBLE);
                squareList.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareCollect.setVisibility(View.INVISIBLE);
                break;
            case 4:
                squareCollect.setVisibility(View.VISIBLE);
                squareList.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareSigner.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sidebar_search:
                selectedImageView(searchImageView, R.drawable.sidebar_icon_search_p, R.drawable.sidebar_icon_search_n);
                break;
            case R.id.sidebar_list:
                setSquareVisible(1);
                break;
            case R.id.sidebar_album:
                setSquareVisible(2);
                break;
            case R.id.sidebar_signer:
                setSquareVisible(3);
                break;
            case R.id.sidebar_collect:
                setSquareVisible(4);
                break;
            case R.id.button_open_sidebar:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }

    private void resumeView() {
        //恢复其余的View

    }
}
