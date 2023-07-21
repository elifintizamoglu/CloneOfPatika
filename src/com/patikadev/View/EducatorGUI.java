package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Subject;
import com.patikadev.Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wtop;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTabbedPane tabbedPane1;
    private JPanel pnl_subject_list;
    private JScrollPane scrll_subject_list;
    private JTable tbl_subject_list;
    private JPanel pnl_content_list;
    private JScrollPane scrll_content_list;
    private JTable tbl_content_list;
    private JPanel pnl_content_add;
    private JTextField fld_content_title;
    private JTextArea area_content_desc;
    private JTextField fld_content_link;
    private JTextArea area_content_ques;
    private JComboBox cmb_content_subject;
    private JButton btn_content_add;
    private DefaultTableModel mdl_subject_list;
    private Object[] row_subject_list;
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;

    private final User user;

    public EducatorGUI(User user) {
        this.user = user;
        add(wrapper);
        setSize(1000, 600);
        setLocation(Helper.screenCenter("x", getSize()), (Helper.screenCenter("y", getSize())));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        lbl_welcome.setText("Welcome " + user.getName());

        // Subject List
        mdl_subject_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Object[] col_subject_list = {"ID", "Subject Name", "Programming Language", "Course Name", "Educator"};
        mdl_subject_list.setColumnIdentifiers(col_subject_list);
        row_subject_list = new Object[col_subject_list.length];
        loadSubjectModel();

        tbl_subject_list.setModel(mdl_subject_list);
        tbl_subject_list.getTableHeader().setReorderingAllowed(false);
        tbl_subject_list.getColumnModel().getColumn(0).setMaxWidth(75);

        // ## Subject List


        // Content Lİst

        mdl_content_list = new DefaultTableModel();
        Object[] col_content_list = {"ID", "Title", "Description", "Link", "Questions", "Subject Name"};
        mdl_content_list.setColumnIdentifiers(col_content_list);
        row_content_list = new Object[col_content_list.length];

        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);
        tbl_content_list.getColumnModel().getColumn(0).setMaxWidth(75);


        tbl_subject_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                int select_subject_id = Integer.parseInt(tbl_subject_list.getValueAt(tbl_subject_list.getSelectedRow(), 0).toString());
                loadContentModel(select_subject_id);
            } catch (Exception exception) {
            }
        });



        // ## Content List


        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI loginGUI = new LoginGUI();
        });
    }

    private void loadContentModel(int selected_id) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_content_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Content content:Content.getListBySubjectId(selected_id)){
            i = 0;
            row_content_list[i++] = content.getId();
            row_content_list[i++] = content.getTitle();
            row_content_list[i++] = content.getDescription();
            row_content_list[i++] = content.getLink();
            row_content_list[i++] = content.getQuizQue();
            row_content_list[i++] = content.getSubject().getName();
            mdl_content_list.addRow(row_content_list);
        }
    }

    private void loadSubjectModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_subject_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Subject subject : Subject.getListByUser(user.getId())) {
            i = 0;
            row_subject_list[i++] = subject.getId();
            row_subject_list[i++] = subject.getName();
            row_subject_list[i++] = subject.getLang();
            row_subject_list[i++] = subject.getCourse().getName();
            row_subject_list[i++] = subject.getEducator().getName();
            mdl_subject_list.addRow(row_subject_list);
        }
    }

}
