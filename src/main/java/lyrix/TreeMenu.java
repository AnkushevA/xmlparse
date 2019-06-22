package lyrix;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.tree.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;

public class TreeMenu extends JPanel {
    private JTree tree;
    private NodeEditorListener nodeEditorListener;


    public void nodeChanded(DefaultMutableTreeNode node) {
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
        try {
            DefaultMutableTreeNode node = buildTree(xmlPath); //построить дерево
            tree.setModel(new DefaultTreeModel(node));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void drawTree(String xmlPath) {
        try {
            DefaultMutableTreeNode node = buildTree(xmlPath); //построить дерево
            tree = new JTree(new DefaultTreeModel(node));
            CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
            tree.setCellRenderer(renderer);

            tree.setCellEditor(new CheckBoxNodeEditor(tree, this));
            tree.setEditable(true);
        } catch (ParserConfigurationException | SAXException | IOException e) {
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

        if (e.hasChildNodes()) {
            NodeList children = e.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                addNode(child, node);
            }
        }
        return node;
    }

    private void addNode(Node child, DefaultMutableTreeNode parent) { //todo открывается не только корень
        short type = child.getNodeType();
        if (type == Node.ELEMENT_NODE) {
            Element e = (Element) child;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new TextFieldNode(e.getTagName(), "", true));
            parent.add(node);

            if (e.hasChildNodes()) {
                NodeList list = e.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    addNode(list.item(i), node);
                }
            }
        }
    }

    void showEditFields(DefaultMutableTreeNode node, JTree tree) {
        nodeEditorListener.showEditFields(node, tree);
    }

    void expandAll(boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(new TreePath(root), expand);
    }

    private void expandAll(TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
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

            Iterator namespacePrefixes = envelope.getNamespacePrefixes();
            while (namespacePrefixes.hasNext()) {
                envelope.removeNamespaceDeclaration(((String) namespacePrefixes.next()));
            }

//            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            soapMsg.getSOAPHeader().setPrefix("soapenv");
            body.setPrefix("soapenv");
            envelope.setPrefix("soapenv");
            envelope.addNamespaceDeclaration("car", "http://cardlibrary2.webservices.integration.css.aamsystems.com");
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
            int childCount = root.getChildCount();

            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (tree.getModel().getChild(root, i));
                TextFieldNode userObject = (TextFieldNode) treeNode.getUserObject();
                if (userObject.getAttribute().toLowerCase().contains("body")) { //узлы внутри body
                    TreeModel treeModel = tree.getModel();
                    int count = treeNode.getChildCount();
                    for (int j = 0; j < count; j++) {
                        DefaultMutableTreeNode bodyChild = (DefaultMutableTreeNode) (treeModel.getChild(treeNode, j));
                        addChildXMLNode(body, bodyChild, envelope);
                    }
                }
            }
            soapMsg.writeTo(System.out);
            FileOutputStream fOut = new FileOutputStream("C:\\Users\\BASS4x4\\IntelliJIDEAProjects\\xmlparse\\src\\main\\resources\\outputXML.xml");
            soapMsg.writeTo(fOut);
        } catch (SOAPException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addChildXMLNode(SOAPElement parent, DefaultMutableTreeNode childNode, SOAPEnvelope envelope) {
        TextFieldNode childTextFieldNode = (TextFieldNode) childNode.getUserObject();
        if (childTextFieldNode.isIncluded()) {
            SOAPElement soapElement = null;
            try {
                if (parent instanceof SOAPBody){
                    soapElement = ((SOAPBody) parent).addBodyElement(envelope.createName(childTextFieldNode.getAttribute()));
                } else{
                    soapElement = parent.addChildElement (childTextFieldNode.getAttribute(), "car");
                }
            } catch (SOAPException e) {
                e.printStackTrace();
            }
            if (childNode.isLeaf()) {
                if (!childTextFieldNode.getText().isEmpty()) {
                    try {
                        soapElement.addTextNode(childTextFieldNode.getText());
                    } catch (SOAPException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                TreeModel treeModel = tree.getModel();
                int childCount = childNode.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) (treeModel.getChild(childNode, i));
                    addChildXMLNode(soapElement, treeNode, envelope);
                }
            }
        }
    }

    void createSoapMessage() {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
