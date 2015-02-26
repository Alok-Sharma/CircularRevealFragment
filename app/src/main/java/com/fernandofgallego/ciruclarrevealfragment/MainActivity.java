package com.fernandofgallego.ciruclarrevealfragment;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/*
License: http://www.apache.org/licenses/LICENSE-2.0
Original Code from from ferdy182. Repo at: https://github.com/ferdy182/Android-CircularRevealFragment
 */

/*
Modified code from ferdy182. Modifications are basically-
- Moved the fragment to another class.
- Using only 1 full screen fragment, instead of allowing creation of many.
- Back button on fragment to remove fragment.
 */

//Bugs may wander beyond this point. Tread carefully.
public class MainActivity extends Activity{
    CircularRevealingFragment mfragment;
    Button button;
    float x,y;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.addButton);

        button.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getX();
                y = event.getY();
                return false;
            }
        });
	}

	public void addFragment(View v)
	{
		int randomColor =
				Color.argb(255, (int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
		mfragment = CircularRevealingFragment.newInstance((int)x, (int)y, randomColor);
        getFragmentManager().beginTransaction().add(android.R.id.content, mfragment).commit();
//        getFragmentManager().beginTransaction().replace(android.R.id.content, mfragment).commit();
	}

    public void remove(View v){
        Animator unreveal = mfragment.prepareUnrevealAnimator(x, y);
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
                mfragment.makeInv();
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
}
