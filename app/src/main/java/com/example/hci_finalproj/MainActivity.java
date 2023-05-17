package com.example.hci_finalproj;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hci_finalproj.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{
    RecipeDatabase recipeDatabase;
    ActivityMainBinding binding;
    AccountDatabase accountDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipeDatabase = new RecipeDatabase(this);
        accountDatabase = new AccountDatabase(this);

        replaceFragment(new HomeFragment());

        accountDatabase.readInfo(getIntent().getStringExtra("email"), getIntent().getStringExtra("password"));

        binding.bottomNavBarView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;

                case R.id.library:
                    replaceFragment(new LibraryFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment instanceof HomeFragment) {
            ((HomeFragment) fragment).setAccountDatabase(accountDatabase);
        }

        else if (fragment instanceof ProfileFragment) {
            ((ProfileFragment) fragment).setAccountDatabase(accountDatabase);
        }

        else if (fragment instanceof LibraryFragment) {
            ((LibraryFragment) fragment).setAccountDatabase(accountDatabase);
        }

        fragmentTransaction.replace(R.id.relative_layout, fragment);
        fragmentTransaction.commit();
    }


    public void logout(){
        Intent intent = new Intent(this, StartUpActivity.class);
        startActivity(intent);
    };
}
	
	