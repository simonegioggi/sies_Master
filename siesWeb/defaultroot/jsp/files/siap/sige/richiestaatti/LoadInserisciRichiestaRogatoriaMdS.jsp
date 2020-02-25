<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.RedirectTo"%>

<%@ page import="siap.sige.avvocato.action.ICostantiAvvocatoFascicoloSige"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<%@ page import="siap.sige.collegio.action.ICostantiCollegio"%>
<%@ page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel"%>

<jsp:useBean id="modalita"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"   			scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"     			scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="tipoUfficioCompetente" scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="tipoUfficioUtente"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoGiudizio"  		scope="request" class="java.lang.String"/>
<jsp:useBean id="TenoriSige" 			scope="session" class="java.util.Vector"/>
<jsp:useBean id="avvocato"				scope="request" class="java.util.Vector" />
<jsp:useBean id="TornaQui" 				scope="request" class="java.lang.String" />

<%
	String isVALIGN = "top";
	String isBorder = "0";
	String lWidth = "96%";

	// Se viene passato nella request la lista con le opzioni Tipo Giudizio 
	// occorre visualizzare la combo per la scelta del Tipo Giudizio per il Fascicolo.
	boolean defTipoGiudizio = false;
	if (tipoGiudizio != null && tipoGiudizio.trim().length() > 0)
		defTipoGiudizio = true;

	//Magistrato Assegnatario
	MagistratoAssegnatarioModel magistratoassegnatario = FascicoloSigeEsteso.getMagAssegnatario();

	/* Estrazione della data udienza */
	String lDataUdienza = "";
	if ((FascicoloSigeEsteso.getUdienzaProcedimento() != null) &&
			(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige()!=null) )
		lDataUdienza = (DateUtils.getDateToString (FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(), "dd/MM/yyyy"));

	// Link alla Gestione Oggetti 
	RedirectTo lRedir = new RedirectTo();
	lRedir.setPage(IWebConstants.PG_MAIN);
	lRedir.setAction("siap.sige.tenore.action.ActLoadDettaglioOggetti");
	lRedir.setParameter("TornaQui", TornaQui );

	String lLinkOggettiSessione = lRedir.toString();
%>


<html>
  <head>
    <title>[S.I.E.S.] - Richiesta Rogatoria Magistrato di Sorveglianza </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

		var desktop;

        // Chiamata funzione lista Oggetti
        function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
        {
          // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
          var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
              aLink += "&formname="+a_formname;
              aLink += "&field_contenuto="+a_field_contenuto;
              aLink += "&fieldname="+a_fieldname;
              aLink += "&fieldcodes="+a_fieldcodes;
              aLink += "&fieldcodesdet="+a_fieldcodesdet;
              aLink += "&ifieldcodes="+i_fieldcodes;
              aLink += "&ifieldcodesdet="+i_fieldcodesdet;
          desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
        }
        
        function Init()
        {
        	<% if (defTipoGiudizio) { %>
        		Visualizza(document.LoadEmissioneOrdinanzaIncompetenza.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value);
        	 <%} %>
        	Verifica();
        }
				// Verifica del Magistrato Assegnatario
        function  Verifica()
        {
          var ritorno = true;

          <% if (magistratoassegnatario == null )	{ %>
              ritorno = false;
          <% } %>

          if (! ritorno)
          alert (" Magistrato non assegnato!");

          return ritorno;
        }
     </script>

    <script language="JavaScript">   
        function Verify()
    {
        var ritorno = true;
        //alert("Verify");
        var data_udienza = '<%=lDataUdienza%>';
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

      // Controllo della data emissione.
      var data_emissione = document.LoadRichiestaRogatoria.<%=ICostantiProvvedimentoSige.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaRogatoria.<%=ICostantiProvvedimentoSige.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaRogatoria.<%=ICostantiProvvedimentoSige.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      //alert("data emissione ->" + data_emissione);
	 
	 		// controllo Magistrato
	 		ritorno = Verifica();
	 
     if (ritorno && ! ControllaData(data_emissione))
      {
        alert('Data emissione non valida!');
        ritorno =  false;
      }
      // Controllo data di sistema >= Data Emissione .
      else if( !CompareDate( data_emissione, data_sistema) )
      {
        alert('Data Emissione maggiore della Data di sistema!');
        ritorno =  false;
      }
      // Controllo della data deposito <= data  di udienza
     //alert("data_udienza ->" + data_udienza);
      else if ( ( ControllaData(data_emissione)) && ( !CompareDate( data_udienza, data_emissione) ) )
      {
        alert('Data Emissione minore della Data di Udienza!');
        ritorno =  false;
      }
<%	if (defTipoGiudizio) { %>
     //Controllo obbligatorietà Collegio in caso di Tipo Giudizio impostato.
    	if ( document.LoadRichiestaRogatoria.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value != "-" )
    	{
    	if (document.LoadRichiestaRogatoria.<%=ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO%>.value == "C")
    		ritorno = checkObblCollegio();
  	<%}%>
     	return ritorno;
    }
     
    	// Check Obbl. Collegio.
		function checkObblCollegio()
		{
			var ritorno = true;
			var collegio = document.LoadRichiestaRogatoria.<%=ICostantiCollegio.CAMPO_ID_COLLEGIO%>.value;
			if ( collegio == '' ) 
	    {
	    	alert("Collegio obbligatorio.");
	      ritorno = false;
	    }
				return ritorno;  
		}
  }

  function ListaUDS(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }
        
        
    
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function Verify()
	  {
		  if (document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaRogatoria.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione non valida');
			 return false;
		  }
		  
		  // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      if (document.LoadRichiestaRogatoria.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>.value == '')
          {
              alert('La Sede del destinatario è obbligatoria');
              return false;
          }
		  
	  }
	  function ListaMagistrati(a_formname)
      {
        var a_codnum = document.LoadRichiestaRogatoria.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO %>.value;
        
        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
  </script>

  </head>

 <body class="corpo" >
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta Rogatoria MdS</font>
<%
        String lAction = new String();
        lAction = "siap.sige.richiestaatti.action.ActInserisciRichiestaRogatoriaMdS";
%>
      <%
			  EventoModel lProvvedimento = new EventoModel();

        String lAzione = new String();

        if( modalita.equals("I") )
         {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.sige.richiestaatti.action.ActInserisciRichiestaRogatoriaMdS";
      %>
          <font class="campo">Rogatoria MdS</font>
      <%}
        else if( modalita.equals("M") )
        {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.sige.richiestaatti.action.ActInserisciRichiestaRogatoriaMdS";
      %>  <font class="campo">Modifica Richiesta Rogatoria</font>
      <%}
      %>
      </td>
    </tr>
    </table>
    
 <br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  <br>
    
   
   <table  width="95%" ><tr>
	    <td class="Titolo">Magistrato</td>
	  </tr></table>
   <table><tr>
    <jsp:include page="<%=ICostantiMagistratoAssegnatario.PG_SINTESIMAGISTRATOASSEGNATARIO%>">
          <jsp:param name="MagAssRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaIncompetenza"/>
    </jsp:include>

    </tr>

      <jsp:include page="<%=ICostantiAvvocatoFascicoloSige.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="siap.sige.provvedimento.action.ActLoadEmissioneOrdinanzaIncompetenza"/>
      </jsp:include>

   </table>
  <FORM method="POST" name="LoadRichiestaRogatoria" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.richiestaatti.action.ActInserisciRichiestaRogatoriaMdS">
    
   
   <table  width="95%" ><tr>
	    <td class="Titolo">Dati Ordine Traduzione</td>
	  </tr></table>    
   <table>
	<tr>
        <td class="l">Data Emissione <font class="ob">(*)</font></td >
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadRichiestaRogatoria','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
   </tr>
   <tr>
        <td class="l">Data Richiesta del Condannato <font class="ob"></font></td >
        <td class="L">
          <input type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_INSERIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_INSERIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_INSERIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadRichiestaRogatoria','<%=ICostantiEvento.CAMPO_ANNO_DATA_INSERIMENTO%>','<%=ICostantiEvento.CAMPO_MESE_DATA_INSERIMENTO%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_INSERIMENTO%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
   </tr>
   
   <tr>
  		<td class="L">
  		  <table cellspacing=1 cellpadding=1  width="100%" border=<%=isBorder%>>
  			<tr>
    		 <td class="label" width=15% colspan=2>
             	<a class="cliccabile" href="<%=lLinkOggettiSessione%>">
       	 			Oggetti
      			</a>
      		 </td>
  			</tr>
  			</table>
  		</td>
  		<td  class="L">
  			<div id="elenco1" style="width: 100%; display:block">
  				<jsp:include page="/jsp/files/siap/sige/tenore/ElencoTenoriSige.jsp"/>
  			</div>
	 	</td>	
		</tr>
   <!-- Campo Note  -->
     <tr>
         <td class="l">Note </td>
         <td class="L" colspan=3>
          	<TEXTAREA title="Note" name="<%= ICostantiIstruttoria.CAMPO_NOTE %>"  cols=80 rows=2 ></textarea>
         </td>
      		<td>
   			</td>   
     </tr>
	</table>

	<br>	

	<table  width="95%" >
		<tr>
	    <td class="Titolo">Destinatari</td>
	  </tr>
	</table>
	
  <table>
   
     <tr>
        <td class="L">Ufficio di Sorveglianza<font class=ob>(*)</font></td>

        <td class="l">
           <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" value="" type="text" maxlength="35" size="35">
           <a href="Javascript:ListaUDS('LoadRichiestaRogatoria','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>');">
							<img src="/images/filefolder.gif" border=0>
           </a>
        </td>
     </tr>
    
    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>

	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
    
    
  
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRichiestaRogatoria");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2999");
  
  frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","maxlen=35","La lunghezza massima per la Sede è di 35 caratteri");
  frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_SEDE%>","alpha");
  
 </script>

</table>
	</form>

	</body>
</html>