package siap.siep.fascicolo.controller;


/**
* <p>Title: IFascicoloSies</p>
* <p>Description: Classe Controller per Fascicolo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

public interface IFascicoloSiepStampa
{
  public TreeModel prelevaDatiStampaFascicolo( FascicoloSiepModel aFascMod, UtenteModel aUtenteMod )
    throws F3BException;

  public TreeModel  prelevaDatiStampaFascicoliMultipli( FascicoloSiepModel aFascMod, UtenteModel aUtenteMod)
	throws F3BException;
}
