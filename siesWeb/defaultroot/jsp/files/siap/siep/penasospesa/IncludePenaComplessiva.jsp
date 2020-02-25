<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.web.ISIAPCostantiWeb" %>

<%@ page import="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>
<%@ page import="siap.siep.continuazione.model.ContinuazioneModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>

<jsp:useBean id="dettaglioPenaComplessiva" 	scope="session" class="siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel"/>
<jsp:useBean id="lTipoFunzione"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       				scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     				scope="request" class="java.lang.String"/>
<jsp:useBean id="penaSigeModificabile"      scope="request" class="java.lang.String"/>
<jsp:useBean id="lPenaResMod"    	 	  	scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>

<%
  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
  
  PenaComplessivaModel lPenCom = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getPenaComplessiva() : null;
  SanzioneSostitutivaModel lSanSos = (dettaglioPenaComplessiva!= null && dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva() != null) ? dettaglioPenaComplessiva.getPenaComplessivaSanzioneSostitutiva().getSanzioneSostitutiva() : null;
  List lListCont = dettaglioPenaComplessiva.getContinuazioni();
	
  if(lSanSos == null)    
	  lSanSos = new SanzioneSostitutivaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Dettaglio Pena Complessiva </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function DisabilitaPenAcc()
  {

   aForm=document.getElementById("PenAcc");
    Disabilita();
  }

 function Disabilita()
  {
 //   if (aForm==null)  /*perchè è stato eliminata la form Concessione Benefici CB */
 //      aForm=document.getElementById("CB");

    document.Abbandona.A.disabled = true;
    document.PenAcc.U.disabled = true;
   // document.CB.D.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
</head>

<body class="corpo">
<%if (lPenCom != null) { %>
  <table cellspacing=2 cellpadding=2 width=100%>
    <tr>
    <% 
    if ((lPenCom.getNumAnniReclusione() != null) || (lPenCom.getNumMesiReclusione() != null)
      || (lPenCom.getNumGiorniReclusione() != null)) { %>
      <td class="l">Reclusione</td>
      <td class="l">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniReclusione(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiReclusione(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniReclusione(), "0")%></font>
      </td>
    <% } %>
    <% if (lPenCom.getImportoMulta() != null) {%>
      <td class="l">Multa</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    <% } %>
    </tr>
    <tr>

  <% if ((lPenCom.getNumAnniArresto() != null) || (lPenCom.getNumMesiArresto() != null)
      || (lPenCom.getNumGiorniArresto() != null)) { %>
      <td class="l">Arresto</td>
      <td class="l">
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniArresto(), "0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiArresto(), "0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniArresto(), "0")%></font>
      </td>
   <% } %>
   <% if (lPenCom.getImportoAmmenda() != null) {%>
      <td class="l">Ammenda</td>
      <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(lPenCom.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
   <% } %>
    </tr>
<% 
		if (   (lPenCom.getDescrTipoPenaDetentivaDB() != null) 
		    && (!(lPenCom.getDescrTipoPenaDetentivaDB().equals("-")))) 
		{ 
%>
			<tr>
		  	<td class="l">Ergastolo</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lPenCom.getDescrTipoPenaDetentivaDB())%></font>&nbsp;</td>
		  </tr>
<%
		}
%>
    <tr>
<% 
		if (lPenCom.getDataInizioIsolamentoDiurno() != null) 
		{ 
%>
      <td class="l">Data Inizio Isolamento Diurno</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataInizioIsolamentoDiurno(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 

		if (lPenCom.getDataFineIsolamentoDiurno() != null) 
		{ 
%>
      <td class="l">Data Fine Isolamento Diurno</td>
      <td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataFineIsolamentoDiurno(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 
%>
    </tr>
    <tr>
<% 
		if (	 (lPenCom.getNumAnniIsolamentoDiurno() != null) || (lPenCom.getNumMesiIsolamentoDiurno() != null)
    		|| (lPenCom.getNumGiorniIsolamentoDiurno() != null)) 
		{ 
%>
	    <td class="l">Durata Isolamento Diurno</td>
	    <td class="l">Anni
	      <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumAnniIsolamentoDiurno(), "0") %>&nbsp;</font>
	    		Mesi
	      <font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumMesiIsolamentoDiurno(), "0")%>&nbsp;</font>
	    		Giorni
	     	<font class="campo"><%=StringUtils.toStringJSP(lPenCom.getNumGiorniIsolamentoDiurno(), "0")%>&nbsp;</font>
      </td>
<% 
		} 

		if (lPenCom.getDataPrescrizione() != null) 
		{ 
%>
   		<td class="l">Data Prescrizione</td>
			<td class="l">
        <font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDateToString(lPenCom.getDataPrescrizione(),"dd-MM-yyyy"))%>
        &nbsp;</font>
      </td>
<% 
		} 
%>
   </tr>
   <% if ((lSanSos.getDescrTipoSanzione() != null) || (lSanSos.getNumAnni() != null) ||
      (lSanSos.getNumMesi() != null) || (lSanSos.getNumGiorni() != null) ||
      (lSanSos.getSanzionePecuniariaMulta() != null)  ||
      (lSanSos.getSanzionePecuniariaAmmenda() != null)) { %>
    <tr><td class="Titolo" colspan=4>Sanzione Sostitutiva</td></tr>
    <% if (lSanSos.getDescrTipoSanzione() != null) { %>
    <tr>
      <td class="l">Tipo Sanzione Sostitutiva</td>
      <td class="l" colspan="3"><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getDescrTipoSanzione())%></font>&nbsp;</td>
    </tr>
   <% } %>
   <% if ((lSanSos.getNumAnni() != null) ||
      (lSanSos.getNumMesi() != null) || (lSanSos.getNumGiorni() != null)) { %>
    <tr>
      <td class="l">Durata Sanzione Sostitutiva</td>
      <td class="l" colspan="3">
        <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumAnni(), "0")%>&nbsp;</font>
        <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumMesi(), "0")%>&nbsp;</font>
        <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(lSanSos.getNumGiorni(), "0")%></font>
      </td>
    </tr>
  <% } %>
  <% if (lSanSos.getSanzionePecuniariaMulta() != null) { %>
    <tr>
      <td class="l" colspan="2">Pena Pecuniaria Sostitutiva Multa</td>
      <td class="l" colspan="2"><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaMulta())%></font>&nbsp;<font class="l">Euro</font></td>
    </tr>
  <% } %>
   <% if (lSanSos.getSanzionePecuniariaAmmenda() != null) { %>
    <tr>
      <td class="l" colspan="2">Pena Pecuniaria Sostitutiva Ammenda</td>
      <td class="l" colspan="2"><font class="campo"><%=StringUtils.toEuroFormat(lSanSos.getSanzionePecuniariaAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
    </tr>
  <% } %>
<% } %>
  </table>


<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<table>
<tr>
<td class="lNoBord">
<FORM method="POST" name="PenAcc" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penaaccessoria.action.ActLoadInserisciPenaAccessoria&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="U" value="Prosegui" onclick="Javascript:DisabilitaPenAcc();">
 </FORM>
</td>

<%--<td class="lNoBord">
  <FORM method="POST" name="CB" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActLoadInserisciBeneficio&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="button" name="D" value="Concessione Benefici" onclick="Javascript:Disabilita();">
  </FORM>
</td>
--%>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.penacomplessiva.action.ActLoadDettaglioPenaComplessiva&<%=ICostantiPenaComplessiva.CAMPO_ID_PENA_COMPLESSIVA%>=<%=lPenCom.getIdPenaComplessiva()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}  
}else { %>
  <table width="80%" >
  <tr>
        <td class="int" align="left">Pena Complessiva non definita </td>
</tr>
<% }// endif lPenCom != null  %>
 </table>
</body>
</html>