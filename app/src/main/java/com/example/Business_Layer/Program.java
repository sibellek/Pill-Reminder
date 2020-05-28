package com.example.Business_Layer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pill_reminder.R;

import Repository.MyConnection;

public class Program extends AppCompatActivity {
    Button btnFetch;
    TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        txtData = (TextView) this.findViewById(R.id.ConStat);
        btnFetch = (Button) findViewById(R.id.btnFetch);


        ArrayList<Intake> arrayOfIntakes = new ArrayList<Intake>();
        IntakesAdapter adapter = new IntakesAdapter(this, Prescription.medIntakes);
        ListView listView =  findViewById(R.id.lvItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Object selectedItem = listView.getItemIdAtPosition(position);
                System.out.println(selectedItem);
                Intent itemInfo = new Intent(Program.this, Iteminfo.class);
                itemInfo.putExtra("EXTRA_SESSION_ID", String.valueOf(selectedItem));
                startActivity(itemInfo);
            }
        });
        btnFetch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Prescription.medIntakes.clear();
                ConnectMySql connectMySql = new ConnectMySql();
                connectMySql.execute("");

            }
        });

    }


    class ConnectMySql extends AsyncTask<String, Void, String> {
        String res = "";
        ArrayList<Medication> medications= new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(Program.this, "Please wait...", Toast.LENGTH_SHORT)
                    .show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = MyConnection.getConnection();
                System.out.println("Databaseection success");

                String result = "Database Connection Successful\n";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT *  FROM Medication WHERE prescriptionId='"+ Prescription.pres+"' ORDER BY time");
                ResultSetMetaData rsmd = rs.getMetaData();

                while (rs.next()) {
                    medications.add(new Medication(rs.getInt(1),rs.getString(2),rs.getTime(6),rs.getInt(3),rs.getDate(4),rs.getDate(5)));

                }
                res = result;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }
            return res;
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {
            int f=1;
            txtData.setText(result);
            AlarmManager[] alarmManager=new AlarmManager[100];
            ArrayList<PendingIntent> intentArray = new ArrayList<>();
            Intent intents[] = new Intent[100];
            PendingIntent pi;
            for (Medication m:medications
                 ) {
                Intake intake1 =new Intake(m.name,m.startDate,m.time);
                intake1.id=0;
                if(m.startDate.compareTo(Calendar.getInstance().getTime())>0) {
                    Prescription.medIntakes.add(intake1);
                }
                long millis = m.startDate.getTime();
                millis= millis  + m.time.getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(millis);
                int skipday=m.skipDays+1;
                Date date=m.startDate;
                millis+=(long)7200000;
                alarmManager[0]= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                intents[0] = new Intent(Program.this, Alarm.class);
                pi = PendingIntent.getBroadcast(Program.this, 0, intents[0], PendingIntent.FLAG_ONE_SHOT);
                if(millis>Calendar.getInstance().getTimeInMillis()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager[0].setExact(AlarmManager.RTC_WAKEUP, millis, pi);
                    }
                    intentArray.add(pi);
                }
                while(date.compareTo(m.endDate)<0) {

                    intake1 = new Intake(m.name, date, m.time);
                    intake1.id = f;
                    if (date.compareTo(Calendar.getInstance().getTime()) > 0) {
                        Prescription.medIntakes.add(intake1);
                    }
                    millis = date.getTime();
                    millis += 86400000 * skipday;
                    date = new Date(millis);
                    millis = millis + m.time.getTime();
                    calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(millis);
                    millis += (long) 7200000;
                    alarmManager[f] = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    intents[f] = new Intent(Program.this, Alarm.class);
                    pi = PendingIntent.getBroadcast(Program.this, f, intents[f], PendingIntent.FLAG_ONE_SHOT);
                    if(millis>Calendar.getInstance().getTimeInMillis()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager[f].setExact(AlarmManager.RTC_WAKEUP, millis, pi);
                    }
                    intentArray.add(pi);
                }
                    f++;
                }
            }

            Comparator<Intake> compareByDate = (Intake i1, Intake i2) -> i1.date.compareTo( i2.date );
            Collections.sort(Prescription.medIntakes, compareByDate);
            finish();
            startActivity(getIntent());
        }
    }
    public class IntakesAdapter extends ArrayAdapter<Intake> {
        public IntakesAdapter(Context context, ArrayList<Intake> intakes) {
            super(context, 0, intakes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Intake intake = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview, parent, false);
            }
            TextView medName = (TextView) convertView.findViewById(R.id.medName);
            TextView medDate = (TextView) convertView.findViewById(R.id.medDate);
            TextView medTime = (TextView) convertView.findViewById(R.id.medTime);
            medName.setText(intake.name);
            medDate.setText(intake.date.toString());
            medTime.setText(intake.time.toString());
            if (intake.check) {
                medName.setTextColor(Color.GREEN);
            }
            return convertView;

        }
    }


}


