<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.aula.action.ICostantiAula"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="ruoloMagistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProcuratore" scope="request" class="java.lang.String"/>
<jsp:useBean id="udienzasige" scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>
<jsp:useBean id="magistratiArray" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="procuraDesc" scope="request" class="java.lang.String"/>
<jsp:useBean id="giudiciPopolari" scope="request" class="java.lang.String"/>
<jsp:useBean id="NumeroUdienzeMagistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="returnModifica" scope="request" class="java.lang.String"/>
<jsp:useBean id="PopUp" scope="request" class="java.lang.String"/>

<html>
  	<head>
    <title>[S.I.E.S.] - Dettaglio Udienza Collegiale Fix </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">
    function confermaBack () {
        try {
    	    var dataUdienza = '<%=DateUtils.getDateToString(udienzasige.getDataUdienza(), "dd-MM-yyyy")%>';
        	var idUdienza = <%=udienzasige.getIdUdienzaSige()%>;
        	var idCollegio = <%=(udienzasige.getCollegio() != null ? udienzasige.getCollegio().getIdCollegio() : "")%>;
        	<%-- 20171005: [SG] se esiste considero l'ID sezione e lo passo nella funzione setUdienza --%>
        	var idSezione = <%=(udienzasige.getCollegio() != null ? udienzasige.getCollegio().getSezIdSezione() : "")%>;
        	window.opener.setUdienza(idUdienza, dataUdienza, idCollegio, idSezione);
        	self.close();
        } catch (e) {
        	history.back();	
        }
    }

//     function init() {
<%-- <% --%>
// if (NumeroUdienzeMagistrato!=null && !"".equals(NumeroUdienzeMagistrato)) {
// 	String mess = "";
// 	if (NumeroUdienzeMagistrato.trim().equals("1")){
// 		mess = "Udienza del magistrato assegnatario trovata";
// 	} else {
// 		mess = "Trovate "+NumeroUdienzeMagistrato+" udienze del magistrato assegnatario, è stata selezionata la più recente";
// 	}
<%-- %>    	 --%>
<%-- alert ("<%=mess%>");	 --%>
<%-- <% --%>
// }
<%-- %> --%>
//     }
    </script>    
  </head>
<%-- 20171013: [SG] rimosso tale messaggio: onload="init()" --%>
  <body class="corpo">
    <FORM name="comandi">
      <table>
        <tr>
        	<td class="LBG">
        		<a href="Javascript:window.print();">
        			<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>	
        		</a>
        	</td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Dettaglio Udienza Collegiale</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_FIX%>">
            	<jsp:param name="funzioni" value="si"/>
            	<jsp:param name="tipoRito" value="C"/>
            	<jsp:param name="PopUp" value="<%=PopUp%>"/>
            </jsp:include>
<%
		if(returnModifica != null && returnModifica.equals("S")){
			if(PopUp != null && !PopUp.equals("") && (PopUp.equals("Y") || PopUp.equals("yes") )){
%>            
	            <a href="javascript:confermaBack()">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
				</a>
<%
			} else {
%>
	            <a href="Main.jsp?Action=siap.sige.udienza.action.ActLoadFSigePFissazioneUdienza">
					<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
				</a>
<%				
			}
		} else {
%> 
            <a href="javascript:confermaBack()">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
			</a>
<%
		}
%>          
          </td>
          
          
        </tr>
      </table>
    </FORM>

		<table cellspacing=4 cellpadding=4>
	  	<tr>
	    	<td class="l">Data Udienza</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(udienzasige.getDataUdienza(),"dd-MM-yyyy"),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

      <tr>
      	<td class="l">Sezione</td>
        <td class="l">
        	<font class="campo">
        		<%
        		try {
        		    out.println (StringUtils.toStringJSP(udienzasige.getCollegio().getSezione().getDescrizione(), "-"));
        		} catch (Exception e) {
        		}
        		%>
			</font>
        </td>
      </tr>

<%
			if( udienzasige.getCollegio() != null && udienzasige.getCollegio().getCollegioMagistrati()!= null )			{
				for( int i=0; i<udienzasige.getCollegio().getCollegioMagistrati().length; i++ ){
					String magDesc = (String)magistratiArray.get(i);
%>			
					<tr>
						<td class="l"><%=magDesc%></font></td>
						<td class="L">
							<%=udienzasige.getCollegio().getCollegioMagistrati()[i].getMagistrato().getCognome()%>
							&nbsp;
							<%=udienzasige.getCollegio().getCollegioMagistrati()[i].getMagistrato().getNome()%>
						</td>
					</tr>
<%
				}
			} 
			
			if( udienzasige.getCollegio() != null && udienzasige.getCollegio().getCollegioGiudiciPopolari()!= null )			{
				for( int i=0; i<udienzasige.getCollegio().getCollegioGiudiciPopolari().length; i++ ){
%>
					<tr>
    				<td class="l">Giudice Popolare</td>
						<td class="L">
							<%=udienzasige.getCollegio().getCollegioGiudiciPopolari()[i].getGiudicePopolare().getCognome()%>
							&nbsp;						
							<%=udienzasige.getCollegio().getCollegioGiudiciPopolari()[i].getGiudicePopolare().getNome()%>
						</td>
    			</tr>
<% 
				}
			}
			
			if( udienzasige.getCollegio() != null && udienzasige.getCollegio().getCollegioEsperti()!= null )			{
				for( int i=0; i<udienzasige.getCollegio().getCollegioEsperti().length; i++ ){
%>
					<tr>
    				<td class="l">Esperto</td>
						<td class="L">
							<%=udienzasige.getCollegio().getCollegioEsperti()[i].getEsperto().getCognome()%>
							&nbsp;						
							<%=udienzasige.getCollegio().getCollegioEsperti()[i].getEsperto().getNome()%>
						</td>
    			</tr>
<% 
				}
			}
%>

<%
if (!"yes".equals(giudiciPopolari)) {
%>
	  	<tr>
	    	<td class="l"><%=procuraDesc%></td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzasige.getDescrProcuratore(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Cancelliere</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzasige.getDescrIdAssistente(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
<%
}
%>

<!-- INTERVENTO PER 11.2.1 -->
<tr>
 <td class="l" colspan="2">Aula <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>"
						value="<%=StringUtils.toStringJSP(udienzasige.getAulaUdienzaModel()!=null?udienzasige.getAulaUdienzaModel().getDescrizioneAula():"")%>"
						size="12" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Ingresso <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>"
						value="<%=StringUtils.toStringJSP(udienzasige.getAulaUdienzaModel()!=null?udienzasige.getAulaUdienzaModel().getDescrizioneIngresso():"")%>"
						size="30" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Stanza <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>"
						value="<%=StringUtils.toStringJSP(udienzasige.getAulaUdienzaModel()!=null?udienzasige.getAulaUdienzaModel().getDescrizioneStanza():"")%>"
						size="30" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Piano <input type="text"
						name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>"
						value="<%=StringUtils.toStringJSP(udienzasige.getAulaUdienzaModel()!=null?udienzasige.getAulaUdienzaModel().getNumeroPiano():"")%>"
						size="12" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						<input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_AULA%>"
						value="<%=StringUtils.toStringJSP(udienzasige.getAulaUdienzaModel()!=null?udienzasige.getAulaUdienzaModel().getNumeroPiano():"")%>"
						>
					</td>
    </tr>
    
    <!-- intervento per 11.1.2 -->
	  	<tr>
	  	
	  	<tr>
	    	<td class="l">Luogo</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzasige.getLuogoUdienza(),"-")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Orario Inizio</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzasige.getOraInizio(),"--")%>:
						<%=StringUtils.toStringJSP(udienzasige.getMinInizio(),"--")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Orario Fine</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzasige.getOraFine(),"--")%>:
						<%=StringUtils.toStringJSP(udienzasige.getMinFine(),"--")%>
					</font>&nbsp;
				</td>
	  	</tr>
	  	
		</table>
  </body>
</html>