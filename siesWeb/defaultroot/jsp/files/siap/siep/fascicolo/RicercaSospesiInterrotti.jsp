<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepAggregatoModel" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />
<jsp:useBean id="motivo" scope="request" class="java.lang.String" />
<jsp:useBean id="tipo" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="elencocompleto" scope="request" class="java.lang.String"/>
<jsp:useBean id="lCodMotivo" scope="request" class="java.lang.String"/>
<jsp:useBean id="lCodTipo" scope="request" class="java.lang.String"/>
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Procedimento Sospesi/Interrotti</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo">Elenco Procedimenti Sospesi/Interrotti</font></td>
        <td class="LBG">
          <a href="Javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
      </td>
    </tr>
  </table>
<br>
  <table>
  <tr>
    <td class="titolo" colspan="4">Criteri Selezionati</td>
  </tr>
  <tr>
      <td class="L">
         <font class="label">Anno/Numero Iniziale</font>        
       </td>
      <td class="l">
         <font class="label"><%=StringUtils.toStringJSP(fascicolo.getChiaveAnnoIniziale())%></font>/<font class="label"><%=StringUtils.toStringJSP(fascicolo.getChiaveProgrIniziale())%></font>     
      </td>
      <td class="L">
        <font class="label">Anno/Numero Finale</font>
      </td>
      <td class="L">
        <font class="label"><%=StringUtils.toStringJSP(fascicolo.getChiaveAnnoFinale())%></font>/<font class="label"><%=StringUtils.toStringJSP(fascicolo.getChiaveProgrFinale())%></font>      
      </td>
  </tr> 
  <tr>
    <td class="l">
      Tipo di sospensione/interruzione 
    </td>
     <td class="l" colspan="3">
        <font class="label"><%=StringUtils.toStringJSP(tipo)%></font>
    </td>
  </tr>
  <tr>
    <td class="l">
      Motivo di sospensione/interruzione 
    </td>
     <td class="l" colspan="3">
        <font class="label"><%=StringUtils.toStringJSP(motivo)%></font>
    </td>
  </tr>
 </table> 
 
<br>
<%
String lClasse = "small";
if(elencocompleto != null && elencocompleto.equals("N"))
{
 lClasse = "l";
%>

<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
 
  <form  method="POST"  action="<%= IWebConstants.PG_MAIN%>" name="f">
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=AzioneChiamante%>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" value="<%=StringUtils.toStringJSP(fascicolo.getChiaveAnnoIniziale()) %>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" value="<%=StringUtils.toStringJSP(fascicolo.getChiaveProgrIniziale()) %>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" value="<%=StringUtils.toStringJSP(fascicolo.getChiaveAnnoFinale()) %>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" value="<%=StringUtils.toStringJSP(fascicolo.getChiaveProgrFinale()) %>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_MOTIVO_INT_SOSP%>" value="<%=lCodMotivo %>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_TIPO_INT_SOSP%>" value="<%=lCodTipo %>">
   <input type="HIDDEN" name="elencocompleto" value="S">

  <table>
    <tr>
     <td>
      <a class="cliccabile" href="javascript:document.f.submit();" title="Elenco Completo per Stampa">
       Elenco Completo per Stampa
      </a>
     </td>
    </tr>
  </table>
 </form>
<%}%>

<br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Numero SIEP</td>
      <td class="int">Cognome e Nome</td>      
      <td class="int">Luogo di Nascita</td>         
      <td class="int">Data di Nascita</td>
<%
    if(motivo.equals("Tutti"))
    {	
%>      
      <td class="int">Motivo Sospensione o Interruzione</td>
<%
    }
%>      
      <td class="int">Data Sospensione o Interruzione</td>
      <td class="int">Scadenza Sospensione o Interruzione</td>      
      <td class="int">Periodo Sospensione o Interruzione</td>
<%
    if(!motivo.equals("Tutti"))
    {	
%>      
      <td class="int">Pena Espiata</td>
      <td class="int">Pena residua da espiare</td>      
<%
    }
%>       
    </tr>

<%
    Iterator itx = fascicoli.iterator();
    while ( itx.hasNext())
    {
    	FascicoloSiepAggregatoModel lAggr = (FascicoloSiepAggregatoModel)itx.next();
%>
    <tr>
      <td class="<%=lClasse%>">
       <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lAggr.getFascicoloSiep().getIdFascicoloSiep()%>&NomeAzione=<%=AzioneChiamante%>" title="Procedimento">
          <%=StringUtils.toStringJSP(lAggr.getFascicoloSiep().getChiaveAnno())%>/<%=StringUtils.toStringJSP(lAggr.getFascicoloSiep().getChiaveProgr())%>
       </a>
      </td>
      <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lAggr.getSoggetto().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAggr.getSoggetto().getNome())%></td>
<%
    if("-".equals(lAggr.getSoggetto().getDescrComuneNascita()))
    {	
%> 
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lAggr.getSoggetto().getDescrStatoNascita())%>(<%= StringUtils.toStringJSP(lAggr.getSoggetto().getDescComuneNascitaEstero())%>)</td>

<%  }else
    {	
%>   
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lAggr.getSoggetto().getDescrComuneNascita())%></td>
<%  }
%>
      <td class="<%=lClasse%>"><%=DateUtils.getDateToString(lAggr.getSoggetto().getDataNascita(),"dd/MM/yyyy")%></td>
 <%
    if(motivo.equals("Tutti"))
    {	
%> 
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(lAggr.getEvento().getDescrMotivo())%></td>
<%
    }
    if(lAggr.getMisuraAlternativa().getDataInizioMisura() != null)
    { 	
%> 
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAggr.getMisuraAlternativa().getDataInizioMisura(),"dd/MM/yyyy"))%>&nbsp;</td>
<% 
    }
    else
    {
%>
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAggr.getSospensione().getDataInizio(),"dd/MM/yyyy"))%>&nbsp;</td>

<%   	
    }
if("2141".equals(lAggr.getEvento().getCodMotivo()))
{
%> 
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAggr.getScadenzario().getDataFineScadenza(),"dd/MM/yyyy"))%>&nbsp;</td>
  
<%
}else if("0269".equals(lAggr.getEvento().getCodMotivo())){
%>   
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAggr.getPenaResidua().getDataFine(),"dd/MM/yyyy"))%>&nbsp;</td>
<% 
}else if(!"0274".equals(lAggr.getEvento().getCodMotivo())){
%>   
      <td class="<%=lClasse%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lAggr.getMisuraAlternativa().getDataFineMisura(),"dd/MM/yyyy"))%>&nbsp;</td>
<% 
}else{
%>	
<td class="<%=lClasse%>">&nbsp;</td>
<%	
}
%>
<td class="<%=lClasse%>">
<%
if(lAggr.getVerbale()!= null && lAggr.getVerbale().getNumAnniEspulsione() != null &&
   lAggr.getVerbale().getNumMesiEspulsione() != null && lAggr.getVerbale().getNumGiorniEspulsione() != null 
   &&(lAggr.getVerbale().getNumAnniEspulsione().compareTo(new BigDecimal(0)) != 0 ||
   lAggr.getVerbale().getNumMesiEspulsione().compareTo(new BigDecimal(0)) != 0 || lAggr.getVerbale().getNumGiorniEspulsione().compareTo(new BigDecimal(0)) != 0))
{
%>
          Anni &nbsp;<%=StringUtils.toStringJSP(lAggr.getVerbale().getNumAnniEspulsione(),"0")%>
          Mesi &nbsp;<%=StringUtils.toStringJSP(lAggr.getVerbale().getNumMesiEspulsione(),"0")%>
          Giorni &nbsp;<%=StringUtils.toStringJSP(lAggr.getVerbale().getNumGiorniEspulsione(),"0")%>
<%
}else if(lAggr.getMisuraAlternativa()!= null && lAggr.getMisuraAlternativa().getNumAnniMisura() != null &&
		   lAggr.getMisuraAlternativa().getNumMesiMisura() != null && lAggr.getMisuraAlternativa().getNumGiorniMisura() != null 
		   &&(lAggr.getMisuraAlternativa().getNumAnniMisura().compareTo(new BigDecimal(0)) != 0 ||
		   lAggr.getMisuraAlternativa().getNumMesiMisura().compareTo(new BigDecimal(0)) != 0 || lAggr.getMisuraAlternativa().getNumGiorniMisura().compareTo(new BigDecimal(0)) != 0))
{
%>	  
          Anni &nbsp;<%=StringUtils.toStringJSP(lAggr.getMisuraAlternativa().getNumAnniMisura(),"0")%>
          Mesi &nbsp;<%=StringUtils.toStringJSP(lAggr.getMisuraAlternativa().getNumMesiMisura(),"0")%>
          Giorni &nbsp;<%=StringUtils.toStringJSP(lAggr.getMisuraAlternativa().getNumGiorniMisura(),"0")%>
  
<%	
 
}
%>
     &nbsp;</td> 
<%
    if(!motivo.equals("Tutti"))
    {	
%>      
     <td class="<%=lClasse%>">
          Anni &nbsp;<%=StringUtils.toStringJSP(lAggr.getSospensione().getNumAnniPenaEspiata(),"0")%>
          Mesi &nbsp;<%=StringUtils.toStringJSP(lAggr.getSospensione().getNumMesiPenaEspiata(),"0")%>
          Giorni &nbsp;<%=StringUtils.toStringJSP(lAggr.getSospensione().getNumGiorniPenaEspiata(),"0")%>
     </td>
     <td class="<%=lClasse%>">
          Reclusione :Anni &nbsp;<%=StringUtils.toStringJSP(lAggr.getPenaResidua().getNumAnniReclusione(),"0")%>
          Mesi &nbsp;<%=StringUtils.toStringJSP(lAggr.getPenaResidua().getNumMesiReclusione(),"0")%>
          Giorni &nbsp;<%=StringUtils.toStringJSP(lAggr.getPenaResidua().getNumGiorniReclusione(),"0")%>
          Arresto :Anni &nbsp;<%=StringUtils.toStringJSP(lAggr.getPenaResidua().getNumAnniArresto(),"0")%>
          Mesi &nbsp;<%=StringUtils.toStringJSP(lAggr.getPenaResidua().getNumMesiArresto(),"0")%>
          Giorni &nbsp;<%=StringUtils.toStringJSP(lAggr.getPenaResidua().getNumGiorniArresto(),"0")%>
     </td>
   
<%
    }
%>      
    </tr>
<%
  }
%>
    </table>
  <br>
  </body>
</html>