package lyrix;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

public class TreeMenu extends JPanel {
    private JTree tree;
    private NodeEditorListener nodeEditorListener;

    public void nodeChanded(DefaultMutableTreeNode node){
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload();
    }

    public void setNodeEditorListener(NodeEditorListener nodeEditorListener) {
        this.nodeEditorListener = nodeEditorListener;
    }

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
        try{
            DefaultMutableTreeNode node = buildTree(xmlPath); //построить дерево
            tree = new JTree(new DefaultTreeModel(node));
            CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
            tree.setCellRenderer(renderer);

            tree.setCellEditor(new CheckBoxNodeEditor(tree, this));
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
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
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

    public void addNode(Node child, DefaultMutableTreeNode parent){ //todo открывается не только корень
        short type = child.getNodeType();
        if(type == Node.ELEMENT_NODE){
            Element e = (Element)child;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TextFieldNode(e.getTagName(), "", true));
            parent.add(node);

            if(e.hasChildNodes()){
                NodeList list = e.getChildNodes();
                for(int i = 0; i < list.getLength(); i++){
                    addNode(list.item(i), node);
                }
            }
        }
    }

    public void showEditFields(DefaultMutableTreeNode node, JTree tree){
        nodeEditorListener.showEditFields(node, tree);
    }

    public void expandAll(boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(new TreePath(root), expand);
    }

    private void expandAll(TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
