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
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
//import per le combo
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciMisuraCautelare
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di MisuraCautelare
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
public class ActLoadInserisciMisuraCautelareCessComp extends ActionSiap implements ICostantiMisuraCautelare {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Se non c'e' il fascicolo in sessione presenta la maschera di ricerca fascicolo
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		// ==========================================================================
		// Verifico se è stato emesso un 'Provvedimento', in questo caso non posso
		// inscrivere misure cautelari, ma devo emettere un provvedimento di computo
		// ==========================================================================
		IEventoSimeone lCtrlEve = SICOLookupRemote.getEventoSimeoneRemote();
		String[] lTipoprov = { "04", "06", "09", "10", "12" };
		Vector lEventi = lCtrlEve.ExRicercaEventoByFascicoloSiepTipProv(lIdFascicolo, lTipoprov);
		if (lEventi != null && lEventi.size() > 0) {
			EventoModel lEveMod = (EventoModel) lEventi.firstElement();
			if (lEveMod != null && lEveMod.getCodMotivo() != null && !lEveMod.getCodMotivo().equals("0078") // di
																											// revoca
																											// di
																											// Decreto
																											// di
																											// Sospensione
																											// art.
																											// 656
																											// c.8
																											// c.p.p.
																											// -
																											// Libero
					&& !lEveMod.getCodMotivo().equals("0079") // di revoca di Decreto di Sospensione art. 656
																// c.8 c.p.p. - Detenuto Altra Causa
					&& !lEveMod.getCodMotivo().equals("0080") // di revoca di Decreto di Sospensione art. 656
																// c.8 c.c.p. - Arresti Domiciliari
					&& !lEveMod.getCodMotivo().equals("5407") // 29/10/2010 Nomina difensore d'ufficio.
			) {
				RedirectTo lRedirigi = new RedirectTo();
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Attenzione: OE già emesso; se si vuole computare una misura "
								+ "cautelare procedere con l'emissione di un decreto di computo.");
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lIdFascicolo);
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
		}

		// ==========================================================================
		// Verifico se già presente una MC in corso di espiazione (data fine a null),
		// in questo caso non visualizzo nella form la relativa sezione di inserimento
		// ==========================================================================
		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		MisuraCautelareModel lMisMod = lCtrl.ExRicercaMisuraCautelareSenzaDataFineByIdFascicolo(lIdFascicolo);

		if (lMisMod != null)
			setRequestAttribute("primaSezione", "S");
		else
			setRequestAttribute("primaSezione", "N");

		// ==========================================================================
		// Recupero i dati per caricare la combo
		// - Tipo Misura
		// - Motivo non Computabilità
		// - Tipo Ufficio
		// ==========================================================================

		Option lOptionComp = new Option(DecodificheManager.getInstance().getMotivoNonComputabile());

		// popolato con misure detentive e non detentive
		Option lOption = new Option(DecodificheManager.getInstance().getTipoMisuraCautelare());
		// popolato con misure detentive
		Option lOptionDetentive = new Option(DecodificheManager.getInstance()
				.getTipoMisuraCautelareDetentive());
		// popolato con misure e non detentive
		Option lOptionNonDetentive = new Option(DecodificheManager.getInstance()
				.getTipoMisuraCautelareNonDetentive());
		// Autorita' competente per territorio
		Option lOptionAutoritaCompTerritorio = new Option(DecodificheManager.getInstance()
				.getAutoritaCompetentePerTerritorio());

		Option lOptionUff = new Option(DecodificheManager.getInstance().getTipoUfficio());

		// Recupero i dati per caricare la combo per un determinato "tipo ufficio"
		UtenteModel utenteConnesso = getUtenteConnesso();
		String codTipoUfficio = "";
		String sedeTipoUfficio = "";
		// String descComune = "";
		if (getUtenteConnesso().getUfficioUtente() != null
				&& getUtenteConnesso().getUfficioUtente().getCodTipoUfficio() != null) {
			codTipoUfficio = utenteConnesso.getUfficioUtente().getCodTipoUfficio();
			sedeTipoUfficio = utenteConnesso.getUfficioUtente().getDescrComune();
			// descComune = utenteConnesso.getUfficioUtente().getDescrComune();
		}
		IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
//		UfficioModel lUfficioModel = null;
		/*lUfficioModel = */lCtrlUfficio.getUfficioByCodTipoUffDescrComune(codTipoUfficio, sedeTipoUfficio);
		Option lOptionUffPM = new Option();
		// if(lUfficioModel.getCodTipoUfficio().equalsIgnoreCase("PM"))
		lOptionUffPM = new Option(DecodificheManager.getInstance().getTipoUfficioPM());

		// UtenteModel utenteConnesso= getUtenteConnesso();
		// String codTipoUfficio = utenteConnesso.getUfficioUtente().getCodTipoUfficio();
		// String sedeTipoUfficio = utenteConnesso.getUfficioUtente().getDescrComune();
		// IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
		// UfficioModel lUfficioModel = null;
		// lUfficioModel = lCtrlUfficio.getUfficioByCodTipoUffDescrComune(codTipoUfficio, sedeTipoUfficio);
		// Collection lOptionPerUfficio = new Vector();
		// DecodificheModel lDec = new DecodificheModel();
		// lDec.setCode(lUfficioModel.getCodTipoUfficio());
		// lDec.setDescription(lUfficioModel.getDescrTipoUfficio());
		// lDec.setContesto(lUfficioModel.getCodTipoUfficio());
		// lOptionPerUfficio.add(lDec);
		// Option lOptionUff= new Option(lOptionPerUfficio);

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
		lOptionTipUffRegGen = new Option(tipUffRegGenCollection);

		// Autorità emittente
		Option lOptionAutoritaEmittenteUff = new Option(DecodificheManager.getInstance()
				.getTipoAutoritaEmittente());

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
		setRequestAttribute("tipUff", "" + lOptionUff);
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
		if (codTipoUfficio.equalsIgnoreCase("PM") || codTipoUfficio.equalsIgnoreCase("PGCAP")) {
			lOptionAutoritaEmittenteUff.setSelected("GIP");
		} else if (codTipoUfficio.equalsIgnoreCase("PMM")) {
			lOptionAutoritaEmittenteUff.setSelected("GIPM");
		}
		setRequestAttribute("tipUffAutEmi", "" + lOptionAutoritaEmittenteUff);
		// default luogo Autorità Emittente = comune ufficio dell'utente
		setRequestAttribute("defaultLuogoAutoritaEmittente", sedeTipoUfficio);
		// default Sede Tipo Ufficio PM = sede ufficio dell'utente
		setRequestAttribute("defaultSedeTipoUfficioPM", sedeTipoUfficio);
		// Recupero i dati per caricare la combo per un determinato "tipo ufficio Reg Gen"
		setRequestAttribute("tipUffRegGen", "" + lOptionTipUffRegGen);
		// Autorita' competente per territorio
		setRequestAttribute("autoritaCompTerritorio", "" + lOptionAutoritaCompTerritorio);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
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
		return PG_LOAD_INSERISCIMISURACAUTELARECESSATECOMPUTABILI; // restituisce la jsp di VIEW
	}

}