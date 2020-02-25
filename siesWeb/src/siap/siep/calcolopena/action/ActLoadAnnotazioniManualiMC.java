package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadAnnotazioniManualiMC</p>
 * <p>Description: Azione Load della form di inserimento Computo Presofferto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.sbperipren.controller.ISbPeripren;
import siap.bdmc.sbperipren.model.SbPeriprenModel;
import siap.bdmc.sbpren.controller.ISbPren;
import siap.bdmc.sbpren.model.ProvvedimentoModelBDMC;
import siap.bdmc.sbpren.model.SbPrenModel;
import siap.bdmc.sbviewprocpena.controller.ISbViewProcpena;
import siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadAnnotazioniManualiMC extends ActLoadInserisciAnnotazioniManuali {

	/**
	 * Rideterminazione Pena --> Computo misura cautelare stesso reato (presofferto)
	 * 
	 * @return Nome della pagina JSP di inserimento dei dati del Presofferto
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		String lPageErr = loadRichiestaAnnotazioniManuali("0121"); // MC STESSO TITOLO

		if (lPageErr != null)
			return lPageErr;

		// Festa Carlo Modifiche per integrazione Bdmc
		Vector lVectProvv = new Vector();
		String flagConnessioneBdmc = new String("S");
		try {
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			SbPrenModel lSbPMod = new SbPrenModel();
			// lSbPMod.setCognSogg(lFascMod.getSoggetto().getCognome());
			// lSbPMod.setNomeSogg(lFascMod.getSoggetto().getNome());
			lSbPMod.setCodiUffiSies(this.getCodUfficioUtenteConnesso());
			lSbPMod.setUtenSies(this.getCodUtenteConnesso());
			lSbPMod.setCodiStatPren(new BigDecimal(0));
			ISbPren lCtrl = BDMCLookupRemote.getSbPrenRemote();
			ISbViewProcpena lProcPenaCtrl = BDMCLookupRemote.getSbViewProcpenaRemote();
			Vector lVect = lCtrl.ExRicercaSbPren(lSbPMod);
			Vector lVectPeripren = new Vector();

			Vector lVectSbViewProcPena = new Vector();
			if (lVect != null) {
				for (int i = 0; i < lVect.size(); i++) {
					ISbPeripren lCtrlPeripren = BDMCLookupRemote.getSbPeriprenRemote();
					SbPeriprenModel aSbPeripren = new SbPeriprenModel();
					SbPrenModel lSbPModAppo = (SbPrenModel) lVect.get(i);
					SbViewProcpenaModel lSbProcPena = new SbViewProcpenaModel();
					lSbProcPena.setIdPren(lSbPModAppo.getIdPren());
					lSbProcPena.setAnnoRegiPmpm(lFascMod.getSentenza().getAnnoRegePm());
					lSbProcPena.setNumeRegiPmpm(new BigDecimal(lFascMod.getSentenza().getNumeroRegePm()));
					lVectSbViewProcPena = lProcPenaCtrl.ExRicercaSbViewProcpena(lSbProcPena);

					if (lVectSbViewProcPena != null && lVectSbViewProcPena.size() != 0) {
						aSbPeripren.setIdPren(lSbPModAppo.getIdPren());
						aSbPeripren.setCodStatPrenPeri("0");
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
			// ???
			flagConnessioneBdmc = "N";
		}

		setRequestAttribute("lProvv", lVectProvv);
		setRequestAttribute("flagConnessioneBdmc", flagConnessioneBdmc);

		String lPage = IWebConstants.ROOT_DIR + "/files/siap/siep/calcolopena/LoadAnnotazioniManualiMC.jsp";

		return lPage;
	}

}