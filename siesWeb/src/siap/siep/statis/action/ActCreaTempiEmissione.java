package siap.siep.statis.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.statis.controller.StatisController;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActCreaTempiEmissione
 * </p>
 * <p>
 * Description: Classe Action per la creazione dei report per Tempi Emissione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActCreaTempiEmissione extends ActionSiap implements ICostantiStatis {

	public String processRequest() throws F3BException {

		// Lock
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "TEMPI_EMISSIONE_PROVVEDIMENTI",
				"1", getCodUtenteConnesso(), getSession().getId());

		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Questa funzione non può essere attivata contemporaneamente da più utenti !<BR>Riprovare più tardi !");
			return IWebConstants.PG_MESSAGE;
		}

		// Recupero dati della maschera

		int annoIni = getRequestIntParameter(CAMPO_ANNO_INIZIALE);
		String meseIni = getRequestStringParameter(CAMPO_MESE_INIZIALE);
		String giornoIni = getRequestStringParameter(CAMPO_GIORNO_INIZIALE);

		int annoFin = getRequestIntParameter(CAMPO_ANNO_FINALE);
		String meseFin = getRequestStringParameter(CAMPO_MESE_FINALE);
		String giornoFin = getRequestStringParameter(CAMPO_GIORNO_FINALE);

		int lCodInter = getRequestIntParameter(CAMPO_LISTA_INTERVALLI);

		String lCodMag = getRequestStringParameter(CAMPO_LISTA_MAGISTRATI);
		// NGG
		String lCodAcco = getRequestStringParameter(CAMPO_COD_ACCORPATO_1);

		// Recupero Ufficio Utente
		UtenteModel lUtenteMod = new UtenteModel((UtenteModel) getSession().getAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lUffUtente = lUtenteMod.getUfficioUtente().getCodUfficio();
		String lTipoUffUte = lUtenteMod.getUfficioUtente().getCodTipoUfficio();

		IUfficio lCtrlUff = SICOLookupRemote.getUfficioRemote();
		Vector lListaUffici = lCtrlUff.ListaUfficiAccorpati(lTipoUffUte, lUffUtente);

		UfficioModel UffMod = new UfficioModel();
		UffMod.setUfficiAccorpati(lListaUffici);
		UffMod.setCodTipoUfficio(lTipoUffUte);
		UffMod.setCodUfficio(lUffUtente);

		// recupero i codoci Uff Accorpati

		String Accorpato1 = "-";
		String Accorpato2 = "-";
		String Accorpato3 = "-";
		String CodUfficioConnesso = "";

		Vector<String> lVec = new Vector<String>();
		int i = 0;
		if (lListaUffici.size() == 0) {

		} else {
			Iterator itx = lListaUffici.iterator();
			while (itx.hasNext()) {
				UfficioAccorpatoModel lUff = (UfficioAccorpatoModel) itx.next();
				String uff1 = lUff.getCodUfficio();
				lVec.add(i, uff1);
				i++;
			}

		}

		if (lCodAcco.equals("-")) {
			CodUfficioConnesso = this.getCodUfficioUtenteConnesso();
			if (i == 1) {
				Accorpato1 = lVec.get(0);
			}
			if (i == 2) {
				Accorpato1 = lVec.get(0);
				Accorpato2 = lVec.get(1);
			}
			if (i == 3) {
				Accorpato1 = lVec.get(0);
				Accorpato2 = lVec.get(1);
				Accorpato3 = lVec.get(2);
			}
		} else {
			Iterator itx1 = lListaUffici.iterator();
			while (itx1.hasNext()) {
				UfficioAccorpatoModel lUff1 = (UfficioAccorpatoModel) itx1.next();
				if (lUff1.getDescrizione().equals(lCodAcco)) {
					CodUfficioConnesso = lUff1.getCodUfficio();
				}

			}
		}

		// END NGG

		// ISTANZA CONTROLLER
		StatisController lCtrl = new StatisController();

		String dataIni = giornoIni + "/" + meseIni + "/" + annoIni;
		String dataFin = giornoFin + "/" + meseFin + "/" + annoFin;

		// RICHIAMO STORED PROCEDURE
		lCtrl.ExTempiEmissioneStoredProcedure(dataIni, dataFin, CodUfficioConnesso, Accorpato1, Accorpato2,
				Accorpato3);

		HSSFWorkbook wb = new HSSFWorkbook();

		Vector lTempi = new Vector();

		// Recupero i dati dalle tabelle caricate dalla SP Ricerca riepilogo
		lTempi = lCtrl.ExRicercaRiepilogoGeneraleTempi(annoIni, annoFin, 4);

		// NGG Statistiche SIEP - Trova la descrizione dell'eventuale ufficio accorpato per le INTESTAZIONI
		IUfficio lCtrlu = SICOLookupRemote.getUfficioRemote();
		UfficioModel ufMod = new UfficioModel();
		ufMod = lCtrlu.getUfficioByKey(CodUfficioConnesso);
		String DescUffIntesta = ufMod.getDescrComune();
		//

		// creazione del file excel (foglio riepilogo)
		lCtrl.ExCreateRiepilogoTempiEmissione(lTempi, wb, this.getUfficioUtenteConnesso(), dataIni, dataFin,
				DescUffIntesta);

		// Ricerca Dettaglio
		lTempi = lCtrl.ExRicercaDettaglioTempiEmissione(lCodInter, lCodMag);

		// Iterator itx = lTempi.iterator();
		// while (itx.hasNext()) {
		// IspTempiEmissioneModel IspMod = (IspTempiEmissioneModel) itx.next();
		// }

		// creazione del file excel (foglio dettaglio)
		lCtrl.ExCreateDettaglioTempiEmissione(lTempi, wb, this.getUfficioUtenteConnesso(), dataIni, dataFin,
				lCodInter, lCodMag, Accorpato1, DescUffIntesta);

		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		} catch (IOException ioe) {
			throw new F3BException("ActCreaTempiIscrizioni.processRequest: " + ioe);
		}

		// setRequestAttribute("cartel", lCartel.toString());
		// setRequestAttribute("report", fileOut.toByteArray());

		// return IWebConstants.PG_DOWNLOAD_XLS;

		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);

		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}