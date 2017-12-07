package bencloud.tech.notes;

import bencloud.tech.notes.recycler.RecyclerPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Filter {

  private String project = "", context = "", key = "", value = "";
  private boolean ascending = false;

  public List<RecyclerPacket> applyFilter(List<RecyclerPacket> inList) {
    inList = wordFilter("+", project, "", "", inList);
    inList = wordFilter("@", context, "", "", inList);
    inList = wordFilter("", key, ":", value, inList);
    inList = orderAlphabetically(inList);
    if (ascending) {
      Collections.reverse(inList);
    }

    return inList;
  }

  private List<RecyclerPacket> wordFilter(String prefix, String wordString, String suffix,
      String postString, List<RecyclerPacket> inList) {
    if (wordString.equals("") && postString.equals("")) {
      return inList;
    }

    String[] words = wordString.split(",");
    List<RecyclerPacket> outList = new ArrayList<>();

    for (RecyclerPacket p : inList) {
      for (String s : words) {
        if (p.getDescription().contains(prefix + s + suffix + postString)) {
          outList.add(p);
          break;
        }
      }
    }

    return outList;
  }

  private List<RecyclerPacket> orderAlphabetically(List<RecyclerPacket> inList) {
    if (inList.size() == 0) {
      return inList;
    }
    List<RecyclerPacket> outList = new ArrayList<>();
    outList.add(inList.get(0));

    // insert sort
    for (int i = 1; i < inList.size(); i++) {
      String in = inList.get(i).toString();
      for (int j = 0; j < outList.size(); j++) {
        String out = outList.get(j).toString();
        if (out.compareToIgnoreCase(in) >= 0) {
          outList.add(j, inList.get(i));
          break;
        } else if (j == outList.size() - 1) {
          outList.add(inList.get(i));
          break;
        }
      }
    }

    return outList;
  }

  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isAscending() {
    return ascending;
  }

  public void setAscending(boolean ascending) {
    this.ascending = ascending;
  }
}
