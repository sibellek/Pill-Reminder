package com.example.Business_Layer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.pill_reminder.R;

public class Iteminfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteminfo);
        String sent=getIntent().getStringExtra("EXTRA_SESSION_ID");
        int id= Integer.parseInt(sent);
        TextView intakeName = (TextView)findViewById(R.id.textView3);
        TextView intakeDate = (TextView)findViewById(R.id.textView4);
        TextView intakeTime = (TextView)findViewById(R.id.textView5);
        intakeName.setText(Prescription.medIntakes.get(id).name);
        intakeDate.setText(Prescription.medIntakes.get(id).date.toString());
        intakeTime.setText(Prescription.medIntakes.get(id).time.toString());
        CheckBox checked = (CheckBox)findViewById(R.id.checkBox);
        if(Prescription.medIntakes.get(id).check){
            checked.setChecked(true);
        }
        checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                Prescription.medIntakes.get(id).check=true;
            }
            }
        });
     }


    public void returnHome(View view) {
        startActivity(new Intent(Iteminfo.this, MainActivity.class));
    }
}


