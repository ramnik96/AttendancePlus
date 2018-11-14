package com.example.ramnik_singh.attendanceplus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView textViewRegister1;
    private Button buttonLogin1;
    private EditText editTextEmail1,editTextPassword1;
    FirebaseAuth mAuth;
    private ProgressBar progressbar1;
    //String text_roleid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ImageView homeimage=findViewById(R.id.homeimage);
        ImageView homeheading=findViewById(R.id.homeheading);
        //int imageResource=getResources().getIdentifier("@drawable/homelogo",null,this.getPackageName());
        int imageResource1=getResources().getIdentifier("@drawable/slide1",null,this.getPackageName());
        //homeimage.setImageResource(imageResource);
        homeheading.setImageResource(imageResource1);
        textViewRegister1=(TextView) findViewById(R.id.textViewRegister1);
        buttonLogin1=(Button) findViewById(R.id.buttonLogin1);

        editTextEmail1=(EditText) findViewById(R.id.editTextEmail1);
        editTextPassword1=(EditText) findViewById(R.id.editTextPassword1);
        mAuth=FirebaseAuth.getInstance();
        progressbar1=(ProgressBar)findViewById(R.id.progressbar1);
        textViewRegister1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        buttonLogin1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userLogin();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //final String u=FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this,StudentDashboard.class));
        }
    }

    private void userLogin(){

        String email=editTextEmail1.getText().toString().trim();
        String password=editTextPassword1.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail1.setError("Email is Required");
            editTextEmail1.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail1.setError("Please enter a valid email");
            editTextEmail1.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword1.setError("Password is Required");
            editTextPassword1.requestFocus();
            return;
        }

        progressbar1.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar1.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent=new Intent(MainActivity.this,StudentDashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                else
                {
                    String errormsg=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),errormsg,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
