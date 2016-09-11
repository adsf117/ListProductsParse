package com.pruebaandroid.misterhouse.Ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pruebaandroid.misterhouse.DataObjects.Producto;
import com.pruebaandroid.misterhouse.R;

import java.sql.SQLException;

/**
 * Created by Andres on 09/09/2016.
 */
public class DBHelperOrm extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "ProductsDB.db";
    private static final int DATABASE_VERSION = 2;



    //---------------------------- DAO  Producto.---------------------------------
    private Dao<Producto, Integer> ProductyDAO = null;
    private RuntimeExceptionDao<Producto, Integer> ProductRuntimeDAO = null;


    public Dao<Producto, Integer> GetProductDAO() throws SQLException {
        if (ProductyDAO == null)
            ProductyDAO = getDao(Producto.class);
        return ProductyDAO;
    }

    public RuntimeExceptionDao<Producto, Integer> GetProductRuntimeDAO() {
        if (ProductRuntimeDAO == null)
            ProductRuntimeDAO = getRuntimeExceptionDao(Producto.class);
        return ProductRuntimeDAO;

    }

    public DBHelperOrm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        // TODO Auto-generated method stub
        try {

            TableUtils.createTable(connectionSource, Producto.class);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int arg2,
                          int arg3) {
        // TODO Auto-generated method stub
        try {
            TableUtils.dropTable(connectionSource, Producto.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}