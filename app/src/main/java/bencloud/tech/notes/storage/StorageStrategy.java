package bencloud.tech.notes.storage;

import java.util.List;

import bencloud.tech.notes.recycler.RecyclerPacket;

public interface StorageStrategy {

  List<RecyclerPacket> readCollection(String... info);

  void createItem(String... info);

  void deleteItem(String... info);

}
