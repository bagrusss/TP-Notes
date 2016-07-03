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
public class NotesAdapter extends CursorAdapter {

    public NotesAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        ViewHolder holder = (ViewHolder) v.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.firstString = (TextView) v.findViewById(R.id.note_first_text);
            holder.category = (TextView) v.findViewById(R.id.category_text);
            holder.color = (CircleImageView) v.findViewById(R.id.color_category);
            v.setTag(holder);
        }
        holder.firstString.setText(c.getString(c.getColumnIndex(HelperDB.FIRST_STRING)));
        holder.category.setText(c.getString(c.getColumnIndex(HelperDB.CATEGORY)));
        holder.name = c.getString(c.getColumnIndex(HelperDB.NAME));
        holder.color.setImageDrawable(new ColorDrawable(
                Color.parseColor(c.getString(c.getColumnIndex(HelperDB.COLOR)))));
    }

    public static class ViewHolder {
        TextView firstString;
        public TextView category;
        CircleImageView color;
        public String name;
    }
}
