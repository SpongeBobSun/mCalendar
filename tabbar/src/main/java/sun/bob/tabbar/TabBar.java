package sun.bob.tabbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by bob.sun on 15/10/16.
 */
public class TabBar extends LinearLayout {
    private int count = 0;
    private int lastIndex = 0;
    private OnTabClickedListener onTabClickedListener;

    public TabBar(Context context) {
        super(context);
        init();
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    void init(){
        setOrientation(LinearLayout.HORIZONTAL);
        Background background = new Background();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackgroundDrawable(background);
        } else {
            this.setBackground(background);
        }
        this.setPadding(0, 10, 0, 0);
    }


    public TabBar addTab(int imgId, int highlightImgId, String text, int fontColor, int highlightFontColor){
        TabView tabView = new TabView(getContext()).NewTabView(imgId, highlightImgId, text, fontColor, highlightFontColor);
        count ++;
        final int index = count - 1;
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        tabView.setLayoutParams(layoutParams);
        this.addView(tabView);
        tabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabBar that = TabBar.this;
                ((TabView) that.getChildAt(lastIndex)).change();
                that.lastIndex = index;
                ((TabView) v).change();
                if (that.onTabClickedListener != null) {
                    that.onTabClickedListener.onTabClicked(index);
                }
            }
        });
        if (index == 0){
            tabView.change();
        }
        return this;
    }

    class Background extends Drawable {

        private Paint paint;
        public Background(){
            super();
            paint = new Paint();
            paint.setColor(Color.GRAY);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawLine(0, 0, canvas.getWidth(), 0, paint);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    public TabBar setOnTabClickedListener(OnTabClickedListener onTabClickedListener) {
        this.onTabClickedListener = onTabClickedListener;
        return this;
    }
}
