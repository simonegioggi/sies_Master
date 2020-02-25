<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<jsp:useBean id="sezioni" scope="request" class="java.lang.String"/>

<% 
 // Il default è ricerca x Estremi Ordinanza
 String testo1 = "Anno/Numero";
 String testoDate = "Data Iscrizione";
%>

 <script language="JavaScript">
	function calendario(a_formname,a_field_year,a_field_month,a_field_day)
	{
	  desktop = 
	      window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
	}
 </script>

  <script language="JavaScript" src="<%=ICostantiFascicoloSige.JS_RICERCA_FASCICOLO%>"></script>
      <table style="width: 95%;" >
      <tr>
      <td class="Titolo"  >tipo di intervallo ricerca procedimenti: </td>
      <td class="Titolo"  >
            <%=testo1%> <input type="radio" name="<%=ICostantiFascicoloSige.CAMPO_TIPO_INTERVALLO%>" value="<%=ICostantiFascicoloSige.ESTREMI_PROVVEDIMENTO_INTERVALLO%>"  onClick="VisualizzaEstremiAnnoNum();"  checked >&nbsp;
			<%=testoDate%> <input type="radio" name="<%=ICostantiFascicoloSige.CAMPO_TIPO_INTERVALLO%>" value="<%=ICostantiFascicoloSige.DATE_INTERVALLO%>" onClick="VisualizzaEstremiDate()" ></td>
      </tr>
      </table>
     <div id="contenitore" style="position: relative; top: 0; left: 0;  " >         
    <div id="EstremiAnnoNum" style="position:relative;  top: 0; left: 0; " >  
      <table  width=65%>
      <tr>
        <td class="l" >
          <font class="label">Anno/Numero Iniziale <font class=ob>(*)</font></font>
        </td>
        <td class="l">
          <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_INI%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          <input type="text" title="Numero Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_NUM_INI_ORIGIN%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          <input type="hidden" name="<%=ICostantiFascicoloSige.CAMPO_NUM_INI%>" value="">
        </td>
        </tr>
        <tr>
        <td class="l" >
          <font class="label">Anno/Numero Finale </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          <input type="text" title="Numero Finale" name="<%=ICostantiFascicoloSige.CAMPO_NUM_FINE_ORIGIN%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          <input type="hidden" name="<%=ICostantiFascicoloSige.CAMPO_NUM_FINE%>" value="">
        </td>
        </tr>
        <tr>
        <td class="l">
          <font class="label">Sezione </font>
        </td>
        <td class="l">
            <select name="<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>">
            <%=sezioni %>
            </select>
        </td>
        </tr>
              
         </table>
   </div>     
      <div id="EstremiDate" style="position: absolute; top: 0; left: 0; visibility:hidden; ">      
       <table  width="65%">
        <tr>
          <td class="l">
            <font class="label"> Data Iniziale <font class=ob>(*)</font></font>
          </td>
          <td class="l">
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('f','<%=ICostantiFascicoloSige.CAMPO_ANNO_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_INIZIALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_INIZIALE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
          </td>
          </tr>
          <tr>
          <td class="l" >
            <font class="label">Data Finale <font class=ob>(*)</font></font>
          </td>
          <td class="l" >
            <input type="text" title="Giorno Finale" name="<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">

			<!-- MEV 15 - Revisione SIGE -->
			<a href="javascript:calendario('f','<%=ICostantiFascicoloSige.CAMPO_ANNO_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_MESE_FINALE%>','<%=ICostantiFascicoloSige.CAMPO_GIORNO_FINALE%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
          </td>
        </tr>
        
        <tr>
        <td class="l">
          <font class="label">Sezione </font>
        </td>
        <td class="l">
            <select name="<%=ICostantiFascicoloSige.CAMPO_SEZ_ID_SEZIONE%>">
            <%=sezioni %>
            </select>
        </td>
        </tr>
      </table>
      </div> 
   </div>
        <br>
        <table>
        <tr>
         <td class="label">
           <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="ValidatorEstesa();">
         </td>
        </tr>
    </table>
    <br>
 

   