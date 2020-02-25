package siap.sico.decodifiche.model;

import java.util.Comparator;

import f3b.model.DecodeModel;

public class DecodificheModel extends DecodeModel implements Comparable<DecodificheModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3507348252308174360L;
	private String mContesto;
	private String mFiltro;
	private String mCodiceAlternativo;
	private String mCodiceAlt2;
	private String mCodiceAlt3;
	private String mCodiceAlt4;
	private String mCodiceAlt5;

	/**
	 * Costruttore di classe.
	 */
	public DecodificheModel() {
		super(new String(), new String());
		this.mContesto = new String();
		this.mFiltro = new String();
		this.mCodiceAlternativo = new String();
		this.mCodiceAlt2 = new String();
		this.mCodiceAlt3 = new String();
		this.mCodiceAlt4 = new String();
		this.mCodiceAlt5 = new String();
	}

	/**
	 * Costruttore di classe con parametro. Riceve un oggetto <code>DecodificheModel</code>
	 * <p>
	 * 
	 * @param aDecModel
	 *            oggetto <code>DecodificheModel</code>.
	 */
	public DecodificheModel(DecodificheModel aDecModel) {
		super(aDecModel.mCode, aDecModel.mDescription);
		this.mContesto = aDecModel.mContesto;
		this.mFiltro = aDecModel.mFiltro;
		this.mCodiceAlternativo = aDecModel.mCodiceAlternativo;
		this.mCodiceAlt2 = aDecModel.mCodiceAlt2;
		this.mCodiceAlt3 = aDecModel.mCodiceAlt3;
		this.mCodiceAlt4 = aDecModel.mCodiceAlt4;
		this.mCodiceAlt5 = aDecModel.mCodiceAlt5;
	}

	/**
	 * Costruttore di classe con parametri.
	 * <p>
	 * 
	 * @param aCodice
	 *            codice
	 * @param aDescrizione
	 *            descrizione.
	 * @param aContesto
	 *            contesto.
	 */
	public DecodificheModel(String aCodice, String aDescrizione, String aContesto, String aFiltro,
			String aCodiceAlternativo, String aCodiceAlt2, String aCodiceAlt3, String aCodiceAlt4,
			String aCodiceAlt5) {
		super(aCodice, aDescrizione);
		this.mContesto = aContesto;
		this.mFiltro = aFiltro;
		this.mCodiceAlternativo = aCodiceAlternativo;
		this.mCodiceAlt2 = aCodiceAlt2;
		this.mCodiceAlt3 = aCodiceAlt3;
		this.mCodiceAlt4 = aCodiceAlt4;
		this.mCodiceAlt5 = aCodiceAlt5;
	}

	//
	// METODI GET()
	//

	/**
	 * Ritorna il contesto di decodifica.
	 * <p>
	 * 
	 * @return il contesto.
	 */
	public String getContesto() {
		return this.mContesto;
	}

	public String getFiltro() {
		return this.mFiltro;
	}

	public String getCodiceAlternativo() {
		return this.mCodiceAlternativo;
	}

	public String getCodiceAlt2() {
		return this.mCodiceAlt2;
	}

	public String getCodiceAlt3() {
		return this.mCodiceAlt3;
	}

	public String getCodiceAlt4() {
		return this.mCodiceAlt4;
	}

	public String getCodiceAlt5() {
		return this.mCodiceAlt5;
	}

	//
	// METODI SET()
	//

	/**
	 * Imposta il contesto, al reltivo membro di classe
	 * <p>
	 * 
	 * @param aValore
	 *            il valore da impostare al relativo menbro di classe.
	 */
	public void setContesto(String aValore) {
		this.mContesto = aValore;
	}

	public void setFiltro(String aValore) {
		this.mFiltro = aValore;
	}

	public void setCodiceAlternativo(String aValore) {
		this.mCodiceAlternativo = aValore;
	}

	public void setCodiceAlt2(String aValore) {
		this.mCodiceAlt2 = aValore;
	}

	public void setCodiceAlt3(String aValore) {
		this.mCodiceAlt3 = aValore;
	}

	public void setCodiceAlt4(String aValore) {
		this.mCodiceAlt4 = aValore;
	}

	public void setCodiceAlt5(String aValore) {
		this.mCodiceAlt5 = aValore;
	}

	/**
	 * Rimarrà questo metodo ?
	 * <p>
	 * 
	 * @param aObj
	 * @return
	 */
	public boolean equals(Object aObj) {
		if ((aObj != null) && (aObj instanceof DecodificheModel)) {
			DecodificheModel lDecMod = (DecodificheModel) aObj;

			if (mContesto.equals(lDecMod.getContesto()) && super.mCode.equals(lDecMod.getCode()))
				return true;
			else
				return false;
		} else
			return false;
	}

	@Override
	public int compareTo(DecodificheModel arg0) {
		return 0;
	}

	public static class OrderByDescrizione implements Comparator<DecodificheModel> {

		@Override
		public int compare(DecodificheModel o1, DecodificheModel o2) {
			return o1.mDescription.compareTo(o2.mDescription);

		}
	}

}