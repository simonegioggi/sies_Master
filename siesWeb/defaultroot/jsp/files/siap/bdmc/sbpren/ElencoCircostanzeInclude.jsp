<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="siap.bdmc.sbpren.action.ICostantiSbPren"%>

<%@ page import="siap.bdmc.sbviewprocpena.model.SbViewProcpenaModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>


<jsp:useBean id="provvedimento"  scope="request" class="siap.bdmc.sbpren.model.ProvvedimentoModelBDMC"/>


<html>

<%

String isVALIGN = "top";
String isBorder = "0";
String lWidth = "96%";
String largh = "8%";
String resto = "92%";

      Vector lCircostanze = provvedimento.getSbViewProcpena();
	  boolean flagCircostanza = false;
	  String visuCircos = " ";
      if(lCircostanze != null && lCircostanze.size() != 0)
      {
       	SbViewProcpenaModel lCircostanza = (SbViewProcpenaModel)lCircostanze.get(0);
		//ART. 89
        if (lCircostanza.getFlagArti0089()!=null && lCircostanza.getFlagArti0089().equals("1")){
			
            flagCircostanza = true;
           
            visuCircos += "<tr>";
            visuCircos += "<td class=\"c\">1</td>";
            visuCircos += "<td class=\"l\">     <font class=\"campo\">";
            visuCircos += " </font>";
            visuCircos += " ART. 89 C.P.</td>";
           visuCircos += " </tr>";
		
		
		}
		//ART. 90
		if (lCircostanza.getFlagArti0090()!=null && lCircostanza.getFlagArti0090().equals("1")){
				
	            flagCircostanza = true;	
	        visuCircos += "    <tr>";
	        visuCircos += "    <td class=\"c\">2</td>";
	        visuCircos += "    <td class=\"l\">     <font class=\"campo\">";
	        visuCircos += "     </font>";
	        visuCircos += "     ART. 90 C.P.</td>";
	        visuCircos += "    </tr>";
			
			
		}
		//ART. 91
		if (lCircostanza.getFlagArti0091()!=null && lCircostanza.getFlagArti0091().equals("1")){
				
			   flagCircostanza = true;	
	          visuCircos += "  <tr>";
	          visuCircos += "  <td class=\"c\">3</td>";
	          visuCircos += "  <td class=\"l\">     <font class=\"campo\">";
	          visuCircos += "   </font>";
	          visuCircos += "   ART. 91 C.P.</td>";
	          visuCircos += "  </tr>";
			
			
		}
		//ART. 92
		if (lCircostanza.getFlagArti0092()!=null && lCircostanza.getFlagArti0092().equals("1")){
			
	            flagCircostanza = true;
	            visuCircos += " <tr>";
	          visuCircos += "  <td class=\"c\">4</td>";
	          visuCircos += "  <td class=\"l\">     <font class=\"campo\">";
	          visuCircos += "   </font>";
	          visuCircos += "   ART. 92 C.P.</td>";
	          visuCircos += "  </tr>";
			
			
		}
		//ART. 93
		if (lCircostanza.getFlagArti0093()!=null && lCircostanza.getFlagArti0093().equals("1")){
				
	            flagCircostanza = true;	
	       visuCircos += "     <tr>";
	       visuCircos += "     <td class=\"c\">5</td>";
	       visuCircos += "     <td class=\"l\">     <font class=\"campo\">";
	       visuCircos += "      </font>";
	       visuCircos += "      ART. 93 C.P.</td>";
	       visuCircos += "     </tr>";
			
			
		}
		//ART. 94
		if (lCircostanza.getFlagArti0094()!=null && lCircostanza.getFlagArti0094().equals("1")){
			
	            flagCircostanza = true;
	       visuCircos += "     <tr>";
	       visuCircos += "     <td class=\"c\">6</td>";
	       visuCircos += "    <td class=\"l\">     <font class=\"campo\">";
	       visuCircos += "      </font>";
	       visuCircos += "      ART. 94 C.P.</td>";
	       visuCircos += "     </tr>";
			
			
		}
		//ART. 95
		if (lCircostanza.getFlagArti0095()!=null && lCircostanza.getFlagArti0095().equals("1")){
				
	            flagCircostanza = true;	
	      	visuCircos += "	      <tr>";
	        visuCircos += "    <td class=\"c\">7</td>";
	        visuCircos += "    <td class=\"l\">     <font class=\"campo\">";
	        visuCircos += "    </font>";
	        visuCircos += "     ART. 95 C.P.</td>";
	        visuCircos += "    </tr>";
			
			
		}
		//ART. 96
		if (lCircostanza.getFlagArti0096()!=null && lCircostanza.getFlagArti0096().equals("1")){
				
	            flagCircostanza = true; 	
	        visuCircos += "    <tr>";
	        visuCircos += "    <td class=\"c\">8</td>";
	        visuCircos += "    <td class=\"l\">     <font class=\"campo\">";
	        visuCircos += "     </font>";
	        visuCircos += "    ART. 96 C.P.</td>";
	        visuCircos += "   </tr>";
			
			
		}
		//ART. 97
		if (lCircostanza.getFlagArti0097()!=null && lCircostanza.getFlagArti0097().equals("1")){
			
	            flagCircostanza = true;	
	          visuCircos += "  <tr>";
	          visuCircos += "  <td class=\"c\">9</td>";
	          visuCircos += "  <td class=\"l\">     <font class=\"campo\">";
	          visuCircos += "   </font>";
	          visuCircos += "   ART. 97 C.P.</td>";
	          visuCircos += "  </tr>";
			
			
		}
		//ART. 98
		if (lCircostanza.getFlagArti0098()!=null && lCircostanza.getFlagArti0098().equals("1")){
				
	            flagCircostanza = true;	
	         visuCircos += "   <tr>";
	         visuCircos += "   <td class=\"c\">10</td>";
	         visuCircos += "   <td class=\"l\">     <font class=\"campo\">";
	         visuCircos += "    </font>";
	         visuCircos += "    ART. 98 C.P.</td>";
	         visuCircos += "   </tr>";
			
			
		}
		//ART. 99
		if (lCircostanza.getFlagArti0099()!=null && lCircostanza.getFlagArti0099().equals("1")){
			
	           flagCircostanza = true;	
	          visuCircos += "  <tr>";
	          visuCircos += "  <td class=\"c\">11</td>";
	          visuCircos += "  <td class=\"l\">     <font class=\"campo\">";
	          visuCircos += "   </font>";
	          visuCircos += "   ART. 99 C.P.</td>";
	          visuCircos += "  </tr>";
			
			
		}
		//ART. 62
		if (lCircostanza.getFlagArti62()!=null && lCircostanza.getFlagArti62().equals("1")){
			
	           flagCircostanza = true;	
	          visuCircos += "  <tr>";
	          visuCircos += "  <td class=\"c\">12</td>";
	          visuCircos += "  <td class=\"l\">     <font class=\"campo\">";
	          visuCircos += "   </font>";
	          visuCircos += "   ART. 62 C.P.</td>";
	          visuCircos += "  </tr>";
			
			
		}
		//ART. 62 COMMA
		if (lCircostanza.getArti0062Comm()!=null && lCircostanza.getArti0062Comm().equals("1")){
				
	           flagCircostanza = true;
	          visuCircos += "  <tr>";
	          visuCircos += "  <td class=\"c\">13</td>";
	          visuCircos += "  <td class=\"l\">     <font class=\"campo\">";
	          visuCircos += "   </font>";
	          visuCircos += "   ART. 62 COMMA C.P.</td>";
	          visuCircos += "  </tr>";
			
			
		}
		//ART. 62 BIS
		if (lCircostanza.getFlagArt62bi()!=null && lCircostanza.getFlagArt62bi().equals("1")){
				
	            flagCircostanza = true; 
	        visuCircos += "    <tr>";
	        visuCircos += "    <td class=\"c\">14</td>";
	        visuCircos += "    <td class=\"l\">     <font class=\"campo\">";
	        visuCircos += "     </font>";
	        visuCircos += "     ART. 62 BIS C.P.</td>";
	        visuCircos += "    </tr>";
			
			
		}%>
		
      <% }
     if (!flagCircostanza)
            {%>      		
       		
        		<td class="l" width="41%" colspan=1 >
       			<font class="cGrigio"> Nessuna Circostanza prenotata</font>
      			</td>
      			<td class="label" width="41%" ></td>
    		</tr>
    		 </table>
<%} else {%>
		  <td class="label" width=<%=resto%> colspan=1>
       <a><img align="left" name="image3" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>collapse.gif"  onClick="return effettoTree(3);" alt="" border=0></a>
		  &nbsp;</td></tr>
		 </table>
		 <div id="elenco3" style="width: 100%; display:block" >
		<table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
			  <tr>  <td width=<%=largh%> align="center"><input type="checkbox" name="<%=ICostantiSbPren.CAMPO_CHECK_CIRCOSTANZA%>" value="<%=ICostantiSbPren.CAMPO_CHECK_CIRCOSTANZA%>" checked="checked"/></td>
			  <td>
					<table cellspacing=1 cellpadding=1 width="50%">
			        	<tr>
					    	<td class="LBGISIV" width="10%"><font class="campoLow">Progr.</font>  </td>
			      			<td class="LBGISIV" width="90%"><font class="campoLow">Circostanza </font></td>
			 		  	</tr>
			 		  	<%=visuCircos  %>
			        </table>
	         </td></tr>
 		</table>
 		<br>
 </div>
    <%}%>   
</html>