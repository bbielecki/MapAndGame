package com.jacek.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.database_textView);

        ArrayList<User> users = User.makeUsers();
        UserDbAdapter userDbAdapter = new UserDbAdapter(this);
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
        userDbAdapter.close();
        textView.setText(results.toString());
        Toast.makeText(getApplicationContext(), MD5Hasher.hashWithMD5("elo"), Toast.LENGTH_LONG).show();

    }
}
