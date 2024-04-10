package siap.siep.rateizzazionepp.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.action.ICostantiArchiviazione;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe per modificare la Definizione Procedimento Pena Pecuniaria
 *
 * @author sgioggi
 * @since MEV_2023-33
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActModificaDefinizioneProcedimentoPP extends ActionSiap implements ICostantiArchiviazione {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: inizio");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		
		// evento notifica da passare al metodo di inserimento
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		// evento
		EventoModel lEveMod = new EventoModel();
		lEveMod.setIdEvento(idEvento);
		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25"); // Tipo Provvedimento = Fine Espiazione
		lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,	CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodEsito("-");
		lEveMod.setCodMagistrato(calcolaMagistrato());
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S"); 
		lEveMod.setFlagStampaSiep("S"); 

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		
		lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// setto l'evento dentro l'eventonotificaModel
		lEveNotMod.setEvento(lEveMod);

		// archiviazione
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		lArcMod.setCodTipoProvvedimento("21"); // 21 = Fine espiazione

		lArcMod.setNumNota(getRequestStringParameter(CAMPO_NUM_NOTA));
		lArcMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,CAMPO_GIORNO_DATA_EMISSIONE));
		lArcMod.setDataRicezione(getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE, CAMPO_MESE_DATA_RICEZIONE,CAMPO_GIORNO_DATA_RICEZIONE));
		lArcMod.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lArcMod.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));

		lArcMod.setCodTipoEmittente("-");

		if (!this.isRequestParameterNullObj(CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
				&& !this.getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE).equals("-")) {
			lArcMod.setCodTipoEmittente("02"); // 02 = polizia (TIPO_EMITTENTE'
			lArcMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

			ComuneModel lCom = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
			lArcMod.setCodLuogoEmittente(lCom.getCodComune());
			lArcMod.setIndirizzoEmittente(getRequestStringParameter(CAMPO_INDIRIZZO_EMITTENTE));
		}

		lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataInserimento(DateUtils.getSysDate());
		lArcMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		// notifica al CASELLARIO
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("E");
		//lNotMod.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,CAMPO_GIORNO_DATA_EMISSIONE));
		// Data DataInvio è not null ma non significativo per il casellario. Si utilizza la data definizione obbligatoria in form
		lNotMod.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE)); // not null		
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNotMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNotMod.setDataInserimento(DateUtils.getSysDate());

		AutoritaEsternaModel lAut = new AutoritaEsternaModel();
		lAut.setCodTipoAutorita("24"); // casellario giudiziale

		// lAut.setCodSede(getRequestStringParameter("codicecomcas"));
		ComuneModel lComCasellarioMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
		lAut.setCodSede(lComCasellarioMod.getCodComune());

		lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAut.setDataInserimento(DateUtils.getSysDate());

		lNotMod.setAutoritaEsterna(lAut);

		// setto la notifica dentro l'eventonotificaModel
		ArrayList lNotifiche = new ArrayList();
		lNotifiche.add(lNotMod);
		lEveNotMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// inserimento
		IArchiviazione lCtrl = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcModRes = lCtrl.ExModificaArchiviazionePP(lEveNotMod, lArcMod);

		// info per il log
		siesLogger.info(getClass().getName() + ".processRequest: fine");

		// valore di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.rateizzazionepp.action.ActDettaglioDefinizioneProcedimentoPP&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lArcModRes.getEveIdEvento();
	}

}