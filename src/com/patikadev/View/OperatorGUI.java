package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Course;
import com.patikadev.Model.Operator;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_Form;
    private JTextField fld_name;
    private JTextField fld_username;
    private JTextField fld_password;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_search_name;
    private JTextField fld_search_username;
    private JComboBox cmb_search_userType;
    private JButton btn_search;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JTextField fld_course_name;
    private JButton btn_course_add;
    private JPanel pnl_course_add;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private JPopupMenu course_menu;

    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;

        add(wrapper);
        setSize(1000, 600);

        int x = Helper.screenCenter("x", getSize());
        int y = Helper.screenCenter("y", getSize());
        setLocation(x, y);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Welcome " + operator.getName());

        //Model User List
        mdl_user_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list = {"ID", "Name-Surname", "Username", "Password", "User-Type"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list = new Object[col_user_list.length];
        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                fld_user_id.setText(select_user_id);
            } catch (Exception exception) {
            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_username = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String user_password = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String user_userType = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();
                if (User.update(user_id, user_name, user_username, user_password, user_userType)) {
                    Helper.showMsg("done");
                }
                loadUserModel();
            }
        });
        // ##User List

        // Course List

        // Course Menu
        course_menu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Update");
        JMenuItem deleteMenu = new JMenuItem("Delete");
        course_menu.add(updateMenu);
        course_menu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(),0).toString());
            UpdateCourseGUI updateGUI = new UpdateCourseGUI(Course.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCourseModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int select_id = Integer.parseInt(tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(),0).toString());
                if(Course.delete(select_id)){
                    Helper.showMsg("done");
                    loadCourseModel();
                } else{
                    Helper.showMsg("error");
                }
            }
        });

        mdl_course_list = new DefaultTableModel();
        Object[] col_course_list = {"ID", "Course Name"};
        mdl_course_list.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        loadCourseModel();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.setComponentPopupMenu(course_menu);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_course_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_course_list.rowAtPoint(point);
                tbl_course_list.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        // ## Model Course List

        btn_user_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_name) || Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_password)) {
                Helper.showMsg("fill");
            } else {
                String name = fld_name.getText();
                String username = fld_username.getText();
                String password = fld_password.getText();
                String userType = cmb_user_type.getSelectedItem().toString();
                if (User.add(name, username, password, userType)) {
                    Helper.showMsg("done");
                    loadUserModel();
                    fld_name.setText(null);
                    fld_username.setText(null);
                    fld_password.setText(null);
                }
            }
        });
        btn_user_delete.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_id)) {
                Helper.showMsg("fill");
            } else {
                if(Helper.confirm("sure")){
                    int user_id = Integer.parseInt(fld_user_id.getText());
                    if (User.delete(user_id)) {
                        Helper.showMsg("done");
                        loadUserModel();
                    } else {
                        Helper.showMsg("error");
                    }
                }
            }
        });

        btn_search.addActionListener(e -> {
            String name = fld_search_name.getText();
            String username = fld_search_username.getText();
            String userType = cmb_search_userType.getSelectedItem().toString();
            String query = User.searchQuery(name, username, userType);
            loadUserModel(User.searchUserList(query));
        });

        btn_logout.addActionListener(e -> {
            dispose();
        });

        btn_course_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_course_name)) {
                Helper.showMsg("fill");
            } else {
                if (Course.add(fld_course_name.getText())) {
                    Helper.showMsg("done");
                    loadCourseModel();
                    fld_course_name.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Course course : Course.getList()) {
            i = 0;
            row_course_list[i++] = course.getId();
            row_course_list[i++] = course.getName();
            mdl_course_list.addRow(row_course_list);
        }

    }

    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (User obj : User.getList()) {
            i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getUserType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadUserModel(ArrayList<User> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        for (User obj : list) {
            int i = 0;
            row_user_list[i++] = obj.getId();
            row_user_list[i++] = obj.getName();
            row_user_list[i++] = obj.getUsername();
            row_user_list[i++] = obj.getPassword();
            row_user_list[i++] = obj.getUserType();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public static void main(String[] args) {
        Helper.setLayout();

        Operator op = new Operator();
        op.setId(2);
        op.setName("Elif İntizamoğlu");
        op.setUsername("elif");
        op.setPassword("1234");
        op.setUserType("operator");
        OperatorGUI opGUI = new OperatorGUI(op);
    }
}
