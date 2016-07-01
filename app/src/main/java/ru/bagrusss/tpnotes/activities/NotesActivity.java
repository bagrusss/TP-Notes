package ru.bagrusss.tpnotes.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class NotesActivity extends AppCompatActivity {

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
        mFragmentManager
                .beginTransaction()
                .replace(R.id.container, new NotesFragment(EditNotesActivity.class))
                .commit();
        buildDrawer();
    }

    private void buildDrawer() {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(new AccountHeaderBuilder().withActivity(this).withHeaderBackground(R.drawable.drawer_light).build())
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.notes).withIcon(R.drawable.ic_assignment_black_24dp),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.categories).withIcon(R.drawable.ic_folder_black_24dp),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.ic_settings_black_24dp),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.about).withIcon(R.drawable.ic_info_black_24dp)
                ).withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    mLastPosition = position;
                    int resTitle = 0;
                    Fragment fragment = null;
                    switch (position) {
                        case NOTES_POSITION:
                            resTitle = R.string.notes;
                            fragment = new NotesFragment(EditNotesActivity.class);
                            break;
                        case SETTINGS_POSITION:
                            resTitle = R.string.settings;
                            fragment = new SettingsFragment();
                            break;
                        case ABOUT_POSITION:
                            resTitle = R.string.about;
                            fragment = new AboutFragment();
                            break;
                        case CATEGORIES_POSITION:
                            resTitle = R.string.categories;
                            fragment = new CategoriesFragment(EditCategoriesActivity.class);
                            break;
                        default:
                            return true;
                    }
                    mToolbar.setTitle(resTitle);
                    mFragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    return false;
                })
                .build();
    }

}
