package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sige.SIGEException;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActInserisciRinvioUdienza
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento del Rinvio Udienza.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActInserisciRinvioUdienza extends ActionSige implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	protected EventoModel mEvento = new EventoModel();
	protected ProvvedimentoSigeModel mProvModel = new ProvvedimentoSigeModel();
	protected String mActionRet = "";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodComune = getCodComuneUtenteConnesso();
		FascicoloSigeEstesoModel lFasEsteso = super.getFascicoloSigeEstesoInSessione();
		if (lFasEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo("05") == 0
				|| lFasEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo("01") == 0) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Rinvio Udienza non consentito. Procedimento già definito.");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(getClass().getName() + ".processRequest: fine");

			return IWebConstants.PG_MESSAGE;
		}

		// Preleva dati dalla request.
		Date lDataUdienza = getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);

		// Data Emissione Ordinanza Rinvio Udienza.
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		// [EC] 20171017 ripristino il recupero lIdUdienzaSige dal parameter
		// ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE e non dal parameter newIdUdienzaSige
		BigDecimal lIdUdienzaSige = this
				.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		// BigDecimal lIdUdienzaSige = this.getRequestBigDecimalParameter("newIdUdienzaSige");
		if (isSessionAttributeNullObj("tenori"))
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");

		// Si prelevano i tenori Sige in sessione.
		Vector lTenoriEstesi = (Vector) getSessionAttribute("tenori");

		if (lTenoriEstesi.size() == 0)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Oggetti assenti !");

		// Si istanzia un model UdienzaProcedimentoModel.
		UdienzaSigeModel lUdiSige = new UdienzaSigeModel();
		if (getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA).length() > 2) {
			lUdiSige.setDataUdienza(lDataUdienza);
			// 14/04/2014 Il controllo seguente è già presente nella form di inserimento
			// if( lFasEsteso.getUdienzaProcedimento().getDataUdienzaSige().after(lDataUdienza))
			// throw new SIGEException(SIGEException.USER_MESSAGE,
			// "Rinvio Udienza non consentito. " +
			// "La data udienza selezionata non può essere precedente all'udienza già fissata.");
			lUdiSige.setIdUdienzaSige(
					super.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE));
		}

		lUdiSige.setCodOperatoreInserimento(lCodiceOperatore);
		lUdiSige.setCodUfficioInserimento(lCodiceUfficio);
		lUdiSige.setDataInserimento(DateUtils.getSysDate());
		lUdiSige.setMinInizio(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		lUdiSige.setOraInizio(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		lUdiSige.setMinFine(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));
		lUdiSige.setOraFine(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		try {
			lUdiSige.setCodIdSezioneUdienza(
					super.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA));
		} catch (Exception e) {
		}

		lUdiSige.setIdUdienzaSige(lIdUdienzaSige);
		AulaUdienzaModel aula = null;
		try {
			aula = (AulaUdienzaModel) super.getRequestAttribute("aula");
		} catch (Exception e) {
		}

		if (aula != null) {
			lUdiSige.setAulaUdienzaModel(aula);
			lUdiSige.setCodIdAulaUdienza(aula.getIdAula());
		}

		// [EC] 20171017 recupero il giudice dell'udienza per settarlo sull'oggetto lUdiSige
		if (lIdUdienzaSige != null) {
			IUdienzaSige lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRemote();
			UdienzaSigeModel lUdienzaSige = lCtrlUdienza.ExRicercaUdienzaSigeById(lIdUdienzaSige);
			// DEVO VERIFICARE SE è COLLEGIALE OPPURE MONOCRATICA
			if (lUdienzaSige.getColIdCollegio() != null
					&& !"0".equals(lUdienzaSige.getColIdCollegio().toString())
					&& lUdienzaSige.getCollegio().getCollegioMagistrati() != null
					&& lUdienzaSige.getCollegio().getCollegioMagistrati().length > 0) {
				// in questo caso sto trattando un rito collegiate, quindi il codice giudice lo devo
				// recuperare dall'oggetto magistrato collegio
				// lUdiSige.setCodGiudice(lUdienzaSige.getCollegio().getCollegioMagistrati()[0].getMagCodMagistrato());

				// NB: per le collegiali l'udinza non deve avere impostatoa il codice giusdice, ma va settato
				// l'id collegio
				lUdiSige.setColIdCollegio(lUdienzaSige.getColIdCollegio());

			} else if (lUdienzaSige != null) {
				lUdiSige.setCodGiudice(lUdienzaSige.getCodGiudice());
				// FINE [EC] 20171017
			}
		} else {
			MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
			String codMagis = lMagAss.getMagCodMagistrato();
			lUdiSige.setCodGiudice(codMagis);
		}

		if (lIdUdienzaSige == null) {
			if (lUdiSige.getDataUdienza() != null) {
				lUdiSige = this.insertUdienza(lUdiSige);
				lIdUdienzaSige = lUdiSige.getIdUdienzaSige();
			}
		} else {
			this.modificaUdienza(lUdiSige);
		}

		setRequestAttribute("udienza", lUdiSige);

		// Condizione di controllo del CHECK NUOVO RUOLO.
		if (lIdUdienzaSige == null && isRequestParameterNullObj(CAMPO_CHECK_NUOVO_RUOLO)) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Rinvio Udienza non consentito. "
					+ "Per il procedimento non risulta fissata la nuova data udienza.");
			return IWebConstants.PG_MESSAGE;
		}

		/*
		 * // Impostazione Evento. EventoModel lEvento = new EventoModel(); lEvento.setCodTipoEvento("01"); //
		 * Tipo Evento = Provvedimento lEvento.setCodTipoProvvedimento("03"); // Tipo Provvedimento =
		 * Ordinanza lEvento.setCodEsito("0603"); // Ordinanza Rinvio Udienza.
		 * lEvento.setTemIdTemplate("OR1"); lEvento.setDataEmissione(lDataEmissione);
		 * lEvento.setCodOperatoreInserimento(lCodiceOperatore); lEvento.setCodLuogoEmittente(lCodComune);
		 * lEvento.setCodUfficioEmittente(lCodiceUfficio); lEvento.setCodUfficioInserimento(lCodiceUfficio);
		 * lEvento.setDataInserimento(DateUtils.getSysDate()); lEvento.setAnnoProtocollo(new
		 * BigDecimal(DateUtils.getSysDate("yyyy"))); lEvento.setCodLuogoDestinatario("-");
		 * lEvento.setCodTipoUfficioDestinatario("-");
		 *
		 * // Impostazione del Provvedimento SIGE. ProvvedimentoSigeModel lProvModel = new
		 * ProvvedimentoSigeModel();
		 * lProvModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		 *
		 * lProvModel.setChiaveUfficio(getCodUfficioUtenteConnesso());
		 * lProvModel.setDataEmissione(lDataEmissione); lProvModel.setCodTipoProvvedimento("03");
		 * lProvModel.setDefinitorio("N"); lProvModel.setLuogoSvolgimento(
		 * getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO));
		 *
		 * String lFlagOrdineTraduzione = "N"; lProvModel.setFlagOrdineTraduzione(lFlagOrdineTraduzione);
		 * lProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); //Codice dell'operatore che
		 * inserisce lProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); //Codice dell'ufficio
		 * dell'operatore che inserisce lProvModel.setDataInserimento(DateUtils.getSysDate());
		 */

		// Impostazione Evento.

		// Definite nella classe specializzata!
		// mEvento.setCodTipoEvento("01"); // Tipo Evento = Provvedimento
		// mEvento.setCodTipoProvvedimento("03"); // Tipo Provvedimento = Ordinanza
		// mEvento.setCodEsito("0603"); // Ordinanza Rinvio Udienza.
		// mEvento.setTemIdTemplate("OR1");
		mEvento.setDataEmissione(lDataEmissione);
		mEvento.setCodOperatoreInserimento(lCodiceOperatore);
		mEvento.setCodLuogoEmittente(lCodComune);
		mEvento.setCodUfficioEmittente(lCodiceUfficio);
		mEvento.setCodUfficioInserimento(lCodiceUfficio);
		mEvento.setDataInserimento(DateUtils.getSysDate());
		mEvento.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		mEvento.setCodLuogoDestinatario("-");
		mEvento.setCodTipoUfficioDestinatario("-");

		// Impostazione del Provvedimento SIGE.
		mProvModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		mProvModel.setChiaveUfficio(getCodUfficioUtenteConnesso());
		mProvModel.setDataEmissione(lDataEmissione);
		// mProvModel.setCodTipoProvvedimento("03");
		mProvModel.setDefinitorio("N");
		mProvModel.setLuogoSvolgimento(
				getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_LUOGO_SVOLGIMENTO));

		String lFlagOrdineTraduzione = "N";
		mProvModel.setFlagOrdineTraduzione(lFlagOrdineTraduzione);
		mProvModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che inserisce
		mProvModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																			// dell'operatore che inserisce
		mProvModel.setDataInserimento(DateUtils.getSysDate());

		// Popola il model Provvedimento Evento Model.
		ProvvedimentoSigeEventoModel lProvvEvento = new ProvvedimentoSigeEventoModel(mEvento);
		lProvvEvento.setTenoriEstesi(lTenoriEstesi); // Imposta i tenori
		lProvvEvento.setProvvedimento(mProvModel); // Imposta il provvedimento.

		IUdienzaProcedimentoSige lCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		EventoModel lEveModel = lCtrl.ExInserisciRinvioUdienza(lUdiSige, lFasEsteso.getFascicoloSige(),
				lProvvEvento);
		// Prepara la pagina di redirezione.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);

		// lPage.setAction("siap.sige.udienzaprocedimento.action.ActLoadDettaglioOrdinanzaRinvioUdienza");
		lPage.setAction(mActionRet); // Definito nella classe specializzata.
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lEveModel.getIdEvento());

		if (lIdUdienzaSige != null)
			lPage.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, lIdUdienzaSige.toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lPage.toString();
	}

	public UdienzaSigeModel modificaUdienza(UdienzaSigeModel lUdienzaSige) throws F3BException {

		IUdienzaSige lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRemote();
		lCtrlUdienza.ExModificaUdienzaSige(lUdienzaSige);
		return lUdienzaSige;
	}

	public UdienzaSigeModel insertUdienza(UdienzaSigeModel lUdienzaSige) throws F3BException {

		lUdienzaSige.setCodUfficioAppartenenza(super.getUtenteConnesso().getUfficioUtente().getCodUfficio());
		IUdienzaSige lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRemote();
		lUdienzaSige = lCtrlUdienza.ExInserisciUdienzaSige(lUdienzaSige, null);
		return lUdienzaSige;
	}

}