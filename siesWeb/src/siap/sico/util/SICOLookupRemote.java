package siap.sico.util;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
//import siap.sico.consolemagistrato.controller.IConsoleMagistrato;
import siap.sico.avvocato.controller.IAvvocato; // 19/03/2009 Rework per Normalizzazione COMBO dei FORI in tutte le fasi di gestione Avvocati.
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.certificato_omonimi_nsc.controller.ICertificatoOmonimiNsc;
import siap.sico.codici_sies_nsc.controller.ICodiciSiesNsc;
//import siap.sico.passaggioevento.controller.IPassaggioEvento;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.controller.IComuneProvincia;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.helponline.controller.IHelponline;
import siap.sico.jms.controller.IPresaInCarico;
import siap.sico.jms.controller.IRicercaSICOJMS;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.log.controller.ILogAttivita;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaBackupSrc;
import siap.sico.misuraalternativa.controller.IMisuraAlternativaIndultino;
import siap.sico.profilo.controller.IProfilo;
import siap.sico.provvedimentisiesnsc.controller.IProvvSiesNsc;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.security.controller.ISecurity;
import siap.sico.soggetto.controller.ISoggetto;
// Ambrosino
import siap.sico.soggetto.controller.ISoggettoFascicolo;
import siap.sico.soggettocertificato.controller.ISoggettoCertificato;
import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.stampa.controller.IStampa;
import siap.sico.storicosoggetto.controller.IStoricoSoggetto;
import siap.sico.template.controller.ITemplate;
import siap.sico.trasmissione.controller.ITrasmissioni;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.utente.controller.IUtente;
//Inserire gli import in testa a file
import siap.sico.w_magistrato.controller.IWMagistrato;
import siap.sico.webservice.controller.IWebServices;
import f3b.util.F3BException;
import f3b.util.LookupClass;

public class SICOLookupRemote extends LookupClass
{
  /**
   * @return
   * @throws F3BException
   */
  public static ISecurity getSecurityRemote() throws F3BException
  {
    Object lRef;
    ISecurity lRemote;

    lRef = lookup("siap.sico.security.controller.SecurityController");
    lRemote = (ISecurity) lRef;

    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static ISoggetto getSoggettoRemote() throws F3BException
  {
    Object lRef;
    ISoggetto lRemote;

    lRef = lookup("siap.sico.soggetto.controller.SoggettoController");
    lRemote = (ISoggetto) lRef;

    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static IComune getComuneRemote() throws F3BException
  {
    Object lRef;
    IComune lRemote;

    lRef = lookup("siap.sico.decodifiche.controller.ComuneController");
    lRemote = (IComune) lRef;

    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static IComuneProvincia getComuneProvinciaRemote() throws F3BException
  {
    Object lRef;
    IComuneProvincia lRemote;

    lRef = lookup("siap.sico.decodifiche.controller.ComuneProvinciaController");
    lRemote = (IComuneProvincia) lRef;

    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static IDecodifiche getDecodificheRemote() throws F3BException
  {
    Object lRef;
    IDecodifiche lRemote;

    lRef = lookup("siap.sico.decodifiche.controller.DecodificheController");
    lRemote = (IDecodifiche) lRef;

    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static IResidenza getResidenzaRemote() throws F3BException
  {
    Object lRef;
    IResidenza lRemote;

    lRef = lookup("siap.sico.residenza.controller.ResidenzaController");
    lRemote = (IResidenza) lRef;

    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static IUfficio getUfficioRemote() throws F3BException
  {
    Object lRef;
    IUfficio lRemote;

    lRef = lookup("siap.sico.ufficio.controller.UfficioController");
    lRemote = (IUfficio) lRef;

    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static IMagistrato getMagistratoRemote() throws F3BException
  {
    Object lRef;
    IMagistrato lRemote;
    lRef = lookup("siap.sico.magistrato.controller.MagistratoController");
    lRemote = (IMagistrato) lRef;
    return lRemote;
  }

  public static IMagistratoCompetente getMagistratoCompetenteRemote() throws F3BException
  {
    Object lRef;
    IMagistratoCompetente lRemote;
    lRef = lookup("siap.sico.magistratocompetente.controller.MagistratoCompetenteController");
    lRemote = (IMagistratoCompetente) lRef;
    return lRemote;
  }

  /**
   *
   * @return
   * @throws F3BException
   */
  public static IAssistenteGiudiziario getAssistenteGiudiziarioRemote() throws F3BException
  {
    Object lRef;
    IAssistenteGiudiziario lRemote;
    lRef = lookup("siap.sico.assistentegiudiziario.controller.AssistenteGiudiziarioController");
    lRemote = (IAssistenteGiudiziario) lRef;
    return lRemote;
  }

  /**
   * Restituisce il Template Controller
   * @return
   * @throws F3BException
   */
  public static ITemplate getTemplateRemote() throws F3BException
  {
    Object lRef;
    ITemplate lRemote;
    lRef = lookup("siap.sico.template.controller.TemplateController");
    lRemote = (ITemplate) lRef;
    return lRemote;
  }

  /**
   * Restuisce l'Evento
   * @return
   * @throws F3BException
   */
  public static IEvento getEventoRemote() throws F3BException
  {
    Object lRef;
    IEvento lRemote;

    lRef = lookup("siap.sico.evento.controller.EventoController");
    lRemote = (IEvento) lRef;

    return lRemote;
  }

  /**
   * Restuisce l'Evento
   * @return
   * @throws F3BException

  public static IPassaggioEvento getPassaggioEventoRemote() throws F3BException
  {
    Object lRef;
    IPassaggioEvento lRemote;

    lRef = lookup("siap.sico.passaggioevento.controller.PassaggioEventoController");
    lRemote = (IPassaggioEvento) lRef;

    return lRemote;
  }
  */

  /**
   * Restuisce la Stampa
   * @return
   * @throws F3BException
   */
  public static IStampa getStampaRemote() throws F3BException
  {
    Object lRef;
    IStampa lRemote;

    lRef = lookup("siap.sico.stampa.controller.StampaController");
    lRemote = (IStampa) lRef;

    return lRemote;
  }

  /**
   * Restuisce la Stampa per le Sanzioni Sostitutive
   * @return
   * @throws F3BException
   
  public static IStampa getStampaSSRemote() throws F3BException
  {
    Object lRef;
    IStampa lRemote;

    lRef = lookup("siap.sico.stampa.controller.StampaSSController");
    lRemote = (IStampa) lRef;

    return lRemote;
  }*/

  /**
   * @return
   * @throws F3BException
   */
  public static IWMagistrato getWMagistratoRemote() throws F3BException
  {
    Object lRef;
    IWMagistrato lRemote;
    lRef = lookup("siap.sico.w_magistrato.controller.WMagistratoController");
    lRemote = (IWMagistrato) lRef;
    return lRemote;
  }

  /**
   * @return
   * @throws F3BException
   */
  public static ICSSA getCSSARemote() throws F3BException
  {
    Object lRef;
    ICSSA lRemote;

    lRef = lookup("siap.sico.cssa.controller.CSSAController");
    lRemote = (ICSSA) lRef;

    return lRemote;
  }

  public static IMisuraAlternativa getMisuraAlternativaRemote() throws F3BException
  {
    Object lRef;
    IMisuraAlternativa lRemote;
    lRef = lookup("siap.sico.misuraalternativa.controller.MisuraAlternativaController");
    lRemote = (IMisuraAlternativa) lRef;
    return lRemote;
  }

  public static IRicercaSICOJMS getRicercaSICOJMSRemote() throws F3BException
  {
    Object lRef;
    IRicercaSICOJMS lRemote;
    lRef = lookup("siap.sico.jms.controller.RicercaSICOJMSController");
    lRemote = (IRicercaSICOJMS) lRef;
    return lRemote;
  }

  public static IProfilo getProfiloRemote() throws F3BException
  {
    Object lRef;
    IProfilo lRemote;
    lRef = lookup("siap.sico.profilo.controller.ProfiloController");
    lRemote = (IProfilo) lRef;
    return lRemote;
  }

  public static IUtente getUtenteRemote() throws F3BException
  {
    Object lRef;
    IUtente lRemote;
    lRef = lookup("siap.sico.utente.controller.UtenteController");
    lRemote = (IUtente) lRef;
    return lRemote;
  }

  public static ILogAttivita getLogAttivitaRemote() throws F3BException
                 {
                         Object lRef;
                         ILogAttivita lRemote;
                         lRef = lookup("siap.sico.log.controller.LogAttivitaController");
                         lRemote = (ILogAttivita)lRef;
                         return lRemote;
                 }

  /**
  * Restuisce l'interfaccia al Controller di
  * "Licenza Periodi Libertà Anticipata".
  * @return
  * @throws F3BException
  */
  public static ILicenzaPeriodiLibAnticipata getLicenzaPeriodiLibAntRemote()
       throws F3BException
  {
     Object lRef;
     ILicenzaPeriodiLibAnticipata lRemote;

     lRef = lookup("siap.sico.libertaanticipata.controller.LicenzaPeriodiLibAnticipataController");
     lRemote = (ILicenzaPeriodiLibAnticipata) lRef;

     return lRemote;
  }

  public static IMisuraAlternativaIndultino getMisuraAlternativaRemoteIndultino() throws F3BException
  {
    Object lRef;
    IMisuraAlternativaIndultino lRemote;
    lRef = lookup("siap.sico.misuraalternativa.controller.MisuraAlternativaIndultinoController");
    lRemote = (IMisuraAlternativaIndultino) lRef;
    return lRemote;
  }

  public static IEventoSimeone getEventoSimeoneRemote() throws F3BException
  {
    Object lRef;
    IEventoSimeone lRemote;
    lRef = lookup("siap.sico.evento.controller.EventoSimeoneController");
    lRemote = (IEventoSimeone) lRef;
    return lRemote;
  }

  public static IStoricoSoggetto getStoricoSoggettoRemote() throws F3BException
  {
    Object lRef;
    IStoricoSoggetto lRemote;
    lRef = lookup("siap.sico.storicosoggetto.controller.StoricoSoggettoController");
    lRemote = (IStoricoSoggetto)lRef;
    return lRemote;
  }

  public static ICampoNota getCampoNotaRemote() throws F3BException
  {
      Object lRef;
      ICampoNota lRemote;
      lRef = lookup("siap.sico.camponota.controller.CampoNotaController");
      lRemote = (ICampoNota)lRef;
      return lRemote;
  }

  public static IMisuraAlternativaBackupSrc getMisuraAlternativaBackupSrcRemote() throws F3BException
  {
    Object lRef;
    IMisuraAlternativaBackupSrc lRemote;
    lRef = lookup("siap.sico.misuraalternativa.controller.MisuraAlternativaControllerBackupSrc");
    lRemote = (IMisuraAlternativaBackupSrc)lRef;
    return lRemote;
  }

  public static IPresaInCarico getPresaInCaricoRemote() throws F3BException
  {
    Object lRef;
    IPresaInCarico lRemote;
    lRef = lookup("siap.sico.jms.controller.PresaInCaricoController");
    lRemote = (IPresaInCarico)lRef;
    return lRemote;
  }
 
  /*****************************************************************************
   * Istanzia e restituisce l'interfaccia del controller HelponlineController
   * @return Un'istanza dell'interfaccia del controller HelponlineController
   * @throws F3BException
   ****************************************************************************/
  public static IHelponline getHelponlineRemote() throws F3BException {
    Object lRef;
    IHelponline lRemote;
    lRef = lookup("siap.sico.helponline.controller.HelponlineController");
    lRemote = (IHelponline)lRef;
    return lRemote;
  }
 
  public static IWebServices getWebServicesRemote() throws F3BException {
    Object lRef;
    IWebServices lRemote;
    lRef = lookup("siap.sico.webservice.controller.WebServicesController");
    lRemote = (IWebServices)lRef;
    return lRemote;
  }
  
  /*****************************************************************************
   * Istanzia e restituisce l'interfaccia del controller TrasmissioniController
   * @return Un'istanza dell'interfaccia del controller TrasmissioniController
   * @throws F3BException
   ****************************************************************************/
  public static ITrasmissioni getTrasmissioniRemote() throws F3BException {
    Object lRef;
    ITrasmissioni lRemote;
    lRef = lookup("siap.sico.trasmissione.controller.TrasmissioniController");
    lRemote = (ITrasmissioni)lRef;
    return lRemote;
  }
  
  /*****************************************************************************
   * Istanzia e restituisce l'interfaccia del controller CodiciSiesNscController
   * @return Un'istanza dell'interfaccia del controller CodiciSiesNscController
   * @throws F3BException
   ****************************************************************************/
  public static ICodiciSiesNsc getCodiciSiesNscRemote() throws F3BException {
    Object lRef;
    ICodiciSiesNsc lRemote;
    lRef = lookup("siap.sico.codici_sies_nsc.controller.CodiciSiesNscController");
    lRemote = (ICodiciSiesNsc)lRef;
    return lRemote;
  }
  
  /*****************************************************************************
   * Istanzia e restituisce l'interfaccia del controller Certificato_Omonimi_NscController
   * @return Un'istanza dell'interfaccia del controller Certificato_Omonimi_NscController
   * @throws F3BException
   ****************************************************************************/
  public static ICertificatoOmonimiNsc getCertificatoOmonimiNscRemote() throws F3BException {
    Object lRef;
    ICertificatoOmonimiNsc lRemote;
    lRef = lookup("siap.sico.certificato_omonimi_nsc.controller.CertificatoOmonimiNscController");
    lRemote = (ICertificatoOmonimiNsc)lRef;
    return lRemote;
  }
 
  /*****************************************************************************
   * Istanzia e restituisce l'interfaccia del controller SoggettoCertificatoController
   * @return Un'istanza dell'interfaccia del controller SoggettoCertificatoController
   * @throws F3BException
   ****************************************************************************/
  public static ISoggettoCertificato getSoggettoCertificatoRemote() throws F3BException {
    Object lRef;
    ISoggettoCertificato lRemote;
    lRef = lookup("siap.sico.soggettocertificato.controller.SoggettoCertificatoController");
    lRemote = (ISoggettoCertificato)lRef;
    return lRemote;
  }
  
  /*****************************************************************************
   * Istanzia e restituisce l'interfaccia del controller ProvvSiesNscController
   * @return Un'istanza dell'interfaccia del controller ProvvSiesNscController
   * @throws F3BException
   ****************************************************************************/
  public static IProvvSiesNsc getProvvSiesNscRemote() throws F3BException {
    Object lRef;
    IProvvSiesNsc lRemote;
    lRef = lookup("siap.sico.provvedimentisiesnsc.controller.ProvvSiesNscController");
    lRemote = (IProvvSiesNsc)lRef;
    return lRemote;
  }

  /**
   * Ritorna l'interfaccia del controller Avvocato.
   * <p>
   * @return l'interfaccia del relativo controller.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static IAvvocato getAvvocatoRemote() throws F3BException
  {
    Object lRef;
    IAvvocato lRemote;
    lRef = lookup("siap.sico.avvocato.controller.AvvocatoController");
    lRemote = (IAvvocato)lRef;

    return lRemote;
  }
  
  /**
   * getConsoleMagistratoRemote
   * @return IConsoleMagistrato
   * @throws F3BException
   */
  /* public static IConsoleMagistrato getConsoleMagistratoRemote() throws F3BException {
    Object lRef;
    IConsoleMagistrato lRemote;
    lRef = lookup("siap.sico.consolemagistrato.controller.ConsoleMagistratoController");
    lRemote = (IConsoleMagistrato)lRef;
    return lRemote;
  }  */

// Ambrosino 03/2010  
  /**
   * @return
   * @throws F3BException
   */
  public static ISoggettoFascicolo getSoggettoFascicoloRemote() throws F3BException
  {
    Object lRef;
    ISoggettoFascicolo lRemote;

    lRef = lookup("siap.sico.soggetto.controller.SoggettoFascicoloController");
    lRemote = (ISoggettoFascicolo) lRef;

    return lRemote;
  }

  public static ISoggettoDattilo getSoggettoDattiloRemote() throws F3BException
  {
    Object lRef;
    ISoggettoDattilo lRemote;

    lRef = lookup("siap.sico.soggettodattilo.controller.SoggettoDattiloController");
    lRemote = (ISoggettoDattilo)lRef;

    return lRemote;
  }

}
