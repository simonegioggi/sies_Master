package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.rateizzazionepp.controller.IRateizzazionePP;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciOrdineIngiunzione extends ActionSiap implements ICostantiSanzioneSostitutiva 
{
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
    public String processRequest() throws Exception {

        FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

        // Controlli preliminari allìinserimento di un nuovo evento 
        if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
            RedirectTo lRedirigi = new RedirectTo();
            lRedirigi.setPage(IWebConstants.PG_MAIN);
            setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
                    + "/" + lFascMod.getChiaveProgr()
                    + " non è stato Validato. Impossibile inserire un ordine d'esecuzione con sospensione!");
            lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
                    + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
            setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

            return IWebConstants.PG_MESSAGE;
        }

        isFascicoloSiepDiCompetenza();

        if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
            RedirectTo lRedirigi = new RedirectTo();
            lRedirigi.setPage(IWebConstants.PG_MAIN);
            setRequestAttribute(IWebConstants.MESSAGE_TEXT,
                    "Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
                            + " Il fascicolo risulta Definito. Impossibile procedere!");
            lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
                    + ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
            setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
            return IWebConstants.PG_MESSAGE;
        }

        this.isEventoNonValidato();
        //=======================================
        
        // Ricerca i pagamenti per id Fascicolo 
        Vector <RateizzazionePPModel> listaRateizzazioni = new Vector <RateizzazionePPModel>();
        IRateizzazionePP lRateCTRL = SIEPLookupRemote.getRateizzazionePPRemote();
        listaRateizzazioni = lRateCTRL.exRicercaRateizzazioniByIdFasc(lFascMod.getIdFascicoloSiep());
        
        if (listaRateizzazioni.size()==0) {
            throw new F3BException(F3BException.USER_MESSAGE,
              "Non e' stato inserito un metodo di pagamento: unica rata o rateizzazione. Impossibile procedere");
        }        
        setRequestAttribute("listaRateizzazioni", listaRateizzazioni);
        
        // Posizione giuridica
        PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
        IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
        lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo (lFascMod.getIdFascicoloSiep());
        setRequestAttribute("posizioneluogoaltra", lPos);
        
        // Ricerco il civilmente Obbligato se esiste
        ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
        Vector<CivilmenteObbligatoModel> coms = ico
                .ExRicercaCivilmenteObbligatiByFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
        setRequestAttribute("civilmenteObbligati", coms);
        
        // Magistrato
        IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
        MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
                .ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
        setRequestAttribute("magistrato", lMagi);
        
        // Avvocati
        IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
        Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
        setRequestAttribute("avvocati", lAvvocati);        
        
        
        // Autorità esterna
        Option lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
        // Verifico se sovrescrivere l'auturità esterna
        if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
            // modifica relativa al tipo istituto
            if (  lPos.getAltraCausa() != null
               && (    lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")
                    || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
                    || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
                    || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
                    || lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) 
            {
                lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
            } else {
                if (lPos.getAltraCausa() != null  && lPos.getAltraCausa().getIstitutoDetenzione() != null)
                    lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
                            lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
            }
        } else {
            if (   lPos.getPosizioneGiuridica().isLibero()
                || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
                || lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) 
            {
                lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
            } else {
                if (lPos.getLuogoDetenzione() != null && lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
                    lOptionAutoritaEsternaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
                            lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
            }
        }
        lOptionAutoritaEsternaE.setSelected("-");
        setRequestAttribute("autoritaEsternaE", "" + lOptionAutoritaEsternaE);
        
        
        // Autorita Notifica Avvocato 
        Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "C0");
        setRequestAttribute("autoritaEsternaN", "" + lOption);
        
        
        // Autorita Notifica Civilmente Obbligati
        Option lOptCivilObb = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
        setRequestAttribute("autoritaEsternaCivilObb", "" + lOptCivilObb);
        
        return PG_LOAD_INSERISCI_ORDINE_INGIUNZIONE;
    }
    
}
