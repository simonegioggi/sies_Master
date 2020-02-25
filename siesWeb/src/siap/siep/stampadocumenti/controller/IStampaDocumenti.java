package siap.siep.stampadocumenti.controller;

/**
* <p>Title: StampaDocumentiController</p>
* <p>Description: Classe Controller per StampaDocumenti</p>
* <p>Copyright: Copyright (c) 2007</p>
* <p>Company: Eunics</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.stampadocumenti.model.StampaDocumentiModel;
import siap.siep.statistiche.model.StatisticheFogliComplementariContainerModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IStampaDocumenti {

	public StampaDocumentiModel ExInserisciStampaDocumenti(StampaDocumentiModel aStampaDocumenti)
			throws F3BException;

	public Vector ExRicercaStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException;

	public void ExModificaStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException;

	public void ExCancellaStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException;

	public BigDecimal ExGetCountStampaDocumenti(StampaDocumentiModel aStampaDocumenti) throws F3BException;

	public StampaDocumentiModel ExRicercaStampaDocumentiById(BigDecimal aIdStampa) throws F3BException;

	public Vector ExRicercaStampaDocumentiPaged(StampaDocumentiModel aStampaDocumenti, int aPage)
			throws F3BException;

	public ByteArrayOutputStream ExPreStampaStatisticheFC(
			StatisticheFogliComplementariContainerModel container) throws F3BException;

}