package com.example.library;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_FRAGMENT_TYPE = "com.example.library.FRAGMENT_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String fragmentType = getIntent().getStringExtra(EXTRA_FRAGMENT_TYPE);
        Fragment fragment = null;

        if (fragmentType != null) {
            switch (fragmentType) {
                case "instructions":
                    fragment = new InstructionsFragment();
                    break;
                case "about_author":
                    fragment = new AboutAuthorFragment();
                    break;
                case "about_app":
                    fragment = new AboutAppFragment();
                    break;
                default:
                    fragment = new InstructionsFragment();
                    break;
            }
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}
