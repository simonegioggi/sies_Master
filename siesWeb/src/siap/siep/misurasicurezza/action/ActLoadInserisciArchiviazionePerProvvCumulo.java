package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.action.ICostantiArchiviazione;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciArchiviazionePerProvvCumulo
 * </p>
 * <p>
 * Description: Load Inserimento di Archiviazione per Provvedimento di cumulo
 * </p>
 */
public class ActLoadInserisciArchiviazionePerProvvCumulo extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiArchiviazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		
		UtenteModel lUtenteMod = getUtenteConnesso();

		// Controllo Presenza del Fascicolo in Sessione
		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicoloSiep = fsm.getIdFascicoloSiep();
		siesLogger.info("FASCICOLO: " + fsm.getChiaveAnno() + "/" + fsm.getChiaveProgr() + " con ID: "
				+ idFascicoloSiep);

		// Funzione di esclusiva competenza dei proc. di classe IV.
		if (fsm.getChiaveProgr().intValue() < 40000 || fsm.getChiaveProgr().intValue() >= 50000) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Operazione consentita solo per procedimento di classe IV!");
			return IWebConstants.PG_MESSAGE;
		}

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		
		isEventoNonValidato();

		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = ipg
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(fsm
						.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(pgldacm))
			return IWebConstants.PG_MESSAGE;
		setRequestAttribute("posizioneluogoaltra", pgldacm);

		// Pena Complessiva
		IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel pcm = ipc.ExRicercaPenaComplessivaByIdFascicolo(idFascicoloSiep);
		if (pcm == null) {
			siesLogger.info("Pena Complessiva non presente.");
			// throw new SIEPException(SIEPException.USER_MESSAGE,
			// "Pena Complessiva non presente. Impossibile eseguire la richiesta.");
		} else {
			// ERGASTOLO
			String flagErgastolo = "N";
			// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
			if (pcm.getCodTipoPenaDetentiva() != null && pcm.getCodTipoPenaDetentiva() != "") {
				if (pcm.getCodTipoPenaDetentiva().equals("03"))
					flagErgastolo = "S";
				else if (pcm.getCodTipoPenaDetentiva().equals("04"))
					flagErgastolo = "D";
			}
			setRequestAttribute("flagergastolo", flagErgastolo);
		}

		/*********************************** Pena Residua ***************************/
		IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(idFascicoloSiep);
		if (prm != null)
			setRequestAttribute("penaresidua", prm);

		/******************************************************************************/
		IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imc.ExRicercaMagistratoCompetenteByFascicolo(fsm
				.getIdFascicoloSiep());
		if (mcmm != null)
			setRequestAttribute("magistratocompetente", mcmm);

		// 27032018 [EC] - MEV_39
		// VERIFICO SE IL FASCICOLO DI CLASSE IV SELEZIONATO E' COINVOLTO
		// IN UNA TRASMISSIONE DI ATTI Per competenza per emissione provvedimento di cumulo
		ICompetenza iiComp =  SIEPLookupRemote.getCompetenzaRemote();		
		Vector listaCompetenze = iiComp.ExRicercaCompetenzePerCumulo(idFascicoloSiep, false);		
		
		
		if (Utils.isPresent(listaCompetenze)) {
			CompetenzaModel icmRes = (CompetenzaModel) listaCompetenze.elementAt(0);
			String codUfficioAutoritaComp = icmRes.getCodUfficioAutoritaComp();
			if(codUfficioAutoritaComp != null){
				// devo capire se è un ufficio del mio stesso distretto
				UfficioModel um = getUfficioByCodUfficio(codUfficioAutoritaComp);
				if(um != null && um.getCodDistretto().equals(lUtenteMod.getUfficioUtente().getCodDistretto())){
					// è del mio stesso distretto, quindi per recuperare i dati del cumulo devo fare una query diversa
					listaCompetenze = iiComp.ExRicercaCompetenzePerCumulo(idFascicoloSiep, true);	
					if (Utils.isPresent(listaCompetenze)) {
						icmRes = (CompetenzaModel) listaCompetenze.elementAt(0);
						setRequestAttribute("datiCumulo", icmRes);
					}else{
						setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
								+ fsm.getChiaveProgr()
								+ " è coinvolto in assorbimento cumulo, ma il cumulo non è stato ancora emesso. Impossibile procedere!");
						return IWebConstants.PG_MESSAGE;
					}
				}
				else{
					setRequestAttribute("datiCumulo", icmRes);
				}
			}						
			
			setRequestAttribute("codUfficioUtente", lUtenteMod.getUfficioUtente().getCodUfficio());			
			setRequestAttribute("codUfficioCumulo", icmRes.getChiaveUfficio());			
			setRequestAttribute("codTipoUfficioCumulo", icmRes.getCodTipoAutoritaComp());
			setRequestAttribute("descrTipoUfficioCumulo", icmRes.getDescrTipoAutoritaComp());
			setRequestAttribute("codComuneCumulo", icmRes.getCodLuogoAutoritaComp());
			setRequestAttribute("descrComuneCumulo", icmRes.getDescrLuogoAutoritaComp());
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr()
					+ " non risulta essere coinvolto in nessuna trasmissione per assorbimento cumulo. Impossibile procedere!");
			return IWebConstants.PG_MESSAGE;
		}	
		
		
		// Ricerca Cumulo Assorbente
//		IIstruttoriaCumulo iic = SIEPLookupRemote.getIstruttoriaCumuloRemote();
//		IstruttoriaCumuloModel icm = new IstruttoriaCumuloModel();
//		icm.setFasSieIdFascicoloSiep(idFascicoloSiep);
//		Vector listaIstruttorie = iic.ExRicercaIstruttoriaCumulo(icm);
//		if (Utils.isPresent(listaIstruttorie)) {
//			IstruttoriaCumuloModel icmRes = (IstruttoriaCumuloModel) listaIstruttorie.elementAt(0);
//			setRequestAttribute("istruttoriaCumulo", icmRes);
//			UfficioModel um = getUfficioByCodUfficio(icmRes.getCodUfficioInserimento());
//			setRequestAttribute("codTipoUfficioCumulo", um.getCodTipoUfficio());
//			setRequestAttribute("descrTipoUfficioCumulo", um.getDescrTipoUfficio());
//			setRequestAttribute("codComuneCumulo", um.getCodComune());
//			setRequestAttribute("descrComuneCumulo", um.getDescrComune());
//		} else {
//			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
//					+ fsm.getChiaveProgr()
//					+ " non risulta essere coinvolto in alcuna istruttoria di cumulo. Impossibile procedere!");
//			return IWebConstants.PG_MESSAGE;
//		}

		Option tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + tipoAutorita);

		Option tipoUfficio = new Option(DecodificheManager.getInstance().getTipoUfficio());
		tipoUfficio.setFilter(new String[] { "-", "DIB", "CAP", "TRIBSD" });
		setRequestAttribute("uffrecrediti", "" + tipoUfficio);

		// pagina di ritorno
		return PG_LOAD_INS_ARCHIVIAZIONE_PER_PROVV_CUMULO;
	}

}