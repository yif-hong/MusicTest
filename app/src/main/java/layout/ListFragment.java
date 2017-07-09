package layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rhong.musictest.R;
import com.example.rhong.musictest.adapter.AlbumSongListAdapter;
import com.example.rhong.musictest.adapter.AllSongListAdapter;
import com.example.rhong.musictest.entity.Song;
import com.example.rhong.musictest.model.IPlayer;
import com.example.rhong.musictest.model.MusicPlayer;
import com.example.rhong.musictest.presenter.ISearchPresenter;
import com.example.rhong.musictest.presenter.SearchSongsPresenter;
import com.example.rhong.musictest.util.ConstantUtil;
import com.example.rhong.musictest.view.IView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static com.example.rhong.musictest.util.ConstantUtil.ACTION_CLEAR;
import static com.example.rhong.musictest.util.ConstantUtil.ACTION_UPDATE;
import static com.example.rhong.musictest.util.ConstantUtil.INTENT_CURRENTTIME;
import static com.example.rhong.musictest.util.ConstantUtil.INTENT_DURATION;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHALBUM;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHALL;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHFAVOUR;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHSINGER;

/**
 * Created by rhong on 2017/7/3.
 */

public class ListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, IView {
    private static final String TAG = "ListFragment";
    private static ArrayList<Song> songList;
    private View view;
    private DrawerLayout mDrawerLayout;
    private ImageView searchIV, sideBarListIcon, sideBarAlbumIcon, sideBarSignerIcon, sideBarCollectIcon, openSideBarIV, squareList, squareAlbum, squareSigner, squareCollect, listBackIV;
    private LinearLayout listSidebarLayout, albumSidebarLayout, signerSidebarLayout, collectSidebarLayout, drawerLayoutLeft;
    private TextView listTextView, albumTextView, signerTextView, collectTextView;
    private Context context;
    private ListView listView;
    private AllSongListAdapter myAdapter;
    private ISearchPresenter searchPresenter;
    private IPlayer mIPlayer;
    private BroadcastReceiver receiver2;
    private int index, prePosition;
    private EditText editText;
    private int searchMode = ConstantUtil.SEARCHALL;
    private int returnSearch;
    private int returnState;
    private int returnSubSearch;
    private int subIndex;
    private ArrayList<Song> subList = new ArrayList<>();
    private String searchString;
    private AlbumSongListAdapter albumSongListAdapter;
    private int isGetSubList;

    public ListFragment() {
        searchPresenter = new SearchSongsPresenter(this);
    }

    public static synchronized ArrayList<Song> getCollected() {
        ArrayList<Song> favList = new ArrayList<>();
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "collect.txt");
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Song song = null;
            while ((song = (Song) ois.readObject()) != null) {
                favList.add(song);
            }
            fis.close();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < songList.size(); i++) {
            for (int j = 0; j < favList.size(); j++) {
                if ((favList.get(j).getId()) == songList.get(i).getId()) {
                    Id3Fragment.collectMap.put(i, true);
                }
            }
        }
        return favList;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        Log.d(TAG, "onCreateView: start ok");
        context = getActivity().getApplicationContext();
        initView();

        mIPlayer = MusicPlayer.getMusicPlayer(context);
        searchPresenter.getSongs(context);

        setListener();
        receiver2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ACTION_UPDATE)) {
                    int currentTime = intent.getIntExtra(INTENT_CURRENTTIME, 0);
                    int duration = intent.getIntExtra(INTENT_DURATION, 0);
                    int progress = currentTime * 1000 / duration;
                    myAdapter.setListProgress(progress);
                } else if (action.equals(ACTION_CLEAR)) {


                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATE);
        intentFilter.addAction(ACTION_CLEAR);
        getActivity().registerReceiver(receiver2, intentFilter);

        return view;
    }

    private void initView() {
        openSideBarIV = (ImageView) view.findViewById(R.id.button_open_sidebar);
        listView = (ListView) view.findViewById(R.id.list_music);
        songList = new ArrayList<>();

        myAdapter = new AllSongListAdapter(context, songList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);



        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        searchIV = view.findViewById(R.id.sidebar_search);
        listSidebarLayout = view.findViewById(R.id.sidebar_all);
        albumSidebarLayout = view.findViewById(R.id.sidebar_album);
        signerSidebarLayout = view.findViewById(R.id.sidebar_signer);
        collectSidebarLayout = view.findViewById(R.id.sidebar_collect);
        drawerLayoutLeft = view.findViewById(R.id.drawer_layout_left);


        editText = view.findViewById(R.id.sidebar_edit_text);
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
        listBackIV = view.findViewById(R.id.list_back);

        openSideBarIV.setOnClickListener(this);
        searchIV.setOnClickListener(this);
        listSidebarLayout.setOnClickListener(this);
        albumSidebarLayout.setOnClickListener(this);
        signerSidebarLayout.setOnClickListener(this);
        collectSidebarLayout.setOnClickListener(this);
        listBackIV.setOnClickListener(this);

        setSquareVisible(1);

    }

    private void setListener() {
        //TODO：设置滑动抽屉菜单动作
        drawerLayoutLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float eventX;
                float eventY;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        eventX = motionEvent.getX();
                        eventY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                    default:
                        break;
                }
                return false;
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
                mDrawerLayout.closeDrawers();
                searchString = editText.getText().toString().trim();
                if (searchMode == SEARCHALL) {
                    searchAll(searchString);
                } else if (searchMode == SEARCHALBUM) {
                    searchAlbum(searchString);
                } else if (searchMode == SEARCHSINGER) {
                    searchSinger(searchString);
                } else if (searchMode == SEARCHFAVOUR) {
                    searchFavourite(searchString);
                }
                break;
            case R.id.sidebar_all:
                showAllSongs();
//                onResume();
                break;
            case R.id.sidebar_album:
                showAlbumList();
                break;
            case R.id.sidebar_signer:
                showSingerList();
                break;
            case R.id.sidebar_collect:
                showFavList();
                break;
            case R.id.button_open_sidebar:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.list_back:
                //TODO
                if (searchMode == SEARCHALBUM) {
                    if (returnState == ConstantUtil.ISSHOW_ALBUM_MUSIC) {
                        showAlbumList();
                    }
                } else if (searchMode == SEARCHSINGER) {
                    if (returnState == ConstantUtil.ISSHOW_SINGER_MUSIC) {
                        showSingerList();
                    }
                }

                if (searchMode == ConstantUtil.SEARCHALL) {
                    if (returnSearch == ConstantUtil.ENTER_SEARCH_ALL) {
                        showAllSongs();
                    }
                } else if (searchMode == ConstantUtil.SEARCHFAVOUR) {
                    if (returnSearch == ConstantUtil.ENTER_SEARCH_FAVOUR) {
                        showFavList();
                    }
                } else if (searchMode == ConstantUtil.SEARCHALBUM) {
                    if (returnSearch == ConstantUtil.ENTER_SEARCH_ALBUM) {
                        if (returnSubSearch == ConstantUtil.ENTER_SUBSEARCH_ALBUM_STATE) {
                            searchAlbum(searchString);
                        }
                        showAlbumList();

                    }
                } else if (searchMode == ConstantUtil.SEARCHSINGER) {
                    if (returnSearch == ConstantUtil.ENTER_SEARCH_SINGER) {
                        if (returnSubSearch == ConstantUtil.ENTER_SUBSEARCH_SINGER_STATE) {
                            searchSinger(searchString);
                        }
                        showSingerList();
                    }
                }
            default:
                break;
        }
    }

    private void searchAll(String s) {

    }

    private void searchAlbum(String s) {

    }

    private void searchSinger(String s) {

    }

    private void searchFavourite(String s) {

    }

    private void showAllSongs() {

    }

    private void showAlbumList() {

    }

    private void showSingerList() {

    }

    private void showFavList() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mIPlayer.callPlay(i);
        if (index != i) {
            AllSongListAdapter.checked.put(index, false);
        }
        AllSongListAdapter.checked.put(prePosition, false);
        AllSongListAdapter.checked.put(i, false);
        prePosition = i;
        myAdapter.setListProgress(0);

        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        index = mIPlayer.callMusicIndex();
        for (int i = 0; i < songList.size(); i++) {
            AllSongListAdapter.checked.put(i, false);
        }
        AllSongListAdapter.checked.put(index, true);
        myAdapter.notifyDataSetChanged();

        getCollected();

        if (isGetSubList == ConstantUtil.IS_GET_FILE_LIST) {
            Message msg = mIPlayer.callMusicSub();
            subIndex = msg.arg1;
            subList = (ArrayList<Song>) msg.obj;
            for (int i = 0; i < subList.size(); i++) {
                AlbumSongListAdapter.subchecked.put(i, false);
            }
            AlbumSongListAdapter.subchecked.put(index, true);

        }

    }

    @Override
    public void showData(ArrayList<Song> songs) {

    }

    @Override
    public void onFalse() {

    }
}
