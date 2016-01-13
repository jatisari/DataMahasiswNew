package net.agusharyanto.datamahasiswa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.agusharyanto.datamahasiswa.adapter.MahasiswaArrayAdapter;
import net.agusharyanto.datamahasiswa.model.Mahasiswa;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listViewMahasiswa;
    private Button buttonTambah;
    private MahasiswaArrayAdapter adapter;
    private ArrayList<Mahasiswa> mahasiswaList = new ArrayList<Mahasiswa>();
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Mahasiswa mahasiswa;
    private int selectedPosition=0;
    private ProgressDialog pDialog;

    private static  final int REQUEST_CODE_ADD =1;
    private static  final int REQUEST_CODE_EDIT =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mahasiswa = new Mahasiswa();
        databaseHelper = new DatabaseHelper(MainActivity.this);
        listViewMahasiswa = (ListView) findViewById(R.id.listViewMahasiswa);
        buttonTambah = (Button) findViewById(R.id.buttonAdd);
        //ambildatadari database;
        db = databaseHelper.getWritableDatabase();
        mahasiswaList = databaseHelper.getDataMahasiswa(db);
        gambarDatakeListView();
        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTambahData();
            }
        });

    }

    private void gambarDatakeListView() {
        adapter = new MahasiswaArrayAdapter(this,
                R.layout.rowmahasiswa, mahasiswaList);
        listViewMahasiswa.setAdapter(adapter);

        listViewMahasiswa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mahasiswa = (Mahasiswa) parent.getAdapter().getItem(position);
                // selectedPosition = position;
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("mahasiswa", mahasiswa);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

    }

    private  void openTambahData(){
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                        mahasiswaList = databaseHelper.getDataMahasiswa(db);
                        gambarDatakeListView();

                    }
                }
                break;
            }
            case REQUEST_CODE_EDIT: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                           mahasiswaList = databaseHelper.getDataMahasiswa(db);
                      gambarDatakeListView();

                    }
                }
                break;
            }

        }
    }

    @Override
    public void onDestroy(){
        db.close();
        databaseHelper.close();
        super.onDestroy();
    }

}
