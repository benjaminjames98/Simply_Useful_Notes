package bencloud.tech.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import java.io.File;

import bencloud.tech.notes.recycler.RecyclerPacket;
import bencloud.tech.notes.recycler.RecyclerFacade;
import bencloud.tech.notes.storage.LocalDatabaseStrategy;
import bencloud.tech.notes.storage.StorageStrategy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {

  // TODO fab filter

  private static final int REQUEST_IMPORT = 1000;
  private static final int REQUEST_EDITOR = 1001;
  public static final String EXTRA_COMPLETE = "bencloud.tech.complete";
  public static final String EXTRA_PRIORITY = "bencloud.tech.priority";
  public static final String EXTRA_CREATION_DATE = "bencloud.tech.creation_date";
  public static final String EXTRA_COMPLETION_DATE = "bencloud.tech.completion_date";
  public static final String EXTRA_DESCRIPTION = "bencloud.tech.description";
  public static final String EXTRA_COMPLETE_NEW = "bencloud.tech.complete_new";
  public static final String EXTRA_PRIORITY_NEW = "bencloud.tech.priority_new";
  public static final String EXTRA_CREATION_DATE_NEW = "bencloud.tech.creation_date_new";
  public static final String EXTRA_COMPLETION_DATE_NEW = "bencloud.tech.completion_date_new";
  public static final String EXTRA_DESCRIPTION_NEW = "bencloud.tech.description_new";

  private RecyclerFacade recyclerFacade;
  private StorageStrategy storageStrategy;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String creationDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
        openEditorActivity(new RecyclerPacket("", "", creationDate, "", ""));
      }
    });

    recyclerFacade = new RecyclerFacade(this);
    storageStrategy = new LocalDatabaseStrategy(this);
    recyclerFacade.setStorageStrategy(storageStrategy);
    recyclerFacade.refresh();

    MobileAds.initialize(this, "ca-app-pub-7451185413780850~5033131887");

    AdView mAdView = findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    /*if (id == R.id.action_settings) {
      // TODO settings
    } else if (id == R.id.action_import) {
      Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
      chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
      chooseFile.setType("text/plain");
      startActivityForResult(Intent.createChooser(chooseFile, "choose my file"), REQUEST_IMPORT);
      // TODO import
      return true;
    } else*/
    if (id == R.id.action_filter) {
      // Shows popup to allow user to set filter settings
      recyclerFacade.showFilterDialogue();
    }

    return super.onOptionsItemSelected(item);
  }

  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMPORT && resultCode == RESULT_OK) {
      Uri uri = data.getData();
      if (uri == null) {
        return;
      }
      String src = uri.getPath();
      File source = new File(src);
    } else if (requestCode == REQUEST_EDITOR && resultCode == RESULT_OK) {
      Bundle b = data.getExtras();
      if (b == null) {
        return;
      }
      // old data
      String complete = b.getString(MainActivity.EXTRA_COMPLETE);
      String priority = b.getString(MainActivity.EXTRA_PRIORITY);
      String creationDate = b.getString(MainActivity.EXTRA_CREATION_DATE);
      String completionDate = b.getString(MainActivity.EXTRA_COMPLETION_DATE);
      String description = b.getString(MainActivity.EXTRA_DESCRIPTION);
      storageStrategy.deleteItem(complete, priority, creationDate, completionDate, description);
      // new data
      String completeN = b.getString(MainActivity.EXTRA_COMPLETE_NEW);
      String priorityN = b.getString(MainActivity.EXTRA_PRIORITY_NEW);
      String creationDateN = b.getString(MainActivity.EXTRA_CREATION_DATE_NEW);
      String completionDateN = b.getString(MainActivity.EXTRA_COMPLETION_DATE_NEW);
      String descriptionN = b.getString(MainActivity.EXTRA_DESCRIPTION_NEW);
      storageStrategy
          .createItem(completeN, priorityN, creationDateN, completionDateN, descriptionN);
      recyclerFacade.refresh();
    }
  }

  public void openEditorActivity(RecyclerPacket p) {
    Intent intent = new Intent(this, EditorActivity.class);
    intent.putExtra(EXTRA_COMPLETE, p.getComplete());
    intent.putExtra(EXTRA_PRIORITY, p.getPriority());
    intent.putExtra(EXTRA_CREATION_DATE, p.getCreationDate());
    intent.putExtra(EXTRA_COMPLETION_DATE, p.getCompletionDate());
    intent.putExtra(EXTRA_DESCRIPTION, p.getDescription());
    startActivityForResult(intent, REQUEST_EDITOR);
  }
}
