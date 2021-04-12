package com.cinemaapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class CustomTextWatcher implements TextWatcher
{
    private final TextInputLayout inputLayout;
    private final RegisterActivity registerActivity;
    private final EditInfoActivity editInfoActivity;
    private boolean[] errors;

    public CustomTextWatcher(TextInputLayout inputLayout, RegisterActivity registerActivity)
    {
        this.inputLayout = inputLayout;
        this.registerActivity = registerActivity;
        editInfoActivity = null;
        errors = new boolean[]{false, false, false};
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
        // First Validation is to check if the TextField is empty
        if (s.toString().equals(""))
        {
            inputLayout.setError("You cannot leave the field blank !!!");
            if (registerActivity != null)

                registerActivity.setError(true);

            else if (editInfoActivity != null)

                editInfoActivity.setError(true);
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

                    if (! commaError)
                    {
                        System.out.println("IO");
                        errors[0] = false;
                    }
                    System.out.println(errors[0]);
                }
            }

            else if (inputLayout.getId() == R.id.yearTextField && chars.length > 0)
            {
                int date = Integer.parseInt(text);
                errors[1] = date < 1855 || date > Calendar.getInstance().get(Calendar.YEAR);
            }
            else if (inputLayout.getId() == R.id.ratingTextField && chars.length > 0)
            {
                int rating = Integer.parseInt(text);
                errors[2] = rating > 10 || rating < 1;
            }

            int errorCount = 0;
            boolean errorOccurred = false;
            for (boolean error : errors)
            {
                if (error && errorCount == 0)
                {
                    inputLayout.setError("You cannot enter a ',' at the end or start");

                    if (registerActivity != null)

                        registerActivity.setError(true);

                    else if (editInfoActivity != null)

                        editInfoActivity.setError(true);

                    errorOccurred = true;
                    break;
                }
                else if (error && errorCount == 1)
                {
                    inputLayout.setError("Year has to be less than Today's Date and Greater than 1855");

                    if (registerActivity != null)

                        registerActivity.setError(true);

                    else if (editInfoActivity != null)

                        editInfoActivity.setError(true);

                    errorOccurred = true;
                    break;
                }
                else if (error && errorCount == 2)
                {
                    inputLayout.setError("Rating must be from 0-10");

                    if (registerActivity != null)

                        registerActivity.setError(true);

                    else if (editInfoActivity != null)

                        editInfoActivity.setError(true);

                    errorOccurred = true;
                    break;
                }
                errorCount ++;
            }

            if (! errorOccurred)
            {
                inputLayout.setErrorEnabled(false);

                if (registerActivity != null)

                    registerActivity.setError(false);

                else if (editInfoActivity != null)

                    editInfoActivity.setError(false);
            }
        }
    }

    public void beforeTextChanged(CharSequence s,int start, int count, int after) { }

    public void afterTextChanged(Editable editable) { }
}