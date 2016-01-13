package net.agusharyanto.datamahasiswa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.agusharyanto.datamahasiswa.model.Mahasiswa;

public class AddEditActivity extends AppCompatActivity {

    private EditText editTextNIM, editTextNama, editTextJurusan;
    private Button buttonSave, buttonHapus;
    private Mahasiswa mahasiswa;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private String action_flag="add";
   // private ProgressDialog pDialog;

    private String refreshFlag="0";
    private static final String TAG="AddEditActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(AddEditActivity.this);
        db= databaseHelper.getWritableDatabase();
        setContentView(R.layout.activity_add_edit);
        mahasiswa = new Mahasiswa();
        initUI();
        initEvent();
        Intent intent = getIntent();
        if (intent.hasExtra("mahasiswa")) {
            mahasiswa = (Mahasiswa) intent.getSerializableExtra("mahasiswa");
            Log.d(TAG, "Mahasiswa : " + mahasiswa.toString());
            setData(mahasiswa);
            action_flag = "edit";
        }else{
            mahasiswa = new Mahasiswa();
        }
    }

    private void setData(Mahasiswa mahasiswa) {

        editTextNIM.setText(mahasiswa.getNim());
        editTextNama.setText(mahasiswa.getNama());
        editTextJurusan.setText(mahasiswa.getJurusan());
    }

    private void initUI() {
       // pDialog = new ProgressDialog(AddEditActivity.this);
        editTextNIM = (EditText) findViewById(R.id.editTextNIM);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextJurusan = (EditText) findViewById(R.id.editTextJurusan);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonHapus = (Button) findViewById(R.id.buttonHapus);
    }
    private void initEvent() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });
        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusData();
            }
        });
    }

    private void saveData() {

        String nama = editTextNama.getText().toString();
        String nim = editTextNIM.getText().toString();
        String jurusan = editTextJurusan.getText().toString();
        mahasiswa.setNim(nim);
        mahasiswa.setNama(nama);
        mahasiswa.setJurusan(jurusan);
        long rowaffect=0;
        if (action_flag.equals("add")) {
            rowaffect = databaseHelper.insertMahasiswa(mahasiswa, db);
        }else if (action_flag.equals("edit")){
            rowaffect = databaseHelper.updateMahasiswa(mahasiswa, db);
        }
        if (rowaffect > 0){
            Toast.makeText(getBaseContext(), "Save Data Sukses", Toast.LENGTH_SHORT).show();
            refreshFlag="1";
            finish();
        }else{
            Toast.makeText(getBaseContext(), "Save Data Gagal", Toast.LENGTH_SHORT).show();
        }
    }

    private void hapusData() {
        new AlertDialog.Builder(this)
                .setTitle("Data Mahasiswa")
                .setMessage("Hapus Data " + mahasiswa.getNama() + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        databaseHelper.deleteMahasiswa(mahasiswa, db);
                        //hapusDataServer();
                        refreshFlag = "1";
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void finish() {
        System.gc();
        Intent data = new Intent();
        data.putExtra("refreshflag", refreshFlag);
        //  data.putExtra("mahasiswa", mahasiswa);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    public void onDestroy(){
        db.close();
        databaseHelper.close();
        super.onDestroy();
    }

}
