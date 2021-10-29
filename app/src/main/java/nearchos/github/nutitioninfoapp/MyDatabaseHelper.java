package nearchos.github.nutitioninfoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "FoodLibrary.db";
    private static final int DATABASE_VERSION = 7;

    //Columns of product table
    private static final String TABLE1_NAME = "products";
    private static final String COLUMN_CODE = "_code";                  //Primary
    private static final String COLUMN_NAME = "_name";
    private static final String COLUMN_GRADE = "_grade";
    private static final String COLUMN_GROUP = "nova_group";
    private static final String COLUMN_INGREDIENTS = "_ingredients";
    private static final String COLUMN_NUTRIENTS = "_nutrients";
    private static final String COLUMN_SPINNER = "_spinner";

    //Columns of list table
    private static final String TABLE2_NAME = "lists";
    private static final String COLUMN_ID = "_id";                       //Primary
    private static final String COLUMN_LIST_NAME = "list_name";
    private static final String COLUMN_DESCRIPTION = "_description";
    private static final String COLUMN_FAVOURITES = "_favourites";
    private static final String COLUMN_COLOUR = "_colour";

    //Columns of products_to_lists table
    private static final String TABLE3_NAME = "products_to_lists";
    private static final String COLUMN_PRODUCT_CODE = "_product_code";
    private static final String COLUMN_LIST_ID = "_list_product_is_in";

    //Create first table
    String query_table1 =
            "CREATE TABLE " + TABLE1_NAME +
                    " (" + COLUMN_CODE + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_GRADE + " TEXT, " +
                    COLUMN_GROUP + " INTEGER, " +
                    COLUMN_INGREDIENTS + " TEXT, " +
                    COLUMN_NUTRIENTS + " TEXT, " +
                    COLUMN_SPINNER + " TEXT);";

    //Create second table
    String query_table2 =
            "CREATE TABLE " + TABLE2_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LIST_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_FAVOURITES + " INTEGER, " +
                    COLUMN_COLOUR + " INTEGER);";

    //Create third table
    String query_table3 =
            "CREATE TABLE " + TABLE3_NAME +
                    " (" + COLUMN_PRODUCT_CODE + " TEXT, "
                    + COLUMN_LIST_ID + " INTEGER, " +
                    " FOREIGN KEY (" + COLUMN_LIST_ID + ") REFERENCES " + TABLE2_NAME + "(" + COLUMN_ID + "), "
                    + " FOREIGN KEY (" + COLUMN_PRODUCT_CODE + ") REFERENCES " + TABLE1_NAME + "(" + COLUMN_CODE + "));";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query_table1);
        db.execSQL(query_table2);
        db.execSQL(query_table3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE1_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3_NAME);
        onCreate(db);
    }

    void addProduct(String code, String name, String grade, int novaGroup, String nutrients, String ingredients, String spinner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CODE, code);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_GRADE, grade);
        cv.put(COLUMN_GROUP, novaGroup);
        cv.put(COLUMN_INGREDIENTS, ingredients);
        cv.put(COLUMN_NUTRIENTS, nutrients);
        cv.put(COLUMN_SPINNER, spinner);

        long result = db.insert(TABLE1_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllProductData() {
        String query = "SELECT * FROM " + TABLE1_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String code, String name, String grade, int novaGroup, String nutrients, String ingredients, String spinner) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CODE, code);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_GRADE, grade);
        cv.put(COLUMN_GROUP, novaGroup);
        cv.put(COLUMN_INGREDIENTS, ingredients);
        cv.put(COLUMN_NUTRIENTS, nutrients);
        cv.put(COLUMN_SPINNER, spinner);

        long result = db.update(TABLE1_NAME, cv, "_code=?", new String[]{code});
        if (result == -1) {
            Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();

        }

    }

    void deleteProduct(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE1_NAME, "_code=?", new String[]{code});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();

        }
    }

    void deleteAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE1_NAME);
        db.close();
    }

    //List operations
    void addList(String listName, String description, int favourites, int colour) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LIST_NAME, listName);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_FAVOURITES, favourites);
        cv.put(COLUMN_COLOUR, colour);

        long result = db.insert(TABLE2_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllListData() {
        String query = "SELECT * FROM " + TABLE2_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateList(int id, String listName, String description, int favourites, int colour) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LIST_NAME, listName);
        cv.put(COLUMN_DESCRIPTION, description);
        cv.put(COLUMN_FAVOURITES, favourites);
        cv.put(COLUMN_COLOUR, colour);

        long result = db.update(TABLE2_NAME, cv, "_id=?", new String[]{String.valueOf(id)});
        if (result == -1) {
            Toast.makeText(context, "Failed to update", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_LONG).show();

        }

    }


    void deleteList(String listId) {
        ArrayList<String> name = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<Integer> favourites = new ArrayList<>();
        ArrayList<Integer> colour = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE2_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(0) != Integer.parseInt(listId)) {
                    name.add(cursor.getString(1));
                    description.add(cursor.getString(2));
                    favourites.add(cursor.getInt(3));
                    colour.add(cursor.getInt(4));
                }
            }
        }

        db.execSQL("DELETE FROM " + TABLE2_NAME);
        /// db.rawQuery("DELETE FROM sqlite_sequence WHERE name='lists'", null);
        db.execSQL("DELETE FROM sqlite_sequence");
        db.close();

        for (String element : name) {
            addList(element, description.get(name.indexOf(element)), favourites.get(name.indexOf(element)), colour.get(name.indexOf(element)));
        }
        /*
           long result = db.delete(TABLE2_NAME, "_id=?", new String[]{listId});
        db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{TABLE2_NAME});

        if(result == -1){
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();

        }
        */
    }

    void deleteAllLists() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE2_NAME);
        // db.rawQuery("DELETE FROM sqlite_sequence WHERE name='lists'", null);
        db.execSQL("DELETE FROM sqlite_sequence");
        db.close();
    }

    //Mapping

    void addMapping(int listId, String productCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LIST_ID, listId);
        cv.put(COLUMN_PRODUCT_CODE, productCode);

        long result = db.insert(TABLE3_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show();
        }
    }




    Cursor readAllMapData() {
        String query = "SELECT * FROM " + TABLE3_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteMappingForList(int listId) {

        String query = "SELECT _list_product_is_in FROM " + TABLE3_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if (listId == cursor.getInt(0)) {
                    db.delete(TABLE3_NAME, "_list_product_is_in=?", new String[]{String.valueOf(listId)});
                }

            }
        }

        cursor.close();
    }


    void updateMapping() {
        ArrayList<String> code = new ArrayList<>();


        String query2 = "SELECT _code FROM " + TABLE1_NAME;
        SQLiteDatabase db2 = this.getReadableDatabase();

        Cursor cursor2 = null;
        if (db2 != null) {
            cursor2 = db2.rawQuery(query2, null);
        }

        if (cursor2.getCount() == 0) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor2.moveToNext()) {
                code.add(cursor2.getString(0));

            }
        }

        cursor2.close();

        String query = "SELECT _product_code FROM " + TABLE3_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        boolean isPresent = false;
        if (cursor.getCount() == 0) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                isPresent = false;
                for (int i = 0; i <= code.size() - 1; i++) {
                    if (cursor.getString(0).equals(code.get(i))) {
                        isPresent = true;
                        break;
                    }

                }

                if (!isPresent) {
                    db.delete(TABLE3_NAME, "_product_code=?", new String[]{cursor.getString(0)});
                }
            }

        }

        cursor.close();


        //For deleted lists
        ArrayList<String> id = new ArrayList<>();
        ArrayList<String> idInListMaps = new ArrayList<>();

        String query3 = "SELECT _id FROM " + TABLE2_NAME;
        SQLiteDatabase db3 = this.getReadableDatabase();

        Cursor cursor3 = null;
        if (db3 != null) {
            cursor3 = db3.rawQuery(query3, null);
        }

        if (cursor3.getCount() == 0) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor3.moveToNext()) {
                id.add((String.valueOf(cursor3.getInt(0))));

            }
        }

        cursor3.close();


        String query4 = "SELECT _list_product_is_in FROM " + TABLE3_NAME;
        SQLiteDatabase db4 = this.getWritableDatabase();

        Cursor cursor4 = null;

        if (db4 != null) {
            cursor4 = db4.rawQuery(query4, null);
        }

        if (cursor4.getCount() == 0) {
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor4.moveToNext()) {
                idInListMaps.add(String.valueOf(cursor4.getInt(0)));
            }
        }

        cursor4.close();

//if listinmap greater

        boolean isListPresentHere = false;
        for (int i = 0; i <= idInListMaps.size() - 1; i++) {
            isListPresentHere = false;
            for (int j = 0; j <= id.size() - 1; j++) {
                if (idInListMaps.get(i).equals(id.get(j))) {
                    isListPresentHere = true;
                    break;
                }
            }

            if (!isListPresentHere) {
                db4.delete(TABLE3_NAME, "_list_product_is_in=?", new String[]{idInListMaps.get(i)});
            }

        }


    }


}