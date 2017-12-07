package bencloud.tech.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import bencloud.tech.notes.MainActivity;
import bencloud.tech.notes.R;

public class EditorActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_editor);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Load Values
    Intent intent = getIntent();

    final String complete = intent.getExtras().getString(MainActivity.EXTRA_COMPLETE);
    final TextView completeView = findViewById(R.id.complete);
    completeView.setText(complete);

    final String priority = intent.getExtras().getString(MainActivity.EXTRA_PRIORITY);
    final TextView priorityView = findViewById(R.id.priority);
    priorityView.setText(priority);

    final String creationDate = intent.getExtras().getString(MainActivity.EXTRA_CREATION_DATE);
    final TextView creationDateView = findViewById(R.id.creation_date);
    creationDateView.setText(creationDate);

    final String completionDate =
        intent.getExtras().getString(MainActivity.EXTRA_COMPLETION_DATE);
    final TextView completionDateView = findViewById(R.id.completion_date);
    completionDateView.setText(completionDate);

    final String description = intent.getExtras().getString(MainActivity.EXTRA_DESCRIPTION);
    final TextView descriptionView = findViewById(R.id.description);
    descriptionView.setText(description);

    // Return
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent r = new Intent();
        // old values
        r.putExtra(MainActivity.EXTRA_COMPLETE, complete);
        r.putExtra(MainActivity.EXTRA_PRIORITY, priority);
        r.putExtra(MainActivity.EXTRA_CREATION_DATE, creationDate);
        r.putExtra(MainActivity.EXTRA_COMPLETION_DATE, completionDate);
        r.putExtra(MainActivity.EXTRA_DESCRIPTION, description);
        // new values
        r.putExtra(MainActivity.EXTRA_COMPLETE_NEW, completeView.getText().toString());
        r.putExtra(MainActivity.EXTRA_PRIORITY_NEW, priorityView.getText().toString());
        r.putExtra(MainActivity.EXTRA_CREATION_DATE_NEW, creationDateView.getText().toString());
        r.putExtra(MainActivity.EXTRA_COMPLETION_DATE_NEW, completionDateView.getText().toString());
        r.putExtra(MainActivity.EXTRA_DESCRIPTION_NEW, descriptionView.getText().toString());
        setResult(Activity.RESULT_OK, r);
        finish();
      }
    });
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

}
