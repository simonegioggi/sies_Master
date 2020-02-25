package siap.sius.udienza.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Fissazione Udienza
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
public class ActDettaglioRinvioUdienza extends ActionSiap implements ICostantiUdienza {

	public String processRequest() throws Exception {

		this.setLinkRitorno();

		// recupero del GENERALE PROCEDIMENTO in sessione.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Caricamento dati in UdienzaModel.
		BigDecimal lId_udienza = null;
		BigDecimal lId_udi_pro = null;
		String sUdienzePrecedenti = "";
		UdienzaModel lUdienza = new UdienzaModel();

		// Luigi 15-2-2005
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)) {
			// Se passato nella request si utilizza l' ID Udienza_procedimento e si ripassa nella request
			lId_udi_pro = getRequestBigDecimalParameter(
					ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO);
			setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO, "" + lId_udi_pro);

			// Si ricava l'udienza attraverso l'ID Udienza_procedimento
			IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
			UdienzaProcedimentoModel lUdienzaProcedimento = lUdiProCtrl
					.ExRicercaUdienzaProcedimentoByKey(lId_udi_pro);
			lId_udienza = lUdienzaProcedimento.getUdiIdUdienza();
		}
		// STUB 22/06/2004 Se rinviato a nuovo ruolo il parametro CAMPO_ID_UDIENZA = null; in tal caso
		// l'udienza non cambia.
		else if (isRequestParameterNullObj(ICostantiUdienza.CAMPO_ID_UDIENZA)
				|| getRequestStringParameter(ICostantiUdienza.CAMPO_ID_UDIENZA).equals("null")) {
			lId_udienza = lFasGPMod.getGeneraleProcedimentoModel().getUdiIdUdienza();
		} else {
			lId_udienza = getRequestBigDecimalParameter(ICostantiUdienza.CAMPO_ID_UDIENZA);
		}

		// chiama il controller Udienza

		if (lId_udienza != null) {
			IUdienza lUdiCtrl = SIUSLookupRemote.getUdienzaRemote();
			lUdienza = lUdiCtrl.ExRicercaUdienzaByKey(lId_udienza);
			/*
			 * new UdienzaModel (); aUdienza.setIdUdienza(lId_udienza);
			 * aUdienza.setDataUdienza(this.getRequestDateParameter(
			 * ICostantiUdienza.CAMPO_DATA_UDIENZA,"yyyyMMdd"));
			 * aUdienza.setLuogoUdienza(this.getRequestStringParameter(ICostantiUdienza.CAMPO_LUOGO_UDIENZA));
			 */
			// Ricerca elenco di Udienze precedenti
			/* BigDecimal lId_GeneraleProcedimento = */lFasGPMod.getGeneraleProcedimentoModel()
					.getIdGeneraleProcedimento();
			GeneraleProcedimentoModel aGeneraleProcedimento = new GeneraleProcedimentoModel();
			aGeneraleProcedimento.setIdGeneraleProcedimento(
					lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
			aGeneraleProcedimento.setUdiIdUdienza(lId_udienza);

			aGeneraleProcedimento.setDataCameraConsiglio(
					lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio());

			sUdienzePrecedenti = lUdiCtrl.ExRicercaUdienzePrecedenti(lUdienza, aGeneraleProcedimento);
		}
		// Imposta l'elenco delle udienze nella request.
		setRequestAttribute("sudienze", sUdienzePrecedenti);
		setRequestAttribute("udienza", lUdienza);

		return PG_DETTAGLIO_RINVIOUDIENZA;
	}

}