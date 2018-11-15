package com.example.ramnik_singh.attendanceplus_student;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

class student{
    String SID;

    public student(String sid){
        this.SID=sid;
    }
}

public class TeacherDashboard extends AppCompatActivity {
    private Button buttonScan;
    private EditText editTextSubject,editTextSemester,editTextTimings;
    String Semester, Subject,Timings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonScan = findViewById(R.id.scan_btn);
        editTextSemester=findViewById(R.id.editTextSemester);
        editTextSubject=findViewById(R.id.editTextSubject);
        editTextTimings=findViewById(R.id.editTextTimings);
        final Activity activity = this;
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                Semester=editTextSemester.getText().toString();
                Subject=editTextSubject.getText().toString();
                Timings=editTextTimings.getText().toString();

                student A=new student(result.getContents().toString());
                FirebaseDatabase.getInstance().getReference().child("AttendanceRegister").child(Subject+"_"+Semester+"_"+Timings).push().setValue(A).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(TeacherDashboard.this, "Attendance Marked",Toast.LENGTH_SHORT).show();

                            buttonScan.performClick();

                        }
                        else{
                            String msg=task.getException().toString();
                            Toast.makeText(TeacherDashboard.this, msg,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

            case R.id.menuProfile:
                finish();
                //startActivity(new Intent(this,StudentProfileActivity.class));
                break;
        }

        return true;
    }
    }

