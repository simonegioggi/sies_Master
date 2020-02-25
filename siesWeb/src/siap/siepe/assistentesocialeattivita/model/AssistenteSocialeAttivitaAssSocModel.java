package siap.siepe.assistentesocialeattivita.model;

/**
 * <p>
 * Title: AssistenteSocialeAttivitaAssSocModel
 * </p>
 * <p>
 * Description: Classe Model atto a contenere i dati dell'entità ASSISTENTE_SOCIALE_ATTIVITA più alcuni dati
 * estratti dall'entità ASSISTENTE_SOCIALE.
 * </p>
 * La classe estende AssistenteSocialeAttivitaModel al fine di aggiungere i dati ricavati da operazioni di
 * lettura dalle due tabelle in join: ASSISTENTE_SOCIALE_ATTIVITA ed ASSISTENTE_SOCIALE.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class AssistenteSocialeAttivitaAssSocModel extends AssistenteSocialeAttivitaModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -5534925038729561946L;
	private String mCognome;
	private String mNome;

	// COSTRUTTORE DI DEFAULT
	public AssistenteSocialeAttivitaAssSocModel() {
		super();
		this.mCognome = "";
		this.mNome = "";
	}

	// COSTRUTTORE DI COPIA
	public AssistenteSocialeAttivitaAssSocModel(AssistenteSocialeAttivitaAssSocModel aModel) {
		super(aModel);
		this.mCognome = aModel.mCognome;
		this.mNome = aModel.mNome;
	}

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
