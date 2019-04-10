package projeto.suporteentrevista.Behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import projeto.suporteentrevista.R;

import edmt.dev.advancednestedscrollview.MaxHeightRecyclerView;
import edmt.dev.advancednestedscrollview.MyViewGroupUtils;

public class CustomBehavior extends CoordinatorLayout.Behavior<NestedScrollView> {

    public CustomBehavior(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child, @NonNull View dependency) {
        return dependency.getId() == R.id.toolbar_container;

    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull final NestedScrollView child, int layoutDirection) {
        parent.onLayoutChild(child,layoutDirection);

        int fabHalfHeight = child.findViewById(R.id.beginBtn).getHeight()/2;
        setTopMargin(child.findViewById(R.id.card_view),fabHalfHeight); //Margin cardViewto 1/2 fab height
        int rvMaxHeight = child.getHeight() - 2*fabHalfHeight - child.findViewById(R.id.card_title).getHeight() - child.findViewById(R.id.editTextNovaPergunta).getHeight();


        final MaxHeightRecyclerView rv = child.findViewById(R.id.card_recycler_view);

        rv.setMaxHeight(rvMaxHeight);

        View cardContainer = child.findViewById(R.id.card_container);
        int toolbarContainerHeight = parent.getDependencies(child).get(0).getHeight();
        setPaddingTop(cardContainer,rvMaxHeight - toolbarContainerHeight);
        ViewCompat.offsetTopAndBottom(child,toolbarContainerHeight);
        setPaddingBottom(rv,toolbarContainerHeight);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child, @NonNull MotionEvent ev) {
        // Block all touch events that originate within the bounds of our
        // NestedScrollView but do *not* originate within the bounds of its
        // inner CardView and FloatingActionButton

        return ev.getActionMasked() == MotionEvent.ACTION_DOWN
                && isTouchInChildBounds(parent,child,ev)
                && !isTouchInChildBounds(parent,child.findViewById(R.id.card_view),ev)
                && !isTouchInChildBounds(parent,child.findViewById(R.id.beginBtn),ev);
    }

    private boolean isTouchInChildBounds(ViewGroup parent, View child, MotionEvent ev) {
        return MyViewGroupUtils.isPointInChildBounds(parent,child,(int)ev.getX(),(int)ev.getY());
    }

    private void setPaddingBottom(View view, int bottom) {
        if(view.getPaddingBottom() != bottom){
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),view.getPaddingRight(),bottom);
        }
    }

    private void setPaddingTop(View view, int top) {
        if(view.getPaddingTop() != top){
            view.setPadding(view.getPaddingLeft(),top,view.getPaddingRight(),view.getPaddingBottom());
        }
    }

    private void setTopMargin(View view, int top){
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if(lp.topMargin != top){
            lp.topMargin = top;
            view.setLayoutParams(lp);
        }
    }

}
