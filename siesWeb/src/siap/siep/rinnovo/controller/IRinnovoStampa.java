package siap.siep.rinnovo.controller;

import java.io.ByteArrayOutputStream;

import siap.sico.utente.model.UtenteModel;
import siap.siep.rinnovo.model.RinnovoModel;
import f3b.util.F3BException;


/**
* <p>Title: RinnovoStampaController</p>
* <p>Description: Classe Controller per Rinnovo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface IRinnovoStampa
{
  public ByteArrayOutputStream ExStampaDocumento ( RinnovoModel aRinnovo, UtenteModel aUtente )
    throws F3BException;
}
