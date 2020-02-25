<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.sico.cssa.model.CSSAModel"%>

<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="verbale"            scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="vedoDataIntermedia" scope="request" class="java.lang.String" />
<jsp:useBean id="cssa"               scope="request" class="siap.sico.cssa.model.CSSAModel" />
<jsp:useBean id="istitutodetenzione" scope="request" class="siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel" />
<jsp:useBean id="misuraalternativa"  scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel" />
<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="camponota"          scope="request" class="siap.sico.camponota.model.CampoNotaModel"/>

<jsp:useBean id="lTotGiorniConcessi"  scope="request" class="java.lang.String"/>
<jsp:useBean id="lTotGiorniRDConcessi"  scope="request" class="java.lang.String"/>

<jsp:useBean id="DettaglioDaElenco" scope="request" class="java.lang.String" />
<jsp:useBean id="dettaglioProvvedimento" scope="request" class="java.lang.String" />

<html>
<head>
<title>[S.I.E.S.] - Dettaglio  </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
<script language="JavaScript" src="/html/conferma.js"></script>
<script language="JavaScript">
  function Verify()
  {
      if   (document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.GPV.value!="" && document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.MPV.value!="" && document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.APV.value!="")
      {
        if (document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.GPV.value.length==1)
          document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.GPV.value='0'+document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.GPV.value;
        if (document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.MPV.value.length==1)
          document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.MPV.value='0'+document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.MPV.value;
  
        var data_to_verify = document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.GPV.value+'/'+document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.MPV.value+'/'+document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.APV.value;
          if (data_to_verify.length>4)
          {
           if (!ControllaData(data_to_verify) )
           {
                  alert('Data di Decorrenza non valida');
                  return false;
           }
          }
      }
      Esegui();
  }

  function Esegui()
  {
      document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.conferma.disabled=true;
     // document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.vai.disabled=true;
      document.DettaglioPenaResiduaVariazioneVerbaleSottoscrizione.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.verbale.action.ActRegistraPenaVariazioneVerbaleSottoscrizione";
  }

 </script>
</head>

<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Variazione Data Inizio Misura</font>
      </td>
   </tr>

 </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
</FORM>
<form name="DettaglioPenaResiduaVariazioneVerbaleSottoscrizione" method="POST" action="/jsp/Main.jsp">
    <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa() %>">
<% if(verbale.getIdVerbale() != null)
  { %>   
      <input type="HIDDEN" name="<%=ICostantiVerbale.CAMPO_ID_VERBALE%>" value="<%=verbale.getIdVerbale()%>">
<% 
  }%> 
    <table cellspacing=4 cellpadding=4>

      <tr>
      
          <%if(misuraalternativa.getCodTipoMisura().equals("0001") || misuraalternativa.getCodTipoMisura().equals("0002") || misuraalternativa.getCodTipoMisura().equals("0003"))
             {%>
              <td class="l" colspan=2>Concessione Affidamento in Prova</td>
           <%}else
             if(misuraalternativa.getCodTipoMisura().equals("0005") || misuraalternativa.getCodTipoMisura().equals("0010") || misuraalternativa.getCodTipoMisura().equals("0013"))
             {%>
              <td class="l" colspan=2>Concessione Detenzione Domiciliare</td>
           <%}else
             if(misuraalternativa.getCodTipoMisura().equals("0004"))
             {%>
              <td class="l" colspan=2>Concessione Semilibertà</td>
           <%}
             else
             if(misuraalternativa.getCodTipoMisura().equals("2245"))
             {%>
              <td class="l" colspan=2>Concessione Sospensione Condizionata esecuzione parte finale pena detentiva</td>
           <%}
             else
               if(misuraalternativa.getCodTipoMisura().equals("2005"))
               {%>
                <td class="l" colspan=2>Ammissione Provvisoria a Detenzione Domiciliare</td>
             <%}
             else
             if(misuraalternativa.getCodTipoMisura().equals("2006") || misuraalternativa.getCodTipoMisura().equals("2008")
               )
             {%>
                  <td class="l" colspan=2>Ammissione Provvisoria ad Affidamento in Prova</td>
           <%}
             else              
                if(misuraalternativa.getCodTipoMisura().equals("0011"))
                {%>
                  <td class="l" colspan=2>Concessione Detenzione Domiciliare a Termine</td>
           <%   }%>
      </tr>

<%
  if(verbale != null && verbale.getCssIdCssa()!= null && verbale.getCssIdCssa().compareTo(new BigDecimal(0))!=0)
  {%>
    <tr>
      <td class="l">Data Pervenimento del Verbale</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataPervenimento(),"dd-MM-yyyy"))%> </font></td>
    </tr>
    <tr>
      <td class="l">Data Sottoscrizione Prescrizioni</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
    </tr>
      <tr>
      <td class="l">UEPE Competente che ha inviato il verbale</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(cssa.getComune())+ " "+StringUtils.toStringJSP(cssa.getIndirizzo()) %></font></td>
    </tr>
<%  
} 

  if(verbale != null && verbale.getIstDetIdIstitutoDetenzione()!= null && !verbale.getIstDetIdIstitutoDetenzione().equals("-"))
  {%>
    <tr>
        <td class="l">Data Ingresso in istituto</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
    </tr>
    <tr>
        <td class="l">Istituto Competente che ha inviato il verbale</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(istitutodetenzione.getDescrTipoIstituto())+" di "+StringUtils.toStringJSP(istitutodetenzione.getDescrComune()) %></font></td>
    </tr>
<%}%>

<!------------------------------  DATI RELATIVI AL VERBALE  ----------------------------------->  

<%
if(verbale.getIdVerbale() != null)
{ 
  if(verbale != null && verbale.getCodTipoUfficioFirmatario() != null
      && !verbale.getCodTipoUfficioFirmatario().equals("-") && verbale.getCodLuogoUfficioFirmatario()!= null
      && !verbale.getCodLuogoUfficioFirmatario().equals("-"))
  {%>
    <tr>
        <td class="l">Data Sottoposizione agli obblighi</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
    </tr>
    <tr>
        <td class="l">Autorità Competente che ha inviato il verbale</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getDescrTipoUfficioFirmatario())+ " di "+StringUtils.toStringJSP(verbale.getDescrLuogoUfficioFirmatario()) %></font></td>
    </tr>
    <tr>
        <td class="l">Indirizzo</td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(verbale.getNote())%>&nbsp;</font></td>
      </tr>
    
<%  }
} 
%>
    
<!------------------------------  VARIAZIONE DATA INIZIO MISURA   ----------------------------------->  
     <tr>
        <td class="Titolo" colspan=10><font  class="label">Variazione Data Inizio Misura</font></td>
     </tr>

      <tr>
        <td class="l" >Data Pervenimento Richiesta Variazione </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getDataEmissione(),"dd-MM-yyyy"))%> </font></td>
      </tr>

    <tr>
      <td class="l">Motivazioni</td>
      <td class="L" colspan="3">
        <font class="campo"><%=StringUtils.toStringJSP(camponota.getDescr())%> </font>
      </td>
    </tr> 
    
    <tr>
        <td class="l" >Nuova Data Inizio Misura </td>
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataInizioMisura(),"dd-MM-yyyy"))%></font></td>
    </tr> 
    
<!---------------------------------------FINE PENA--------------------------------------------------------------->

</table>
<table>

     <tr>
        <td class="Titolo" colspan="15"><font class="label">Pena da Espiare</font></td>
   </tr>

<%
 if(penaresidua.getFlagErgastolo().equals("N"))
 {%>
        <tr>
            <td class="l"><font class="label">Reclusione : </font></td>
            
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=penaresidua.getNumAnniReclusione()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=penaresidua.getNumMesiReclusione()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=penaresidua.getNumGiorniReclusione()%></font></td>

            <td class="l"><font class="label">Arresto :</font></td>
            <td class="l"><font class="label">Anni</font></td>
            <td class="r"><font class="campo"><%=penaresidua.getNumAnniArresto()%></font></td>
            <td class="l"><font class="label">Mesi</font></td>
            <td class="r"><font class="campo"><%=penaresidua.getNumMesiArresto()%></font></td>
            <td class="l"><font class="label">Giorni</font></td>
            <td class="r"><font class="campo"><%=penaresidua.getNumGiorniArresto()%></font></td>
      </tr>

<%}
  else
  {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {%>
             <tr>
              <td class="l"><font class="label">Reclusione : </font></td>
            <td class="l"><font class="campo">ERGASTOLO</font></td>
          </tr>
      <%}
        else if(penaresidua.getFlagErgastolo().equals("D"))
        {%>
          <tr>
              <td class="l"><font class="label">Reclusione : </font></td>
            <td class="l"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO</font></td>
          </tr>
      <%}%>
      

 <%}%>
</table>

<table>

  <%if(penaresidua.getDataInizio() != null)
  {%>
        <tr>
          <td class="l"><font class="label">Data Decorrenza Pena: </font></td>
          <td class="l"><font class="campo">
              <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>
          </font></td>
<%
  }
  if (vedoDataIntermedia.equals("S"))
  { %>
        <td class="l"><font  class="label">Data Fine Reclusione : </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFineReclusione(),"dd-MM-yyyy"))%>
          </font></td>

    </tr>

    <tr>
        <td class="l"><font  class="label">Data Inizio Arresto : </font></td>
         <td class="l"> <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizioArresto(),"dd-MM-yyyy"))%>
          </font></td>
 <% } %>

<%
  if(penaresidua.getFlagErgastolo().equals("N"))
  {
         if(penaresidua.getDataFinePresunta() != null)
         {
%>
              <td class="l"><font  class="label">Data Fine Pena Automatica : </font></td>
                <td class="l">
                  <font class="campo">
                    <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%>
                  </font>
                </td>
              </tr>
  <% 
              if(dettaglioProvvedimento!=null && dettaglioProvvedimento.equals("SI"))
              {
       %>
               <tr>
                <td colspan=4><font  class="label"></font>
                   <input type="hidden" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(penaresidua.getDataFinePresunta())%>">
                    <input type="hidden" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(penaresidua.getDataFinePresunta())%>">
                    <input  type="hidden" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(penaresidua.getDataFinePresunta())%>">
                </td>
              </tr>
       <%       }
              else
              {
      %>
                <tr>
                <td class="l" colspan="4"><font  class="label">Data Fine Pena Manuale : </font>
                   <input type="text" name="GPV" maxlength="2" size="2" value="<%=DateUtils.getDayToString(penaresidua.getDataFinePresunta())%>">
                    /
                    <input type="text" name="MPV" maxlength="2" size="2" value="<%=DateUtils.getMonthToString(penaresidua.getDataFinePresunta())%>">
                    /
                    <input  type="text" name="APV" maxlength="4" size="4" value="<%=DateUtils.getYearToString(penaresidua.getDataFinePresunta())%>">
                    &nbsp;&nbsp;&nbsp;
                </td>
              </tr>
    <%      }
    
    
            if( lTotGiorniConcessi!=null && !"0".equals(lTotGiorniConcessi) )
            {
    %>
                <tr>
                  <td class="l">Giorni di Lib. ant. concessi</td>
                  <td class="l">
                    <font class="campo"> <%=StringUtils.toStringJSP(lTotGiorniConcessi)%></font>
                  </td>
                </tr>
    <%
                } %>

    <% if( lTotGiorniRDConcessi!=null && lTotGiorniRDConcessi.length()>0 && Integer.parseInt(lTotGiorniRDConcessi)!=0 ) {%>
        <tr>
          <td class="l">Giorni di Risarcimento D.L. 92/2014 concessi</td>
          <td class="l">
            <font class="campo"> <%=StringUtils.toStringJSP(lTotGiorniRDConcessi)%></font>
          </td>
        </tr>
    <% } %>
                
          <%      if(!(dettaglioProvvedimento!=null && dettaglioProvvedimento.equals("SI")))
                {
          %>        <tr>
                      <td colspan="2"><br><INPUT class="bottone" type="submit" name="conferma" value="Validazione Fine Pena"></td>
                </tr>
          <%  
                }
        } // chiude if(penaresidua.getDataFinePresunta() != null)
  
  }  // chiude if(penaresidua.getFlagErgastolo().equals("N"))
%>
    </table>
   
<!-----------------------------------------FINE PENA------------------------------------------------------------->
  
     <input type="hidden" name="idpenaresidua" value="<%=penaresidua.getIdPenaResidua()%>">
     <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
     <input type="hidden" name="EventoId"            value="<%=evento.getIdEvento()%>">

  </form>
  </body>

<%
  if(penaresidua.getFlagErgastolo().equals("N"))
   {

       if(penaresidua.getDataFinePresunta() != null)
       {
  %>
          <script language="JavaScript" type="text/javascript">
              var frmvalidator  = new Validator("DettaglioPenaResiduaVariazioneVerbaleSottoscrizione");
              
              frmvalidator.addValidation("GPV","maxlen=2","La lunghezza massima per il Giorno Pena Validata è di 2 caratteri");
              frmvalidator.addValidation("GPV","numeric","Il campo Giorno Pena Validata deve essere numerico");
              frmvalidator.addValidation("GPV","gt=1","Il campo Giorno Pena Validata deve essere maggiore di 0");
              frmvalidator.addValidation("GPV","lt=31","Il campo Giorno Pena Validata deve essere minore di 31");
              frmvalidator.addValidation("MPV","maxlen=2","La lunghezza massima per il Mese Pena Validata è di 2 caratteri");
              frmvalidator.addValidation("MPV","numeric","Il campo Mese Pena Validata deve essere numerico");
              frmvalidator.addValidation("MPV","gt=1","Il campo Mese Pena Validata deve essere maggiore di 0");
              frmvalidator.addValidation("MPV","lt=12","Il campo Mese Pena Validata deve essere minore di 12");
              frmvalidator.addValidation("APV","maxlen=4","La lunghezza massima per l'Anno Pena Validata è di 4 caratteri");
              frmvalidator.addValidation("APV","minlen=4","La lunghezza minima per l'Anno Pena Validata è di 4 caratteri");
              frmvalidator.addValidation("APV","numeric","Il campo Anno Pena Validata deve essere numerico");
              frmvalidator.addValidation("APV","gt=1900","Il campo Anno Pena Validata deve essere maggiore di 1900");
              frmvalidator.addValidation("APV","lt=2100","Il campo Anno Pena Validata deve essere minore di 2100");
      
              frmvalidator.setAddnlValidationFunction("Verify");
          </script>
  <%
      }
}
%>

</html>