package com.example.firebasekullanimi;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ListMedicineActivity extends AppCompatActivity {

    private ListView lvMedicines;
    private List<Medicine> medicineList;
    private MedicineAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference medicinesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_medicine);

        lvMedicines = findViewById(R.id.lvMedicines); // lvMedicines değişkenini tanımla
        medicineList = new ArrayList<>();
        adapter = new MedicineAdapter(this, medicineList);
        lvMedicines.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        medicinesRef = database.getReference("medicines");

        medicinesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Medicine medicine = postSnapshot.getValue(Medicine.class);
                    if (medicine != null) {
                        medicineList.add(medicine);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

