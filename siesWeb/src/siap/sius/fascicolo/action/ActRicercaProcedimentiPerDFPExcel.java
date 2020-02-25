package siap.sius.fascicolo.action;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sige.web.ActionSige;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.model.DecodeModel;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaProcedimentiPerDFPExcel extends ActionSige implements ICostantiFascicoloSius {

	public String processRequest() throws Exception {

		HashMap<String, Object> lHashParametri = (HashMap<String, Object>) getSessionAttribute(
				"ParametriRicercaProcedimentiPerDFP");

		// Recupero informazioni per i filtri di ricerca.

		String lIncludeArchiviati = (String) lHashParametri.get("IncludeArchiviati");
		String lCodContenuto = (String) lHashParametri.get("CodContenuto");
		String lDescrContenuto = (String) lHashParametri.get("DescrContenuto");
		String lCodPosGiuridica = (String) lHashParametri.get("CodPosGiuridica");
		Date lDataDalIscrizione = (Date) lHashParametri.get("DataDalIscrizione");
		Date lDataAlIscrizione = (Date) lHashParametri.get("DataAlIscrizione");
		Date lDataDalFinePena = (Date) lHashParametri.get("DataDalFinePena");
		Date lDataAlFinePena = (Date) lHashParametri.get("DataAlFinePena");

		// la Collection PosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		Collection lPosizioneGiuridica = new ArrayList(
				DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione());
		// Si aggiungono le posizioni giuridiche di Esecuzione.
		Iterator lItx = (DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione()).iterator();
		while (lItx.hasNext()) {
			DecodeModel lElemento = (DecodeModel) lItx.next();
			if (!(lElemento.getCode().equals("-")))
				lPosizioneGiuridica.add(lElemento);
		}
		// Si aggiungono le posizioni giuridiche di Altra Causa.
		lItx = (DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa()).iterator();
		while (lItx.hasNext()) {
			DecodeModel lElemento = (DecodeModel) lItx.next();
			if (!(lElemento.getCode().equals("-")))
				lPosizioneGiuridica.add(lElemento);
		}

		String lDescrPosGiuridica = DecodificheUtils.getDescbyCode(lPosizioneGiuridica, lCodPosGiuridica);

		// Chiama il controller.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		ByteArrayOutputStream fileOut = lFascSogCtrl.ExReportFascicoliPerDataFinePenaExcel(
				getUfficioUtenteConnesso(), lDataDalIscrizione, lDataAlIscrizione, lDataDalFinePena,
				lDataAlFinePena, lIncludeArchiviati, lCodPosGiuridica, lCodContenuto, lDescrPosGiuridica,
				lDescrContenuto);
		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}

}