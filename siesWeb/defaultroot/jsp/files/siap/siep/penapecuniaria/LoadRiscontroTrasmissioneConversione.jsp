<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.jms.ICostantiJMS"%>
<%@ page import="siap.sico.jms.action.ICostantiSicoJMS"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>

<jsp:useBean id="UtenteConnesso" 		scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="richiestaconversione" 	scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>
<jsp:useBean id="autoritaConv" 			scope="request" class="java.lang.String" />
<jsp:useBean id="tipoOperazione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>

<%
//==============================================================================
// Form utilizzata per la ricerca di Riscontro Trasmissioni
// - Conversione Pene Pecuniarie 
//==============================================================================

%>


<html>
<head>
  <title>[S.I.E.S.] - Conversione Pene Pecuniarie - Ricerca (Riscontro) Atti Trasmessi per Conversione </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    function Verify()
    {
      var data_inizio=document.LoadRiscontroTrasmissioneConversione.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadRiscontroTrasmissioneConversione.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>.value+'/'+document.LoadRiscontroTrasmissioneConversione.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>.value;
      var data_fine=document.LoadRiscontroTrasmissioneConversione.<%=ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadRiscontroTrasmissioneConversione.<%=ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>.value+'/'+document.LoadRiscontroTrasmissioneConversione.<%=ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data iniziale non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data finale non valida');
        return false;
      }

      if(data_inizio.length==2 || data_fine.length==2)
       return true;

      if(!CompareDate(data_inizio,data_fine))
      {
        alert('La Data di trasmissione finale non può essere inferiore alla data iniziale');
        return false;
      }
  return true;
    }
    
	function ListaComuni(a_formname,a_fieldname)
	{
	  var desktop;
	  desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	 }    
    
    function ListaUDS(a_formname,a_fieldname)
    {
      var desktop1;
      desktop1 = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }    
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Atti Trasmessi per Conversione </font>
        </td>
      </tr>
    </table>
    <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRiscontroTrasmissioneConversione'>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.penapecuniaria.action.ActListaTrasmissioneConversione">
      <table cellspacing=2 cellpadding=2>
	
		<tr>
	  		<td class="Titolo" colspan="6"> Selezione della Data di trasmissione</td>
		</tr>
		
			 	<% RichiestaConversioneModel lRichiestaConversione = new RichiestaConversioneModel();  %>       
        
        <tr>
        	<td class="l">
            	<table cellspacing=2 cellpadding=2>
	      			<tr>
						<td class="label">Dalla data </td>
						<td class="label">
		  					<input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		   					-
		  					<input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
		   					-
		  					<input Title="Data di trasmissione inizio" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
						</td>

						<td class="label">&nbsp; &nbsp; Alla data </td>
						<td class="label">
		  					<input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		   					-
		  					<input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
		   					-
		  					<input Title="Data di trasmissione fine" type="text" name="<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
						</td>
	      			</tr>
	    		</table>
	  		</td>
        </tr>
        
      </table>

    <BR>
    <table cellspacing=2 cellpadding=2>
			<tr>
	  		<td class="Titolo" colspan="6"> Selezione dell'ufficio Destinatario</td>
			</tr>

			<tr>
	  		<td class="l">
            <table cellspacing=2 cellpadding=2>

    			<tr>
    				<td class="l" colspan="2" >Ufficio di Sorveglianza<font class="ob">(*)</font></td>
     	 			<td class="L" colspan="4">
      					<input title="Sede Ufficio Sorveglianza" value="" type="text" name="<%= ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS %>" maxlength="35" size="35">
        				<a href="Javascript:ListaUDS('LoadRiscontroTrasmissioneConversione','<%=ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS %>');">
         				<img src="/images/filefolder.gif" border=0>
         				</a>
    				</td>
    			</tr>
    			
    			<tr>
					<td class="l" colspan="2" >Ufficio Recupero Crediti</td>
					<td class="L" colspan="4"><select Title="Ufficio Recupero Crediti"
						name="<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
						<%=autoritaConv%>
					</select></td>	
				</tr>

				<tr>
					<td class="l" colspan="2" >Sede</td>
					<td class="L"colspan="4" ><input Title="Sede"
						name="<%=ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE%>"
						type="text" maxlength="35" size="35"> <a
						href="Javascript:ListaComuni('LoadRiscontroTrasmissioneConversione','<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>');">
						<img src="/images/filefolder.gif" border=0> </a></td>
				</tr>
	      		     			
	  		</table>
	  		</td>
			</tr>
   </table>

      <BR>
      
	<table cellspacing=2 cellpadding=2>
		<tr>
	  		<td class="Titolo" colspan="6"> Selezione del Fascicolo SIEP Inviato</td>
		</tr>
		
		<tr>
    		<td class="l">Numero SIEP (Anno/Progressivo)</td>
    		<td class="l" colspan="4"><input Title="ChiaveAnnoSiep"
    			name="<%= ICostantiJMS.CHIAVE_ANNO_SIEP %>" 
    			value="" >&nbsp;</td>
    			<td>/</td>
    		<td class="l" colspan="4"><input Title="ChiaveProgrSiep"
    			name="<%= ICostantiJMS.CHIAVE_PROGR_SIEP %>" 
    			value="" >&nbsp;</td>  			
    		
  		</tr>
	</table>
		      
      <BR>
      <table cellspacing=2 cellpadding=2>
	<tr>
	  <td class="Titolo">
	    Selezione del Tipo Esito
	  </td>

	</tr>
	
	<%-- ESITO TRASMISS --%>

	<tr>
	  <td class="l">
            <table cellspacing=2 cellpadding=2>
	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=0 CHECKED >
		</td>
		<td class="label" >
		  Tutti
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=1>
		</td>
		<td class="label" >
		  In attesa di risposta&nbsp;
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_ESITO%>" value=2>
		</td>
		<td class="label" >
		  Esito Positivo
		</td>
	      </tr>
	    </table>
	  </td>

		<%-- UTENTE CHE TRASM --%>

	</tr>
      </table>

      <BR>
      <table cellspacing=2 cellpadding=2>
	<tr>
	  <td class="Titolo">
	    Selezione dell'utente che ha effettuato la trasmissione
	  </td>
	</tr>
	<tr>
	  <td class="l">
            <table cellspacing=2 cellpadding=2>
	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=0 >
		</td>
		<td class="label" >
		  Tutti
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=1 CHECKED >
		</td>
		<td class="label" >
		  Utente Collegato
		</td>
	      </tr>

	      <tr>
		<td class="label" >
		  <input type=radio name="<%=ICostantiSicoJMS.CAMPO_TIPO_UTENTE%>" value=2>
		</td>
		<td class="label" >
		  Utente con codice :&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td><input Title="Codice Utente" type="text" name="<%= ICostantiSicoJMS.CAMPO_COD_UTENTE %>" maxlength="11" size="8" ></td>
	      </tr>
	    </table>
	  </td>
	</tr>
      </table>

      <BR>
      <table cellspacing=2 cellpadding=2>
        <tr>
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca">
          </td>
        </tr>
      </table>
  <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>" >
    </form>

    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("LoadRiscontroTrasmissioneConversione");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il giorno di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_INIZIO%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","minlen=2","La lunghezza minima per il mese di inizio è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_INIZIO%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_INIZIO%>","lt=3000");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il giorno di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_GIORNO_DATA_TRASMISSIONE_FINE%>","lt=31");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","minlen=2","La lunghezza minima per il mese di fine è di 2 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","gt=1");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_MESE_DATA_TRASMISSIONE_FINE%>","lt=12");

      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","numeric");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","gt=1900");
      frmvalidator.addValidation("<%= ICostantiSicoJMS.CAMPO_ANNO_DATA_TRASMISSIONE_FINE%>","lt=3000");
    </script>

  </body>
</html>