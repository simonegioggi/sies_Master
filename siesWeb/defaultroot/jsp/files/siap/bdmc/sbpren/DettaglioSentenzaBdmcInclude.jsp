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
  <a class="cliccabileBlu" title="Dettaglio Sentenza" href="<%=ISIAPCostantiWeb.PG_MAIN%>?<%=ISIAPCostantiWeb.ACTION_FIELD%>=siap.bdmc.sbviewprocpena.action.ActLoadDettaglioSbViewProcpena&<%=ICostantiSbViewProcpena.CAMPO_ID_PREN %>=<%=sbviewprocpena.getIdPren() %>&<%=ICostantiSbViewProcpena.CAMPO_FLAG_INFO_SELE %>=<%=sbviewprocpena.getFlagInfoSele() %>" >
         SENTENZA
       </a>
     </td>
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
   
    //if ((annoSentenza != null) && (numeroSentenza != null) && (dataSentenza != null) ) {
	  %>
	  <input type="hidden" name="appoDataArrivoAtto" value="1">
 <table width="100%" >
  <tr>
    <td class="l" width="23%" >Descrizione Ufficio Bdmc </td>
     <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getDescriSedeInst()) %> di <%=StringUtils.toStringJSP(sbviewprocpena.getDescriComuSedeInst()) %></font>&nbsp;</td>
   
   
    <td class="l" width="18%" >Anno/Numero Bdmc</td>   
     <td class="L" width="15%" ><font class="campo"><%=StringUtils.toStringJSP(sbviewprocpena.getAnnoFascBdmc()) %>/<%=StringUtils.toStringJSP(sbviewprocpena.getNumeFascBdmc()) %></font>&nbsp;</td>
  </tr>
  <tr> <td class="l" >Anno/Numero Sentenza</td>
      <td class="L">
     <% if (annoSentenza != null) {  %>
        <input type="hidden" name="appoAnnoSentenza" value="0">
        <font class="campo"><%=StringUtils.toStringJSP(annoSentenza)%></font>&nbsp;
     <%} else { %> 
     <input type="hidden" name="appoAnnoSentenza" value="1">
     <input Title="Anno Sentenza" type="text"  name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_SENT_1GRA %>" maxlength="4" size="4" >
    	
    <%  } %>
     <% if (numeroSentenza != null) {  %>
      <input type="hidden" name="appoNumeroSentenza" value="0">
        /<font class="campo"><%=StringUtils.toStringJSP(numeroSentenza)%></font>&nbsp;
       <%} else { %> 
        <input type="hidden" name="appoNumeroSentenza" value="1">
         /<input Title="Numero Sentenza" type="text"  name="<%= ICostantiSbViewProcpena.CAMPO_NUME_SENT_1GRA %>" maxlength="6" size="6" >
    	
    <%  } %>
      </td>
      <td class="l" >Data Sentenza</td>
      <% if (dataSentenza != null) { %>
       <input type="hidden" name="appoDataSentenza" value="0">
      <td class="L" ><font class="campo"><%=StringUtils.toStringJSP(dataSentenza)%></font>&nbsp;
         <input Title="Data Sentenza" type="hidden" value="<%=StringUtils.toStringJSP(dataSentenza.substring(0,2)) %>" name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA %>" maxlength="2" size="2" >
         <input Title="Data Sentenza" type="hidden" value="<%=StringUtils.toStringJSP(dataSentenza.substring(3,5)) %>" name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA %>" maxlength="2" size="2"  >
         <input Title="Data Sentenza" type="hidden" value="<%=StringUtils.toStringJSP( dataSentenza.substring(6,10)) %>" name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA %>"maxlength="4" size="4" >
      </td>
       <%} else { %> 
        <input type="hidden" name="appoDataSentenza" value="1">
       <td class="L" >
       		 <input Title="Data Sentenza" type="text"  name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_SENTENZA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
         <input Title="Data Sentenza" type="text"  name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_SENTENZA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         <input Title="Data Sentenza" type="text"  name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_SENTENZA %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       	</td>	
        <%  } %>
    </tr>
    <tr>
      <td class="l" >Autorità Emittente</td>
      <td class="L" >
      <% if(descAutoritaEmittente != null){  %>
      <input type="hidden" name="appoAutoEmi" value="0">
        <font class="campo"><%=StringUtils.toStringJSP(descAutoritaEmittente)%> </font>&nbsp;
        <%} else { %> 
        
       <input type="hidden" name="appoAutoEmi" value="1">
          <select  Title="Autorità emittente" name="<%= ICostantiSbPren.CAMPO_AUTORITA %>">
          		<%=autoritaEmi%>
          </select>
    <%  } %>
      </td>
      <td class="l">Data Irrevocabilità</td>
      <td class="L" width="22%">
       	<% 
		if (sbviewprocpena.getDataPassGiud() != null)  {
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
		%>
			<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"dd-MM-yyyy"))%></font>&nbsp;
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_DATA_IRR%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_DATA_IRR %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sbviewprocpena.getDataPassGiud(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        <% } else if ((provvedimento.getSentenza() != null) && (provvedimento.getFascicoloSiep().getDataIrrevocabilita() != null))  {
        %>
        <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(provvedimento.getFascicoloSiep().getDataIrrevocabilita(),"dd-MM-yyyy"))%></font>&nbsp;
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getFascicoloSiep().getDataIrrevocabilita(),"dd")) %>" name="<%= ICostantiSbPren.CAMPO_GIORNO_DATA_IRR%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getFascicoloSiep().getDataIrrevocabilita(),"MM")) %>" name="<%= ICostantiSbPren.CAMPO_MESE_DATA_IRR %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            <input Title="Data Irrevocabilita" type="hidden" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(provvedimento.getFascicoloSiep().getDataIrrevocabilita(),"yyyy")) %>" name="<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRR%>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
        <% 
        } else  { %>
        	<input Title="Data Irrevocabilita" type="text"  name="<%= ICostantiSbPren.CAMPO_GIORNO_DATA_IRR %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
            <input Title="Data Irrevocabilita" type="text"  name="<%= ICostantiSbPren.CAMPO_MESE_DATA_IRR %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            <input Title="Data Irrevocabilita" type="text"  name="<%= ICostantiSbPren.CAMPO_ANNO_DATA_IRR %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        <% } 
		%>	
     </td>
    </tr>
    <tr>
     <% if(descAutoritaEmittente != null){}  else { %> 
         <td class="l" >Luogo Emittente</td>
      <td class="L" >
        
    	 <input Title="Luogo emittente" name="<%=ICostantiSbPren.CAMPO_LUOGO_AUTORITA %>"
            value="<%=descrLuogoEmittente%>" type="text" maxlength="35" size="35">
         <a href="Javascript:ListaComuni('DettaglioProvvedimentoBDMC','<%= ICostantiSbPren.CAMPO_LUOGO_AUTORITA %>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
    	</td>
    <%  } %>

      		<td class="l">Data Arrivo Atto</td>
          	<td class="L" >
	            <input Title="Data Arrivo Atto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sbviewprocpena.getDataArrivoAtto(),"dd")) %>" name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	            <input Title="Data Arrivo Atto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sbviewprocpena.getDataArrivoAtto(),"MM")) %>" name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_ARRIVO_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
	            <input Title="Data Arrivo Atto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(sbviewprocpena.getDataArrivoAtto(),"yyyy")) %>" name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_ARRIVO_ATTO %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
				<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
	         	<%--
	            <input Title="Data Arrivo Atto" type="text" value="" name="<%= ICostantiSbViewProcpena.CAMPO_GIORNO_DATA_ARRIVO_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	          	-<input Title="Data Arrivo Atto" type="text" value="" name="<%= ICostantiSbViewProcpena.CAMPO_MESE_DATA_ARRIVO_ATTO %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	            -<input Title="Data Arrivo Atto" type="text" value="" name="<%= ICostantiSbViewProcpena.CAMPO_ANNO_DATA_ARRIVO_ATTO %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	          	--%>
			</td>
		</tr>  
	</table>
 <%
 if ((annoSentenza != null) && (numeroSentenza != null) && (dataSentenza != null) ) {} else { %>
<table>  

<tr>  
	 <td class="lRosso" >N.B.: Per proseguire con la creazione di un nuovo Fascicolo è necessario inserire le informazioni relative  alla sentenza. </td>
	 </tr> 
</table> 
 <%}} %>
   
</table>
<br>

</html>