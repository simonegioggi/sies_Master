package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.motivazioneprovvedimento.action.ICostantiMotivazioneProvvedimento;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActModificaOrdinanzaSospensione
 * </p>
 * <p>
 * Description: Classe Action per la modifica ordinanza SIGE.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Luigi
 * @version 1.0
 */
public class ActModificaOrdinanzaSospensione extends ActInserisciOrdinanzaSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActModificaOrdinanzaSospensione: inizio");

		BigDecimal lIdProvvedimento = getRequestBigDecimalParameter(CAMPO_ID_PROVVEDIMENTO_SIGE);
		BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO_GENERATO);

		// Fascicolo Sige Esteso in sessione.
		FascicoloSigeEstesoModel lFasEsteso = getFascicoloSigeEstesoInSessione();

		// Gestione Ordinanza di Sospensione Precedente Ordinanza
		// Mancando i dati del provvedimento (es. tipo Ordinanza) occorre leggerli
		IProvvedimentoSige lProvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEveModel = lProvCtrl.ExRicercaProvvedimentoById(lIdProvvedimento);
		if (lProvEveModel == null || lProvEveModel.getProvvedimento() == null)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Provvedimento Sige inesistente!");

		// Vengono inseriti i dati relativi all'aggionamento
		lProvEveModel.getProvvedimento().setIdEventoGenerato(lIdEvento);
		lProvEveModel.getProvvedimento().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lProvEveModel.getProvvedimento().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																									// dell'ufficio
																									// dell'operatore
																									// che
																									// inserisce
		lProvEveModel.getProvvedimento().setDataAggiornamento(DateUtils.getSysDate());

		if (lProvEveModel.getProvvedimento().getCodTipoProvvedimentoSige() == null || lProvEveModel
				.getProvvedimento().getCodTipoProvvedimentoSige().compareTo(COD_ORDINANZA_SOSPENSIONE) != 0)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Tipo Provvedimento Sige non corretto!");

		if (!isRequestParameterNullObj(
				ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE_SOSPENSIONE)) {
			lProvEveModel.getProvvedimento().setNote(getRequestStringParameter(
					ICostantiMotivazioneProvvedimento.CAMPO_DESCR_MOTIVAZIONE_SOSPENSIONE));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MOTIVAZIONI SOSPENSIONE: " + lProvEveModel.getProvvedimento().getNote());
		}

		if (!isRequestParameterNullObj(SEDE_MAGISTRATO_COMPETENTE)
				&& getRequestStringParameter(ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE).trim()
						.length() > 0) {
			lProvEveModel.getProvvedimento()
					.setCodUfficioDestinatario(UfficioUtils.getCodUfficioByCodTipoUfficioDescrComune("UDS",
							getRequestStringParameter(ICostantiProvvedimentoSige.SEDE_MAGISTRATO_COMPETENTE)
									.trim()));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("SEDE MAG : " + lProvEveModel.getProvvedimento().getCodUfficioDestinatario());
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO)
				&& !isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO)) {
			if (getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO).trim().length() > 1
					&& getRequestStringParameter(ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO).trim()
							.length() > 1)

				lProvEveModel.getProvvedimento()
						.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune(
								getRequestStringParameter(ICostantiFascicoloSige.CAMPO_CHIAVE_UFFICIO),
								getRequestStringParameter(
										ICostantiFascicoloSige.CAMPO_DESCR_COMUNE_UFFICIO)));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(
					"COD UFF. DEST. : " + lProvEveModel.getProvvedimento().getCodUfficioDestinatario());
		}

		// Fase di Aggiornamento!
		// Si inserisce comunque un nuovo evento notifiche
		// Da verificare se è giusto così o se invece è necessario aggiornare il preesistente Evento Notifiche

		// Creazione del Nuovo Elenco dei Destinatari delle Notifiche
		Date lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE);

		Vector lDestinatariNotifiche = creaListaDestinatariNotifiche(lFasEsteso, lDataEmissione);

		// Creazione del nuovo EventoNotificaModel
		EventoNotificaModel lEveNotMod = new EventoNotificaModel(lProvEveModel.getEventoNotifica());
		lEveNotMod.setNotifiche((NotificaModel[]) lDestinatariNotifiche.toArray(new NotificaModel[0]));

		// Inserimento del nuovo Evento e Notifiche
		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = lEveCtrl.ExInserisciEventoNotifica(lEveNotMod);

		// Associazione del Provvedimento Modificato al nuovo Evento e Notifiche
		lProvEveModel.getProvvedimento().setIdEventoGenerato(lEveNot.getEvento().getIdEvento());

		// Aggiornamento del Provvedimento
		lProvCtrl.ExModificaProvvedimentoSige(lProvEveModel.getProvvedimento(), letturaMotivazioni());

		// Fine Fase di Aggiornamento!

		// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
		IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
		lFasEsteso = lCtrlFas
				.ExRicercaEstesaFascicoloSigeByKey(lFasEsteso.getFascicoloSige().getIdFascicoloSige());
		setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);

		// Rimozione dell'elenco Tenori dalla sessione removeSessionAttribute("tenori");

		// Prepara la "pagina" di destinazione
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sige.provvedimento.action.ActDettaglioOrdinanzaSospensione");
		lRedirigi.setParameter(CAMPO_ID_PROVVEDIMENTO_SIGE,
				(lProvEveModel.getProvvedimento().getIdProvvedimentoSige()).toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActModificaOrdinanzaSospensione: fine");
		return lRedirigi.toString();
	}

}