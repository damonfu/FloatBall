package com.android.fjg.ball;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by fujianguo on 16-12-21.
 */
public class TouchPanelView extends LinearLayout implements View.OnClickListener {

    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    private IPanelViewClick mIPanelViewClick;
    private int panelSize;

    public TouchPanelView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.touch_panel_view, null);
        view.findViewById(R.id.clickRecent).setOnClickListener(this);
        view.findViewById(R.id.clickBack).setOnClickListener(this);
        view.findViewById(R.id.clickHome).setOnClickListener(this);
        view.findViewById(R.id.clickMenu).setOnClickListener(this);
        view.findViewById(R.id.clickLauncher).setOnClickListener(this);
        wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        panelSize = getContext().getResources().getDimensionPixelSize(R.dimen.panel_size);
        wmParams = new WindowManager.LayoutParams();
        //设置你要添加控件的类型，TYPE_ALERT需要申明权限，Toast不需要，在某些定制系统中会禁止悬浮框显示，所以最后用TYPE_TOAST
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        //设置控件在坐标计算规则，相当于屏幕左上角
        wmParams.gravity = GravityCompat.START | Gravity.TOP;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.width = panelSize;
        wmParams.height = panelSize;
        wmParams.x = screenWidth / 2 - panelSize / 2;
        wmParams.y = (screenHeight - 25) / 2 - panelSize / 2;
        wmParams.windowAnimations = R.style.PanelAnimalStyle;
        if (view != null) {
            removeAllViews();
            addView(view);
        }
    }

    public void setIPanelViewClick(IPanelViewClick panelViewClick) {
        this.mIPanelViewClick = panelViewClick;
    }

    public void changeXYParams() {
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        wmParams.x = screenWidth / 2 - panelSize / 2;
        wmParams.y = (screenHeight - 25) / 2 - panelSize / 2;
    }

    /**
     * 添加至窗口
     * @return
     */
    public  boolean addToWindow(){
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!isAttachedToWindow()) {
                    wm.addView(this,wmParams);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() == null) {
                        wm.addView(this,wmParams);
                    }
                    return true;
                } catch (Exception e) {
                    return  false;
                }
            }


        } else {
            return false;
        }
    }

    /**
     * 从窗口移除
     * @return
     */
    public boolean removeFromWindow() {
        if (wm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (isAttachedToWindow()) {
                    wm.removeViewImmediate(this);
                    return true;
                } else {
                    return false;
                }
            } else {
                try {
                    if (getParent() != null) {
                        wm.removeViewImmediate(this);
                    }
                    return true;
                } catch (Exception e) {
                    return  false;
                }
            }


        } else {
            return false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (mIPanelViewClick != null) {
                mIPanelViewClick.clickItem(IPanelViewClick.OUTSIDE);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clickRecent:
                if (mIPanelViewClick != null) {
                    if (mIPanelViewClick != null){
                        mIPanelViewClick.clickItem(IPanelViewClick.RECENT);
                    }
                }
                break;
            case R.id.clickBack:
                if (mIPanelViewClick != null) {
                    if (mIPanelViewClick != null){
                        mIPanelViewClick.clickItem(IPanelViewClick.BACK);
                    }
                }
                break;
            case R.id.clickHome:
                if (mIPanelViewClick != null) {
                    if (mIPanelViewClick != null){
                        mIPanelViewClick.clickItem(IPanelViewClick.HOME);
                    }
                }
                break;
            case R.id.clickMenu:
                if (mIPanelViewClick != null) {
                    if (mIPanelViewClick != null){
                        mIPanelViewClick.clickItem(IPanelViewClick.MENU);
                    }
                }
                break;
            case R.id.clickLauncher:
                if (mIPanelViewClick != null) {
                    if (mIPanelViewClick != null){
                        mIPanelViewClick.clickItem(IPanelViewClick.LAUCHER);
                    }
                }
                break;
        }
    }

    public interface IPanelViewClick {
        int BACK = 0;
        int HOME = 1;
        int LAUCHER = 2;
        int MENU = 3;
        int RECENT = 4;
        int OUTSIDE = 5;
        void clickItem(int item);
    }
}

