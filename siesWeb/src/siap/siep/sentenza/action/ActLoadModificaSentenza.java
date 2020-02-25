package siap.siep.sentenza.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.html.Option;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadModificaSentenza extends ActionSiap implements ICostantiSentenza {

	public String processRequest() throws Exception {

		// Parse della request
		String lId = super.getRequestStringParameter(CAMPO_ID_SENTENZA);

		// Lock per evitare accesso contemporaneo alla funzione chiamante che operi sulla stessa sentenza
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "Sentenza", lId,
				getCodUtenteConnesso(), getSession().getId());
		if (lck != null)
			throw new SIGEException(F3BException.USER_MESSAGE, "La gestione della  " + lck.getEntity()
					+ " è in gestione ad un altro utente!");

		// Riempie il model
		SentenzaModel lSmod = new SentenzaModel();

		lSmod.setIdSentenza(new BigDecimal(lId));
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();

		SentenzaModel lSmodRet = lCtrl.ExRicercaSentenzaByKey(lSmod.getIdSentenza());

		if (lSmodRet.getDescrLuogoProvvRif().equals("-"))
			lSmodRet.setDescrLuogoProvvRif("");

		// -- DL 16/02/2006 Gestione combo 'Tipo Sentenza'
		// -- Modifica effettuata per risolvere un problema
		// -- sulla modifica della sentenza:
		// -- se il campo COD_TIPO_PROVV_RIF era valorizzato con '03' o '04'
		// -- la combo riportava la combo valorizzata a '-'.
		// -- !!!! Non risolto definitivamente
		// -- a causa della annosa questione di distinguere tra
		// -- 'conferma'/'confermata' e 'riformata'/'in riforma'!!!!
		String lCodTipoSentenza = lSmodRet.getCodTipoProvvRif();
		if ("03".equals(lCodTipoSentenza)) {
			lSmodRet.setCodTipoProvvRif("01");
		} else if ("04".equals(lCodTipoSentenza)) {
			lSmodRet.setCodTipoProvvRif("02");
		}
		// -----------------------------------------------------------------

		String lPage = "";

		setRequestAttribute("sentenza", lSmodRet);
		setRequestAttribute("modalita", "M");

		setRequestAttribute(CAMPO_ID_SENTENZA, lId);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoDecisioneCassazione(),
				lSmodRet.getCodTipoDecisioneCassazione());
		setRequestAttribute("tipoDecisioneCassazione", "" + lOption);

		// lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(),
		// lSmodRet.getCodTipoAutoritaEmittente());
		// modificato il metodo per rendere il contenuto della combo uguale
		// a quello della ActLoadInserisciSentenza
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
				lSmodRet.getCodTipoAutoritaEmittente());
		lOption.setFilter(new String[] { "-", "CAP", "CAPMI", "CAPMID", "CAPSM", "CAS", "CASAP", "CSS",
				"DIB", "DIBM", "GIP", "GIPM", "GIPMI", "GIPP", "GIPPSD", "GP", "GUP", "GUPM", "GUPMI", "PT",
				"PTC", "PTCSD", "TMI", "TRIBSD" });
		setRequestAttribute("autoritaEmi", "" + lOption);

		Option lOptionProvv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti(),
				lSmodRet.getCodTipoProvvedimentoRif());
		lOptionProvv.setFilter(new String[] { "-", "01", "53" });
		setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv);
		Option lOptionProvvAltro = new Option(DecodificheManager.getInstance().getTipoProvvedimenti(),
				lSmodRet.getCodTipoProvvedimentoAltro());
		lOptionProvvAltro.setFilter(new String[] { "-", "01", "53" });
		setRequestAttribute("tipoProvvedimentiAltro", "" + lOptionProvvAltro);

		String selected1 = new String("-");
		String selected2 = new String("-");
		if (lSmodRet.getCodTipoAutoritaEmittente().equals("DIB")
				|| lSmodRet.getCodTipoAutoritaEmittente().equals("TRIBSD")) {
			selected1 = StringUtils.toStringJSP(lSmodRet.getCodTipoRito());
		}
		lOption = new Option(DecodificheManager.getInstance().getTipoRitoSentenza(), selected1);
		setRequestAttribute("tipoRito1", "" + lOption);

		if (lSmodRet.getCodTipoAutoritaProvvRif().equals("DIB")
				|| lSmodRet.getCodTipoAutoritaProvvRif().equals("TRIBSD")) {
			selected2 = StringUtils.toStringJSP(lSmodRet.getCodTipoRito());
		}
		lOption = new Option(DecodificheManager.getInstance().getTipoRitoSentenza(), selected2);
		setRequestAttribute("tipoRito2", "" + lOption);

		if (lSmodRet.getCodTipoProvvedimento().equals("02")) { // DECRETO
			// MEV 15
			// In fase di iscrizione del titolo esecutivo, selezionando il tipo
			// "Decreto Penale", tra le Autorità Emittenti devono essere presenti
			// solo i valori contenenti:GIP,GUP e Pretura.
			lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
					lSmodRet.getCodTipoAutoritaEmittente());
			String[] lFilter = { "-", "GIP", "GIPM", "GIPMI", "GIPP", "GIPPSD", "GUP", "GUPM", "GUPMI", "PT",
					"PTC" };
			lOption.setFilter(lFilter);
			setRequestAttribute("autoritaEmiDecretoPenale", "" + lOption);

			lPage = PG_LOAD_INSERISCIDECRETO;
		} else if (lSmodRet.getCodTipoProvvedimento().equals("05")) { // SENTENZA STRANIERA
			lPage = PG_LOAD_INSERISCISENTENZASTRANIERA;
		}
		// segnalazioni 4: aggiunte tre casistiche
		else if ("13".equals(lSmodRet.getCodTipoProvvedimento())) { // CUMULO
			Option lOptionCumulo = new Option(DecodificheManager.getInstance().getListaTipoProvvCumulo(),
					lSmodRet.getCodTipoProvvedimento());
			setRequestAttribute("listaTipoProvvCumulo", "" + lOptionCumulo);
			lOptionCumulo = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
					lSmodRet.getCodTipoAutoritaEmittente());
			String[] lFilter4 = { "-", "PM", "PMM", "PGCAP" };
			lOptionCumulo.setFilter(lFilter4);
			setRequestAttribute("autoritaEmiCumulo", "" + lOptionCumulo);
		} else if ("03".equals(lSmodRet.getCodTipoProvvedimento())) { // ORDINANZA
			Option lOptionOrdinanza = new Option(DecodificheManager.getInstance()
					.getListaTipoProvvOrdinanza(), lSmodRet.getCodTipoProvvedimento());
			setRequestAttribute("listaTipoProvvOrdinanza", "" + lOptionOrdinanza);
			lOptionOrdinanza = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
					lSmodRet.getCodTipoAutoritaEmittente());
			String[] lFilter3 = { "-", "CAP", "CAS", "CASAP", "GIP", "GIPM", "GP", "GUP", "GUPM", "DIB",
					"DIBM", "TRIBSD" };
			lOptionOrdinanza.setFilter(lFilter3);
			setRequestAttribute("autoritaEmiOrdinanza", "" + lOptionOrdinanza);
		} else if ("63".equals(lSmodRet.getCodTipoProvvedimento())) { // DECRETO ARCHIVIAZIONE
			Option lOptionDecretoArchiviazione = new Option(DecodificheManager.getInstance()
					.getListaTipoProvvDecretoArchiviazione(), lSmodRet.getCodTipoProvvedimento());
			setRequestAttribute("listaTipoProvvDecretoArchiviazione", "" + lOptionDecretoArchiviazione);
			lOptionDecretoArchiviazione = new Option(DecodificheManager.getInstance()
					.getTipoAutoritaEmittente(), lSmodRet.getCodTipoAutoritaEmittente());
			String[] lFilter2 = { "-", "GIP", "GIPM", "GIPMI", "GIPP", "GIPPSD", "GUP", "GUPM", "GUPMI",
					"PT", "PTC" };
			lOptionDecretoArchiviazione.setFilter(lFilter2);
			setRequestAttribute("autoritaEmiDecretoPenale", "" + lOptionDecretoArchiviazione);
		} else { // SENTENZA
			lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimentiRif(),
					lSmodRet.getCodTipoProvvRif());
			setRequestAttribute("tipoProvvedimentiRif", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(),
					lSmodRet.getCodTipoAutoritaProvvRif());
			setRequestAttribute("autoritaProvRif", "" + lOption);

			lOption = new Option(DecodificheManager.getInstance().getFlagSN(), "N");
			setRequestAttribute("flagSN", "" + lOption);

			// lPage = PG_LOAD_INSERISCISENTENZA;
			setRequestAttribute("pageInclude", "IncludeSentenza.jsp");
			lPage = PG_LOAD_INSERIMENTO_TITOLO;
		}

		return lPage; // restituisce la jsp di VIEW
	}

}