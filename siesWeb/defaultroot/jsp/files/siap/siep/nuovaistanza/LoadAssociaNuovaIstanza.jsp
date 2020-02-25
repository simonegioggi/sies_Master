<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%--@ taglib prefix="s" uri="/struts-tags" --%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"   scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="oggettoIstanza"   scope="request" class="java.lang.String"/>
<jsp:useBean id="listaIstanze"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="Cumulato"            scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="autoritaEmiCumuloSentenzaDecreto" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmiCumulo"   scope="request" class="java.lang.String"/>
<jsp:useBean id="LuogoUtenteConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCumulo"      scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Asociazione Nuova Istanza a Procedimento</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
   
    //==========================================================================
    //
    //==========================================================================
    function LoadCumulato()
    {
      if (document.f.AnnoCumulato.value.length<4 || document.f.AnnoCumulato.value<1900 || isNaN(document.f.AnnoCumulato.value))
      {
          alert ("Anno Fascicolo Non Valido");
          document.f.AnnoCumulato.focus();
          document.f.CONFERMA.disabled=false;
          return false;
      }
      if (document.f.NumeroCumulato.value.length<=0 || document.f.NumeroCumulato.value<0 || isNaN(document.f.NumeroCumulato.value))
      {
            alert ("Numero Fascicolo Non Valido");
            document.f.NumeroCumulato.focus();
            document.f.CONFERMA.disabled=false;
            return false;
      }
      if (document.f.NumeroCumulato.value > 90000)
      {
            alert ("Numero Fascicolo Non Valido");
            document.f.NumeroCumulato.focus();
            document.f.CONFERMA.disabled=false;
            return false;
      }
      document.f.<%=IWebConstants.ACTION_FIELD%>.value="siap.siep.nuovaistanza.action.ActLoadAssociaRIaFascicoloSIEP";
      document.f.submit();
    }
    
    function Verify()
    {
     
   	}
   	
  </script>
  </head>
 <body class="corpo" >
 <form name="f"  method="POST" action="<%= IWebConstants.PG_MAIN%>" >
   <input type="hidden"  name="Action" value="siap.siep.nuovaistanza.action.ActAssociaRIaFascicoloSIEP" />    
 
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Associazione Registro Istanza a Procedimento</font>
      </td>
    </tr>
  </table>
 
  <table>
      <td class="L">
        <font class="label">Registro Istanza : N.</font>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=fascicolo.getIdFascicoloSiep()%>"
           title="Procedimento">
            <%=fascicolo.getChiaveAnno()%>
            /
            <%=fascicolo.getChiaveProgr()%>
          </a>
  </table>
 
    <jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeDettaglioSoggetto.jsp"/>
    <jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeDettaglioSentenza.jsp"/>
   <br>
  
  <!-- ******************FORM****************** -->
  
  	<table>
	  	<jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeListaIstanzeNoCheck.jsp">
	  	<jsp:param name="formname" value="f" />
	  	</jsp:include> 
	</table>
	<br>

<%
if(listaIstanze.size()==1)
{
%>	
<br>
<%}%>

<%
//==============================================================================
//            Sezione con i dati del fascicolo 
//==============================================================================
if (Cumulato.getChiaveAnno()==null)
{
// Primo caricamento della maschera
%>
<table width=100%>
  <tr><td class=Titolo>Estremi del Procedimento</td></tr>
</table>

<table width=70%>
  <tr><td class=Titolo colspan=2></td></tr>
  <tr>
    <td class="L" width=50%> Anno/Numero SIEP</td>
    <td class="L" >
      <input type="text" title="Anno" name="AnnoCumulato" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
      <input type="text" title="Numero SIEP" name="NumeroCumulato" maxlength="6" size="6">
    </td>
  </tr>
  <tr>
    <td class=lNoBord>
      <input class="bottone" type="button" name="CONFERMA" value="Carica" onClick="Javascript:this.disabled=true;LoadCumulato();">
    </td>
  </tr>
</table>

<%
//==============================================================================
//  DIV                                
//==============================================================================
%>
<%}
else {
//==============================================================================
// Seconda chiamata alla finestra dopo aver inserito gli estremi del fascicolo 
// In questo caso carico i dati del fascicolo selezionato.
//==============================================================================
%>
<br><br>

<table width=100%>
  <tr><td class=Titolo  width=100%>Estremi del Procedimento</td></tr>
</table>
<%
  FascicoloSiepModel lfasCum= new FascicoloSiepModel(Cumulato);
  SentenzaModel lSentenza = lfasCum.getSentenza();
%>
<input type=hidden name="idFascicolo" value="<%=lfasCum.getIdFascicoloSiep()%>">
<input type=hidden name="idSentenza" value="<%=lSentenza.getIdSentenza()%>">
<table cellspacing=0 cellpadding=0 width=95%>
  <tr>
    <td class="L">
      <font class="label">Procedimento : N.</font>
      <font class="campo">
       <%=StringUtils.toStringJSP(lfasCum.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lfasCum.getChiaveProgr())%>
      </font>
    </td>
  </tr>
</table>

<table width="100%" cellspacing=2 cellpadding=2>
	<tr><td class=Titolo colspan=4>Sentenza</td></tr>
    <tr>
  		<td	class="l">Data Provvedimento :</td>

     	<td class="L">	
      		<%if(lfasCum.getSentenza().getDataProvvedimento()!= null)
       		{%>
       			<font class="campo">
       			<%=StringUtils.toStringJSP(DateUtils.getDateToString(lfasCum.getSentenza().getDataProvvedimento(),"dd-MM-yyyy"))%>
        		</font>
       	<%	}else{%>-<%}%>
      	</td>
  	</tr>
  
  	<tr>
      <td class="l">Anno/Numero</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getAnnoSentenza())%></font>&nbsp;
        /
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getNumeroSentenza())%></font>&nbsp;
      </td>
      <td class="l">Autorità Emittente</td>
      <td class="L">
        <font class="campo"><%=StringUtils.toStringJSP(lSentenza.getDescrTipoAutoritaEmittente())%></font>&nbsp;
      </td>
	</tr>
  
<%
String reg="";
String anno_reg="";
String num_reg="";
if (lSentenza.getAnnoRegeCap()!=null)
{
  reg="CAP";
  anno_reg=lSentenza.getAnnoRegeCap()+"";
  num_reg=lSentenza.getNumeroRegeCap()+"";
}
if (lSentenza.getAnnoRegeCas()!=null)
{
  reg="CAS";
  anno_reg=lSentenza.getAnnoRegeCas()+"";
  num_reg=lSentenza.getNumeroRegeCas()+"";
}
if (lSentenza.getAnnoRegeDib()!=null)
{
  reg="DIB";
  anno_reg=lSentenza.getAnnoRegeDib()+"";
  num_reg=lSentenza.getNumeroRegeDib()+"";
}
if (lSentenza.getAnnoRegeCasap()!=null)
{
  reg="CASAP";
  anno_reg=lSentenza.getAnnoRegeCasap()+"";
  num_reg=lSentenza.getNumeroRegeCasap()+"";
}
if (lSentenza.getAnnoRegeGip()!=null)
{
  reg="GIP";
  anno_reg=lSentenza.getAnnoRegeGip()+"";
  num_reg=lSentenza.getNumeroRegeGip()+"";
}
// MEV_66: aggiunte quattro nuove proprietà
if (lSentenza.getAnnoRegeGup() != null) {
	reg = "GUP";
  	anno_reg = lSentenza.getAnnoRegeGup() + "";
  	num_reg = lSentenza.getNumeroRegeGup() + "";
}
if (lSentenza.getAnnoRegeCapsm() != null) {
	reg = "CAPSM";
  	anno_reg = lSentenza.getAnnoRegeCapsm() + "";
  	num_reg = lSentenza.getNumeroRegeCapsm() + "";
}

if (!reg.equals("")) {
%>
<tr>
      <td class="l">Numero Reg.Gen.</td>
      <td class="L"><font class="campo">
      <%=anno_reg%> / <%=num_reg%>&nbsp;&nbsp;&nbsp;  <%=reg%></font>
      </td>
      <td class="l"></td>
      <td class="L"> </td>
</tr>
<%}%>

</table>
<%}
if (Cumulato.getChiaveAnno()!=null)
{
%>

<div id="LayerSubmit" style="visibility:visible; position:relative; ">
  <table>
    <tr>
     <td class="lNoBord"><Input onClick="Javascript:return Verify();" class=bottone type="submit" value="Salva"></td>
    </tr>
  </table>
</div>

<%}%>
 
</form>
<script language="JavaScript" type="text/javascript">

 var frmvalidator  = new Validator("f");


</script>

</body>
</html>
  