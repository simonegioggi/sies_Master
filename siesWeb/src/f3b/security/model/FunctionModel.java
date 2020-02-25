package f3b.security.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: Classe Function Model.
 * </p>
 * <p>
 * Description: Classe dati di una funzione.
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FunctionModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7074816436596413488L;

	private BigDecimal mFunctionId;
	private String mDescription;
	private String mNameAction;
	private String mLabelFunction;
	private String mFunctionType;
	private BigDecimal mVisualizationOrder;
	private String mVisualizzazionType;

	private String mImmagine; // path all'eventuale immagine da visualizzare

	private LinkedList mAncestorsFunctions;
	private ArrayList mDaughtersFunctions;

	// COSTRUTTORE DI DEFAULT
	/**
	 * Costruttore di Classe.
	 */
	public FunctionModel() {
		this.mFunctionId = null;
		this.mDescription = "";
		this.mNameAction = "";
		this.mLabelFunction = "";
		this.mFunctionType = "";
		this.mVisualizationOrder = null;
		this.mVisualizzazionType = "";
		this.mImmagine = "";

		this.mAncestorsFunctions = new LinkedList();
		this.mDaughtersFunctions = new ArrayList();
	}

	// COSTRUTTORE
	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aFunctionId
	 *            ID della funzione, scritta nel Repository.
	 */
	public FunctionModel(BigDecimal aFunctionId) {
		this.mFunctionId = aFunctionId;
		this.mDescription = "";
		this.mNameAction = "";
		this.mLabelFunction = "";
		this.mFunctionType = "";
		this.mVisualizationOrder = null;
		this.mVisualizzazionType = "";
		this.mImmagine = "";

		this.mAncestorsFunctions = new LinkedList();
		this.mDaughtersFunctions = new ArrayList();
	}

	// COSTRUTTORE MODEL
	/**
	 * Costruttore di classe con parametri.
	 * <p>
	 * 
	 * @param aFunctionId
	 *            id della funzione, contenuta nel repository.
	 * @param aDescription
	 *            descrizione della funzione.
	 * @param aNameAction
	 *            nome dell'azione in contesto java.
	 * @param aLabelFunction
	 *            label di funzione.
	 * @param aFunctionType
	 *            tipo della funzione.
	 * @param aVisualizationOrder
	 *            ordine di visualizzazione.
	 * @param aVisualizzazionType
	 *            tipo di visualizzazione.
	 */
	public FunctionModel(BigDecimal aFunctionId, String aDescription, String aNameAction,
			String aLabelFunction, String aFunctionType, BigDecimal aVisualizationOrder,
			String aVisualizzazionType, String aImmagine) {
		this.mFunctionId = aFunctionId;
		this.mDescription = aDescription;
		this.mNameAction = aNameAction;
		this.mLabelFunction = aLabelFunction;
		this.mFunctionType = aFunctionType;
		this.mVisualizationOrder = aVisualizationOrder;
		this.mVisualizzazionType = aVisualizzazionType;
		this.mImmagine = aImmagine;
		this.mAncestorsFunctions = new LinkedList();
		this.mDaughtersFunctions = new ArrayList();
	}

	//
	// METODI GET()
	//

	/**
	 * Ritorna l'id della funzione.
	 * <p>
	 * 
	 * @return l'id della funzione.
	 */
	public BigDecimal getFunctionId() {
		return mFunctionId;
	}

	/**
	 * Ritorna la descrizione.
	 * <p>
	 * 
	 * @return la descrizione.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Ritorna il nome dell'azione.
	 * <p>
	 * 
	 * @return il nome della funzione.
	 */
	public String getNameAction() {
		return mNameAction;
	}

	/**
	 * Ritorna l'etichetta della funzione.
	 * <p>
	 * 
	 * @return l'etichetta della funzione.
	 */
	public String getLabelFunction() {
		return mLabelFunction;
	}

	/**
	 * Ritorna il tipo di funzione.
	 * <p>
	 * 
	 * @return il tipo di funzione.
	 */
	public String getFunctionType() {
		return mFunctionType;
	}

	/**
	 * Ritorna il numero dell'ordine di visualizzazione.
	 * <p>
	 * 
	 * @return il numero dell'ordine di visualizzazione.
	 */
	public BigDecimal getVisualizationOrder() {
		return mVisualizationOrder;
	}

	/**
	 * Ritorna il tipo di visualizzazione.
	 * <p>
	 * 
	 * @return il tipo di visualizzazione.
	 */
	public String getVisualizzazionType() {
		return mVisualizzazionType;
	}

	/**
	 * Ritorna le funzioni antenate.
	 * <p>
	 * 
	 * @return le funzioni antenate.
	 */
	public LinkedList getAncestorsFunctions() {
		return mAncestorsFunctions;
	}

	/**
	 * Ritorna le funzioni figlie.
	 * <p>
	 * 
	 * @return le funzioni figlie.
	 */
	public ArrayList getDaughtersFunctions() {
		return mDaughtersFunctions;
	}

	/**
	 * Ritorna il numero delle funzioni figlie.
	 * <p>
	 * 
	 * @return il numero delle funzioni figlie.
	 */
	public int getDaughtersNumber() {
		return mDaughtersFunctions.size();
	}

	/**
	 * Ritorna il path all'immagine da visualizzare
	 * 
	 * @return
	 */
	public String getImmagine() {
		return mImmagine;
	}
	//
	// METODI SET()
	//

	/**
	 * Imposta l'id della funzione.
	 * <p>
	 * 
	 * @param aValue
	 *            l'id della funzione.
	 */
	public void setFunctionId(BigDecimal aValue) {
		mFunctionId = aValue;
	}

	/**
	 * Imposta la descrizione.
	 * <p>
	 * 
	 * @param aValue
	 *            la descrizione.
	 */
	public void setDescription(String aValue) {
		mDescription = aValue;
	}

	/**
	 * Imposta il nome dell'azione.
	 * <p>
	 * 
	 * @param aValue
	 *            nome dell'azione.
	 */
	public void setNameAction(String aValue) {
		mNameAction = aValue;
	}

	/**
	 * Imposta l'etichetta della funzione.
	 * <p>
	 * 
	 * @param aValue
	 *            etichetta della funzione.
	 */
	public void setLabelFunction(String aValue) {
		mLabelFunction = aValue;
	}

	/**
	 * Imposta il tipo della funzione.
	 * <p>
	 * 
	 * @param aValue
	 *            il tipo della funzione.
	 */
	public void setFunctionType(String aValue) {
		mFunctionType = aValue;
	}

	/**
	 * Imposta l'ordine di visualizzazione.
	 * <p>
	 * 
	 * @param aValue
	 *            l'ordine di visualizzazione.
	 */
	public void setVisualizationOrder(BigDecimal aValue) {
		mVisualizationOrder = aValue;
	}

	/**
	 * Imposta il tipo di visualizzazione.
	 * <p>
	 * 
	 * @param aValue
	 *            il tipo di visualizzazione.
	 */
	public void setVisualizzazionType(String aValue) {
		mVisualizzazionType = aValue;
	}

	/**
	 * Imposta le funzioni antenate.
	 * <p>
	 * 
	 * @param aValue
	 *            le funzioni antenate.
	 */
	public void setAncestorsFunctions(LinkedList aValue) {
		mAncestorsFunctions = aValue;
	}

	/**
	 * Imposta le funzioni figlie.
	 * <p>
	 * 
	 * @param aValue
	 *            imposta le funzioni figlie.
	 */
	public void setDaughtersFunctions(ArrayList aValue) {
		mDaughtersFunctions = aValue;
	}

	/**
	 * Imposta il path relativo all'immagine associata con la voce di Menu
	 * 
	 * @param aValore
	 */
	public void setImmagine(String aValore) {
		mImmagine = aValore;
	}

	/**
	 * Aggiunge una funzione padre nell'insime degli antenati.
	 * <p>
	 * 
	 * @param aFatherFunction
	 *            funzione padre.
	 */
	public void addAncestor(FunctionModel aFatherFunction) {
		mAncestorsFunctions.addLast(aFatherFunction);
	}

	/**
	 * Verifica se una determinata funzione esiste tra le funzioni figlie
	 * <p>
	 * 
	 * @param aFunctionId
	 *            id funzione da verificare.
	 * @return l'esito della ricerca.
	 */
	public boolean existFunction(BigDecimal aFunctionId) {
		if ((mDaughtersFunctions == null) || (mDaughtersFunctions.size() == 0))
			return false;

		FunctionModel lFun = new FunctionModel(aFunctionId);

		Iterator lIt = mDaughtersFunctions.iterator();
		FunctionModel lDaughterFun = null;

		// per ogni funzione figlia
		while (lIt.hasNext()) {
			lDaughterFun = (FunctionModel) lIt.next();

			// verifica l'uguaglianza
			if (lFun.equals(lDaughterFun))
				return true;

			// verifica le funzioni figlie
			if (lDaughterFun.existFunction(aFunctionId))
				return true;
		}

		return false;
	}

	/**
	 * Ritorna il contenuto del model come unica stringa.
	 * <p>
	 * 
	 * @return ritorna il contenuto degli attributi di classe.
	 */
	public String toString() {
		String lToString = this.mFunctionId + " - " + this.mDescription + " - " + this.mNameAction + " - "
				+ this.mLabelFunction + " - " + this.mFunctionType + " - " + this.mVisualizationOrder + " - "
				+ this.mVisualizzazionType + " - " + this.mImmagine;

		return lToString;
	}

	/**
	 * Effettua l'uguaglinaza di una istanza di <code>FunctionModel</code>
	 * <p>
	 * 
	 * @param aObj
	 *            istanza di <code>FunctionModel</code>.
	 * @return ritorna l'esito.
	 */
	public boolean equals(Object aObj) {
		boolean lIsEquals = false;

		if (aObj != null && aObj instanceof FunctionModel) {
			FunctionModel lModel = (FunctionModel) aObj;

			lIsEquals = mFunctionId.equals(lModel.getFunctionId());
		}

		return lIsEquals;
	}

}