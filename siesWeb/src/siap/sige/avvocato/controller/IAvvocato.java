package siap.sige.avvocato.controller;

/**
* <p>Title: AvvocatoController</p>
* <p>Description: Classe Controller per Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.sentenza.model.SentenzaSigeModel;
import siap.sige.udienzaparti.model.AvvocatoParteModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;

@SuppressWarnings("rawtypes")
public interface IAvvocato {

	public AvvocatoModel ExModificaStoricizzaAvvocato(AvvocatoModel aAvvocato, StoricoAvvocatoModel aStorico)
			throws F3BException;

	public AvvocatoSigeModel ExInserisciAvvocato(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSigeModel aAvvFascMod) throws F3BException;

	public AvvocatoModel ExInserisciAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSigeModel aAvvFascMod)
			throws F3BException;

	public Vector ExRicercaAvvocatiAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSigeModel aAvvFascMod) throws F3BException;

	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public Vector ExRicercaAvvocatoProvvedimento(AvvocatoFascicoloSigeModel lAvvFascMod) throws F3BException;

	public Vector ExRicercaAvvocatoSiep(AvvocatoModel aAvvocato, BigDecimal iKey) throws F3BException;

	public AvvocatoModel ExRicercaAvvocatoByKey(BigDecimal aKey) throws F3BException;

	// controlla se già ci sono 2 avvocati per lo stesso fascicolo
	public ArrayList ExRicercaAvvocatiByFascicoloNoError(BigDecimal aKey) throws F3BException;

	public AvvocatoModel ExModificaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public void ExCancellaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public void ExCancellaAvvocatoFascicoloSige(AvvocatoFascicoloSigeModel aAvvocato) throws F3BException;

	public Vector ExRicercaAvvocatiByFascicolo(BigDecimal aKey) throws F3BException;

	public AvvocatoSigeModel ExRicercaAvvocatoByKeyAvvocatoFasSige(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAvvocatoFascicoloSigeByKeyAvvocato(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaDifensoreAttualiFascicolo(AvvocatoModel aModel, AvvocatoFascicoloSigeModel aFModel)
			throws F3BException;

	public AvvocatoFascicoloSigeModel ExDeassegnaAvvocato(AvvocatoFascicoloSigeModel aAvvocato)
			throws F3BException;

	public AvvocatoFascicoloSigeModel ExSostituzioneAvvocato(AvvocatoFascicoloSigeModel aAvvUp,
			AvvocatoFascicoloSigeModel aAvvocatoIns) throws F3BException;

	public Vector ExRicercaDifensore(AvvocatoModel aAvvocato) throws F3BException;

	public Vector ricercaDifensoreDallaListaSiep(AvvocatoModel aAvvocato, SentenzaSigeModel sentenza)
			throws F3BException;

	// public Vector ExRicercaAvvocatoPerInserimento(AvvocatoModel aAvvocato)
	// throws F3BException;
	public Vector ExRicercaForo() throws F3BException;

	public Vector ExRicercaAvvocatiByParteUdienza(BigDecimal aKey) throws F3BException;

	public AvvocatoParteModel ExInserisciAvvocato(AvvocatoModel aAvvocato,
			PartiUdienzaDifensoreModel aAvvParteMod) throws F3BException;

	public Vector ExRicercaDifensoreAttualiParteUdienza(AvvocatoModel aModel,
			PartiUdienzaDifensoreModel aPUDModel) throws F3BException;

	public PartiUdienzaDifensoreModel ExDeassegnaDifensoreParteUdienza(PartiUdienzaDifensoreModel aPUDModel)
			throws F3BException;

	public Vector ExRicercaDifensoreParteByKeyAvvocato(BigDecimal aKey) throws F3BException;

	public PartiUdienzaDifensoreModel ExSostituzioneAvvocato(PartiUdienzaDifensoreModel aAvvUp,
			PartiUdienzaDifensoreModel aAvvocatoIns) throws F3BException;

	// MEV_21: aggiunto metodo di ricerca avvocato certificato reginde
	public AvvocatoModel ExRicercaAvvocatoCertRegInde(AvvocatoModel am) throws F3BException;

	// MEV_21: aggiunto metodo di aggiornamento avvocato da reginde
	public AvvocatoModel ExAggiornaAvvocatoDaReginde(AvvocatoModel am) throws F3BException;

}