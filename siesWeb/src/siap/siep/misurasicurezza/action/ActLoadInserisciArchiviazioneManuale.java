package siap.siep.misurasicurezza.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
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
 * Title: ActLoadInserisciArchiviazioneManuale
 * </p>
 * <p>
 * Description: Load dell Inserimento del Provvedimento
 * </p>
 * <p>
 * ( tipo provv = Annotazione (25) )
 * </p>
 * <p>
 * Archiviazione manuale (appl. MIS. SIC.)
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AMBROS
 *
 */
public class ActLoadInserisciArchiviazioneManuale extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

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

		// Ricerca pena residua per quel fascicolo

		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// if(notEsistePenaResiduaCorrenteByFascicoloSiep(lPenaResMod))
		// return IWebConstants.PG_MESSAGE;

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

			Option lOptionAutEst = new Option(DecodificheManager.getInstance().getTipoAutorita());
			String[] lFiltroAutEst = { "22", "C0" };
			lOptionAutEst.setFilter(lFiltroAutEst);
			setRequestAttribute("autoritaEsterna", "" + lOptionAutEst);
		} catch (SIEPException e) {
			// d.f.01/12/2014 su richiesta m.t. gli avvocati non sono obbligatori
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

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Riempimento ComboBox Autorità
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		// Magistrato
		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		// OGGETTO DEFINIZIONE --> MEV_39: aggiunte 3 definizioni (1150, 1151, 1152) (order by rv_meaning)
		Option lOptionT = new Option(DecodificheManager.getInstance().getMotivoProvvedimento());
		lOptionT.setFilter(new String[] { "1151", "0400", "0613", "0614", "1152", "2804", "1150", "0612" });

		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO)) {
			String lCodMotivoPreselezionato = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
			lOptionT.setSelected(lCodMotivoPreselezionato);
		}
		setRequestAttribute("tipoArchiviazioni", "" + lOptionT);

		List lListMis = new ArrayList();
		MisuraSicurezzaModel MisMod = null;

		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		try {
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
			if (lListMis != null && lListMis.size() > 0) {
				if (lListMis.size() == 1)
					MisMod = (MisuraSicurezzaModel) lListMis.get(0);
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

		// 24/11/2014
		// Aggiunto Desinatatio Sorveglianza
		Option lOptionSor = new Option(DecodificheManager.getInstance().getTipoUfficio());
		lOptionSor.setFilter(new String[] { "-", "TDS", "UDS", "UDSM" });
		setRequestAttribute("tipoUDS", "" + lOptionSor);

		return PG_LOAD_INS_ARCHIVIAZIONE_MANUALE; // restituisce la jsp di VIEW
	}

}