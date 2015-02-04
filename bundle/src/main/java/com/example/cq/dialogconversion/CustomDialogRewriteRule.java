package com.example.cq.dialogconversion;

import com.adobe.cq.dialogconversion.AbstractDialogRewriteRule;
import com.adobe.cq.dialogconversion.DialogRewriteException;
import com.day.cq.commons.jcr.JcrUtil;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.Set;

import static com.adobe.cq.dialogconversion.DialogRewriteUtils.copyProperty;
import static com.adobe.cq.dialogconversion.DialogRewriteUtils.hasPrimaryType;
import static com.adobe.cq.dialogconversion.DialogRewriteUtils.hasXtype;
import static com.adobe.cq.dialogconversion.DialogRewriteUtils.rename;

/**
 * Implements a sample custom rewrite rule. <code>AbstractDialogRewriteRule</code> implements the
 * <code>getRanking</code> method using the <code>service.ranking</code> OSGi property. Alternatively, implement
 * the <code>DialogRewriteRule</code> interface directly.
 */
@Component
@Service
@Properties({
        @Property(name="service.ranking", intValue = 20)
})
public class CustomDialogRewriteRule extends AbstractDialogRewriteRule {

    private static final String PRIMARY_TYPE = "cq:Widget";
    private static final String XTYPE = "custom-xtype";

    /**
     * Matches nodes with jcr:primaryType = "cq:Widget" and xtype = "custom-xtype".
     */
    public boolean matches(Node root)
            throws RepositoryException {
        return hasPrimaryType(root, PRIMARY_TYPE) && hasXtype(root, XTYPE);
    }

    /**
     * Rewrites the matched subtree.
     */
    public Node applyTo(Node root, Set<Node> finalNodes)
            throws DialogRewriteException, RepositoryException {
        // save reference to parent node and node name
        Node parent = root.getParent();
        String name = root.getName();

        // rename the root of the tree to rewrite
        rename(root);

        // add a new root with the same name as the original tree
        Node newRoot = parent.addNode(name, "nt:unstructured");

        // add new nodes / properties etc.
        newRoot.setProperty("sling:resourceType", "granite/ui/components/custom");
        Node foo = newRoot.addNode("foo", "nt:unstructured");
        foo.setProperty("bar", 10);

        // copy / convert properties from the original tree
        copyProperty(root, "someName", newRoot, "someNewName");

        // copy nodes from the original tree
        if (root.hasNode("items")) {
            NodeIterator iterator = root.getNode("items").getNodes();
            while (iterator.hasNext()) {
                Node child = iterator.nextNode();
                JcrUtil.copy(child, foo, child.getName());
            }
        }

        // etc...

        // as an optimization, optionally report which of the nodes of the resulting tree are final
        // and don't need to be reconsidered again
        finalNodes.add(newRoot);
        finalNodes.add(foo);

        // remove old root and return new one
        root.remove();
        return newRoot;
    }

}
