package com.fernandofgallego.ciruclarrevealfragment;

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
- Back button on fragment to removeFragment fragment.
 */

//Bugs may wander beyond this point. Tread carefully.
public class MainActivity extends Activity{
    private CircularRevealingFragment mfragment;
    private Button button1, button2;
    private float x,y;
    private Boolean fragUp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.addButton1);
        button2 = (Button)findViewById(R.id.addButton2);

        button1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = v.getLeft() + event.getX();
                y = v.getTop() + event.getY();
                return false;
            }
        });

        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = v.getLeft() + event.getX();
                y = v.getY() + event.getY();
                return false;
            }
        });
	}

    /*
    Called by the button onClicks in activity_main.xml
     */
	public void addFragment(final View v)
	{
//		int randomColor =
//				Color.argb(255, (int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
        fragUp = true;
        int randomColor = Color.GREEN;
        if(v.getId() == R.id.addButton1) {
            mfragment = CircularRevealingFragment.newInstance((int) x, (int) y, randomColor, true);
        }else{
            mfragment = CircularRevealingFragment.newInstance((int) x, (int) y, randomColor, false);
        }
        getFragmentManager().beginTransaction().add(android.R.id.content, mfragment).addToBackStack(null).commit();
	}

    @Override
    public void onBackPressed(){
        if(fragUp){
            removeFragment(mfragment.getView());
        }else{
            super.onBackPressed();
        }
    }

    /*
    Called by the back button in fragment_main.xml
     */
    public void removeFragment(View v){
        fragUp = false;
        mfragment.removeYourself();
    }
}
