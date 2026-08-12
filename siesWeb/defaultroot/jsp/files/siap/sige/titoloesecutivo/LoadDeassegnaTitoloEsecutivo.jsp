<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.titoloesecutivo.action.ICostantiTitoloEsecutivo" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>


<jsp:useBean id="AutoritaCompetente" scope="request" class="java.lang.String"/>
<jsp:useBean id="LuogoUtenteConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni"     scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoCittadinanza" scope="request" class="java.lang.String"/>
<jsp:useBean id="sesso"       scope="request" class="java.lang.String"/>
<jsp:useBean id="dataNascitaPresunta" scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso"       scope="session" class="siap.sico.utente.model.UtenteModel"/>

<%
	SoggettoModel lSoggetto = fascicoloSigeEsteso.getSoggetto();
%>


<html>
  <head>
    <title>[S.I.E.S.] - De-assegnazione Procedimento SIEP</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

    var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      
      <%-- Ticket#20260806014 --%>
      function ListaComuniNascita(a_formname,a_fieldname) {
    		desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneNascita&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=400,height=500");
      }      
      <%-- Ticket#20260806014 - FINE --%>      
      
      function Verifica ()
      {
          return true;
      }
          
      function Verify()
      {
        var ritorno = true;
        
        if ( !document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CHECK_DEASSEGNA_TITOLO_ESECUTIVO%>.checked ){
        	alert('Selezionare la casella di controllo');
       		return false;
        } 
        else
        if(! confirm("Si conferma la De-assegnazione del Procedimento SIEP?" ) ){
        	return false;
        }else{
        	document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CHECK_DEASSEGNA_TITOLO_ESECUTIVO%>.checked = true;  
        	return true;
        }

        if (document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>[document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039')
        {
          document.LoadDeassegnaTitoloEsecutivo.<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>.value="";
          if (document.LoadDeassegnaTitoloEsecutivo.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value.length==0)
          {
            alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
            return false;
          }
        }
        else {
          document.LoadDeassegnaTitoloEsecutivo.<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>.value='';
          cancellaCodComuneReale();
		}
		
        if (document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
          document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
        if (document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
          document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
       if(document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>[document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>.selectedIndex].value == 'N')
        {
          var data_to_verify=document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value;
          if (! ControllaData(data_to_verify))
          {
            alert('Data di nascita non valida');
            return false;
          }
        }
        ritorno =  controllaEtaSoggetto();
        
        return ritorno;
      }
    </script>

    <script language="JavaScript">
      function controllaEtaSoggetto()
      {
        var tipoUff = "<%=UtenteConnesso.getUfficioUtente().getCodTipoUfficio()%>";
        var ritorno = true;
        var oggi = new Date();
        var anno = Math.abs(document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>.value);
        var mese = 1;
        var giorno = 1;
        if (document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value.length > 1 )
            mese = document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA%>.value;
        if (document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value.length > 1)
            giorno = document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>.value;
        var anno14 = anno + 14;
        var anno18 = anno + 18;
        var data_compleanno14 = new Date( anno14, mese -1, giorno);
        var data_compleanno18 = new Date( anno18, mese -1, giorno);
           //alert ("14esimo compleanno ->" + data_compleanno14.toString());
           //alert ("18esimo compleanno ->" + data_compleanno18.toString());
           //alert("Tipo Ufficio " + tipoUff);

        if (tipoUff == "PMM" || tipoUff ==  "DIBM")
        {
           // caso Tribunale dei Minori
           if (oggi < data_compleanno14 )
             ritorno = window.confirm('Il soggetto non ha compiuto i 14 anni! Confermi il suo inserimento?');
           if ( oggi > data_compleanno18)
             ritorno = window.confirm('Il soggetto ha più di 18 anni! Confermi il suo inserimento?');
        }
        else
        {
           if (oggi < data_compleanno18)
              ritorno = window.confirm('Il soggetto non ha compiuto i 18 anni. Confermi il suo inserimento?');
        }
        return ritorno;
      }
      
      function cancellaCodComuneReale() {
      
      	document.LoadDeassegnaTitoloEsecutivo.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
      }
    </script>

  </head>

  <body class="corpo">
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%
          String lAction = new String();
          lAction = "siap.sige.titoloesecutivo.action.ActDeassegnaTitoloEsecutivo";
          
%>
            <font class="campo">De-assegnazione Procedimento SIEP</font>

      </td>
    </tr>
  </table>

  <br>

  <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>    
    </tr>
  </table>
  <br>
  
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadDeassegnaTitoloEsecutivo'>
   
   <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="l" width=50%>Per confermare la De-assegnazione del Procedimento SIEP </td>
      <td class="lRosso">  
      	  <%=fascicoloSigeEsteso.getFascicoloSiep().getChiaveAnno()%>/<%=fascicoloSigeEsteso.getFascicoloSiep().getChiaveProgr()%>&nbsp;
          &nbsp;&nbsp;<%=fascicolo.getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getDescrComuneUfficio()%>&nbsp;
      </td>
    </tr>
  </table>
  <table cellspacing=0 cellpadding=0 width=95%>  
    <tr>
      <td class="l" width=50%>Selezionare la seguente casella di controllo  </td>
      <td class="l"> <input type="checkbox"  name="<%=ICostantiTitoloEsecutivo.CHECK_DEASSEGNA_TITOLO_ESECUTIVO%>" value="S" ></td>
      <!-- <td class="l"><input title="Cognome" name="<//%=ICostantiTitoloEsecutivo.CHECK_DEASSEGNA_TITOLO_ESECUTIVO%>" value="S" type="text"></td> -->	
    </tr>
  </table>
  
  <br>
  <br>
  <br>
  <table cellspacing=0 cellpadding=0 width=95%>  
    <tr>
      <td class="l" width=50%>Eventualmente procedere alla modifica degli estremi del soggetto:  </td>
      <td class="l" > </td>
    </tr>
  </table>
       <table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l">Cognome <font class=ob>(*)</font></td>
				<td class="L"><input title="Cognome" value="<%=lSoggetto.getCognome() %>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_COGNOME %>"  maxlength="35" size="35"></td>
				  <td class="l">Nome <font class=ob>(*)</font></td>
				<td class="L"><input title="Nome" value="<%=lSoggetto.getNome() %>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_NOME %>"  maxlength="35" size="35"></td>
		</tr>
		<tr>
				<td class="l">Sesso <font class=ob>(*)</font></td>
				<td class="L">
          <select title="Sesso" name="<%=ICostantiSoggetto.CAMPO_SESSO%>">
           <%= sesso %>
          </select>
        </td>
		</tr>
		<tr>
		    <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="L">
          
<%
            if(lSoggetto.getDataNascita() == null)
            {
%>
              <input type="text" title="Giorno Data di nascita" name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2">
              /
              <input title="Mese Data di nascita" value="<%=StringUtils.toStringJSP( lSoggetto.getMeseNascita() )%>" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2">
              /
              <input title="Anno Data di nascita" value="<%= StringUtils.toStringJSP( lSoggetto.getAnnoNascita() )%>" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>" maxlength="4" size="4">
<%
            }
            else
            {
%>
              <input title="Giorno Data di nascita" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"dd")) %>" type="text" name="<%=ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              /
              <input title="Mese Data di nascita" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"MM")) %>" type="text" name="<%= ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
              /
              <input title="Anno Data di nascita" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSoggetto.getDataNascita(),"yyyy")) %>" type="text" name="<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
            }
         
%>

          </td>
        <td class="l">Data Presunta</td>
        <td class="L">
          <select title="Data presunta" name="<%=ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA%>">
            <%= dataNascitaPresunta %>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Comune Nascita <font class=ob>(*)</font></td>
        <td class="L">
          <input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getDescrComuneNascita()) %>" type="text" name="<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>"  maxlength="35" size="35" onChange="cancellaCodComuneReale();">
          <%-- Ticket#20260806014 --%>
          <input type="hidden" title="Cod Comune Nascita" 
                 value="<%=StringUtils.toStringJSP(lSoggetto.getCodComuneNascita()) %>"            
                 name="<%= ICostantiComune.CAMPO_COD_COMUNE_REALE %>">
          <%--  
          <a href="Javascript:ListaComuni('LoadDeassegnaTitoloEsecutivo','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
          --%>
          <a href="Javascript: ListaComuniNascita ('LoadDeassegnaTitoloEsecutivo','<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
          <%-- Ticket#20260806014 - FINE --%> 
        </td>
      </tr>

		<tr>
				<td class="l">Stato Cittadinanza</td>
				<td class="L">
          	<select title="Stato Cittadinanza" name="<%=ICostantiSoggetto.CAMPO_NAZIONALITA%>" > 
          			<%= StatoCittadinanza %>
         		</select>
				</td>

				<td class="l">Stato di Nascita</td>
				<td class="L">

        <select  title="Stato di Nascita" name="<%=ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>">
         <%= nazioni %>

         </select>
         </td>
 		</tr>

		<tr>
				<td class="l">Comune Nascita Estero</td>
				<td class="L"><input title="Comune di Nascita Estero" value="<%=StringUtils.toStringJSP(lSoggetto.getDescComuneNascitaEstero()) %>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>"  ></td>
		</tr>

		<tr>
				<td class="l">Paternità</td>
				<td class="L"><input title="Paternità" value="<%=StringUtils.toStringJSP(lSoggetto.getPaternita() )%>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_PATERNITA %>"  maxlength="35" size="35"></td>
		</tr>
		<tr>
				<td class="l">Cognome Madre</td>
				<td class="L"><input title="Cognome della madre" value="<%=StringUtils.toStringJSP(lSoggetto.getCognomeMadre()) %>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE %>"  maxlength="35" size="35"></td>

				<td class="l">Nome Madre</td>
				<td class="L"><input title="Nome della madre" value="<%=StringUtils.toStringJSP(lSoggetto.getNomeMadre() )%>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_NOME_MADRE %>"  maxlength="35" size="35"></td>
		</tr>
		<tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
        <td class="l">Codice Fiscale</td>
        <td class="L"><input  title="Codice Fiscale" id=<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>
        value="<%=StringUtils.toStringJSP(lSoggetto.getCodFiscale()) %>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>"  maxlength="16" size="18"></td>

				<td class="l">Atto Nascita</td>
				<td class="L"><input title="Atto di nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getAttoNascita() )%>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA %>"  maxlength="10" size="10"></td>
		</tr>

    <tr>
				
       <td class="l">Codice CUI</td>
				<td class="L"><input title="Codice CUI" value="<%=StringUtils.toStringJSP(lSoggetto.getCodAfis()) %>" type="text"
        name="<%= ICostantiSoggetto.CAMPO_COD_AFIS %>"  maxlength="7" size="7"></td>
		  <td >&nbsp;</td><td >&nbsp;</td>

        </tr>

		<tr>
      <td class="l">Note</td>
      <td class="L" colspan=3>
        <TEXTAREA title="note" name="<%= ICostantiSoggetto.CAMPO_NOTE %>"  cols=80 rows=5 ><%=StringUtils.toStringJSP(lSoggetto.getNote() )%></textarea>
      </td>
		</tr>
       
  </table>
     
   <br>
    <tr>
      <td>
        <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="Ricerca" value="Conferma">
      </td>
    </tr>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=lSoggetto.getIdSoggetto()%>">
    <input type="HIDDEN" name="modalita" value="<%=modalita%>" >

  </FORM>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadDeassegnaTitoloEsecutivo");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","req","Il campo Nome Soggetto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME %>","alpha");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","req","Il campo Cognome Soggetto è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME %>","alpha");


    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA %>","req","Il campo Anno di Nascita è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_FISCALE %>","alphanumeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_AFIS %>","alphanumeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NAZIONALITA%>","alphabetic");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_STATO_NASCITA%>","alphanumeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alphanumeric");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_PATERNITA%>","alphabetic");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COGNOME_MADRE%>","alphabetic");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_NOME_MADRE%>","alphabetic");
    frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_ATTO_NASCITA%>","alphanumeric");
    <%-- Ticket#202506130166 - SIES: Anomalia inserimento provvedimento - schermata sede dell'autorità emittente--%>
    <%-- ELIMINATO CONTROLLO per consentire inserimento comuni tipo MERANO/MERAN) --%>
<%--     frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA %>","alpha"); --%>
<%--     frmvalidator.addValidation("<%= ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>","alpha"); --%>
    frmvalidator.setAddnlValidationFunction("Verifica");
    
  </script>

  </body>
</html>