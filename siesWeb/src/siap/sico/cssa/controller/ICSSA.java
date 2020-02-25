package siap.sico.cssa.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.cssa.model.CSSAModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ICSSA {

	public Vector ListaCSSA() throws F3BException;
	
	public Vector ListaUSSM() throws F3BException;

	public CSSAModel getCSSAByDescrComune(String aDescrComune) throws F3BException;

	public CSSAModel getUSSMByDescrComune(String aDescrComune) throws F3BException;

	public CSSAModel getCSSAByKey(BigDecimal aIdCSSA) throws F3BException;

	public Vector ExGetListaComuniCssa(CSSAModel lModel) throws F3BException;

	public Vector ExGetListaComuniCssaMinor(CSSAModel lModel) throws F3BException;

	public CSSAModel ExRicercaCSSAByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;

	public CSSAModel ExRicercaCSSAByIdFascicoloVerbaleNonFirmato(BigDecimal aIdFascicolo) throws F3BException;

	// MEV10-s3: aggiunto metodo
	public CSSAModel getCSSAByDescrComuneETipo(String aDescrComune, String aTipo) throws F3BException;

}