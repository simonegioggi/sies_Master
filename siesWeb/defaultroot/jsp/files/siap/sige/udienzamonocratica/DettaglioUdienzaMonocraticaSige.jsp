<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.aula.action.ICostantiAula"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>

<jsp:useBean id="ruoloMagistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="udienzamonocraticasige" scope="request" 
						 class="siap.sige.udienza.model.UdienzaSigeModel"/>
<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio Udienza Monocratica </title>
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
            <font class="campo">Dettaglio Udienza Monocratica</font>
          </td>
          <td class="LBG">
            <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
            	<jsp:param name="CampoIdEntita" value="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>"/>
            	<jsp:param name="ValoreIdEntita" value="<%=udienzamonocraticasige.getIdUdienzaSige()%>"/>
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
						<%=StringUtils.toStringJSP(DateUtils.getDateToString(udienzamonocraticasige.getDataUdienza(),"dd-MM-yyyy"),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Giudice Udienza</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrGiudice(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
      <td class="l">Sezione</td>
      <td class="l">

        <input type="text" name="<%=ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE%>" value="<%=StringUtils.toStringJSP(udienzamonocraticasige.getSezioneModel()!=null ?udienzamonocraticasige.getSezioneModel().getDescrizione():"")%>"
						size="30" maxlength="30" readonly="readonly"> 
      </td>
    </tr>
    
    <tr>
    <td class="l" colspan="2">Aula <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_AULA%>"
						value="<%=StringUtils.toStringJSP(udienzamonocraticasige.getAulaUdienzaModel()!=null?udienzamonocraticasige.getAulaUdienzaModel().getDescrizioneAula():"")%>"
						size="12" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Ingresso <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_INGRESSO%>"
						value="<%=StringUtils.toStringJSP(udienzamonocraticasige.getAulaUdienzaModel()!=null?udienzamonocraticasige.getAulaUdienzaModel().getDescrizioneIngresso():"")%>"
						size="30" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Stanza <input type="text"
						name="<%=ICostantiAula.CAMPO_DESCRIZIONE_STANZA%>"
						value="<%=StringUtils.toStringJSP(udienzamonocraticasige.getAulaUdienzaModel()!=null?udienzamonocraticasige.getAulaUdienzaModel().getDescrizioneStanza():"")%>"
						size="30" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						Piano <input type="text"
						name="<%=ICostantiAula.CAMPO_NUMERO_PIANO%>"
						value="<%=StringUtils.toStringJSP(udienzamonocraticasige.getAulaUdienzaModel()!=null?udienzamonocraticasige.getAulaUdienzaModel().getNumeroPiano():"")%>"
						size="12" maxlength="30" readonly="readonly">&nbsp;&nbsp;
						<input type="HIDDEN" name="<%=ICostantiAula.CAMPO_ID_AULA%>"
						value="<%=StringUtils.toStringJSP(udienzamonocraticasige.getAulaUdienzaModel()!=null?udienzamonocraticasige.getAulaUdienzaModel().getNumeroPiano():"")%>"
						>
					</td>
    </tr>
    
    <!-- intervento per 11.1.2 -->
	  	<tr>
	    	<td class="l">Procuratore della Repubblica</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrProcuratore(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Cancelliere</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getDescrIdAssistente(),"&nbsp;")%>
					</font>&nbsp;
				</td>
	  	</tr>
	  	<tr>
	    	<td class="l">Luogo</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getLuogoUdienza(),"-")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Orario Inizio</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getOraInizio(),"--")%>:<%=StringUtils.toStringJSP(udienzamonocraticasige.getMinInizio(),"--")%>
					</font>&nbsp;
				</td>
	  	</tr>

	  	<tr>
	    	<td class="l">Orario Fine</td>
	    	<td class="l">
					<font class="campo">
						<%=StringUtils.toStringJSP(udienzamonocraticasige.getOraFine(),"--")%>:<%=StringUtils.toStringJSP(udienzamonocraticasige.getMinFine(),"--")%>
					</font>&nbsp;
				</td>
	  	</tr>

		</table>

  </body>
</html>