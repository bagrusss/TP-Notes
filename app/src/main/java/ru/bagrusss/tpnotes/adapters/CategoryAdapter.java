package ru.bagrusss.tpnotes.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.data.HelperDB;

/**
 * Created by bagrusss.
 */
public class CategoryAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    public CategoryAdapter(Context context, Cursor c) {
        super(context, c, false);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.item_category, parent, false);
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.text = (TextView) v.findViewById(R.id.text_category);
            holder.color = (CircleImageView) v.findViewById(R.id.color_category);
        }
        holder.text.setText(c.getString(c.getColumnIndex(HelperDB.NAME)));
        holder.color.setImageDrawable(new ColorDrawable(
                Color.parseColor(c.getString(c.getColumnIndex(HelperDB.COLOR)))));
    }

    public static class ViewHolder {
        public TextView text;
        public CircleImageView color;
    }
}
