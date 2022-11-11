package siap.siep.risultatoricerca.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

import siap.sico.ufficio.model.UfficioModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: RisultatoRicercaController
 * </p>
 * <p>
 * Description: Classe Controller per RisultatoRicerca
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
@SuppressWarnings("rawtypes")
public interface IRisultatoRicerca {

	public BigDecimal ExRicercaConStoreProcedure(String aCodUtente, String aCodUfficio, BigDecimal aAnnoInzio,
			BigDecimal aNumeroInzio, BigDecimal aAnnoFine, BigDecimal aNumeroFine, String aDataReato,
			BigDecimal aAnniRes, BigDecimal aMesiRes, BigDecimal aGiorniRes, String aDataPr,
			BigDecimal aPosAggregata, String aCodPosGiuridica, BigDecimal aAnniSen, BigDecimal aMesiSen,
			BigDecimal aGiorniSen, String aNazione) throws F3BException;

	public Vector ExRicercaRisultatoRicercaByKeyPage(BigDecimal aKey, int aPage) throws F3BException;

	public BigDecimal ExGetCountRicercaRisultatoByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaCompletoById(BigDecimal aKey) throws F3BException;

	// generazione foglio xls.
	public ByteArrayOutputStream ExReportRicercaByIdExcel(String aIdRisultatoRicerca, UfficioModel aUfficio,
			HashMap<String, Object> aParams) throws F3BException;

}