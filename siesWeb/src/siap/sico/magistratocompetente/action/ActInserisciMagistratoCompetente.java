package siap.sico.magistratocompetente.action;


/**
* <p>Title: ActInserisciMagistratoCompetente</p>
* <p>Description: Classe Action per l'inserimento di MagistratoCompetente</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.magistratocompetente.model.MagistratoCompetenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;



public class ActInserisciMagistratoCompetente extends ActionSiap implements ICostantiMagistratoCompetente
{
/**
* Azione di Inserimento del MagistratoCompetente
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws F3BException
{
   //generale
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
    MagistratoCompetenteMagistratoModel lMagistrato = new MagistratoCompetenteMagistratoModel();

    //magistrato nuovo
    //lMagistrato.getMagistrato().setCognome(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME));
    //lMagistrato.getMagistrato().setNome(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME));
    //magistrato vecchio
    lMagistrato.getMagistrato().setCodMagistrato(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO));

    lMagistrato.getMagistratoCompetente().setMagCodMagistrato(this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
    lMagistrato.getMagistratoCompetente().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
    lMagistrato.getMagistratoCompetente().setDataInizio( this.getRequestDateParameter(ICostantiMagistratoCompetente.CAMPO_ANNO_DATA_INIZIO,ICostantiMagistratoCompetente.CAMPO_MESE_DATA_INIZIO,ICostantiMagistratoCompetente.CAMPO_GIORNO_DATA_INIZIO));
    lMagistrato.getMagistratoCompetente().setCodRuoloMagistrato("01");
    lMagistrato.getMagistratoCompetente().setDataInserimento(DateUtils.getSysDate());
    lMagistrato.getMagistratoCompetente().setCodOperatoreInserimento(this.getCodUtenteConnesso());
    lMagistrato.getMagistratoCompetente().setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

   //---Aggiungere in SICOLookupRemote il metodo getMagistratoCompetenteRemote()
    IMagistratoCompetente lCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
    MagistratoCompetenteModel llMagModRet = lCtrl.ExInserisciAggiornaMagistratoCompetente(lMagistrato);

  // setta la risposta nella request
    setRequestAttribute("magistratoprecedente",this.getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO));
    setRequestAttribute("magistratocompetente", llMagModRet);


    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.magistratocompetente.action.ActLoadDettaglioMagistratoCompetente&"+CAMPO_MAG_COD_MAGISTRATO+"="+llMagModRet.getMagCodMagistrato().toString();
    return lPage;
}



}
