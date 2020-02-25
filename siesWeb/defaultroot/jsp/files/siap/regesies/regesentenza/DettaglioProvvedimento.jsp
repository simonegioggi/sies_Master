<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.regesies.action.ICostantiRegeSies"%>
<%@ page import="siap.regesies.regesentenza.model.RegeSentenzaModel"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.regesies.regesentenza.action.ICostantiRegeSentenza"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="provvedimento"  scope="request" class="siap.regesies.regesentenza.model.ProvvedimentoModel"/>
<jsp:useBean id="NomeAzione"     scope="request" class="java.lang.String" />

<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";
String lTipoProvv = provvedimento.getRegeSentenza().getDescrTipoProvvedimento();

%>

<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Provvedimento ReGe - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var node;
      function effettoTree(a)
      {
        node=document.getElementById("elenco"+a);
        node.style.display = (node.style.display == "none")? "block" : "none";
        document.images["image"+a].src = (node.style.display == "none")? "<%=IWebConstants.IMAGES_DIR%>expand.gif" : "<%=IWebConstants.IMAGES_DIR%>collapse.gif";
        return false;
      }

    function Verify()
      {
      
  <%    if(provvedimento.getSentenza()==null)
{%>
      var d1=document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>.value;
      if (! ControllaData(d1))
      {
        alert('Data di arrivo atto non valida');
        return false;
      }
            //Data Sentenza
     var d2=document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;

     if (CompareDate(d1,d2))
      {
        alert('La Data di Arrivo Atto deve essere successiva alla Data <%=lTipoProvv%>');
        document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>.focus()
        return false;
      }
      var d4=document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>.value;
      if (! ControllaData(d4))
      {
        alert('Data di Iscrizione non valida');
        return false;
      }
      if (CompareDate(d4,d2))
      {
        alert('La Data di Iscrizione deve essere successiva alla Data <%=lTipoProvv%>');
        document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>.focus()
        return false;
      }
      var d3=document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA%>.value+'/'+document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA%>.value;
      if(d3.length!=10)
      {
         alert('Impossibile inserire un Provvedimento senza Data Irrevocabilità.');
         document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesentenza.action.ActLoadModificaRegeSentenza&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSentenza().getIdFile()%>";
         return false;
      }
      <%}%>
    return true;
  }
  </script>
</head>
<%
if(provvedimento.getSentenza()==null)
{%>
<BODY class="corpo" onload="javascript:document.DettaglioProvvedimentoRege.<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>.focus()">
<%}else{%>
  <BODY class="corpo">
  <%}%>
  <FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Provvedimento Rege</font>
        </td>
      <%

      //Setto le check box da disabilitare
      String disableCheckResidenza = " checked=\"checked\" ";
      String disableCheckReato = " checked=\"checked\" ";
      String disableCheckNotiziaReato = " checked=\"checked\" ";
      String disableCheckCircostanza = " checked=\"checked\" ";
      String disableCheckDifensori = " checked=\"checked\" ";
      String disableCheckDispositivo = " checked=\"checked\" ";

       if ((provvedimento.getResidenze() == null) || (provvedimento.getResidenze() != null && provvedimento.getResidenze().size()==0))
          disableCheckResidenza = "disabled=\"disabled\"";
      if ((provvedimento.getReati() == null) || (provvedimento.getReati() != null && provvedimento.getReati().size()==0))
          disableCheckReato = "disabled=\"disabled\"";
      if ((provvedimento.getCircostanze() == null) || (provvedimento.getCircostanze() != null && provvedimento.getCircostanze().size()==0))
          disableCheckCircostanza = "disabled=\"disabled\"";
      if ((provvedimento.getNotizieDiReato() == null) || (provvedimento.getNotizieDiReato() != null && provvedimento.getNotizieDiReato().size()==0))
          disableCheckNotiziaReato = "disabled=\"disabled\"";
      if ((provvedimento.getDifensori() == null) || (provvedimento.getDifensori() != null && provvedimento.getDifensori().size()==0))
          disableCheckDifensori = "disabled=\"disabled\"";
      if ((provvedimento.getRegeSentenza().getNotaDispositivo()== null) || (provvedimento.getRegeSentenza().getNotaDispositivo() != null && provvedimento.getRegeSentenza().getNotaDispositivo().length()<=1))
          disableCheckDispositivo = "disabled=\"disabled\"";
      %>
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER%>">
             <jsp:param name="CampoIdEntita" value="<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>" />
             <jsp:param name="ValoreIdEntita" value="<%=provvedimento.getRegeSentenza().getIdFile()%>" />
          </jsp:include>
        </td>
      </tr>
    </table>
  </FORM>
<form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="DettaglioProvvedimentoRege">


<%//La sentenza Rege non esiste nel sistema SIEP
if(provvedimento.getSentenza()==null)
{
  RegeSentenzaModel lSentenza = provvedimento.getRegeSentenza();
  %>
<table cellspacing=1 cellpadding=1  border=<%=isBorder%>>
  <tr>
    <td class="LBGISI" width=<%=largh%> valign="middle" >
     <a class="cliccabileBlu" title="Dettaglio Rege <%=lTipoProvv%>" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesentenza.action.ActDettaglioRegeSentenza&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=lSentenza.getIdFile()%>">
         <%=lTipoProvv.toUpperCase()%> REGE
       </a>
 </td>
 <td>
   <table  cellspacing=1 cellpadding=1 border=<%=isBorder%>>
     <tr> <td class="l" >Anno/Numero</td>
      <td class="L">
        <font class="campo"><%=lSentenza.getAnnoSentenza()%></font>&nbsp;
        /<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
      </td>
      <td class="l" >Data <%=lTipoProvv%></td>
      <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
         <input Title="Data Provvedimento" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" >
         <input Title="Data Provvedimento" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" maxlength="2" size="2"  >
         <input Title="Data Provvedimento" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"maxlength="4" size="4" >
      </td>
    </tr>
    <tr>
      <td class="l" >Autorità Emittente</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
      <td class="l">Data Irrevocabilità</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd")) %>" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" maxlength="2" size="2" >
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"MM")) %>" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" maxlength="2" size="2"  >
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"yyyy")) %>" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>"maxlength="4" size="4" >
     </td>
    </tr>
    <tr>
      <td class="l">Data Arrivo Atto</td>
          <td class="L" >
            <input Title="Data Arrivo Atto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd")) %>" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -<input Title="Data Arrivo Atto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"MM")) %>" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -<input Title="Data Arrivo Atto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"yyyy")) %>" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
      <td class="l">Note</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
      </td>
    </tr>
    <tr><%if( lSentenza.getDataIscrizione() == null) 
    		{lSentenza.setDataIscrizione(DateUtils.getSysDate());}%>
      <td class="l">Data Iscrizione</td>
          <td class="L" >
            <input Title="Data Iscrizione" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd")) %>" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -<input Title="Data Iscrizione" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"MM")) %>" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -<input Title="Data Iscrizione" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"yyyy")) %>" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
     
     
    </tr>
   </table>
   <%
   }
  else
   {//--------------------------Sentenza già presente nel sistema SIEP------------------------------
    SentenzaModel lSentenza = provvedimento.getSentenza();
   %>
     <table cellspacing=1 cellpadding=1 width="100%"  border=<%=isBorder%>>
      <tr><td colspan=2 class="LBGISIV"><font class="cRosso">
    <%=lTipoProvv%> già presente</font></td>
  </tr>
  <tr>
    <td class="LBGISI" width=<%=largh%> valign="middle" >
    	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
     	<%--a class="cliccabileBlu" title="Dettaglio < %=lTipoProvv%>" href="< %=IWebConstants.PG_MAIN%>?< %=IWebConstants.ACTION_FIELD%>=siap.sies.sentenza.action.ActDettaglioSentenza& < %=ICostantiSentenza.CAMPO_ID_SENTENZA%>=<  %=lSentenza.getIdSentenza()%>">
         < % =lTipoProvv.toUpperCase()%>
       	</a--%>
           <a class="cliccabileBlu" title="Dettaglio Rege <%=lTipoProvv%>" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesentenza.action.ActDettaglioRegeSentenza&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSentenza().getIdFile()%>">
         <%=lTipoProvv.toUpperCase()%> REGE
       </a>
 </td>
 <td>

   <table width="100%">
     <tr> <td class="l">Anno/Numero</td>
      <td class="L">
        <font class="campo"><%=lSentenza.getAnnoSentenza()%></font>&nbsp;
        /<font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
      </td>
      <td class="l" >Data <%=lTipoProvv%></td>
      <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd-MM-yyyy"))%></font>&nbsp;
      </td>
    </tr>
    <tr>
      <td class="l" >Autorità Emittente</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%> di <%=StringUtils.toStringJSP(lSentenza.getDescrLuogoEmittente())%></font>&nbsp;
      </td>
      <%-- 
      //  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
      td class="l">Data Irrevocabilità</td>
      <td class="L">
          <font class="campo">< %=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;
          <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"dd")) %>" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_IRREVOCABILITA %>" maxlength="2" size="2" >
          <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"MM")) %>" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_IRREVOCABILITA %>" maxlength="2" size="2"  >
          <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIrrevocabilita(),"yyyy")) %>" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_IRREVOCABILITA %>"maxlength="4" size="4" >
      </td--%>
    </tr>
    <tr>
      <%-- 
      //  modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
      td class="l">Data Arrivo Atto</td>
        <td class="L" >
          <font class="campo">< %=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd-MM-yyyy"),"-")%></font>&nbsp;
          <input Title="Data Arrivo Atto" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"dd")) %>" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>" maxlength="2" size="2" >
          <input Title="Data Arrivo Atto" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"MM")) %>" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO %>" maxlength="2" size="2"  >
          <input Title="Data Arrivo Atto" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataArrivoAtto(),"yyyy")) %>" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO %>"maxlength="4" size="4" >
        </td--%>
      <td class="l">Note</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNote())%></font>&nbsp;
      </td>
    </tr>
     <tr><%if( lSentenza.getDataIscrizione() == null) 
    		{lSentenza.setDataIscrizione(DateUtils.getSysDate());}%>
      <td class="l">Data Iscrizione</td>
          <td class="L" >
                <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd-MM-yyyy"),"-")%></font>&nbsp;
    
            <input Title="Data Iscrizione" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"dd")) %>" name="<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE %>" maxlength="2" size="2" >
           -<input Title="Data Iscrizione" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"MM")) %>" name="<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE %>" maxlength="2" size="2" >
           -<input Title="Data Iscrizione" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataIscrizione(),"yyyy")) %>" name="<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE %>"maxlength="4" size="4" >
          </td>
     
     
    </tr>
   </table>

<% }   %>
  </td>
  </tr>
</table>
<br>
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>  <td class="LBGISI" width=<%=largh%> valign="middle">
  <a class="cliccabileBlu" title="Dettaglio Soggetto" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesoggetto.action.ActDettaglioRegeSoggetto&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSoggetto().getIdFile()%>">
         SOGGETTO
       </a>
     </td>
    <td>
<table width="50%">
<tr> <td class="L" colspan=4 width="50%">
		<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <font class="campo">
        <a class="cliccabile" title="Dettaglio Soggetto" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesoggetto.action.ActDettaglioRegeSoggetto&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSoggetto().getIdFile()%>">
          <%=provvedimento.getRegeSoggetto().getCognome()%>&nbsp;<%=provvedimento.getRegeSoggetto().getNome()%>
        </a>
      </font>
<%    if (provvedimento.getRegeSoggetto().getSesso().compareTo("F")==0)
       {%>  <font class="label">&nbsp; nata il </font><%}else{
%>          <font class="label">&nbsp; nato il </font><%}%>
       <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getRegeSoggetto().getDataNascita(),"dd-MM-yyyy"),"-")%></font>
       <font class="label">&nbsp; in  </font>
<%   if (provvedimento.getRegeSoggetto().getDescrComuneNascita().compareTo("-")==0)
       {%> <font class="campo"><%=provvedimento.getRegeSoggetto().getDescrStatoNascita()%> </font>
<%     }else{%><font class="campo"><%=provvedimento.getRegeSoggetto().getDescrComuneNascita() + "  ("+provvedimento.getRegeSoggetto().getCodProvinciaNascita()+")" %></font>
<%     }%>
      </td></tr>
    </table></td></tr>
</table>

<%if (provvedimento.getSoggettiOmonimi()!=null && provvedimento.getSoggettiOmonimi().size()>0)
{//-----------------TROVATI SOGGETTI OMONIMI ----------------------------
  %>
<table cellspacing=1 cellpadding=1  width=100% border=<%=isBorder%>>
  <tr>
    <td width=<%=largh%> valign="middle">&nbsp;</td>
  <td>

 <table cellspacing=1 cellpadding=1 border=<%=isBorder%> width="50%">
 <tr>
 <td class="label" width="8%">
       <a><img name="image7" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(7);" alt="" border=0></a>
  &nbsp;</td>
   <td class="LBGISIV"><font class="cRosso">Trovati Soggetti Omonimi</font></td>
  </tr>
  </table>
  <div id="elenco7" style="display:block; width:100%;" >
 <table cellspacing=1 cellpadding=1 border=<%=isBorder%> width="50%">
  <tr>
   <td class="c" width="8%"> <input type="radio" name="<%=ICostantiRegeSentenza.CAMPO_SOGGETTO_OMONIMO%>" value="NUOVO" checked="checked"/> </td>
   <td class="l" colspan="2"> Inserisci un nuovo soggetto con i dati ReGe</td><td></td>
  </tr>
 <%
  Iterator lItOmonimi = provvedimento.getSoggettiOmonimi().iterator();
  while (lItOmonimi.hasNext())
  {
    SoggettoModel lSoggSiep = (SoggettoModel)lItOmonimi.next();
  %>
  <tr>
 <td class="c"> <input type="radio" name="<%=ICostantiRegeSentenza.CAMPO_SOGGETTO_OMONIMO%>" value="<%=lSoggSiep.getIdSoggetto()%>"/></td>
 <td class="l"> <font class="campo"><%=lSoggSiep.getCognome()%>&nbsp;<%=lSoggSiep.getNome()%></font>&nbsp;
 <%if(lSoggSiep.getSesso().equals("F")){%> <font class="label">nata il&nbsp; <%}else{%> <font class="label">nato il&nbsp; <%}%>
 </font> <font class="campo"><%=DateUtils.getDateToString(lSoggSiep.getDataNascita(),"dd-MM-yyyy")%>&nbsp;
 </font> <font class="label">in</font> <font class="campo">
 <%=lSoggSiep.getDescrComuneNascita()%>&nbsp;</font>
 </td>
  <td class="c">
       <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regesoggetto.action.ActDettaglioSoggettoOmonimo&IdSoggetto=<%=lSoggSiep.getIdSoggetto()%>">
           <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
       </a>
    </td>
 </tr>
<%}
%></table></div></td></tr></table>

<%
}else{//non ci sono omonimi%>
 <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_SOGGETTO_OMONIMO%>" value="NUOVO"/>
</table></div></td></tr></table>
<%}%>
<br>
<!--------------RESIDENZA--------------->
<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
 <tr> <td class="LBGISI" width=<%=largh%>>
       <a class="cliccabileBlu" title="Elenco Residenze/Domicili" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regeresidenza.action.ActRicercaRegeResidenza&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSoggetto().getIdFile()%>">
         Residenze
       </a></td>
     <td class="label" width=<%=resto%>>
       <a><img name="image1" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(1);" alt="" border=0>
       </a>&nbsp;</td>
  </tr>
 </table>
 <div id="elenco1" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr> <td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiRegeSentenza.CAMPO_CHECK_RESIDENZA%>" value="<%=ICostantiRegeSentenza.CAMPO_CHECK_RESIDENZA%>" <%=disableCheckResidenza%>/> </td>
  <td> <jsp:include page="/jsp/files/siap/regesies/regeresidenza/ElencoResidenzeInclude.jsp"/></td>
  </tr>
 </table>
 <br>
</div>
<!--------------REATI--------------->
<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%>  align="left">
       <a class="cliccabileBlu" title="Elenco Reati" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regereato.action.ActRicercaRegeReato&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSoggetto().getIdFile()%>" >
         Reati
       </a>
    <td class="label" width=<%=resto%>>
       <a><img name="image2" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(2);" alt="" border=0></a>
  &nbsp;</td></tr>
 </table>
 <div id="elenco2" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>><tr>
  <td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiRegeSentenza.CAMPO_CHECK_REATO%>" value="<%=ICostantiRegeSentenza.CAMPO_CHECK_REATO%>" <%=disableCheckReato%>/> </td>
<td><jsp:include page="/jsp/files/siap/regesies/regereato/ElencoReatoInclude.jsp"/></td>
  </tr>
 </table>
 <br>
 </div>
<!--------------CIRCOSTANZE--------------->
<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%> colspan=2>
       <a class="cliccabileBlu" title="Elenco Circostanze" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regecircostanza.action.ActRicercaRegeCircostanza&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSoggetto().getIdFile()%>">
         Circostanze
       </a>
     <td class="label" width=<%=resto%> colspan=1>
       <a><img name="image3" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(3);" alt="" border=0></a>
  &nbsp;</td></tr>
 </table>
 <div id="elenco3" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <tr>  <td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiRegeSentenza.CAMPO_CHECK_CIRCOSTANZA%>" value="<%=ICostantiRegeSentenza.CAMPO_CHECK_CIRCOSTANZA%>" <%=disableCheckCircostanza%>/> </td>
  <td>
  <jsp:include page="/jsp/files/siap/regesies/regecircostanza/ElencoCircostanzeInclude.jsp"/>
  </td></tr>
 </table>
 <br>
 </div>
<!--------------NOTIZIE REATO--------------->
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%> colspan=2>
       <a class="cliccabileBlu" title="Elenco Notizie di Reato" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.regesies.regenotiziareato.action.ActRicercaRegeNotiziaReato&<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>=<%=provvedimento.getRegeSoggetto().getIdFile()%>">
         Notizie di Reato
       </a>
     <td class="label"  width=<%=resto%> colspan=1>
       <a><img name="image4" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(4);" alt="" border=0></a>
   &nbsp;</td></tr>
 </table>
 <div id="elenco4" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
   <tr><td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiRegeSentenza.CAMPO_CHECK_NOTIZIAREATO%>" value="<%=ICostantiRegeSentenza.CAMPO_CHECK_NOTIZIAREATO%>" <%=disableCheckNotiziaReato%>/> </td>
<td>
  <jsp:include page="/jsp/files/siap/regesies/regenotiziareato/ElencoNotizieDiReatoInclude.jsp"/>
 </td> </tr>
 </table>
 <br>
 </div>
<!--------------AVVOCATI--------------->
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%> colspan=2>
          <font  class="campoLow">Difensori</font>
     <td class="label"  width=<%=resto%> colspan=1>
       <a><img align="left" name="image5" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(5);" alt="" border=0></a>
    </tr>
 </table>
 <div id="elenco5" style="width: 100%; display:block" >
 <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
   <tr><td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiRegeSentenza.CAMPO_CHECK_DIFENSORI%>" value="<%=ICostantiRegeSentenza.CAMPO_CHECK_DIFENSORI%>" <%=disableCheckDifensori%>/> </td>
<td>
  <jsp:include page="/jsp/files/siap/regesies/regeavvocato/ElencoAvvocatoInclude.jsp"/>
 </td> </tr>
 </table>
 <br>
 </div>
 <!--------------DISPOSITIVO--------------->
<table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
  <tr>
     <td class="LBGISI" width=<%=largh%> colspan=2>
         <font  class="campoLow">Dispositivo</font>
     <td class="label" width=<%=resto%> colspan=1>
       <a><img name="image6" src="<%=IWebConstants.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(6);" alt="" border=0>
       </a></td>
  </tr>
 </table>
 <div id="elenco6" style="display:block;  width:<%=resto%>;">
 <table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
  <tr><td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiRegeSentenza.CAMPO_CHECK_DISPOSITIVO%>" value="<%=ICostantiRegeSentenza.CAMPO_CHECK_DISPOSITIVO%>" <%=disableCheckDispositivo%>/> </td>
<td>
  <table width="50%"><tr><td class="l">
   <%if(provvedimento.getRegeSentenza().getNotaDispositivo()!=null && provvedimento.getRegeSentenza().getNotaDispositivo().trim().length()>0){%>
   <font class="campo"><%=StringUtils.toStringJSP(provvedimento.getRegeSentenza().getNotaDispositivo(),"-")%></font>
  <%}else{%>
     <font class="cGrigio">Nessun Dispositivo per il provvedimento.</font>
  <%}%></td></tr>
  </table>
   </td></tr>
 </table>
 <br>
 </div>
  <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  <%
  FascicoloSiepModel fascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");

 //Fascicolo SIEP in Sessione
  if(fascicolo!=null && fascicolo.getIdFascicoloSiep()!=null && fascicolo.getFlagValidato()!=null && fascicolo.getFlagValidato().equalsIgnoreCase("N"))
  {%>
  <tr>
     <td class="c"> <input type="radio" name="importa" value="NUOVO" checked="checked"/> </td>
   <td class="l">Importazione in nuovo fascicolo</td>
  </tr>
  <tr>
   <td class="c"> <input type="radio" name="importa" value="VECCHIO"/> </td>
   <td class="l">Importazione in fascicolo esistente (<font class="campo"><%=fascicolo.getChiaveAnno()%></font>/
   <font class="campo"><%=fascicolo.getChiaveProgr() %></font> emesso da
   <font class="campo"><%=fascicolo.getDescrTipoUfficio()%></font> di
   <font class="campo"><%=fascicolo.getDescrComuneUfficio()%>)</font></td>
  </tr>

  <%}%>



  <tr>
   <td colspan=2>
 	      <br>
        <input  class="bottone" type="submit" name="R" value="Conferma Importazione Dati Rege in SIEP">
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.regesies.regesentenza.action.ActConfermaImportaDatiRege">
        <input type="HIDDEN" name="<%=ICostantiRegeSentenza.CAMPO_ID_FILE%>" value="<%=provvedimento.getRegeSentenza().getIdFile()%>">

      </td>
  </tr></table></form>
  <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("DettaglioProvvedimentoRege");

      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","req");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","req");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","req");

      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","numeric");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","numeric");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","numeric");

      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ARRIVO_ATTO%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ARRIVO_ATTO%>","lt=12");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ARRIVO_ATTO%>","lt=31");
   	  frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","req");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","req");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","req");

      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","numeric");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","numeric");
      frmvalidator.addValidation("<%=ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","numeric");

      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","minlen=4","La lunghezza minima per l'anno di iscrizione agli atti è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_ANNO_DATA_ISCRIZIONE%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_MESE_DATA_ISCRIZIONE%>","lt=12");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiRegeSentenza.CAMPO_GIORNO_DATA_ISCRIZIONE%>","lt=31");

      frmvalidator.setAddnlValidationFunction("Verify");

     </script>
  </body>
</html>