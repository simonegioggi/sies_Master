<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"  %>

<jsp:useBean id="ListaOggetti"  scope="request" class="java.lang.String"/>
 <%
// Flag per la visualizzazione dell'opzione Elenco Pendenti alla fine del periodo
     boolean pendenti = false;
  if (request.getParameter("pendenti") != null)
  {
	  pendenti = true;
  }
%>
        <table width="70%">
      <tr height=20> <td></td></tr>  
      <%if (ListaOggetti == null || ListaOggetti.length() == 0) { %>
       <td class="label"> <font color="red"> Nessun elemento estratto !</font> </td>
      <% 
      pendenti = false;
      }else { %>
      <tr> <td class="label" >Spuntare questa opzione e selezionare oggetto per produrre anche stampa di dettaglio <input type="checkbox" name="<%=ICostantiStatistiche.CHK_DETTAGLIO%>" value="B" onClick="enableBtn();"></td></tr>
      <tr>    
         <td class="label" >
			<select name="<%=ICostantiStatistiche.CB_LISTA_OGGETTI%>" SIZE=10 onChange="enableBtn();">
			<%=ListaOggetti%>
			</select>        
          </td>
          <%} %>
           </tr>
      </table>
      <%if (pendenti) { %>
       <table width="70%">
      <tr> <td class="label" >Spuntare questa opzione per produrre anche stampa elenco Procedimenti pendenti alla fine del periodo <input type="checkbox" name="<%=ICostantiStatistiche.CHK_PENDENTI%>" value="S" onClick="enableBtn();"></td></tr>
 	</table>
 	<%} %>
 