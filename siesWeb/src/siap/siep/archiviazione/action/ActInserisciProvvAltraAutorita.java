package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciProvvAltraAutorita
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Evento
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
public class ActInserisciProvvAltraAutorita extends ActionSiap implements ICostantiArchiviazione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// evento notifica da passare al metodo di inserimento
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		// evento
		EventoModel lEveMod = new EventoModel();

		// commentato il 15-04-2005 -- viviana -- dario altrimenti nn si vede nello stato esecuzione,elenco
		// prov, ecc.
		// lEveMod.setCodTipoEvento("15"); // Tipo Evento = Definizione Procedimento
		lEveMod.setCodTipoEvento("01");
		// STUB 17/10/2005 REWORK STATO ESECUZIONE
		// lEveMod.setCodTipoProvvedimento("23"); // Tipo Provvedimento = Provvedimento altra autorità
		lEveMod.setCodTipoProvvedimento("25");

		lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodEsito("-");
		lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));

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

		lArcMod.setCodTipoProvvedimento("23");
		lArcMod.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		lArcMod.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lArcMod.setAnnoProvvedimento(getRequestBigDecimalParameter(CAMPO_ANNO_PROVVEDIMENTO));
		lArcMod.setNumProvvedimento(getRequestStringParameter(CAMPO_NUM_PROVVEDIMENTO));
		lArcMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		lArcMod.setDataRicezione(getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE, CAMPO_MESE_DATA_RICEZIONE,
				CAMPO_GIORNO_DATA_RICEZIONE));
		lArcMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lArcMod.setCodTipoEmittente("05");
		lArcMod.setCodProvvedimento(getRequestStringParameter(CAMPO_COD_PROVVEDIMENTO));
		lArcMod.setCodTipoProvvedimentoArc(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_ARC));

		lArcMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		ComuneModel lCom = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lArcMod.setCodLuogoEmittente(lCom.getCodComune());

		lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataInserimento(DateUtils.getSysDate());
		lArcMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		// inserimento --- IN QUESTO CASO LE NOTIFICHE NN CI SONO MA USIAMO SEMPRE LO STESSO METODO
		IArchiviazione lCtrl = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcModRes = lCtrl.ExInserisciEventoNotificaArchiviazione(lEveNotMod, lArcMod,
				lFascMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.archiviazione.action.ActLoadDettaglioProvvAltraAutorita&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lArcModRes.getEveIdEvento();

		return lPage;
	}

	/**
	 * calcolaMagistrato
	 * 
	 * @return
	 */
	protected String calcolaMagistrato() throws F3BException {

		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME).toUpperCase());
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME).toUpperCase());

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

}