package layout;

import android.app.Activity;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rhong.musictest.R;
import com.example.rhong.musictest.adapter.AlbumAdapter;
import com.example.rhong.musictest.adapter.AlbumSongListAdapter;
import com.example.rhong.musictest.adapter.AllSongListAdapter;
import com.example.rhong.musictest.entity.Song;
import com.example.rhong.musictest.model.IPlayer;
import com.example.rhong.musictest.model.MusicPlayer;
import com.example.rhong.musictest.model.OnSlideBarListener;
import com.example.rhong.musictest.presenter.ISearchPresenter;
import com.example.rhong.musictest.presenter.SearchSongsPresenter;
import com.example.rhong.musictest.util.ConstantUtil;
import com.example.rhong.musictest.util.ToastUtil;
import com.example.rhong.musictest.view.IView;
import com.yinglan.keyboard.HideUtil;

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
import static com.example.rhong.musictest.util.ConstantUtil.LIST_ALBUM;
import static com.example.rhong.musictest.util.ConstantUtil.LIST_ALL;
import static com.example.rhong.musictest.util.ConstantUtil.LIST_FAVOURITE;
import static com.example.rhong.musictest.util.ConstantUtil.LIST_SINGER;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_ALL;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_FOLDER;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_SINGLE;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHALBUM;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHALL;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHFAVOUR;
import static com.example.rhong.musictest.util.ConstantUtil.SEARCHSINGER;
import static layout.Id3Fragment.playMode;

/**
 * Created by rhong on 2017/7/3.
 */

public class ListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, IView {
    private static final String TAG = "ListFragment";
    private static ArrayList<Song> songList;
    private View view;
    private DrawerLayout mDrawerLayout;
    private ImageView searchIV, sideBarListIcon, sideBarAlbumIcon, sideBarSignerIcon, sideBarCollectIcon, openSideBarIV, squareAllSong, squareAlbum, squareSinger, squareFav, listBackIV;
    private LinearLayout allSongSidebarLayout, albumSidebarLayout, singerSideBarLayout, favSidebarLayout, drawerLayoutLeft;
    private TextView listTextView, albumTextView, signerTextView, collectTextView, listTitleTextView;
    private Context context;
    private ListView listView;
    private AllSongListAdapter myAdapter;
    private ISearchPresenter searchPresenter;
    private IPlayer mIPlayer;
    private BroadcastReceiver receiver2;
    private int index, prePosition;
    private EditText searchEditText;
    private int searchMode = ConstantUtil.SEARCHALL;
    private int returnSearch;
    private int returnState;
    private int returnSubSearch;
    private int subIndex;
    private ArrayList<Song> subList = new ArrayList<>();
    private String searchString;
    private AlbumSongListAdapter albumSongListAdapter;
    private int isGetSubList;
    private AlbumAdapter albumAdapter;
    private ArrayList<String> albumList = new ArrayList<>();
    private ArrayList<String> singerList = new ArrayList<>();
    private ArrayList<Song> favList = new ArrayList<>();
    private boolean isOpenDrawerLayout = false;
    private OnSlideBarListener onSlideBarListener;
    private boolean isPreparedFragment = false;
    private int listPosition;

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

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof OnSlideBarListener) {
            onSlideBarListener = (OnSlideBarListener) activity;
        }
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUserVisibleHint(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        Log.d(TAG, "onCreateView: start ok");
        context = getActivity().getApplicationContext();
        initView();
        listPosition = LIST_ALL;

        mIPlayer = MusicPlayer.getMusicPlayer(context);
        searchPresenter.getSongs(context);

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


        allSongSidebarLayout.setSelected(true);
        albumSidebarLayout.setSelected(false);
        singerSideBarLayout.setSelected(false);
        favSidebarLayout.setSelected(false);

        squareAllSong.setVisibility(View.VISIBLE);
        squareAlbum.setVisibility(View.INVISIBLE);
        squareSinger.setVisibility(View.INVISIBLE);
        squareFav.setVisibility(View.INVISIBLE);

        HideUtil.init(getActivity());

        isPreparedFragment = true;

        return view;
    }

    private void initView() {
        openSideBarIV = view.findViewById(R.id.button_open_sidebar);
        listView = view.findViewById(R.id.list_music);
        songList = new ArrayList<>();

        myAdapter = new AllSongListAdapter(getActivity(), songList);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(this);

        listTitleTextView = view.findViewById(R.id.item_list_title);
        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        searchIV = view.findViewById(R.id.sidebar_search);
        allSongSidebarLayout = view.findViewById(R.id.sidebar_all);
        albumSidebarLayout = view.findViewById(R.id.sidebar_album);
        singerSideBarLayout = view.findViewById(R.id.sidebar_singer);
        favSidebarLayout = view.findViewById(R.id.sidebar_collect);
        drawerLayoutLeft = view.findViewById(R.id.drawer_layout_left);


        searchEditText = view.findViewById(R.id.sidebar_edit_text);
        sideBarListIcon = view.findViewById(R.id.sidebar_list_icon);
        sideBarAlbumIcon = view.findViewById(R.id.sidebar_album_icon);
        sideBarSignerIcon = view.findViewById(R.id.sidebar_singer_icon);
        sideBarCollectIcon = view.findViewById(R.id.sidebar_collect_icon);
        listTextView = view.findViewById(R.id.sidebar_list_text);
        albumTextView = view.findViewById(R.id.sidebar_album_text);
        signerTextView = view.findViewById(R.id.sidebar_signer_text);
        collectTextView = view.findViewById(R.id.sidebar_collect_text);
        squareAllSong = view.findViewById(R.id.sidebar_square_list);
        squareAlbum = view.findViewById(R.id.sidebar_square_album);
        squareSinger = view.findViewById(R.id.sidebar_square_singer);
        squareFav = view.findViewById(R.id.sidebar_square_collect);
        listBackIV = view.findViewById(R.id.list_back);

        openSideBarIV.setOnClickListener(this);
        searchIV.setOnClickListener(this);
        allSongSidebarLayout.setOnClickListener(this);
        albumSidebarLayout.setOnClickListener(this);
        singerSideBarLayout.setOnClickListener(this);
        favSidebarLayout.setOnClickListener(this);
        listBackIV.setOnClickListener(this);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                isOpenDrawerLayout = true;
                onSlideBarListener.isSlideBarOpen(isOpenDrawerLayout);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                isOpenDrawerLayout = false;
                onSlideBarListener.isSlideBarOpen(isOpenDrawerLayout);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        setSquareVisible(1);

    }

    private void setSquareVisible(int id) {
        switch (id) {
            case 0:
                squareAllSong.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareSinger.setVisibility(View.INVISIBLE);
                squareFav.setVisibility(View.INVISIBLE);
            case 1:
                squareAllSong.setVisibility(View.VISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareSinger.setVisibility(View.INVISIBLE);
                squareFav.setVisibility(View.INVISIBLE);
                break;
            case 2:
                squareAllSong.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.VISIBLE);
                squareSinger.setVisibility(View.INVISIBLE);
                squareFav.setVisibility(View.INVISIBLE);
                break;
            case 3:
                squareSinger.setVisibility(View.VISIBLE);
                squareAllSong.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareFav.setVisibility(View.INVISIBLE);
                break;
            case 4:
                squareFav.setVisibility(View.VISIBLE);
                squareAllSong.setVisibility(View.INVISIBLE);
                squareAlbum.setVisibility(View.INVISIBLE);
                squareSinger.setVisibility(View.INVISIBLE);
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
                searchString = searchEditText.getText().toString().trim();
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
                onResume();
                break;
            case R.id.sidebar_album:
                showAlbumList();
                break;
            case R.id.sidebar_singer:
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
                if (searchMode == ConstantUtil.SEARCHALBUM) {
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
        returnSearch = ConstantUtil.ENTER_SEARCH_ALL;
        final ArrayList<Song> searchAllSongList = new ArrayList<>();
        for (Song song : songList) {
            if (song.getName().contains(s)) {
                searchAllSongList.add(song);
            }
        }
        if (searchAllSongList.size() == 0) {
            ToastUtil.showToast(context, "没有搜索到此歌曲");
            return;
        }
        albumSongListAdapter = new AlbumSongListAdapter(context, searchAllSongList);
        listView.setAdapter(albumSongListAdapter);
        albumSongListAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mIPlayer.callPlaySong(searchAllSongList, i);
            }
        });
    }

    private void searchAlbum(String s) {
        returnSearch = ConstantUtil.ENTER_SEARCH_ALBUM;
        final ArrayList<String> searchAlbumList = new ArrayList<>();
        for (String album : albumList) {
            if (album.contains(s)) {
                searchAlbumList.add(album);
            }
        }
        if (searchAlbumList.size() == 0) {
            ToastUtil.showToast(context, "没有搜索到此专辑");
            return;
        }
        albumAdapter = new AlbumAdapter(context, searchAlbumList);
        listView.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                returnSubSearch = ConstantUtil.ENTER_SUBSEARCH_ALBUM_STATE;
                getAlbumSongsByAlbum(searchAlbumList.get(i));
            }
        });
    }

    private void getAlbumSongsByAlbum(String album) {
        final ArrayList<Song> albumSongs = new ArrayList<>();
        for (Song song : songList) {
            if (song.getAlbum().contains(album)) {
                albumSongs.add(song);
            }
        }
        albumSongListAdapter = new AlbumSongListAdapter(context, albumSongs);
        listView.setAdapter(albumSongListAdapter);
        albumSongListAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int preIndex;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mIPlayer.callPlaySong(albumSongs, i);
                for (int j = 0; j < albumSongs.size(); j++) {
                    AlbumSongListAdapter.subChecked.put(j, false);
                }

                AlbumSongListAdapter.subChecked.put(preIndex, false);
                AlbumSongListAdapter.subChecked.put(i, true);
                preIndex = i;
                albumSongListAdapter.notifyDataSetChanged();
                isGetSubList = ConstantUtil.IS_GET_FILE_LIST;

            }
        });
    }

    private void searchSinger(final String s) {
        returnSearch = ConstantUtil.ENTER_SEARCH_SINGER;
        final ArrayList<String> searchSingerList = new ArrayList<>();
        for (String singer : singerList) {
            if (singer.contains(s)) {
                searchSingerList.add(singer);
            }
        }
        if (searchSingerList.size() == 0) {
            ToastUtil.showToast(context, "没有搜索到此歌手");
            return;
        }
        albumAdapter = new AlbumAdapter(context, searchSingerList);
        listView.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                returnSubSearch = ConstantUtil.ENTER_SUBSEARCH_SINGER_STATE;
                getSingerSongsBySinger(searchSingerList.get(i));
            }
        });
    }

    private void getSingerSongsBySinger(String singer) {
        final ArrayList<Song> singerSongs = new ArrayList<>();
        for (Song song : songList) {
            if (song.getArtist().equals(singer)) {
                singerSongs.add(song);
            }
        }
        albumSongListAdapter = new AlbumSongListAdapter(context, singerSongs);
        listView.setAdapter(albumSongListAdapter);
        albumSongListAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int preIndex;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mIPlayer.callPlaySong(singerSongs, i);
                for (int j = 0; j < songList.size(); j++) {
                    AlbumSongListAdapter.subChecked.put(j, false);
                }
                AlbumSongListAdapter.subChecked.put(preIndex, false);
                AlbumSongListAdapter.subChecked.put(i, true);
                preIndex = i;
                albumSongListAdapter.notifyDataSetChanged();
                isGetSubList = ConstantUtil.IS_GET_FILE_LIST;

            }
        });
    }

    private void searchFavourite(String s) {
        returnSearch = ConstantUtil.ENTER_SEARCH_FAVOUR;
        final ArrayList<Song> searchFavList = new ArrayList<>();
        for (Song song : favList) {
            if (song.getName().contains(s)) {
                searchFavList.add(song);
            }
        }
        if (searchFavList.size() == 0) {
            ToastUtil.showToast(context, "没有搜索到此喜欢歌曲");
            return;
        }
        albumSongListAdapter = new AlbumSongListAdapter(context, searchFavList);
        listView.setAdapter(albumAdapter);
        albumSongListAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int preIndex;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mIPlayer.callPlaySong(favList, i);
                AlbumSongListAdapter.subChecked.put(preIndex, false);
                AlbumSongListAdapter.subChecked.put(i, true);
                preIndex = i;
                albumSongListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showAllSongs() {

        searchMode = ConstantUtil.SEARCHALL;
        listPosition = LIST_ALL;

        searchEditText.setHint("请输入搜索歌曲名");
        searchEditText.setText("");
        listTitleTextView.setText("ALL");

//        设置选中状态
        allSongSidebarLayout.setSelected(true);
        albumSidebarLayout.setSelected(false);
        singerSideBarLayout.setSelected(false);
        favSidebarLayout.setSelected(false);

        squareAllSong.setVisibility(View.VISIBLE);
        squareAlbum.setVisibility(View.INVISIBLE);
        squareSinger.setVisibility(View.INVISIBLE);
        squareFav.setVisibility(View.INVISIBLE);

        mDrawerLayout.closeDrawers();

        myAdapter = new AllSongListAdapter(getActivity(), songList);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (index != i) {
                    AllSongListAdapter.checked.put(index, false);
                }
                mIPlayer.callPlay(i);
                AllSongListAdapter.checked.put(prePosition, false);
                AllSongListAdapter.checked.put(i, true);
                prePosition = i;
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showAlbumList() {
        searchMode = ConstantUtil.SEARCHALBUM;
        listPosition = LIST_ALBUM;

        searchEditText.setHint("请输入搜索专辑");
        searchEditText.setText("");
        listTitleTextView.setText("Album List");

        //        设置选中状态
        allSongSidebarLayout.setSelected(false);
        albumSidebarLayout.setSelected(true);
        singerSideBarLayout.setSelected(false);
        favSidebarLayout.setSelected(false);

        squareAllSong.setVisibility(View.INVISIBLE);
        squareAlbum.setVisibility(View.VISIBLE);
        squareSinger.setVisibility(View.INVISIBLE);
        squareFav.setVisibility(View.INVISIBLE);

        mDrawerLayout.closeDrawers();

        albumList = getAlbumList();
        albumAdapter = new AlbumAdapter(context, albumList);
        listView.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String album = albumList.get(i);
                returnState = ConstantUtil.ISSHOW_ALBUM_MUSIC;
                getAlbumSongsByAlbum(album);
            }
        });

    }

    public ArrayList<String> getAlbumList() {
        for (Song song : songList) {
            albumList.add(song.getAlbum());
        }
        for (int i = 0; i < albumList.size() - 1; i++) {
            for (int j = albumList.size() - 1; j > i; j--) {
                if (albumList.get(i).equals(albumList.get(j))) {
                    albumList.remove(j);
                }
            }
        }
        return albumList;
    }

    private void showSingerList() {
        searchMode = ConstantUtil.SEARCHSINGER;
        listPosition = LIST_SINGER;

        searchEditText.setHint("请输入搜索歌手");
        searchEditText.setText("");
        listTitleTextView.setText("Singer List");

        //        设置选中状态
        allSongSidebarLayout.setSelected(false);
        albumSidebarLayout.setSelected(false);
        singerSideBarLayout.setSelected(true);
        favSidebarLayout.setSelected(false);

        squareAllSong.setVisibility(View.INVISIBLE);
        squareAlbum.setVisibility(View.INVISIBLE);
        squareSinger.setVisibility(View.VISIBLE);
        squareFav.setVisibility(View.INVISIBLE);

        mDrawerLayout.closeDrawers();

        singerList = getSingerList();
        albumAdapter = new AlbumAdapter(context, singerList);
        listView.setAdapter(albumAdapter);
        albumAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String singer = singerList.get(i);
                returnState = ConstantUtil.ISSHOW_SINGER_MUSIC;
                getSingerSongsBySinger(singer);
            }
        });
    }

    private ArrayList<String> getSingerList() {
        for (Song song : songList) {
            singerList.add(song.getArtist());
        }
        for (int i = 0; i < singerList.size() - 1; i++) {
            for (int j = singerList.size() - 1; j > i; j--) {
                if (singerList.get(i).equals(singerList.get(j))) {
                    singerList.remove(j);
                }
            }
        }
        return singerList;
    }

    private void showFavList() {
        searchMode = ConstantUtil.ENTER_SEARCH_FAVOUR;

        listPosition = LIST_FAVOURITE;


        searchEditText.setHint("请输入搜索歌曲");
        searchEditText.setText("");
        listTitleTextView.setText("Favourite List");

        //        设置选中状态
        allSongSidebarLayout.setSelected(false);
        albumSidebarLayout.setSelected(false);
        singerSideBarLayout.setSelected(false);
        favSidebarLayout.setSelected(true);

        squareAllSong.setVisibility(View.INVISIBLE);
        squareAlbum.setVisibility(View.INVISIBLE);
        squareSinger.setVisibility(View.INVISIBLE);
        squareFav.setVisibility(View.VISIBLE);

        mDrawerLayout.closeDrawers();

        favList = getCollected();

        albumSongListAdapter = new AlbumSongListAdapter(context, favList);
        listView.setAdapter(albumSongListAdapter);
        albumSongListAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int preIndex;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mIPlayer.callPlaySong(favList, i);
                if (subIndex != i) {
                    AlbumSongListAdapter.subChecked.put(preIndex, false);
                    AlbumSongListAdapter.subChecked.put(subIndex, false);
                    AlbumSongListAdapter.subChecked.put(i, true);
                } else {
                    AlbumSongListAdapter.subChecked.put(subIndex, true);
                    AlbumSongListAdapter.subChecked.put(preIndex, false);
                }
                preIndex = i;
                albumSongListAdapter.notifyDataSetChanged();

                if (playMode != PLAY_SINGLE) {
                    playMode = PLAY_FOLDER;
                }


                isGetSubList = ConstantUtil.IS_GET_FILE_LIST;
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        mIPlayer.callPlay(i);
        if (index != i) {
            AllSongListAdapter.checked.put(index, false);
        }
        AllSongListAdapter.checked.put(prePosition, false);
        AllSongListAdapter.checked.put(i, true);
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
                AlbumSongListAdapter.subChecked.put(i, false);
            }
            AlbumSongListAdapter.subChecked.put(subIndex, true);
            albumSongListAdapter.notifyDataSetChanged();

        }
        Log.d(TAG, "onResume: over");
    }

    @Override
    public void showData(ArrayList<Song> songs) {
        songList.clear();
        songList.addAll(songs);
        mIPlayer.setData(songs);
        myAdapter = new AllSongListAdapter(getActivity(), songList);
        listView.setAdapter(myAdapter);
        Log.d(TAG, "showData: song list size---------->" + songList.size());
    }

    @Override
    public void onFalse() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isPreparedFragment && isVisibleToUser) {
            if (playMode == PLAY_ALL) {
                showAllSongs();
                return;
            }
            switch (listPosition) {
                case LIST_ALL:
                    showAllSongs();
                    break;
                case LIST_ALBUM:
                    showAlbumList();
                    break;
                case LIST_SINGER:
                    showSingerList();
                    break;
                case LIST_FAVOURITE:
                    showFavList();
                    break;
                default:
                    break;
            }
        }
    }
}
