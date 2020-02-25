package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.istruttoria.action.ICostantiIstruttoria;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.richiestaatti.action.ICostantiRichiestaAtti;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciAccertamentiAnagrafici
 * </p>
 * <p>
 * Description: ActInserisciRicIntegrale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciRichiestaGenerica extends ActionSiap implements ICostantiIstruttoria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		int lSize = 0;
//		String lCodice = null;
		EventoNotificaModel lEve = new EventoNotificaModel();

		String[] lSedi = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_SEDE);
		String[] lDestinatari = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO);
		String[] lCampiNoteReq = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO);
//		String[] lNote = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);
		String[] lTipoDest = getRequestStringParameters("tipoDest"); // Tipo di destinatario

		String tipoRichiesta = lCampiNoteReq[0];
		// ComuneModel lCom =
		// this.getCodComuneByDescr(getRequestStringParameter(ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO));
		// String lSede = lCom.getCodComune();

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = RIchiesta Istruttoria
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

		lEve.getEvento().setCodMotivo("0708"); // Richiesta Generica

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();

		// QUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

		// **********************************************************
		// Imposta i dati necessari per la gestione delle Notifica
		Collection lNotifiche = new ArrayList();
		// Cicla sui tipi di destinatari.
		for (int x = 0; x < lTipoDest.length; x++) {
			NotificaModel lNotifica = null;
			if (lTipoDest[x].equalsIgnoreCase("AUT_EXT")) // Gestione Autorità Esterna
			{
				if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.info(">>> Lavorazione TipoDest11 : " + lTipoDest[x]);

					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					lNotifica.setUffCodUfficio("-");
					// Preleva e verifica il codice comune della sede inputata.
					String lCodComuneSede = getCodComuneByDescrFlagVal(lSedi[x]).getCodComune();
					// Popola il model dell'autorità esterna
					AutoritaEsternaModel lAutorita = new AutoritaEsternaModel();
					lAutorita.setCodTipoAutorita(lDestinatari[x]);
					lAutorita.setCodSede(lCodComuneSede);
					lAutorita.setCodOperatoreInserimento(lCodiceOperatore);
					lAutorita.setCodUfficioInserimento(lCodiceUfficio);
					lAutorita.setDataInserimento(DateUtils.getSysDate());

					// Aggiunge il model Autorità Esterna alla Notifica.
					lNotifica.setAutoritaEsterna(lAutorita);

					// Inserisce le Note se sono diverse dal vuoto.
					// if( !lNote[x].equals("") )
					// {
					// lNotifica.setNote(lNote[x]);

					// }
				}

			} else if (lTipoDest[x].equalsIgnoreCase("AUT_EXT2")) // Gestione Ufficio
			{
				// Gestione Ufficio
				if (!lDestinatari[x].equals("-") && !lSedi[x].equals("")) {

					// Preleva il codice d'ufficio per la coppia comune e codice
					// tipo ufficio.
					String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune(lDestinatari[x],
							lSedi[x]);

					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					lNotifica.setUffCodUfficio(lCodUfficioDest);

				}
			} else if (lTipoDest[x].equalsIgnoreCase("IST_DET")) // Gestione Istituto di Detenzione
			{
				// Gestione IST_DET
				if (!lDestinatari[x].equals("")) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(">>> Lavorazione TipoDestinatario : " + lTipoDest[x]);

					lNotifica = creaNotifica(lDataEmissione, lCodiceOperatore, lCodiceUfficio);
					lNotifica.setIstDetIdIstitutoDetenzione(lDestinatari[x]);
					lNotifica.setUffCodUfficio("-");
				}
			}
			// Inserisce la notifica nel vettore delle notifiche.
			lNotifiche.add(lNotifica);
		}

		// ************************************************************

		// QUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

		ProvvedimentoSigeModel lProvvedimentoSigeModel = new ProvvedimentoSigeModel();
		lProvvedimentoSigeModel.setFasIdFascicoloSige(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		lProvvedimentoSigeModel.setFasIdFascicoloSige(lProvvedimentoSigeModel.getFasIdFascicoloSige());
		lProvvedimentoSigeModel.setDataEmissione(lDataEmissione); // DATA EMISSIONE DI EVENTO
		lProvvedimentoSigeModel.setCodTipoProvvedimento("52");
		lProvvedimentoSigeModel.setCodTipoProvvedimentoSige("52");
		lProvvedimentoSigeModel.setCodOperatoreInserimento(getCodUtenteConnesso());
		lProvvedimentoSigeModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lProvvedimentoSigeModel.setDataInserimento(DateUtils.getSysDate());
		lProvvedimentoSigeModel.setDefinitorio("N");
		lProvvedimentoSigeModel.setChiaveUfficio(getCodUfficioUtenteConnesso());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Provvedimento SIGE Valorizzato :" + lProvvedimentoSigeModel);

		// Popola il model campo note aggiuntive
		if (lCampiNoteReq != null) {
			Vector lCampiNote = new Vector();
			lSize = lCampiNoteReq.length;

			for (int x = 0; x < lSize; x++) {
				if (!lCampiNoteReq[x].equals("")) {
					CampoNotaModel lCampoNotaMod = new CampoNotaModel();
					lCampoNotaMod.setDescr(lCampiNoteReq[x]);
					lCampoNotaMod.setCodOperatoreInserimento(lCodiceOperatore);
					lCampoNotaMod.setCodUfficioInserimento(lCodiceUfficio);
					lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
					lCampiNote.add(lCampoNotaMod);
				}
			}

			// Se esistono campi di note aggiuntive
			// inserisce il contenuto del vettore nell'eventoModel
			if (lCampiNote.size() != 0)
				lEve.setCampoNote((CampoNotaModel[]) lCampiNote.toArray(new CampoNotaModel[0]));
		}
		// Inserisce le notifiche nell'eventoModel, prelevando
		// un array di oggetti dal vettore.
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// Chiamata al Controller per gli inserimenti.
		ProvvedimentoSigeModel lRetModel = lProvCtrl.ExInserisciEventoNotificaProv(lEve,
				lProvvedimentoSigeModel);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.richiestaatti.action.ActDettaglioRichiestaGenerica&"
				+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "="
				+ lRetModel.getIdProvvedimentoSige() + "&modalita=I" + "&tipoRichiesta=" + tipoRichiesta;

		return lPage;

	}

	/**
	 * Metodo private che ha la responsabilità di creare e popolare la notifica model nelle parti comuni.
	 * <p>
	 * 
	 * @return Istanza di NotificaModel.
	 */
	private NotificaModel creaNotifica(Date lDataEmissione, String lCodiceOperatore, String lCodiceUfficio) {

		NotificaModel lNotifica = new NotificaModel();
		lNotifica.setCodTipoNotifica(ICostantiRichiestaAtti.CODTIPONOTIFICA);
		lNotifica.setDataInvio(lDataEmissione);
		lNotifica.setCodOperatoreInserimento(lCodiceOperatore);
		lNotifica.setDataInserimento(DateUtils.getSysDate());
		lNotifica.setCodUfficioInserimento(lCodiceUfficio);
		lNotifica.setCodEsito("-");

		return lNotifica;
	}

}