package com.jacek.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText name_editText;
    private EditText password_editText;
    private Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.database_textView);
        name_editText = (EditText) findViewById(R.id.name_editText);
        password_editText = (EditText) findViewById(R.id.password_editText);
        login_button = (Button) findViewById(R.id.login_button);

        ArrayList<User> users = User.makeUsers();
        final UserDbAdapter userDbAdapter = new UserDbAdapter(this);
        userDbAdapter.open();
        for (User user : users){
            ContentValues newValues = new ContentValues();
            newValues.put(UserDbAdapter.NAME, user.getName());
            newValues.put(UserDbAdapter.PASSWORD, user.getPassword());
            newValues.put(UserDbAdapter.SURNAME, user.getSurname());
            newValues.put(UserDbAdapter.POINTS, user.getPoints());
            newValues.put(UserDbAdapter.AGE, user.getAge());
            newValues.put(UserDbAdapter.WEIGHT, user.getWeight());
            newValues.put(UserDbAdapter.HEIGHT, user.getHeight());
            newValues.put(UserDbAdapter.LEVEL, user.getLevel());
            userDbAdapter.insertUser(newValues);
        }

        Cursor userCursor = userDbAdapter.getUsers();
        StringBuilder results = new StringBuilder();
        if(userCursor.moveToFirst()){
            do{
                User user = userDbAdapter.getUserFromCursor(userCursor);
                results.append(user.getId() + " " + user.getName() + " " + user.getSurname() + " " + user.getPassword() + " "
                + " " + user.getPoints() + " " + user.getAge() + " " + user.getWeight()
                + " " + user.getHeight() + " " + user.getLevel() + "\n");
            } while (userCursor.moveToNext());
        }
        userCursor.close();
        textView.setText(results.toString());
        Toast.makeText(getApplicationContext(), MD5Hasher.hashWithMD5("elo"), Toast.LENGTH_LONG).show();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean logged = userDbAdapter.loginUser(String.valueOf(name_editText.getText()), MD5Hasher.hashWithMD5(String.valueOf(password_editText.getText())));
                if(logged) Toast.makeText(getApplicationContext(), "LOGGED", Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(), "NOT LOGGED", Toast.LENGTH_LONG).show();
            }
        });

    }


}
