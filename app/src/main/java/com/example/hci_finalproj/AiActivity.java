package com.example.hci_finalproj;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;

public class AiActivity{
    private static final String API_KEY = "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";
    //private static final String BASE_URL = "https://api.openai.com/";
    //private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    public RecipeDatabase recipeDatabase;
    public String promptIngredients;
    public String promptInstructions;
    public String recipeName;
    public String recipeFull = "";
    public ArrayList<String> ingredientsArrayList = new ArrayList<String>();
    public AiActivity(RecipeDatabase recipeDatabase) {
        this.recipeDatabase = recipeDatabase;
    }
    OpenAiService aiService = new OpenAiService(API_KEY);

    public void setRecipeDatabase(RecipeDatabase recipeDatabase) {
        this.recipeDatabase = recipeDatabase;
    }
    public void makeRecipeToDB(String ingredientsInput) {

        int count = 0;

        String ingredients = ingredientsList(ingredientsInput);
        String instructions = instructionsList(ingredients);

        String[] ingredientsArray = ingredients.split(", ");
        String[] instructionsArray = instructions.split("Step ");

        StringBuilder sb = new StringBuilder();

        for (String ingredientsStr : ingredientsArray) {
            ingredientsArrayList.add(ingredientsStr);
            this.recipeFull += "- " + ingredientsStr + "\n";
        }

        for (String instructionsStr : instructionsArray){
            if (count == 0){
                this.recipeFull += "\nRecipe Name: " + instructionsStr + "\n\n\n";
            }
            else{
                this.recipeFull += "Step " + count + ": " + instructionsStr + "\n";
            }
            count++;
        }

        SQLiteDatabase db = null;
        if (recipeDatabase != null) {
            db = recipeDatabase.getWritableDatabase();
        }

        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(RecipeDatabase.COLUMN_RECIPE_NAME, recipeName);
            values.put(RecipeDatabase.COLUMN_RECIPE_PROCEDURE, recipeFull);
            db.insert(RecipeDatabase.TABLE_NAME, null, values);

            db.insert(RecipeDatabase.TABLE_NAME, null, values);
        }

        db.close();
    }

    public String ingredientsList(String ingredientsInput) {
        promptIngredients = "Make an ingredient list of the following in this format: (including the comma) \n" +
                "Ingredient 1, Ingredient 2, Ingredient 3\n\n" +
                "Here are the ingredients (you can add/recommend other ingredients ONLY IF necessary): " + ingredientsInput;
        CompletionRequest ingredientsRequest = CompletionRequest.builder()
                .prompt(promptIngredients)
                .model("text-davinci-003")
                .maxTokens(252)
                .temperature(0.7)
                .echo(true)
                .build();
        CompletionResult completion = aiService.createCompletion(ingredientsRequest);
        String generatedText = completion.getChoices().get(0).getText();
        generatedText = generatedText.replaceFirst(promptIngredients, "").trim();

        return generatedText.substring(generatedText.lastIndexOf("\n\n") + 1).trim();

    }

    public String instructionsList(String ingredientsList) {
        promptInstructions = "Make a meal/recipe with the following ingredients with the following format: \n" +
                "A Catchy Recipe/Meal Name\n" +
                "Step number - instruction for step number\n" + ingredientsList;
        CompletionRequest instructionsRequest = CompletionRequest.builder()
                .prompt(promptInstructions)
                .model("text-davinci-003")
                .maxTokens(1024)
                .temperature(0.7)
                .echo(true)
                .build();
        CompletionResult completion = aiService.createCompletion(instructionsRequest);
        String generatedText = completion.getChoices().get(0).getText();
        generatedText = generatedText.replaceFirst(promptInstructions, "").trim();

        this.recipeName = generatedText.substring(0, (generatedText.indexOf("\n"))).trim();

        return generatedText.trim();
    }
}