package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rhong.musictest.R;

/**
 * Created by rhong on 2017/7/4.
 */

public class SetFragment extends Fragment implements View.OnClickListener {
    private View view;
    private DrawerLayout mDrawerLayout;
    private ImageView searchImageView;
    private LinearLayout listSidebarLayout, albumSidebarLayout, signerSidebarLayout, collectSidebarLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set, container, false);
        initView();
        return view;
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        searchImageView = (ImageView) view.findViewById(R.id.search_image_view);
        listSidebarLayout = (LinearLayout) view.findViewById(R.id.sidebar_list);
        albumSidebarLayout = (LinearLayout) view.findViewById(R.id.sidebar_album);
        signerSidebarLayout = (LinearLayout) view.findViewById(R.id.sidebar_signer);
        collectSidebarLayout = (LinearLayout) view.findViewById(R.id.sidebar_collect);

        Button button = (Button) view.findViewById(R.id.button_test);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_image_view:
                Toast.makeText(getContext(), "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sidebar_list:
                Toast.makeText(getContext(), "list", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sidebar_album:
                Toast.makeText(getContext(), "album", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sidebar_signer:
                Toast.makeText(getContext(), "signer", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sidebar_collect:
                Toast.makeText(getContext(), "collect", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
