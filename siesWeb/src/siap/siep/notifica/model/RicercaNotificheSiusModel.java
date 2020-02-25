package siap.siep.notifica.model;

import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.notifica.action.ICostantiNotifica;

/**
 * *
 * <p>
 * Title: RicercaNotificheSiusModel
 * </p>
 * <p>
 * Description: La Classe Model rappresenta il filtro da utilizzare nella Ricerca di Notifiche/Comunicazioni
 * per Procedimenti SIUS. I possibili parametri utilizzati nella ricerca sono: l'intervallo delle date di
 * emissione, il tipo di provvedimenti, il tipo di Autorità Destinatarie, l'utente che ha inserito la
 * Notifica/Comunicazione.
 * </p>
 * 
 * @author Lesposito
 *
 */
public class RicercaNotificheSiusModel extends NotificaFasSiusEveModel implements ICostantiNotifica {
	/**
	 *
	 */
	private static final long serialVersionUID = 4411827344388542053L;
	// Flag che indica l'esclusione dalla ricerca degli atti relativi a Decreti di Citazione
	private boolean mNoDecretoCitazione = false;
	// Flag che indica l'esclusione dalla ricerca degli atti che abbiano come destinatario Autorità UNEP
	private boolean mNoUNEP = false;
	// Flag che indica l'esclusione dalla ricerca degli atti che abbiano come destinatario Autorità UNEP della
	// sede
	private boolean mNoSedeUNEP = false;
	// Flag che indica l'esclusione dalla ricerca degli atti aventi come destinatari uffici di tipo
	// specificato nell'UfficioModel
	private boolean mNoUff = false;
	// Flag che indica l'esclusione dalla ricerca degli atti aventi come destinatari uffici di tipo e sede
	// specificato nell'UfficioModel
	private boolean mNoSedeUff = false;

	// Data iniziale dell'intervallo di ricerca
	private Date mDataIniziale;
	// Data finale dell'intervallo di ricerca
	private Date mDataFinale;

	// Codice che individua il tipo di Provvedimento e sua decodifica
	private String mTipoProvvedimento = "";
	private String mDescTipoProvvedimento = "";
	// Codice che individua tipo di Destinatari
	private String mTipoDestinatario = "";
	private String mDescTipoDestinatario = "";

	// La stringa eventualmente valorizzata individua un filtro da effettuare nella ricerca
	// sul Campo NOTE (filtro LIKE)
	private String mFiltroNote = "";

	/*
	 * Flag che stabilisce il tipo di Ordinamento: P : Ordinamento per ANNO/PROGR del Procedimento; S :
	 * Ordinamento per Cognome e Nome del Soggetto; D : ordinamento decrescente per data di inserimento .
	 */
	private String mOrdinamento = "";
	private String mDescOrdinamento = "";

	// COSTRUTTORE DI DEFAULT
	public RicercaNotificheSiusModel() {
		super();
		// Nell'Evento è sempre valorizzato il Tipo a '01'
		setEvento(new EventoModel());
		getEvento().setCodTipoEvento("01");
	}

	/*
	 * Get
	 */
	public boolean getNoDecretoCitazione() {
		return mNoDecretoCitazione;
	}

	public boolean getNoUNEP() {
		return mNoUNEP;
	}

	public boolean getNoSedeUNEP() {
		return mNoSedeUNEP;
	}

	public boolean getNoUff() {
		return mNoUff;
	}

	public boolean getNoSedeUff() {
		return mNoSedeUff;
	}

	public Date getDataIniziale() {
		return mDataIniziale;
	}

	public Date getDataFinale() {
		return mDataFinale;
	}

	public String getOrdinamento() {
		return mOrdinamento;
	}

	public String getDescOrdinamento() {
		return mDescOrdinamento;
	}

	public String getTipoProvvedimento() {
		return mTipoProvvedimento;
	}

	public String getDescTipoProvvedimento() {
		return mDescTipoProvvedimento;
	}

	public String getTipoDestinatario() {
		return mTipoDestinatario;
	}

	public String getDescTipoDestinatario() {
		return mDescTipoDestinatario;
	}

	public String getFiltroNote() {
		return mFiltroNote;
	}

	/*
	 * I seguenti get vengono inseriti per far comparire i campi relativi nell'XML. Questo poichè il Parser
	 * non utilizza i get ereditati dall'Ancestor, quindi per rendere visibili alcuni degli attrinuti
	 * ereditati bisogna riscrivere la get.
	 */
	public String getCodUtente() {
		return super.getCodOperatoreInserimento();
	}

	public String getCodUfficio() {
		return super.getCodUfficioInserimento();
	}

	@Override
	public String getCodTipoNotifica() {
		return super.getCodTipoNotifica();
	}

	// Si setta l'Ordinamento e la sua descrizione
	public void setOrdinamento(String aValue) {
		mOrdinamento = aValue;
		setDescOrdinamento(aValue);
	}

	public void setDataIniziale(Date aValore) {
		mDataIniziale = aValore;
	}

	public void setDataFinale(Date aValore) {
		mDataFinale = aValore;
	}

	// Si setta il codice del Tipo di Provvedimento e la sua decodifica
	public void setTipoProvvedimento(String aValue) {
		mTipoProvvedimento = aValue;
		setDescTipoProvvedimento(aValue);
	}

	// Si setta il codice del Tipo di destinatari e la sua decodifica
	public void setTipoDestinatario(String aValue) {
		mTipoDestinatario = aValue;
		setDescTipoDestinatario(aValue);
	}

	/**
	 * Predispone la ricerca a provvedimenti di tipo decreto di Citazione.
	 */
	public void setDecret1Citazione() {
		mNoDecretoCitazione = false;
		getEvento().setCodEsito("0601");
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a soli provvedimenti di tipo "Decreto
	 * di Citazione" .
	 *
	 * @return
	 */
	public boolean isDecretoCitazione() {
		boolean lRet = false;
		if (!mNoDecretoCitazione && getEvento() != null && getEvento().getCodEsito() != null
				&& getEvento().getCodEsito().equalsIgnoreCase("0601"))
			lRet = true;
		return lRet;
	}

	/**
	 * Predispone la ricerca a tutti i provvedimenti escluso i Decreti di Citazione.
	 */
	public void setEsclusoDecret1Citazione() {
		mNoDecretoCitazione = true;
		getEvento().setCodEsito("0601");
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a tutti i provvedimenti diversi dal
	 * "Decreto di Citazione" .
	 *
	 * @return
	 */
	public boolean isEsclusoDecret1Citazione() {
		boolean lRet = false;
		if (mNoDecretoCitazione && getEvento() != null && getEvento().getCodEsito() != null
				&& getEvento().getCodEsito().equalsIgnoreCase("0601"))
			lRet = true;
		return lRet;
	}

	/**
	 * Predispone la ricerca ad Autorità esterne UNEP.
	 */
	public void setUNEP() {
		mNoUNEP = false;
		mNoSedeUNEP = false;
		AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
		lAutorita.setCodTipoAutorita("22");
		lAutorita.setCodSede("");
		setAutoritaEsterna(lAutorita);
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a destinatari di tipo UNEP.
	 *
	 * @return
	 */

	public boolean isUNEP() {
		boolean lRet = false;
		if (!mNoUNEP && getAutoritaEsterna() != null && getAutoritaEsterna().getCodTipoAutorita() != null
				&& getAutoritaEsterna().getCodTipoAutorita().equalsIgnoreCase("22"))
			lRet = true;
		return lRet;
	}

	/**
	 * Predispone la ricerca ad Autorità esterne diverse da UNEP.
	 */
	public void setEsclusoUNEP() {
		mNoUNEP = true;
		mNoSedeUNEP = false;
		AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
		lAutorita.setCodTipoAutorita("22");
		lAutorita.setCodSede("");
		setAutoritaEsterna(lAutorita);
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a destinatari di tipo Autorità Esterne
	 * diverse da UNEP.
	 *
	 * @return
	 */

	public boolean isEsclusoUNEP() {
		boolean lRet = false;
		if (mNoUNEP && getAutoritaEsterna() != null && getAutoritaEsterna().getCodTipoAutorita() != null
				&& getAutoritaEsterna().getCodTipoAutorita().equalsIgnoreCase("22"))
			lRet = true;
		return lRet;
	}

	public void setFiltroNote(String aValue) {
		mFiltroNote = aValue;
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a destinatari di tipo Istituti di
	 * detenzione.
	 *
	 * @return
	 */

	public boolean isIstitutoDiDetenzione() {
		boolean lRet = false;
		if (getTipoDestinatario() != null && getTipoDestinatario().equalsIgnoreCase(ISTITUTO_DETENZIONE))
			lRet = true;
		return lRet;
	}

	/**
	 * Predispone la ricerca ad Autorità esterne UNEP di una sede specifica.
	 */
	public void setUNEP(String aCodSede) {
		mNoUNEP = false;
		mNoSedeUNEP = false;
		AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
		lAutorita.setCodTipoAutorita("22");
		lAutorita.setCodSede(aCodSede);
		setAutoritaEsterna(lAutorita);
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a destinatari di tipo UNEP di una sede
	 * specifica.
	 *
	 * @return
	 */

	public boolean isUNEPSede() {
		boolean lRet = false;
		if (isUNEP() && !mNoSedeUNEP && getAutoritaEsterna().getCodTipoAutorita() != null
				&& getAutoritaEsterna().getCodSede().trim().length() > 0)
			lRet = true;
		return lRet;
	}

	/**
	 * Predispone la ricerca ad Autorità esterne UNEP escludendo una sede specifica.
	 */
	public void setUNEPEsclusoSede(String aCodSede) {
		mNoUNEP = false;
		mNoSedeUNEP = true;
		AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
		lAutorita.setCodTipoAutorita("22");
		lAutorita.setCodSede(aCodSede);
		setAutoritaEsterna(lAutorita);
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a destinatari di tipo UNEP escludendo
	 * quelli di una sede specifica.
	 *
	 * @return
	 */

	public boolean isUNEPEsclusoSede() {
		boolean lRet = false;
		if (isUNEP() && mNoSedeUNEP && getAutoritaEsterna().getCodSede().trim().length() > 0)
			lRet = true;
		return lRet;
	}

	/**
	 * Predispone la ricerca filtrata per destinatari Uffici di tipo aCodUfficio.
	 */
	public void setUffDestinatario(String aCodTipoUfficio) {
		// mNoUNEP = false;
		// mNoSedeUNEP = false;
		mNoUff = false;
		mNoSedeUff = false;

		UfficioModel lUfficio = new UfficioModel();
		lUfficio.setCodTipoUfficio(aCodTipoUfficio);
		setUfficio(lUfficio);
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca per destinatari Ufficio di un tipo
	 * specifico
	 * 
	 * @return
	 */
	public boolean isUffDestinatario() {
		boolean lRet = false;
		if (!mNoUff && getUfficio() != null && getUfficio().getCodTipoUfficio() != null
				&& getUfficio().getCodTipoUfficio().trim().length() > 0)
			lRet = true;
		return lRet;
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca a destinatari di Ufficio del tipo
	 * specificato da aCodTipoUfficio.
	 *
	 * @return
	 */

	public boolean isUffDestinatario(String aCodTipoUfficio) {
		boolean lRet = false;
		if (isUffDestinatario() && getUfficio().getCodTipoUfficio().equalsIgnoreCase(aCodTipoUfficio))
			lRet = true;
		return lRet;
	}

	/**
	 * Seleziona la condizione di ricerca che escluda dai destinatari gli Uffici di un tipo specifico.
	 */
	public void setEsclusoUffDestinatario(String aCodTipoUfficio) {
		setUffDestinatario(aCodTipoUfficio);
		mNoUff = true;
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca che esluda dai destinatari gli Uffici
	 * di un tipo specifico.
	 * 
	 * @return
	 */
	public boolean isEsclusoUffDestinatario() {
		boolean lRet = false;
		if (mNoUff && getUfficio() != null && getUfficio().getCodTipoUfficio() != null
				&& getUfficio().getCodTipoUfficio().trim().length() > 0)
			lRet = true;
		return lRet;
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca che esluda dai destinatari gli Uffici
	 * del tipo specificato dall'argomento.
	 * 
	 * @return
	 *
	 */
	public boolean isEsclusoUffDestinatario(String aCodTipoUfficio) {
		boolean lRet = false;
		if (isEsclusoUffDestinatario() && getUfficio().getCodTipoUfficio() != null
				&& getUfficio().getCodTipoUfficio().equalsIgnoreCase(aCodTipoUfficio))
			lRet = true;
		return lRet;
	}

	/**
	 * Predispone la ricerca ai destinatari Uffici di tipo aCodUfficio e di sede aCodSede.
	 */
	public void setUffDestinatario(String aCodTipoUfficio, String aCodSede) {
		setUffDestinatario(aCodTipoUfficio);
		getUfficio().setCodComune(aCodSede);
	}

	/**
	 * Predispone la ricerca esludendo i destinatari Uffici di tipo aCodUfficio e di sede aCodSede.
	 */
	public void setEsclusoSedeUffDestinatario(String aCodTipoUfficio, String aCodSede) {
		setUffDestinatario(aCodTipoUfficio, aCodSede);
		mNoSedeUff = true;
	}

	/**
	 * Restituisce true se è stata selezionata la condizione di ricerca che esluda dai destinatari gli Uffici
	 * di un ed una sede specifici.
	 * 
	 * @return
	 *
	 */
	public boolean isUffEsclusoSede() {
		boolean lRet = false;
		if (isUffDestinatario() && mNoSedeUff && getUfficio().getCodComune().trim().length() > 0)
			lRet = true;
		return lRet;
	}

	/**
	 * Decodifica del flag Tipo Provvedimento che seleziona il tipo provvedimenti nella ricerca. La decodifica
	 * viene memorizzata nell'attributo mDescTipoProvvedimento. La funzione viene richiamata quando si
	 * valorizza l'attributo mDescTipoProvvedimento.
	 * 
	 * @param aValue
	 */
	private void setDescTipoProvvedimento(String aValue) {
		if (aValue != null) {
			if (aValue.equalsIgnoreCase(TUTTI))
				mDescTipoProvvedimento = "Tutti";
			else if (aValue.equalsIgnoreCase(DECRETO_CITAZIONE))
				mDescTipoProvvedimento = "Decreti di Citazione";
			else if (aValue.equalsIgnoreCase(ALTRI_PROVVEDIMENTI))
				mDescTipoProvvedimento = "Tutti tranne i Decreti di Citazione";
			else
				mDescTipoProvvedimento = "";
		}
	}

	/**
	 * Decodifica del flag Tipo Destinatario che seleziona il tipo destinatario nella ricerca. La decodifica
	 * viene memorizzata nell'attributo mDescTipoDestinatario. La funzione viene richiamata quando si
	 * valorizza l'attributo mDescTipoDestinatario.
	 * 
	 * @param aValue
	 */
	private void setDescTipoDestinatario(String aValue) {
		if (aValue != null) {
			if (aValue.equalsIgnoreCase(TUTTI))
				mDescTipoDestinatario = "Tutti";

			// UNEP
			else if (aValue.equalsIgnoreCase(UNEP_TUTTI))
				mDescTipoDestinatario = "UNEP";
			else if (aValue.equalsIgnoreCase(UNEP_SEDE))
				mDescTipoDestinatario = "UNEP SEDE";
			else if (aValue.equalsIgnoreCase(UNEP_ALTRE_SEDI))
				mDescTipoDestinatario = "UNEP Altre Sedi";
			else if (aValue.equalsIgnoreCase(UNEP_ESCLUSO))
				mDescTipoDestinatario = "Tutti tranne UNEP e tranne Istituti di Detenzione";
			// Procura Generale
			else if (aValue.equalsIgnoreCase(PGCAP_TUTTI))
				mDescTipoDestinatario = "Procura Generale";
			else if (aValue.equalsIgnoreCase(PGCAP_SEDE))
				mDescTipoDestinatario = "Procura Generale SEDE";
			else if (aValue.equalsIgnoreCase(PGCAP_ALTRE_SEDI))
				mDescTipoDestinatario = "Procura Generale Altre Sedi";
			else if (aValue.equalsIgnoreCase(PGCAP_ESCLUSO))
				mDescTipoDestinatario = "Tutti tranne Procura Generale";
			// Procura Generale
			else if (aValue.equalsIgnoreCase(PM_TUTTI))
				mDescTipoDestinatario = "Procura Repubblica c/o Tribunale Ordinario ";
			else if (aValue.equalsIgnoreCase(PM_SEDE))
				mDescTipoDestinatario = "Procura Repubblica c/o Tribunale Ordinario SEDE";
			else if (aValue.equalsIgnoreCase(PM_ALTRE_SEDI))
				mDescTipoDestinatario = "Procura Repubblica c/o Tribunale Ordinario Altre Sedi";
			else if (aValue.equalsIgnoreCase(PM_ESCLUSO))
				mDescTipoDestinatario = "Tutti tranne Procura Repubblica c/o Tribunale Ordinario ";
			else if (aValue.equalsIgnoreCase(ISTITUTO_DETENZIONE))
				mDescTipoDestinatario = "Istituto di Detenzione";
			else
				mDescTipoDestinatario = "";
		}
	}

	/**
	 * Decodifica del flag Ordinamento l'ordinamento nel risultato della ricerca. La decodifica viene
	 * memorizzata nell'attributo mDescOrdinamento. La funzione viene richiamata quando si valorizza
	 * l'attributo Ordinamento.
	 * 
	 * @param aValue
	 */
	private void setDescOrdinamento(String aValue) {
		if (aValue != null) {
			if (aValue.equalsIgnoreCase("P"))
				mDescOrdinamento = "Numero Procedimento";
			else if (aValue.equalsIgnoreCase("S"))
				mDescOrdinamento = "Cognome Nome";
			else if (aValue.equalsIgnoreCase("D"))
				mDescOrdinamento = "data di inserimento";
			else
				mDescOrdinamento = "";
		}
	}

}
