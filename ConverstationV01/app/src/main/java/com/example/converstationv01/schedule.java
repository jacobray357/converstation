package com.example.converstationv01;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class schedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


       plans = new ArrayList<Plan>();

        FileInputStream fis = null;
        try {
            fis = openFileInput("ConverStationPlans");
            Scanner scanner = new Scanner(fis);
            scanner.useDelimiter(",");
            while(scanner.hasNext())
            {
                int day = Integer.parseInt(scanner.next());
                int start = Integer.parseInt(scanner.next());
                int duration = Integer.parseInt(scanner.next());
                plans.add(new Plan(day, start, duration));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

       int day = getIntent().getIntExtra("day", -1);
       if(day >= 0)
       {
           int start = getIntent().getIntExtra("start", -1);
           int duration = getIntent().getIntExtra("duration", -1);
           Plan newPlan = new Plan(day, start, duration);
           for(int i = plans.size() - 1; i >= 0; i--)
           {
               if(newPlan.overlaps(plans.get(i)))
               {
                   newPlan.combine(plans.get(i));
                   plans.remove(i);
               }
           }
           plans.add(newPlan);
           savePlans();
       }

       calendarView = findViewById(R.id.calendarView);
       calendarView.setPlans(plans);
       calendarView.setDeleteButton((Button) findViewById(R.id.deleteTime));
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    ArrayList<Plan> plans;
    CalendarView calendarView;


    public void openAddTime(View view)
    {
        Intent intent = new Intent(this, AddTimeActivity.class);
        startActivity(intent);
    }

    public void savePlans()
    {
        System.out.println("Saving . . .");
        String saveData = "";

        for(Plan plan: plans)
        {
            saveData+=plan.getDayInt()+","+plan.getStartInt()+","+plan.getDuration()+",";
        }

        try {
            FileOutputStream fos = openFileOutput("ConverStationPlans", MODE_PRIVATE);
            fos.write(saveData.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePlan(View view)
    {
        Plan plan = calendarView.getSelected();
        if(plan == null) return;
        plans.remove(plan);
        calendarView.setPlans(plans);
        savePlans();
        Toast toast = Toast.makeText(this, "Time Deleted", Toast.LENGTH_LONG);
        toast.show();
    }
}
