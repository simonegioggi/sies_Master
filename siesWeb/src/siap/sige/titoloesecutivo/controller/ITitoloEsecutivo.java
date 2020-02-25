package siap.sige.titoloesecutivo.controller;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import f3b.util.F3BException;

public interface ITitoloEsecutivo
{
  public FascicoloSigeEstesoModel ExAssegnaTitoloEsecutivo (FascicoloSigeEstesoModel aFascicoloSigeEsteso, FascicoloSiepModel lFasSiepMod, FascicoloSiepModel lFasSiepOld )
          throws F3BException;
  public FascicoloSigeEstesoModel ExDeassegnaTitoloEsecutivo (FascicoloSigeEstesoModel aFascicoloSigeEsteso, String chiaveAnnoSiep, String chiaveProgSiep)
		  throws F3BException;

  //public Vector ExRicercaTitoloEsecutivoByIdFascicoloSius (BigDecimal aKey)
  		  //throws F3BException;
}
