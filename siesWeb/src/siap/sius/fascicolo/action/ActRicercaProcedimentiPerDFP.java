package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.model.DecodeModel;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

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

public class ActRicercaProcedimentiPerDFP extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		this.setLinkRitorno();
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Recupero informazioni per i filtri di ricerca.
		Date dataDalIscrizione = (getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO,
				CAMPO_MESE_DATA_INSERIMENTO, CAMPO_GIORNO_DATA_INSERIMENTO));
		Date dataAlIscrizione = (getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO,
				CAMPO_MESE_DATA_AGGIORNAMENTO, CAMPO_GIORNO_DATA_AGGIORNAMENTO));
		Date dataDalFinePena = (getRequestDateParameter(CAMPO_ANNO_FINE_PENA, CAMPO_MESE_FINE_PENA,
				CAMPO_GIORNO_FINE_PENA));
		Date dataAlFinePena = (getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO, CAMPO_MESE_DATA_ARRIVO,
				CAMPO_GIORNO_DATA_ARRIVO));
		String lIncludeArchiviati = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_INCLUDE_ARCHIVIATI);
		String lCodPosGiuridica = getRequestStringParameter(CAMPO_COD_POS_GIURIDICA);
		// la Collection PosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		Collection lPosizioneGiuridica = new ArrayList(DecodificheManager.getInstance()
				.getPosizioneGiuridicaIscrizione());
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
		String lCodContenuto = getRequestStringParameter(CAMPO_COD_CONTENUTO);
		String lDescrContenuto = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getOggettoProcedimento(), lCodContenuto);

		String lUfficioUtenteConnesso = getUfficioUtenteConnesso().getCodUfficio();
//		String lCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// Chiama il controller.
		IFascicoloSius lFascSogCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		Vector lFascicoliDFP = lFascSogCtrl.ExRicercaFascicoliPerDataFinePena(lUfficioUtenteConnesso,
				dataDalIscrizione, dataAlIscrizione, dataDalFinePena, dataAlFinePena, lIncludeArchiviati,
				lCodPosGiuridica, lCodContenuto, Integer.parseInt(lPagina));

		String lReturnPage = "";

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", lUfficioUtenteConnesso);
		setRequestAttribute("lIncludeArchiviati", lIncludeArchiviati);
		setRequestAttribute("codContenuto", lCodContenuto);
		setRequestAttribute("codPosGiuridica", lCodPosGiuridica);
		setRequestAttribute("descrContenuto", lDescrContenuto);
		setRequestAttribute("descrPosGiuridica", lDescrPosGiuridica);
		setRequestAttribute("dataDalFinePena", DateUtils.getDateToString(dataDalFinePena, "dd/MM/yyyy"));
		setRequestAttribute("dataAlFinePena", DateUtils.getDateToString(dataAlFinePena, "dd/MM/yyyy"));
		setRequestAttribute("dataDalIscrizione", DateUtils.getDateToString(dataDalIscrizione, "dd/MM/yyyy"));
		setRequestAttribute("dataAlIscrizione", DateUtils.getDateToString(dataAlIscrizione, "dd/MM/yyyy"));

		// Inserisce i parametri di ricerca in un hashmap he verrà passata in sessione per la stampa in Excel
		HashMap<String, Object> lHashParametri = new HashMap<String, Object>();

		lHashParametri.put("IncludeArchiviati", lIncludeArchiviati);
		lHashParametri.put("CodContenuto", lCodContenuto);
		lHashParametri.put("CodPosGiuridica", lCodPosGiuridica);
		lHashParametri.put("DescrContenuto", lDescrContenuto);
		lHashParametri.put("DescrPosGiuridica", lDescrPosGiuridica);
		lHashParametri.put("DataDalFinePena", dataDalFinePena);
		lHashParametri.put("DataAlFinePena", dataAlFinePena);
		lHashParametri.put("DataDalIscrizione", dataDalIscrizione);
		lHashParametri.put("DataAlIscrizione", dataAlIscrizione);
		setSessionAttribute("ParametriRicercaProcedimentiPerDFP", lHashParametri);

		// Paginazione.
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati"))
			CountRisultati = lFascSogCtrl.ExGetNumRicercaFascicoliPerDataFinePena(lUfficioUtenteConnesso,
					dataDalIscrizione, dataAlIscrizione, dataDalFinePena, dataAlFinePena, lIncludeArchiviati,
					lCodPosGiuridica, lCodContenuto);
		else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		// setRequestAttribute("fascicoli", lVect);

		// Setta la risposta nella request.
		setRequestAttribute("fascicoli", lFascicoliDFP);

		lReturnPage = ICostantiFascicoloSius.PG_RICERCA_PROC_PERDFP;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}