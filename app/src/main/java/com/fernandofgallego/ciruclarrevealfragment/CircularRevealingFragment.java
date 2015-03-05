package com.fernandofgallego.ciruclarrevealfragment;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Alok on 2/26/2015.
 */
/**
 * Our demo fragment
 */
public class CircularRevealingFragment extends Fragment
{
    int cx, cy;

    public CircularRevealingFragment()
    {
    }

    public static CircularRevealingFragment newInstance(int centerX, int centerY, int color, boolean doAccelerate)
    {
        Bundle args = new Bundle();
        args.putInt("cx", centerX);
        args.putInt("cy", centerY);
        args.putInt("color", color);
        args.putBoolean("doAccelerate", doAccelerate);
        CircularRevealingFragment fragment = new CircularRevealingFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootView.setBackgroundColor(getArguments().getInt("color"));

        // To run the animation as soon as the view is layout in the view hierarchy we add this
        // listener and removeFragment it
        // as soon as it runs to prevent multiple animations if the view changes bounds
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener()
        {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom)
            {
                v.removeOnLayoutChangeListener(this);
                cx = getArguments().getInt("cx");
                cy = getArguments().getInt("cy");
                // get the hypothenuse so the radius is from one corner to the other
                int radius = (int)Math.hypot(right, bottom);

                Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                if(getArguments().getBoolean("doAccelerate")) {
                    reveal.setInterpolator(new DecelerateInterpolator(1.5f));
                }
                reveal.setDuration(700);
                reveal.start();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    /*
    Executes fragment removal animation and removes the fragment from view.
     */
    public void removeYourself(){
        final CircularRevealingFragment mfragment = this;
        Animator unreveal = mfragment.prepareUnrevealAnimator(cx, cy);
        if(unreveal != null) {
            unreveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // removeFragment the fragment only when the animation finishes
                    getFragmentManager().popBackStack();
                    getFragmentManager().beginTransaction().remove(mfragment).commit();
                    getFragmentManager().executePendingTransactions(); //Prevents the flashing.
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            unreveal.start();
        }
    }


    /**
     * Get the animator to unreveal the circle
     *
     * @param cx center x of the circle (or where the view was touched)
     * @param cy center y of the circle (or where the view was touched)
     * @return Animator object that will be used for the animation
     */
    public Animator prepareUnrevealAnimator(float cx, float cy)
    {

        int radius = getEnclosingCircleRadius(getView(), (int)cx, (int)cy);
        if(radius == -1){
            return null;
        }
        Animator anim = ViewAnimationUtils.createCircularReveal(getView(), (int) cx, (int) cy, radius, 0);
        if(getArguments().getBoolean("doAccelerate")) {
            anim.setInterpolator(new AccelerateInterpolator(1.5f));
        }
        anim.setDuration(700);
        return anim;
    }

    /**
     * To be really accurate we have to start the circle on the furthest corner of the view
     *
     * @param v the view to unreveal
     * @param cx center x of the circle
     * @param cy center y of the circle
     * @return the maximum radius
     */
    private int getEnclosingCircleRadius(View v, int cx, int cy)
    {
        if(v == null){
            return -1;
        }
        int realCenterX = cx + v.getLeft();
        int realCenterY = cy + v.getTop();
        int distanceTopLeft = (int)Math.hypot(realCenterX - v.getLeft(), realCenterY - v.getTop());
        int distanceTopRight = (int)Math.hypot(v.getRight() - realCenterX, realCenterY - v.getTop());
        int distanceBottomLeft = (int)Math.hypot(realCenterX - v.getLeft(), v.getBottom() - realCenterY);
        int distanceBotomRight = (int)Math.hypot(v.getRight() - realCenterX, v.getBottom() - realCenterY);

        int[] distances = new int[] {distanceTopLeft, distanceTopRight, distanceBottomLeft, distanceBotomRight};
        int radius = distances[0];
        for (int i = 1; i < distances.length; i++)
        {
            if (distances[i] > radius)
                radius = distances[i];
        }
        return radius;
    }
}

