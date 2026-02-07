package com.example.foodplannerapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Entity(tableName = "meals_table")
public class Meal implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strArea")
    private String area;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strMealThumb")
    private String thumbUrl;

    @SerializedName("strYoutube")
    private String youtubeUrl;

    // --- NEW: Ingredient Fields (Required for Ingredients to show) ---
    public String strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
            strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
            strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
            strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20;

    public String strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
            strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
            strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
            strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20;

    // Default Constructor
    public Meal() {}

    // --- Getters for Ingredients ---
    public String getStrIngredient1() { return strIngredient1; }
    public String getStrIngredient2() { return strIngredient2; }
    public String getStrIngredient3() { return strIngredient3; }
    public String getStrIngredient4() { return strIngredient4; }
    public String getStrIngredient5() { return strIngredient5; }
    public String getStrIngredient6() { return strIngredient6; }
    public String getStrIngredient7() { return strIngredient7; }
    public String getStrIngredient8() { return strIngredient8; }
    public String getStrIngredient9() { return strIngredient9; }
    public String getStrIngredient10() { return strIngredient10; }
    public String getStrIngredient11() { return strIngredient11; }
    public String getStrIngredient12() { return strIngredient12; }
    public String getStrIngredient13() { return strIngredient13; }
    public String getStrIngredient14() { return strIngredient14; }
    public String getStrIngredient15() { return strIngredient15; }
    public String getStrIngredient16() { return strIngredient16; }
    public String getStrIngredient17() { return strIngredient17; }
    public String getStrIngredient18() { return strIngredient18; }
    public String getStrIngredient19() { return strIngredient19; }
    public String getStrIngredient20() { return strIngredient20; }

    public String getStrMeasure1() { return strMeasure1; }
    public String getStrMeasure2() { return strMeasure2; }
    public String getStrMeasure3() { return strMeasure3; }
    public String getStrMeasure4() { return strMeasure4; }
    public String getStrMeasure5() { return strMeasure5; }
    public String getStrMeasure6() { return strMeasure6; }
    public String getStrMeasure7() { return strMeasure7; }
    public String getStrMeasure8() { return strMeasure8; }
    public String getStrMeasure9() { return strMeasure9; }
    public String getStrMeasure10() { return strMeasure10; }
    public String getStrMeasure11() { return strMeasure11; }
    public String getStrMeasure12() { return strMeasure12; }
    public String getStrMeasure13() { return strMeasure13; }
    public String getStrMeasure14() { return strMeasure14; }
    public String getStrMeasure15() { return strMeasure15; }
    public String getStrMeasure16() { return strMeasure16; }
    public String getStrMeasure17() { return strMeasure17; }
    public String getStrMeasure18() { return strMeasure18; }
    public String getStrMeasure19() { return strMeasure19; }
    public String getStrMeasure20() { return strMeasure20; }

    // --- Standard Getters & Setters ---
    public void setId(@NonNull String id) { this.id = id; }
    public String getId() { return id; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    public void setArea(String area) { this.area = area; }
    public String getArea() { return area; }

    public void setCategory(String category) { this.category = category; }
    public String getCategory() { return category; }

    public void setInstructions(String instructions) { this.instructions = instructions; }
    public String getInstructions() { return instructions; }

    public void setThumbUrl(String thumbUrl) { this.thumbUrl = thumbUrl; }
    public String getThumbUrl() { return thumbUrl; }

    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
    public String getYoutubeUrl() { return youtubeUrl; }
}