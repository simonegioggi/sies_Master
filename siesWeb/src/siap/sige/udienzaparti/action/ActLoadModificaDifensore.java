package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActLoadModificaDifensore
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica Difensore associato alla Parte
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActLoadModificaDifensore extends ActionSige implements ICostantiPartiUdienza, ICostantiEvento {

	public String processRequest() throws Exception {

		// Prepara la pagina di destinazione
		String lPage = PG_LOAD_MODIFICA_DIFENSORE_PARTE;

		// INIZIO 20200624 [SG]: tolto set di navigazione, deve tornare sempre all'ordinanza
		// Gestione pulsante di ritorno.
		// setLinkRitorno();

		// Identificativo della parte
		String lIdSoggetto = getRequestStringParameter(CAMPO_ID_SOGGETTO);

		// Identificativo evento udienza
		String idEventoUdienza = null;
		if (!isRequestParameterNullObj(CAMPO_ID_EVENTO_UDIENZA)) {
			idEventoUdienza = getRequestStringParameter(CAMPO_ID_EVENTO_UDIENZA);
		}

		String idUdienzaSIGE = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE)) {
			idUdienzaSIGE = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		}

		String idUdienzaProcedimentoSIGE = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)) {
			idUdienzaProcedimentoSIGE = getRequestStringParameter(
					ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
		}

		String codTipoParte = "";
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PART)) {
			codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);
		}

		// Identificativo evento udienza
		String modalita = "M";
		if (!isRequestParameterNullObj("modalita")) {
			modalita = getRequestStringParameter("modalita");
		}

		IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();

		AnagraficaPartiUdienzaModel anagParteUdienzaRet = lCtrl
				.ExRicercaParteUdienzaByKey(new BigDecimal(lIdSoggetto));

		// Difensori associati alla Parte Offesa/Civile, con relative notifiche.
		Vector<PartiUdienzaDifensoreModel> lDifensori = lCtrl
				.ExRicercaDifensoreByIdSoggetto(new BigDecimal(lIdSoggetto));
		if (lDifensori.size() > 0) {
			setRequestAttribute("difensori", lDifensori);
		}

		// Notifica del soggetto
		NotificaModel notificaSoggetto = lCtrl.ExRicercaNotificaByIdSoggetto(new BigDecimal(lIdSoggetto));
		setRequestAttribute("notificaSoggetto", notificaSoggetto);

		setRequestAttribute("anagraficaParteUdienza", anagParteUdienzaRet);
		setRequestAttribute("idEventoUdienza", idEventoUdienza);
		setRequestAttribute("idUdienzaSige", idUdienzaSIGE);
		setRequestAttribute("idUdienzaProcedimentoSige", idUdienzaProcedimentoSIGE);
		setRequestAttribute("codTipoParte", codTipoParte);
		setRequestAttribute("modalita", modalita);
		// setRequestAttribute(CAMPO_ID_SOGGETTO, lIdSoggetto);

		Option lOption = new Option(DecodificheManager.getInstance().getSesso(),
				anagParteUdienzaRet.getSesso());
		setRequestAttribute("sesso", "" + lOption);

		if (anagParteUdienzaRet.getRagSociale() != null && !anagParteUdienzaRet.getRagSociale().equals("")) {
			lOption = new Option(DecodificheManager.getInstance().getRagioneSociale(),
					anagParteUdienzaRet.getRagSociale());
		} else {
			lOption = new Option(DecodificheManager.getInstance().getRagioneSociale(), "-");
		}
		setRequestAttribute("ragioneSociale", "" + lOption);

		if (anagParteUdienzaRet.getCodProvincia() != null
				&& !anagParteUdienzaRet.getCodProvincia().equals("")) {
			lOption = new Option(DecodificheManager.getInstance().getProvincie(),
					anagParteUdienzaRet.getCodProvincia());
		} else {
			lOption = new Option(DecodificheManager.getInstance().getProvincie(), "-");
		}
		setRequestAttribute("province", "" + lOption);

		if (anagParteUdienzaRet.getCodStatoNascita() != null) {
			lOption = new Option(DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"),
					anagParteUdienzaRet.getCodStatoNascita());
		} else {
			lOption = new Option(DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
		}
		setRequestAttribute("nazioni", "" + lOption);

		if (anagParteUdienzaRet.getResidenza() != null
				&& anagParteUdienzaRet.getResidenza().getCodStato() != null) {
			lOption = new Option(DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"),
					anagParteUdienzaRet.getResidenza().getCodStato());
		} else {
			lOption = new Option(DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
		}
		setRequestAttribute("nazioniResidenza", "" + lOption);

		// LISTA UFFICI per la Notifica all'Avvocato (solo ufficio UNEP)
		Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		String[] lStringFilter = { "22" };
		Option lOptionAvv = new Option(lTipoIstituto, "22", 75);
		lOptionAvv.setFilter(lStringFilter);
		setRequestAttribute("tipiIstituto", "" + lOptionAvv);

		// LISTA UFFICI per la Notifica all'Avvocato (solo ufficio UNEP)
		String[] lStringFilterSNT = { "-", "22" };
		Option lOptionSNT = new Option(lTipoIstituto);
		lOptionSNT.setFilter(lStringFilterSNT);
		setRequestAttribute("tipiIstitutoSNT", "" + lOptionSNT);

		// Lista Autorità per la Notifica al Soggetto
		Option lOptionAut = new Option();
		if (notificaSoggetto != null && notificaSoggetto.getAutoritaEsterna() != null
				&& notificaSoggetto.getAutoritaEsterna().getIdAutoritaEsterna() != null) {
			lOptionAut = new Option(DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getTipoAutorita(), "-"),
					notificaSoggetto.getAutoritaEsterna().getCodTipoAutorita().toString());
		} else {
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);
		}
		setRequestAttribute("tipoAutorita", lOptionAut.toString());

		if (anagParteUdienzaRet != null && anagParteUdienzaRet.getFlagConvUdienza() != null
				&& anagParteUdienzaRet.getFlagConvUdienza().equals("S")) {
			lOption = new Option(DecodificheManager.getInstance().getFlagSN(), "S");
		} else {
			lOption = new Option(DecodificheManager.getInstance().getFlagSN(), "N");
		}

		setRequestAttribute("convocazioneUdienza", "" + lOption);

		return lPage; // restituisce la jsp di VIEW
	}

}