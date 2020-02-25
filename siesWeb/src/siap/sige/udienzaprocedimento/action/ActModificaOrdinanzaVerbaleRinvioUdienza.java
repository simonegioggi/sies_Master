package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.aula.action.ICostantiAula;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
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
public class ActModificaOrdinanzaVerbaleRinvioUdienza extends ActionSige
		implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	protected EventoModel mEvento = new EventoModel();
	protected ProvvedimentoSigeModel mProvModel = new ProvvedimentoSigeModel();
	protected String mActionRet = "";

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Preleva dalla sessione i dati dell'utente connesso.
		BigDecimal idUdienzaProcedimentoSige = super.getRequestBigDecimalParameter(
				ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
		IUdienzaProcedimentoSige lCtrlUproc = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		UdienzaProcedimentoSigeModel udienzaProcedimento = lCtrlUproc
				.ExRicercaUdienzaProcedimentoSigeByKey(idUdienzaProcedimentoSige);
		BigDecimal idEvento = udienzaProcedimento.getEveIdEvento();
		IEvento ctrlEv = SICOLookupRemote.getEventoRemote();

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		// Preleva dati dalla request.
		Date lDataUdienza = getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA);

		// Data Emissione Ordinanza Rinvio Udienza.
		EventoModel evento = ctrlEv.ExRicercaEventoByKey(idEvento);
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);

		evento.setDataEmissione(lDataEmissione);
		evento.setCodUfficioAggiornamento(lCodiceUfficio);
		evento.setCodOperatoreAggiornamento(lCodiceOperatore);
		ctrlEv.ExUpdateDocument(evento);

		// Si istanzia un model UdienzaProcedimentoModel.
		UdienzaSigeModel lUdiSige = new UdienzaSigeModel();

		lUdiSige.setDataUdienza(lDataUdienza);
		lUdiSige.setIdUdienzaSige(
				super.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE));

		lUdiSige.setCodOperatoreAggiornamento(lCodiceOperatore);
		lUdiSige.setCodUfficioAggiornamento(lCodiceUfficio);
		lUdiSige.setDataAggiornamento(DateUtils.getSysDate());
		lUdiSige.setMinInizio(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		lUdiSige.setOraInizio(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		lUdiSige.setMinFine(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));
		lUdiSige.setOraFine(super.getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		// 20171020: [EC] gestisco l'eccezione rilanciata in caso di parametro non trovato nella request
		try {
			String sez = null;
			String aula = null;

			if (!this.isRequestAttributeNullObj(ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA)) {
				sez = (String) this.getRequestAttribute(ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA);
				if ("!".equals(sez))
					lUdiSige.setCodIdSezioneUdienza(super.getRequestBigDecimalParameter(
							ICostantiUdienzaSige.CAMPO_COD_SEZIONE_UDIENZA));
			}

			if (!this.isRequestAttributeNullObj(ICostantiAula.CAMPO_ID_AULA)) {
				aula = (String) this.getRequestAttribute(ICostantiAula.CAMPO_ID_AULA);
				if ("!".equals(aula))
					lUdiSige.setCodIdSezioneUdienza(
							super.getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));
			}
			// AGGIUNGO PER INTERVENTO PER PROBLEMATICA ORDINANZE DI RINVIO UDIENZE SU ESERCIZION 11.2.1
			BigDecimal lIdCollegio = null;
			if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_ID_COLLEGIO)) {
				lIdCollegio = getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_ID_COLLEGIO);
				lUdiSige.setColIdCollegio(lIdCollegio);
			}
			// fine

		} catch (F3BException e) {
			siesLogger.debug("");
		}
		// [EC] 20171018 recupero il giudice dell'udienza per settarlo sull'oggetto lUdiSige
		BigDecimal lIdUdienzaSige = null;
		if (!super.isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE)) {
			lIdUdienzaSige = super.getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		}
		if (lIdUdienzaSige != null) {
			IUdienzaSige lCtrlUdienza = SIGELookupRemote.getUdienzaSigeRemote();
			UdienzaSigeModel lUdienzaSige = lCtrlUdienza.ExRicercaUdienzaSigeById(lIdUdienzaSige);
			if (lUdienzaSige != null)
				lUdiSige.setCodGiudice(lUdienzaSige.getCodGiudice());
			// FINE [EC] 20171017
		} else {
			MagistratoAssegnatarioModel lMagAss = getFascicoloSigeEstesoInSessione().getMagAssegnatario();
			String codMagis = lMagAss.getMagCodMagistrato();
			lUdiSige.setCodGiudice(codMagis);
		}

		// Condizione di controllo del CHECK NUOVO RUOLO.
		if (!isRequestParameterNullObj(CAMPO_CHECK_NUOVO_RUOLO)) {
			// SE è CHECKED NUOVO RUOLO, DEVE CANCELLARE L'UDIENZA INSERITIVA EVENTUALMENTE PRIMA
			udienzaProcedimento.setUdiIdUdienzaSige(null);
			udienzaProcedimento.setFlagRinviata("N");
		} else {

			this.modificaUdienza(lUdiSige);
			setRequestAttribute("udienza", lUdiSige);
			udienzaProcedimento.setUdiIdUdienzaSige(lUdiSige.getIdUdienzaSige());
			udienzaProcedimento.setFlagRinviata("S");
		}

		udienzaProcedimento.setCodOperatoreAggiornamento(lCodiceOperatore);
		udienzaProcedimento.setCodUfficioAggiornamento(lCodiceUfficio);
		udienzaProcedimento.setDataAggiornamento(new Date());
		lCtrlUproc.ExModificaUdienzaProcedimento(udienzaProcedimento);

		// Prepara la pagina di redirezione.
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);

		lPage.setAction("siap.sige.udienzaprocedimento.action.ActLoadDettaglioVerbaleRinvioUdienza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + idEvento);
		// Modifica del 24/02/2016 Nuova Infrastruttura - INIZIO ******
		if (lUdiSige != null && lUdiSige.getIdUdienzaSige() != null) {
			lPage.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE,
					lUdiSige.getIdUdienzaSige().toString());
		} else {
			lPage.setParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE, "");
		}
		// Modifica del 24/02/2016 Nuova Infrastruttura - FINE ******
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