package controller;

import view.JF_Manage_Videos;
import view.JF_Home_Page;
import model.Model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Controller implements ActionListener {

    // Constantes para acceder a los elementos otras capas
    private JF_Manage_Videos viewManage;
    private JF_Home_Page viewHome_Page;
    private Model model;

    /*
        Constructor
    
        En el constructor se asignan los eventos y se hacen 
        las operaciones iniciales como consultar el contenido de 
        los archivos y cargarlos en la tabla
     */
    public Controller(JF_Manage_Videos viewManage, JF_Home_Page viewHome, Model model) {
        this.viewHome_Page = viewHome;
        this.viewManage = viewManage;
        this.model = model;

        // Evento de jframe de gestionar
        this.viewManage.btnGoHome.addActionListener(this);
        this.viewManage.btnCreate.addActionListener(this);
        this.viewManage.btnUpdate.addActionListener(this);
        this.viewManage.btnDelete.addActionListener(this);
        this.viewManage.btnCancel.addActionListener(this);
        this.viewManage.btnSuggestId.addActionListener(this);

        // Evento de jframe de home
        this.viewHome_Page.btnManageVideos.addActionListener(this);

        // Evento de la tabla        
        this.viewManage.jtblVideos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                DefaultTableModel modelTable = (DefaultTableModel) viewManage.jtblVideos.getModel();

                viewManage.txtId.setText((Integer) modelTable.getValueAt(viewManage.jtblVideos.getSelectedRow(), 0) + "");
                viewManage.txtTitle.setText((String) modelTable.getValueAt(viewManage.jtblVideos.getSelectedRow(), 1));
                viewManage.txtCategory.setText((String) modelTable.getValueAt(viewManage.jtblVideos.getSelectedRow(), 2));
                viewManage.txtDuration.setText((String) modelTable.getValueAt(viewManage.jtblVideos.getSelectedRow(), 3));
                viewManage.txtAuthor.setText((String) modelTable.getValueAt(viewManage.jtblVideos.getSelectedRow(), 4));
                viewManage.txtPublishDate.setText((String) modelTable.getValueAt(viewManage.jtblVideos.getSelectedRow(), 5));

                // Establacer botones activos
                viewManage.btnCreate.setEnabled(false);
                viewManage.btnUpdate.setEnabled(true);
                viewManage.btnDelete.setEnabled(true);
            }
        });

        /*
            OPERACIONES INICIALES 
         */
        // Crear los archivos en caso de que no existan
        model.CreateFiles();

        // Cargar datos en la tabla
        SyncTable();
    }

    // Sincronizar la tabla: extraer el contenido de los archivos y deslpleagarlo en la tabla
    public void SyncTable() {
        // Configurar la tabla
        String[] tableHeaders = {"ID", "Titulo", "Categoria", "Duración", "Autor", "Fecha de publicación"};
        DefaultTableModel modelTable = new DefaultTableModel(null, tableHeaders);
        viewManage.jtblVideos.setModel(modelTable);

        // Limpiar tabla para evitar duplicidad
        while (modelTable.getRowCount() > 0) {
            modelTable.removeRow(0);
        }

        try {
            // Obtener la lista amacenada en un archivo
            ArrayList<Model> listVideos = model.ReadFileUsers();

            // Recorrer la lista y agregar cada item a la tabla
            for (Model mod : listVideos) {
                Object[] obj = {mod.getId(), mod.getTitle(), mod.getCategory(), mod.getDuration(), mod.getAuthor(), mod.getPublishDate()};
                modelTable.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void ClearFields() {
        viewManage.txtId.setText("");
        viewManage.txtTitle.setText("");
        viewManage.txtCategory.setText("");
        viewManage.txtDuration.setText("");
        viewManage.txtAuthor.setText("");
        viewManage.txtPublishDate.setText("");
    }

    public void SuggestId() {
        // Obtener la lista amacenada en un archivo
        ArrayList<Model> listVideos = model.ReadFileUsers();
        
        // Si la lista esta vacia se comienza en id 1
        if (model.EmptyFile()) {
            viewManage.txtId.setText("1");
            return;
        }
        
        // Si no: sugerir id
        int id = 1;
        for (Model model: listVideos) {
            id++;
        }
        viewManage.txtId.setText(Integer.toString(id));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
            Eventos
        */

        // BTN abrir ventana MANAGEVIDEOS  
        if (e.getSource() == viewHome_Page.btnManageVideos) {
            viewHome_Page.setVisible(false);
            viewManage.setVisible(true);

            // Establacer botones activos
            viewManage.btnCreate.setEnabled(true);
            viewManage.btnUpdate.setEnabled(false);
            viewManage.btnDelete.setEnabled(false);

        } // BTN GO HOME
        else if (e.getSource() == viewManage.btnGoHome) {
            viewHome_Page.setVisible(true);
            viewManage.setVisible(false);

        } // BTN CREATE
        else if (e.getSource() == viewManage.btnCreate) {
            // Validar si se ingreso el ID
            if (viewManage.txtId.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Ingresa el ID del nuevo video");
            } else {
                try {
                    /*
                        El try catch valida pricipalmente que el id sea de tipo numerico 
                     */
                    int id = Integer.parseInt(viewManage.txtId.getText());

                    // Obtener los campos de texto
                    String title = viewManage.txtTitle.getText();
                    String category = viewManage.txtCategory.getText();
                    String duration = viewManage.txtDuration.getText();
                    String author = viewManage.txtAuthor.getText();
                    String publishDate = viewManage.txtPublishDate.getText();

                    Model Omodel = new Model();

                    // Validar Si ya exiten usuarios
                    if (Omodel.EmptyFile()) {
                        Omodel.CreateList(new Model(id, title, category, duration, author, publishDate));
                        System.out.println("*** Se creo la lista");
                        System.out.println("*** Se agrego el primer video");
                        System.out.println("*** Se guardo la lista");
                    } else {
                        // Obtener la lista amacenada en un archivo
                        ArrayList<Model> listVideos = Omodel.ReadFileUsers();

                        // Validar si el usuario existe
                        for (Model mod : listVideos) {
                            if (mod.getId() == id) {
                                JOptionPane.showMessageDialog(null, "El ID de usuario ya existe");
                                return;
                            }
                        }

                        // Agregar el usuario
                        listVideos.add(new Model(id, title, category, duration, author, publishDate));

                        // Guradar la lista actualizada
                        Omodel.WriteFileUsers(listVideos);
                    }

                    SyncTable();

                    // Establacer botones activos
                    viewManage.btnCreate.setEnabled(true);
                    viewManage.btnUpdate.setEnabled(false);
                    viewManage.btnDelete.setEnabled(false);
                    ClearFields();

                    JOptionPane.showMessageDialog(null, "El video se ha CREADO correctamente");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "El id debe estar compuesto solo por números");
                }
            }
        } // BTN UPDATE
        else if (e.getSource() == viewManage.btnUpdate) {
            // Validar si se selecciono un video
            if (viewManage.txtId.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Elige un video para ACTUALIZAR");
            } else {
                // Obtener los campos de texto
                int id = Integer.parseInt(viewManage.txtId.getText());
                String title = viewManage.txtTitle.getText();
                String category = viewManage.txtCategory.getText();
                String duration = viewManage.txtDuration.getText();
                String author = viewManage.txtAuthor.getText();
                String publishDate = viewManage.txtPublishDate.getText();

                Model Omodel = new Model();

                // Obtener la lista amacenada en un archivo
                ArrayList<Model> listVideos = Omodel.ReadFileUsers();

                for (Model video : listVideos) {
                    if (video.getId() == id) {
                        video.setTitle(title);
                        video.setCategory(category);
                        video.setDuration(duration);
                        video.setAuthor(author);
                        video.setPublishDate(publishDate);
                    }
                }

                // Guradar la lista actualizada
                Omodel.WriteFileUsers(listVideos);

                SyncTable();

                // Establacer botones activos
                viewManage.btnCreate.setEnabled(true);
                viewManage.btnUpdate.setEnabled(false);
                viewManage.btnDelete.setEnabled(false);
                ClearFields();

                JOptionPane.showMessageDialog(null, "El video se ha ACTUALIZADO correctamente");
            }

        } // BTN DELETE
        else if (e.getSource() == viewManage.btnDelete) {
            // Validar si se selecciono un video
            if (viewManage.txtId.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "Elige un video para ELIMINAR");
            } else {
                Model Omodel = new Model();
                int id = Integer.parseInt(viewManage.txtId.getText());

                // Obtener la lista amacenada en un archivo
                ArrayList<Model> listVideos = Omodel.ReadFileUsers();

                int index = -1;

                for (int i = 0; i < listVideos.size(); i++) {
                    if (listVideos.get(i).getId() == id) {
                        index = i;
                        break;
                    }
                }

                if (index != -1) {
                    listVideos.remove(index);
                }

                // Guradar la lista actualizada
                Omodel.WriteFileUsers(listVideos);

                SyncTable();

                // Establacer botones activos
                viewManage.btnCreate.setEnabled(true);
                viewManage.btnUpdate.setEnabled(false);
                viewManage.btnDelete.setEnabled(false);
                ClearFields();

                JOptionPane.showMessageDialog(null, "El video ha sido ELIMINADO correctamente");
            }
        } // Boton cancelar: deja la app como recien abierta
        else if (e.getSource() == viewManage.btnCancel) {
            ClearFields();

            // Establacer botones activos
            viewManage.btnCreate.setEnabled(true);
            viewManage.btnUpdate.setEnabled(false);
            viewManage.btnDelete.setEnabled(false);
        } // Boton sugerir id
        else if (e.getSource() == viewManage.btnSuggestId){
            SuggestId();
        }
    }
}
