package siap.siep.misuracautelare.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbperipren.controller.ISbPeripren;
import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.bdmc.sbpren.controller.ISbPren;
import siap.bdmc.sbpren.model.ProvvedimentoModelBDMC;
import siap.bdmc.sbpren.model.SbPrenModel;
import siap.bdmc.sbviewprocpena.controller.ISbViewProcpena;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaMisuraCautelare
 * </p>
 * <p>
 * Description: Classe Action per la load Modifica di Misura Cautelare
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
public class ActLoadModificaMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare {

	/**
	 * Effettua la ricerca della misura passata in input e restituisce la pagina per la modifica dei dati
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("misura cautelare",
				getRequestStringParameter(CAMPO_ID_MISURA_CAUTELARE), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "La " + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Recupero la Misura e la passo alla form
		// ==========================================================================
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE);

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		MisuraCautelareModel llMisMod = lCtrl.ExRicercaMisuraCautelareByKey(lId);

		setRequestAttribute("misuracautelare", llMisMod);

		// ==========================================================================
		// Carico le combo da passare alla pagina di visualizzazione preselezionando
		// l'opportuno valore
		// ==========================================================================
		// Option lOptionMisura = new Option( DecodificheManager.getInstance().getTipoMisuraCautelare(),
		// llMisMod.getCodTipoMisura());
		// Option lOptionUfficio = new Option( DecodificheManager.getInstance().getTipoUfficio(),
		// llMisMod.getCodTipoUfficioRifer());
		// Option lOptionMotivo = new Option( DecodificheManager.getInstance().getMotivoNonComputabile(),
		// llMisMod.getCodMotivoNonComputabile());
		//
		// setRequestAttribute("tipoMisura", "" + lOptionMisura );
		// setRequestAttribute("tipoUfficio", "" + lOptionUfficio );
		// setRequestAttribute("tipoMotivo", "" + lOptionMotivo );
		//
		// String lFlagComp = llMisMod.getFlagComputabile();
		// String lDataFine =StringUtils.toStringJSP(llMisMod.getDataFine());
		// setRequestAttribute("computabilità", lFlagComp);
		// setRequestAttribute("datafine", lDataFine);
		//
		// // Imposta Modalità.
		// setRequestAttribute("modalita", "M");
		//
		// return PG_LOAD_MODIFICAMISURACAUTELARE; //restituisce la jsp di VIEW

		// inizio nuova gestione modifica

		Option lOptionComp = new Option(DecodificheManager.getInstance().getMotivoNonComputabile(),
				llMisMod.getCodMotivoNonComputabile());
		// popolato con misure detentive e non detentive
		Option lOption = new Option(DecodificheManager.getInstance().getTipoMisuraCautelare());
		// popolato con misure detentive
		Option lOptionDetentive = new Option(DecodificheManager.getInstance()
				.getTipoMisuraCautelareDetentive(), llMisMod.getCodTipoMisura());
		// popolato con misure e non detentive
		Option lOptionNonDetentive = new Option(DecodificheManager.getInstance()
				.getTipoMisuraCautelareNonDetentive(), llMisMod.getCodTipoMisura());
		// Autorita' competente per territorio
		Option lOptionAutoritaCompTerritorio = new Option(DecodificheManager.getInstance()
				.getAutoritaCompetentePerTerritorio(), llMisMod.getAutoritaCompetente());

		// Option lOptionUff = new Option( DecodificheManager.getInstance().getTipoUfficio());

		// Recupero i dati per caricare la combo per un determinato "tipo ufficio"
//		UtenteModel utenteConnesso = getUtenteConnesso();
		String codTipoUfficio = "";
		String sedeTipoUfficio = "";
		// if (getUtenteConnesso().getUfficioUtente() != null &&
		// getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
		// codTipoUfficio = utenteConnesso.getUfficioUtente().getCodTipoUfficio();
		// //sedeTipoUfficio = utenteConnesso.getUfficioUtente().getDescrComune();
		// }
		if (llMisMod != null) {
			codTipoUfficio = llMisMod.getAutoritaEmittente();
			sedeTipoUfficio = llMisMod.getAutoritaEmittenteLuogoDesc();
		}
		IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
//		UfficioModel lUfficioModel = null;
		if (codTipoUfficio != null && sedeTipoUfficio != null)
			/*lUfficioModel = */lCtrlUfficio.getUfficioByCodTipoUffDescrComune(codTipoUfficio, sedeTipoUfficio);
		Option lOptionUffPM = new Option();
		lOptionUffPM = new Option(DecodificheManager.getInstance().getTipoUfficioPM());

		// MERGE v10: aggiunto GUP
		// lOptionTipUffRegGen Valorizzata tramite elenco := {"GUP","GIP","DIB","CAS","CAP","CASAP"}.
		Option lOptionTipUffRegGen = new Option();
		Collection tipUffRegGenCollection = new Vector();
		DecodificheModel lDec = new DecodificheModel();
		lDec.setCode(" - ");
		lDec.setDescription(" - ");
		tipUffRegGenCollection.add(lDec);
		lDec = new DecodificheModel();
		lDec.setCode("GIP");
		lDec.setDescription("GIP");
		tipUffRegGenCollection.add(lDec);
		lDec = new DecodificheModel();
		lDec.setCode("GUP");
		lDec.setDescription("GUP");
		tipUffRegGenCollection.add(lDec);
		lDec = new DecodificheModel();
		lDec.setCode("DIB");
		lDec.setDescription("DIB");
		tipUffRegGenCollection.add(lDec);
		lDec = new DecodificheModel();
		lDec.setCode("CAS");
		lDec.setDescription("CAS");
		tipUffRegGenCollection.add(lDec);
		lDec = new DecodificheModel();
		lDec.setCode("CAP");
		lDec.setDescription("CAP");
		tipUffRegGenCollection.add(lDec);
		lDec = new DecodificheModel();
		lDec.setCode("CASAP");
		lDec.setDescription("CASAP");
		tipUffRegGenCollection.add(lDec);
		lOptionTipUffRegGen = new Option(tipUffRegGenCollection, llMisMod.getTipoUfficioRegGen());

		// Autorità emittente
		Option lOptionAutoritaEmittenteUff = new Option(DecodificheManager.getInstance()
				.getTipoAutoritaEmittente(), llMisMod.getAutoritaEmittente());

		// Ufficio (condizioni uguali a Autorità emittente)
		Option lOptionUfficioDetNonDet = new Option(DecodificheManager.getInstance()
				.getTipoAutoritaEmittente(), llMisMod.getCodTipoUfficioRifer());

		// Festa Carlo Modifiche per integrazione Bdmc
		Vector lVectProvv = new Vector();
		String flagConnessioneBdmc = new String("S");
		try {
			// FascicoloSiepModel lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");
			SbPrenModel lSbPMod = new SbPrenModel();
			// lSbPMod.setCognSogg(lFascMod.getSoggetto().getCognome());
			// lSbPMod.setNomeSogg(lFascMod.getSoggetto().getNome());
			lSbPMod.setCodiUffiSies(this.getCodUfficioUtenteConnesso());
			lSbPMod.setUtenSies(this.getCodUtenteConnesso());
			ISbPren lCtrlSbPren = BDMCLookupRemote.getSbPrenRemote();
			lSbPMod.setCodiStatPren(new BigDecimal(0));
			ISbViewProcpena lProcPenaCtrl = BDMCLookupRemote.getSbViewProcpenaRemote();
			Vector lVect = lCtrlSbPren.ExRicercaSbPren(lSbPMod);
			Vector lVectPeripren = new Vector();

			Vector lVectSbViewProcPena = new Vector();
			if (lVect != null) {
				for (int i = 0; i < lVect.size(); i++) {
					ISbPeripren lCtrlPeripren = BDMCLookupRemote.getSbPeriprenRemote();
					SbPeriprenModel aSbPeripren = new SbPeriprenModel();
					SbPrenModel lSbPModAppo = (SbPrenModel) lVect.get(i);
					SbViewProcpenaModel lSbProcPena = new SbViewProcpenaModel();
					lSbProcPena.setIdPren(lSbPModAppo.getIdPren());
					// lSbProcPena.setAnnoRegiPmpm(lFascMod.getSentenza().getAnnoRegePm());
					// lSbProcPena.setNumeRegiPmpm(new BigDecimal(lFascMod.getSentenza().getNumeroRegePm()));
					lVectSbViewProcPena = lProcPenaCtrl.ExRicercaSbViewProcpena(lSbProcPena);
					if (lVectSbViewProcPena != null && lVectSbViewProcPena.size() != 0) {
						aSbPeripren.setIdPren(lSbPModAppo.getIdPren());
						// aSbPeripren.setCodStatPrenPeri("0");
						lVectPeripren = lCtrlPeripren.ExRicercaSbPeripren(aSbPeripren);

						// ========================================================================
						// = Routine di bonifica dei periodi già iscritti ma non ancora
						// = segnalati a Bdmc
						// = Powere by Festa Carlo
						// =========================================================================
						Vector lVectPeriprenScope = new Vector();
						MisuraCautelareBdmcModel lModBdmc = new MisuraCautelareBdmcModel();
						lModBdmc.setIdPren(lSbPModAppo.getIdPren());
						lModBdmc.setFlagComputabile("0");
						// lModBdmc.setFlagStato("I");
						lModBdmc.setStatoTrasmissioneIsc("N");
						IMisuraCautelareBdmc lCtrlBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
						Vector misCautBdmc = lCtrlBdmc.ExRicercaMisuraCautelareBdmc(lModBdmc);
						if (misCautBdmc != null && misCautBdmc.size() != 0 && lVectPeripren != null
								&& lVectPeripren.size() != 0) {
							for (int ii = 0; ii < lVectPeripren.size(); ii++) {
								SbPeriprenModel aSbPeriprenIsc = (SbPeriprenModel) lVectPeripren.get(ii);
								for (int kk = 0; kk < misCautBdmc.size(); kk++) {
									MisuraCautelareBdmcModel lModBdmcIsc = (MisuraCautelareBdmcModel) misCautBdmc
											.get(kk);
									if (aSbPeriprenIsc.getProgPeriPres()
											.equals(lModBdmcIsc.getProgPeriPres())) {
										aSbPeriprenIsc.setCodStatPrenPeri("9");
										break;
									}
								} // fine for misCautBdmc
								lVectPeriprenScope.add(aSbPeriprenIsc);
							} // fine for lVectPeriPren

						} // fine "if (misCautBdmc != null && misCautBdmc.size() != 0)"
						else {
							lVectPeriprenScope = lVectPeripren;
						}
						// = fine Routine di bonifica dei periodi già iscritti ma non ancora
						// ==============================================================================

						ProvvedimentoModelBDMC lProvvBdmc = new ProvvedimentoModelBDMC();
						lProvvBdmc.setSbPren(lSbPModAppo);
						lProvvBdmc.setSbPeriPren(lVectPeriprenScope);
						lProvvBdmc.setSbViewProcpena(lVectSbViewProcPena);
						lVectProvv.add(lProvvBdmc);
					}
				}
			}
		} catch (F3BException ex) {
			flagConnessioneBdmc = "N";
		}
		setRequestAttribute("lProvv", lVectProvv);
		setRequestAttribute("flagConnessioneBdmc", flagConnessioneBdmc);
		setSessionAttribute("lProvvSession", lVectProvv);
		setRequestAttribute("tipoMisura", "" + lOption);
		setRequestAttribute("tipoMisuraDetentive", "" + lOptionDetentive);
		setRequestAttribute("tipoMisuraNonDetentive", "" + lOptionNonDetentive);

		setRequestAttribute("motComp", "" + lOptionComp);
		// Recupero i dati per caricare la combo per tutti i "tipo ufficio"
		// setRequestAttribute("tipUff", "" + lOptionUff );
		// Recupero i dati per caricare la combo per un determinato "tipo ufficio PM"
		// PGCAP= Procura Generale della Repubblica Presso la Corte D'Appello
		// PM =Procura della Repubblica Presso il Tribunale Ordinario
		// PMM =Procura della Repubblica Presso il Tribunale per i Minorenni
		lOptionUffPM.setFilter(new String[] { "PM", "PMM", "PGCAP" });
		// default codice ufficio utente
		lOptionUffPM.setSelected(codTipoUfficio);
		setRequestAttribute("tipUffPM", "" + lOptionUffPM);
		// Autorita Emittente
		// GIP =Gip Presso il Tribunale Ordinario
		// GIPM=Gip Presso il Tribunale per i Minorenni
		// default codice ufficio utente
		// if(codTipoUfficio.equalsIgnoreCase("PM") || codTipoUfficio.equalsIgnoreCase("PGCAP")) {
		// lOptionAutoritaEmittenteUff.setSelected("GIP");
		// } else if(codTipoUfficio.equalsIgnoreCase("PMM")) {
		// lOptionAutoritaEmittenteUff.setSelected("GIPM");
		// }
		if (llMisMod.getAutoritaEmittente() != null) {
			lOptionAutoritaEmittenteUff.setSelected(llMisMod.getAutoritaEmittente());
			setRequestAttribute("tipUffAutEmi", "" + lOptionAutoritaEmittenteUff);
		} else {
			lOptionAutoritaEmittenteUff = new Option(DecodificheManager.getInstance()
					.getTipoAutoritaEmittente());
			setRequestAttribute("tipUffAutEmi", "" + lOptionAutoritaEmittenteUff);
		}

		setRequestAttribute("tipUffDetNonDet", "" + lOptionUfficioDetNonDet);
		// default luogo Autorità Emittente = comune ufficio dell'utente
		setRequestAttribute("defaultLuogoAutoritaEmittente", sedeTipoUfficio);
		// default Sede Tipo Ufficio PM = sede ufficio dell'utente
		setRequestAttribute("defaultSedeTipoUfficioPM", sedeTipoUfficio);
		// Recupero i dati per caricare la combo per un determinato "tipo ufficio Reg Gen"
		if (llMisMod.getTipoUfficioRegGen() != null) {
			setRequestAttribute("tipUffRegGen", "" + lOptionTipUffRegGen);
		} else {
			lOptionTipUffRegGen = new Option(tipUffRegGenCollection);
			setRequestAttribute("tipUffRegGen", "" + lOptionTipUffRegGen);
		}
		// Autorita' competente per territorio
		setRequestAttribute("autoritaCompTerritorio", "" + lOptionAutoritaCompTerritorio);

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");
		// Inserisci Misura Cautelare Cessate al momento del passaggio in giudicato computabili
		setRequestAttribute("flagComputabile", "S");

		// In fase di inserimento
		// 1)prevalorizzato per default con la sede dell’operatore, oppure
		// 2) valorizzato con lo stesso valore della “Sede del PM”
		// 3) Valorizzato dall’operatore tramite funzione “Ricerca comune”.
		setRequestAttribute(ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO, DecodificheManager
				.getInstance().getTipoUfficioPM());

		// return IWebConstants.ROOT_DIR +
		// "files/siap/siep/misuracautelare/LoadInserisciMisuraCautelare_new.jsp";

		// return PG_LOAD_INSERISCIMISURACAUTELARE; //restituisce la jsp di VIEW
		// return PG_LOAD_INSERISCIMISURACAUTELARECESSATE; //restituisce la jsp di VIEW

		if (llMisMod.getFlagComputabile().equalsIgnoreCase("S")) {
			return PG_LOAD_INSERISCIMISURACAUTELARECESSATECOMPUTABILI; // restituisce la jsp di VIEW
		} else {
			return PG_LOAD_INSERISCIMISURACAUTELARECESSATENONCOMPUTABILI; // restituisce la jsp di VIEW
		}
		// fine nuova gestione modifica
	}

}