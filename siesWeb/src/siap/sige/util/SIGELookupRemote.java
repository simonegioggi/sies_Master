package siap.sige.util;

/**
* <p>Title: SIGELookupRemote</p>
* <p>Description: Classe Lookup dei  controller in ambito SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

import siap.sige.aula.controller.IAula;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.camponota.controller.ICampoNota;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.datiprovsige.controller.IDatiProvvedimentoSige;
import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.documentoallegato.controller.IDocumentoAllegato;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.posizionematerialefascsige.controller.IPosizioneMaterialeFascSige;
import siap.sige.provvInterlocutori.controller.IProvvInterlocutoriSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.richiesta.controller.IRichiestaSige;
import siap.sige.richiestaatti.controller.IRichiestaAttiSige;
import siap.sige.scadenzario.controller.IScadenzarioSige;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.sezione.controller.ISezione;
import siap.sige.stampa.controller.IStampaSige;
import siap.sige.statistiche.controller.IStatisticheSige;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.titoloesecutivo.controller.ITitoloEsecutivo;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSigeRuolo;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import f3b.util.F3BException;
import f3b.util.LookupClass;

public class SIGELookupRemote extends LookupClass
{
  public static IFascicoloSige getFascicoloSigeRemote() throws F3BException
	{
    Object lRef;
		IFascicoloSige lRemote;
		lRef = lookup("siap.sige.fascicolo.controller.FascicoloSigeController");
		lRemote = (IFascicoloSige)lRef;
		return lRemote;
	}
	
  public static IRichiestaSige getRichiestaSigeRemote() throws F3BException
	{
    Object lRef;
		IRichiestaSige lRemote;
		lRef = lookup("siap.sige.richiesta.controller.RichiestaSigeController");
		lRemote = (IRichiestaSige)lRef;
		return lRemote;
	}
  
  public static ITenoreSige getTenoreSigeRemote() throws F3BException
  {
    Object lRef;
    ITenoreSige lRemote;
    lRef = lookup("siap.sige.tenore.controller.TenoreSigeController");
    lRemote = (ITenoreSige)lRef;
    return lRemote;
  }
  
  public static IMagistratoAssegnatario getMagistratoAssegnatarioRemote() throws F3BException
  {
    Object lRef;
    IMagistratoAssegnatario lRemote;
    lRef = lookup("siap.sige.magistratoassegnatario.controller.MagistratoAssegnatarioController");
    lRemote = (IMagistratoAssegnatario)lRef;
    return lRemote;
  }
  
  public static ICuratore getCuratoreRemote() throws F3BException
  {
    Object lRef;
    ICuratore lRemote;
    lRef = lookup("siap.sige.curatore.controller.CuratoreController");
    lRemote = (ICuratore)lRef;
    return lRemote;
  }

  public static ISezione getSezioneRemote() throws F3BException
  {
    Object lRef;
    ISezione lRemote;
    lRef = lookup("siap.sige.sezione.controller.SezioneController");
    lRemote = (ISezione)lRef;
    return lRemote;
  }
  
  public static IMagistrato getMagistratoRemote() throws F3BException
  {
    Object lRef;
    IMagistrato lRemote;
    lRef = lookup("siap.sige.magistrato.controller.MagistratoController");
    lRemote = (IMagistrato)lRef;
    return lRemote;
  }
  
  public static IFasSigeSentenza getFasSigeSentenzaRemote() throws F3BException
  {
    Object lRef;
    IFasSigeSentenza lRemote;
    lRef = lookup("siap.sige.sentenza.controller.FasSigeSentenzaController");
    lRemote = (IFasSigeSentenza)lRef;
    return lRemote;
  }

  /**
   * Ritorna l'interfaccia del controller Stampa.
   * <p>
   * @return l'interfaccia del relativo controller.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static IStampaSige getStampaRemote() throws F3BException
  {
     Object lRef;
     IStampaSige lRemote;
     lRef = lookup("siap.sige.stampa.controller.StampaSigeController");
     lRemote = (IStampaSige)lRef;

     return lRemote;
  }

  
  public static IGiudicePopolare getGiudicePopolareRemote() throws F3BException
  {
    Object lRef;
    IGiudicePopolare lRemote;
    lRef = lookup("siap.sige.giudicepopolare.controller.GiudicePopolareController");
    lRemote = (IGiudicePopolare)lRef;
    return lRemote;
  }
  
  public static ICollegio getCollegioRemote() throws F3BException
  {
    Object lRef;
    ICollegio lRemote;
    lRef = lookup("siap.sige.collegio.controller.CollegioController");
    lRemote = (ICollegio)lRef;
    return lRemote;
  }

  public static IAvvocato getAvvocatoRemote() throws F3BException
  {
    Object lRef;
    IAvvocato lRemote;
    lRef = lookup("siap.sige.avvocato.controller.AvvocatoController");
    lRemote = (IAvvocato)lRef;
    return lRemote;
  }
  
  public static IFasSigeDetenzione getFasSigeDetenzioneRemote() throws F3BException
  {
    Object lRef;
    IFasSigeDetenzione lRemote;
    lRef = lookup("siap.sige.detenzione.controller.FasSigeDetenzioneController");
    lRemote = (IFasSigeDetenzione) lRef;
    return lRemote;
  }


  public static IProvvedimentoSige getProvvedimentoRemote() throws F3BException
  {
    Object lRef;
    IProvvedimentoSige lRemote;
    lRef = lookup("siap.sige.provvedimento.controller.ProvvedimentoSigeController");
    lRemote = (IProvvedimentoSige) lRef;
    return lRemote;
  }
  
  public static IDatiProvvedimentoSige getDatiProvvedimentoSigeRemote() throws F3BException
  {
    Object lRef;
    IDatiProvvedimentoSige lRemote;
    lRef = lookup("siap.sige.datiprovsige.controller.DatiProvvedimentoSigeController");
    lRemote = (IDatiProvvedimentoSige) lRef;
    return lRemote;
  }
  
  public static IImpugnazioneSige getImpugnazioneSigeRemote() throws F3BException
  {
    Object lRef;
    IImpugnazioneSige lRemote;
    lRef = lookup("siap.sige.impugnazione.controller.ImpugnazioneSigeController");
    lRemote = (IImpugnazioneSige) lRef;
    return lRemote;
  }

  public static IScadenzarioSige getScadenzarioSigeRemote() throws F3BException
  {
    Object lRef;
    IScadenzarioSige lRemote;
    lRef = lookup("siap.sige.scadenzario.controller.ScadenzarioSigeController");
    lRemote = (IScadenzarioSige) lRef;
    return lRemote;
  }

  public static IUdienzaSige getUdienzaSigeRemote() throws F3BException
  {
    Object lRef;
    IUdienzaSige lRemote;
    lRef = lookup("siap.sige.udienza.controller.UdienzaSigeController");
    lRemote = (IUdienzaSige)lRef;
    return lRemote;
  }

  public static IUdienzaProcedimentoSige getUdienzaProcedimentoSigeRemote() throws F3BException
  {
    Object lRef;
    IUdienzaProcedimentoSige lRemote;
    lRef = lookup("siap.sige.udienzaprocedimento.controller.UdienzaProcedimentoSigeController");
    lRemote = (IUdienzaProcedimentoSige)lRef;
    return lRemote;
  }

  public static IUdienzaSigeRuolo getUdienzaSigeRuoloRemote() throws F3BException
  {
    Object lRef;
    IUdienzaSigeRuolo lRemote;
    lRef = lookup("siap.sige.udienza.controller.UdienzaSigeRuoloController");
    lRemote = (IUdienzaSigeRuolo)lRef;
    return lRemote;
  }

  public static IRichiestaAttiSige getRichiestaAttiSigeRemote() throws F3BException
	{
    Object lRef;
		IRichiestaAttiSige lRemote;
		lRef = lookup("siap.sige.richiestaatti.controller.RichiestaAttiSigeController");
		lRemote = (IRichiestaAttiSige)lRef;
		return lRemote;
	}

  public static IDecretoUnificazioneSige getDecretoUnificazioneSigeRemote() throws F3BException
	{
  Object lRef;
  		IDecretoUnificazioneSige lRemote;
		lRef = lookup("siap.sige.decretounificazione.controller.DecretoUnificazioneSigeController");
		lRemote = (IDecretoUnificazioneSige)lRef;
		return lRemote;
	}

  public static IProvvInterlocutoriSige getProvvInterlocutoriRemote() throws F3BException
  {
    Object lRef;
    IProvvInterlocutoriSige lRemote;
    lRef = lookup("siap.sige.provvInterlocutori.controller.ProvvInterlocutoriSigeController");
    lRemote = (IProvvInterlocutoriSige) lRef;
    return lRemote;
  }

  /**
   * Ritorna l'interfaccia del controller PosizioneMaterialeFascSige.
   * <p>
   * @return l'interfaccia del relativo controller.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static IPosizioneMaterialeFascSige getPosizioneMaterialeFascSigeRemote() throws F3BException
  {
    Object lRef;
    IPosizioneMaterialeFascSige lRemote;
    lRef = lookup("siap.sige.posizionematerialefascsige.controller.PosizioneMaterialeFascSigeController");
    lRemote = (IPosizioneMaterialeFascSige)lRef;
    return lRemote;
  }

  public static IAula getAulaRemote() throws F3BException
  {
    Object lRef;
    IAula lRemote;
    lRef = lookup("siap.sige.aula.controller.AulaController");
    lRemote = (IAula)lRef;
    return lRemote;
  }

  public static IPartiUdienza getPartiUdienzaRemote() throws F3BException
  {
    Object lRef;
    IPartiUdienza lRemote;
    lRef = lookup("siap.sige.udienzaparti.controller.PartiUdienzaController");
    lRemote = (IPartiUdienza)lRef;
    return lRemote;
  }

  /**
   * Ritorna l'interfaccia del controller TitoloEsecutivo.
   * <p>
   * @return l'interfaccia del relativo controller.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static ITitoloEsecutivo getTitoloEsecutivoRemote() throws F3BException
  {
    Object lRef;
    ITitoloEsecutivo lRemote;
    lRef = lookup("siap.sige.titoloesecutivo.controller.TitoloEsecutivoController");
    lRemote = (ITitoloEsecutivo)lRef;

    return lRemote;
  }

  /**
   * Ritorna l'interfaccia del controller della StatisticheSige.
   * <p>
   * @return l'interfaccia del relativo controller.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static IStatisticheSige getStatisticheSigeRemote() throws F3BException
  {
    Object lRef;

    IStatisticheSige lRemote;
    lRef = lookup("siap.sige.statistiche.controller.StatisticheSigeController");
    lRemote = (IStatisticheSige) lRef;

    return lRemote;
  }
  
  
  public static IDocumentoAllegato getDocumentoAllegatoController () throws F3BException{
      Object lRef;
      IDocumentoAllegato lRemote;
	  lRef = lookup("siap.sige.documentoallegato.controller.DocumentoAllegatoController");
	  lRemote = (IDocumentoAllegato ) lRef;
      return lRemote;
  }
  
  public static ICampoNota getCampoNotaController () throws F3BException{
      Object lRef;
      ICampoNota lRemote;
	  lRef = lookup("siap.sige.camponota.controller.CampoNotaController");
	  lRemote = (ICampoNota) lRef;
      return lRemote;
  }
  
}
