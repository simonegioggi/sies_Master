<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo" %>

<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiProcedimentoCumulato" %>



<jsp:useBean id="IstruttoriaCumulo"  scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="procedimentocumulato" scope="request" class="siap.siep.modulocumulo.model.ProcedimentoCumulatoModel"/>

<html>
<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>" ></script>
</head>


<BODY class="corpo">

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formComandi">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>" value="<%=StringUtils.toStringJSP(TitoloInCumulo.getIdTitoloCumulato()) %>">
  <input type="hidden" name="<%=ICostantiProcedimentoCumulato.CAMPO_ID_PROCEDIMENTO_CUMULATO%>" value="<%=StringUtils.toStringJSP(procedimentocumulato.getIdProcedimentoCumulato()) %>">
  <input type="hidden" name="modalita" value="">


  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="/images/quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp; 
        <font class="campo">Dettaglio Estremi Procedimento di Esecuzione</font>
      </td>      
      
      <td class="LBG">
        <% if ("A".equals(IstruttoriaCumulo.getFlagStato()) ) { %>
        <a href="javascript:submit('modifica')">
          <img align="middle" src="/images/modifica24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        
        <%// La cancellazione non è consentita se preso in carico. Non ha senso. Inoltre si perde l'accoppiamento con NSC.%>
        <% if ( "I".equals(procedimentocumulato.getFlagStato())) { %>
        <a href="javascript:submit('siap.siep.modulocumulo.action.ActLoadInserisciProcedimentoCumulato')">
          <img align="middle" src="/images/delete24.gif" alt="Modifica" width="24" height="24" border="0"></a>
        <% } %>    
        <SCRIPT LANGUAGE="JavaScript">
          function submit( aAction )
          {
            var action = "";
            if (aAction=="modifica") {
              document.formComandi.modalita.value="M";
              action = "siap.siep.modulocumulo.action.ActLoadInserisciProcedimentoCumulato";
            }
            else {
              if (confirm ("Se vuole cancellare il dato relativo al Procedimento di esecuzione")) {
                document.formComandi.modalita.value="C";
                action = "siap.siep.modulocumulo.action.ActInserisciProcedimentoCumulato";
              }
              else {
                return;
              }
            }
            
            document.formComandi.<%=IWebConstants.ACTION_FIELD%>.value=action;
            document.formComandi.submit();
          }
        </SCRIPT>
        <% } %>
      </td>    
    </tr>
  </table>
</form>


<table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
  <tr>
    <td>
      <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    </td>
  </tr>
  <tr>
    <td>
      <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
    </td>
  </tr>
</table>

<br>

  <table cellspacing="2" cellpadding="2" width="800px">
    <tr><td class="Titolo" colspan="100%">Estremi del procedimento di esecuzione del titolo da cumulare</td></tr>
    <tr>
      <td class="l">Anno/Numero SIEP</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(procedimentocumulato.getChiaveAnnoFasCumulato()) %></font>
        /
        <% if (!"S".equals (procedimentocumulato.getFlagAccorpato()) ) { %>
        <font class="campo"><%=StringUtils.toStringJSP(procedimentocumulato.getChiaveProgrFasCumulato()) %></font>
        <% } else {%>        
        <font class="campo"><%=StringUtils.toStringJSP(procedimentocumulato.getChiaveProgrOrigine()) %></font>
        <font class="cRosso">(Ex  <%=StringUtils.toStringJSP(procedimentocumulato.getUfficioOrigine().getDescrTipoUfficio()) %> 
        di <%=StringUtils.toStringJSP(procedimentocumulato.getUfficioOrigine().getDescrComune()) %>
         ) </font>
        <% } %>
      </td>
    </tr>
    <tr>
      <td class="l">Autorità</td>
      <td class="l"> 
        <font class="campo"><%=StringUtils.toStringJSP(procedimentocumulato.getDescrTipoUfficioFasCumulato()) %>&nbsp;</font>   
      </td> 
    </tr>
    
    <tr>
      <td class="l">Luogo</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(procedimentocumulato.getDescrLuogoUfficioFasCumulato()) %>&nbsp;</font>
      </td>
    </tr>

    <tr>
      <td class="l">Data Richiesta Fascicolo</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(procedimentocumulato.getDataRichiestaFascicolo(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    </tr>
    
    <tr>
      <td class="l">Data Pervenimento Fascicolo</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(procedimentocumulato.getDataPervenimentoFascicolo(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
    </tr>
    
    <tr>
      <td class="l">Note</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(procedimentocumulato.getNote()) %></font>&nbsp;</td> 
    </tr>

    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l"><font class="campo"><%=StringUtils.toStringJSP(procedimentocumulato.getMotivoModifica()) %></font>&nbsp;</td> 
    </tr>
  </table>
  

</body>







