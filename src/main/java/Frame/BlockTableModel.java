package Frame;

import Models.BlockModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BlockTableModel extends AbstractTableModel {

    private List<BlockModel> list;
    private final String[] tableNames = new String[]{"PrevHash", "Data", "Name", "Signature", "Time", "Public Key"};

    public BlockTableModel() {
    }

    public void setList(List<BlockModel> list) {
        this.list = list;
    }

    @Override
    public String getColumnName(int column) {
        return tableNames[column];
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BlockModel blockModel = list.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> blockModel.getPrevhash();
            case 1 -> blockModel.getData().getData();
            case 2 -> blockModel.getData().getName();
            case 3 -> blockModel.getSignature();
            case 4 -> blockModel.getTs();
            default -> blockModel.getPublickey();
        };
    }
}
