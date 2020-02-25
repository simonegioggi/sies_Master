<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>

<jsp:useBean id="collegio" scope="request" class="siap.sige.collegio.model.CollegioModel"/>
<jsp:useBean id="ruoloMagistrato" scope="request" class="java.lang.String"/>


<% 
// Flag che sta ad indicare che la jsp viene usata includendola in un'altra e quindi vanno nascosti dei campi
boolean lInclude = false;
  if (request.getParameter("include") != null && request.getParameter("include").equalsIgnoreCase("SI"))
  {
	  lInclude = true;
  }
 %>

<% if (!lInclude) { %>
<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Collegio </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>


  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr>
        	<td class="LBG">
        		<a href="Javascript:window.print();">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>	
        		</a>
        	</td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Collegio</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
            	<jsp:param name="CampoIdEntita" value="<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>" />
            	<jsp:param name="ValoreIdEntita" value="<%=collegio.getIdCollegio()%>" />
            </jsp:include>
          </td>
          <!-- BOTTONE DI RITORNO -->
    			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        </tr>
      </table>
    </FORM>
<%} // endif (!lInclude) %>

    <table cellspacing=4 cellpadding=4>
    	<tr>
      	<td class="l">Numero</td>
        <td class="l">
        	<font class="campo"><%=collegio.getCodCollegio()%></font>
        </td>
      </tr>

      <tr>
      	<td class="l">Sezione</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(collegio.getSezione().getDescrizione(), "-")%>
					</font>
        </td>
      </tr>
<%
			if( collegio.getCollegioMagistrati()!= null )
			{
				for( int i=0; i<collegio.getCollegioMagistrati().length; i++ ){
%>			
					<tr>
						<td class="l"><%=i==0 ? "Presidente" : ruoloMagistrato%></font></td>
						<td class="L">
							<%=collegio.getCollegioMagistrati()[i].getMagistrato().getCognome()%>
							&nbsp;
							<%=collegio.getCollegioMagistrati()[i].getMagistrato().getNome()%>
						</td>
					</tr>
<%
				}
			} 
			
			if( collegio.getCollegioGiudiciPopolari()!= null )
			{
				for( int i=0; i<collegio.getCollegioGiudiciPopolari().length; i++ ){
%>
					<tr>
    				<td class="l">Giudice Popolare</td>
						<td class="L">
							<%=collegio.getCollegioGiudiciPopolari()[i].getGiudicePopolare().getCognome()%>
							&nbsp;						
							<%=collegio.getCollegioGiudiciPopolari()[i].getGiudicePopolare().getNome()%>
						</td>
    			</tr>
<% 
				}
			}
			
			if( collegio.getCollegioEsperti()!= null )
			{
				for( int i=0; i<collegio.getCollegioEsperti().length; i++ ){
%>
					<tr>
    				<td class="l">Esperto</td>
						<td class="L">
							<%=collegio.getCollegioEsperti()[i].getEsperto().getCognome()%>
							&nbsp;						
							<%=collegio.getCollegioEsperti()[i].getEsperto().getNome()%>
						</td>
    			</tr>
<% 
				}
			}
%>
<% if (!lInclude) { %>
     	<tr>
     		<td class="l">Data Inizio Validità</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(
        		    DateUtils.getDateToString(collegio.getDataInizioValidita(),"dd-MM-yyyy"),"-")%>
					</font>
        </td>
      </tr>

			<tr>
      	<td class="l">Data Fine Validità</td>
        <td class="l">
        	<font class="campo">
        		<%=StringUtils.toStringJSP(
        		    DateUtils.getDateToString(collegio.getDataFineValidita(),"dd-MM-yyyy"),"-")%>
					</font>
        </td>
      </tr>
    </table>
   </body>
</html>
<%} // endif (!lInclude) %>