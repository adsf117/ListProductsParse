package com.pruebaandroid.misterhouse.DataObjects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Andres on 09/09/2016.
 */
@DatabaseTable
public class Producto {

    @DatabaseField(generatedId = true)
    private int idproducto = 0;

    public static String OBJETCID_FIELD_NAME="objectId";
    @DatabaseField
    private String objectId;

    @DatabaseField
    private String name;

    @DatabaseField
    private String price;

    @DatabaseField
    private String imageUrl;

    @DatabaseField
    private String quantity;

    public static String DESCRIPTION_FIELD_NAME="description";
    @DatabaseField
    private String description;

    @DatabaseField
    private Boolean updated =false;

    @DatabaseField
    private String updatedAt;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public void setUpdated(Boolean updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }
}
