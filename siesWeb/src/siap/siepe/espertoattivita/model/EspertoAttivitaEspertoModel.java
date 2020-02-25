package siap.siepe.espertoattivita.model;

/**
 * <p>
 * Title: EspertoAttivitaModel
 * </p>
 * <p>
 * Description: Classe Model atto a contenere i dati dell'entità Esperto_Attivita più alcuni dati estratti
 * dall'entità Esperto.
 * </p>
 * La classe estende EspertoAttivitaModel al fine di aggiungere i dati ricavati da operazioni di lettura dalle
 * due tabelle in join: Esperto_Attivita ed Esperto.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class EspertoAttivitaEspertoModel extends EspertoAttivitaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5845270402567196776L;

	private String mCognome;
	private String mNome;

	// COSTRUTTORE DI DEFAULT
	public EspertoAttivitaEspertoModel() {
		super();
		this.mCognome = "";
		this.mNome = "";
	}

	// COSTRUTTORE DI COPIA
	public EspertoAttivitaEspertoModel(EspertoAttivitaEspertoModel aModel) {
		super(aModel);
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
	}

	// COSTRUTTORE MODEL
	/*
	 * public EspertoAttivitaEspertoModel ( Date aDataInizio, Date aDataFine, String aCodOperatoreInserimento,
	 * Date aDataInserimento, String aCodUfficioInserimento, String aDescrUfficioInserimento, String
	 * aCodOperatoreAggiornamento, Date aDataAggiornamento, String aCodUfficioAggiornamento, String
	 * aDescrUfficioAggiornamento, BigDecimal aEspIdEsperto, BigDecimal aAttIdAttivita) { }
	 */
	//
	// METODI GET()
	//

	public String getCognome() {
		return mCognome;
	}

	public String getNome() {
		return mNome;
	}

	//
	// METODI SET()
	//
	public void setCognome(String aValore) {
		mCognome = aValore;
	}

	public void setNome(String aValore) {
		mNome = aValore;
	}

}