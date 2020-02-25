package siap.sige.udienzacollegiale.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.collegio.util.CollegioUtils;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciUdienzaCollegiale
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di UdienzaCollegialeSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciUdienzaCollegiale extends ActUdienzaCollegiale
		implements ICostantiUdienzaCollegiale {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private String switchPage(Vector idUdiSige) {

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");
		lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdiSige.get(0).toString());
		lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
		String lParamValue = this.getParameter("TornaQui");
		lRedir.setParameter("TornaQui", (lParamValue != null) ? lParamValue : "");
		lRedir.setParameter(ICostantiUdienzaSige.CAMPO_NUMERO_UDIENZE_MAGRISTRATO, "" + idUdiSige.size());
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

		if (isRequestParameterNullObj("FormDefCollegio")) {
			siesLogger.debug("VIETATO INSERIMENTO UDIENZA COLLEGIALE DA FUNZIONI AMMINISTRATIVE!");
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Attenzione! L'inserimento dell'udienza collegiale va fatto dalla funzione 'Inserimento Fissazione Udienza'.");
			return IWebConstants.PG_MESSAGE;
		} else {
			if ("yes".equals(getRequestStringParameter("FormDefCollegio"))) {
				siesLogger.debug("INSERIMENTO COLLEGIO DA PAGINE INTERNE ALL'APPLICAZIONE!");
				// controllo che il fascicolo abbia un magistrato assegnatario (intervento per 11.2.1)
				// recupero il magistrato assegnatario
				MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
				if (lMagAss == null)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Non è possibile fissare un'udienza se non è stato assegnato un Magistrato.");
			}
		}

		super.gestioneRitorno();

		String popUp = "";
		if (!isRequestParameterNullObj("PopUp")) {
			popUp = getRequestStringParameter("PopUp");
		}
		setRequestAttribute("PopUp", popUp);

		String lCodUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		String lCodTipoUfficio = getTipoUfficio();
		Option lOption = null;
		BigDecimal idUdienza = getIdUdienza();
		if (idUdienza != null) {
			RedirectTo lRedir = new RedirectTo();
			lRedir.setPage(IWebConstants.PG_MAIN);
			lRedir.setAction("siap.sige.udienzacollegiale.action.ActLoadDettaglioUdienzaCollegiale");
			lRedir.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, idUdienza.toString());
			lRedir.setParameter(ICostantiCollegio.FORM_DEF_COLLEGIO, "yes");
			lRedir.setParameter("PopUp", popUp);
			return lRedir.toString();
		}
		String sDataUdienza = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_DATA_UDIENZA)) {
			sDataUdienza = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_DATA_UDIENZA);
			// 20171004: [SG] imposto la data udienza nella request
			setRequestAttribute("dataUdienzaImpostata", sDataUdienza);
		}

		// recupero il magistrato assegnatario
		MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
		if (sDataUdienza != null && !"".equals(sDataUdienza) && lMagAss != null
				&& lMagAss.getMagCodMagistrato() != null) {

			// ==========================================
			// Verifica se è presente un udienza
			// ==========================================
			IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
			Vector lIdUdienzaSige = lCtrl.ExRicercaUdienzaCollegialeSige(lMagAss.getMagCodMagistrato(),
					sDataUdienza, lCodUfficio);
			if (lIdUdienzaSige.size() > 0) {
				return switchPage(lIdUdienzaSige);
			}
		}

		// Crea Lista Elenco Magistrati per ruolo di giudice.
		IMagistrato lMagCtrl = SIGELookupRemote.getMagistratoRemote();
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoGiudici", "" + lOption);

		// Crea lista Elenco procuratori.
		lOption = new Option(lMagCtrl.ExElencoCbxMagByCodComuneCodTipoUff(lCodComune, lCodTipoUfficio),
				Option.BLANK_ITEM);
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoProcuratori", "" + lOption);

		// Crea lista elenco assistenti.
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		lOption = new Option(lAssistenteCtrl.ExElencoCbxAssistenteGiudiziarioByCodUfficio(lCodUfficio),
				Option.BLANK_ITEM);
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("elencoAssistenti", "" + lOption);

		// Elenco delle sezioni.
		lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");

		BigDecimal idSezFasc = null;

		// intervento per richiesta nunzia per 11.2.1
		// la sez da caricare se presente è quella inserita a livello fascicolo, NON BISOGNA PRENDERE QUELLA
		// DEL MAGISTRATO!!!
		FascicoloSigeEstesoModel mFasEsteso = null;
		if (!isSessionAttributeNullObj("FascicoloSigeEsteso"))
			mFasEsteso = getFascicoloSigeEstesoInSessione();

		if (mFasEsteso != null && mFasEsteso.getFascicoloSige().getIdSezione() != null) {
			idSezFasc = mFasEsteso.getFascicoloSige().getIdSezione();
		}
		// intervento per richiesta nunzia per 11.2.1 NON BISOGNA PRENDERE QUELLA DEL MAGISTRATO!!!
		// if ( idSezMag== null && lMagAss != null) {
		// // 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito da un
		// // ufficio differente da quello in cui ha delle udienze poichè trasferito
		// MagistratoModel lMagMod = lMagCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
		// getCodUfficioUtenteConnesso());
		// if (lMagMod!= null && lMagMod.getMagistratoSezioni().length > 0) {
		// idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
		// }
		// }
		if (idSezFasc != null) {
			lOption.setSelected(idSezFasc.toString());
		} else {
			lOption.setSelected("-");
		}
		setRequestAttribute("elencoSezioni", "" + lOption);

		lOption = new Option(CollegioUtils.getElencoCodiciCollegi(), Option.BLANK_ITEM);
		setRequestAttribute("elencoCodiciCollegi", "" + lOption);

		// Elenco giudici popolari.
		GiudicePopolareModel lGiudicePopolare = new GiudicePopolareModel();
		lGiudicePopolare.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lGiudicePopolare.setMessage("dataFineisNull");

		Vector lGiudPopVect = null;
		try {
			IGiudicePopolare lGiudPopCtrl = SIGELookupRemote.getGiudicePopolareRemote();
			lGiudPopVect = lGiudPopCtrl.ExRicercaGiudicePopolare(lGiudicePopolare);
		} catch (Exception e) {
		}
		setRequestAttribute("elencoGiudiciPopolari", lGiudPopVect);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("numProcePerUdienza", "0");

		// intervento per 11.2.1
		UfficioModel ufficio = super.getUfficioUtenteConnesso();
		String indirizzoUfficio = (ufficio.getIndirizzo() == null ? "" : ufficio.getIndirizzo());
		super.setRequestAttribute("indirizzoUfficio", indirizzoUfficio);
		setRequestAttribute("magAssegnatario", lMagAss);

		// Restituisce la pagina di Inserimento dei Dati

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return getInsViewJSP();
	}

	private BigDecimal getIdUdienza() throws F3BException {

		String value = super.getRequest().getParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		if (value == null || value.equals(""))
			return null;

		return new BigDecimal(value);
	}

}