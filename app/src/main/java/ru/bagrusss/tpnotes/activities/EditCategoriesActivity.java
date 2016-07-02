package ru.bagrusss.tpnotes.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.services.ServiceHelper;

public final class EditCategoriesActivity extends BaseActivity {

    public static final int CODE = 11;

    private EditText mCategoryText;
    private CircleImageView mCategoryColor;
    private String mColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        mCategoryText = (EditText) findViewById(R.id.category_text);
        mCategoryColor = (CircleImageView) findViewById(R.id.category_color);
        if (mCategoryColor != null) {
            mCategoryColor.setOnClickListener(v -> colorPickerDialog());
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void colorPickerDialog() {
        ColorPickerDialog colorPickerDialog =
                ColorPickerDialog.createColorPickerDialog(this, R.style.CustomColorPicker);
        colorPickerDialog.setOnColorPickedListener((color, hexVal) -> {
            mColor = hexVal;
            mCategoryColor.setImageDrawable(new ColorDrawable(color));
        });
        colorPickerDialog.setHexaDecimalTextColor(Color.parseColor("#000000"));
        colorPickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_action) {
            save();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void save() {
        String category = mCategoryText.getText().toString();
        ServiceHelper.saveCategory(this, category, mColor);
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }
}
