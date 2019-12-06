package edu.stanford.converstationv0;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GroupMembersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, PartnerProfile.class);

        startActivity(intent);
    }
}
