package front;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.IconView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import models.File_;
import models.Package_;
import models.Storage_;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
public class MainFile extends JFrame {
	public static MainFile mainFile=null;
	private JPanel contentPane;
	public static Package_ root=null;
	public static Vector<String> columnName=null;
	public static JTree tree;
	private JScrollPane scrollPane_tree;
	private JScrollPane scrollPane_table;
	private JTable table;
	private JTextField textField_path;
	private JButton search_btn;
	private ArrayList<File_> copyf=null;
	private ArrayList<Package_> copyp=null;
	private JScrollPane scrollPane_fat;
	private JScrollPane scrollPane_bit;
	private JTable table_fat;
	private JTable table_bit;
	private JPopupMenu menus[]=null;
	public MainFile() {
		mainFile=this;
		root=new Package_("root");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		scrollPane_tree = new JScrollPane();
		scrollPane_table = new JScrollPane();
		scrollPane_fat = new JScrollPane();
		scrollPane_bit = new JScrollPane();
		setTitle("文件管理器");
		columnName=new Vector<>(Arrays.asList(new String[]{"文件名","文件类型","文件大小","修改时间","FAT头"}));
		copyf=new ArrayList<>();
		copyp=new ArrayList<>();		
		textField_path = new JTextField();
		textField_path.setColumns(10);
		search_btn = new JButton("搜索");
		search_btn.setBackground(SystemColor.menu);
		search_btn.setFont(new Font("宋体", Font.PLAIN, 12));
		search_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		JButton button = new JButton("进程管理器");
		button.setFont(new Font("宋体", Font.PLAIN, 12));
		button.setBackground(SystemColor.menu);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainProcess.mainProcess.setVisible(true);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(scrollPane_tree, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(scrollPane_table, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addComponent(textField_path, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 0, Short.MAX_VALUE)
							.addComponent(search_btn)
							.addGap(131)
							.addComponent(button))
						.addComponent(scrollPane_fat, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_bit, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_path, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(button)
						.addComponent(search_btn))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane_tree, GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(0)
									.addComponent(scrollPane_fat, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(scrollPane_bit, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(19)
							.addComponent(scrollPane_table, GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)))
					.addContainerGap())
		);
		scrollPane_table.setColumnHeaderView(table);
		contentPane.setLayout(gl_contentPane);
		setMenu();
	}
	public JPopupMenu getMenu(){		
		TreePath paths[]=tree.getSelectionPaths();
		if(paths.length>1){
			return menus[2];
		}
		else {
			DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if(node.getUserObject() instanceof File_)
				return menus[0];
			else {
				return menus[1];
			}
		}
	}
	public void removeFile(DefaultMutableTreeNode node){
		int i=javax.swing.JOptionPane.showConfirmDialog(this,"确认要删除文件"+node+"吗？");
		if(i==0){
			File_ file = (File_)node.getUserObject();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			Package_ p = (Package_) parent.getUserObject();
			p.remove(file);
			node.removeFromParent();
			node=null;
			SwingUtilities.updateComponentTreeUI(tree);
		}
		tree.setSelectionPath(null);
	}
	public void removePackage(DefaultMutableTreeNode node){
		if(node.getUserObject()==root){
			JOptionPane.showMessageDialog(null,"无法删除根目录", null, JOptionPane.ERROR_MESSAGE);
			return;
		}
		int i=javax.swing.JOptionPane.showConfirmDialog(this,"确认要删除文件夹"+node+"吗？");
		if(i==0){
			Package_ pac=(Package_)node.getUserObject();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
			Package_ p = (Package_)parent.getUserObject();
			p.remove(pac);
			node.removeFromParent();
			node=null;
			SwingUtilities.updateComponentTreeUI(tree);
		}
		tree.setSelectionPath(null);
	}	
	public void setFile(){
		DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		try{
			String line=JOptionPane.showInputDialog("大小");
			if(line==null) return;
			int size=Integer.parseInt(line);
			File_ file = (File_) node.getUserObject();
			if(file.resize(size)) return;
			else JOptionPane.showMessageDialog(null,"容量不足",null,JOptionPane.ERROR_MESSAGE);
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,"非整数",null,JOptionPane.ERROR_MESSAGE);
		}
	}
	public void renamePackage(){
		DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		String pname=JOptionPane.showInputDialog("文件夹名");
		if(pname==null) return;
		pname=pname.trim();
		if(pname.equals("")) {
			JOptionPane.showMessageDialog(null,"请输入文件夹名",null,JOptionPane.ERROR_MESSAGE);
			return;
		}
		Package_ p=(Package_) node.getUserObject();
		if(pname.equals(p.toString())) return ;
		Package_ parent=(Package_) ((DefaultMutableTreeNode)node.getParent()).getUserObject();
		if(parent.check(pname)){
			p.setName(pname);
			return;
		}
		else JOptionPane.showMessageDialog(null,"文件夹"+pname+"重名",null,JOptionPane.ERROR_MESSAGE);
	}
	public void renameFile(){
		DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		String name=JOptionPane.showInputDialog("文件名和类型名");
		if(name==null) return;
		name=name.trim();
		if(name.equals("")){
			JOptionPane.showMessageDialog(null,"请输入文件",null,JOptionPane.ERROR_MESSAGE);
			return;
		}
		DefaultMutableTreeNode parent=(DefaultMutableTreeNode) node.getParent();
		File_ f=(File_) node.getUserObject();
		if(f.toString().equals(name)) return;
		Package_ p=(Package_) parent.getUserObject();
		if(p.check(name)) {
			f.SetFile(name);
			return;
		}
		else JOptionPane.showMessageDialog(null,"文件"+name+"重名",null,JOptionPane.ERROR_MESSAGE);
	}
	public void createFile(){
		AddFileFrame m4=new AddFileFrame();
		m4.setVisible(true);
	}
	public void createPackage(){	
		DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		String pname=JOptionPane.showInputDialog("文件夹名");
		if(pname==null) return;
		pname=pname.trim();
		if(pname.equals("")) {
			JOptionPane.showMessageDialog(null,"请输入文件夹名",null,JOptionPane.ERROR_MESSAGE);
			return;
		}
		Package_ p=(Package_)node.getUserObject();
		if(p.check(pname)) {
			Package_ newp=new Package_(pname);
			p.add(newp);
			DefaultMutableTreeNode newnode=new DefaultMutableTreeNode(newp);
			node.add(newnode);
			SwingUtilities.updateComponentTreeUI(tree);
			MainFile.mainFile.updateTable();
		}
		else JOptionPane.showMessageDialog(null,"文件夹"+pname+"重名",null,JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * 多选粘贴
	 */
	public void paste(){
		DefaultMutableTreeNode node1=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Package_ parent=(Package_)node1.getUserObject();
		for(File_ e:copyf){
			if(parent.check(e.toString())==false){
				int i=JOptionPane.showConfirmDialog(this,"是否覆盖文件"+e.toString()+"？");
				if(i == 0)
				{	File_ f_copy=new File_(e);
					if(!f_copy.exists()){
						JOptionPane.showMessageDialog(null,"文件"+e.toString()+"过大", null, JOptionPane.ERROR_MESSAGE);
						continue;
					}
					parent.add(f_copy);
					Enumeration<?> enumeration;
					enumeration=node1.children();
					while(enumeration.hasMoreElements()){
						DefaultMutableTreeNode node;
						node=(DefaultMutableTreeNode) enumeration.nextElement();
						Object object1=node.getUserObject();
						if(object1 instanceof File_){
							File_ f1=(File_)object1;
							if(f1.toString().equals(f_copy.toString())){
								parent.remove(f1);
								node.removeFromParent();
								break;
							}
						}
					}
					DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(f_copy);
					node1.add(newnode);
				}
			}
			else{
				File_ file_Copy=new File_(e);
				if(!file_Copy.exists()){
					JOptionPane.showMessageDialog(null,"文件"+e.toString()+"过大", null, JOptionPane.ERROR_MESSAGE);
					continue;
				}
				parent.add(file_Copy);
				DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(file_Copy);
				node1.add(newnode);
			}
		}
		for(Package_ e:copyp){
			if(parent.check(e.toString())==false){
				int i=javax.swing.JOptionPane.showConfirmDialog(this,"是否覆盖文件夹"+e.toString()+"？");
				if(i == 0){	
					Package_ p_copy=new Package_(e);
					Enumeration<?> enumeration;
					enumeration=node1.children();
					while(enumeration.hasMoreElements()){
						DefaultMutableTreeNode node=(DefaultMutableTreeNode) enumeration.nextElement();
						Object object=node.getUserObject();
						if(object instanceof Package_){
							Package_ p=(Package_)object;
							if(p.toString().equals(p_copy.toString())){
								node.removeFromParent();
								parent.remove(p);
								break;
							}
						}
					}
					parent.add(p_copy);
					DefaultMutableTreeNode newnode =getFileTree(p_copy);
					node1.add(newnode);
				}
			}else{
				Package_ pac_Copy=new Package_(e);
				parent.add(pac_Copy);
				DefaultMutableTreeNode newnode =getFileTree(pac_Copy);
				node1.add(newnode);
			}
		}
		SwingUtilities.updateComponentTreeUI(tree);
	}
	public void copy(){
		TreePath[] paths=tree.getSelectionPaths();
		copyf.clear();
		copyp.clear();
		for (TreePath path:paths){
			Object object=((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
			if(object instanceof File_){
				copyf.add((File_) object);
			}
			else copyp.add((Package_) object);
		}
	}
	public void remove(){
		TreePath[] paths=tree.getSelectionPaths();
		for (TreePath path:paths){
			DefaultMutableTreeNode node=(DefaultMutableTreeNode)path.getLastPathComponent();
			Object object=node.getUserObject();
			if(object instanceof File_) removeFile(node);
			else removePackage(node);
		}
		tree.setSelectionPath(null);
	}
	public void search(){
		Vector<Vector<Object>> vector=new Vector<>();
		ArrayList<Object> objects=new ArrayList<Object>();
		root.find(textField_path.getText(),objects);
		for(Object object:objects){
			if(object instanceof File_) {
				vector.add(((File_)object).getRow());
				textField_path.setText(((File_)object).getPath());
			}
			else if(object instanceof Package_){
				Package_ p=(Package_)object;
				vector.add(p.getRow());
				for(File_ e:p.getFilelist()) vector.add(e.getRow());
				for(Package_ e:p.getPackagelist()) vector.add(e.getRow());
				textField_path.setText(p.getPath());
			}
		}
		DefaultTableModel tableModel=new DefaultTableModel(vector, columnName);
		table=new JTable(tableModel);
		scrollPane_table.setViewportView(table);
	}
	public static DefaultMutableTreeNode getFileTree(Package_ p){
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(p);
		for(Package_ e:p.getPackagelist()) node.add(getFileTree(e));
		for(File_ e:p.getFilelist()) node.add(new DefaultMutableTreeNode(e));
		return node;
	}
	/**
	 * 初始化文件树
	 */
	public void updateTree(){
		tree=new JTree(getFileTree(root));
		tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
            	updateTable();
            	if (evt.getButton() == MouseEvent.BUTTON3&&tree.getLastSelectedPathComponent()!=null) {
            		getMenu().show(tree, evt.getX(), evt.getY());
                }
            }
		});
        tree.setCellRenderer(new DefaultTreeCellRenderer(){
        	@Override
        	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
        			boolean leaf, int row, boolean hasFocus) {
        		Component component=super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        		Object object =((DefaultMutableTreeNode) value).getUserObject();
                if(object instanceof File_){
                	File_ file_=(File_)object;
                	setIcon(new ImageIcon("img/"+file_.getType()+".png"));     
                }
                else setIcon(new ImageIcon("img/package.png"));
        		return component;
        	}
        });
		scrollPane_tree.setViewportView(tree);
	}
	public void updateTable(){
		DefaultTableModel tableModel=new DefaultTableModel(gettable(), columnName);
		table=new JTable(tableModel);
		scrollPane_table.setViewportView(table);
		DefaultTableModel tableModel_fat=new DefaultTableModel(Storage_.gettable_fat(), Storage_.name);
		table_fat=new JTable(tableModel_fat){
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {  
		        Component comp = super.prepareRenderer(renderer, row, column);
		        if (column == 1){
		        	switch ((String)getValueAt(row,1)) {
					case "-1":
						comp.setBackground(java.awt.Color.green);
						break;
					case "-2":
						comp.setBackground(java.awt.Color.red);
						break;
					default:
						comp.setBackground(table_fat.getBackground());
						break;
					}
		        }
		        else {
		        	comp.setBackground(table_fat.getBackground());
				}
		        return comp;
		    }
		    public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		scrollPane_fat.setViewportView(table_fat);
		DefaultTableModel tableModel_bit=new DefaultTableModel(Storage_.gettable_bit(), Storage_.name_bit);
		table_bit=new JTable(tableModel_bit){
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {    
		        Component comp = super.prepareRenderer(renderer, row, column);  
		        if(column>0){
		        	String a=(String) getValueAt(row,column) ;
	                if (a.equals("1")) comp.setBackground(java.awt.Color.RED);
	                else comp.setBackground(java.awt.Color.green);
		        }
		        else comp.setBackground(getTableHeader().getBackground());
		        return comp;
		    }
		    public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		table_bit.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_bit.setRowHeight(16);
		int height=table_bit.getRowHeight();
		TableColumnModel columnModel=table_bit.getColumnModel();
		Enumeration<TableColumn> columns=columnModel.getColumns();
		columns.nextElement().setPreferredWidth(20);
		while(columns.hasMoreElements()){
			columns.nextElement().setPreferredWidth(height);
		}
		DefaultTableCellRenderer cellRenderer=new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table_bit.setDefaultRenderer(Object.class, cellRenderer);
		scrollPane_bit.setViewportView(table_bit);
	}
	public Vector<Vector<Object>> gettable(){
		Vector<Vector<Object>> vector=new Vector<>();
		DefaultMutableTreeNode node=(DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if(node==null) return vector;
		Object object=node.getUserObject();
		if(object instanceof File_) {
			vector.add(((File_)object).getRow());
			textField_path.setText(((File_)object).getPath());
		}
		else if(object instanceof Package_){
			Package_ p=(Package_)object;
			vector.add(p.getRow());
			for(File_ e:p.getFilelist()) vector.add(e.getRow());
			for(Package_ e:p.getPackagelist()) vector.add(e.getRow());
			textField_path.setText(p.getPath());
		}
		return vector;
	}
	/**
	 * 右键菜单
	 */
	private void setMenu(){
		menus=new JPopupMenu[3];
		ActionListener copylisten=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		};
		{
			JPopupMenu menu=new JPopupMenu();
			JMenuItem run=new JMenuItem("运行");
			JMenuItem renamef=new JMenuItem("重命名");
			JMenuItem copy=new JMenuItem("复制");copy.addActionListener(copylisten);
			JMenuItem mremovef=new JMenuItem("删除");
			JMenuItem resizef=new JMenuItem("修改大小");
			run.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					AddProcessFrame m=new AddProcessFrame(tree.getLastSelectedPathComponent().toString());
					m.setVisible(true);
				}
			});
			mremovef.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removeFile((DefaultMutableTreeNode) tree.getLastSelectedPathComponent());
					updateTable();
				}
			});
			renamef.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					renameFile();
					updateTable();
				}
			});
			resizef.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setFile();
					updateTable();
				}
			});
			menu.add(run);
			menu.add(copy);
			menu.add(mremovef);
			menu.add(renamef);
			menu.add(resizef);
			menus[0]=menu;
		}
		{
			JPopupMenu menu=new JPopupMenu();
			JMenuItem renamep=new JMenuItem("重命名");
			JMenuItem newf=new JMenuItem("新建文件");
			JMenuItem newp=new JMenuItem("新建文件夹");
			JMenuItem copy=new JMenuItem("复制");copy.addActionListener(copylisten);
			JMenuItem mpaste=new JMenuItem("粘贴");
			JMenuItem mremovep=new JMenuItem("删除");
			newf.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					createFile();
				}
			});
			newp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					createPackage();
				}
			});
			mpaste.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					paste();
					updateTable();
				}
			});
			mremovep.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					removePackage((DefaultMutableTreeNode) tree.getLastSelectedPathComponent());
					updateTable();
				}
			});
			renamep.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					renamePackage();
					updateTable();
				}
			});
			menu.add(newf);
			menu.add(newp);
			menu.add(copy);
			menu.add(mpaste);
			menu.add(mremovep);
			menu.add(renamep);
			menus[1]=menu;
		}
		{
			JPopupMenu menu=new JPopupMenu();
			JMenuItem copy=new JMenuItem("复制");copy.addActionListener(copylisten);
			JMenuItem remove=new JMenuItem("删除");
			remove.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					remove();
					updateTable();
				}
			});
			menu.add(copy);
			menu.add(remove);
			menus[2]=menu;
		}
	}
}
