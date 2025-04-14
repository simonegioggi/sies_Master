<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Vector"%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B"%>


<%@ page import="siap.sige.util.SIGELookupRemote"%>
<%@ page import="siap.sige.avvocato.controller.IAvvocato"%>

<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel"%>
<%@ page import="siap.sico.avvocato.model.AvvocatoModel"%>

<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="TornaQui"            scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>

<%
//Fascicolo SIGE
FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
//String lDataUdienza = (FascicoloSigeEsteso.getUdienzaProcedimento() != null && FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige() != null ) ? StringUtils.toStringJSP(DateUtils.getDateToString(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy")) : "-";
String lDataUdienza = null;
if (FascicoloSigeEsteso.getUdienzaProcedimento()!=null && FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige()!=null)
{
  lDataUdienza = DateUtils.getDateToString(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy");
}else{
  lDataUdienza = "-";
}
  
// presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  
  //========================================== 
  // MEV29 - 07/2015 - Test su Foro Avvocato
  //========================================== 
  // Se fascicolo modificabile, verifico se gli avvocati associti al procedimento
  // appartengono a fori soppressi
  boolean isFascicoloModificabile = true;
/*
  if (   fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01") == 0
      || fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("04") == 0
      || fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05") == 0
      || fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("99") == 0
      || UtenteConnesso.getUfficioUtente().getCodUfficio().compareTo(fascicoloSiusGP.getFascicoloSiusModel().getChiaveUfficio()) !=0
     )
  {
    isFascicoloModificabile = false;  
  }
*/  
  boolean isAvvocatoForoSoppresso = false;
  String strAlertAvvocato = "";
 
  boolean isAvvocatoForoRegInde = false;
  String strAlertAvvocatoReginde = "";

  
  if (isFascicoloModificabile){
    Vector listaAvvocati = null;
    try { 
      IAvvocato lAvvCtrl = SIGELookupRemote.getAvvocatoRemote();
      listaAvvocati = new Vector(   lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(lFascicolo.getIdFascicoloSige()));
      
      Collection listaFori = DecodificheManager.getInstance().getForoAll();
      
      int contaSoppressi = 0;
      int contaRegInde = 0;  
      if (listaAvvocati!=null){
        for (int i=0; i<listaAvvocati.size();i++) {
         
          AvvocatoSigeModel lAvvocatoSige = (AvvocatoSigeModel) listaAvvocati.elementAt(i);
          AvvocatoModel lAvvocatoModel = lAvvocatoSige.getAvvocato();
          
          String lStatoForo = DecodificheUtils.getCodAltebyCode(listaFori, lAvvocatoModel.getForo());
    
          if ("SOPPRESSO".equals(lStatoForo)){
            isAvvocatoForoSoppresso = true;
            contaSoppressi++;
            
            strAlertAvvocato+= " L’Avvocato "+StringUtils.toStringJSP(lAvvocatoModel.getCognome())+" "
                               +StringUtils.toStringJSP(lAvvocatoModel.getNome())
                               +" risulta iscritto al Foro di "
                               +StringUtils.toStringJSP(lAvvocatoModel.getForo())
                               +" soppresso a seguito dell’accorpamento degli uffici giudiziari. ";
          }
          
          // INIZIO: MEV_21 (avvocati)
          if ("NO".equals(lAvvocatoModel.getFlagRegInde())) {
             isAvvocatoForoRegInde = true;
             contaRegInde++;
             strAlertAvvocatoReginde+= " L'anagrafica dell'Avvocato "+StringUtils.toStringJSP(lAvvocatoModel.getCognome())+" "
                               +StringUtils.toStringJSP(lAvvocatoModel.getNome())
                               +" non risulta certificata su RegInde. "
                               +"  ";
           
          }
          // FINE: MEV_21           
          
        }
      }
  
      if (isAvvocatoForoSoppresso){
        if (contaSoppressi==1)
          strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
        else
          strAlertAvvocato += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
      } 
      
      // INIZIO: MEV_21 (avvocati)
      if (isAvvocatoForoRegInde){
        if (contaRegInde==1)
          strAlertAvvocatoReginde += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati dell'Avvocato utilizzando le opportune funzioni.";
        else
          strAlertAvvocatoReginde += "Prima di procedere con l'emissione di nuovi provvedimenti è necessario provvedere ad aggiornare i dati degli Avvocati utilizzando le opportune funzioni.";
      }      
        
    }catch(Exception e){
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.error("Exception",e);
    } 
  }  
  
  
  
  
%>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Procedimento N.</font>
         <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>=<%=lFascicolo.getIdFascicoloSige()%><%=retParam%>">
           <%=lFascicolo.getChiaveAnno() %>/<%=lFascicolo.getChiaveProgr()%>
        </a>
           <font class="campo"> &nbsp;&nbsp;<%=lFascicolo.getDescrUfficio()%> </font>
       </td>
    </tr>
    <tr>
      <td class="L"><font class="label">Soggetto: </font>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_INCLUDE_SOGGETTO%>"/>
      </td>
    </tr>

    <tr>
      <td class="L">
        <font class="label">Data Udienza : <%=lDataUdienza%> </font>
      </td>
    </tr>


    <%
    //==========================================================================
    // Test per segnalare che uno o più avvocati associati al Procedimento risultano
    // iscritti a Fori Soppressi.
    //==========================================================================
    %>    
    <% 
    if (isAvvocatoForoSoppresso || isAvvocatoForoRegInde) { 
    %>
    <script type="text/javascript">
      function blink() {
        var blinks = document.getElementsByTagName('blink');
        
        for (var i = blinks.length - 1; i >= 0; i--) {
          var s = blinks[i];
          s.style.visibility = (s.style.visibility === 'visible') ? 'hidden' : 'visible';
        }
        window.setTimeout(blink, 500);  
      }
      if (document.addEventListener) document.addEventListener("DOMContentLoaded", blink, false);
      else if (window.addEventListener) window.addEventListener("load", blink, false);
      else if (window.attachEvent) window.attachEvent("onload", blink);
      else window.onload = blink;
    </script>    
    
    <% if (isAvvocatoForoSoppresso) { %>
    <tr>
      <td class="cRosso">
        <blink>Attenzione!!</blink> <%=strAlertAvvocato%>
      </td>
    </tr>
    <% } %> 
        
    <% if (isAvvocatoForoRegInde) {  // INIZIO: MEV_21 (avvocati) %>
    <tr>
      <td class="cRosso">
        <blink>Attenzione!!</blink> <%=strAlertAvvocatoReginde%>
      </td>
    </tr>
    <% } // FINE: MEV_21 %>    
    <% } %>  
    
  </table>
 <br>  