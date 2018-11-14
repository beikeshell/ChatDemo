package com.tap4fun.chatdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class NotifyListFragment extends Fragment {
    private static final String TAG = "NotifyListFragment";

    private List<NotifyItem> mNotifyList = new ArrayList<>();
    private NotifyListAdapter mNotifyListAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notify_list, container, false);
        initNotify();

        recyclerView = view.findViewById(R.id.notify_recycler_view);
        recyclerView.setItemAnimator(new FadeInAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mNotifyListAdapter = new NotifyListAdapter(mNotifyList);
        AnimationAdapter adapter = new ScaleInAnimationAdapter(mNotifyListAdapter);
        adapter.setFirstOnly(false);
        adapter.setDuration(500);
        adapter.setInterpolator(new OvershootInterpolator(.5f));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initNotify() {
        mNotifyList.clear();
        for (int i = 0; i < 10; i++) {
            NotifyItem notifyItem = new NotifyItem("四个同学躲在厕所抽烟，刚整完出来迎面就撞上了也要如厕的教导主任，主任闻着满厕所的烟味勃然大怒，立刻就把这四个同学叫到了办公室。四人自然都为自己开脱，纷纷表示去厕所的时候烟味已经如此浓重，绝不是我们所为。主任一眼便已明了，这四人定是来的路上串好了口供，看来想一举拿下是不可能了，待我耍个手段，让这些猢狲收了神通。于是主任便让四人通通到外面站着，一个一个的进来问话。四人在外面嘀嘀咕咕，各自对号入座暂且不表，单说老师打开抽屉，掏出一包亲亲薯条，思绪刹那万千，好似回到那硝烟弥漫的战场，主人轻抚胸膛舒缓情绪，张口叫了第一位同学进来。主任：你抽烟了么？学生甲：老师我怎么会抽烟呢？我从来不抽烟，我对年纪轻轻就不学好这种行为深恶痛绝。主任：嗯，我相信你，但是这也是没办法，毕竟要严抓这一块，同学你受惊了，来，吃根薯条。学生甲：谢谢主任。说罢就从老师手中接过了薯条，习惯性的夹在了食指与中指之间。等反应过来四目相对，学生甲觉得很尴尬，主任面上倒是波澜不惊，淡淡地说：回去一千字检讨，出去吧。\n" +
                    "\n" +
                    "作者：王大猫\n" +
                    "链接：https://www.zhihu.com/question/35703039/answer/65106495\n" +
                    "来源：知乎\n" +
                    "著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。");
            mNotifyList.add(notifyItem);
            NotifyItem notifyItem1 = new NotifyItem("notify" + i * 2);
            mNotifyList.add(notifyItem1);
        }
    }
}
