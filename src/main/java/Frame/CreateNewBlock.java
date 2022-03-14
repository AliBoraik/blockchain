package Frame;

import Models.BlockChain;
import Models.BlockModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class CreateNewBlock extends JFrame {
    private JButton btnCreate;
    private JTextField name;
    private JTextField data;

    public CreateNewBlock(BlockChain blockChain, BlockTableModel blockTableModel) {
        setTitle("Create new Block");

        JPanel editorPanel = new JPanel();
        JLabel nameLabel = new JLabel("Name");
        name = new JTextField();
        JLabel dataLabel = new JLabel("Data");
        data = new JTextField();
        name.setPreferredSize(new Dimension(200, 30));
        data.setPreferredSize(new Dimension(200, 30));
        btnCreate = new JButton("Create");
        btnCreate.setBackground(new Color(59, 89, 182));
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFocusPainted(false);
        btnCreate.setFont(new Font("Tahoma", Font.BOLD, 12));
        name.setName("Name");


        editorPanel.setPreferredSize(new Dimension(100, 100));
        editorPanel.add(nameLabel);
        editorPanel.add(name);
        editorPanel.add(dataLabel);
        editorPanel.add(data);
        editorPanel.add(btnCreate);
        add(editorPanel, BorderLayout.NORTH);


        btnCreate.addActionListener(e -> {
            if (name.getText().isEmpty() || data.getText().isEmpty()) {
                JOptionPane.showConfirmDialog(
                        CreateNewBlock.this,
                        "There are empty fields!!!",
                        "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);

            } else {
                try {
                    blockChain.addNewBlockChain(data.getText(), name.getText());
                    List<BlockModel> blockList = Arrays.stream(blockChain.getApiService().getBlockChain()).toList();
                    if (!blockList.equals(blockChain.getBlockModelList())) {
                        blockTableModel.setList(blockList);
                        blockTableModel.fireTableDataChanged();
                    }
                    this.dispose();
                    //  System.exit(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        setVisible(true);
        setSize(700, 400);

    }

}
