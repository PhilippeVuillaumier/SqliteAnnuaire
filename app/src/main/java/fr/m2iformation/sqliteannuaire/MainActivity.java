package fr.m2iformation.sqliteannuaire;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity implements AlertDialog.OnClickListener {

    EditText etSearch, etName, etTel, etId;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etSearch = findViewById(R.id.etSearch);
        etName = findViewById(R.id.etName);
        etTel = findViewById(R.id.etTel);
        etId = findViewById(R.id.etId);
// =============  ces 2 lignes creent la BDD  ===============
        DbInit dbinit = new DbInit(this);
        db = dbinit.getWritableDatabase();
    }

    public void search(View view) {
        String columns[] = {"id", "name", "tel"};  // creation et declaration du tableau all in one
        String nom = etSearch.getText().toString();
        nom = nom.toUpperCase();
        nom = nom.replace("'", "''");     //pour pas avoir de pb avec les ' pour les noms a particule
        String critere = "UPPER(name) = '" + nom + "'";   // upper pour ruser les problemes de MAJ et min
        Cursor cursor = db.query("contacts", columns, critere, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            etId.setText(cursor.getString(  0));
            etName.setText(cursor.getString(1));
            etTel.setText(cursor.getString(2));
        } else {
            Toast.makeText(this, "nom introuvable", Toast.LENGTH_LONG).show();
        }

    }

    public void clear(View view) {
        etId.getText().clear();
        etTel.getText().clear();
        etName.getText().clear();
    }

    public void delete(View view) {
     createAndShowDialog();  // pour avoir la popup oui ou non, et la methode est plus bas
    }

    public void save(View view) {
        ContentValues values = new ContentValues();
        values.put("name", etName.getText().toString());
        values.put("tel", etTel.getText().toString());
        if (etId.getText().toString().isEmpty()) {
            db.insert("contacts", "", values);
        } else {
            String clause = "id = " + etId.getText().toString();
            db.update("contacts", values, clause, null);
        }
    }

    void createAndShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("confirmation suppression");
        builder.setMessage("Voulez vous supprimer ce contact ?");
        builder.setPositiveButton("OUI", this);
        builder.setNegativeButton("NON", this);
        builder.create().show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_NEGATIVE:
                dialog.dismiss();
                break;

            case BUTTON_POSITIVE:
                String clause = "id = " + etId.getText().toString();
                db.delete("contacts", clause, null);
                clear(null);
                dialog.dismiss();
                break;
        }
    }


}
