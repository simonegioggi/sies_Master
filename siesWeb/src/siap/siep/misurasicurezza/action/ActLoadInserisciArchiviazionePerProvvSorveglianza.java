package siap.siep.misurasicurezza.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
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
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 *
 * <p>
 * Title: ActLoadInserisciArchiviazionePerProvvSorveglianza
 * </p>
 * <p>
 * Description: Load dell Inserimento del Provvedimento
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * Archiviazione per Provvedimento della Sorveglianza (appl. MIS. SIC.)
 * </p>
 * <p>
 * Company: IS Italia 2014
 * </p>
 * 
 * @author AMBROSINO
 *
 */
@SuppressWarnings("rawtypes")
public class ActLoadInserisciArchiviazionePerProvvSorveglianza extends ActionSiap implements
		ICostantiMisuraSicurezza {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo") || this.isSessionAttributeNullObj("soggetto")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// SoggettoModel lSog=(SoggettoModel)getSessionAttribute("soggetto");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		this.setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

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

		isEventoNonValidato();

		// Controllo Esistenza pena residua non validata per quel fascicolo
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		}

		Date lDataInizioPena = null;
		if (lPenaResMod != null && lPenaResMod.getDataInizio() != null) {
			lDataInizioPena = lPenaResMod.getDataInizio();
			setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		}

		if (lPenaResMod != null)
			setRequestAttribute("penaresidua", lPenaResMod);

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			Vector lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvVect);
		} catch (SIEPException e) {
			// NON Rilancio l'eccezione: Deve passare anche in assenza del Difensore
		}

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Oggetti per il caricamento della combo Autorità Emittente
		Option lAutoritaSORV = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		lAutoritaSORV.setFilter(new String[] { "TDS", "UDS", "UDSM", "TDSM", "-" });
		setRequestAttribute("autorita", "" + lAutoritaSORV);

		// Oggetti per il caricamento delle combo Tipo Provvedimento
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		// Oggetti per il caricamento delle combo Tipo Definizione
		Option lOptionT = new Option(DecodificheManager.getInstance().getMotivoProvvedimento(), "-");
		lOptionT.setFilter(new String[] { "2760", "2761", "2762", "2763", "2764", "2765", "2766", "2767",
				"2768", "2769", "2770", "2772", "2773", "2670", "2777", "-" });
		setRequestAttribute("tipoArchiviazioni", "" + lOptionT);

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// Autorità esterna (UNEP)
		Option lOptionUnep = new Option(DecodificheManager.getInstance().getTipoAutorita());
		String[] lFiltroAutEst = { "22", "C0" };
		lOptionUnep.setFilter(lFiltroAutEst);
		setRequestAttribute("autoritaEsterna", "" + lOptionUnep);

		// Riempimento ComboBox Autorità
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		// Ricerca Misure sicurezza già presenti nel fascicolo

		List lListMis = new ArrayList();
		MisuraSicurezzaModel MisMod = null;

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		try {
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
			if (lListMis != null && lListMis.size() > 0) {
				MisMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);
			} else {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
								+ " Risulta privo di Misure di Sicurezza");
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
						+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}
		} catch (F3BException e) {

		}

		setRequestAttribute("listaMisure", lListMis);
		setRequestAttribute("MisuraModel", MisMod);

		// 12/03/2015
		// Aggiunto Desinatatio Sorveglianza
		Option lOptionSor = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionSor.setFilter(new String[] { "-", "TDS", "UDS", "UDSM" });
		setRequestAttribute("tipoUDS", "" + lOptionSor);

		return PG_LOAD_INS_ARCHIVIAZIONE_PROVV_SORVE; // restituisce la jsp di VIEW
	}

}