package ru.bagrusss.tpnotes.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.eventbus.Message;
import ru.bagrusss.tpnotes.services.ServiceHelper;

public final class EditCategoriesActivity extends BaseActivity {

    public static final int CODE = 11;
    public static final int REQUEST_CODE = 301;

    private EditText mCategoryText;
    private CircleImageView mCategoryColor;
    private String mColor;
    private long mID;
    public static final String KEY_COLOR = "COLOR";

    private boolean back = false;

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

        Intent intent = getIntent();
        mAction = intent.getAction();
        if (mAction.equals(BaseActivity.ACTION_EDIT)) {
            mCategoryColor.setImageDrawable(new ColorDrawable(
                    Color.parseColor(mColor = intent.getStringExtra(KEY_COLOR))));
            mCategoryText.setText(intent.getStringExtra(KEY_CATEGORY));
            mID = intent.getLongExtra(KEY_ID, 0);
        } else
            mCategoryColor.setImageDrawable(new ColorDrawable(Color.parseColor(mColor = "#00FFFF")));
        EventBus.getDefault().register(this);
    }


    public void colorPickerDialog() {
        ColorPickerDialog colorPickerDialog =
                ColorPickerDialog.createColorPickerDialog(this, R.style.CustomColorPicker);
        colorPickerDialog.setOnColorPickedListener((color, hexVal) -> {
            mColor = hexVal;
            mCategoryColor.setImageDrawable(new ColorDrawable(color));
        });
        if (mColor == null)
            colorPickerDialog.setHexaDecimalTextColor(Color.parseColor(mColor = "#000000"));
        else colorPickerDialog.setHexaDecimalTextColor(Color.parseColor(mColor));
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
        if (mAction.equals(ACTION_NEW))
            ServiceHelper.saveCategory(this, category, mColor);
        else ServiceHelper.updateCategory(this, mID, category, mColor);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Message msg) {
        if (msg.reqCode == REQUEST_CODE) {
            if (msg.status == Message.OK) {
                onBackPressed();
            } else {
                Toast.makeText(this, R.string.error_name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
