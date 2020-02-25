package f3b.web.html;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import f3b.model.DecodeModel;

/**
 * <p>
 * Title: Options.java
 * </p>
 * <p>
 * Description: Classe di utilità per la costrutione iterativa dei Tag Options, della Combo Box o Lista a
 * Selezione Multpila.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class Option {

	public static final boolean BLANK_ITEM = true;
	public static final boolean NO_BLANK_ITEM = false;
	public static final int DEFAULT_DESCR_LENGTH = 40;

	private Collection mValues;
	private String[] mSelecteds;
	private String[] mFilter;

	private boolean mAddBlankItem = NO_BLANK_ITEM;
	private String mValueBlankItem = "";
	private boolean mAdjustDescrLenght = false;
	private int mMaxDescrLength = DEFAULT_DESCR_LENGTH;

	/**
	 * Costruttore di classe.
	 */
	public Option() {
	}

	/**
	 * Costruttore di classe con parametro.
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme delle istanze <code>DecodeModel</code> relative ai dati da processare.
	 */
	public Option(Collection aValues) {
		this.mValues = aValues;
	}

	/**
	 * Costruttore di classe con l'insieme dei valori da visualizzare con il parametro di lunghezza massima da
	 * visualizzare per la descrizione.
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme delle istanze <code>DecodeModel</code> relative ai dati da processare.
	 * @param aMaxDescrLength
	 *            Numero massimo caratteri da visualizzare per la descrizione.
	 */
	public Option(Collection aValues, int aMaxDescrLength) {
		this(aValues);

		this.mAdjustDescrLenght = true;
		this.mMaxDescrLength = aMaxDescrLength;
	}

	/**
	 * Costruttore con parametri.
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme delle istanze <code>DecodeModel</code> relative ai dati da processare.
	 * @param aAddBlankItem
	 *            Impostato a <code>true</code> aggiunge un elemento vuoto alla lista da generare, per default
	 *            è <code>false</code>.
	 */
	public Option(Collection aValues, boolean aAddBlankItem) {
		this.mValues = aValues;
		this.mAddBlankItem = aAddBlankItem;
	}

	/**
	 * Costruttore con parametri.
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme delle istanze <code>DecodeModel</code> relative ai dati da processare.
	 * @param aSelected
	 *            Valore di riferimento per posizionamento su item corrispondente.
	 */
	public Option(Collection aValues, String aSelected) {
		this.mValues = aValues;
		this.mSelecteds = new String[1];
		this.mSelecteds[0] = aSelected;
	}

	/**
	 * Costruttore con parametri.
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme delle istanze <code>DecodeModel</code> relative ai dati da processare.
	 * @param aSelected
	 *            Valore di riferimento per posizionamento su item corrispondente.
	 * @param aMaxDescrLength
	 *            Numero massimo caratteri da visualizzare per la descrizione.
	 */
	public Option(Collection aValues, String aSelected, int aMaxDescrLength) {
		this(aValues, aSelected);

		this.mAdjustDescrLenght = true;
		this.mMaxDescrLength = aMaxDescrLength;
	}

	/**
	 * Costruttore con parametri.
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme delle istanze <code>DecodeModel</code> relative ai dati da processare.
	 * @param aSelecteds
	 *            Valore di riferimento per posizionamento su item corrispondente.
	 */
	public Option(Collection aValues, String[] aSelecteds) {
		this.mValues = aValues;
		this.mSelecteds = aSelecteds;
	}

	/**
	 * Costruttore con parametri
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme delle istanze <code>DecodeModel</code> relative ai dati da processare.
	 * @param aSelected
	 *            Valore di riferimento per posizionamento su item corrispondente.
	 * @param aAddBlankItem
	 *            Impostato a <code>true</code> aggiunge un elemento vuoto alla lista da generare, per default
	 *            è <code>false</code>.
	 */
	public Option(Collection aValues, String aSelected, boolean aAddBlankItem) {
		this.mValues = aValues;
		this.mSelecteds = new String[1];
		this.mSelecteds[0] = aSelected;
		this.mAddBlankItem = aAddBlankItem;
	}

	/**
	 * Costruttore con parametri, per la costruzione di liste a selezione Multipla.
	 * <p>
	 * 
	 * @param aValues
	 *            Imsieme dei valori da iterare.
	 * @param aSelecteds
	 *            Array di selezioni effettuate.
	 * @param aAddBlankItem
	 *            aggiunge un item vuoto nella costruzione della lista.
	 */
	public Option(Collection aValues, String[] aSelecteds, boolean aAddBlankItem) {
		this.mValues = aValues;
		this.mSelecteds = aSelecteds;
		this.mAddBlankItem = aAddBlankItem;
	}

	/**
	 * Imposta un flag per indicare che nella creazione della lista dovrà inserire un item vuoto. Per ottenere
	 * il valore corrispondente si può fare riferimento alle costanti appartenenti a questa classe :
	 * <code>Option.BLANK_ITEM</code> oppure <code>Option.NO_BLANK_ITEM</code>
	 * <p>
	 * 
	 * @param aValue
	 *            flag di abilitazione.
	 */
	public void setAddBlankItem(boolean aValue) {
		this.mAddBlankItem = aValue;
	}

	/**
	 * Imposta il valore da confrontare con gli elementi della lista in caso di occorrenza pone la stringa
	 * <code>selected</code>, nella creazione della lista.
	 * <p>
	 * 
	 * @param aValue
	 */
	public void setSelected(String aValue) {
		this.mSelecteds = new String[1];
		this.mSelecteds[0] = aValue;
	}

	/**
	 * Imposta l'array dei valori selezionati.
	 * <p>
	 * 
	 * @param aValues
	 *            Array di valori selezionati.
	 */
	public void setSelecteds(String[] aValues) {
		this.mSelecteds = aValues;
	}

	/**
	 * Restituisce l'array dei valori selezionati.
	 * <p>
	 * 
	 * @return copia dell'array delle stringhe (descrizione) selezionate.
	 */
	public String[] getSelecteds() {
		String[] lCopySel = null;
		if (mSelecteds != null && mSelecteds.length > 0) {
			lCopySel = new String[mSelecteds.length];
			for (int i = 0; i < lCopySel.length; i++)
				lCopySel[i] = new String();

			// Copia nell'array delle descrizioni delle voci selezionate
			if (mValues != null) {
				Iterator itx = mValues.iterator();

				while (itx.hasNext()) {
					DecodeModel decodeModel = (DecodeModel) itx.next();
					int ind = Arrays.binarySearch(mSelecteds, decodeModel.getCode());
					if (ind > -1)
						lCopySel[ind] = new String(decodeModel.getDescription());
				}
			}
		}
		return lCopySel;
	}

	/**
	 * Imposta l'array di filtro per estrazione elementi desiderati.
	 * <p>
	 * 
	 * @param aValues
	 *            array di stringe, cui passare i codici da estrarre.
	 */
	public void setFilter(String[] aValues) {
		this.mFilter = aValues;
	}

	/**
	 * Imposta il valore di filtro per estrazione elementi desiderati. aValue è la stringa cui passare il
	 * codice da estrarre.
	 * <p>
	 * 
	 * @param aValue
	 *            stringa per cui filtrare.
	 */
	public void setFilter(String aValue) {
		this.mFilter = new String[1];
		this.mFilter[0] = aValue;
	}

	/**
	 * Imposta il codice dell'Item-Blak nel caso lo si voglia diverso da stringa vuota ( es. "-" ).
	 * <p>
	 * 
	 * @param aValue
	 *            il valore dell' Item-Blank.
	 */
	public void setValueBlankItem(String aValue) {
		this.mValueBlankItem = aValue;
	}

	/**
	 * Imposta l'insieme dei valori da processare.
	 * <p>
	 * 
	 * @param aValues
	 *            Insieme dei valori.
	 */
	public void setValues(Collection aValues) {
		this.mValues = aValues;
	}

	/**
	 * Imposta il numero massimo di caratteri da visualizzare per la descrizione.
	 * <p>
	 * 
	 * @param aMaxDescrLength
	 *            Numero massimo caratteri da visualizzare per la descrizione.
	 */
	public void setMaxDescrLength(int aMaxDescrLength) {
		this.mMaxDescrLength = aMaxDescrLength;
	}

	/**
	 * Ritorna il numero massimo di caratteri da visualizzare per la descrizione.
	 * <p>
	 * 
	 * @return Numero massimo caratteri da visualizzare per la descrizione.
	 */
	public int getMaxDescrLength() {
		return this.mMaxDescrLength;
	}

	/**
	 * Visualizza se richiesto un numero massimo di valori per la descrizione, altrimenti restituisce la
	 * descrizione per intero.
	 * <p>
	 * 
	 * @param aDescription
	 *            La descrizione eventualmente da ridimensionare.
	 * @return stringa ridimensionata.
	 */
	private String adjustDescription(String aDescription) {
		// Se la descrizione è null oppure non è richiesto nessun aggiustamento si esce dal metodo
		if (aDescription == null || !mAdjustDescrLenght)
			return aDescription;

		String lDescription = "";

		if (aDescription.length() > mMaxDescrLength)
			lDescription = aDescription.substring(0, mMaxDescrLength) + "...";
		else
			lDescription = aDescription;

		return lDescription;
	}

	/**
	 * Ritorna crea e ritorna la lista dei valori, in formato HTML.
	 * <p>
	 * 
	 * @return ritorna la lista dei valori in formato HTML.
	 */
	public String toString() {
		String lString = new String();

		// Effettua il sort dell'array di selecteds se != null
		if (mSelecteds != null)
			Arrays.sort(mSelecteds);

		if (mFilter != null && mFilter.length > 0) {
			// Effettua il sort dell'array di filtro se != null
			Arrays.sort(mFilter);
			lString = this.buildOptionWithFilter();
		} else
			lString = this.buildOption();

		return lString;
	}

	/**
	 * Effettua la costruzione degli iteme di una Option considerando che è stato impostato un filtro di
	 * estrazione.
	 * <p>
	 * 
	 * @return ritorna la lista dei valori in formato HTML.
	 */
	private String buildOptionWithFilter() {
		StringBuffer lBuffer = new StringBuffer();
		Iterator itx = mValues.iterator();

		// Se vi sono elementi nella collection e si desidera uno spazio vuoto,
		// allora crea un item vuoto.
		if (mAddBlankItem && itx.hasNext())
			lBuffer.append("<option value = \"" + mValueBlankItem + "\" />" + mValueBlankItem + "\n");

		int count = 0;

		while (itx.hasNext()) {
			DecodeModel decodeModel = (DecodeModel) itx.next();

			if (Arrays.binarySearch(mFilter, decodeModel.getCode()) > -1) {
				lBuffer.append("<option value = \"" + decodeModel.getCode() + "\" ");

				if (mSelecteds != null)
					if (Arrays.binarySearch(mSelecteds, decodeModel.getCode()) > -1)
						lBuffer.append("selected");

				lBuffer.append(" />");
				lBuffer.append(adjustDescription(decodeModel.getDescription()) + "\n");

				count++;
			}

			// Se sono stati trovate tutte le occorenze specificate nell'array filtro
			// interrompe il ciclo
			if (count == mFilter.length)
				break;
		}

		return lBuffer.toString();
	}

	/**
	 * Effettua la costruzione dei item della option senza filtro.
	 * <p>
	 * 
	 * @return ritorna il set di item in stringa.
	 */
	private String buildOption() {
		StringBuffer lBuffer = new StringBuffer();
		if (mValues != null) {
			Iterator itx = mValues.iterator();

			// Se vi sono elementi nella collection e si desidera uno spazio vuoto,
			// allora crea un item vuoto.
			if (mAddBlankItem && itx.hasNext())
				lBuffer.append("<option value = \"" + mValueBlankItem + "\" />" + mValueBlankItem + "\n");

			while (itx.hasNext()) {
				DecodeModel decodeModel = (DecodeModel) itx.next();
				lBuffer.append("<option value = \"" + decodeModel.getCode() + "\" ");

				if (mSelecteds != null)
					if (Arrays.binarySearch(mSelecteds, decodeModel.getCode()) > -1)
						lBuffer.append("selected");

				lBuffer.append(" />");
				lBuffer.append(adjustDescription(decodeModel.getDescription()) + "\n");
			}
		}

		return lBuffer.toString();
	}

	/**
	 * Restituisce una String[] con i soli codici applicando eventualmente il filtro specificatos
	 * 
	 * @return
	 */
	public String[] getCodes() {
		Vector<String> lListaCodici = new Vector<String>();

		if (mValues != null) {
			Iterator itx = mValues.iterator();
			while (itx.hasNext()) {
				DecodeModel decodeModel = (DecodeModel) itx.next();

				if (mFilter != null) {
					if (Arrays.binarySearch(mFilter, decodeModel.getCode()) > -1)
						lListaCodici.add(decodeModel.getCode());
				} else {
					lListaCodici.add(decodeModel.getCode());
				}
			}
		}

		String[] lCodeArray = new String[lListaCodici.size()];

		lListaCodici.toArray(lCodeArray);

		return lCodeArray;
	}

}