package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

// genny 02/03/2004
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;

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
@SuppressWarnings("rawtypes")
public class ActDettaglioVerbaleRinvioUdienza extends ActionSius implements ICostantiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		this.setLinkRitorno();

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		UdienzaProcedimentoModel lUdienzaProcedimento = null;
		String sUdienzePrecedenti = "";

		// genny 05/04/2003
		// mi leggo l'ID_UDIENZA
		BigDecimal lId_udienza = lFasGPMod.getGeneraleProcedimentoModel().getUdiIdUdienza();
		BigDecimal lId_evento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();

		// Luigi 15-2-2005
		// Se passato nella request l' ID Udienza_procedimento si avanti ripassa nella request
		// Si Chiama il controller Udienza_Procedimento per risalire all'Udienza
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)) {
			BigDecimal lIdUdiPro = getRequestBigDecimalParameter(
					ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO);
			setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO, "" + lIdUdiPro);
			lUdienzaProcedimento = lUdiProCtrl.ExRicercaUdienzaProcedimentoByKey(lIdUdiPro);
		} else {
			lUdienzaProcedimento = lUdiProCtrl.ExRicercaUdienzaProcedimentoByEve(lId_evento);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("###### Model UDIENZA_PROCEDIMENTO : " + lUdienzaProcedimento);

		// Se trovato UDIENZA_PROCEDIMENTO si ricava l'ID Udienza e
		if (lUdienzaProcedimento != null) {
			lId_udienza = lUdienzaProcedimento.getUdiIdUdienza();
		}

		UdienzaModel aUdienzaRet = new UdienzaModel();

		if (lId_udienza != null) {
			// throw new SIUSException(SIUSException.USER_MESSAGE, "Impossibile individuare l'udienza!");

			IUdienza lCtrlUdienza = SIUSLookupRemote.getUdienzaRemote();
			aUdienzaRet = lCtrlUdienza.ExRicercaUdienzaByKey(lId_udienza);

			// mi leggo l'ID_GENERALE PROCEDIMENTO E ..
			BigDecimal lIdGeneraleProcedimento = lFasGPMod.getGeneraleProcedimentoModel()
					.getIdGeneraleProcedimento();
			GeneraleProcedimentoModel aGeneraleProcedimento = new GeneraleProcedimentoModel();
			aGeneraleProcedimento.setIdGeneraleProcedimento(lIdGeneraleProcedimento);
			aGeneraleProcedimento.setUdiIdUdienza(lId_udienza);
			aGeneraleProcedimento.setDataCameraConsiglio(
					lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio());

			// chiama il controller
			IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
			sUdienzePrecedenti = lCtrl.ExRicercaUdienzePrecedenti(aUdienzaRet, aGeneraleProcedimento);
		}

		// genny 02/03/2004
		// Preleva i tenori, per il generale procedimento.
		ITenore lTenCtrl = SIUSLookupRemote.getTenoreRemote();
		Vector lTenori = lTenCtrl.ExRicercaTenoreByGenProc(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());

		// genny 03/03/2004
		// Preleva i dati dell'Evento legato a lId_evento.
		IEvento lventoCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel aEvento = lventoCtrl.ExRicercaEventoByKey(lId_evento);

		// Modificabilità
		String lModificabile = "NO";
		String lCancellabile = "NO";

		if (IsFascicoloSiusModificabile()) {
			lModificabile = "SI";
			// Stampabilità
			if (aEvento.getFlagDocumentoRegistrato() == null
					|| aEvento.getFlagDocumentoRegistrato().compareTo("N") == 0)
				lCancellabile = "SI";
		}

		// Imposta gli oggetti nella request.
		setRequestAttribute("tenori", lTenori);

		// Imposta l'elenco delle udienze nella request.
		// setRequestAttribute( "", aUdienza )

		setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
				lUdienzaProcedimento.getIdUdienzaProcedimento());

		setRequestAttribute("sudienze", sUdienzePrecedenti);
		setRequestAttribute("udienza", aUdienzaRet);
		setRequestAttribute("evento", aEvento);
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Cancellabile", lCancellabile);

		return PG_DETTAGLIO_VERBALERINVIOUDIENZA;
	}

}