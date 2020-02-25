package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.istruttoriacumulo.model.EsitoArchiviazioniCumuloModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.action.ICostantiModuloCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.util.ModuloCumuloUtils;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActLoadDettaglioProvvedimentoCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		IstruttoriaCumuloModel lIstruttoriaModel = null;
		DatiFinaliCumuloAggregatoModel lDatiFinaliAggModel = null;

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)
				&& getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO) != null) {
			// Provengo da elenco provvedimenti. Potrei non avere i dati dell'istruttoria
			// sulla request
			siesLogger.debug("Provengo da elenco provvedimenti. Recupero l'istruttoria");

			BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoModel lEvento = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);

			siesLogger.debug("Evento = " + lEvento);
			lIstruttoriaModel = super.getDatiIstruttoria(lEvento.getIstruIdIstruttoriaCumulo());

			lDatiFinaliAggModel = super.getDatiFinaliCumuloAggregato(lEvento.getIstruIdIstruttoriaCumulo());

			// Ricerco lo Stato dei Fascicoli Cumulati e l'ESITO delle ARCHIVIAZIONI degli stessi fascicoli
			// Cumulati;
			// Queste informazioni saranno visualizzate nella successiva FORM di Dettaglio
			Vector<EsitoArchiviazioniCumuloModel> lEsiti = null;
			IIstruttoriaCumulo lctrlI = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			lEsiti = lctrlI.ExRicercaEsitoArchiviazionideiCumulatiByIdEvento(lIdEvento);
			setRequestAttribute("Esiti", lEsiti);

		} else {
			lIstruttoriaModel = super.getDatiIstruttoria();
			lDatiFinaliAggModel = super.getDatiFinaliCumuloAggregato();
		}

		if (lDatiFinaliAggModel.getProvvedimentoCumulo() != null) {
			// Verifico la congruenza della PG con il tipo provvedimento. Se non coerenti
			// (PG modificata), forzo il caricamento del Provvedimento in Modifica.
			boolean isCoerente = true;
			if (ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA.equals(lIstruttoriaModel.getFlagStato())) {
				DatiFinaliCumuloModel lDatiFinaliModel = lDatiFinaliAggModel.getDatiFinaliCumulo();
				boolean isGE = false;
				if ("03".equals(lDatiFinaliModel.getTipoUfficioEmissione())) {
					isGE = true;
				}

				boolean isNLP = false;
				PenaRideterminataCumuloModel lPenaResiduaC = lDatiFinaliAggModel.getPenaResiduaCumulo();
				if (!lPenaResiduaC.isReclusione() && !lPenaResiduaC.isArresto()) {
					// non c'è detentiva
					isNLP = true;
				}

				isCoerente = checkCoerenzaPG(lDatiFinaliAggModel.getPosizioneGiuridicaCumulo(),
						lDatiFinaliAggModel.getProvvedimentoCumulo().getEvento(), isGE, isNLP);
			}

			if (isCoerente) {
				MagistratoModel lMagistrato = null;
				String lCodMag = lDatiFinaliAggModel.getProvvedimentoCumulo().getEvento().getCodMagistrato();
				IMagistrato lCtrlMagistrato = SICOLookupRemote.getMagistratoRemote();
				lMagistrato = lCtrlMagistrato.ExRicercaMagistratoByCod(lCodMag);
				setRequestAttribute("magistrato", lMagistrato);

				EventoNotificaModel lProvvedimentoCumulo = lDatiFinaliAggModel.getProvvedimentoCumulo();
				EventoModel lEve = lProvvedimentoCumulo.getEvento();

				Collection lCodiciProvv = DecodificheManager.getInstance().getMotiviProvvCumuloNew();

				Iterator<DecodificheModel> iter = lCodiciProvv.iterator();
				while (iter.hasNext()) {
					DecodificheModel lDecode = iter.next();

					if (lEve.getCodMotivo().equals(lDecode.getCode())) {
						lEve.setDescrMotivo(lDecode.getFiltro());
						break;
					}
				}

				return PG_LOAD_DETTAGLIO_PROVVEDIMENTO_CUMULO;
			} else {
				String lPage = IWebConstants.PG_MAIN
						+ "?"
						+ IWebConstants.ACTION_FIELD
						+ "=siap.siep.modulocumulo.action.ActLoadInserisciProvvedimentoCumulo"
						+ "&"
						+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO
						+ "="
						+ lDatiFinaliAggModel.getProvvedimentoCumulo().getEvento()
								.getIstruIdIstruttoriaCumulo() + "&" + ICostantiModuloCumulo.MODALITA + "="
						+ ICostantiModuloCumulo.MODALITA_MODIFICA;
				return lPage;
			}
		} else {

			if (!lIstruttoriaModel.getFlagStato().equals(ICostantiIstruttoriaCumulo.FLAG_STATO_APERTA)) {
				// Se l'istruttoria non è in stato APERTA e il provvedimento non è presente,
				// visualizzo il messaggio
				throw new SIEPException(SIEPException.USER_MESSAGE, "Nessuna Provvedimento presente.");
			}

			// carico la funzione di inserimento
			String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActLoadInserisciProvvedimentoCumulo" + "&"
					+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "=" + getIdIstruttoria() + "&"
					+ ICostantiModuloCumulo.MODALITA + "=" + ICostantiModuloCumulo.MODALITA_INSERIMENTO;
			return lPage;
		}
	}

	/**
	 * Restituisce true se il Provvedimento è coerente con la PG
	 * 
	 * @param aPosizione
	 * @param aEvento
	 * @return
	 */
	private boolean checkCoerenzaPG(PosizioneGiuridicaCumuloModel aPosizione, EventoModel aEvento,
			boolean isGE, boolean isNLP) {

		boolean isCoerente = false;

		ModuloCumuloUtils lModCumUtil = new ModuloCumuloUtils(isGE, isNLP);

		// String [] lProvvPerPG = lModCumUtil.getCodMotivoByPosGiu (aPosizione.getCodPosizioneGiuridica());
		String[] lProvvPerPG = lModCumUtil.getCodMotivoByPosGiu(aPosizione);

		String lCodEvento = aEvento.getCodMotivo();
		for (int i = 0; i < lProvvPerPG.length; i++) {
			if (lCodEvento.equals(lProvvPerPG[i])) {
				isCoerente = true;
				break;
			}
		}

		return isCoerente;
	}

}