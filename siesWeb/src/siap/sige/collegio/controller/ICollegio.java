package siap.sige.collegio.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ICuratore
 * </p>
 * <p>
 * Description: Classe Interfaccia Collegio
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
* @version 1.0
*/
@SuppressWarnings("rawtypes")
public interface ICollegio {

  public CollegioModel ExInserisciCollegio            ( CollegioModel aCollegio )  throws F3BException;
  public Vector ExRicercaCollegio                     ( CollegioModel aCollegio )  throws F3BException;
  public CollegioModel ExRicercaCollegioByKey         ( BigDecimal aKey )          throws F3BException;
  public CollegioModel ExModificaCollegio             ( CollegioModel aCollegio )  throws F3BException;
  public void ExCancellaCollegio                      ( BigDecimal IdCollegio )    throws F3BException;
  public Collection ExElencoCbxCollegiByCodUfficio    ( String aCodUfficio )       throws F3BException;
  public Vector ExRicercaCollegioByCodUfficio         ( String aCodUfficio )       throws F3BException;
  public int ExGetNumRicercaCollegio                  ( CollegioModel aCollegio )  throws F3BException;
  public CollegioModel ExRicercaCollegioByIdUdienzaSige  ( BigDecimal aIdUdienzaSige) throws F3BException;

	// 20170913: [SG] aggiunti parametri di passaggio, cambiata firma del metodo
  	// 20171129: [EC] aggiunto parametro di passaggio tipoGiudizio
	public String ExRicercaMaxCodCollegio(CollegioModel aCollegio, Date dataUdienza, String codMagis, String tipoGiudizio, String prove)
			throws F3BException;

	// 20171012: [SG] ricerca Paginata!!!
	public Vector ExRicercaCollegioPaged(CollegioModel lCollMod, int i) throws F3BException;

	// 20171013: [SG] aggiorno la tabella collegio_magistrato col collegamento all'udienza sige
	public void ExAggiornaCollegioMagistrati(UdienzaSigeModel lUdiMod,
			CollegioMagistratoModel[] collegioMagistrati, String modo) throws F3BException;

}