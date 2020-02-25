package siap.sige.udienza.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.udienza.model.UdienzaSigeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: UdienzaSigeController
 * </p>
 * <p>
 * Description: Classe Controller per UdienzaSige
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
public interface IUdienzaSige {

	public UdienzaSigeModel ExInserisciUdienzaSige(UdienzaSigeModel aUdienzaSige, BigDecimal aIdFascicoloSige)
			throws F3BException;

	public Vector ExRicercaUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException;

	public void ExModificaUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException;

	public void ExCancellaUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException;

	public BigDecimal ExGetCountUdienzaSige(UdienzaSigeModel aUdienzaSige) throws F3BException;

	public UdienzaSigeModel ExRicercaUdienzaSigeById(BigDecimal aIdUdienzaSige) throws F3BException;

	public Vector ExRicercaUdienzaSigePaged(UdienzaSigeModel aUdienzaSige, int aPage) throws F3BException;

	public Vector ExRicercaUdienzaCollegialeSige(String codMag, String dataUdienza,String ufficioAppartenenza) throws F3BException;

	// [EC] 20171019: aggiungo paametro in input
	// [EC] 20190325:   AGGIUNGO IL PARAMETR COD_UFFICIO IN INPUT (L'UDIENZA DEVE ESSERE UNIVOCA PER UFFICIO)
	public Vector ExRicercaUdienzaMonocraticaSige(String codMag, String dataUdienza, BigDecimal sez, String ufficioAppartenenza) throws F3BException;

	// 20170914: [SG] aggiunto metodo di ricerca
	// 20170914: [SG] aggiunto parametro di input ufficioAppartenenza
	public Vector ExRicercaUdienzaCollegialeSige(String codMagis, String sDataUdienza, BigDecimal idSezione,
			BigDecimal idAssistente, String idProcuratore, String modalita, String ufficioAppartenenza)
			throws F3BException;
	
	// aggiunto il seguente metodo per intervento sies 11.2.1
	public Vector ExRicercaUdienzaSigePerFunzioniSupporto(UdienzaSigeModel aUdienzaSige) throws F3BException;
	
	// aggiunto il seguente metodo per intervento sies 11.2.1
	public Vector ExRicercaUdienzaCollegialeSige(UdienzaSigeModel aUdienzaSige, String ufficioAppartenenza, String prove) throws F3BException;

}