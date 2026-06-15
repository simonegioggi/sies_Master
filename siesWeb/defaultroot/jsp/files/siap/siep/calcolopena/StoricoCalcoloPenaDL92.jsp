<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.calcolopena.action.ICostantiCalcoloPena"%>

<%@ page import="siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB"%>

<jsp:useBean id="storicoCalcoli"  scope="request" class="java.util.Vector" />

<jsp:useBean id="UtenteConnesso"  scope="session" class="siap.sico.utente.model.UtenteModel" />

<%
//==============================================================================
// Jsp per il calcolo della pena "virtuale" DL 92/2024  - MEV_2024-092
// La jsp visualizza:
//==============================================================================
%>

<html>
<head>
  <title>[S.I.E.S.] - Calcolo Pena DL92/2024 </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>" >
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  
  <script language="JavaScript">
  function tornaIndietro()
  {
	document.StoricoCalcoloPenaDL92.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.calcolopena.action.ActLoadCalcoloPenaDL92';
    document.StoricoCalcoloPenaDL92.submit();
  } 
  
  function cancella(idRecord)
  {
    var msgAlert = "Si sta cancellando la Pena Virtuale. Si vuole procedere?";
    if (window.confirm(msgAlert)){
		document.StoricoCalcoloPenaDL92.<%=ICostantiCalcoloPena.CAMPO_ID_CALCOLO_PENA_DL92%>.value=idRecord;
	    document.StoricoCalcoloPenaDL92.<%=IWebConstants.ACTION_FIELD%>.value='siap.siep.calcolopena.action.ActCancStoricoCalcoloPenaDL92';
	    document.StoricoCalcoloPenaDL92.submit();
    }
  } 
    
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img src="../../images/quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG" style="padding-right: 5px;">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Storico Calcolo pena ipotetica con detrazioni DL. 92/2024</font>
      </td>
      <td class="LBG">
        <a href="Javascript:tornaIndietro();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  
  <jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>


<br><br>

<div align="left" style="padding-left:10px;">
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int" style="width: 150px;">Data Validazione</td>
      <%-- 
      <td class="int" style="padding-left:5px;padding-right:5px;width: 100px;">Operatore</td>
      --%>
      <td class="int" colspan="1">&nbsp;</td>
      <td class="int" colspan="3" style="padding-left:10px;padding-right:10px;">Reclusione<br>(anni-mesi-giorni)</td>
      <td class="int" colspan="1">&nbsp;</td>
      <td class="int" colspan="3" style="padding-left:10px;padding-right:10px;">Arresto<br>(anni-mesi-giorni)</td>
      <td class="int" colspan="1">&nbsp;</td>
      <td class="int" colspan="3" style="padding-left:10px;padding-right:10px;">Presoffero<br>(anni-mesi-giorni)</td>
      <td class="int" colspan="1">&nbsp;</td>
      <td class="int" style="padding-left:5px;padding-right:5px;">Posizione Giuridica</td>
      <td class="int" style="padding-left:5px;padding-right:5px;">Data Decorrenza Pena</td>
<td class="int" style="padding-left:5px;padding-right:5px;width: 100px;">Giorni liberazione anticipata</td>      
      <td class="int" style="padding-left:5px;padding-right:5px;">Data Fine Pena Virtuale</td>
      <td class="int" style="width: 100px;">Azioni&nbsp;</td>
    </tr>  
    <%
      Iterator itx = storicoCalcoli.iterator();
      while ( itx.hasNext())
      {
          CalcoloPenaDL92ModelDB lCalcoloModel = (CalcoloPenaDL92ModelDB) itx.next();
    %>
    <tr>
       <td class="c"><%= StringUtils.toStringJSP (DateUtils.getDateToString(lCalcoloModel.getDataInserimento(),"dd-MM-yyyy HH:mm"))%></td>
       <%--
       <td class="c"><%= StringUtils.toStringJSP (lCalcoloModel.getCodOperatoreInserimento() )%></td>
       --%>
       
       <%-- Reclusione --%>
       <td class1="c" colspan="1">&nbsp;</td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumAnniReclusione().intValue()>0 ? lCalcoloModel.getNumAnniReclusione() : "&nbsp;" %></font></td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumMesiReclusione().intValue()>0 ? lCalcoloModel.getNumMesiReclusione() : "&nbsp;" %></font></td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumGiorniReclusione().intValue()>0 ? lCalcoloModel.getNumGiorniReclusione() : "&nbsp;" %></font></td>

       <%-- Arresto --%>
       <td class1="c" colspan="1">&nbsp;</td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumAnniArresto().intValue()>0 ? lCalcoloModel.getNumAnniArresto() : "&nbsp;" %></font></td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumMesiArresto().intValue()>0 ? lCalcoloModel.getNumMesiArresto() : "&nbsp;" %></font></td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumGiorniArresto().intValue()>0 ? lCalcoloModel.getNumGiorniArresto() : "&nbsp;" %></font></td>

       <%-- Presofferto --%>
       <td class1="c" colspan="1">&nbsp;</td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumAnniPresofferto().intValue()>0 ? lCalcoloModel.getNumAnniPresofferto() : "&nbsp;" %></font></td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumMesiPresofferto().intValue()>0 ? lCalcoloModel.getNumMesiPresofferto() : "&nbsp;" %></font></td>
       <td class="r" nowrap><font class="label"><%=lCalcoloModel.getNumGiorniPresofferto().intValue()>0 ? lCalcoloModel.getNumGiorniPresofferto() : "&nbsp;" %></font></td>
       <td class1="c" colspan="1">&nbsp;</td>
       
       <% if ("L".equals(lCalcoloModel.getPosizioneGiuridica())) { %>
       <td class="c" nowrap><font class="label">Libero</font></td>
       <% } else { %>
       <td class="c" nowrap><font class="label">Detenuto</font></td>
       <% } %>

       <td class="c"><%= StringUtils.toStringJSP (DateUtils.getDateToString(lCalcoloModel.getDataInizioPena(),"dd-MM-yyyy"),"&nbsp;")%></td>
<td class="c" nowrap><font class="label"><%=lCalcoloModel.getLaApplicate().intValue()>0 ? lCalcoloModel.getLaApplicate() : "&nbsp;" %></font></td>
       <td class="c"><%= StringUtils.toStringJSP (DateUtils.getDateToString(lCalcoloModel.getDataScarcLaFung(),"dd-MM-yyyy"),"&nbsp;")%></td>
       
       <td class="c" nowrap>
        <table>
          <tr>
            <td>
              <a href="/jsp/Main.jsp?Action=siap.siep.calcolopena.action.ActDettStoricoCalcoloPenaDL92&<%=ICostantiCalcoloPena.CAMPO_ID_CALCOLO_PENA_DL92 %>=<%=lCalcoloModel.getIdCalcoloPenaDL92() %>&fromLista=S">
                <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio" border="0">
              </a>
            </td>
            <%-- 
            <td>
              <a href="Javascript:stampaSiep('/jsp/Main.jsp?Action=siap.sico.evento.action.ActLoadDocumento&IdEvento=1839505212026')">
                <img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
              </a>
            </td>
            --%>
            <% if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lCalcoloModel.getCodUfficioInserimento())) { %>
            <td>
              <a href="Javascript:cancella('<%=lCalcoloModel.getIdCalcoloPenaDL92() %>');">
                      <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
              </a>
            </td>
            <% } %>
          </tr>
        </table>
       </td>
    </tr>
    <% } %>

    <% if (storicoCalcoli.size()==0) { %>
    <tr>
      <td class="c" colspan="100%">Nessun calcolo e' stato validato per il fascicolo corrente</td>
    </tr>
    <% } %>
   </table>
   
  <form method="POST" action="/jsp/Main.jsp" name="StoricoCalcoloPenaDL92">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiCalcoloPena.CAMPO_ID_CALCOLO_PENA_DL92%>" value="">
  </form> 
</div> 
