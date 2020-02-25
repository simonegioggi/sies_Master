package siap.siep.sentenzariunita.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.siep.sentenzariunita.model.SentenzaRiunitaFascSiepModel;
import siap.siep.sentenzariunita.model.SentenzaRiunitaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: SentenzaRiunitaController
 * </p>
 * <p>
 * Description: Classe Controller per SentenzaRiunita
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
public interface ISentenzaRiunita {

	public SentenzaRiunitaModel ExInserisciSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita)
			throws F3BException;

	public Vector ExRicercaSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita) throws F3BException;

	public SentenzaRiunitaModel ExRicercaSentenzaRiunitaByKey(BigDecimal aKey) throws F3BException;

	public SentenzaRiunitaModel ExModificaSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita)
			throws F3BException;

	public void ExCancellaSentenzaRiunita(SentenzaRiunitaModel aSentenzaRiunita) throws F3BException;

	public SentenzaRiunitaModel ExInserisciSentenzaRiunitaFascicoloSiep(SentenzaRiunitaModel aSentenzaRiunita,
			SentenzaRiunitaFascSiepModel aSentenzaRiunitaFasSiep) throws F3BException;

	public Vector ExRicercaSentenzaRiunitaFascSiep(SentenzaRiunitaFascSiepModel aSentenzaRiunita)
			throws F3BException;

	public void ExAggiornaSentenzaRiunitaFascicolo(SentenzaRiunitaFascSiepModel aSentenzaRiunitaFasSiep,
			int modo) throws F3BException;

	public Vector<SentenzaRiunitaFascSiepModel> ExRicercaSentenzeRiuniteByIdFascSiep(
			BigDecimal aIdFascicoloSiep) throws F3BException;

	public String ExInserisciSentenzaRiunitaWithoutSequence(ArrayList aSentenzeRiunite, Connection lConn)
			throws F3BException;

}