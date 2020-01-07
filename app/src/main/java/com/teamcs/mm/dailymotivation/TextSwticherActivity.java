package com.teamcs.mm.dailymotivation;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.Toast;

public class TextSwticherActivity extends AppCompatActivity {

    private TextSwitcher textSwitcher;
    private ConstraintLayout textSwitcherLayout;
    //private Button nextButton;
    private int count =0;

    private String[] motivation_quoutes = new String[10];
    private static int motivation_index = 0;
    private static boolean show_fab_menu = true;

    private int[] background_image = new int[3];
    private static int background_index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_swticher);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fabFav = findViewById(R.id.fabFav);
        fabFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Added to Favourite!", Snackbar.LENGTH_LONG)
                        .setAction("Fav", null).show();
            }
        });

        final FloatingActionButton fabShare = findViewById(R.id.fabShare);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Share", Snackbar.LENGTH_LONG)
                 //       .setAction("Share", null).show();

                String shareBody = motivation_quoutes[motivation_index];
                /*
                String shareBody = motivation_quoutes[motivation_index];
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Daily Motivation");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                */
                //Uri imageUri = Uri.parse("android.resource://" + getPackageName()
                //        + "/drawable/" + "pixel3_wp1");
                Uri imageUri = resourceToUri(getApplicationContext(),background_image[background_index]);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Daily Motivation");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "send"));
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show_fab_menu){
                    fabFav.show();
                    fabShare.show();
                    show_fab_menu = false;
                }else{
                    fabFav.hide();
                    fabShare.hide();
                    show_fab_menu = true;
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });


        fabFav.hide();
        fabShare.hide();

        motivation_quoutes[0] = "Every new beginning comes from some other beginning’s end.";
        motivation_quoutes[1] = "No one can ever take your memories from you – each day is a new beginning, make good memories every day.";
        motivation_quoutes[2] = "Take the first step in faith. You don’t have to see the whole staircase, just take the first step.";
        motivation_quoutes[3] = "Although no one can go back and make a brand new start, anyone can start from now and make a brand new ending.";
        motivation_quoutes[4] = "It’s never too late to become who you want to be. I hope you live a life that you’re proud of, and if you find that you’re not, I hope you have the strength to start over.";
        motivation_quoutes[5] = "There are two mistakes one can make along the road to truth… not going all the way, and not starting.";
        motivation_quoutes[6] = "New beginnings are often disguised as painful endings.";
        motivation_quoutes[7] = "Failure is the opportunity to begin again more intelligently.";
        motivation_quoutes[8] = "No river can return to its source, yet all rivers must have a beginning.";
        motivation_quoutes[9] = "Nothing in the universe can stop you from letting go and starting over.";

        background_image[0] = R.drawable.pixel3_wp1;
        background_image[1] = R.drawable.pixel3_wp2;
        background_image[2] = R.drawable.pixel3_wp3;

        textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        //nextButton = (Button) findViewById(R.id.nextButton);

        textSwitcher.setCurrentText("Hello Android App Developer");

        Animation textAnimationIn =  AnimationUtils.
                loadAnimation(this,   android.R.anim.fade_in);
        textAnimationIn.setDuration(200);

        Animation textAnimationOut =  AnimationUtils.
                loadAnimation(this,   android.R.anim.fade_out);
        textAnimationOut.setDuration(200);

        textSwitcher.setInAnimation(textAnimationIn);
        textSwitcher.setOutAnimation(textAnimationOut);

        textSwitcherLayout = (ConstraintLayout) findViewById(R.id.textSwitcherLayout);
        textSwitcherLayout.setBackground(getResources().getDrawable(background_image[background_index]));

        textSwitcherLayout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
                //showPrevText();
                // next
                showNextText();
               // Toast.makeText(getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                // previous
                showPrevText();
                //Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                // next
                showNextText();
                //Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                //showNextText();
                showPrevText();
                //Toast.makeText(getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void showNextText(){
        motivation_index++;
        background_index++;
        if(motivation_index >= motivation_quoutes.length) motivation_index = 0;
        if(background_index >= background_image.length) background_index = 0;
        textSwitcher.setText(motivation_quoutes[motivation_index]);
        textSwitcherLayout.setBackground(getResources().getDrawable(background_image[background_index]));

    }
    public void showPrevText(){
        motivation_index--;
        background_index--;
        if(motivation_index < 0) motivation_index = motivation_quoutes.length - 1;
        if(background_index < 0)background_index = background_image.length - 1;
        textSwitcher.setText(motivation_quoutes[motivation_index]);
        textSwitcherLayout.setBackground(getResources().getDrawable(background_image[background_index]));

    }

    /* test code */
    /* --

        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.pixel3_wp1), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.pixel3_wp2), 500);
        animation.addFrame(getResources().getDrawable(R.drawable.pixel3_wp3), 300);
        animation.setOneShot(false);


        ImageView imageAnim =  (ImageView) findViewById(R.id.img);
        imageAnim.setBackgroundDrawable(animation);

    //textSwitcherLayout.setBackgroundDrawable(animation);
    // start the animation!
    //animation.start();
     */

    public static Uri resourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID) );
    }
}
