package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Matthew on 10/13/16.
 * Teg wrote extract integers and helped with other things
 */
//the purpose of this class is to eat the contents of a meal
    //and save the changes of the meal to the database.
public class meal_editMeal extends AppCompatActivity {
    private meal_mealDBHelper mydb;
    private meal_foodDBHelper fooddb = new meal_foodDBHelper(this); //fooddb is the fooddatabase accessor
    ;
    private boolean DEBUG = false;
    String mealtablename;
    private ListView lv; //used to display foods
    ArrayList<String> foodkeyset;// = new ArrayList<>();  //store all foods in the foods database

    HashMap<String ,HashMap<String ,Double>> mainmacrosmap;
    HashMap<String ,HashMap<String ,Double>> allmacrosmap;



    ArrayList<String> items_add = new ArrayList<>(); // items to add to this meal
    ArrayList<String> already_existing_foods = new ArrayList<>(); //items that are already present in the meal

    private meal_foodDBHelper foodDB = new meal_foodDBHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);
        Bundle extras = getIntent().getExtras();    //meal name coming in to be edited
        if (extras == null) {
            Log.e("meal_editMeal: ", "error getting table name");
            Intent intent = new Intent(meal_editMeal.this, MainActivity.class); //if weird error here, just go back to main activity
            startActivity(intent);
            return;
        }
        mealtablename =  extras.getString("TABLE"); //this is the name of the [meal] table that is being edited.
        mainmacrosmap = foodDB.getfourfoodmacros();
        allmacrosmap = foodDB.getAllFoodInfo();
        foodkeyset = new ArrayList<String>(mainmacrosmap.keySet());

        mydb = new meal_mealDBHelper(this, mealtablename); //myDB is the name of the meal db that is being edited
        lv = (ListView) findViewById(R.id.meal_items);


       // Log.v("meal_editMeal:", " table name is: " + mealtablename);

        /*
        //iterate through foods database and add all foods and their calories
        if (DEBUG) {
            food_names.add("bacon"); //dummy data, not correctly formatted
        }
        try {
            already_existing_foods = mydb.getAllmacrosInfo(); //this gets all of the foods currently in this meal
        } catch (Exception e) {
            already_existing_foods.clear(); //just in case there is somehow data in here
            mealtablename = "[" + mealtablename + "]";
            mydb = new meal_mealDBHelper(this, mealtablename); //myDB is the name of the meal db that is being edited
            already_existing_foods = mydb.getAllmacrosInfo(); //this gets all of the foods currently in this meal
        }
        Log.v("meal_editMeal:", " already_existing_foods is: " + already_existing_foods);
        Log.v("food_names:", " food_names is: " + food_names);

        food_names.removeAll(already_existing_foods);//in the list of all foods, get rid of elements that are already in the meal, they will be inserted at the front
        //put  list in lexographical order
      //  Collections.sort(already_existing_foods);

        if (DEBUG) {
            System.out.println("food names: " + food_names);
            System.out.println("already_existing_foods in meal: " + already_existing_foods);
        }
        for (String temp_food : already_existing_foods) { //iterate through the list, adding each item to the beginning of the list
            food_names.add(0, temp_food);
        }
        System.out.println("food names before header: " + food_names);

       // food_names.add(0, "Foods in Meal:"); //explain to user what is already in meal
        if (DEBUG) {
            System.out.println("food names: " + food_names);
        }
        System.out.println("food names after header: " + food_names);


*/

        ArrayAdapter editmealListAdapter = new meal_editmeal_adapter(this, mainmacrosmap, foodkeyset);
        ListView editmealListView = (ListView) findViewById(R.id.meal_items);
        editmealListView.setAdapter(editmealListAdapter);
        editmealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){


                String foodnamer = mainmacrosmap.keySet().toArray(new String [mainmacrosmap.size()])[position];
              //  Toast.makeText(getApplicationContext(), "printing position: " + position + foodnamer, Toast.LENGTH_SHORT).show();
               // Toast.makeText(getApplicationContext(), "printing position: " + position + foodName, Toast.LENGTH_SHORT).show();

                //find the the four parameters from this position and add them to mydb table
                if (items_add.contains(foodnamer)) { //food_names.get(position)
                    //we can update the food in meal, rather than jus deleting it
                    items_add.remove(foodnamer);
                    Toast.makeText(getApplicationContext(), "Removed: " + foodnamer + " from meal", Toast.LENGTH_SHORT).show();
                  //  String name = foodName.substring(0, foodName.indexOf(','));
                    mydb.deleteFoodinMeal(foodnamer);
                    System.out.println("all macro after delete: " + mydb.getAllmacrosInfo());
                } else { //means it's not already in the meal
                    items_add.add(foodnamer);
                    System.out.println("items add:" + items_add);
                    Toast.makeText(getApplicationContext(), "Added: " + foodnamer + " to meal", Toast.LENGTH_SHORT).show();
                    Log.d("tag", "printing after adding to items" + items_add);
                    try {
                        HashMap<String,Double> innermap = allmacrosmap.get(foodnamer);
                        mydb.insertFoodinMeal(foodnamer, innermap.get("calories"), innermap.get("fat"), innermap.get("transfat"), innermap.get("satfat"),
                                innermap.get("Cholestrol"), innermap.get("sodium"), innermap.get("carbs"), innermap.get("fiber"), innermap.get("sugar"),
                                innermap.get("protein")); //inserting to meals DB
                        Log.d("tag", "total cal: " + mydb.getTotalCalories());
                    } catch (Exception e) {//this is just for debugging to stop app from crashing based off of incomplete hardcoded database entries
                        //this catch will *not* get triggered with actual values
                        System.out.println(e.toString());
                    }
                }




            }
        });















        //this attaches the listview to the array list to display the food names and calories
        /*
        ArrayAdapter<String> foodArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, food_names);

        lv.setAdapter(foodArrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String foodName = food_names.get(position);
                if (DEBUG) {
                    Toast.makeText(getApplicationContext(), "position: " + position, Toast.LENGTH_SHORT).show();
                    //getApplicationContext().
                }
                if (foodName.equals("Foods in Meal:") || foodName.equals("Foods in Database:")) { //can't add this to meal, it's just a prompt
                    return;
                }
                //find the the four parameters from this position and add them to mydb table
                if (items_add.contains(food_names.get(position))) {
                    //we can update the food in meal, rather than jus deleting it
                    items_add.remove(food_names.get(position));
                    Toast.makeText(getApplicationContext(), "removed: " + food_names.get(position) + " from meal", Toast.LENGTH_SHORT).show();
                    String name = foodName.substring(0, foodName.indexOf(','));
                    mydb.deleteFoodinMeal(name);
                    System.out.println("all macro after delete: " + mydb.getAllmacrosInfo());
                } else { //means it's not already in the meal
                    items_add.add(food_names.get(position));
                    System.out.println("items add:" + items_add);
                    Toast.makeText(getApplicationContext(), "added: " + food_names.get(position) + " to meal", Toast.LENGTH_SHORT).show();
                    Log.d("tag", "printing after adding to items" + items_add);
                    try {
                        ArrayList<Double> item = extractIntegers(foodName); //extracting the double paramters from the foods
                        String name = foodName.substring(0, foodName.indexOf(','));
                        Log.d("tag", "printing after decypher " + "name[" + name + "]");
                        Log.d("tag", "printing after decypher " + "Cals[" + item.get(0) + "]");
                        Log.d("tag", "printing after decypher " + "Fals[" + item.get(1) + "]");
                        Log.d("tag", "printing after decypher " + "Carbs[" + item.get(2) + "]");
                        Log.d("tag", "printing after decypher " + "Protein[" + item.get(3) + "]");

                        mydb.insertFoodinMeal(name, item.get(0), item.get(1), fooddb.getTransFat(name), fooddb.getSatFat(name),
                                fooddb.getCholesterol(name), fooddb.getSodium(name), item.get(2), fooddb.getFiber(name), fooddb.getSugar(name),
                                item.get(3)); //inserting to meals DB
                        Log.d("tag", "total cal: " + mydb.getTotalCalories());
                    } catch (Exception e) {//this is just for debugging to stop app from crashing based off of incomplete hardcoded database entries
                        //this catch will *not* get triggered with actual values
                        System.out.println(e.toString());
                    }
                }
            }
        });
        */

        //mydb.insertFoodinMeal(it)

        //need to load all existing food names/calories into array list to see if they exist already. if it already exists, in a meal. don't add it
        // and display error message saying
        //no need to use a map instead since at absolute most a meal will probably have 20 items in it

        Toast.makeText(this, "Tap the items you wish to add to the meal, Tap again to remove", Toast.LENGTH_LONG).show();
    }

    //extract integers from a string and return them in Arraylist of double
    public ArrayList<Double> extractIntegers(String input) {
        ArrayList<Double> result = new ArrayList<Double>();
        int index = 0;
        double v = 0;
        int l = 0;
        while (index < input.length())
        {
            char c = input.charAt(index);
            if (Character.isDigit(c)) {
                v *= 10;
                v += c - '0';
                l++;
            }
            else if (l > 0) {
                result.add(v);
                l = 0;
                v = 0;
            }
            index++;
        }
        if (l > 0) {
            result.add(v);
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(meal_editMeal.this, meal_Onboarding.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
