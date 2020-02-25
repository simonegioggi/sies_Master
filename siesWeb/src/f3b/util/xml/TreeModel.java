package f3b.util.xml;

import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.model.GenericModel;

/**
 * <p>
 * Title: TreeModel
 * </p>
 * <p>
 * Description: Classe Elemento di una struttura Albero
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A. Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class TreeModel extends DefaultMutableTreeNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9044859666167151805L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Attributo GenericModel associato all'Elemento Nodo
	 */
	protected GenericModel mModel;

	/**
	 * Attributo mark di un Elemento nodo
	 */
	protected boolean mMark = false;

	public TreeModel() {
		mModel = null;
		mMark = false;
	}

	public TreeModel(GenericModel aModel) {
		mModel = aModel;
		mMark = false;
	}

	public GenericModel getModel() {
		return mModel;
	}

	public void setModel(GenericModel aModel) {
		mModel = aModel;
	}

	public void setMark() {
		mMark = true;
	}

	public boolean getMark() {
		return mMark;
	}

	public void add(TreeModel newChild) {
		if (newChild != null) {
			if (newChild.getModel() != null)
				super.add(newChild);
		}
	}

	/**
	 * Ricerca non ricorsiva su due livelli di un certo Model sui nodi del TreeModel
	 */
	public TreeModel findTreeModel(TreeModel aTreeModel, GenericModel aModel) {
		try {
//			TreeModel lReturn = null;

//			int nChildCount = aTreeModel.getChildCount();
			Enumeration lChildRoot = aTreeModel.children();

//			int lDepth = aTreeModel.getDepth();

			// Ciclo di primo livello sui figli della root
			while (lChildRoot.hasMoreElements()) {
				TreeModel lTNod = (TreeModel) lChildRoot.nextElement();

				if (!lTNod.isLeaf() && lTNod.getChildCount() > 0) {
					Enumeration lEnumCH2 = lTNod.children();

					// Ciclo a livello 2
					while (lEnumCH2.hasMoreElements()) {
						TreeModel lTNod2 = (TreeModel) lEnumCH2.nextElement();

						if (lTNod2.getModel().getClass().getName().equals(aModel.getClass().getName())) {
							return lTNod2;
						}
					}// fine while 2 livello
				}
				if (lTNod.getModel().getClass().getName().equals(aModel.getClass().getName())) {
					return lTNod;
				}
			}// fine for sui figli della root
			return null;
		} catch (Exception ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error(this.getClass().getName(), ex);
			ex.printStackTrace();
		}

		return null;
	}

}