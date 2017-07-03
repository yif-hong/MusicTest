package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rhong.musictest.R;

/**
 * Created by rhong on 2017/7/3.
 */

public class ListFragment extends Fragment {
    private View view;
    private Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //List中的toolbar
        toolbar.setTitle("Album List");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container);
        return view;
    }

    private void initView() {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

    }
}
