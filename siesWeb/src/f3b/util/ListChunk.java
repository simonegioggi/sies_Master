package f3b.util;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * Title: ListChunck
 * </p>
 * <p>
 * Description: Questa classe serve per ritornare i risultati di un'operazione di ricerca
 * </p>
 * <p>
 * in blocchi di lunghezza prefissata.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull ITALIA S.p.A.
 * </p>
 */
@SuppressWarnings("rawtypes")
public class ListChunk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5534099025624914093L;

	/**
	 * Il numero di elementi contenuti nella lista completa
	 */
	private int mTotalElements;

	/**
	 * Gli elementi di questo blocco
	 */
	private Collection mElementsInThisList;

	/**
	 * L'indice del primo elemento contenuto nel blocco nella lista originaria
	 */
	private int mFirstElementOfThisList;

	/**
	 * Il numero di elementi contenuti nel blocco
	 */
	private int mCountOfElementsInThisList;

	/**
	 * Costruttore di default per l'instanzazione del bean.
	 */
	public ListChunk() {
		this(0, null, 0, 0);
	}

	/**
	 * Costruisce il blocco di lista.
	 * <p>
	 * 
	 * @param aCount
	 *            Numero di elementi della lista completa.
	 * @param aColl
	 *            Gli elementi da inserire nel blocco.
	 * @param aFirst
	 *            L'indice del primo elemento rispetto alla lista completa.
	 * @param aNumElements
	 *            Il numero di elementi contenuti nel blocco.
	 */
	public ListChunk(int aCount, Collection aColl, int aFirst, int aNumElements) {
		this.mTotalElements = aCount;
		this.mElementsInThisList = aColl;
		this.mFirstElementOfThisList = aFirst;
		this.mCountOfElementsInThisList = aNumElements;
	}

	/**
	 * Restituisce il numero degli elementi contenuti nella lista completa.
	 * <p>
	 * 
	 * @return il numero totale di elementi.
	 */
	public int getTotalCount() {
		return mTotalElements;
	}

	/**
	 * Restituisce gli elementi contenuti nel blocco.
	 * <p>
	 * 
	 * @return gli elementi contenuti nel blocco.
	 */
	public Collection getCollection() {
		return mElementsInThisList;
	}

	/**
	 * Restituisce il numero degli elementi contenuti nel blocco.
	 * <p>
	 * 
	 * @return il numero degli elementi contenuti nel blocco.
	 */
	public int getCurrentCount() {
		return mCountOfElementsInThisList;
	}

	/**
	 * Restituisce l'indice del primo elemento rispetto alla lista completa.
	 * <p>
	 * 
	 * @return l'indice del primo elemento rispetto alla lista completa.
	 */
	public int getFirstElementIndex() {
		return mFirstElementOfThisList;
	}

}