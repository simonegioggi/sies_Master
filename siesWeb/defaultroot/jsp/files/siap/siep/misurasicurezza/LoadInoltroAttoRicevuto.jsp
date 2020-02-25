<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.ICostantiJMS"%>


<jsp:useBean id="ufficioPM"             scope="request" class="java.lang.String"/>
<jsp:useBean id="Messaggio"        scope="request" class="siap.jms.messaggio.model.MessaggioModel"/>
<jsp:useBean id="dettaglioFasSIEP" scope="request" class="siap.siep.fascicolo.model.DettaglioFascicoloModel" />

<html>
  <head>
    <title>[S.I.E.S.] - TRASMISSIONE PER COMPETENZA</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      function ListaUfficiComuni(a_formname,a_fieldname,codTipoUfficio){
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    </script>
  </head>
  
<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">INOLTRO PER COMPETENZA ESECUZIONE MISURE DI SICUREZZA</font>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%-- jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/ --%>
      <td class="LBG">
        <a href="/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActGestioneMisureSicurezza">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>

<%
  FascicoloSiepModel fascicoloSIEP  = dettaglioFasSIEP.getFascicoloSiep();
  SoggettoModel soggettoRicevuto    = fascicoloSIEP.getSoggetto();
  SentenzaModel sentenzaRicevuta    = fascicoloSIEP.getSentenza();
%> 
<br><br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="Titolo" colspan=4>Procedimento Ricevuto</td>
    </tr>
    <tr>
      <td class="L" colspan=4>
        <font class="label">Procedimento N.</font>
        <font class="campo">
          <%=fascicoloSIEP.getChiaveAnno()%>/<%=fascicoloSIEP.getChiaveProgr()%>
        </font>
        <% if(   fascicoloSIEP.getFlagCumulante() != null && fascicoloSIEP.getFlagCumulante().equals("S") ) { %>
        <font class="cRossoCumulo"> &nbsp;C&nbsp; </font>&nbsp;
        <% } %>
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        <font class="campo">
          - <%=fascicoloSIEP.getDescrTipoUfficio()%>&nbsp;<%=fascicoloSIEP.getDescrComuneUfficio()%><BR>
        </font>

      </td>
    </tr>
    
    <tr>
      <td class="L">
        <font class="label">Data Iscrizione :</font>&nbsp;
        <font class="campo">
          <%=DateUtils.getDateToString(fascicoloSIEP.getDataIscrizione(), "dd-MM-yyyy") %>
        </font>&nbsp;
        <font class="label">Data Irrevocabilità :</font>&nbsp;
        <font class="campo"><%=DateUtils.getDateToString(fascicoloSIEP.getDataIrrevocabilita(), "dd-MM-yyyy") %></font>
      </td> 
    </tr>
    
    <tr>
      <td class="L" width=100% colspan=4>
        <font class="label">Soggetto :</font>&nbsp;
        <font class="campo"><%=soggettoRicevuto.getCognome()%>&nbsp;<%=soggettoRicevuto.getNome()%></font>
        <%if (soggettoRicevuto.getSesso().compareTo("F")==0){%>&nbsp;
        <font class="label">nata il :</font>&nbsp;
        <%}else{%>
        <font class="label">nato il :</font>&nbsp;
        <% } %>
      
        <%
        if(soggettoRicevuto.getDataNascita() == null){
          if(soggettoRicevuto.getDataNascitaPresunta().equals("S")) {%>
            <font class="campo"><%=StringUtils.toStringJSP(soggettoRicevuto.getAnnoNascita())%></font>&nbsp;
          <%} else{%>
            <font class="campo">***</font>&nbsp;
          <%}
        }else{%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(soggettoRicevuto.getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
        <%}%>
        
        <font class="label">in :</font>&nbsp;
        <font class="campo"> 
        <%if (soggettoRicevuto.getDescrComuneNascita().compareTo("-")==0){%>
            <%=soggettoRicevuto.getDescComuneNascitaEstero()%>  (<%=soggettoRicevuto.getDescrStatoNascita().toUpperCase()%>)
        <%} else {%>
            <%=soggettoRicevuto.getDescrComuneNascita()%> (<%=soggettoRicevuto.getCodProvinciaNascita()%>)
        <% } %>      
        </font>
      </td>
    </tr>
  </table>
  
  <% if (Messaggio.getCodUfficioReplyTo()!=null) { %>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="L">
        <font class="label">Inoltrato da :</font>&nbsp;
        <font class="campo"><%=StringUtils.toStringJSP(Messaggio.getDescrUfficioMittente())%> DI <%=StringUtils.toStringJSP(Messaggio.getDescrSedeUfficioMittente())%></font>
    </tr>
  </table>
  <br>
  <% } %>
  
  
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInoltraTrasmissioneCompetenza" >
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misurasicurezza.action.ActInoltraAttoRicevuto">
    <input type="HIDDEN" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" value="<%=Messaggio.getIdMessaggio()%>">

    <%
    //==============================================================================
    //      Ufficio Competente all'emissione del Provvedimento
    //==============================================================================
    %>
    <table cellspacing=2 cellpadding=2 >
      <tr>
        <td class="Titolo" colspan="2">Ufficio Competente all'Esecuzione delle Misure</td>
      </tr>      

      <tr>
        <td class="l">Ufficio del Pubblico Ministero <font class=ob>(*)</font></td>
        <td class="L" >
          <select title="Sede Ufficio Pubblico Ministero"  name="<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>">
            <%=ufficioPM %>
          </select>
        </td>
      </tr>
      <tr>  
        <td class="l">Luogo <font class=ob>(*)</font></td>
        <td class="L" >      
          <input title="Sede Ufficio Giudice Esecuzione"  type="text" name="<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>"  maxlength="35" size="30">
          <a href="Javascript:ListaUfficiComuni('LoadInoltraTrasmissioneCompetenza','<%=ICostantiEvento.CAMPO_COD_LUOGO_DESTINATARIO%>'
                                               ,document.LoadInoltraTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>[document.LoadInoltraTrasmissioneCompetenza.<%=ICostantiEvento.CAMPO_COD_TIPO_UFFICIO_DESTINATARIO%>.options.selectedIndex].value);">
             <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>      
    </table>
    
    <table cellspacing=2 cellpadding=2>
     <tr>
        <td class="Titolo" colspan='8'> Dati Atto </td>
     </tr>      
      <tr>
        <td class="l" width="25%">Tipologia Atto</td>
        <td class="L" colspan="3">
          <font class="campo">Inoltro</font>
        </td>
      </tr>
      <tr>
        <td class="l">Oggetto Atto</td>
        <td class="L" colspan="3">
          <font class="campo">Atti per competenza ai fini dell'esecuzione della misura di sicurezza</font>
        </td>
      </tr>
      <tr>
        <td class="l">Motivo inoltro</td>
        <td  class="L" colspan="3">
          <TEXTAREA title="Contenuto" name="<%=ICostantiJMS.NOTE%>" cols=90 rows=5 ></textarea>
        </td>
      </tr>
    </table>
 

    <br>
    <table>
      <tr>
        <td>
          <input name="conferma" class="bottone"  type="submit" value="Conferma">
        </td>
      </tr>      
    </table>
  </FORM>
</body>