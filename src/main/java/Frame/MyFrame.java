package Frame;

import Models.BlockChain;
import Models.BlockModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class MyFrame extends JFrame {

    private final JFileChooser fileChooser;
    private final BlockChain blockChain;
    private final BlockTableModel blockTableModel;

    public MyFrame(BlockChain blockChain) {
        this.blockChain = blockChain;
        setTitle("Block Chain");
        setLayout(new BorderLayout());

        // get list blocks
        List<BlockModel> list = blockChain.getBlockModelList();

        // blockTableModel ->  to control and display list
        blockTableModel = new BlockTableModel();
        blockTableModel.setList(list);
        //

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(new JScrollPane(new JTable(blockTableModel)));

        // for file ....
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        add(new JScrollPane(tablePanel));

        setJMenuBar(createMenu());

        setVisible(true);
        setMinimumSize(new Dimension(900, 500));
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    private JMenuBar createMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu tools = new JMenu("Tools");

        // tools
        JMenuItem addNewBlock = new JMenuItem("Add new Item");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem export = new JMenuItem("Export Block");
        JMenuItem exit = new JMenuItem("Exit");

        tools.add(addNewBlock);
        tools.add(refresh);
        tools.add(export);
        tools.add(exit);

        tools.setMnemonic(KeyEvent.VK_T);
        exit.setMnemonic(KeyEvent.VK_E);

        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));

        exit.addActionListener(e -> {
            int action = JOptionPane.showConfirmDialog(
                    MyFrame.this,
                    "Do you really want to Exit application?",
                    "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);

            if (action == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        });

        export.addActionListener(e -> {
            if (fileChooser.showOpenDialog(MyFrame.this) == JFileChooser.APPROVE_OPTION) {
                System.out.println(fileChooser.getSelectedFile());
            }
        });

        refresh.addActionListener(e -> {
            List<BlockModel> blockList = Arrays.stream(blockChain.getApiService().getBlockChain()).toList();
            if (!blockList.equals(blockChain.getBlockModelList())) {
                blockTableModel.setList(blockList);
                blockTableModel.fireTableDataChanged();
                blockChain.addToDB();
            }
        });

        addNewBlock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new CreateNewBlock(blockChain, blockTableModel);
            }
        });

        jMenuBar.add(tools);
        return jMenuBar;
    }


}
