package org.pentaho.ui.xul.swt.tags.treeutil;

import java.util.Vector;

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.pentaho.ui.xul.components.XulTreeCell;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.containers.XulTreeItem;

public class XulTreeColumnModifier implements ICellModifier {

  XulTree tree;
  public XulTreeColumnModifier(XulTree tree){
    this.tree = tree;
  }
  
  public boolean canModify(Object arg0, String property) {
    return tree.getColumns().getColumn(Integer.parseInt(property)).isEditable();
  }

  public Object getValue(Object arg0, String property) {

    final int colIdx = Integer.parseInt(property);
    XulTreeCell cell = ((XulTreeItem) arg0).getRow().getCell(colIdx);
    if (cell == null) {
      return "";
    }

    switch (tree.getColumns().getColumn(colIdx).getColumnType()) {
    case CHECKBOX:
      return cell.getValue();
    case COMBOBOX:
    case EDITABLECOMBOBOX:
      Vector vals = (Vector) cell.getValue();
      String[] items = new String[vals.size()];
      for (int i = 0; i < vals.size(); i++) {
        items[i] = "" + vals.get(i);
      }
      ((ComboBoxCellEditor) ((TreeViewer) tree.getManagedObject()).getCellEditors()[colIdx]).setItems(items);
      return cell.getSelectedIndex();
    case TEXT:
      ((TextCellEditor) ((TreeViewer) tree.getManagedObject()).getCellEditors()[colIdx]).getControl()
          .setEnabled(!cell.isDisabled());

      return cell.getLabel() != null ? cell.getLabel() : "";
    default:
      return cell.getValue();
    }

  }

  public void modify(Object element, String property, Object value) {

    final int colIdx = Integer.parseInt(property);
    XulTreeCell cell = ((XulTreeItem) ((TreeItem) element).getData())
        .getRow().getCell(colIdx);
    if (cell == null) {
      return;
    }

    switch (tree.getColumns().getColumn(colIdx).getColumnType()) {
    case CHECKBOX:
      cell.setValue((Boolean) value);
      break;
    case COMBOBOX:
    case EDITABLECOMBOBOX:
      if (value instanceof String) {
        cell.setLabel((String) value);
      } else if (((Integer) value) > -1) {
        cell.setSelectedIndex((Integer) value);
      }
      break;
    default:
      TextCellEditor editor = ((TextCellEditor) ((TreeViewer) tree.getManagedObject()).getCellEditors()[colIdx]);
//      if(editor.isActivated() == false){
//        cell.setLabel((String) value);
//      }
      cell.setLabel((String) value);
    }
    ((TreeViewer) tree.getManagedObject()).refresh();
  }

}