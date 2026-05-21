package siap.siep.jms.controller;

import f3b.util.F3BException;
import siap.jms.ICostantiJMS;
import siap.jms.messaggio.model.MessaggioModel;

public interface IPresaInCaricoJMS extends ICostantiJMS {

	public MessaggioModel ExInserisciIstanzaTrasmessa(MessaggioModel aMessaggio) throws F3BException;

	public MessaggioModel ExInserisciNuovaIstanzaTrasmessa(MessaggioModel aMessaggio) throws F3BException;

	public MessaggioModel ExInserisciProvvedimentoTrasmesso(MessaggioModel aMessaggio) throws F3BException;

	public MessaggioModel ExInserisciFascicoloSiep(MessaggioModel aMessaggio) throws F3BException;

}