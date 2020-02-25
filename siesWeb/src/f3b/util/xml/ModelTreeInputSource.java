package f3b.util.xml;

import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.InputSource;

/**
 * <p>Title: ModelTreeInputSource.java</p>
 * <p>Description: Classe derivata da InputSource per il ParserTreeModel</p>
 * <p>Copyright: Bull Italia S.p.A. Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class ModelTreeInputSource extends InputSource
{
  /**
   * Struttura TreeNode associata al ModelTreeInputSource
   */
  private DefaultMutableTreeNode mTreeNode;

  public ModelTreeInputSource(DefaultMutableTreeNode aModel)
  {
    mTreeNode = aModel;
  }

  public DefaultMutableTreeNode getTreeNode()
  {
    return mTreeNode;
  }

  public void setTreeNode(DefaultMutableTreeNode aTreeNode)
  {
    mTreeNode = aTreeNode;
  }

}