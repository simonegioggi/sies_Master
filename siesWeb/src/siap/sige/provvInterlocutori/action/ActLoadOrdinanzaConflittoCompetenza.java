package siap.sige.provvInterlocutori.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadOrdinanzaConflittoCompetenza
 * </p>
 * <p>
 * Description: Classe Action per la visualizzazione della Form di Input dell'Ordinanza di Conflitto di
 * Competenza.
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * Author: Luigi
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActLoadOrdinanzaConflittoCompetenza extends ActLoadInserisciProvvInterlocutori {

	protected FascicoloSigeEstesoModel mFasEsteso;
	String lTipoGiudizio = "-";

	protected boolean letturaFascicoloEstesoinSessione() throws Exception {

		boolean lRet = true;

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = getFascicoloSigeEstesoInSessione();

		if (mFasEsteso.getFascicoloSige() == null
				|| mFasEsteso.getFascicoloSige().getIdFascicoloSige() == null) {
			lRet = false;
		} else {
			// Si Rilegge il fasciclo SIGE Esteso e lo si inserisce in SESSIONE.
			IFascicoloSige lCtrlFas = SIGELookupRemote.getFascicoloSigeRemote();
			mFasEsteso = lCtrlFas
					.ExRicercaEstesaFascicoloSigeByKey(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
			setSessionAttribute("FascicoloSigeEsteso", mFasEsteso);
		}

		return lRet;
	}

	protected void loadDatiUdienzaSige() throws Exception {

		if (mFasEsteso.getUdienzaProcedimento() != null
				&& mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige() != null) {
			caricaDatiUdienza(mFasEsteso.getUdienzaProcedimento().getIdUdienzaProcedimentoSige());
		} else if (!isRequestParameterNullObj(ICostantiCollegio.FORM_DEF_COLLEGIO)) {
			// Se nella request è flaggato il FORM_DEF_COLLEGIO siamo nel caso di un inserimento udienza
			// diretto
			lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);
			String idUdienza[] = super.getRequest()
					.getParameterValues(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
			BigDecimal lIdUdienzaSige = new BigDecimal(idUdienza[idUdienza.length - 1]);

			String sIdUdienzaSige = lIdUdienzaSige.toString();
			UdienzaSigeModel udiSige = getUdienzaSige(sIdUdienzaSige);
			setRequestAttribute("UdienzaSige", udiSige);
		}
		// 20190519 [SG]: aggiunta gestione idUdienzaSige
		else if (!isRequestParameterNullEmptyObj("idUdiSig")) {
			UdienzaSigeModel udiSige = getUdienzaSige(getRequestStringParameter("idUdiSig"));
			setRequestAttribute("UdienzaSige", udiSige);
		}
	}

	protected void caricaDatiUdienza(BigDecimal idUdienzaProcedimentoSige) throws Exception {

		setRequestAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE,
				idUdienzaProcedimentoSige.toString());

		// Preventivamente si controlla l'esistenza di una Udienza per il fascicolo
		IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		UdienzaProcedimentoSigeModel lUdiProSige = lUdiProCtrl
				.ExRicercaUdienzaProcedimentoSigeByKey(idUdienzaProcedimentoSige);

		if (lUdiProSige != null) {

			// Se trovato UDIENZA_PROCEDIMENTO_SIGE si ricava l'ID Udienza e l'ID Evento
			BigDecimal lIdEvento = lUdiProSige.getEveIdEvento();
			setRequestAttribute("IdUdienzaEvento", lIdEvento.toString());

			// IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			// eventoNotificaModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

			// IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
			// provvSigeEveMod = lCtrlProv.ExRicercaProvvedimentoByIdEvento(lIdEvento);

			// chiama il controller
			IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();
			Vector lPartiC = lCtrl.ExRicercaPartiUdienzaByIdUdienza(idUdienzaProcedimentoSige, "C");
			setRequestAttribute("udienzaPartiC", lPartiC);

			Vector lPartiO = lCtrl.ExRicercaPartiUdienzaByIdUdienza(idUdienzaProcedimentoSige, "O");
			setRequestAttribute("udienzaPartiO", lPartiO);

			BigDecimal lIdUdienzaSige = lUdiProSige.getUdiIdUdienzaSige();
			if (lIdUdienzaSige != null) {
				String sIdUdienzaSige = lIdUdienzaSige.toString();
				setRequestAttribute("IdUdienzaSige", sIdUdienzaSige);
				setRequestAttribute("UdienzaSige", getUdienzaSige(sIdUdienzaSige));
			}

			// nel caso di rifissazione prelevo i dati della sezione e dell'aula dalla Udienza Sige associata
			// lSezioneUdienza = getIdSezioneUdienzaSige();
			// aulaUdienza = getAulaUdienzaSige(lSezioneUdienza);
		}

	}

	public String processRequest() throws Exception {

		// Valorizzazione della pagina di Load Inserimento specifica
		mRetPage = ICostantiProvvInterlocutoriSige.PG_LOAD_ORDINANZA_CONFLITTO_COMPETENZA;
		String lretPage = super.processRequest();

		// / Se il Fascicolo non è modificabile non è possibile emettere provvedimento
		// if (!IsFascicoloSigeModificabile())
		// throw new SIGEException(SIGEException.USER_MESSAGE,
		// "Non è possibile emettere provvedimento per questo Procedimento!");

		// @emma 24072018 intervento post COLLAUDO 11.2
		FascicoloSigeEstesoModel lFasEsteso = super.getFascicoloSigeEstesoInSessione();
		if (lFasEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo("05") == 0
				|| lFasEsteso.getFascicoloSige().getCodStatoFascicolo().compareTo("01") == 0) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Non è possibile emettere provvedimento per questo Procedimento!");
			return IWebConstants.PG_MESSAGE;
		}

		// mostraUfficiGE();
		setRequestAttribute("modalita", "I");

		letturaFascicoloEstesoinSessione();

		// preleva i dati della udienza sige
		loadDatiUdienzaSige();

		// il tipo giudizio va definito quando si definisce l'udienza
		if (lTipoGiudizio == null || "".equals(lTipoGiudizio) || "-".equals(lTipoGiudizio)) {
			// altrimenti si verifica quello in precedenza selezionato nella definizione del procedimento
			lTipoGiudizio = (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
					: mFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim());
		}
		setRequestAttribute("tipoGiudizioVal", lTipoGiudizio);

		return lretPage;
	}

}