<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel"%>
<%@ page import="siap.sige.avvocato.model.AvvocatoSigeModel"%>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>

<jsp:useBean id="anagraficaParteUdienza" scope="request" class="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel" />
<jsp:useBean id="notificaSoggetto" scope="request" class="siap.siep.notifica.model.NotificaModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String" />
<jsp:useBean id="sesso" scope="request" class="java.lang.String" />
<jsp:useBean id="nazioni" scope="request" class="java.lang.String" />
<jsp:useBean id="difensori" scope="request" class="java.util.Vector" />
<jsp:useBean id="nazioniResidenza" scope="request" class="java.lang.String" />
<jsp:useBean id="ragioneSociale" scope="request" class="java.lang.String" />
<jsp:useBean id="province" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />

<jsp:useBean id="codTipoParte" scope="request" class="java.lang.String" />
<jsp:useBean id="idEventoUdienza" scope="request" class="java.lang.String" />
<jsp:useBean id="idUdienzaSige" scope="request" class="java.lang.String" />
<jsp:useBean id="idUdienzaProcedimentoSige" scope="request" class="java.lang.String" />

<%
	String lAction = new String();
	String lTitolo = new String();
	String azioneChiamante = new String();

	if (modalita.equals("I")) {
		lAction = "siap.sige.udienzaparti.action.ActInserisciParteUdienza";
		lTitolo = "Inserimento Nuova Parte";
		azioneChiamante = "siap.sige.udienzaparti.action.ActLoadInserisciParteUdinza";
	} else if (modalita.equals("M")) {
		lAction = "siap.sige.udienzaparti.action.ActModificaParteUdienza";
		lTitolo = "Modifica Parte";
		azioneChiamante = "siap.sige.udienzaparti.action.ActLoadModificaParteUdinza";
	}
%>
<script language="JavaScript">
	var desktop;
	function ListaComuni(a_formname,a_fieldname)
	{
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	}

	<!-- 20210524	MEV Scheda-21 -->
	function ListaComuniNascita(a_formname,a_fieldname)
	{
		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
	}

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }

    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function cancellaCodComuneReale() 
    {
      	document.LoadInserisciPersonaGiuridica.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
    }

	function checkSNT(idEle) {
	  	var flag = document.getElementById("flagSNT_"+idEle);
	  	var select = document.getElementById("<%=ICostantiPartiUdienza.CAMPO_COD_DESTINATARIO%>_"+idEle);
	  	var sede = document.getElementById("<%=ICostantiPartiUdienza.CAMPO_SEDE%>_"+idEle);
	  	var autRow = document.getElementById("AutDestRow_"+idEle);
	  	var sedeRow = document.getElementById("SedeDestRow_"+idEle);
	  	if (flag.checked){
  			select.options[0].setAttribute("selected", "selected");
	  		sede.value = "";
	  		autRow.style.display="none";
	  		sedeRow.style.display="none";
	  	}else{
	  		// SNT off
			autRow.style.display="block";
			sedeRow.style.display="block";
	  	}
	}
	
    function VerifyG()
    {

      if (document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_COD_STATO_NASCITA%>[document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039')
      {
        if (document.LoadInserisciPersonaGiuridica.<%= ICostantiPartiUdienza.CAMPO_COD_COMUNE_NASCITA %>.value.length==0)
        {
          	alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
          	document.LoadInserisciPersonaGiuridica.<%= ICostantiPartiUdienza.CAMPO_COD_COMUNE_NASCITA %>.focus;
          	return false;
        }
      } else {
        document.LoadInserisciPersonaGiuridica.<%= ICostantiPartiUdienza.CAMPO_COD_COMUNE_NASCITA %>.value='';
        cancellaCodComuneReale();
	  }
      
      if (document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
        document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>.value;
      if (document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
        document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>.value;

      var data_to_verify = document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA%>.value;
      if (! ControllaData(data_to_verify)){
          alert('Data di nascita non valida');
          return false;
      }

      if (typeof(document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>) != "undefined"){
	      if (document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>.checked == false
	    	  && document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_CONVOCAZIONE_UDIENZA%>.value == "S" ){ 	  
	    	  // se la parte non è domiciliata presso il difensore
	    	  // bisogna compilare la sezione "Notifica al Soggetto"
	   	      if ( document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_COD_IST_DETENZIONE%>.value == "-"
	    	       || document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_COD_IST_DETENZIONE%>.value == ""
	    	       || document.LoadInserisciPersonaGiuridica.<%=ICostantiPartiUdienza.CAMPO_COD_LUOGO_DETENZIONE%>.value == "" )
	    	  {
	    	      alert('Scegliere Autorità di Destinazione e Sede per il destinario Soggetto!');
	    	      return false;
	    	  }
	      }
      }
      
      return true;
    }

</script>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>"
	name="LoadInserisciPersonaGiuridica">
	<table cellspacing=2 cellpadding=2>

		<tr>
			<td class="Titolo" colspan="4"><%=lTitolo%></td>
		</tr>

		<tr>
			<td class="l">Società <font class=ob>(*)</font></td>
			<td class="L"><input title="Denominazione"
				value="<%=anagraficaParteUdienza.getDenominazione() %>" type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_DENOMINAZIONE %>"
				maxlength="200" size="50"></td>
			<td class="l">Ragione Sociale</td>
			<td class="L"><select title="Ragione Sociale"
				name="<%=ICostantiPartiUdienza.CAMPO_RAG_SOCIALE%>">
					<%= ragioneSociale %></select></td>
		</tr>

		<tr>
			<td class="l">Provincia</td>
			<td class="L"><select title="Provincia"
				name="<%=ICostantiPartiUdienza.CAMPO_COD_PROVINCIA%>">
					<%= province %></select></td>
			<td class="l">Partita IVA/Codice Fiscale</td>
			<td class="l"><input title="Codice Fiscale"
				id=<%= ICostantiPartiUdienza.CAMPO_COD_FISCALE %>
				value="<%=StringUtils.toStringJSP(anagraficaParteUdienza.getCodFiscale()) %>"
				type="text" name="<%= ICostantiPartiUdienza.CAMPO_COD_FISCALE %>"
				maxlength="16" size="18"></td>
		</tr>

		<tr>
			<td class="l">Sede Legale</td>
			<td class="L"><input title="Sede Legale"
				value="<%=StringUtils.toStringJSP(anagraficaParteUdienza.getIndSedeLegale())%>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_IND_SEDE_LEGALE %>"
				maxlength="200" size="50"></td>
			<td class="l">Sede Operativa/Indirizzo Attività</td>
			<td class="L"><input title="Sede Operativa"
				value="<%=StringUtils.toStringJSP(anagraficaParteUdienza.getIndSedeOperativa())%>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_IND_SEDE_OPERATIVA %>"
				maxlength="200" size="50"></td>
		</tr>

		<tr>
			<td class="Titolo" colspan="4">Inserimento Legale Rappresentante</td>
		</tr>

		<tr>
			<td class="l">Cognome <font class=ob>(*)</font></td>
			<td class="L"><input title="Cognome"
				value="<%=anagraficaParteUdienza.getCognome() %>" type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_COGNOME %>" maxlength="100"
				size="35"></td>
			<td class="l">Nome <font class=ob>(*)</font></td>
			<td class="L"><input title="Nome"
				value="<%=anagraficaParteUdienza.getNome() %>" type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_NOME %>" maxlength="100"
				size="35"></td>
		</tr>

		<tr>
			<td class="l">Sesso <font class=ob>(*)</font></td>
			<td class="L"><select title="Sesso"
				name="<%=ICostantiPartiUdienza.CAMPO_SESSO%>">
					<%= sesso %></select></td>
			<td class="l" colspan="2">&nbsp;</td>
		</tr>

		<tr>
			<td class="l">Data di Nascita <font class=ob>(*)</font></td>
			<td class="l">
				<%
          if(modalita.equals("I")) {
%> <input type="text" title="Giorno Data di nascita"
				name="<%=ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>"
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)"> / <input
				type="text" title="Mese Data di nascita"
				name="<%= ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA %>"
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)"> / <input
				type="text" title="Anno Data di nascita"
				name="<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA %>"
				maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillYear(value)"> <%
          } else {
%> <input title="Giorno Data di nascita"
				value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(anagraficaParteUdienza.getDataNascita(),"dd")) %>"
				type="text"
				name="<%=ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>"
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)"> / <input
				title="Mese Data di nascita"
				value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(anagraficaParteUdienza.getDataNascita(),"MM")) %>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA %>"
				maxlength="2" size="2" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillDM(value)"> / <input
				title="Anno Data di nascita"
				value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(anagraficaParteUdienza.getDataNascita(),"yyyy")) %>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA %>"
				maxlength="4" size="4" onFocus="javascript:textboxSelect(this)"
				onkeypress="return TicTabNumField(this,event)"
				onBlur="javascript:value=FillYear(value)"> <%
          }
%> <a
				href="javascript:calendario('LoadInserisciPersonaGiuridica','<%=ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA%>','<%=ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>','<%=ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>');">
					<img src="/images/calendario.gif" border=0>
			</a>
			</td>

			<td class="l">Comune di Nascita <font class=ob>(*)</font></td>
			<td class="L"><input title="Comune di Nascita"
				value="<%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascita()) %>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_COD_COMUNE_NASCITA %>"
				maxlength="35" size="35" onChange="cancellaCodComuneReale();">
				<a href="Javascript:ListaComuniNascita('LoadInserisciPersonaGiuridica','<%= ICostantiPartiUdienza.CAMPO_COD_COMUNE_NASCITA %>');">
					<img src="/images/filefolder.gif" border=0>
				</a>
			</td>
		</tr>

		<tr>
			<td class="l">Stato di Nascita</td>
			<td class="L"><select title="Stato di Nascita"
				name="<%=ICostantiPartiUdienza.CAMPO_COD_STATO_NASCITA%>">
					<%= nazioni %>
			</select></td>
			<td class="l">Luogo di Nascita Estero</td>
			<td class="L"><input title="Luogo di Nascita Estero"
				value="<%=StringUtils.toStringJSP(anagraficaParteUdienza.getDescComuneNascitaEstero()) %>"
				type="text"
				name="<%=ICostantiPartiUdienza.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>">
			</td>
		</tr>

		<tr>
			<td class="l">Codice Fiscale</td>
			<td class="l"><input title="Codice Fiscale"
				id=<%= ICostantiPartiUdienza.CAMPO_COD_FISCALE_RAP %>
				value="<%=StringUtils.toStringJSP(anagraficaParteUdienza.getCodFiscaleRap()) %>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_COD_FISCALE_RAP %>"
				maxlength="16" size="18"></td>
			<td class="l" colspan="2">&nbsp;</td>
		</tr>

		<tr>
			<td class="Titolo" colspan="4">Residenza/Domicilio</td>
		</tr>

		<tr>
			<td class="l">Indirizzo</td>
			<td class="l"><input size=50 maxlength=200 title="Indirizzo"
				value="<%if(anagraficaParteUdienza.getResidenza()!= null)%><%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getIndirizzo())%>"
				type="text" name="<%= ICostantiPartiUdienza.CAMPO_INDIRIZZO %>">
				<input type="HIDDEN"
				name="<%=ICostantiPartiUdienza.CAMPO_ID_RESIDENZA %>"
				value="<%if(anagraficaParteUdienza.getResidenza()!= null)%><%=anagraficaParteUdienza.getResidenza().getIdResidenza()%>">
			</td>
			<td class="l">Luogo</td>
			<td class="L"><input title="Comune di Residenza"
				value="<%if(anagraficaParteUdienza.getResidenza()!= null)%><%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getDescrComune())%>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_COD_COMUNE_RESIDENZA %>"
				maxlength="35" size="35"> <a
				href="Javascript:ListaComuni('LoadInserisciPersonaGiuridica','<%= ICostantiPartiUdienza.CAMPO_COD_COMUNE_RESIDENZA %>');">
					<img src="/images/filefolder.gif" border=0>
			</a></td>
		</tr>

		<tr>
			<td class="l">CAP</td>
			<td class="l"><input size=5 maxlength=5
				value="<%if(anagraficaParteUdienza.getResidenza()!= null)%><%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getCap())%>"
				title="Cap" type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_CAP_RESIDENZA %>"></td>
			<td class="l">Luogo Estero</td>
			<td class="l"><input size=50 maxlength=200 title="Luogo Estero"
				value="<%if(anagraficaParteUdienza.getResidenza()!= null)%><%=StringUtils.toStringJSP(anagraficaParteUdienza.getResidenza().getDescComuneEstero())%>"
				type="text"
				name="<%= ICostantiPartiUdienza.CAMPO_DESC_COMUNE_ESTERO_RESIDENZA %>"></td>
		</tr>

		<tr>
			<td class="l">Stato</td>
			<td class="L"><select title="Stato di Residenza"
				name="<%=ICostantiPartiUdienza.CAMPO_COD_STATO_RESIDENZA%>">
					<%= nazioniResidenza %>
			</select></td>
			<td class="l">&nbsp;</td>
		</tr>

	</table>

	<table cellspacing=2 cellpadding=2>
		<tr>
			<td colspan=2><br> <INPUT class="bottone" type="submit"
				name="INSERISCI" value="Conferma"></td>
		</tr>
	</table>

	<input type="HIDDEN" name="Action" value="<%=lAction%>"> 
	<input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value=""> 
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_COD_TIPO_PART %>" value="<%=codTipoParte%>"> 
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA%>" value="<%=idEventoUdienza%>"> 
	<input type="HIDDEN" name="<%=ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE%>" value="<%=idUdienzaSige%>"> 
	<input type="HIDDEN" name="<%=ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE%>" value="<%=idUdienzaProcedimentoSige%>"> 
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.RADIO_COD_PARTE %>" value="G"> 
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_ID_SOGGETTO%>" value="<%=anagraficaParteUdienza.getIdSoggetto()%>"> 
	<input type="HIDDEN" name="modalita" value="<%=modalita%>"> 
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_CONVOCAZIONE_UDIENZA%>" value="<%=(anagraficaParteUdienza.getFlagConvUdienza()==null?"":anagraficaParteUdienza.getFlagConvUdienza())%>">
	<input type="HIDDEN" name="<%=ICostantiPartiUdienza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>" value="<%if(anagraficaParteUdienza.getResidenza()!= null)%><%=anagraficaParteUdienza.getResidenza().getFlgDomicilioDifensore()%>">
</form>

<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadInserisciPersonaGiuridica");

    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_DENOMINAZIONE %>","req","Il campo Società è obbligatorio");
    
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_COGNOME %>","req","Il campo Cognome è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_COGNOME %>","maxlen=100","La lunghezza massima per il cognome è di 100 caratteri");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_COGNOME %>","alpha");
    
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_NOME %>","req","Il campo Nome è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_NOME %>","maxlen=100","La lunghezza massima per il nome è di 100 caratteri");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_NOME %>","alpha");

    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA %>","req","Il campo Giorno di Nascita è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_GIORNO_DATA_NASCITA%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA %>","req","Il campo Mese di Nascita è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_MESE_DATA_NASCITA%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA %>","req","Il campo Anno di Nascita è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");
    
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_COD_FISCALE %>","alphanumeric");
<%--     frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alpha"); --%>
    
    frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_CAP_RESIDENZA%>","numeric");
<%--     frmvalidator.addValidation("<%= ICostantiPartiUdienza.CAMPO_DESC_COMUNE_ESTERO_RESIDENZA%>","alpha"); --%>
    
    frmvalidator.setAddnlValidationFunction("VerifyG");
  </script>