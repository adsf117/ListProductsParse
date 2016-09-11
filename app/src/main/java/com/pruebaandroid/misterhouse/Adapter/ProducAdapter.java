package com.pruebaandroid.misterhouse.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.pruebaandroid.misterhouse.DataObjects.Producto;
import com.pruebaandroid.misterhouse.Ormlite.DBHelperOrm;
import com.pruebaandroid.misterhouse.R;

import java.util.ArrayList;

/**
 * Created by Andres on 09/09/2016.
 */
public class ProducAdapter extends ArrayAdapter<Producto> {

    private static DBHelperOrm dbHelperOrm;
    private static RuntimeExceptionDao<Producto, Integer> daoProduct;

    public ProducAdapter(Context context, ArrayList<Producto> results) {
        super(context, 0, results);

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final View finalConvertView = convertView;
        if (convertView == null) {
            dbHelperOrm = OpenHelperManager.getHelper(getContext(), DBHelperOrm.class);
            daoProduct = dbHelperOrm.getRuntimeExceptionDao(Producto.class);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder();
            holder.itemName = (TextView) convertView.findViewById(R.id.name);
            holder.itemPrice = (TextView) convertView.findViewById(R.id.price);
            holder.itemQuantity = (TextView) convertView.findViewById(R.id.quantity);
            holder.itemImageProduct = (ImageView) convertView.findViewById(R.id.imageproduct);
            holder.itemButtonNewItem = (ImageButton) convertView.findViewById(R.id.btn_new_item);
            holder.imageflagsincro =(ImageView) convertView.findViewById(R.id.flagsincro);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Producto currentProducts = getItem(position);
        holder.itemName.setText(currentProducts.getName());
        holder.itemPrice.setText(currentProducts.getPrice());
        holder.itemQuantity.setText(String.format(convertView.getContext().getString(R.string.quantity), currentProducts.getQuantity()));
        if(currentProducts.getUpdated())
        {
            holder.imageflagsincro.setVisibility(View.VISIBLE);
        }

        Glide.with(convertView.getContext()).
                load(currentProducts.getImageUrl()).
                placeholder(R.drawable.ic_notification_sync).
                error(R.drawable.ic_file_cloud_off).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(holder.itemImageProduct);


        holder.itemButtonNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getContext().getString(R.string.title_popup_newitem));
                builder.setMessage(getContext().getString(R.string.message_popup_newitem));

                // Use an EditText view to get user input.
                final EditText input = new EditText(getContext());
                input.setHint(R.string.description);
                builder.setView(input);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        currentProducts.setUpdated(true);
                        currentProducts.setDescription(value);
                        daoProduct.update(currentProducts);
                        notifyDataSetChanged();
                        return;
                    }
                });
                builder.setNegativeButton(getContext().getString(R.string.btn_no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.create().show();

            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;
        ImageView itemImageProduct;
        ImageButton itemButtonNewItem;
        ImageView imageflagsincro;

    }
}
