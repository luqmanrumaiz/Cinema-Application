package com.cinemaapplication;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

/*
 * This Class is used to make Validating User Inputs easier, by implementing the TextWatcher Class
 * the need to set a new TextWatcher for each specific Validation is too much work, this simply
 * accepts parameters of the Activity (Register and Edit Activities have some different inputs) and
 * also the TextInputLayout to get the Input and set Errors
 *
 * https://stackoverflow.com/questions/5702771/how-to-use-single-textwatcher-for-multiple-edittexts\
 */

public class CustomTextWatcher implements TextWatcher
{
    private final TextInputLayout inputLayout;
    private final RegisterActivity registerActivity;
    private final EditInfoActivity editInfoActivity;
    private boolean[] errors;   // This Array Contains 3 Booleans, each for each Validation

    /* The Rating Input For the Edit Activity uses a RatingBar instead of a TextField so we create
     * 02 Constructors for each Activity, and it can be differentiated based on if an Activity is
     * null or not
     */

    public CustomTextWatcher(TextInputLayout inputLayout, RegisterActivity registerActivity)
    {
        this.inputLayout = inputLayout;
        this.registerActivity = registerActivity;
        editInfoActivity = null;
        errors = new boolean[]{false, false, false, false};
    }

    public CustomTextWatcher(TextInputLayout inputLayout, EditInfoActivity editInfoActivity)
    {
        this.inputLayout = inputLayout;
        this.editInfoActivity = editInfoActivity;
        registerActivity = null;
        errors = new boolean[]{false, false, false};
    }

    public void onTextChanged(CharSequence s,int start, int before, int count)
    {
        int index = 0;

        if (inputLayout.getId() == R.id.yearTextField)

            index = 1;

        else if (inputLayout.getId() == R.id.actorAndActressTextField)

            index = 2;

        else if (inputLayout.getId() == R.id.ratingTextField)

            index = 3;

        else if (inputLayout.getId() == R.id.reviewTextField)

            index = 4;

        // First Validation is to check if the TextField is empty
        if (s.toString().equals(""))
        {
            inputLayout.setError("You cannot leave the field blank !!!");
            if (registerActivity != null)

                registerActivity.setError(true, index);

            else if (editInfoActivity != null)

                editInfoActivity.setError(true, index);
        }
        else
        {
            // Storing the text in a variable and also in a Char Array required for some validations
            String text = inputLayout.getEditText().getText().toString().trim();
            char[] chars = text.toCharArray();

            /* Second Validation is to check if the TextField for Actors and Actresses
             * where the first and last Character cannot contain a ','. And there cannot be an empty
             * name between two ','
             */
            if (inputLayout.getId() == R.id.actorAndActressTextField && chars.length > 0)
            {
                errors[0] = chars[0] == ',' || chars[chars.length - 1] == ',';

                /* Third Validation is to make sure that no empty Actor/Actress is left between
                 * two commas
                 */
                if (! errors[0])
                {
                    // Iterating Through each Letter to see if the letter before is a ',' and the letter after
                    boolean commaError = false;
                    int letterCount = 0;
                    for (char letter : chars)
                    {
                        if (letter == ',' && chars[letterCount + 1] == ',')
                        {
                            errors[0] = true;
                            commaError = true;
                            break;
                        }

                        letterCount ++;
                    }

                    // If Third Validation is passed then error is set to false
                    if (! commaError)

                        errors[0] = false;
                }
            }
            // The Fourth Validation is to make sure that the year is not <1855 or > The Current Year
            else if (inputLayout.getId() == R.id.yearTextField && chars.length > 0)
            {
                int date = Integer.parseInt(text);
                errors[1] = date < 1855 || date > Calendar.getInstance().get(Calendar.YEAR);
            }
            // The Fourth Validation is to make sure that the rating is not <1 or > 10
            else if (inputLayout.getId() == R.id.ratingTextField && chars.length > 0)
            {
                int rating = Integer.parseInt(text);
                errors[2] = rating > 10 || rating < 1;
            }
            /* Now after analyzing all Errors we loop through all the booleans in errors and
             * see if we have made an Error, If so we set a Error Message for the TextField and
             * also set the error Property of the Activity to true to make sure that the user
             * does not register or edit if an Error is existent.
             */
            int errorCount = 0;
            boolean errorOccurred = false;
            for (boolean error : errors)
            {
                if (error && errorCount == 0)
                {
                    inputLayout.setError("You cannot enter a ',' at the end or start");

                    if (registerActivity != null)

                        registerActivity.setError(true, index);

                    else if (editInfoActivity != null)

                        editInfoActivity.setError(true, index);

                    errorOccurred = true;
                    break;
                }
                else if (error && errorCount == 1)
                {
                    inputLayout.setError("Year has to be less than Today's Date and Greater than 1855");

                    if (registerActivity != null)

                        registerActivity.setError(true, index);

                    else if (editInfoActivity != null)

                        editInfoActivity.setError(true, index);

                    errorOccurred = true;
                    break;
                }
                else if (error && errorCount == 2)
                {
                    if (registerActivity != null)
                    {
                        inputLayout.setError("Rating must be from 0-10");
                        registerActivity.setError(true, index);

                        errorOccurred = true;
                        break;
                    }
                }
                errorCount ++;
            }

            /* Removing all Errors from the TextField and the setting the error property of the
             * Activity to false if there is not error that has occurred
             */

            if (! errorOccurred)
            {

                inputLayout.setErrorEnabled(false);

                if (registerActivity != null)

                    registerActivity.setError(false, index);

                else if (editInfoActivity != null)

                    editInfoActivity.setError(false, index);
            }
        }
    }

    public void beforeTextChanged(CharSequence s,int start, int count, int after) { }

    public void afterTextChanged(Editable editable) { }
}