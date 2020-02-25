package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.modulocumulo.controller.IDatiFinaliCumulo;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Action che effettua l'inserimento del provvedimento di cumulo
 * 
 * @author d.fiorletta
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciProvvedimentoCumulo extends ActionModuloCumulo implements ICostantiModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del cumulo
		// ==========================================================================
		/* IstruttoriaCumuloModel lIstrModel = */super.getDatiIstruttoria();
		DatiFinaliCumuloAggregatoModel lDatiFinaliAggr = super.getDatiFinaliCumuloAggregato();

		BigDecimal lIdDatiFinali = lDatiFinaliAggr.getDatiFinaliCumulo().getIdDatiFinaliCumulo();

		// ==========================================================================
		// Recupera i dati ed effettuo l'inserimento
		// ==========================================================================
		EventoNotificaModel lEveNotModel = getDatiForm();
		IDatiFinaliCumulo lCtrlDatFin = SIEPLookupRemote.getDatiFinaliCumuloRemote();

		String lModalita = getRequestStringParameter("modalita");

		if (lModalita.equals(ICostantiModuloCumulo.MODALITA_INSERIMENTO)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sono in Inserimento Provvedimento di Cumulo");
			lEveNotModel = lCtrlDatFin.ExInserisciProvvedimentoCumulo(lEveNotModel, lIdDatiFinali);
		} else if (lModalita.equals(ICostantiModuloCumulo.MODALITA_MODIFICA)) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Sono in Modifica Provvedimento di Cumulo");
			lEveNotModel = lCtrlDatFin.ExModificaProvvedimentoCumulo(lEveNotModel, lIdDatiFinali);
		}

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActLoadDettaglioProvvedimentoCumulo" + "&"
				+ ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO + "="
				+ this.getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO)
				+ "&" + ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * 
	 * @return
	 */
	private EventoNotificaModel getDatiForm() throws F3BException {
		FascicoloSiepModel lFascicoloSiep = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// ==========================================================================
		// Dati delle notifiche
		// ==========================================================================

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		if (getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO) != null) {
			lEveNot.getEvento().setIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		}

		lEveNot.getEvento().setFasSieIdFascicoloSiep(lFascicoloSiep.getIdFascicoloSiep());
		lEveNot.getEvento().setIstruidIstruttoriaCumulo(
				getRequestBigDecimalParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));

		lEveNot.getEvento().setCodTipoEvento("01"); //
		lEveNot.getEvento().setCodTipoProvvedimento("04");
		lEveNot.getEvento()
				.setCodMotivo(getRequestStringParameter(ICostantiDatiFinaliCumulo.CAMPO_MOTIVO_PROVV));

		// Data Emissione
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEveNot.getEvento().setDataEmissione(lDataEmissione);

		// Data Trasmissione
		Date lDataTrasmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI);
		lEveNot.getEvento().setDataTrasmissioneAtti(lDataTrasmissione);

		// Magistratodel firmatario
		lEveNot.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		UfficioModel lUff = this.getUfficioUtenteConnesso();

		lEveNot.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEveNot.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEveNot.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEveNot.getEvento().setFlagStampaSiep("S");
		lEveNot.getEvento().setFlagVideoSiep("S");
		// lEveNot.getEvento().setFlagDocumentoRegistrato("N");

		lEveNot.getEvento().setCodEsito("-");
		lEveNot.getEvento().setCodLuogoDestinatario("-");
		lEveNot.getEvento().setCodTipoUfficioDestinatario("-");

		lEveNot.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveNot.getEvento().setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveNot.getEvento().setDataInserimento(DateUtils.getSysDate());

		// ==========================================================================
		// Dati delle notifiche
		// ==========================================================================
		ArrayList lNotifiche = new ArrayList();

		// Istituto di detenzione
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			lNotModIst.setCodEsito("-");
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setDataInvio(lDataEmissione);
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);

			lNotModIst.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lNotModIst.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lNotModIst.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			lNotifiche.add(lNotModIst);
		}

		// UEPE
		if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null) {
			NotificaModel lNotModUEPE = new NotificaModel();

			BigDecimal lIdCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			lNotModUEPE.setCodEsito("-");
			lNotModUEPE.setCodTipoNotifica("E");
			lNotModUEPE.setDataInvio(lDataEmissione);

			lNotModUEPE.setCssIdCssa(lIdCssa);

			lNotModUEPE.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lNotModUEPE.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lNotModUEPE.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			lNotifiche.add(lNotModUEPE);
		}

		// Tribunale di sorveglianza
		if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO + "_TDS")
				&& !getRequestStringParameter(ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO + "_TDS").equals("-")
				&& !isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)) {
			NotificaModel lNotModTDS = new NotificaModel();

			String lCodTipoTDS = getRequestStringParameter(ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO + "_TDS");

			String lCodUffTDS = this.getCodUfficioByCodTipoUfficioDescrComune(lCodTipoTDS,
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodTipoNotifica("E");
			lNotModTDS.setDataInvio(lDataEmissione);

			lNotModTDS.setUffCodUfficio(lCodUffTDS);

			lNotModTDS.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lNotModTDS.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lNotModTDS.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			lNotifiche.add(lNotModTDS);
		}

		// Magistrato di sorveglianza
		if (!isRequestParameterNullObj(ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO + "_UDS")
				&& !getRequestStringParameter(ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO + "_UDS").equals("-")
				&& !isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)) {
			NotificaModel lNotModUDS = new NotificaModel();

			String lCodTipoUDS = getRequestStringParameter(ICostantiUfficio.CAMPO_COD_TIPO_UFFICIO + "_UDS");

			String lCodUffUDS = this.getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUDS,
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));

			lNotModUDS.setCodEsito("-");
			lNotModUDS.setCodTipoNotifica("E");
			lNotModUDS.setDataInvio(lDataEmissione);

			lNotModUDS.setUffCodUfficio(lCodUffUDS);

			lNotModUDS.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lNotModUDS.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lNotModUDS.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			lNotifiche.add(lNotModUDS);
		}

		// Autorità
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N)
				&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N).equals("-")
				&& !this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N)
						.equals("")) {
			NotificaModel lNotModPol = new NotificaModel();

			String lPolizia = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N);
			String lSedePolizia = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N)) {
				String lNotePolizia = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodTipoNotifica("E");
			lNotModPol.setDataInvio(lDataEmissione);

			lNotModPol.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lNotModPol.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lNotModPol.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			// Autorità esterna
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lAut.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lAut.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// Ufficio Recupero Crediti
		//
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI)
				&& !this.getRequestStringParameter(ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI)
						.equals("-")
				&& !this.getRequestStringParameter(ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI)
						.equals("")) {
			NotificaModel lNotModUffRecCred = new NotificaModel();

			String lCodTipoUffRecCred = this
					.getRequestStringParameter(ICostantiNotifica.CAMPO_COD_TIPO_UFF_REC_CREDITI);
			String lSedeUffRecCredito = this
					.getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_UFF_REC_CREDITI);

			// if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N))
			// {
			// String lNotePolizia =
			// this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N);
			// lNotModPol.setNote(lNotePolizia);
			// }

			lNotModUffRecCred.setCodEsito("-");
			lNotModUffRecCred.setCodTipoNotifica("C");
			lNotModUffRecCred.setDataInvio(lDataEmissione);

			lNotModUffRecCred.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lNotModUffRecCred.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lNotModUffRecCred.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			// Autorità esterna
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lCodTipoUffRecCred);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedeUffRecCredito));
			lAut.setCodSede(lComMod.getCodComune());

			lAut.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lAut.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lAut.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			lNotModUffRecCred.setAutoritaEsterna(lAut);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lNotModUffRecCred = " + lNotModUffRecCred);
			lNotifiche.add(lNotModUffRecCred);
		}

		// Avvocati
		String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		// String[] lArrayNote =
		// this.getRequestStringParameters(ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI);

		// String[] lArrayTipoAutAvv = this.getRequestStringParameters
		// (ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		// String[] lArraySedeAvv = this.getRequestStringParameters (ICostantiAutoritaEsterna.CAMPO_COD_SEDE);

		int lNumAvvNotifiche = lAvvocati.length;
		for (int i = 0; i < lNumAvvNotifiche; i++) {
			NotificaModel lNot = new NotificaModel();

			lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[i]));

			lNot.setCodTipoNotifica("N");
			lNot.setDataInvio(lDataEmissione);
			lNot.setCodEsito("-");

			// lNot.setNote(lArrayNote[i]);

			lNot.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
			lNot.setDataInserimento(lEveNot.getEvento().getDataInserimento());
			lNot.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());

			// AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			// lAut.setCodTipoAutorita (lArrayTipoAutAvv[i]);
			//
			// ComuneModel lComMod = new ComuneModel (getCodComuneByDescr(lArraySedeAvv[i]));
			//
			// lAut.setCodSede(lComMod.getCodComune());
			//
			// lAut.setCodOperatoreInserimento (lEveNot.getEvento().getCodOperatoreInserimento());
			// lAut.setDataInserimento (lEveNot.getEvento().getDataInserimento());
			// lAut.setCodUfficioInserimento (lEveNot.getEvento().getCodUfficioInserimento());
			//
			// //Setto l'Autorita Esterna per la notifica corrente
			// lNot.setAutoritaEsterna(lAut);
			lNotifiche.add(lNot);
		}

		lEveNot.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Evento = " + lEveNot.getEvento());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Numero Notifiche = " + lNotifiche.size());

		Iterator lIterNotifiche = lNotifiche.iterator();
		while (lIterNotifiche.hasNext()) {
			NotificaModel lNotifica = (NotificaModel) lIterNotifiche.next();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lNotifica = " + lNotifica);
		}
		return lEveNot;
	}

}