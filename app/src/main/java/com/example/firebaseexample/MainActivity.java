package com.example.firebaseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    Button submitButton;
    EditText studentId,firstName, lastName,
             studentAge;
    String idNum,fName,lName,gender,
           age,city,operation;
    RadioButton btnGender,btnOper;
    RadioGroup operRadioGroup,
               genderRadioGroup;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentId = findViewById(R.id.StudentId);
        firstName = findViewById(R.id.FirstName);
        lastName  = findViewById(R.id.FamilyName);
        studentAge  = findViewById(R.id.Age);
        operRadioGroup = findViewById(R.id.Operation);
        genderRadioGroup = findViewById(R.id.Gender);
        spinner = findViewById(R.id.City);

        //Setting Firebase database and reference
        database = FirebaseDatabase.getInstance();
        //dbRef = database.getReferenceFromUrl("https://fir-example-3050c.firebaseio.com");
        dbRef = FirebaseDatabase.getInstance().getReference().child("Student");

        String[] array = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_layout,R.id.textCity,array);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                city = spinner.getSelectedItem().toString();
                Toast.makeText(MainActivity.this,city,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        submitButton = findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId1 = genderRadioGroup.getCheckedRadioButtonId();
                int selectedId2 = operRadioGroup.getCheckedRadioButtonId();
                btnGender = findViewById(selectedId1);
                btnOper = findViewById(selectedId2);


                if(selectedId1 == -1){
                    Toast.makeText(MainActivity.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    gender = btnGender.getText().toString();
                }

                if(selectedId2 == -1){
                    Toast.makeText(MainActivity.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                }
                else{
                    operation = btnOper.getText().toString();
                    switch (selectedId2){
                        case R.id.btnView:
                            viewStudent();
                            break;
                        case R.id.btnInsert:
                            insertStudent();
                            break;
                        case R.id.btnUpdate:
                            updateStudent();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    public void onOperationClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.btnView:
                if (checked)
                    studentId.setEnabled(true);
                    break;
            case R.id.btnInsert:
                if (checked)
                    studentId.setEnabled(true);
                    break;
            case R.id.btnUpdate:
                if (checked)
                    studentId.setEnabled(false);
                break;
        }
    }

    public void getInputData()
    {
        idNum = studentId.getText().toString();
        fName = firstName.getText().toString();
        lName = lastName.getText().toString();
        age = studentAge.getText().toString();
    }

    public void insertStudent()
    {
        getInputData();

        // Declaring student class object.
        Student student = new Student();
        student.setFirstName(fName);
        student.setFamilyName(lName);
        student.setAge(age);
        student.setCity(city);
        student.setGender(gender);

        ///Passing student data into firebase reference object
        // to insert into database.
        dbRef.child(idNum).setValue(student);
    }

    public void viewStudent(){
        idNum = studentId.getText().toString();
        DatabaseReference ref = dbRef.child(idNum);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);

                firstName.setText(student.getFirstName());
                lastName.setText(student.getFamilyName());
                studentAge.setText(student.getAge());

                 spinner = findViewById(R.id.City);
                for (int i=0; i<spinner.getAdapter().getCount(); i++)
                       if ((spinner.getAdapter().getItem(i).toString()).equals(
                               student.getCity()))
                         spinner.setSelection(i,true);

                for (int i=0; i<genderRadioGroup.getChildCount(); i++){
                    btnGender = (RadioButton)genderRadioGroup.getChildAt(i);
                    if (btnGender.getText().equals(student.getGender()))
                        btnGender.setChecked(true);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateStudent()
    {
        getInputData();

        // Declaring student class object.
        Student student = new Student();
        student.setFirstName(fName);
        student.setFamilyName(lName);
        student.setAge(age);
        student.setCity(city);
        student.setGender(gender);

        //Passing student data into firebase reference object
        // to update student record into database.
        dbRef.child(idNum).setValue(student);
    }

    public void clearValues()
    {
        firstName.setText("");
        lastName.setText("");
        studentAge.setText("");
        btnGender = findViewById(R.id.btnMale);
        spinner = findViewById(R.id.City);
        btnGender.setChecked(true);
        spinner.setSelection(0,true);
    }
}
