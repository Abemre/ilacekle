package com.example.firebasekullanimi;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MedicineAdapter extends ArrayAdapter<Medicine> {

    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(@NonNull Context context, List<Medicine> list) {
        super(context, R.layout.medicine_item, list);
        this.context = context;
        this.medicineList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.medicine_item, parent, false);

        Medicine currentMedicine = medicineList.get(position);

        TextView name = listItem.findViewById(R.id.tvMedicineName);
        name.setText(currentMedicine.getName());

        TextView dose = listItem.findViewById(R.id.tvMedicineDose);
        dose.setText(currentMedicine.getDose());

        ImageView image = listItem.findViewById(R.id.ivMedicineImage);
        Picasso.get().load(currentMedicine.getImageUrl()).into(image);

        return listItem;
    }
}

