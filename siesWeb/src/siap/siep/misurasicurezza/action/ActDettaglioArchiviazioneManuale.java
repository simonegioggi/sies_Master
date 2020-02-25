package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.DateUtils;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActDettaglioArchiviazioneManuale
 * </p>
 * <p>
 * Description: Load dell Dettaglio del Provvedimento
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * di Archiviazione manuale (appl. MIS. SIC.)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 * @version 1.0
 */
public class ActDettaglioArchiviazioneManuale extends ActSIESDettaglioProvvedimento implements
		ICostantiMisuraSicurezza, ICostantiOrdineEsecuzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// EventoNotifica (Provvedimento di annotazione Inserito)
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		setRequestAttribute("eventonotifica", lEveMod);

		// Cerco Isstituto Detenzione
		IstitutoDetenzioneModel lIstDetenzione = null;
		IIstitutoDetenzione ctrld = SIEPLookupRemote.getIstitutoDetenzioneRemote();

		for (int i = 0; i < lEveMod.getNotifiche().length; i++) {
			if (lEveMod.getNotifiche()[i].getCodTipoNotifica().equals("E")) {
				if (lEveMod.getNotifiche()[i].getIstDetIdIstitutoDetenzione() != null) {
					lIstDetenzione = (IstitutoDetenzioneModel) ctrld.ExRicercaIstitutoDetenzioneByKey(lEveMod
							.getNotifiche()[i].getIstDetIdIstitutoDetenzione());
				}
			}
		}
		setRequestAttribute("istitutodetenzione", lIstDetenzione);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
//		String lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();

		setRequestAttribute("posizioneluogoaltra", lPos);

		// PenaResidua
		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
//		Date lDataFinePenaM = null;

		// BigDecimal idPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);

		PenaResiduaModel llPenMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		llPenMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (llPenMod != null) {
			lDataInizioPena = llPenMod.getDataInizio();
//			lDataFinePenaM = llPenMod.getDataFine();
			lDataFinePenaA = llPenMod.getDataFinePresunta();
		}
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", llPenMod);

		// ARCHIVIAZIONE
		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();
		lArcMod = lCtrlArc.ExRicercaArchiviazioneCssaIstitutoByIdEvento(lId);
		setRequestAttribute("archiviazione", lArcMod);

		// Misure Sicurezza

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		List lListMis = new ArrayList();

		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("listaMisureSic", lListMis);

		// Magistrato
		IMagistrato lMCtrl = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagMod = lMCtrl.ExRicercaMagistratoByEvento(lId);
		setRequestAttribute("magistrato", lMagMod);

		return PG_DETTAGLIO_ARCHIVIAZIONE_MANUALE;

	} // chiude processRequest

} // Chiude Classe