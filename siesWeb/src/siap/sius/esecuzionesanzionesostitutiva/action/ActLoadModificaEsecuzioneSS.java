package siap.sius.esecuzionesanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.util.FascicoloUtils;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: Modifica Esecuzione Sanzione Sostitutiva
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */
public class ActLoadModificaEsecuzioneSS extends ActionSiap implements ICostantiEsecuzioneSS {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione del Bottone di ritorno.
		this.gestioneRitorno();

		// Modifica Procedimento di esecuzione SS.
		BigDecimal lIdFascicoloSius = null;
		BigDecimal lIdSoggetto = null;
		if (isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIUS))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Fascicolo di Esecuzione Sanzione Sostitutiva non selezionato");
		else {
			lIdFascicoloSius = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIUS));

			// Riempio il model.
			FascicoloGPModel lFasGPMod = new FascicoloGPModel();
			lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(lIdFascicoloSius);

			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasGPMod = lCtrl.ExRicercaFascicoloByKey(lIdFascicoloSius);

			setRequestAttribute("fascicoloEsecuzione", lFasGPMod);
			// Metto in sessione il fascicolo SIUS.
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);
		}

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO)) {
			lIdSoggetto = new BigDecimal(getRequestStringParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Soggetto titolare di Esecuzione Sanzione Sostitutiva non individuato");

		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
		EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
				.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFascicoloSius);

		if (lESSModel == null || lESSModel.getIdEsecuzioneSanzioneSost().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!");
		else
			setRequestAttribute("sanzioneSostitutiva", lESSModel);

		// se esiste un periodo altra sanzione non permetto la modifica dei quantum
		// nella maschera successiva
		IPeriodoAltraSanzione lCtrllst = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		List lListaSanzioniSius = lCtrllst.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFascicoloSius);
		if (lListaSanzioniSius != null)
			setRequestAttribute("listaSanzioniSius", lListaSanzioniSius);

	    // MEV_2023-35 Recupero GP per determinare il contenuto  e passarlo al CTRL
        String lCodContenuto = "";
	    if (!lESSModel.getGenPridGeneraleProcedimento().equals(null)) {
            IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
            FascicoloGPModel lFasGPModel = lFasCtrl
                    .ExRicercaFascicoloByGenProc(lESSModel.getGenPridGeneraleProcedimento());
            setRequestAttribute("sanzioneUno", lFasGPModel);
            lCodContenuto = lFasGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
        }
       // MEV_2023-35 
        
        // MEV_2023-35 aggiunto lCodContenuto
		Vector lVect = lESSCtrl.ExRicercaDettaglioEsecuzioneSS(lESSModel.getIdEsecuzioneSanzioneSost(),
				lIdSoggetto, this.getCodUfficioUtenteConnesso(),lCodContenuto);



		setRequestAttribute("sanzioni", lVect);

		// Lettura del fascicolo SIEP se il parametro passato è != null.
		BigDecimal lIdFascicoloSiep = null;
		DettaglioFascicoloModel lDettaglio = null;
		if (!(isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP))
				&& (getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP)).length() > 4) {
			lIdFascicoloSiep = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP));

			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);
		}

		if (lDettaglio != null)
			setRequestAttribute("dettaglioFascSiep", lDettaglio);

		// Imposta Tipo Atto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto());
		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), 36);
		setRequestAttribute("mittenteAtto", "" + lOption);

		// Imposta Contenuto.
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// MERGE v10 COLLAUDO: aggiunta casistica per i minorenni
		if ("UDSM".equals(strCodTipoUfficio))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);
		setRequestAttribute("contenuto", "" + lOption);

		// Realizzazione filtro sui contenuti per "S12"
		FascicoloUtils lFascicoloUtils = new FascicoloUtils();
		// MEV_66: aggiunto parametro di passaggio
		String[] lFilter = lFascicoloUtils.filtraContenutiSS(strCodTipoUfficio);
		lOption.setFilter(lFilter);
		setRequestAttribute("contenutoEsecuzione", "" + lOption);

		// Imposta la Collection Contenuto.
		Collection lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDS();
		setRequestAttribute("collContenuto", lCol);

		return PG_LOAD_MODIFICA_ESECUZIONE_SS; // restituisce la jsp di VIEW
	}

}