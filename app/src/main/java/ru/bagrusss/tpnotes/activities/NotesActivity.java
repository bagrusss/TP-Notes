package ru.bagrusss.tpnotes.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import ru.bagrusss.tpnotes.R;
import ru.bagrusss.tpnotes.fragments.AboutFragment;
import ru.bagrusss.tpnotes.fragments.CategoriesFragment;
import ru.bagrusss.tpnotes.fragments.NotesFragment;
import ru.bagrusss.tpnotes.fragments.SettingsFragment;

public class NotesActivity extends BaseActivity {

    private FragmentManager mFragmentManager;
    private Drawer mDrawer;
    private Toolbar mToolbar;
    private int mLastPosition;

    private static final int NOTES_POSITION = 1;
    private static final int CATEGORIES_POSITION = 3;
    private static final int SETTINGS_POSITION = 5;
    private static final int ABOUT_POSITION = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mFragmentManager = getFragmentManager();
        Object lastFragment = getLastCustomNonConfigurationInstance();
        buildDrawer();
        if (lastFragment != null) {
            changeFragment((Integer) lastFragment);
            mDrawer.setSelectionAtPosition(mLastPosition);
        } else changeFragment(NOTES_POSITION);
    }

    private void buildDrawer() {
        TypedArray ta = obtainStyledAttributes(mThemeId,
                new int[]{
                        R.attr.drawerHeader,
                        R.attr.notesIcon,
                        R.attr.categoriesIcon,
                        R.attr.settingsIcon,
                        R.attr.aboutIcon});
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(new AccountHeaderBuilder()
                        .withActivity(this)
                        .withHeaderBackground(ta.getResourceId(0, 0))
                        .build())
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.notes)
                                .withIcon(ta.getResourceId(1, R.drawable.ic_notes_black)),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.categories)
                                .withIcon(ta.getResourceId(2, R.drawable.ic_categories_black)),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.settings)
                                .withIcon(ta.getResourceId(3, R.drawable.ic_settings_black)),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.about)
                                .withIcon(ta.getResourceId(4, R.drawable.ic_info_black))
                ).withOnDrawerItemClickListener(
                        (view, position, drawerItem) -> changeFragment(position))
                .build();
        ta.recycle();
    }

    private boolean changeFragment(int pos) {
        mLastPosition = pos;
        int resTitle;
        Fragment lastFragment;
        switch (pos) {
            case NOTES_POSITION:
                resTitle = R.string.notes;
                lastFragment = NotesFragment.newInstance(EditNotesActivity.CODE);
                break;
            case SETTINGS_POSITION:
                resTitle = R.string.settings;
                lastFragment = new SettingsFragment();
                break;
            case ABOUT_POSITION:
                resTitle = R.string.about;
                lastFragment = new AboutFragment();
                break;
            case CATEGORIES_POSITION:
                resTitle = R.string.categories;
                lastFragment = CategoriesFragment.newInstance(EditCategoriesActivity.CODE);
                break;
            default:
                return true;
        }
        mToolbar.setTitle(resTitle);
        mFragmentManager.beginTransaction()
                .replace(R.id.container, lastFragment).commit();
        return false;
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mLastPosition;
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void save() {

    }

}
