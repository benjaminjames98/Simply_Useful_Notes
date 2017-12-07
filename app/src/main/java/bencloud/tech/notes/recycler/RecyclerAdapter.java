package bencloud.tech.notes.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import bencloud.tech.notes.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

  private final List<RecyclerPacket> recyclerPacketList;
  private OnItemClickListener onItemClickListener;

  RecyclerAdapter(List<RecyclerPacket> recyclerPacketList) {
    this.recyclerPacketList = recyclerPacketList;
  }

  @Override
  public RecyclerHolder onCreateViewHolder(ViewGroup parent, int i) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View view = inflater.inflate(R.layout.list_row, parent, false);

    return new RecyclerHolder(view);
  }

  @Override
  public void onBindViewHolder(final RecyclerHolder h, int i) {
    final RecyclerPacket p = recyclerPacketList.get(i);

    h.complete.setText(p.getComplete());
    h.priority.setText(p.getPriority());
    h.creationDate.setText(p.getCreationDate());
    h.completionDate.setText(p.getCompletionDate());

    String descriptionText = p.getDescription();
    if (descriptionText.length() > 128) {
      descriptionText = (String) descriptionText.subSequence(0, 128);
      descriptionText = descriptionText.trim();
      descriptionText = descriptionText + "...";
    } else {
      descriptionText = descriptionText.trim();
    }
    h.description.setText(descriptionText);

    h.expand.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.onItemClick(p, h, OnItemClickListener.CLICK_CODE_EXPAND);
      }
    });
    h.edit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.onItemClick(p, null, OnItemClickListener.CLICK_CODE_EDIT);
      }
    });
    h.delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onItemClickListener.onItemClick(p, null, OnItemClickListener.CLICK_CODE_DELETE);
      }
    });

    h.edit.setVisibility(View.GONE);
    h.delete.setVisibility(View.GONE);
  }


  @Override
  public int getItemCount() {
    return recyclerPacketList == null ? 0 : recyclerPacketList.size();
  }

  class RecyclerHolder extends RecyclerView.ViewHolder {

    final TextView complete;
    final TextView priority;
    final TextView completionDate;
    final TextView creationDate;
    final TextView description;
    final ImageButton expand;
    final ImageButton edit;
    final ImageButton delete;

    RecyclerHolder(View view) {
      super(view);
      this.complete = view.findViewById(R.id.complete);
      this.priority = view.findViewById(R.id.priority);
      this.creationDate = view.findViewById(R.id.creationDate);
      this.completionDate = view.findViewById(R.id.completionDate);
      this.description = view.findViewById(R.id.description);
      this.expand = view.findViewById(R.id.expand);
      this.edit = view.findViewById(R.id.edit);
      this.delete = view.findViewById(R.id.delete);
    }
  }

  void setOnItemClickListener(OnItemClickListener l) {
    onItemClickListener = l;
  }

  public interface OnItemClickListener {

    int CLICK_CODE_EXPAND = 1000;
    int CLICK_CODE_EDIT = 1001;
    int CLICK_CODE_DELETE = 1002;

    void onItemClick(RecyclerPacket recyclerPacket, RecyclerHolder recyclerHolder, int code);
  }
}
