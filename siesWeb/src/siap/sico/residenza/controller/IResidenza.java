package siap.sico.residenza.controller;

/**
* <p>Title: ResidenzaController</p>
* <p>Description: Classe Controller per Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import siap.sico.residenza.model.ResidenzaModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IResidenza {

	public ResidenzaModel ExInserisciResidenza(ResidenzaModel aResidenza) throws F3BException;

	public Vector ExRicercaResidenza(ResidenzaModel aResidenza) throws F3BException;

	public HashMap ExRicercaSiepResidenzeDomiciliByIdSoggetto(BigDecimal aIdSoggetto) throws F3BException;

	public ResidenzaModel ExModificaResidenza(ResidenzaModel aResidenza) throws F3BException;

	public void ExCancellaResidenza(ResidenzaModel aResidenza) throws F3BException;

	public String ExInserisciResidenzaWithoutSequence(ResidenzaModel lResidenza, Connection lConn,
			BigDecimal lKeyFascicolo) throws F3BException;

	public Vector ExRicercaResidenzeDomiciliByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;

	public String ExInserisciResidenzeWithoutSequence(ArrayList aResidenze, Connection lConn)
			throws F3BException;

	public Vector ExRicercaResidenzeByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;

}