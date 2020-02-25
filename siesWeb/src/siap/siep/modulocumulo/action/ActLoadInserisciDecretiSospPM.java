package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.modulocumulo.controller.IStatoEsecTitoloCumulato;
import siap.siep.modulocumulo.model.NotificaCumuloModel;
import siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel;
import siap.siep.modulocumulo.util.StatoEsecuzioneCumuloUtils;
import siap.siep.util.SIEPLookupRemote;

/**
 * Classe Action per il caricamento della form di Inserimento e Modifica dei decreti di sospensione disposti
 * dal PM
 *
 * @author
 *
 */
public class ActLoadInserisciDecretiSospPM extends ActionModuloCumulo
		implements ICostantiStatoEsecTitoloCumulato, ICostantiStatoEsecuzioneCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		// ==========================================================================
		// Preparo le combo
		// ==========================================================================
		Option lOptionAutorita = null;
		lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());

		String[] lFilter = new String[10];
		lFilter[0] = "-";
		lFilter[1] = "92"; // Carabinieri
		lFilter[2] = "93"; // Polizia di Stato
		lFilter[3] = "32"; // Guardia di Finanza
		lFilter[4] = "79"; // Polizia Penitenziaria
		lFilter[5] = "94"; // Polizia Municipale

		lFilter[6] = "20"; // Questura
		lFilter[7] = "19"; // Commissariato di P.S.
		lFilter[8] = "28"; // Carabinieri - Comando Stazione
		lFilter[9] = "58"; // Carabinieri - Nucleo Operativo

		lOptionAutorita.setFilter(lFilter);

		// Combo Uffici di sorveglianza per comma 5
		Option lComboUfficiSorv = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lComboUfficiSorv.setFilter(new String[] { "TDS", "TDSM", "UDS", "UDSM" });

		Option lComboUfficiUDS = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lComboUfficiUDS.setFilter(new String[] { "UDS", "UDSM" });

		Option lComboUfficiTDS = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lComboUfficiTDS.setFilter(new String[] { "TDS", "TDSM" });

		// Combo tipo provvedimento per il Comma 5
		Option lComboTipoProvvC5 = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
    lComboTipoProvvC5.setFilter(new String[]{"0061","0104","0063","0117","5506","5507","5508","5511","5512","5510","5513"});

		// Combo Istanza Pervenuta/Depositata
		Vector<DecodificheModel> lIstanzaDepPer = new Vector<>();
		lIstanzaDepPer.add(new DecodificheModel("-", "-", "-", "", "", "", "", "", ""));
		lIstanzaDepPer.add(new DecodificheModel("P", "Pervenuta", "", "", "", "", "", "", ""));
		lIstanzaDepPer.add(new DecodificheModel("D", "Depositata", "", "", "", "", "", "", ""));
		Option lComboIstPerDep = new Option(lIstanzaDepPer);

		// Combo Contenuto Istanza
		Option lComboContenutoIstanza = new Option(
				DecodificheManager.getInstance().getTipoContenutoIstanza());
		lComboContenutoIstanza.setFilter(new String[] { "-", "C001" });

		// Combo tipo provvedimento Revoca il Comma 5
		Option lComboTipoProvvC5Rev = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
    lComboTipoProvvC5Rev.setFilter(new String[]{"-","0078","0079","0080","5514","5515","5516","5519","5520","5518","5521"}); 

		// Combo Motivo Revoca Comma 5
		Option lComboMotivoRevC5 = new Option(DecodificheManager.getInstance().getRevocaDecSosp());

		// Combo Motivo Revoca PM Comma 5
		Option lComboMotivoRevPMC5 = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		lComboMotivoRevPMC5.setFilter(new String[] { "-", "9003", "9004", "9005" });

		// Combo Motivo Revoca TDS Comma 5
		Option lComboMotivoRevTDSC5 = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		lComboMotivoRevTDSC5.setFilter(new String[] { "-", "9000", "9001", "9002" });

		// Combo tipo provvedimento per il DL 78
		Option lComboTipoProvv78 = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
    lComboTipoProvv78.setFilter(new String[]{"1022","1023","5522","5523","5524","1024"});
		// Combo tipo provvedimento Concessione per il 199
		Option lComboTipoProvv199Conc = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
    lComboTipoProvv199Conc.setFilter(new String[]{"0499","0364","5527","5528","5504","0365","0498"});
		// Combo tipo provvedimento Sorveglianza per il 199
		Option lComboTipoProvv199Sorv = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		lComboTipoProvv199Sorv.setFilter(new String[] { "9000", "9001", "9002" });
		// Combo tipo provvedimento Revoca il 199
		Option lComboTipoProvv199Rev = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
    lComboTipoProvv199Rev.setFilter(new String[]{"0495","0496","0497","5530","5531","5532"});

		// ==========================================================================
		//
		// ==========================================================================
		String lModalita = "I"; // default inserimento
		if (!isRequestParameterNullObj("modalita"))
			lModalita = getRequestStringParameter("modalita");

		if ("I".equals(lModalita)) {
			// Inserimento

			// 23/07/2018 Descrizione Motivo provvedimento per il Comma 5 concatenata con RV_ALT5
			Collection lTipoProvvedimento = DecodificheManager.getInstance().getMotivoProvvedimento();
			Collection DecodComboTipoProvvC5 = new Vector();
      String[] lComboTipoProvvC5Filter = {"0061","0104","0063","0117","5506","5507","5508","5511","5512","5510","5513"};
			Iterator itx = lTipoProvvedimento.iterator();
			// int ind = 0;
			while (itx.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) itx.next();
				if (Arrays.asList(lComboTipoProvvC5Filter).contains(lDecMod.getCode().trim())) {
					if (lDecMod.getCodiceAlt5() != null && lDecMod.getCodiceAlt5().length() > 0
							&& lDecMod.getDescription().indexOf(lDecMod.getCodiceAlt5()) < 0) {
						String lDescription = lDecMod.getDescription() + " " + lDecMod.getCodiceAlt5();
						lDecMod.setDescription(lDescription);
					}
					DecodComboTipoProvvC5.add(lDecMod);
				}
				// ind++;
			}
			lComboTipoProvvC5 = new Option(DecodComboTipoProvvC5);
		} else if ("M".equals(lModalita)) {
			// Modifica. Si modifica un provvedimento per volta
			BigDecimal lIdStatoEsecuzione = getRequestBigDecimalParameter(
					CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO);

			IStatoEsecTitoloCumulato lCtrlStato = SIEPLookupRemote.getStatoEsecTitoloCumulatoRemote();
			// StatoEsecTitoloCumulatoModel lStatoModel = lCtrlStato.ExRicercaStatoEsecTitoloCumulatoById
			// (lIdStatoEsecuzione);

			StatoEsecTitoloCumulatoModel lStatoModel = lCtrlStato
					.ExRicercaStatoEsecTitoloCumulatoByIdFull(lIdStatoEsecuzione);

			siesLogger.debug("lStatoModel = " + lStatoModel);

			if (StatoEsecuzioneCumuloUtils.isSospensioneC5Provv(lStatoModel.getCodMotivo())) {
				setRequestAttribute("aProvvedimentoSosp", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				if (lStatoModel.getCodMotivo() != null)
					lComboTipoProvvC5.setSelected(lStatoModel.getCodMotivo());
			} else if (StatoEsecuzioneCumuloUtils.isSospensioneC5VVR(lStatoModel.getCodMotivo())) {
				setRequestAttribute("aVerbaleVaneRicerche", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				if (lStatoModel.getCodAutoritaEmittente() != null)
					lOptionAutorita.setSelected(lStatoModel.getCodAutoritaEmittente());
			} else if (StatoEsecuzioneCumuloUtils.isSospensioneC5DecIrr(lStatoModel.getCodMotivo())) {
				siesLogger.debug("Modifica Decreto Irreperibilità");
				setRequestAttribute("aDecretoIrreperibilita", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());
			} else if (StatoEsecuzioneCumuloUtils.isSospensioneC5Istanza(lStatoModel.getCodMotivo())) {
				setRequestAttribute("aIstanzaSimeone", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				if (lStatoModel.getFlagIstanzaPresdep() != null)
					lComboIstPerDep.setSelected(lStatoModel.getFlagIstanzaPresdep());
				if (lStatoModel.getCodContenutoIstanza() != null)
					lComboContenutoIstanza.setSelected(lStatoModel.getCodContenutoIstanza());
				if (lStatoModel.getCodTipoUfficioDestinatario() != null)
					lComboUfficiSorv.setSelected(lStatoModel.getCodTipoUfficioDestinatario());
			} else if (StatoEsecuzioneCumuloUtils.isSospC5Revoca(lStatoModel.getCodMotivo())) {
				setRequestAttribute("aProvvRevoca", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				if (lStatoModel.getCodMotivo() != null)
					lComboTipoProvvC5Rev.setSelected(lStatoModel.getCodMotivo());
				if (lStatoModel.getCodMotivoRevoca() != null)
					lComboMotivoRevC5.setSelected(lStatoModel.getCodMotivoRevoca());
				if ("0001".equals(lStatoModel.getCodMotivoRevoca()))
					lComboMotivoRevPMC5.setSelected(lStatoModel.getCodMotivoRevocaPm());
			} else if (StatoEsecuzioneCumuloUtils.isSospC5RevocaSorv(lStatoModel.getCodMotivo(),
					lStatoModel.getFlagTipoSosp())) {
				siesLogger.debug("Provv Revoca Sorv");
				setRequestAttribute("aOrdinanzaRevocaC5", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_C5_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				UfficioModel uffSORV = getUfficioByCodUfficio(lStatoModel.getCodUfficioEmittente());
				lComboUfficiTDS.setSelected(uffSORV.getCodTipoUfficio());

				lComboMotivoRevTDSC5.setSelected(lStatoModel.getCodMotivo());
			} else if (StatoEsecuzioneCumuloUtils.isSospensionePM78(lStatoModel.getCodMotivo())) {
				setRequestAttribute("aProvvedimentoSosp", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_78);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_78_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				if (lStatoModel.getListaNotifiche() != null) {
					Iterator<NotificaCumuloModel> iterNotifiche = lStatoModel.getListaNotifiche().iterator();
					while (iterNotifiche.hasNext()) {
						NotificaCumuloModel lNotifica = iterNotifiche.next();

						try {
							if (lNotifica.getUffCodUfficio() != null) {
								lNotifica.setUfficio(getUfficioByCodUfficio(lNotifica.getUffCodUfficio()));

								// Preseleziono le combo UDS
								lComboUfficiUDS.setSelected(lNotifica.getUfficio().getCodTipoUfficio());
							}
						} catch (Exception e) {
						}
					}
				}

				lComboTipoProvv78.setSelected(lStatoModel.getCodMotivo());

			} else if (StatoEsecuzioneCumuloUtils.isSospensionePM199_Conc(lStatoModel.getCodMotivo())) {
				setRequestAttribute("aProvvedimentoSosp", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				lComboTipoProvv199Conc.setSelected(lStatoModel.getCodMotivo());
			} else if (StatoEsecuzioneCumuloUtils.isSospensionePM199_Rev(lStatoModel.getCodMotivo())) {
				setRequestAttribute("aProvvedimentoRev", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				lComboTipoProvv199Rev.setSelected(lStatoModel.getCodMotivo());
			} else if (StatoEsecuzioneCumuloUtils.isSospensionePM199_Sorv(lStatoModel.getCodMotivo(),
					lStatoModel.getFlagTipoSosp())) {
				setRequestAttribute("aProvvedimentoSorv", lStatoModel);
				setRequestAttribute("TipoSosp", ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199);
				setRequestAttribute("DescTipoSosp",
						ICostantiStatoEsecTitoloCumulato.ATTIVITA_PM_SOSP_199_DESC);
				setRequestAttribute("idProvvMod", "" + lStatoModel.getIdStatoEsecTitoloCumulato());
				setRequestAttribute("flagStato", "" + lStatoModel.getFlagStato());

				UfficioModel uffSORV = getUfficioByCodUfficio(lStatoModel.getCodUfficioEmittente());
				lComboUfficiSorv.setSelected(uffSORV.getCodTipoUfficio());

				lComboTipoProvv199Sorv.setSelected(lStatoModel.getCodMotivo());
			}
		} else {
			// Rilanciare Eccezione - Operazione non supportata
		}

		// ==========================================================================
		// Passo le combo i dati delle combo
		// ==========================================================================
		// Tipo Autorità per il Verbale Vane Ricerche
		setRequestAttribute("tipoAutorita", "" + lOptionAutorita);

		// Combo Uffici di sorveglianza per comma 5
		setRequestAttribute("comboUfficiSorv", "" + lComboUfficiSorv);

		setRequestAttribute("comboUfficiUDS", "" + lComboUfficiUDS);

		setRequestAttribute("comboUfficiTDS", "" + lComboUfficiTDS);

		// Combo tipo provvedimento per il Comma 5
		setRequestAttribute("comboTipoProvvC5", "" + lComboTipoProvvC5);
		setRequestAttribute("comboIstanzaPerDep", "" + lComboIstPerDep);
		setRequestAttribute("comboContenutoIstanza", "" + lComboContenutoIstanza);
		setRequestAttribute("comboTipoProvvC5Rev", "" + lComboTipoProvvC5Rev);
		setRequestAttribute("comboMotivoRevC5", "" + lComboMotivoRevC5);
		setRequestAttribute("comboMotivoRevPMC5", "" + lComboMotivoRevPMC5);
		setRequestAttribute("comboMotivoRevTDSC5", "" + lComboMotivoRevTDSC5);

		// Combo tipo provvedimento per il DL 78
		setRequestAttribute("comboTipoProvv78", "" + lComboTipoProvv78);

		// Combo tipo provvedimento per il 199
		setRequestAttribute("comboTipoProvv199Conc", "" + lComboTipoProvv199Conc);
		setRequestAttribute("comboTipoProvv199Rev", "" + lComboTipoProvv199Rev);
		setRequestAttribute("comboTipoProvv199Sorv", "" + lComboTipoProvv199Sorv);

		setRequestAttribute("modalita", lModalita);

		return PG_LOAD_INSERISCI_SOSP_PM;
	}

}