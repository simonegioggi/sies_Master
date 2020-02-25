package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaUdienza
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Udienza
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

public class ActRicercaUdienzaUDS extends ActionSiap implements ICostantiUdienza {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		Date[] lCampoDate = new Date[2]; // intervallo di date di ricerca
		String lReturnPage = ""; // pagina jsp di ritorno
		UdienzaModel lUdiMod = new UdienzaModel();
		MagistratoModel lMagMod = new MagistratoModel();
		AssistenteGiudiziarioModel lAssMod = new AssistenteGiudiziarioModel();

		// inserisce le condizioni di ricerca nel model
		lUdiMod.setNumCollegio(getRequestBigDecimalParameter(CAMPO_NUM_COLLEGIO));
		lCampoDate[0] = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA, CAMPO_MESE_DATA_UDIENZA,
				CAMPO_GIORNO_DATA_UDIENZA);
		lCampoDate[1] = getRequestDateParameter(CAMPO_ANNO_DATA_UDIENZA_FINE, CAMPO_MESE_DATA_UDIENZA_FINE,
				CAMPO_GIORNO_DATA_UDIENZA_FINE);
		lUdiMod.setDataUdienza(lCampoDate[0]);
		lUdiMod.setDataUdienzaFine(lCampoDate[1]);
		// La ricerca è sempre filtrata per ufficio
		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		// 05.12.2008 Angela sono stati aggiunti ulteriori criteri di ricerca
		lUdiMod.setCodPresidente(getRequestStringParameter(CAMPO_COD_PRESIDENTE));
		lUdiMod.setCodPg(getRequestStringParameter(CAMPO_COD_PG));
		lUdiMod.setCodIdAssistente(getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE));

		// ANGELA Decodifico il codice assistente e passo il parametro in sessione ************************************
		lAssMod.setIdAssistenteGiudiziario(getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE));
		BigDecimal assg = getRequestBigDecimalParameter(CAMPO_COD_ID_ASSISTENTE);
		IAssistenteGiudiziario lAssistenteCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
		AssistenteGiudiziarioModel lAssistente = lAssistenteCtrl.ExRicercaAssistenteGiudiziarioByKey(assg);
		setRequestAttribute("assistente", lAssistente);

		// ANGELA Decodifico il codice presidente e passo il parametro in sessione ************************************
		lMagMod.setCodMagistrato(getRequestStringParameter(CAMPO_COD_PRESIDENTE));
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		Vector lMagistrato = lMagCtrl.ExRicercaMagistrato(lMagMod);
		MagistratoModel lMagiModel = new MagistratoModel((MagistratoModel) lMagistrato.firstElement());
		setRequestAttribute("presidente", lMagiModel);

		// ANGELA Decodifico il codice procuratore e passo il parametro in sessione ************************************
		lMagMod.setCodMagistrato(getRequestStringParameter(CAMPO_COD_PG));
		Vector lProcuratore = lMagCtrl.ExRicercaMagistrato(lMagMod);
		MagistratoModel lProcuModel = new MagistratoModel((MagistratoModel) lProcuratore.firstElement());
		setRequestAttribute("procuratore", lProcuModel);

		// Imposta, nell'attributo Message di GenericModel, il
		// criterio di ordinamento, si utilizza l'attributo Message
		// per convenienza, al fine di ridurre l'impatto delle modifiche.
		lUdiMod.setMessage(getRequestStringParameter(ICostantiUdienza.CAMPO_TIPOORDINAMENTO));

		// richiama il controller per la ricerca
		IUdienza lCtrl = SIUSLookupRemote.getUdienzaRemote();
		Vector lUdienze = lCtrl.ExRicercaUdienzaUDS(lUdiMod);

		// inserisce l'intervallo date di ricerca nella request

		setRequestAttribute("campo_date", lCampoDate);

		if (lUdienze.size() == 1) { // unica udienza
			UdienzaModel lUdiModel = new UdienzaModel((UdienzaModel) lUdienze.firstElement());
			// Inserisce il model Udienza nella request
			setRequestAttribute("udienza", lUdiModel);
			setFunctionsAvailableToRequest("siap.sius.udienza.action.ActLoadDettaglioUdienzaUDS");
			lReturnPage = PG_LOAD_DETTAGLIOUDIENZA_UDS;
		} else { // lista di udienze
					// Inserisce il Vector Udienza nella request
			setRequestAttribute("udienze", lUdienze);
			// Inserisce in request, il valore del tipo ordinamento richiesto.
			setRequestAttribute(ICostantiUdienza.CAMPO_TIPOORDINAMENTO, lUdiMod.getMessage());
			lReturnPage = PG_RICERCAUDIENZA_UDS;
		}

		// MEV10-s3: modificato il codice tipo ufficio: lo prelevo dalla tipologia di utente connesso
		String lCodTipoUfficio = getCodTipoUfficioConnesso();
		setRequestAttribute("codTipoUfficio", "" + lCodTipoUfficio);

		// valore di ritorno
		return lReturnPage;
	}
}