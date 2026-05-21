package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * ActLoadInserisciPassaggioClasse - Classe per il caricamento della definizione procedimento per passaggio di
 * classe
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
public class ActLoadInserisciPassaggioClasse extends ActionSiap implements ICostantiArchiviazione {

	public String processRequest() throws Exception {

		// Controllo Presenza del Fascicolo in Sessione
		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		isFascicoloSiepDiCompetenza();

		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
		// Aggiunto un controllo per annullare i procedimenti creati a fronte di una conversione di una
		// istanza. Se l'utente si accorge di aver sbagliato a fare la conversione e la annulla sul registro
		// istanza occorre archiviare il procedimento creato in classe I non ancora validato.
		// Quindi consento ai procedimenti non validati diversi da registro istanza di essere comunque
		// archiviati, ma solo per motivo "0353" archiviazione per fascicolo iscritto per errore
		int chiaveProgr = fsm.getChiaveProgr().intValue();
		// chiaveProgr > 90000 && chiaveProgr < 100000 è un registro istanza
		if (fsm.getFlagValidato().equalsIgnoreCase("N") && (chiaveProgr > 90000 && chiaveProgr < 100000)) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

			// pagina di ritorno
			return IWebConstants.PG_MESSAGE;
		}

		// Controllo Fascicolo definito
		if (fsm.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + fsm.getChiaveAnno() + "/"
					+ fsm.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			rt.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

			// pagina di ritorno
			return IWebConstants.PG_MESSAGE;
		}

		isEventoNonValidato();

		if (fsm.getFlagValidato().equalsIgnoreCase("S")) {
			/******************************* Posizione Giuridica **********************************/
			IPosizioneGiuridica ipg = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel pgldacm = ipg
					.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
							fsm.getIdFascicoloSiep());
			if (pgldacm == null || pgldacm.getPosizioneGiuridica() == null) {
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + fsm.getChiaveAnno()
						+ "/" + fsm.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
				rt.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
						+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("posizioneluogoaltra", pgldacm);

			/******************************* Pena Complessiva *****************************/
			IPenaComplessiva ipc = SIEPLookupRemote.getPenaComplessivaRemote();
			PenaComplessivaModel pcm = ipc.ExRicercaPenaComplessivaByIdFascicolo(idFascicolo);

			if (pcm == null)
				throw new SIEPException(SIEPException.USER_MESSAGE,
						"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

			String lFlagErgastolo = "N";
			// se la Pena Complessiva è un ergastolo motivoPC ergastolo con isolamento diurno
			if (pcm.getCodTipoPenaDetentiva() != null && pcm.getCodTipoPenaDetentiva() != "") {
				if (pcm.getCodTipoPenaDetentiva().equals("03"))
					lFlagErgastolo = "S";
				else if (pcm.getCodTipoPenaDetentiva().equals("04"))
					lFlagErgastolo = "D";
			}

			setRequestAttribute("flagergastolo", lFlagErgastolo);
			/******************************* Fine Pena Complessiva ************************/

			/*********************************** Pena Residua ***************************/
			IPenaResidua ipr = SIEPLookupRemote.getPenaResiduaRemote();
			PenaResiduaModel prm = ipr.ExRicercaPenaResiduaCorrenteByFascicoloSiep(idFascicolo);
			if (prm == null && fsm.getFlagValidato().equalsIgnoreCase("N")) {
				RedirectTo rt = new RedirectTo();
				rt.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
				rt.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
						+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);

				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("penaresidua", prm);
		}

		/******************************************************************************/
		IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imc
				.ExRicercaMagistratoCompetenteByFascicolo(fsm.getIdFascicoloSiep());
		if (mcmm != null)
			setRequestAttribute("magistratocompetente", mcmm);

		Option motivoPC = new Option(DecodificheManager.getInstance().getMotivoPC());
		setRequestAttribute("oggettodefinzione", "" + motivoPC);

		// Altra Autorità
		Option tipoAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + tipoAutorita);

		// Ufficio recupero crediti
		Option tipoUfficio = new Option(DecodificheManager.getInstance().getTipoUfficio());
		tipoUfficio.setFilter(new String[] { "-", "DIB", "CAP" });
		setRequestAttribute("uffrecrediti", "" + tipoUfficio);

		// pagina di ritorno
		return PG_LOAD_INSERISCI_PASSAGGIO_CLASSE;
	}

}