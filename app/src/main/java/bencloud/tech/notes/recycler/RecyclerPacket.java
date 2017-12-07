package bencloud.tech.notes.recycler;

public class RecyclerPacket {

  private final String complete;
  private final String priority;
  private final String completionDate;
  private final String creationDate;
  private final String description;

  public RecyclerPacket(String complete, String priority, String creationDate,
      String completionDate, String description) {
    this.complete = complete;
    this.priority = priority;
    this.completionDate = completionDate;
    this.creationDate = creationDate;
    this.description = description;
  }

  public String getComplete() {
    return complete;
  }

  public String getPriority() {
    return priority;
  }

  public String getCompletionDate() {
    return completionDate;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    String completeString = (complete.equals("")) ? "-" : complete;
    String priorityString = (priority.equals("")) ? "Ω" : priority;
    String completionString = (completionDate.equals("")) ? "Ω" : completionDate;
    String creationString = (creationDate.equals("")) ? "Ω" : creationDate;
    String descriptionString = (description.equals("")) ? "Ω" : description;

    return completeString + "" + priorityString + "" + completionString + "" + creationString + ""
        + descriptionString;
  }
}
