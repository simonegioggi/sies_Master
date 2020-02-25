<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel"%>
<%@ page import="siap.bdmc.sbviewprocpena.action.ICostantiSbViewProcpena"%>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>
<jsp:useBean id="provvedimento"  scope="request" class="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="lVectProcPena" scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>
<jsp:useBean id="descrLuogoEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoAutoritaEmittente" scope="request" class="java.lang.String"/>

<html>

<%
String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";

%>

 <%
 lVectProcPena=provvedimento.getSbViewProcpena();
 if (( lVectProcPena != null) && (lVectProcPena.size() != 0))
 { 
   SbViewProcpenaModel sbviewprocpena = (SbViewProcpenaModel)lVectProcPena.get(0);
          %>
 <table cellspacing=1 cellpadding=1 width="100%" border=<%=isBorder%>>
<tr>  <td class="LBGISI" width=<%=largh%> valign="middle">
         SENTENZA
      
     </td>
     <td> </td>
    <td>
          

  <% 
  	BigDecimal annoSentenza = null;
 	BigDecimal numeroSentenza= null;
  	String dataSentenza = null;
  	String descAutoritaEmittente = null;
  	boolean flagAutorita = false;
    if (sbviewprocpena.getDataDeciCass() != null){
    	flagAutorita = true;
    	annoSentenza = sbviewprocpena.getAnnoDeciCass();
    	numeroSentenza = sbviewprocpena.getNumeDeciCass();
    	dataSentenza = DateUtils.getDateToString(sbviewprocpena.getDataDeciCass(),"dd-MM-yyyy");
    	descAutoritaEmittente = "CORTE DI CASSAZIONE";
    }
    if ((!flagAutorita)&& (sbviewprocpena.getDataSent2gra() != null) ){
    	flagAutorita = true;
	annoSentenza = sbviewprocpena.getAnnoSent2gra();
	numeroSentenza = sbviewprocpena.getNumeSent2gra();
	dataSentenza = DateUtils.getDateToString(sbviewprocpena.getDataSent2gra(),"dd-MM-yyyy");
	descAutoritaEmittente = sbviewprocpena.getDescriUffiCoap()+" di "+sbviewprocpena.getDescriComuUffiCoap();
}
    if ((!flagAutorita)&& (sbviewprocpena.getDataSent1gra() != null) ){
    	flagAutorita = true;
	annoSentenza = sbviewprocpena.getAnnoSent1gra();
	numeroSentenza = sbviewprocpena.getNumeSent1gra();
	dataSentenza = DateUtils.getDateToString(sbviewprocpena.getDataSent1gra(),"dd-MM-yyyy");
	descAutoritaEmittente = sbviewprocpena.getDescriUffiDibb()+" di "+sbviewprocpena.getDescriComuUffiDibb();
} 
    if ((!flagAutorita)&& (sbviewprocpena.getDataSentGippGupp() != null) ){
    	flagAutorita = true;
	annoSentenza = sbviewprocpena.getAnnoSentGippGupp();
	numeroSentenza = sbviewprocpena.getNumeSentGippGupp();
	dataSentenza = DateUtils.getDateToString(sbviewprocpena.getDataSentGippGupp(),"dd-MM-yyyy");
	descAutoritaEmittente = sbviewprocpena.getDescriUffiGipp()+" di "+sbviewprocpena.getDescriComuUffiGipp();
} 
     if ((annoSentenza != null) && (numeroSentenza != null) && (dataSentenza != null) ) {

    // if (annoSentenza != null) {  %>
 <table width="100%" >
  <tr>
    <td class="l" width="23%" >Descrizione Ufficio Bdmc </td>
     <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescriSedeInst()) %> di <%=StringUtils.toStringJSP(sbviewprocpena.getDescriComuSedeInst()) %></font>&nbsp;</td>
   
   
    <td class="l" width="18%" >Anno/Numero Bdmc</td>   
     <td class="L" width="15%" ><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoFascBdmc()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr> <td class="l" >Anno/Numero Sentenza</td>
      <td class="L">
    
       
        <font class="campo"><%=StringUtils.toStringJSP(annoSentenza)%></font>&nbsp;
    
        /<font class="campo"><%=StringUtils.toStringJSP(numeroSentenza)%></font>&nbsp;
     
      </td>
      <td class="l" >Data Sentenza</td>
     
      <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(dataSentenza)%></font>&nbsp;
           </td>
      
    </tr>
    <tr>
      <td class="l" >Autorità Emittente</td>
      	<td class="L" >
       		<font class="campo"><%=StringUtils.toStringJSP(descAutoritaEmittente)%> </font>&nbsp;
		</td>
      	<td class="l">Data Irrevocabilità</td>
      	<td class="L" width="22%">
			<%   if (sbviewprocpena.getDataPassGiud() != null)  {%>
	        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"dd-MM-yyyy"))%></font>&nbsp;
	        <% 
	        /* 
			 * ISSUE MEV : dataIrrevocabilita non esiste per la sentenza bensì per il fascicolo
			 * Numero MEV : SIES v10
			 * Autore    : gioggi
			 * Data      : 28/gen/2016
			 * Branch    : MEV_SIES v10
			 */
			// EX: provvedimento.getSentenza().getDataIrrevocabilita()
			// NEW: provvedimento.getFascicoloSiep().getDataIrrevocabilita()
			//***** FINE INTERVENTO MEV_SIES v10 *****//
	       	} else if ((provvedimento.getSentenza() != null) && (provvedimento.getFascicoloSiep().getDataIrrevocabilita() != null))  {%>
	        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getFascicoloSiep().getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;
	       	<% } %>	
		</td>
	</tr>
</table>
<%} else { %>
<table cellspacing=1 cellpadding=1 width="50%" >
	<tr>
		<td class="l">
			<font class="cGrigio">Nessuna  Sentenza prenotata</font>
		</td>
	</tr>
</table>
  <% }} %>	 
</table>
<br>

</html>