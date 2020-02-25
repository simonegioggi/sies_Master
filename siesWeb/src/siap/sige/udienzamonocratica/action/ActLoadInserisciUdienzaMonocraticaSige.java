package siap.sige.udienzamonocratica.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciUdienzaMonocraticaSige
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di UdienzaMonocraticaSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciUdienzaMonocraticaSige extends ActUdienzaMonocraticaSige implements
		ICostantiUdienzaMonocraticaSige, ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 * @param idUdiSige
	 * @return
	 */
	private String switchPage(Vector idUdiSige) {

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaSige");
		//[EC] 20171020: idUdiSige.get(0) corrisponde sia all'unica trovata che alla massima udienza trovata in caso in la numerosità del vettore sia >1 
		lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige.get(0).toString());
		lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");		
		String lParamValue = this.getParameter("TornaQui");
		lRedir.setParameter("TornaQui", (lParamValue != null) ? lParamValue : "");
		//[EC] 20171020: commento la riga sottostante perchè il parametro non è più usato nella pagina
		//lRedir.setParameter(ICostantiUdienzaSige.CAMPO_NUMERO_UDIENZE_MAGRISTRATO, "" + idUdiSige.size());
		return lRedir.toString();
	}

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		// se provengo da fuznioni amministrativi
		//rimuovo gli attributi presenti in sessioe
		if (isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			super.pulisciSessione();
		}
		
		super.gestioneRitorno();

		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAP")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAS")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CASAP")
		//[EC] segnalazione di Nunzia per test su 11.2.1 vanno aggiunti anche gli uffici minori (DIBM,CAPSM e GUPM )			
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("CAPSM")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("DIBM")
				|| getUfficioUtenteConnesso().getCodTipoUfficio().equals("GUPM"))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Funzione inibita per il tipo ufficio di competenza.");

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		String lCodTipoUfficio = getTipoUfficio();

		
		UfficioModel ufficio = super.getUfficioUtenteConnesso();
		String indirizzoUfficio = (ufficio.getIndirizzo() == null ? "" : ufficio.getIndirizzo());
		super.setRequestAttribute("indirizzoUfficio", indirizzoUfficio);
		
		// Se L'utente connesso appartiene ad un ufficio distaccato
		// s'imposta come codice comune i primi 6 caratteri del codice distretto.
		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("TRIBSD"))
			lCodComune = getUfficioUtenteConnesso().getCodDistretto().substring(0, 6);

		String sDataUdienza = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_DATA_UDIENZA)) {
			sDataUdienza = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_DATA_UDIENZA);
			// 20171004: [SG] imposto la data udienza nella request
			setRequestAttribute("dataUdienzaImpostata", sDataUdienza);
		}

		MagistratoAssegnatarioModel lMagAss = new MagistratoAssegnatarioModel();
		BigDecimal sez = null;
		// sto provenendo da Inserimento Monicatica dalla pagina di pop-up all'interno della funzione di fissazione udienza o emissione ordinanza
		if (!isRequestParameterNullObj("FormDefCollegio")) {
			lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
			if (lMagAss == null)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Non è possibile fissare un'udienza se non è stato assegnato un Magistrato.");
			
			
			// [EC] 20171019: recupero la sezione se è stata specificata 
			
			try {
				sez = getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA);			
			} catch (Exception e) {}
	
			if (sDataUdienza != null && !"".equals(sDataUdienza) && lMagAss.getMagCodMagistrato() != null) {
				// ==========================================
				// Verifica se è presente un udienza
				// ==========================================
				IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
				// [EC] 20171019: AGGIUNGO IL PARAMETR SEZIONE IN INPUT
				// [EC] 20190325: AGGIUNGO IL PARAMETR COD_UFFICIO IN INPUT (L'UDIENZA DEVE ESSERE UNIVOCA PER UFFICIO)
				Vector lIdUdienzaSige = lCtrl.ExRicercaUdienzaMonocraticaSige(lMagAss.getMagCodMagistrato(),
						sDataUdienza, sez, lCodUfficio);				
				// 20171004: [SG] cambiato il controllo, se è 1 associo altrimenti faccio inserire ex novo
				// if (lIdUdienzaSige.size() > 0)
				//if (lIdUdienzaSige.size() == 1) 
				// [EC] 20171020 ripristino il controllo  su lIdUdienzaSige.size() > 0, perchè se è una prende la get(0), se è più di na va bene comunque la get(0) pechè è l'utlima udienza inserita
				if (lIdUdienzaSige.size() > 0)
					return switchPage(lIdUdienzaSige);
	//			else if(lIdUdienzaSige.size() > 0){
	//				//throw new F3BException(F3BException.USER_MESSAGE, "Attenzione! Esistono più udienze per i dati immessi! Situazione inconsistente per dati sporchi!");
	//			}
			}
		}

		// Crea Lista Elenco Magistrati per ruolo di giudice.
		IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
		Option lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOption.setSelected(lMagAss.getMagCodMagistrato());
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoGiudici", "" + lOption);
		setRequestAttribute("giudiceSelezionato", lMagAss.getMagCodMagistrato());
		
		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		// recupero il magistrato assegnatario attuale
		String codMagAss = "-";
		Option lOptionMagAss = null;
		if (!isSessionAttributeNullObj("FascicoloSigeEsteso") && 
				getFascicoloSigeEstesoInSessione().getMagAssegnatario()!= null){
			codMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario().getMagCodMagistrato();
			lOptionMagAss = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
			lOptionMagAss.setSelected(codMagAss);
			lOptionMagAss.setAddBlankItem(Option.BLANK_ITEM);
			lOptionMagAss.setValueBlankItem("-");
		}
		// intervento per 11.2.1 nuova gestione udienze monocratiche/collegiali
		setRequestAttribute("elencoMagAsseg", "" + lOptionMagAss);

		// Crea lista Elenco procuratori.
		// 20171016: [EC] aggiungo l'option vuota nella lista dei procuratori
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio),
				Option.BLANK_ITEM);
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoProcuratori", "" + lOption);
		//
		// lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio));
		// setRequestAttribute("elencoProcuratori", "" + lOption);

		// Crea lista elenco assistenti.
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio),
				Option.BLANK_ITEM);
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoAssistenti", "" + lOption);

		// Elenco delle sezioni.
		// 20171016: [SG] ricerca puntuale delle sezioni per magistrato ed ufficio
		String lSezioneUdienza = "";
		if(sez != null){
			lSezioneUdienza = sez.toString();
		}
		else {
			if (!isSessionAttributeNullObj("FascicoloSigeEsteso") && getFascicoloSigeEstesoInSessione().getFascicoloSige() != null		
				&& getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdSezione() != null) 
			lSezioneUdienza = "" + getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdSezione();
		else
			lSezioneUdienza = "-";
		}
		lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), lSezioneUdienza,
				Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		
		// 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
		// ufficio differente da quello in cui ha delle udienze poichè trasferito
		
		// intervento per richiesta nunzia per 11.2.1 NON BISOGNA PRENDERE QUELLA DEL MAGISTRATO!!!		
		// MagistratoModel lMagMod = lMagCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
		// lCodUfficio);
		// if (lMagMod.getMagistratoSezioni().length > 0) {
		// BigDecimal idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
		// if (idSezMag != null) {
		// lOption.setSelected(idSezMag.toString());
		// }
		// }
		
		setRequestAttribute("elencoSezioni", "" + lOption);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("numProcePerUdienza", "0");
		// Restituisce la pagina di Inserimento dei Dati

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		
		String insViewJSP = getInsViewJSP();
		return insViewJSP;
	}
	

}