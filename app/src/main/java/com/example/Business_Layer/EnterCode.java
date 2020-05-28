package com.example.Business_Layer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pill_reminder.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Repository.MyConnection;

public class EnterCode extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
    }

    public void checkCode(View view) {
        ConnectMySql connectMySql = new ConnectMySql();
        connectMySql.execute("");

    }


    class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(EnterCode.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        int code = Integer.parseInt(((EditText) findViewById(R.id.textInputEditText)).getText().toString());

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con= MyConnection.getConnection();
                TextView status = (TextView)findViewById(R.id.textView);
                status.setText("Database Connection Successful");

                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                PreparedStatement ps =
                        con.prepareStatement
                                ("SELECT * FROM `Prescription` WHERE `QRcode` = ?");
                ps.setInt(1, code);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    status.setText("Prescription found");
                    Prescription.pres=rs.getInt(1);
                    System.out.println(Prescription.pres);





                } else {
                    status.setText("Prescription not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }


        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);

        }
    }
}
