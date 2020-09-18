package com.rotimijohnson.notetakingapp.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rotimijohnson.notetakingapp.R;
import com.rotimijohnson.notetakingapp.database.DataBaseHandler;
import com.rotimijohnson.notetakingapp.model.Note;
import com.rotimijohnson.notetakingapp.model.User;
import com.rotimijohnson.notetakingapp.util.SessionManagement;
import com.rotimijohnson.notetakingapp.view.auth.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private View view;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextInputLayout title, note;
    private Button savebtn;
    FloatingActionButton floatingActionButton;
    String addTitle,addNote;

    private TextView textView,username;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.fab_btn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab_background, null);
        textView = view.findViewById(R.id.tv1);
        imageView = view.findViewById(R.id.iv1);
        username = findViewById(R.id.log_in_text);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("account");
        username.setText(user.getUsername());


        //change the font family of text view in tabs(Make sure internet is on, for the first time to prevent crash)
        //if(internet is on)
        Typeface typeface = ResourcesCompat.getFont(getApplication(), R.font.poppins_medium);
        textView.setTypeface(typeface);
        //else
        //..default font


        //set view to display and text and image to display
        setCustomView(0, 1);
        setTextAndImageWithAnimation("HOME", R.drawable.ic_home);
        handleFragment(new HomeFragment());


        //tab layout selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

//                    case 1:
//                        setCustomView(1, 0, 2, 3);
//                        setTextAndImageWithAnimation("CATEGORIES", R.drawable.ic_categories);
//                        //change to the fragment which you want to display
//                        handleFragment(new HomeFragment());
//                        break;

//                    case 2:
//                        setCustomView(2, 1, 0, 3);
//                        setTextAndImageWithAnimation("SETTINGS", R.drawable.ic_settings);
//                        //change to the fragment which you want to display
//                        handleFragment(new HomeFragment());
//                        break;

                    case 1:
                        setCustomView(1, 0);
                        setTextAndImageWithAnimation("PROFILE", R.drawable.ic_person);
                        //change to the fragment which you want to display
                        handleFragment(new ProfileFragment());
                        break;
                    case 0:
                        //3 methods will be used in each case.
                        //method 1 : custom view(selected tab, non selected tabs) -> done
                        //method 2 : set text and image in tab with animation -> done
                        //method 3 : set fragment

                    default:
                        setCustomView(0, 1);
                        setTextAndImageWithAnimation("HOME", R.drawable.ic_home);
                        //change to the fragment which you want to display
                        handleFragment(new HomeFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    //set Views for each tab layout
    private void setCustomView(int selectedtab, int non1) {
        Objects.requireNonNull(tabLayout.getTabAt(selectedtab)).setCustomView(view);
//        Objects.requireNonNull(tabLayout.getTabAt(non1)).setCustomView(null);
//        Objects.requireNonNull(tabLayout.getTabAt(non2)).setCustomView(null);
        Objects.requireNonNull(tabLayout.getTabAt(non1)).setCustomView(null);
    }

    //set animations on the text and image tab layout
    private void setTextAndImageWithAnimation(String text, int images) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        textView.setText(text);
        imageView.setImageResource(images);
        textView.startAnimation(animation);
        imageView.startAnimation(animation);
    }

    //function for creating new fragment
    private void handleFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    //add note to the database
    private void addNote() {

        dialogBuilder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.add_dialog, null);

        title =  v.findViewById(R.id.title_note);
        note =  v.findViewById(R.id.text_note);
        savebtn = (Button) v.findViewById(R.id.save);

        dialogBuilder.setView(v);
        dialog = dialogBuilder.create();
        dialog.show();

        addTitle = title.getEditText().getText().toString().trim();
        addNote = note.getEditText().getText().toString().trim();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!title.getEditText().getText().toString().isEmpty() && !note.getEditText().getText().toString().isEmpty()){
                    DataBaseHandler db = new DataBaseHandler(MainActivity.this);
                    Note nt = new Note(title.getEditText().getText().toString(), note.getEditText().getText().toString());
                    db.addNote(nt);
                    db.close();

                    String _username = getIntent().getStringExtra("username");
                    username.setText(_username);
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //logout user method
    public void logOutScreen(View view) {
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.removeSession();

        moveToLogin();
    }

    //move the user to the login activity
    private void moveToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
