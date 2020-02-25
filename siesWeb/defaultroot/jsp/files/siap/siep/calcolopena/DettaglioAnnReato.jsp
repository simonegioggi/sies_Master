<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>

<jsp:useBean id="annotazioni"     scope="request" class="java.util.Vector" />
<jsp:useBean id="penacomplessiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />


<html>
  <head>
    <title> [S.I.E.S.] - Dettaglio Evento - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>


<BODY class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>       <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Annotazione Manuale per Reato</font>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <table>
    <tr>
      <td colspan=19 class=Titolo>Pena Complessiva</td>
    </tr>
    <tr>
      <td class="l"><font  class="label">Reclusione : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penacomplessiva.getNumAnniReclusione(),"0")%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penacomplessiva.getNumMesiReclusione(),"0")%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniReclusione(),"0")%></font></td>
      <td class="l"><font class="label">Multa</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(penacomplessiva.getImportoMulta())%></font></td>
    </tr>

    <tr>
      <td class="l"><font  class="label">Arresto :</font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penacomplessiva.getNumAnniArresto(),"0")%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penacomplessiva.getNumMesiArresto(),"0")%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(penacomplessiva.getNumGiorniArresto(),"0")%></font></td>
      <td class="l"><font class="label">Ammenda</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(penacomplessiva.getImportoAmmenda())%></font></td>
    </tr>
  </table>

  <br>
  
  <table width=100%>
    <tr>
      <td colspan=6 class=Titolo>Annotazione Manuale</td>
    </tr>
	  <tr>
      <td class="int">Tipo Annotazione</td>
      <td class="int">+/-</td>
      <td class="int">Reclusione</td>
      <td class="int">Arresto</td>
      <td class="int">Ordinanza Declaratoria G.E.</td>
      <td class="int">Data Ricezione del Documento</td>
    </tr>

  <%
  AnnotazioneManualeModel lAnnMod;

  boolean lFlagAnnoNumero;
  for (int i=0;i<annotazioni.size();i++)
  {
    lAnnMod=(AnnotazioneManualeModel)annotazioni.get(i);

 %>
  <tr>

      <td class="l"><font class="campo"><%= StringUtils.toStringJSP(lAnnMod.getDescrTipoAnnotazione())%></font>&nbsp;</td>
      <td class="c"><font class="campo"><%= StringUtils.toStringJSP(lAnnMod.getFlagPiuMeno()) %></font>&nbsp;</td>
      <td class="l">
      <table>
         <td class="lnobord"><font class="label">Anni</font></td>
         <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniReclusione(),"0")%></font></td>
         <td class="lnobord"><font class="label">Mesi</font></td>
         <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiReclusione(),"0")%></font></td>
         <td class="lnobord"><font class="label">Giorni</font></td>
         <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniReclusione(),"0")%></font></td>
       </table>
      </td>
      <td class="l">
        <table>
         <td class="lnobord"><font class="label">Anni</font></td>
         <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumAnniArresto(),"0")%></font></td>
         <td class="lnobord"><font class="label">Mesi</font></td>
         <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumMesiArresto(),"0")%></font></td>
         <td class="lnobord"><font class="label">Giorni</font></td>
         <td class="rnobord"><font class="campo"><%=StringUtils.toStringJSP(lAnnMod.getNumGiorniArresto(),"0")%></font></td>
        </table>
       </td>
<%if(lAnnMod.getAnnoGe() != null && lAnnMod.getNumeroGe() != null)
  {%>
       <td class="c"><font class="campo"><%= StringUtils.toStringJSP(lAnnMod.getAnnoGe())+"/"+StringUtils.toStringJSP(lAnnMod.getNumeroGe()) %></font>&nbsp;</td>
<%}else{%>
     <td class="l"> &nbsp;</td>
<%}%>
       <td class="c"><font class="campo"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lAnnMod.getDataRicezioneDoc(),"dd/MM/yyyy"))%></font>&nbsp;</td>


</tr>
<%} %>
</table>
</body>
</html>