package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciOrdineEsecuzioneSanSos extends ActionSiap implements ICostantiOrdineEsecuzione {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// ----Grande grandissima IDEA!!! if
		// (isRequestParameterNullObj(ICostantiFascicoloSiep.FASCICOLO_RICERCATO))
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire un ordine d'esecuzione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;

		try {
			lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage()
					+ " Impossibile eseguire l'Ordine di Esecuzione. Inserire almeno un avvocato.");
			// commento perchè quando torna va in errore avendo perso l'evento corrente Paolo Cherubini
			// lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&" +
			// ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			// setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// !!!! POSIZIONI GIURIDICHE ATTUALMENTE GESTITE IN QUESTA FUNZIONE !!!!
		if (lPos != null && lPos.getPosizioneGiuridica() != null
				&& (!lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") // Libero
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") // Libero
						// 20191002 [SG]: intervento post collaudo 11.3 --> aggiunto caso "01"
						// Custodia Cautelare per Questa Causa in Regime di Detenzione
						&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("01")
				/*
				 * && !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("03") // Espiazione Pena
				 * in Regime Carcerario &&
				 * !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("15") // Espiazione Pena
				 * Sostitutiva (Liberta' Controllata) &&
				 * !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("19") // Espiazione Pena
				 * Sostitutiva (Semidetenzione)
				 */
				)) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Posizione Giuridica non gestita, impossibile procedere!");

			/*
			 * lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&" +
			 * ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			 * setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			 */
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPos);

		// Ricerca l'ultima pena residua per quel fascicolo
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = lPenResCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null || (lPos.getPosizioneGiuridica().getCodPosizioneGiuridica() != null
				&& !lPos.getPosizioneGiuridica().isLibero()
				&& !lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("-") && lPenaResMod != null
				&& lPenaResMod.getDataInizio() == null)) {
			// throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il calcolo della pena.
			// Impossibile eseguire l'ordine di esecuzione.");

			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			if (lPenaResMod == null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			} else {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare incoerente con Posizione Giuridica. Eseguire Calcolo della pena?");
			}

			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		/******************************************************************************************/
		// L'Annotazione manuale corrisponde alla pena convertita, ovvero alla nuova pena residua
		// che viene ricalcolata durante l'inserimento dell'OE
		BigDecimal lIdEvePenaConvertita = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO, "" + lIdEvePenaConvertita);

		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = lCtrlAnn
				.ExRicercaAnnotazioniManualiByIdEvento(lIdEvePenaConvertita);

		if (lAnnMod != null) {
			PenaResiduaModel lPenaSanSosConvertita = new PenaResiduaModel();

			lPenaSanSosConvertita.setQuantumReclusione(lAnnMod.getQuantumReclusione());
			lPenaSanSosConvertita.setQuantumArresto(lAnnMod.getQuantumArresto());
			lPenaSanSosConvertita.setImportoMulta(lAnnMod.getImportoMulta());
			lPenaSanSosConvertita.setImportoAmmenda(lAnnMod.getImportoAmmenda());

			lPenaSanSosConvertita.setFlagValidato("S"); // solo per problemi di visualizzazione

			lPenaResMod = lPenaSanSosConvertita;
		}
		/******************************************************************************************/

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// modifica relativa al tipo istituto
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
			// 20191002 [SG]: intervento post collaudo 11.3 --> aggiunto controllo preventivo
			if (lPos.getAltraCausa() != null) {
				// modifica relativa al tipo istituto
				if (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")) {
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
				} else {
					if (lPos.getAltraCausa().getIstitutoDetenzione() != null)
						lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
								lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
				}
			}
		} else {
			// modifica relativa al tipo istituto
			if (lPos.getPosizioneGiuridica().isLibero()
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}

		setRequestAttribute("autoritaEsternaE", "" + lOption);

		/*
		 * if(lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("07") ||
		 * lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("10") ||
		 * lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")) { lOption = new
		 * Option(DecodificheManager.getInstance().getTipoAutorita(),"35"); } else {
		 * if(lFascMod.getFlagAltraCausa()!= null && lFascMod.getFlagAltraCausa().equals("S")) { lOption = new
		 * Option(DecodificheManager.getInstance().getTipoAutorita(),lPos.getAltraCausa().getCodTipoIstituto()
		 * ); } else { lOption = new
		 * Option(DecodificheManager.getInstance().getTipoAutorita(),lPos.getLuogoDetenzione().
		 * getCodTipoIstituto()); } }
		 *
		 * setRequestAttribute("autoritaEsternaE", "" + lOption);
		 */

		// fine modifica relativa al tipo istituto

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);

		/*
		 * IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote(); Vector lAvvocati =
		 * lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		 */

		setRequestAttribute("avvocati", lAvvocati);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("evento", lEve);

		// IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		// PenaResiduaModel llPenMod =
		// lCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		lDataInizioPena = lPenaResMod.getDataInizio();
		// lDataFinePenaM = lPenaResMod.getDataFine();
		lDataFinePenaA = lPenaResMod.getDataFinePresunta();

		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyyy"));
		setRequestAttribute("penaresidua", lPenaResMod);

		return PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_SAN_SOS;
	}

}