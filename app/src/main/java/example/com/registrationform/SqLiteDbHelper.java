package example.com.registrationform;
import android.util.Log;
import android.app.NotificationManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import android.util.StringBuilderPrinter;

/**
 * Created by tyson_000 on 06/10/2016.
 */

public class SqLiteDbHelper extends SQLiteOpenHelper {

    private static final int KDatabaseVer = 1;
    public static final String KDatabaseName = "RegistrationDb";
    public static final String KTblUsers = "users";
    private static final String KColId = "_id";
    private static final String KTerm = ");";
    private static final String KAutoIncr = " INTEGER PRIMARY KEY AUTOINCREMENT,  ";

    public static final String KName = "NAME";
    public static final String KDateOfBirth="DateOfBirth";
    public static final String KNationality="Nationality";
    public static final String KGender="Gender";
    public static final String KPhoneNbr="PhoneNumber";
    public static final String KEMailAddress="EMailAddress";
    private static final String KPhoto="Photo";


    private static final String KCreateDbSql =
            ("CREATE TABLE IF NOT EXISTS    "
                    + KTblUsers +
                        " (" +
                    KColId +
                    KAutoIncr +
                    KName + " TEXT, " +
                    KDateOfBirth + " TEXT, " +
                    KNationality + " TEXT, " +
                    KGender + " TEXT, " +
                    KPhoneNbr + " TEXT, " +
                    KEMailAddress + " TEXT, " +
                    KPhoto + " BLOB " +
                    KTerm);

    /* Data Model:

        * Name
        * Date of Birth
        * Nationality
        * Gender
        * Address
        * Phone Number
        * E-Mail Address
        * Photo
     */

    @Override
    public void onCreate(SQLiteDatabase aDb) {
        aDb.execSQL(KCreateDbSql);

        Log.d("SqLiteDbHelper.onCreate", ("Path is : " + aDb.getPath()));
        Log.d("SqLiteDbHelper.onCreate", KCreateDbSql);
    }



    @Override
    public void onUpgrade(SQLiteDatabase aDb, int aOldVersion, int aNewVersion) {
        Log.d("SqLiteDbHelper", "onUpgrade() was called...");
    }

    public SqLiteDbHelper(Context aContext)  {

        super(aContext, KDatabaseName, null, KDatabaseVer);
        Log.d("SqLiteDbHelper", aContext.toString());
    }
}
