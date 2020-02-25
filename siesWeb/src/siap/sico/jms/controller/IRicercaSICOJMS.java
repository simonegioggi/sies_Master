package siap.sico.jms.controller;

import java.util.Vector;

import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.soggetto.model.SoggettoModel;
import f3b.model.GenericModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IRicercaSICOJMS {

	public MessaggioModel ExSpedisciRichiestaRicerca(GenericModel aModel) throws F3BException;

	// STUB 03/02/2010
	public MessaggioModel ExRicercaSoggetto(SoggettoModel aModel, String CodBDIMittente) throws F3BException;

	public MessaggioModel ExRicercaSoggetto(SoggettoModel aModel, String CodBDIMittente, boolean checkMinore)
			throws F3BException;

	// STUB 10/09/2005
	public Vector ExRicercaAllBDI() throws F3BException;

}