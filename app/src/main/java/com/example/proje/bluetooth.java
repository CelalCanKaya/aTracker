package com.example.proje;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class bluetooth extends MenuBar {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        super.menuBar();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                //Verileri yazmak için. Eğer o isimde bir entry yoksa oluşturuyor.
                for(int i=0; i<5; i++){
                    mDatabase.child("sensor").child("entry" + i).child("x_axis").setValue(i+1);
                    mDatabase.child("sensor").child("entry" + i).child("y_axis").setValue(i+1);
                    mDatabase.child("sensor").child("entry" + i).child("z_axis").setValue(i+1);
                    mDatabase.child("sensor").child("entry" + i).child("_class").setValue(3);

                }

                //Verileri okumak için
                mDatabase = FirebaseDatabase.getInstance().getReference().child("sensor");

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot dataSnapshot1 = iterator.next();
                            Entry post = dataSnapshot1.getValue(Entry.class);
                            System.out.println(post.x_axis);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        });


    }
}
