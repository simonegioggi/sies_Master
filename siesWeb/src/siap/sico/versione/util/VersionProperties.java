package siap.sico.versione.util;

import siap.sico.versione.controller.VersioneController;
import siap.sico.versione.model.VersioneModel;
import f3b.util.F3BException;

/**
 * <p>Title:VersionProperties </p>
 * <p>Description: Classe per la visulizzazione del Versioning </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class VersionProperties {

  private static VersionProperties mVersion = null;
  private static String mStringVersion = "No Version Found";

  protected VersionProperties(){}

  public static String getVersion() throws F3BException
  {
    if(mVersion == null)
    {
      mVersion = new VersionProperties();

      // chiama il controller
      VersioneController lCtrl = new VersioneController();

      // instanzia e riempie il model
      VersioneModel lVersion = lCtrl.ExRicercaVersione();

      mStringVersion = lVersion.getCodVersione();
    }

    return mStringVersion;
  }





}