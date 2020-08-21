package com.softwaregroup.android.signuppage;

import android.media.MediaCodec;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //SignUp button
        final Button buttonSignUp = findViewById(R.id.button_signup);
        final EditText editTextEmail = findViewById(R.id.edittext_email);
        final EditText editTextPassword = findViewById(R.id.edittext_password);
        final EditText editTextFirst = findViewById(R.id.edittext_firstname);
        final EditText editTextLast = findViewById(R.id.edittext_lastname);
        final TextView textViewErrors = findViewById(R.id.textview_errors);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textViewErrors.setText("");
                boolean passwordValidation = validatePassword(editTextPassword.getText().toString());
                boolean emailValidation = validateEmail(editTextEmail.getText().toString());
                boolean firstNameValidation = !TextUtils.isEmpty(editTextFirst.getText().toString());
                boolean lastNameValidation = !TextUtils.isEmpty(editTextLast.getText().toString());
                formatText(passwordValidation, editTextPassword);
                formatText(emailValidation, editTextEmail);
                formatText(firstNameValidation, editTextFirst);
                formatText(lastNameValidation && firstNameValidation, editTextLast);
                if(UpdateEditText(firstNameValidation, lastNameValidation, emailValidation, passwordValidation, textViewErrors))
                {
                    //TODO - Open Welcome Page  and send to database
                }
            }
        });

        //email text
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* When focus is lost check that the text field
                 * has valid values.
                 */
                if (!hasFocus && !TextUtils.isEmpty(editTextEmail.getText().toString())) {
                    boolean emailValid = validateEmail(editTextEmail.getText().toString());
                    formatText(emailValid, editTextEmail);
                    UpdateEditText(emailValid, validatePassword(editTextPassword.getText().toString()) || TextUtils.isEmpty(editTextPassword.getText().toString()), textViewErrors);
                }
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean emailValid = validateEmail(editTextEmail.getText().toString());
                if(emailValid) {
                    formatText(emailValid, editTextEmail);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //password text
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /* When focus is lost check that the text field
                 * has valid values.
                 */
                if (!hasFocus && !TextUtils.isEmpty(editTextPassword.getText().toString())) {
                    boolean passwordValid = validatePassword(editTextPassword.getText().toString());
                    formatText(passwordValid, editTextPassword);
                    UpdateEditText(validateEmail(editTextEmail.getText().toString()) || TextUtils.isEmpty(editTextEmail.getText().toString()), passwordValid, textViewErrors);
                }
            }
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean passwordValid = validatePassword(editTextPassword.getText().toString());
                if(passwordValid)
                    formatText(passwordValid, editTextPassword);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



        //first name text
   //     editTextFirst.setOnFocusChangeListener(new View.OnFocusChangeListener() {

   //         @Override
   //         public void onFocusChange(View v, boolean hasFocus) {
   //             formatText(!TextUtils.isEmpty(editTextFirst.getText().toString()), editTextFirst);
   //         }
   //     });

        //first name
        editTextFirst.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean firstValid = !TextUtils.isEmpty(editTextFirst.getText().toString());
                if(firstValid)
                    formatText(true, editTextFirst);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //last name
        editTextLast.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean firstValid = !TextUtils.isEmpty(editTextLast.getText().toString());
                if(firstValid)
                    formatText(true, editTextLast);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        //last name text
    //    editTextLast.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
    //        @Override
    //        public void onFocusChange(View v, boolean hasFocus) {
    //            formatText(!TextUtils.isEmpty(editTextLast.getText().toString()), editTextLast);
    //        }
    //    });
    }

    private boolean validateEmail(String email)
    {
        //EMAIL - https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
        if(TextUtils.isEmpty(email)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword(String password)
    {
        //PASSWORD - https://stackoverflow.com/questions/36574183/how-to-validate-password-field-in-android
        if(TextUtils.isEmpty(password)) {
            return false;
        }

        Pattern pattern;
        Matcher matcher;
        //needs to contain number, lower-case, upper-case, special, at least 6 chars
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!?*])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        if(!matcher.matches()) {
            return false;
        }

        return true;
    }

    private boolean UpdateEditText(boolean firstName, boolean lastName, boolean email, boolean password, TextView textViewErrors)
    {
        textViewErrors.setText("");
        if(!UpdateEditText(email, password, textViewErrors)) {
            return false;
        }
        else if (!firstName) {
            textViewErrors.setText(R.string.firstname_error_msg);
            return false;
        } else if (!lastName) {
            textViewErrors.setText(R.string.lastname_error_msg);
            return false;
        }
        return true;
    }


    private boolean UpdateEditText(boolean email, boolean password, TextView textViewErrors)
    {
        textViewErrors.setText("");
        if(!email) {
            textViewErrors.setText(R.string.email_error_msg);
            return false;
        }
        else if(!password)
        {
            textViewErrors.setText(R.string.password_error_msg);
            return false;
        }
        return true;
    }


    private void formatText(boolean isCorrect, EditText editText)
    {
        if(isCorrect) {
            editText.setTextColor(getResources().getColor(R.color.colorBlack));
            editText.setBackgroundResource(R.drawable.rounded_edittext);
        }
        else {
            editText.setTextColor(getResources().getColor(R.color.colorRed));
            editText.setBackgroundResource(R.drawable.rounded_edittext_error);
        }
    }


}
