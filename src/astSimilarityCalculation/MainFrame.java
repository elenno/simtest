package astSimilarityCalculation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class MainFrame extends JFrame {
    private JScrollPane jspResult;
    private JButton btnA;
    private JButton btnB;
    private JButton btnStart;
    private JButton btnRemove;
    private JLabel lbLocA;
    private JLabel lbResult;
    private JTable tblResult;
    public MainFrame(){
    	this.setTitle("C/C++代码抄袭检测器");
    	this.setBounds(300, 50, 600, 700);
    	
    	lbResult = new JLabel("这是结果");
    //	tblResult = new JTable(new Object[][]{},new Object[]{"编号", "路径", "相似度"});
    	TableModel tableModel = new DefaultTableModel(new Object[][]{},new Object[]{"编号", "路径", "相似度"});
    	tblResult = new JTable(tableModel);
    	
    	jspResult = new JScrollPane(tblResult);
    	btnA = new JButton("待检代码");
    	btnB = new JButton("添加样本代码");
    	btnStart = new JButton("开始检测");
    	btnRemove = new JButton("移除样本");
    	lbLocA = new JLabel("");

    	this.setLayout(null);
    	this.add(jspResult);
    	this.add(btnA);
    	this.add(btnB);
    //	this.add(btnRemove);
    	this.add(btnStart);
    	this.add(lbLocA);
    	
    	jspResult.setBounds(50, 250, 500, 380);
    	btnA.setBounds(20, 50, 100, 50);
    	btnB.setBounds(150, 50, 100, 50);
    //	btnRemove.setBounds(220, 50, 80, 50);
    	btnStart.setBounds(320, 50, 100, 50);
    	lbLocA.setBounds(100, 0, 200, 50);
    	
    	btnA.addActionListener(new FileChooseActionListener(lbLocA, 1));
    	btnB.addActionListener(new FileChooseActionListener(tblResult, 2));
    	btnStart.addActionListener(new StartButtonActionListener(lbLocA, tblResult));
    	
    	tblResult.getColumnModel().getColumn(0).setPreferredWidth(50);
    	tblResult.getColumnModel().getColumn(1).setPreferredWidth(400);
    	tblResult.getColumnModel().getColumn(2).setPreferredWidth(200);
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setVisible(true);

    }
    
	public JScrollPane getJspResult() {
		return jspResult;
	}
	public void setJspResult(JScrollPane jspResult) {
		this.jspResult = jspResult;
	}
	public JButton getBtnA() {
		return btnA;
	}
	public void setBtnA(JButton btnA) {
		this.btnA = btnA;
	}
	public JButton getBtnB() {
		return btnB;
	}
	public void setBtnB(JButton btnB) {
		this.btnB = btnB;
	}
	public JButton getBtnStart() {
		return btnStart;
	}
	public void setBtnStart(JButton btnStart) {
		this.btnStart = btnStart;
	}
	public JButton getBtnRemove() {
		return btnRemove;
	}
	public void setBtnRemove(JButton btnRemove) {
		this.btnRemove = btnRemove;
	}
	public JLabel getLbLocA() {
		return lbLocA;
	}
	public void setLbLocA(JLabel lbLocA) {
		this.lbLocA = lbLocA;
	}
    
}

class FileChooseActionListener implements ActionListener
{
	public JLabel label = null;
	public JTable table = null;
	public int flag = 0;
	public FileChooseActionListener(JLabel label, int flag) {
		this.label = label;
		this.flag = flag;
	}
	public FileChooseActionListener(JTable table, int flag) {
		this.table = table;
		this.flag = flag;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "C & CPP", "c", "cpp");
		fileChooser.setFileFilter(filter);
    	fileChooser.setDialogTitle("选择待检代码，.c/.cpp格式");
    	int returnVal = fileChooser.showOpenDialog(null);
    	    if(returnVal == JFileChooser.APPROVE_OPTION) {
    	       System.out.println("You chose to open this file: " +
    	            fileChooser.getSelectedFile().getAbsolutePath());
    	    }
    	if(1 == flag)
    	{
    		label.setText(fileChooser.getSelectedFile().getAbsolutePath());
    	}
    	else if(2 == flag)
    	{
    		((DefaultTableModel)(table.getModel())).addRow(new Object[]{
    				table.getModel().getRowCount()+1,
    				fileChooser.getSelectedFile().getAbsolutePath(),
    				0});
    	}
	}	
}


class StartButtonActionListener implements ActionListener
{
    private JLabel label;
    private JTable table;
    
	public StartButtonActionListener(JLabel lbLocA, JTable tblResult) {
        label = lbLocA;
        table = tblResult;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String A = FileToString.readFile(label.getText());
		int len = table.getRowCount();
		List<String> B = new ArrayList<String>();
		for (int i = 0; i < len; i++) {
			B.add(FileToString.readFile((String)(table.getValueAt(i, 1))));
		}
		GetAstSimilarity gas = new GetAstSimilarity();
		gas.SimilarityCalculation(A, B);
		List<Double> simList = gas.getSimList();
		for (int i = 0; i < len; i++){
			table.setValueAt(simList.get(i), i, 2);
		}
			
	}
	
}
