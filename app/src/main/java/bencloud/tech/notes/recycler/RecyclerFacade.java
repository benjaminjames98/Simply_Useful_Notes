package bencloud.tech.notes.recycler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import bencloud.tech.notes.Filter;
import bencloud.tech.notes.recycler.RecyclerAdapter.RecyclerHolder;
import java.util.ArrayList;
import java.util.List;

import bencloud.tech.notes.MainActivity;
import bencloud.tech.notes.R;
import bencloud.tech.notes.storage.StorageStrategy;

public class RecyclerFacade {

  private final Activity activity;
  private final List<RecyclerPacket> recyclerPacketList;
  private final RecyclerAdapter adapter;
  private StorageStrategy storageStrategy;
  private Filter filter;

  public RecyclerFacade(final Activity act) {
    activity = act;
    recyclerPacketList = new ArrayList<>();
    adapter = new RecyclerAdapter(recyclerPacketList);

    adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(final RecyclerPacket p, final RecyclerHolder h, int code) {
        if (code == CLICK_CODE_EXPAND) {
          if (h.edit.getVisibility() == View.VISIBLE) {
            h.edit.setVisibility(View.GONE);
            h.delete.setVisibility(View.GONE);
          } else if (h.edit.getVisibility() == View.GONE) {
            h.edit.setVisibility(View.VISIBLE);
            h.delete.setVisibility(View.VISIBLE);
          }
        } else if (code == CLICK_CODE_EDIT) {
          ((MainActivity) act).openEditorActivity(p);
        } else if (code == CLICK_CODE_DELETE) {
          new AlertDialog.Builder(act)
              .setTitle("Deletion Confirmation")
              .setMessage("I want to delete this?")
              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                  storageStrategy.deleteItem(p.getComplete(), p.getPriority(), p.getCreationDate(),
                      p.getCompletionDate(), p.getDescription());
                  refresh();
                }
              })
              .setNegativeButton(android.R.string.no, null).show();
        }
      }
    });

    RecyclerView recyclerView = act.findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(act));
    recyclerView.setAdapter(adapter);
    resetFilter();
  }

  public void refresh() {
    if (storageStrategy == null) {
      return;
    }

    recyclerPacketList.clear();
    List<RecyclerPacket> list = storageStrategy.readCollection();
    if (list == null) {
      return;
    }
    if (filter == null) {
      resetFilter();
    }
    List<RecyclerPacket> filteredList = filter.applyFilter(list);
    recyclerPacketList.addAll(filteredList);
    adapter.notifyDataSetChanged();
  }

  public void setStorageStrategy(StorageStrategy storageStrategy) {
    this.storageStrategy = storageStrategy;
  }

  public void showFilterDialogue() {
    AlertDialog.Builder b = new AlertDialog.Builder(activity);
    LayoutInflater i = activity.getLayoutInflater();

    final View v = i.inflate(R.layout.filter_layout, null);
    if (filter != null) {
      ((TextView) v.findViewById(R.id.project)).setText(filter.getProject());
      ((TextView) v.findViewById(R.id.context)).setText(filter.getContext());
      ((TextView) v.findViewById(R.id.key)).setText(filter.getKey());
      ((CheckBox) v.findViewById(R.id.ascending)).setChecked(filter.isAscending());
    } else {
      resetFilter();
    }

    b.setView(v);
    b.setPositiveButton(R.string.action_filter, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        String project = ((TextView) v.findViewById(R.id.project)).getText().toString();
        String context = ((TextView) v.findViewById(R.id.context)).getText().toString();
        String key = ((TextView) v.findViewById(R.id.key)).getText().toString();
        String value = ((TextView) v.findViewById(R.id.value)).getText().toString();
        boolean ascending = ((CheckBox) v.findViewById(R.id.ascending)).isChecked();

        filter.setProject(project);
        filter.setContext(context);
        filter.setKey(key);
        filter.setValue(value);
        filter.setAscending(ascending);
        refresh();
      }
    });
    b.setNegativeButton(R.string.reset, new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        resetFilter();
        refresh();
      }
    });

    b.show();
  }

  private void resetFilter() {
    filter = new Filter();
  }
}
