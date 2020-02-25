package siap.siep.misuracautelare.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;
//import per le combo

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
public class ActLoadInserisciMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare {

	private String griglia() throws F3BException {

		setRequestAttribute("strFunzione", "Misure Cautelari");

		return PG_GRIGLIA_MISURE_CAUTELARI;
	}

	public String processRequest() throws F3BException {

		// Se non c'e' il fascicolo in sessione presenta la maschera di ricerca fascicolo
		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// UN GENIO!!! il codice dopo il return è piuttosto inutile!!!
		// if (true) NON SI PUO' VEDERE!!!
//		if (true) {
		return griglia();
//		}

//		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
//				.getIdFascicoloSiep();

//		if (isFascicoloArchiviatoDefinito())
//			return IWebConstants.PG_MESSAGE;

		// ==========================================================================
		// Verifico se è stato emesso un 'Provvedimento', in questo caso non posso
		// inscrivere misure cautelari, ma devo emettere un provvedimento di computo
		// ==========================================================================
//		IEventoSimeone lCtrlEve = SICOLookupRemote.getEventoSimeoneRemote();
//		String[] lTipoprov = { "04", "06", "09", "10", "12" };
//		Vector lEventi = lCtrlEve.ExRicercaEventoByFascicoloSiepTipProv(lIdFascicolo, lTipoprov);
//		if (lEventi != null && lEventi.size() > 0) {
//			EventoModel lEveMod = (EventoModel) lEventi.firstElement();
//			if (lEveMod != null && lEveMod.getCodMotivo() != null && !lEveMod.getCodMotivo().equals("0078") // di
//																											// revoca
//																											// di
//																											// Decreto
//																											// di
//																											// Sospensione
//																											// art.
//																											// 656
//																											// c.8
//																											// c.p.p.
//																											// -
//																											// Libero
//					&& !lEveMod.getCodMotivo().equals("0079") // di revoca di Decreto di Sospensione art. 656
//																// c.8 c.p.p. - Detenuto Altra Causa
//					&& !lEveMod.getCodMotivo().equals("0080") // di revoca di Decreto di Sospensione art. 656
//																// c.8 c.c.p. - Arresti Domiciliari
//					&& !lEveMod.getCodMotivo().equals("5407") // 29/10/2010 Nomina difensore d'ufficio.
//			) {
//				RedirectTo lRedirigi = new RedirectTo();
//				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
//						"Attenzione: OE già emesso; se si vuole computare una misura "
//								+ "cautelare procedere con l'emissione di un decreto di computo.");
//				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
//						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lIdFascicolo);
//				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
//
//				return IWebConstants.PG_MESSAGE;
//			}
//		}
//
//		// ==========================================================================
//		// Verifico se già presente una MC in corso di espiazione (data fine a null),
//		// in questo caso non visualizzo nella form la relativa sezione di inserimento
//		// ==========================================================================
//		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
//		MisuraCautelareModel lMisMod = lCtrl.ExRicercaMisuraCautelareSenzaDataFineByIdFascicolo(lIdFascicolo);
//
//		if (lMisMod != null)
//			setRequestAttribute("primaSezione", "S");
//		else
//			setRequestAttribute("primaSezione", "N");
//
//		// ==========================================================================
//		// Recupero i dati per caricare la combo
//		// - Tipo Misura
//		// - Motivo non Computabilità
//		// - Tipo Ufficio
//		// ==========================================================================
//		// Option lOptionComp = new Option( DecodificheManager.getInstance().getMotivoNonComputabile());
//		// Option lOption = new Option( DecodificheManager.getInstance().getTipoMisuraCautelare());
//		// Option lOptionUff = new Option( DecodificheManager.getInstance().getTipoUfficio());
//
//		// Festa Carlo Modifiche per integrazione Bdmc
//		Vector lVectProvv = new Vector();
//		String flagConnessioneBdmc = new String("S");
//		try {
//			// FascicoloSiepModel lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");
//			SbPrenModel lSbPMod = new SbPrenModel();
//			// lSbPMod.setCognSogg(lFascMod.getSoggetto().getCognome());
//			// lSbPMod.setNomeSogg(lFascMod.getSoggetto().getNome());
//			lSbPMod.setCodiUffiSies(getCodUfficioUtenteConnesso());
//			lSbPMod.setUtenSies(getCodUtenteConnesso());
//			ISbPren lCtrlSbPren = BDMCLookupRemote.getSbPrenRemote();
//			lSbPMod.setCodiStatPren(new BigDecimal(0));
//			ISbViewProcpena lProcPenaCtrl = BDMCLookupRemote.getSbViewProcpenaRemote();
//			Vector lVect = lCtrlSbPren.ExRicercaSbPren(lSbPMod);
//			Vector lVectPeripren = new Vector();
//
//			Vector lVectSbViewProcPena = new Vector();
//			if (lVect != null) {
//				for (int i = 0; i < lVect.size(); i++) {
//					ISbPeripren lCtrlPeripren = BDMCLookupRemote.getSbPeriprenRemote();
//					SbPeriprenModel aSbPeripren = new SbPeriprenModel();
//					SbPrenModel lSbPModAppo = (SbPrenModel) lVect.get(i);
//					SbViewProcpenaModel lSbProcPena = new SbViewProcpenaModel();
//					lSbProcPena.setIdPren(lSbPModAppo.getIdPren());
//					// lSbProcPena.setAnnoRegiPmpm(lFascMod.getSentenza().getAnnoRegePm());
//					// lSbProcPena.setNumeRegiPmpm(new BigDecimal(lFascMod.getSentenza().getNumeroRegePm()));
//					lVectSbViewProcPena = lProcPenaCtrl.ExRicercaSbViewProcpena(lSbProcPena);
//					if (lVectSbViewProcPena != null && lVectSbViewProcPena.size() != 0) {
//						aSbPeripren.setIdPren(lSbPModAppo.getIdPren());
//						// aSbPeripren.setCodStatPrenPeri("0");
//						lVectPeripren = lCtrlPeripren.ExRicercaSbPeripren(aSbPeripren);
//
//						// ========================================================================
//						// = Routine di bonifica dei periodi già iscritti ma non ancora
//						// = segnalati a Bdmc
//						// = Powere by Festa Carlo
//						// =========================================================================
//						Vector lVectPeriprenScope = new Vector();
//						MisuraCautelareBdmcModel lModBdmc = new MisuraCautelareBdmcModel();
//						lModBdmc.setIdPren(lSbPModAppo.getIdPren());
//						lModBdmc.setFlagComputabile("0");
//						// lModBdmc.setFlagStato("I");
//						lModBdmc.setStatoTrasmissioneIsc("N");
//						IMisuraCautelareBdmc lCtrlBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
//						Vector misCautBdmc = lCtrlBdmc.ExRicercaMisuraCautelareBdmc(lModBdmc);
//						if (misCautBdmc != null && misCautBdmc.size() != 0 && lVectPeripren != null
//								&& lVectPeripren.size() != 0) {
//							for (int ii = 0; ii < lVectPeripren.size(); ii++) {
//								SbPeriprenModel aSbPeriprenIsc = (SbPeriprenModel) lVectPeripren.get(ii);
//								for (int kk = 0; kk < misCautBdmc.size(); kk++) {
//									MisuraCautelareBdmcModel lModBdmcIsc = (MisuraCautelareBdmcModel) misCautBdmc
//											.get(kk);
//									if (aSbPeriprenIsc.getProgPeriPres()
//											.equals(lModBdmcIsc.getProgPeriPres())) {
//										aSbPeriprenIsc.setCodStatPrenPeri("9");
//										break;
//									}
//								} // fine for misCautBdmc
//								lVectPeriprenScope.add(aSbPeriprenIsc);
//							} // fine for lVectPeriPren
//
//						} // fine "if (misCautBdmc != null && misCautBdmc.size() != 0)"
//						else {
//							lVectPeriprenScope = lVectPeripren;
//						}
//						// = fine Routine di bonifica dei periodi già iscritti ma non ancora
//						// ==============================================================================
//
//						ProvvedimentoModelBDMC lProvvBdmc = new ProvvedimentoModelBDMC();
//						lProvvBdmc.setSbPren(lSbPModAppo);
//						lProvvBdmc.setSbPeriPren(lVectPeriprenScope);
//						lProvvBdmc.setSbViewProcpena(lVectSbViewProcPena);
//						lVectProvv.add(lProvvBdmc);
//					}
//				}
//			}
//		} catch (F3BException ex) {
//			flagConnessioneBdmc = "N";
//		}
//		setRequestAttribute("lProvv", lVectProvv);
//		setRequestAttribute("flagConnessioneBdmc", flagConnessioneBdmc);
//		setSessionAttribute("lProvvSession", lVectProvv);
//		// setRequestAttribute("tipoMisura", "" + lOption );
//		// setRequestAttribute("motComp", "" + lOptionComp );
//		// setRequestAttribute("tipUff", "" + lOptionUff );
//
//		// Imposta Modalità.
//		setRequestAttribute("modalita", "I");
//
//		// return PG_LOAD_INSERISCIMISURACAUTELARE; //restituisce la jsp di VIEW
//		return PG_LOAD_INSERISCIMISURACAUTELARECESSATE; // restituisce la jsp di VIEW
	}
}