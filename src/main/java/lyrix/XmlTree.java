package lyrix;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.swing.tree.DefaultMutableTreeNode;

public class XmlTree extends JPanel{
    private JTree tree;

//    private DefaultMutableTreeNode rootNode;

    public XmlTree(){
        addTree("C:\\Users\\BASS4x4\\IntelliJIDEAProjects\\xmlparse\\src\\main\\resources\\example.xml");
        //tree = new JTree();
        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
    }

    public void addTree(String xmlPath) {
        //tree.setModel(null); //сбросить дерево
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("root"); //корень дерева
        tree = new JTree(node);
        try{
            buildTree(xmlPath, node); //построить дерево
        }catch(ParserConfigurationException e){
            e.printStackTrace();
        }catch(SAXException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void buildTree(String xmlPath, DefaultMutableTreeNode node) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        File file = null;

        try {
            file = getFileResource("example2.xml");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Document document = builder.parse(file);
        Element e = document.getDocumentElement();

        if(e.hasChildNodes()){
//            DefaultMutableTreeNode root = new DefaultMutableTreeNode(e.getTagName());
            NodeList children = e.getChildNodes();
            for(int i = 0; i < children.getLength(); i++){
                Node child = children.item(i);
                addNode(child,node);
            }
        }
    }

    public void addNode(Node child, DefaultMutableTreeNode parent){
        short type = child.getNodeType();
        if(type == Node.ELEMENT_NODE){
            Element e = (Element)child;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(e.getTagName());
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
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(textContent);
                parent.add(node);
            }
        }
    }

    // If expand is true, expands all nodes in the tree.
// Otherwise, collapses all nodes in the tree.
    public void expandAll(boolean expand) {
//        tree.setModel(null);

        TreeNode root = (TreeNode) tree.getModel().getRoot();
        // Traverse tree from root
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

    private File getFileResource(String fileName)
    {
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {
            return new File(resource.getFile());
        }
    }

}
