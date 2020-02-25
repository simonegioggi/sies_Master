package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.archiviazione.action.ICostantiArchiviazione;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciArchiviazionePerProvvGEsecuzione
 * </p>
 * <p>
 * Description: Load dell Inserimento di Archiviazione
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * per Provvedimento emesso dal G.E. (appl. MIS. SIC.)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Ambros
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciArchiviazionePerProvvGEsecuzione extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiArchiviazione {
	public String processRequest() throws Exception {
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// 05/11/2014 Funzione di esclusiva competenza dei proc. di classe IV.
		if (lFascMod.getChiaveProgr().intValue() < 40000 || lFascMod.getChiaveProgr().intValue() >= 50000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione consentita solo per procedimento di classe IV!");
			return IWebConstants.PG_MESSAGE;
		}

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		this.isEventoNonValidato();

		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltra = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
						.getIdFascicoloSiep());

		if (lPosLuoAltra == null || lPosLuoAltra.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPosLuoAltra);

		/*********************************** Pena Residua ***************************/
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		if (lPenaResidua != null)
			setRequestAttribute("penaresidua", lPenaResidua);

		// *********************************** Controllo esistenza Avvocato *************************/
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvVect);

			Option lOptionAutEst = new Option(DecodificheManager.getInstance().getTipoAutorita());
			String[] lFiltroAutEst = { "22", "C0" };
			lOptionAutEst.setFilter(lFiltroAutEst);
			setRequestAttribute("autoritaEsterna", "" + lOptionAutEst);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve passare anche in assenza del Difensore
			// 02/12/2014 su richiesta m.t. gli avvocati non sono obbligatori
			// inoltre la form non li usa
			// RedirectTo lRedirigi = new RedirectTo();
			// lRedirigi.setPage(IWebConstants.PG_MAIN);
			// setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage() +
			// " Impossibile eseguire Richiesta di Accertamento.");
			// lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&" +
			// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			//
			// return IWebConstants.PG_MESSAGE;
		}

		/******************************************************************************/
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// Oggetti per il caricamento delle combo Tipo Provvedimento (Ordinanza/Decreto)
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		// combo tipo Ufficio esterno (GIUDICE ESECUZIONE G.E. )
		Option lOptionEmi = new Option(DecodificheManager.getInstance().getMotivoDefiAltro());
		lOptionEmi.setFilter("0001");
		setRequestAttribute("emessoda", "" + lOptionEmi);

		// Oggetti per il caricamento delle combo autorita che ha emesso il Ordinanza/Decreto
		Option lAutoOption = new Option(DecodificheManager.getInstance().getTipoUfficioGE());
		setRequestAttribute("autorita", "" + lAutoOption);

		// Oggetti per il caricamento delle combo Tipo Definizione (Archiviazione)
		Option lOptionT = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		// lOptionT.setFilter(new String[] {"2774","2775","2776","2778","2779"});
		lOptionT.setFilter(new String[] { "2774", "2775", "2776", "2778", "2779", "2798", "2799", "2800",
				"2801", "2802", "2803" });
		setRequestAttribute("tipoArchiviazioni", "" + lOptionT);

		// Ricerca Misure sicurezza già presenti nel fascicolo

		List lListMis = new ArrayList();
		MisuraSicurezzaModel MisMod = null;

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		try {
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
			if (lListMis != null)
				if (lListMis.size() == 1)
					MisMod = (MisuraSicurezzaModel) lListMis.get(0);
		} catch (F3BException e) {

		}

		setRequestAttribute("listaMisure", lListMis);
		setRequestAttribute("MisuraModel", MisMod);

		// Riempimento ComboBox Autorità Destinatari
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// 12/03/2015
		// Aggiunto Desinatatio Sorveglianza
		Option lOptionSor = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionSor.setFilter(new String[] { "-", "TDS", "UDS", "UDSM" });
		setRequestAttribute("tipoUDS", "" + lOptionSor);

		// combo autorita che ha emesso il Ordinanza/Decreto (Destinatari di notifica)
		Option lOptionDest = new Option(DecodificheManager.getInstance().getTipoUfficioGE());
		setRequestAttribute("autoritaDest", "" + lOptionDest);

		return PG_LOAD_INS_ARCHIVIAZIONE_PROVV_GE;
	}

}