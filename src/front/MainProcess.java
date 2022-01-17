package front;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import controller.DeviceController;
import controller.MemoryController;
import controller.ProcessController;
import models.Process_;
import models.Resourse_;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map.Entry;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JTextField;
public class MainProcess extends JFrame {
	public static MainProcess mainProcess=null;
	private JPanel contentPane;
	private JScrollPane scrollPane_m;//内存
	private JTable table_m;
	private JScrollPane scrollPane_p;//进程
	private JScrollPane scrollPane_pd;
	private JScrollPane scrollPane_d;//设备
	private JTable table_d;
	private JTable table_p;
	private JTable table_pd;
	public JButton run;
	private JButton button_f;
	private JTextField textField_dname;
	private JTextField textField_dcount;
	private JLabel label_pd;
	private static int runstatus=0;
	private static final String[] runStr={"银行家算法检查","运行","运行结束"};
	private JButton skip;
	/**
	 * Create the frame.
	 */
	public MainProcess() {
		mainProcess=this;
		setTitle("进程管理器");	
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(700, 700, 750, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		scrollPane_m = new JScrollPane();
		scrollPane_p = new JScrollPane();
		scrollPane_pd = new JScrollPane();
		run = new JButton();
		run.setToolTipText("分为3个阶段");
		run.setBackground(SystemColor.menu);
		run.setFont(new Font("宋体", Font.PLAIN, 12));
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				run.setEnabled(false);
				new Thread(new Runnable() {
					public void run() {
						_run();
						update();
						run.setEnabled(true);
					}
				}).start();
			}
		});
		JLabel label_p = new JLabel("进程");
		label_p.setToolTipText("");
		label_p.setFont(new Font("宋体", Font.PLAIN, 12));
		JLabel label_m = new JLabel(MemoryController.virtualMemory?"虚拟内存":"内存");
		label_m.setFont(new Font("宋体", Font.PLAIN, 12));
		JLabel label_d = new JLabel("设备");
		label_d.setFont(new Font("宋体", Font.PLAIN, 12));
		button_f = new JButton("文件管理器");
		button_f.setBackground(SystemColor.menu);
		button_f.setFont(new Font("宋体", Font.PLAIN, 12));
		button_f.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFile.mainFile.setVisible(true);
			}
		});
		scrollPane_d = new JScrollPane();
		JButton button_add = new JButton("添加设备");
		button_add.setBackground(SystemColor.menu);
		button_add.setFont(new Font("宋体", Font.PLAIN, 12));
		button_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(DeviceController.add(textField_dname.getText(),Integer.parseInt(textField_dcount.getText()))) {
						JOptionPane.showMessageDialog(null,"添加设备成功",null,JOptionPane.INFORMATION_MESSAGE);
					}
					else{
						JOptionPane.showMessageDialog(null,"无法删除设备", null, JOptionPane.ERROR_MESSAGE);
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null,"请输入整数", null, JOptionPane.ERROR_MESSAGE);
				}				
				update();
			}
		});
		JButton button_remove = new JButton("删除设备");
		button_remove.setBackground(SystemColor.menu);
		button_remove.setFont(new Font("宋体", Font.PLAIN, 12));
		button_remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					if(DeviceController.add(textField_dname.getText(),-Integer.parseInt(textField_dcount.getText()))) {
						JOptionPane.showMessageDialog(null,"删除设备成功",null,JOptionPane.INFORMATION_MESSAGE);
					}
					else{
						JOptionPane.showMessageDialog(null,"无法删除设备", null, JOptionPane.ERROR_MESSAGE);
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null,"请输入整数", null, JOptionPane.ERROR_MESSAGE);
				}
				update();
			}
		});
		textField_dname = new JTextField();
		textField_dname.setFont(new Font("宋体", Font.PLAIN, 12));
		textField_dname.setColumns(10);
		textField_dcount = new JTextField();
		textField_dcount.setFont(new Font("宋体", Font.PLAIN, 12));
		textField_dcount.setColumns(10);
		JLabel label_dname = new JLabel("设备名");
		label_dname.setFont(new Font("宋体", Font.PLAIN, 12));
		JLabel label_dcount = new JLabel("设备数量");
		label_dcount.setFont(new Font("宋体", Font.PLAIN, 12));
		label_pd = new JLabel("进程资源");
		label_pd.setFont(new Font("宋体", Font.PLAIN, 12));
		
		skip = new JButton();
		skip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				skip.setEnabled(false);
				run.setEnabled(false);
				new Thread(new Runnable() {
					public void run() {
						_run();
						while(runstatus!=0){
							_run();
						}
						update();
					}
				}).start();
				run.setEnabled(true);
				skip.setEnabled(true);
			}
		});
		skip.setToolTipText("运行至下一次银行家算法检查之前");
		skip.setText("时间片跳过");
		skip.setFont(new Font("宋体", Font.PLAIN, 12));
		skip.setBackground(SystemColor.menu);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_p, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane_d, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
							.addGap(169)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(label_dname)
									.addGap(18)
									.addComponent(textField_dname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(button_add))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(label_dcount)
									.addGap(18)
									.addComponent(textField_dcount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(button_remove))
								.addComponent(run, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
								.addComponent(skip, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
								.addComponent(button_f, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(label_m)
								.addComponent(scrollPane_m, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(label_pd)
								.addComponent(scrollPane_pd, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)))
						.addComponent(label_p)
						.addComponent(label_d))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_p, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_p, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_m)
						.addComponent(label_pd))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPane_m, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_pd, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(label_d)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(button_add)
								.addComponent(textField_dname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_dname))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(button_remove)
								.addComponent(textField_dcount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_dcount))
							.addPreferredGap(ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
							.addComponent(button_f, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(skip, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(run, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane_d, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
					.addContainerGap())
		);
		update();
		contentPane.setLayout(gl_contentPane);
	}
	public void update(){
		run.setText(runStr[runstatus]);
		table_d=new JTable(DeviceController.gettable(),DeviceController.Dname);
        table_d.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		textField_dname.setText(table_d.getValueAt(table_d.getSelectedRow(), 0).toString());
        	}
        });
        table_d.setFont(new Font("宋体", Font.PLAIN, 12));
		scrollPane_d.setViewportView(table_d);
		DefaultTableModel tableModel_p=new DefaultTableModel(ProcessController.gettable(),ProcessController.name);
		table_p = new JTable(tableModel_p){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		        Component comp = super.prepareRenderer(renderer, row, column);  
		        switch ((String)getValueAt(row,2)) {
				case "就绪":
					comp.setBackground(java.awt.Color.yellow);break;
				case "阻塞":
					comp.setBackground(java.awt.Color.red);break;
				default:
					comp.setBackground(java.awt.Color.green);
				}
		        return comp;
		    }
			@Override
			public void setTableHeader(JTableHeader tableHeader) {
				tableHeader.setDefaultRenderer(new DefaultTableCellRenderer(){
					@Override
					public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {
						setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
						Component component=super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
						if(column==ProcessController.col) component.setBackground(Color.ORANGE);
						else component.setBackground(new Color(240, 240, 240));
						return component;
					}
				});
				super.setTableHeader(tableHeader);
			}
		};
		table_p.setShowGrid(true);
		table_p.setFont(new Font("黑体", Font.PLAIN, 20));
		table_p.setRowHeight(30);
		table_p.add(table_p.getTableHeader(), BorderLayout.NORTH);
		table_p.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		String Process_id=table_p.getValueAt(table_p.getSelectedRow(), 0).toString();
        		Process_ process_=ProcessController.get(Integer.valueOf(Process_id));
        		Resourse_ need=process_.getNeed();
        		Resourse_ allocation_=process_.getAllocation();
        		Vector<Vector<Object>> vec=new Vector<Vector<Object>>();
        		for(Entry<String, Integer> m:need.entrySet()){
        			Vector<Object> objects=new Vector<Object>();
        			objects.add(m.getKey());
        			objects.add(m.getValue());
        			objects.add(allocation_.getOrDefault(m.getKey(),0));
        			vec.add(objects);
        		}
				DefaultTableModel tableModel_d=new DefaultTableModel(vec,DeviceController.PDname);
				table_pd=new JTable(tableModel_d);
				scrollPane_pd.setViewportView(table_pd); 
        	}
        });
		DefaultTableModel tableModel_pd=new DefaultTableModel(new Vector<>(),DeviceController.PDname);
		table_pd=new JTable(tableModel_pd);
		table_pd.setFont(new Font("宋体", Font.PLAIN, 12));
		scrollPane_pd.setViewportView(table_pd); 
		scrollPane_p.setViewportView(table_p);
		DefaultTableModel tableModel_m=new DefaultTableModel(MemoryController.gettable(), MemoryController.name);
		table_m=new JTable(tableModel_m){
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		        Component comp = super.prepareRenderer(renderer, row, column);  
		        if(getValueAt(row,0).toString().equals("0")) comp.setBackground(java.awt.Color.green);
		        else comp.setBackground(table_m.getBackground());
		        return comp;
		    }
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		scrollPane_m.setViewportView(table_m);
	}
	public void _run(){
		switch (runstatus) {
		case 0:
			ProcessController.run1();
			++runstatus;
			break;
		case 1:
			ProcessController.run2();
			++runstatus;
			break;
		case 2:
			ProcessController.run3();
			runstatus=0;
			break;
		default:
			break;
		}
	}
}
