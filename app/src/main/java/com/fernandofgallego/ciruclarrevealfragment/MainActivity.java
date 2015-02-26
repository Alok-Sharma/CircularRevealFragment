package com.fernandofgallego.ciruclarrevealfragment;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity implements OnFragmentTouched
{
    CircularRevealingFragment mfragment;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void addFragment(View v)
	{
		int randomColor =
				Color.argb(255, (int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		mfragment = CircularRevealingFragment.newInstance(20, 20, randomColor);
		getFragmentManager().beginTransaction().add(R.id.container, mfragment).commit();
	}

    public void remove(View v){
        Animator unreveal = mfragment.prepareUnrevealAnimator(0, 0);

        unreveal.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                // remove the fragment only when the animation finishes
                getFragmentManager().beginTransaction().remove(mfragment).commit();
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }
        });
        unreveal.start();
    }

	@Override
	public void onFragmentTouched(Fragment fragment, float x, float y)
	{
        remove(null);
	}


}
