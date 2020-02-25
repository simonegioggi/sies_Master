package siap.sige.sentenza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.sentenza.model.FasSigeSentenzaModel;
import siap.sige.sentenza.model.SentenzaSigeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: FasSigeSentenzaController
 * </p>
 * <p>
 * Description: Classe Controller per FasSigeSentenza
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
public interface IFasSigeSentenza {

	public SentenzaSigeModel ExAssegnaSentenzaFascicoloSige(SentenzaSigeModel aFasSigeSentenza)
			throws F3BException;

	public Vector ExRicercaSentenzeAssegnateFascicolo(BigDecimal aIdFascicoloSiep) throws F3BException;

	public Vector ExRicercaFasSigeSentenza(SentenzaSigeModel aFasSigeSentenza) throws F3BException;

	public SentenzaSigeModel ExRicercaFasSigeSentenzaByKey(BigDecimal aKey) throws F3BException;

	public SentenzaSigeModel ExAssegnaSentenzaReatiFasSiep(SentenzaSigeModel aFasSigeSentenza,
			Connection aConn) throws F3BException;

	public SentenzaSigeModel ExModificaFasSigeSentenza(SentenzaSigeModel aFasSigeSentenza)
			throws F3BException;

	public void ExDeAssegnaSentenzaFascicoloSige(BigDecimal aIdFasSigeSentenza) throws F3BException;

		// 20181031: aggiunto parametro di passaggio
	public Vector ExRicercaSentenzePerSIGEPaged(SentenzaModel aSentenza, int aPage, String majorOffice)
			throws F3BException;

	public BigDecimal ExGetCountFasSigeSentenze(SentenzaModel aSentenza) throws F3BException;

	// 20/01/2010
	public Vector ExRicercaFSSentenza(FasSigeSentenzaModel aFasSigeSentenza) throws F3BException;

	public Vector ExRicercaFascicoloSigeSentenzaAssociata(BigDecimal aSenIdSentenza,
			BigDecimal aIdFascicoloSige) throws F3BException;

	public Vector ExRicercaProcedimentoSiepDiCumulo(BigDecimal aIdFascicoloSiep) throws F3BException;

}