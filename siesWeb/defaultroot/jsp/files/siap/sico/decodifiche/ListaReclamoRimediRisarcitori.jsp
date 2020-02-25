<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.evento.model.EventoLicenzePeriodiModel" %>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="listaEventiLicenze" scope="request" class="java.util.Vector" />

<%
//==============================================================================
// JSP per la viisualizzazione della lista dei provvedimenti (decreti/ordinanze) 
// di concessione dei "Reclamo Rimedi Risarcitori" DL 92/2014
//==============================================================================
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Provvedimenti DL92</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function Verify(idEvento, elaborato)
      {
        if( elaborato=='S' )
        {
          alert("Provvedimento già elaborato");
        }
        else
        {
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=IWebConstants.ACTION_FIELD%>.value = "siap.sico.libertaanticipata.action.ActLoadInserisciReclamo35Ter";
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_ID_EVENTO%>.value=idEvento;

          window.parent.opener.document.<%=request.getParameter("formname")%>.bottConferma.disabled=true;
          window.parent.opener.document.<%=request.getParameter("formname")%>.submit();

          window.parent.close();
        }
      }

      function controlla()
      {
        if(document.elenco.numeroLiberazioni.value==0)
        {
          alert("Nessuna Provvedimento Presente");

          window.parent.close();
        }
      }

    </script>

  </head>

<body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
    <input type="hidden" name="numeroLiberazioni" value="<%=listaEventiLicenze.size()%>">
    <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a>
        </td>
        <td class="LBG">
          <font class=label>Funzione :</font>
          <font class=campo>Elenco Provvedimenti D.L. 92/2014</font>
        </td>
      </tr>
    </table>
    
    <br>
    
    <table>
      <tr>
        <td class="int">Descrizione</td>
        <td class="int">Anno Sius</td>
        <td class="int">Numero Sius</td>
        <td class="int">Autorità Emittente</td>
        <td class="int">Data Emissione Provvedimento</td>
        <td class="int" width=5%>Azioni</td>
      </tr>
<%
  if(listaEventiLicenze.size()>0)
  {
    Iterator iterEventiLic = listaEventiLicenze.iterator();
    
    while (iterEventiLic.hasNext()){
      EventoLicenzePeriodiModel lEveLicModel = (EventoLicenzePeriodiModel)iterEventiLic.next();
      
      EventoModel lEveSorvModel = lEveLicModel.getEvento();
      EventoModel lEveEsecModel = lEveLicModel.getEventoCollegato();
      
      Vector<LicenzaLibAnticipataModel> lListaLicenze = lEveLicModel.getListaLicenze();
      
      BigDecimal ggConcessi = null;
      BigDecimal sommaLiquidata = null;
       
      LicenzaLibAnticipataModel lLicAppoModel = null;
      Iterator iterLicenze = lListaLicenze.iterator();
      while (iterLicenze.hasNext()){
        LicenzaLibAnticipataModel lLicenza = (LicenzaLibAnticipataModel)iterLicenze.next();
        lLicAppoModel = lLicenza;
        
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        //siesLogger.debug("lLicenza = "+lLicenza);
      
        if ("RD".equals(lLicenza.getCodTipoLicenza()) && "C".equals(lLicenza.getFlagConcesso()))
          ggConcessi = lLicenza.getNumeroGiorni();
        else if ("SL".equals(lLicenza.getCodTipoLicenza()) && "C".equals(lLicenza.getFlagConcesso())) 
          sommaLiquidata = lLicenza.getSommaRisarcDanni();
      }
      
      
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("ggConcessi = "+ggConcessi);
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("sommaLiquidata = "+sommaLiquidata);
      // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      //siesLogger.debug("lEveEsecModel = "+lEveEsecModel);
      
      
      String isElaborato = "N";
      if (lEveEsecModel!=null && "S".equals(lEveEsecModel.getFlagDocumentoRegistrato()) )
        isElaborato = "S";
        
      String lRD_SL = "";
      if (ggConcessi!=null && ggConcessi.intValue()>0)
        lRD_SL += "giorni <font class='campo'>"+ggConcessi+"</font>";
      
      if (sommaLiquidata!=null && sommaLiquidata.intValue()>0)
        lRD_SL += " somma liquidata <font class='campo'>"+sommaLiquidata+"</font> &euro;";
      
      if (!lRD_SL.equals(""))
        lRD_SL = " <br> ("+lRD_SL+")";
      
%>
        <tr>
          <td class="l"><%=StringUtils.toStringJSP(lEveSorvModel.getDescrEsito(),"-")%>&nbsp;<%=lRD_SL%></td>
          <td class="l"><%if(lLicAppoModel != null)%><%=StringUtils.toStringJSP(lLicAppoModel.getAnnoSius(),"-")%></td>
          <td class="l"><%if(lLicAppoModel != null)%><%=StringUtils.toStringJSP(lLicAppoModel.getNumeroSius(),"-")%></td>
          
          <td class="l"><%=StringUtils.toStringJSP(lEveSorvModel.getDescrUfficioEmittente(),"-")%> di <%=StringUtils.toStringJSP(lEveSorvModel.getDescrLuogoEmittente(),"-")%></td>
          <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveSorvModel.getDataEmissione(),"dd-MM-yyyy"),"-")%></td>

          <td class="c">
            <a href="Javascript:Verify( <%=lEveSorvModel.getIdEvento()%>
                                      , '<%=isElaborato%>');">
              <img align="middle" src="/images/fileselected.gif" border=0>
            </a>
            
            <% if( "S".equals(isElaborato)) { %>
              <font class="cRosso"> Elaborato </font>
            <% } %>
          </td>
        </tr>
<%
    } // end While
  } // end If
%>
    </table>
  </form>
  </body>
</html>