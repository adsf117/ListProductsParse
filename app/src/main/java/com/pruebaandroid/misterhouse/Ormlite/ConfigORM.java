package com.pruebaandroid.misterhouse.Ormlite;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.pruebaandroid.misterhouse.DataObjects.Producto;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Andres on 09/09/2016.
 */
public class ConfigORM extends OrmLiteConfigUtil{

    private static final Class<?>[] classes = new Class[]{Producto.class};

    public static void main(String[] args) throws SQLException, IOException {
        // TODO Auto-generated method stub
        writeConfigFile("ormlite_config.txt", classes);
    }
}
