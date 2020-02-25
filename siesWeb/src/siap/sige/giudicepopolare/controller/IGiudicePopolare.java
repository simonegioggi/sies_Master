package siap.sige.giudicepopolare.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IGiudicePopolare
 * </p>
 * <p>
 * Description: Classe Interfaccia GiudicePopolare
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IGiudicePopolare {

	public GiudicePopolareModel ExInserisciGiudicePopolare(GiudicePopolareModel aGiudicePopolare)
			throws F3BException;

	public Vector ExRicercaGiudicePopolare(GiudicePopolareModel aGiudicePopolare) throws F3BException;

	public GiudicePopolareModel ExRicercaGiudicePopolareByKey(BigDecimal aKey) throws F3BException;

	public GiudicePopolareModel ExModificaGiudicePopolare(GiudicePopolareModel aGiudicePopolare)
			throws F3BException;

	public void ExCancellaGiudicePopolare(BigDecimal IdGiudicePopolare) throws F3BException;

	public Vector ExElencoCbxGiudiciPopolariByCodUfficio(String aCodUfficio) throws F3BException;

	public Vector ExRicercaGiudicePopolareByCodUfficio(String aCodUfficio) throws F3BException;

	public int ExGetNumRicercaGiudicePopolare(GiudicePopolareModel aGiudicePopolare) throws F3BException;

}