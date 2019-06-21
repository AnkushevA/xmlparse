package lyrix;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.soap.Text;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;

public class TreeMenu extends JPanel {
    private JTree tree;
    private NodeEditorListener nodeEditorListener;


    public void nodeChanded(DefaultMutableTreeNode node){
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        model.reload();
    }

    void setNodeEditorListener(NodeEditorListener nodeEditorListener) {
        this.nodeEditorListener = nodeEditorListener;
    }

    TreeMenu() {
        drawTree("C:\\Users\\BASS4x4\\IntelliJIDEAProjects\\xmlparse\\src\\main\\resources\\example1.xml");
        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
    }

    void update(String xmlPath) {
        try{
            DefaultMutableTreeNode node = buildTree(xmlPath); //построить дерево
            tree.setModel(new DefaultTreeModel(node));

        }catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }
    }

    private void drawTree(String xmlPath) {
        try{
            DefaultMutableTreeNode node = buildTree(xmlPath); //построить дерево
            tree = new JTree(new DefaultTreeModel(node));
            CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
            tree.setCellRenderer(renderer);

            tree.setCellEditor(new CheckBoxNodeEditor(tree, this));
            tree.setEditable(true);
        }catch(ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }


    }

    private DefaultMutableTreeNode buildTree(String xmlPath) throws ParserConfigurationException, SAXException, IOException {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("root"); //корень дерева

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        File file = new File(xmlPath);

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

    private void addNode(Node child, DefaultMutableTreeNode parent){ //todo открывается не только корень
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

    void showEditFields(DefaultMutableTreeNode node, JTree tree){
        nodeEditorListener.showEditFields(node, tree);
    }

    void expandAll(boolean expand) {
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

    void makeXML() {
        try {
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage soapMsg = factory.createMessage();
            SOAPPart part = soapMsg.getSOAPPart();

            SOAPEnvelope envelope = part.getEnvelope();
//            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
            int childCount = root.getChildCount();

            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (tree.getModel().getChild(root, i));
                TextFieldNode userObject = (TextFieldNode) treeNode.getUserObject();
                if (userObject.getAttribute().toLowerCase().contains("body")) {
                    if (!treeNode.isLeaf()) {
                        TreeModel treeModel = tree.getModel();
                        int count = treeNode.getChildCount();
                        for (int j = 0; j < count; j++) {
                            DefaultMutableTreeNode bodyChild = (DefaultMutableTreeNode) (treeModel.getChild(treeNode, j));
                            SOAPBodyElement element = body.addBodyElement(envelope.createName(userObject.getAttribute()));
                            addChildXMLNode(element, bodyChild);
                        }
                    }
                }
            }
            soapMsg.writeTo(System.out);
            FileOutputStream fOut = new FileOutputStream("C:\\Users\\BASS4x4\\IntelliJIDEAProjects\\xmlparse\\src\\main\\resources\\outputXML.xml");
            soapMsg.writeTo(fOut);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addChildXMLNode(SOAPElement parent, DefaultMutableTreeNode node) {
        TextFieldNode textFieldNode = (TextFieldNode) node.getUserObject();
        if (textFieldNode.isIncluded()) {
            SOAPElement soapElement = null;
            try {
                soapElement = parent.addChildElement(textFieldNode.getAttribute()).addTextNode(textFieldNode.getText());
            } catch (SOAPException e) {
                e.printStackTrace();
            }
            if (!node.isLeaf()) {
                TreeModel treeModel = tree.getModel();
                int childCount = node.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (treeModel.getChild(node, i));
                    addChildXMLNode(soapElement, treeNode);
                }
            }
        }
    }

    void createSoapMessage() {
        try{
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage soapMsg = factory.createMessage();
            SOAPPart part = soapMsg.getSOAPPart();

            SOAPEnvelope envelope = part.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

//            header.addTextNode("Training Details");

            SOAPBodyElement element = body.addBodyElement(envelope.createName("JAVA", "training", "https://jitendrazaa.com/blog"));
            element.addChildElement("WS").addTextNode("Training on Web service");

            SOAPBodyElement element1 = body.addBodyElement(envelope.createName("JAVA", "training", "https://jitendrazaa.com/blog"));
            element1.addChildElement("Spring").addTextNode("Training on Spring 3.0");

            soapMsg.writeTo(System.out);

            FileOutputStream fOut = new FileOutputStream("SoapMessage.xml");
            soapMsg.writeTo(fOut);

            System.out.println();
            System.out.println("SOAP msg created");

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
