<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.aula.action.ICostantiAula"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="ruoloMagistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProcuratore" scope="request" class="java.lang.String"/>
<jsp:useBean id="udienzasige" 		scope="request" class="siap.sige.udienza.model.UdienzaSigeModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Udienza Collegiale </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

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
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
            	<jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>"/>
            	<jsp:param name="ValoreIdEntita" value="<%=udienzasige.getIdUdienzaSige()%>"/>
            </jsp:include>
          </td>
          <!-- BOTTONE DI RITORNO -->
    			<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
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

<%--       <tr>
      	<td class="l">Numero</td>
        <td class="l">
        	<font class="campo"><%=udienzasige.getCollegio().getCodCollegio()%></font>
        </td>
      </tr> --%>

      <tr>
      	<td class="l">Sezione</td>
        <td class="l">

        <input type="text" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>" value="<%=StringUtils.toStringJSP(udienzasige.getSezioneModel()!=null ?udienzasige.getSezioneModel().getDescrizione():"")%>"
						size="30" maxlength="30" readonly="readonly">
        </td>
      </tr>
<%
			if( udienzasige.getCollegio().getCollegioMagistrati()!= null )
			{
				for( int i=0; i<udienzasige.getCollegio().getCollegioMagistrati().length; i++ ){
%>			
					<tr>
						<td class="l"><%=i==0 ? "Presidente" : ruoloMagistrato%></font></td>
						<td class="L">
							<%=udienzasige.getCollegio().getCollegioMagistrati()[i].getMagistrato().getCognome()%>
							&nbsp;
							<%=udienzasige.getCollegio().getCollegioMagistrati()[i].getMagistrato().getNome()%>
						</td>
					</tr>
<%
				}
			} 
			
			if( udienzasige.getCollegio().getCollegioGiudiciPopolari()!= null )
			{
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
			
			if( udienzasige.getCollegio().getCollegioEsperti()!= null )
			{
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
	  	<tr>
	    	<td class="l"><%=tipoProcuratore%></td>
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
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%-- 20090402 Come richiesto dall' amministrazione
	  	<tr>
	    	<td class="l">Num. Max. Fascicoli</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzasige.getNumeroMaxFascicoli(),"-")%>
					</font>&nbsp;
				</td>
	  	</tr>
--%>

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