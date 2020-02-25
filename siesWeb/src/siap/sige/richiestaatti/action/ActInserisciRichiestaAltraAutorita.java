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
 * Title: ActInserisciRicSIntegrale
 * </p>
 * <p>
 * Description: ActInserisciRicSIntegrale
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
public class ActInserisciRichiestaAltraAutorita extends ActionSiap implements ICostantiIstruttoria,
		ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Preleva dalla sessione i dati dell'utente connesso.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
//		String lCodComune = getCodComuneUtenteConnesso();
//		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		int lSize = 0;
//		String lCodice = null;
		// String[] lDestinatari = getRequestStringParameters( AUTORITA_DESTINATARIO );
		// String[] lSedi = getRequestStringParameters( SEDE_AUTORITA_DESTINATARIO );

		String[] lSedi = getRequestStringParameters(CAMPO_SEDE);
		String[] lDestinatari = getRequestStringParameters(CAMPO_COD_DESTINATARIO);
		String[] lCampiNoteReq = getRequestStringParameters(CAMPO_AGGIUNTIVO);
		String[] lNote = getRequestStringParameters(ICostantiRichiestaAtti.CAMPO_NOTE);
		String[] lTipoDest = getRequestStringParameters("tipoDest"); // Tipo di destinatario

		EventoNotificaModel lEve = new EventoNotificaModel();

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = RIchiesta Istruttoria
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

		lEve.getEvento().setCodMotivo("0704");

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
					siesLogger.info(">>> Lavorazione TipoDest : " + lDestinatari[x]);

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
					if (!lNote[x].equals("")) {
						lNotifica.setNote(lNote[x]);

					}
				}
			}
			// Inserisce la notifica nel vettore delle notifiche.
			lNotifiche.add(lNotifica);
		}

		// ************************************************************

		/*
		 * lSize = lDestinatari.length; for( int x = 0; x < lSize; x++ ) { if( !lDestinatari[x].equals("-") &&
		 * !lSedi[x].equals("") ) { lCodice =
		 * getCodUfficioByCodTipoUfficioDescrComune(lDestinatari[x],lSedi[x] ); } }
		 */

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

		// NotificaModel lNotifiche[] = new NotificaModel[1];
		// NotificaModel lNot = new NotificaModel();
		// lNot.setCodTipoNotifica("N");
		// lNot.setDataInvio(DateUtils.getSysDate());
		// lNot.setCodEsito("-");
		// lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		// lNot.setDataInserimento(DateUtils.getSysDate());
		// lNot.setCodUfficioInserimento(lUff.getCodUfficio());
		// lNot.setUffCodUfficio(lCodice);

		// Lettura degli indirizzi destinatari
		/*
		 * String lTestoNote = null; int lSizeI=0; String[] indirizzo =
		 * getRequestStringParameters(ICostantiIstruttoria.INDIRIZZO_DESTINATARIO); lSizeI=indirizzo.length;
		 * for( int y = 0; y < lSizeI; y++ ) { if( !indirizzo[y].equals("")) { lTestoNote = indirizzo[y]; } }
		 * 
		 * lNot.setNote(lTestoNote);
		 */
		// lNotifiche[0] = lNot;

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

		// Inserisco l'array di Notifiche nell'Evento
		// lEve.setNotifiche(lNotifiche);

		// Inserisce le notifiche nell'eventoModel, prelevando
		// un array di oggetti dal vettore.
		lEve.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		ProvvedimentoSigeModel lRetModel = lProvCtrl.ExInserisciEventoNotificaProv(lEve,
				lProvvedimentoSigeModel);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.richiestaatti.action.ActDettaglioRicAltraAutorita&"
				+ ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE + "="
				+ lRetModel.getIdProvvedimentoSige() + "&modalita=I";

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