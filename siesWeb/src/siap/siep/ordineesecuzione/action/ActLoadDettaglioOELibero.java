package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.web.ActSIESDettaglioProvvedimento;

/**
 * <p>
 * Title: ActLoadDettaglioOELibero
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Ordine Esecuzione Libero
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
public class ActLoadDettaglioOELibero extends ActSIESDettaglioProvvedimento
		implements ICostantiOrdineEsecuzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();

		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		/*
		 * REWORK DETTAGLIO PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); IPosizioneGiuridica lPosCtrl =
		 * SIEPLookupRemote.getPosizioneGiuridicaRemote(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lEveMod.
		 * getEvento().getFasSieIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento,
						lEveMod.getEvento().getFasSieIdFascicoloSiep());

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		if (lPos == null || lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Al Procedimento non ha una Posizione Giuridica");

		setRequestAttribute("posizioneluogoaltra", lPos);

		/*
		 * REWORK DETTAGLIO - IPenaResidua lCtrlp = SIEPLookupRemote.getPenaResiduaRemote(); PenaResiduaModel
		 * llPenMod =
		 * lCtrlp.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lEveMod.getEvento().getFasSieIdFascicoloSiep());
		 */
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,
				lEveMod.getEvento().getFasSieIdFascicoloSiep());

		if (llPenMod != null) {

			lDataInizioPena = llPenMod.getDataInizio();
			// lDataFinePenaM = llPenMod.getDataFine();
			lDataFinePenaA = llPenMod.getDataFinePresunta();
			setRequestAttribute("StrdataInizioPena",
					DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
			setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
			setRequestAttribute("penaresidua", llPenMod);
		}
		// int lIndex = 0;

		/*--- REWORK DETTAGLIO
		INUTILE DATO CHE LA JSP RICAVA GLI AVVOCATI DALL'EVENTO
		
		Vector lVectAvvocati = new Vector();
		
		  for (lIndex = 0; lIndex < lEveMod.getNotifiche().length; lIndex++)
		  {
		    //Controllo se c'e' un Avvocato associato alla Notifica
		    if (lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep() != null)
		    {
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		AvvocatoSiepModel lAvvocato = lAvvCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lEveMod.getNotifiche()[lIndex].getAvvIdAvvocatoFascicoloSiep());
		lVectAvvocati.add(lAvvocato);
		    }
		  }
		
		  if (lVectAvvocati.size() > 0)
		    setRequestAttribute("avvocati", lVectAvvocati);
		*/
		setRequestAttribute("eventonotifica", lEveMod);

		// Visualizzazione fascicolo Bdmc
		FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();
		IFascicoloSiepBdmc lCtrlFas = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
		lFasMod.setIdEvento(lEveMod.getEvento().getIdEvento());
		Vector lVect = lCtrlFas.ExRicercaFascicoloSiepBdmc(lFasMod);
		setRequestAttribute("fascicoloBdmc", lVect);

		// setRequestAttribute("modifica",getRequestStringParameter("modifica"));

		return PG_LOAD_DETTAGLIO_OE_CONDANNATO_LIBERO;
	}

}