package com.example.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.animatetest.R;
import com.example.fragment.BitmapMeshFragment;
import com.example.fragment.PolygonFragment;
import com.example.fragment.SquareFragment;
import com.example.viewport.PullToZoomListView;

/**
 * 
 * 类描述：主界面
 * 
 * @Package com.example.activity
 * @ClassName: MainActivity
 * @author 尤洋
 * @mail youyang@ucweb.com
 * @date 2015-3-28 下午8:05:35
 */
public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    protected static final int POLYGONFRAGMENT_DEMO = 0x0000;// 20面体
    protected static final int BITMAPMESHFRAGMENT_DEMO1 = 0x0001; // 窗帘效果
    protected static final int BITMAPMESHFRAGMENT_DEMO2 = 0x0002;// 放大镜效果
    protected static final int BITMAPMESHFRAGMENT_DEMO3 = 0x0003;// 点击扭曲效果
    protected static final int BITMAPMESHFRAGMENT_DEMO4 = 0x0004; // 滤镜效果
    protected static final int BITMAPMESHFRAGMENT_DEMO5 = 0x0005; // 三张图片切换
    protected static final int ROTATE_TRANSFLATE_DEMO = 0x0006; // opengl实现的三张图片切换
    private ArrayList<String> drawerList;
    private PullToZoomListView drawerListView;
    private FragmentManager manager;
    private DrawerLayout drawLayout;
    private ImageView runImage;
    TranslateAnimation left, right, up, down;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        intView();
        intData();
        startAnimation();
        Toast.makeText(this, "侧滑或点击菜单可以显示更多哦~", Toast.LENGTH_SHORT).show();
        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.item_filename, drawerList));
        drawerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                drawLayout.closeDrawers();
                switch (position - 1) {
                case ROTATE_TRANSFLATE_DEMO:      // opengl 平移和旋转
                    switchToNextFragment(new SquareFragment());
                    break;
                case POLYGONFRAGMENT_DEMO:      // 20面体
                    switchToNextFragment(new PolygonFragment());
                    break;
                case BITMAPMESHFRAGMENT_DEMO1: // mesh网格形成的窗帘效果
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.FRAGMENT_MESH));
                    break;

                case BITMAPMESHFRAGMENT_DEMO2: // 放大镜效果
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.MAGNIFIER));
                    break;

                case BITMAPMESHFRAGMENT_DEMO3: // 点击扭曲效果
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.TOUCHWRAPVIEW));
                    break;

                case BITMAPMESHFRAGMENT_DEMO4: // 点击变色效果
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.COLORIMAGEVIEW));
                    break;
                case BITMAPMESHFRAGMENT_DEMO5: // 三张图片切换
                    switchToNextFragment(new BitmapMeshFragment(BitmapMeshFragment.THIRDDIMENSIONVIEW));
                    break;
                default:
                    break;
                }
                if (position >= 1)
                    Toast.makeText(MainActivity.this, drawerList.get(position - 1), 0).show();
            }

        });

    }

    /**
     * 方法描述：背景动态切换动画
     * 
     * @author 尤洋
     * @Title: startAnimation
     * @return void
     * @date 2015-3-28 上午12:42:21
     */
    private void startAnimation() {
        right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, -1.5f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
                0f);
        left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.5f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
                -0.1f, Animation.RELATIVE_TO_PARENT, -0.1f);
        up = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
                -0.1f, Animation.RELATIVE_TO_PARENT, 0f);
        down = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.5f,
                Animation.RELATIVE_TO_PARENT, -1.5f, Animation.RELATIVE_TO_PARENT,
                0f, Animation.RELATIVE_TO_PARENT, -0.1f);
        right.setDuration(25000);
        left.setDuration(25000);
        up.setDuration(15000);
        down.setDuration(15000);
        right.setFillAfter(true);
        left.setFillAfter(true);
        down.setFillAfter(true);
        up.setFillAfter(true);
        setAllAnimation(right, runImage, down);
        setAllAnimation(down, runImage, left);
        setAllAnimation(left, runImage, up);
        setAllAnimation(up, runImage, right);
    }

    /**
     * 方法描述：设置4个方向的动画
     * 
     * @author 尤洋
     * @Title: setAllAnimation
     * @param up2
     * @param runImage2
     * @param runImage3
     * @return void
     * @date 2015-3-28 下午8:09:01
     */
    private void setAllAnimation(TranslateAnimation ta, final ImageView runImage2, final TranslateAnimation to
            ) {
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                runImage2.startAnimation(to);
            }
        });
        runImage2.startAnimation(to);
    }

    /**
     * 方法描述：
     * 
     * @author 尤洋
     * @Title: switchToNextFragment
     * @param squareFragment
     * @return void
     * @date 2015-3-25 下午3:36:26
     */
    private void switchToNextFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    /**
     * 方法描述：初始化控件
     * 
     * @author 尤洋
     * @Title: intView
     * @return void
     * @date 2015-3-13 上午10:39:58
     */
    private void intView() {
        drawLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerListView = (PullToZoomListView) findViewById(R.id.left_drawer);
        runImage = (ImageView) findViewById(R.id.run_image);
    }

    /**
     * 方法描述：初始化数据
     * 
     * @author 尤洋
     * @Title: intData
     * @return void
     * @date 2015-3-13 上午10:28:38
     */
    private void intData() {
        drawerList = new ArrayList<String>();
        drawerList.add("20面体");
        drawerList.add("bitmapMesh窗帘效果");
        drawerList.add("magnifier放大镜效果");
        drawerList.add("点击水波纹效果");
        drawerList.add("颜色过滤器效果");
        drawerList.add("三张图片来回切换");
        drawerList.add("opengl平移和旋转");
        drawerList.add("Blur高斯模糊效果");
        drawerList.add("后续效果逐渐添加");
        drawerList.add("后续效果逐渐添加");
        drawerList.add("后续效果逐渐添加");
        drawerList.add("后续效果逐渐添加");

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
     */
    @SuppressLint("NewApi")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_MENU:
            if (drawLayout.isDrawerOpen(GravityCompat.START)) {
                drawLayout.closeDrawers();
            } else {
                drawLayout.openDrawer(GravityCompat.START);
            }

            break;

        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
