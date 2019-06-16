package lyrix;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class TreeMenu extends JPanel {
    private JTree tree;

    public TreeMenu() {
        drawTree("C:\\Users\\BASS4x4\\IntelliJIDEAProjects\\xmlparse\\src\\main\\resources\\example1.xml");
        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
    }

    public void update(String xmlPath) {
        try{
            DefaultMutableTreeNode node = buildTree(xmlPath); //построить дерево
            tree.setModel(new DefaultTreeModel(node));
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }catch(SAXException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void drawTree(String xmlPath) {
        //tree.setModel(null); //сбросить дерево
        try{
            DefaultMutableTreeNode node = buildTree(xmlPath); //построить дерево
            tree = new JTree(new DefaultTreeModel(node));
            CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
            tree.setCellRenderer(renderer);

            tree.setCellEditor(new CheckBoxNodeEditor(tree));
            tree.setEditable(true);
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }catch(SAXException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


    }

    private DefaultMutableTreeNode buildTree(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("root"); //корень дерева

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File file = new File(xmlPath); //todo проверить что файл существует

        Document document = builder.parse(file);
        Element e = document.getDocumentElement();

        if(e.hasChildNodes()){
            NodeList children = e.getChildNodes();
            for(int i = 0; i < children.getLength(); i++){
                Node child = children.item(i);
                addNode(child, node);
            }
        }
        return node;
    }

    public void addNode(Node child, DefaultMutableTreeNode parent){
        short type = child.getNodeType();
        if(type == Node.ELEMENT_NODE){
            Element e = (Element)child;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new CheckBoxNode(e.getTagName(), false));
            parent.add(node);

            if(e.hasChildNodes()){
                NodeList list = e.getChildNodes();
                for(int i = 0; i < list.getLength(); i++){
                    addNode(list.item(i), node);
                }
            }
        } else if (type == Node.TEXT_NODE) {
            Text t = (Text)child;
            String textContent = t.getTextContent();
            if (!textContent.contains("\n")) { //todo убирать пробелы к конце xml
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TextFieldNode(textContent));
                parent.add(node);
            }
        }
    }

    public void expandAll(boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(new TreePath(root), expand);
    }

    private void expandAll(TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path, expand);
            }
        }
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
