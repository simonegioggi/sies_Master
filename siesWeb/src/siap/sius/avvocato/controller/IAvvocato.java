package siap.sius.avvocato.controller;

/**
* <p>Title: AvvocatoController</p>
* <p>Description: Classe Controller per Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IAvvocato {

	public AvvocatoModel ExModificaStoricizzaAvvocato(AvvocatoModel aAvvocato, StoricoAvvocatoModel aStorico)
			throws F3BException;

	public AvvocatoSiusModel ExInserisciAvvocato(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiusModel aAvvFascMod) throws F3BException;

	public AvvocatoModel ExInserisciAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSiusModel aAvvFascMod)
			throws F3BException;

	public Vector ExRicercaAvvocatiAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiusModel aAvvFascMod) throws F3BException;

	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public Vector ExRicercaAvvocatoProvvedimento(AvvocatoFascicoloSiusModel lAvvFascMod) throws F3BException;

	public Vector ExRicercaAvvocatoSiep(AvvocatoModel aAvvocato, BigDecimal iKey) throws F3BException;

	public AvvocatoModel ExRicercaAvvocatoByKey(BigDecimal aKey) throws F3BException;

	// controlla se già ci sono 2 avvocati per lo stesso fascicolo
	public Vector ExRicercaAvvocatiByFascicoloNoError(BigDecimal aKey) throws F3BException;

	public AvvocatoModel ExModificaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public void ExCancellaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public void ExCancellaAvvocatoFascicoloSius(AvvocatoFascicoloSiusModel aAvvocato) throws F3BException;

	public Vector ExRicercaAvvocatiByFascicolo(BigDecimal aKey) throws F3BException;

	public AvvocatoSiusModel ExRicercaAvvocatoByKeyAvvocatoFasSius(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAvvocatoFascicoloSiusByKeyAvvocato(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaDifensoreAttualiFascicolo(AvvocatoModel aModel, AvvocatoFascicoloSiusModel aFModel)
			throws F3BException;

	public AvvocatoFascicoloSiusModel ExDeassegnaAvvocato(AvvocatoFascicoloSiusModel aAvvocato)
			throws F3BException;

	public AvvocatoFascicoloSiusModel ExSostituzioneAvvocato(AvvocatoFascicoloSiusModel aAvvUp,
			AvvocatoFascicoloSiusModel aAvvocatoIns) throws F3BException;

	public Vector ExRicercaDifensore(AvvocatoModel aAvvocato) throws F3BException;

	// public Vector ExRicercaAvvocatoPerInserimento(AvvocatoModel aAvvocato)
	// throws F3BException;
	public Vector ExRicercaForo() throws F3BException;

	// MEV_21
	public AvvocatoModel ExRicercaAvvocatoCertRegInde (AvvocatoModel lAvvMod) throws F3BException;	
	public AvvocatoModel ExAggiornaAvvocatoDaReginde (AvvocatoModel aAvvocato) throws F3BException;	

}