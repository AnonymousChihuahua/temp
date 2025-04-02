package com.example.myapplication;











import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etItem, etCost;
    Button btnAdd, btnCalculate;
    Spinner spinnerItems;
    TextView tvTotal;
    DBHelper dbHelper;
    ArrayList<String> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etItem = findViewById(R.id.etItem);
        etCost = findViewById(R.id.etCost);
        btnAdd = findViewById(R.id.btnAdd);
        btnCalculate = findViewById(R.id.btnCalculate);
        spinnerItems = findViewById(R.id.spinnerItems);
        tvTotal = findViewById(R.id.tvTotal);

        dbHelper = new DBHelper(this);
        loadSpinnerData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString().trim();
                String costStr = etCost.getText().toString().trim();

                if (item.isEmpty() || costStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter item and cost", Toast.LENGTH_SHORT).show();
                    return;
                }

                double cost = Double.parseDouble(costStr);
                dbHelper.addItem(item, cost);
                Toast.makeText(MainActivity.this, "Item Added!", Toast.LENGTH_SHORT).show();
                etItem.setText("");
                etCost.setText("");
                loadSpinnerData();
            }
        });

        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItems.add(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double totalCost = dbHelper.getTotalCost(selectedItems);
                tvTotal.setText("Total: ₹" + totalCost);
            }
        });
    }

    private void loadSpinnerData() {
        ArrayList<String> items = dbHelper.getItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerItems.setAdapter(adapter);
    }
}




