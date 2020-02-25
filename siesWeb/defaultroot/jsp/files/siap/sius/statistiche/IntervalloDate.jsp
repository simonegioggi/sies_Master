<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria" %>

<jsp:useBean id="sysdate" scope="request" class="java.lang.String"/>

<jsp:useBean id="SecondoGiro" scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoI" scope="request" class="java.lang.String"/>
<jsp:useBean id="MeseI" scope="request" class="java.lang.String"/>
<jsp:useBean id="GiornoI" scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoF" scope="request" class="java.lang.String"/>
<jsp:useBean id="MeseF" scope="request" class="java.lang.String"/>
<jsp:useBean id="GiornoF" scope="request" class="java.lang.String"/>

<%
String readOnly = "";	
boolean secondoGiro = false;
		if(SecondoGiro !=null && SecondoGiro.equals("SI")) 
		{
			readOnly = "readOnly";	
			secondoGiro = true;
		}
		%>

<table>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo di Tempo da verificare</td></tr>
<tr>
       <td class="L" width="20%" >
        <font class="label">
          Data Iniziale
        </font>
      </td>
      <td class="l" >
        <input type="text" value="<%=GiornoI%>" title="Giorno Iscrizione Iniziale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_INIZIALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"   <%=readOnly%> >
        -
        <input type="text" value="<%=MeseI%>" title="Mese Iscrizione Iniziale" name="<%=ICostantiStatistiche.CAMPO_MESE_INIZIALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%=readOnly%> >
        -
        <input type="text" value="<%=AnnoI%>"  title="Anno Iscrizione Iniziale" name="<%=ICostantiStatistiche.CAMPO_ANNO_INIZIALE%>"
        maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" <%=readOnly%> >

</td>
 <td class="L" >
        <font class="label">
          Data Finale
        </font>
      </td>
      <td class="l">
        <input type="text" value="<%=GiornoF%>"  title="Giorno Iscrizione Finale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_FINALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%=readOnly%> >
        -
        <input type="text" value="<%=MeseF%>"  title="Mese Iscrizione Finale" name="<%=ICostantiStatistiche.CAMPO_MESE_FINALE%>"
        maxlength="2" size="2" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" <%=readOnly%> >
        -
        <input type="text" value="<%=AnnoF%>"  title="Anno Iscrizione Finale" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINALE%>"
        maxlength="4" size="4" onFocus="javascript:textboxSelect(this);enableBtn();" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" <%=readOnly%> >
      </td>
   </tr>
   </table>
        <jsp:include page="<%=ICostantiStatistiche.PG_INCLUDE_COLLABORATORE%>"/>
        <jsp:include page="<%=ICostantiStatistiche.PG_INCLUDE_POSIZIONE_GIURIDICA%>"/>
          <table>
         <% if (secondoGiro) { %> 
           <jsp:include page="<%=ICostantiCancelleriaAssegnataria.PG_CANCELLERIE_COMBO%>"/>
           <%}else { %>
                <jsp:include page="<%=ICostantiCancelleriaAssegnataria.PG_CANCELLERIE_COMBO%>">
               <jsp:param name="nulla" value="nulla"/>  
   			</jsp:include>
   			<%} %>
          </table>
  
      