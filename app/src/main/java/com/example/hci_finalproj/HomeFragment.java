package com.example.hci_finalproj;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AccountDatabase accountDatabase;
    public RecipeDatabase recipeDatabase;
    TextView greet_user;
    AiActivity openAiService;
    LinearLayout ingredientsContainer;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setAccountDatabase(AccountDatabase accountDatabase) {
        this.accountDatabase = accountDatabase;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        greet_user = view.findViewById(R.id.greet_user);

        String greetings = "Hi "+ accountDatabase.getUsername() + "!";
        greet_user.setText(greetings);

        recipeDatabase = new RecipeDatabase(getContext());

        RelativeLayout searchIcon = view.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchRecipe(v);
            }
        });
        return view;
    }

    public void onSearchRecipe(View view) {
        EditText input = getView().findViewById(R.id.search_et);
        String ingredientsInput = input.getText().toString();

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            AiActivity aiActivity = new AiActivity(recipeDatabase);
            aiActivity.setRecipeDatabase(recipeDatabase);
            aiActivity.makeRecipeToDB(ingredientsInput);

            openAiService = aiActivity;

            getActivity().runOnUiThread(() -> {
                String recipeName = openAiService.recipeName;

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View recipeView = inflater.inflate(R.layout.recipe_item_layout, null);

                TextView recipeNameTV = recipeView.findViewById(R.id.recipe_name);
                recipeNameTV.setText(recipeName);

                recipeNameTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setRecipeSheet(recipeName);
                    }
                });

                ingredientsContainer = recipeView.findViewById(R.id.ingredients_container);

                for (String ingredient : openAiService.ingredientsArrayList) {
                    View ingredientView = inflater.inflate(R.layout.ingredients_item_layout, null);

                    TextView ingredientTV = ingredientView.findViewById(R.id.ingredient_tv);
                    ingredientTV.setText(ingredient);

                    ingredientsContainer.addView(ingredientView);
                }


                LinearLayout recentRecipesContainer = getView().findViewById(R.id.recent_recipes_container);
                recentRecipesContainer.addView(recipeView, 0);
            });
        });
    }

    public void setRecipeSheet(String recipeName){
        View recipeSheet = getLayoutInflater().inflate(R.layout.recipe_sheet, null, false);
        RelativeLayout screen = getActivity().findViewById(R.id.main_screen);

        recipeDatabase.readInfo(recipeName);

        TextView rtText = recipeSheet.findViewById(R.id.recipe_title);
        TextView rfText = recipeSheet.findViewById(R.id.recipe_full);
        ImageView doBack = recipeSheet.findViewById(R.id.back);

        rtText.setText(recipeName);
        rfText.setText(recipeDatabase.getRecipeProcedure());

        screen.addView(recipeSheet);

        doBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen.removeView(recipeSheet);
            }
        });
    }
}