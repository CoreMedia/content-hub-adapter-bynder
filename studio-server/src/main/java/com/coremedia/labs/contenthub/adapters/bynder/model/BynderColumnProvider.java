package com.coremedia.labs.contenthub.adapters.bynder.model;

import com.coremedia.contenthub.api.ContentHubObject;
import com.coremedia.contenthub.api.Folder;
import com.coremedia.contenthub.api.column.Column;
import com.coremedia.contenthub.api.column.ColumnValue;
import com.coremedia.contenthub.api.column.DefaultColumnProvider;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BynderColumnProvider extends DefaultColumnProvider {

  @Override
  public List<Column> getColumns(Folder folder) {
    List<Column> columns = new ArrayList<>(super.getColumns(folder));
    columns.add(new Column("user", "user", 100, -1));
    columns.add(new Column("dateCreated", "dateCreated", 100, -1));
    columns.add(new Column("dateModified", "dateModified", 100, -1));
    return columns;
  }

  @NonNull
  @Override
  public List<ColumnValue> getColumnValues(ContentHubObject hubObject) {
    if (hubObject instanceof BynderItem) {
      return getColumnValues((BynderItem) hubObject);
    }

   //TODO handle folder metadata

    List<ColumnValue> columnValues = new ArrayList<>(super.getColumnValues(hubObject));
    columnValues.add(new ColumnValue("user", null, null, null));
    columnValues.add(new ColumnValue("dateCreated", null, null, null));
    columnValues.add(new ColumnValue("dateModified", null, null, null));
    return columnValues;
  }

  @NonNull
  private List<ColumnValue> getColumnValues(@NonNull BynderItem bynderItem) {
    List<ColumnValue> columnValues = new ArrayList<>(super.getColumnValues(bynderItem));
    columnValues.add(new ColumnValue("user", bynderItem.getUserCreated(), null, null));
    columnValues.add(new ColumnValue("dateModified", getCalendarFromDate(bynderItem.getDateModified()), null, null));
    columnValues.add(new ColumnValue("dateCreated", getCalendarFromDate(bynderItem.getDateCreated()), null, null));
    return columnValues;
  }

  @NonNull
  private Calendar getCalendarFromDate(@NonNull Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }
}
