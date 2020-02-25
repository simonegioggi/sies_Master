<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.model.DecodeModel"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.decodifiche.util.DecodificheUtils"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeModel"%>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.udienza.model.UdienzaSigeModel"%>

<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="idProvvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="ProvvedimentoEvento"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="ProvvedimentoEventoSospeso"		scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel" />
<jsp:useBean id="ordinanzeSospensione" scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoGiudizio"  scope="request" class="java.lang.String"/>
<jsp:useBean id="UdienzaSige"	scope="request" class="siap.sige.udienza.model.UdienzaSigeModel" />

<%
	boolean retFlag = false;
	retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
	String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

	String lAction = "siap.sige.provvedimento.action.ActLoadDettaglioDataDeposito";
	
	String dataEmissione=DateUtils.getDateToString(ProvvedimentoEvento.getEventoNotifica().getEvento().getDataEmissione(),"dd/MM/yyyy");
	if (dataEmissione==null)
		dataEmissione="-";
	
	String dataUdienza=null;
	if (ProvvedimentoEvento.getProvvedimento()!=null && ProvvedimentoEvento.getProvvedimento().getUdienzaSige()!=null
			&& ProvvedimentoEvento.getProvvedimento().getUdienzaSige().getDataUdienza()!=null){
		dataUdienza=DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getUdienzaSige().getDataUdienza(),"dd/MM/yyyy");
	} else if (UdienzaSige != null && UdienzaSige.getDataUdienza() != null){
		dataUdienza = DateUtils.getDateToString(UdienzaSige.getDataUdienza(),"dd/MM/yyyy");
	}
	
	if (dataUdienza==null)
		dataUdienza="-";

%>
  <table cellspacing=4 cellpadding=4  width=95%>
    <input type="HIDDEN" name="Azione" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%= ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO %>" value="<%=ProvvedimentoEvento.getEventoNotifica().getEvento().getIdEvento()%>" >
    <input type="HIDDEN" name="<%= ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE %>" value="<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige()%>" >
    <tr>
      <td class="Titolo" colspan="2"> Dati Provvedimento</td>
    </tr>
    <% 
	
    	if (ordinanzeSospensione != null && ordinanzeSospensione.size() >0 ) {
			Iterator itx = ordinanzeSospensione.iterator();
			while ( itx.hasNext()) {
				ProvvedimentoSigeModel provvSospensione = (ProvvedimentoSigeModel) itx.next();
				if (provvSospensione == null || provvSospensione.getChiaveAnno() == null || provvSospensione.getChiaveProgr() == null ||
						provvSospensione.getDataDeposito() == null)
					continue;
			%>
			<tr>
	 			<td class="L"><font class="crosso">Ordinanza Sospesa</font></td>
	 			<td class="L"><font class="crosso">Ordinanza di Sospensione N. <%=provvSospensione.getChiaveProgr()%>/<%=provvSospensione.getChiaveAnno()%> del <%=DateUtils.getDateToString(provvSospensione.getDataDeposito(), "dd-MM-yyyy")%></font></td>
			</tr>
		<%}
    	}
    	if (ProvvedimentoEvento.getProvvedimento().getCodTipoProvvedimentoSige().compareTo(ICostantiProvvedimentoSige.COD_ORDINANZA_SOSPENSIONE)==0) { 
    		if (ProvvedimentoEventoSospeso != null && ProvvedimentoEventoSospeso.getProvvedimento() != null &&  
    				ProvvedimentoEventoSospeso.getProvvedimento().getChiaveProgr() != null && 
    					ProvvedimentoEventoSospeso.getProvvedimento().getChiaveAnno() != null && 
    						ProvvedimentoEventoSospeso.getProvvedimento().getDataDeposito() != null) {
    			%>
    			<tr>
      	 		<td class="L"><font class="crosso">Ordinanza Sospesa N. <%=ProvvedimentoEventoSospeso.getProvvedimento().getChiaveProgr()%>/<%=ProvvedimentoEventoSospeso.getProvvedimento().getChiaveAnno()%> del <%=DateUtils.getDateToString(ProvvedimentoEventoSospeso.getProvvedimento().getDataDeposito(), "dd-MM-yyyy")%></font></td>
    			</tr>
    		<%}
    		%>
    <tr>
    </tr>
    <%}%>
    
    <% if (tipoGiudizio!=null) { %> 
	    <tr>
	        <td class="L"><font class="label"> Tipo Rito </font> </td>
		    <td class="L">
		    	<select title="Tipo Rito" name="<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>" disabled="disabled">
		        	<%=tipoGiudizio%>
		    	</select>
		    </td>
	    </tr>
	    <tr>
	      <td class="L"><font class="label"> Data Udienza </font></td>
	      <td class="L"><font class="campo"> <%=dataUdienza%></font></td>
	    </tr>
    <%}%>
    <tr>
      <td class="L"><font class="label"> Data Emissione </font></td>
      <td class="L"><font class="campo"> <%=dataEmissione%></font></td>
    </tr>
<%
		if (ProvvedimentoEvento.getProvvedimento().getDataDeposito() != null)
		{%>
  		<tr>
    		<td class="l"> Anno / Numero <%=DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoProvvedimenti(), ProvvedimentoEvento.getProvvedimento().getCodTipoProvvedimento() )%></td>
    		<td class="l">
      		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAction%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>=<%=ProvvedimentoEvento.getProvvedimento().getIdProvvedimentoSige()%>&<%=ICostantiProvvedimentoSige.CAMPO_ID_EVENTO_GENERATO%>=<%=ProvvedimentoEvento.getProvvedimento().getIdEventoGenerato()%><%=retParam%>">
         		<%=StringUtils.toStringJSP(ProvvedimentoEvento.getProvvedimento().getChiaveAnno())%>
          		/
         		<%=StringUtils.toStringJSP(ProvvedimentoEvento.getProvvedimento().getChiaveProgr())%>
      		</a>
    		</td>
  		</tr>
  		<tr>
    		<td class="l"> Data Deposito in Cancelleria</td>
    		<td class="l"><font class="campo"> <%=DateUtils.getDateToString(ProvvedimentoEvento.getProvvedimento().getDataDeposito(),"dd/MM/yyyy")%></font></td>
  		</tr>
	<%}%>

  	<tr>
    	<td class="l"> Stato del provvedimento</td>
<% 		if (ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null && ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("A"))
			{%>
    		<td class="l"><font class="cRosso">ANNULLATO</font></td>
<% 		} else if (ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != null && ProvvedimentoEvento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato().equalsIgnoreCase("S"))
   		{%>
    		<td class="l"><font class="campo">Validato</font></td>
		<%} else
   		{%>
    		<td class="l"><font class="campo">Da Validare </font></td>
		<%}%>
		</tr>
<%
		if (ProvvedimentoEvento.getEventoNotifica().getEvento().getEveIdEventoRevoca() != null)
		{%>
  		<tr>
    		<td class="l"><font class="crosso"> Revocato</font></td>
			</tr>
	<%}%>

<%--
  if(ProvvedimentoEvento.getProvvedimento().getCodTipoProvvedimentoSige() != null)
  if (ProvvedimentoEvento.getProvvedimento().getCodTipoProvvedimentoSige().compareTo(ICostantiProvvedimentoSige.COD_DECRETO_INAMMISSIBILITA) == 0  )
  {%>
    <tr>
      <td class="L"><font class="label"> Totale giorni concessi</font></td>
      <td class="L"><font class="campo"> <%=StringUtils.toStringJSP(datiOrdinanza.getOrdinanza().getNumGiorniLibanticipata(), "-")%></font></td>
    </tr>
<%}--%>
   </table>   