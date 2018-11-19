package fr.m2iformation.sqliteannuaire;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// =====  on fixe la BD à annuaire car pas renomable ======
// =====   on fixe la version à 1 à la creation  ==========
public class DbInit extends SQLiteOpenHelper {
    public DbInit(Context ctxt) {
        super(ctxt, "annuaire", null, 1);
    }

    // ========     methode pour creer la BD la 1ere fois et QUE la 1ere fois  ==========
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE contacts(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                ", name TEXT NOT NULL" +
                ", tel TEXT" +
                ")";
        db.execSQL(sql);
    }


    // ========   Methode de mise à jour  qd version fichier <> version constructeur======================
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
