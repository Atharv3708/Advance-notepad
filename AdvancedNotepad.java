import javax.swing.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AdvancedNotepad extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private UndoManager undoManager;

    public AdvancedNotepad() {
        // Initialize the main frame
        setTitle("Advanced Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Initialize components
        textArea = new JTextArea();
        fileChooser = new JFileChooser();
        undoManager = new UndoManager();

        // Add a scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Set up the menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // File menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        addMenuItem(fileMenu, "New", "new");
        addMenuItem(fileMenu, "Open", "open");
        addMenuItem(fileMenu, "Save", "save");
        addMenuItem(fileMenu, "Save As", "saveas");
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", "exit");

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        addMenuItem(editMenu, "Undo", "undo");
        addMenuItem(editMenu, "Redo", "redo");
        editMenu.addSeparator();
        addMenuItem(editMenu, "Cut", "cut");
        addMenuItem(editMenu, "Copy", "copy");
        addMenuItem(editMenu, "Paste", "paste");
        editMenu.addSeparator();
        addMenuItem(editMenu, "Find", "find");

        // Add undo/redo functionality
        textArea.getDocument().addUndoableEditListener(undoManager);

        // Set visible
        setVisible(true);
    }

    private void addMenuItem(JMenu menu, String text, String actionCommand) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setActionCommand(actionCommand);
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "new":
                newFile();
                break;
            case "open":
                openFile();
                break;
            case "save":
                saveFile();
                break;
            case "saveas":
                saveFileAs();
                break;
            case "exit":
                System.exit(0);
                break;
            case "undo":
                undo();
                break;
            case "redo":
                redo();
                break;
            case "cut":
                textArea.cut();
                break;
            case "copy":
                textArea.copy();
                break;
            case "paste":
                textArea.paste();
                break;
            case "find":
                findText();
                break;
        }
    }

    private void newFile() {
        textArea.setText("");
        currentFile = null;
        setTitle("Advanced Notepad");
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                textArea.read(reader, null);
                setTitle(currentFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "File could not be opened.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                textArea.write(writer);
                setTitle(currentFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "File could not be saved.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            saveFileAs();
        }
    }

    private void saveFileAs() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            saveFile();
        }
    }

    private void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    private void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }

    private void findText() {
        String searchTerm = JOptionPane.showInputDialog(this, "Enter text to find:");
        if (searchTerm != null) {
            String content = textArea.getText();
            int index = content.indexOf(searchTerm);
            if (index != -1) {
                textArea.setCaretPosition(index);
                textArea.setSelectionStart(index);
                textArea.setSelectionEnd(index + searchTerm.length());
            } else {
                JOptionPane.showMessageDialog(this, "Text not found.", "Find", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdvancedNotepad());
    }
}
