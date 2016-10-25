package fitness.cs115.a115fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Matthew on 10/9/16.
 */

public class meal_overview extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_overview);

        Button meal = (Button) findViewById(R.id.newMeal);
        meal.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_createMeal.class);
                startActivity(intent);
            }
        });

        Button addFood = (Button) findViewById(R.id.addFood);
        addFood.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_addFood.class);
                startActivity(intent);
            }
        });

        Button viewMeals = (Button) findViewById(R.id.view_meals);
        viewMeals.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(meal_overview.this, meal_viewAllMeals.class);
                startActivity(intent);
            }
        });

    }
}
