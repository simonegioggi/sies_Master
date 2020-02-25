<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.penapecuniaria.action.ICostantiPenaPecuniaria"%>
<%@ page import="siap.siep.penapecuniaria.model.RichiestaConversioneModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>

<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="residenzaassociata"  scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>
<jsp:useBean id="domicilioassociato"  scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>
<jsp:useBean id="autoritaConv" scope="request" class="java.lang.String" />
<jsp:useBean id="richiestaconversione" scope="request" class="siap.siep.penapecuniaria.model.RichiestaConversioneModel"/>
<jsp:useBean id="lAnnotazione"        scope="request" class="java.lang.String"/>
<jsp:useBean id="comuneUDS"           scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUDS"             scope="request" class="java.lang.String"/>

<% 

  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
     lPosizione = new PosizioneGiuridicaModel();
  
  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel(); 
 
  
%>

<html>
<head>
  <title> Trasmissione Atti per Conversione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() 
    { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      /* Esempio:
      if (document.LoadInserisciRichiestaConversione.<%="ICostantiPenaPecuniaria.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciRichiestaConversione.<%="ICostantiPenaPecuniaria.CAMPO_"%>.focus(); 
        return false; 
      } 
      */
   	  //=============================================================
      // controllo obbligatorietà magistrato
      //=============================================================
      if(document.LoadInserisciRichiestaConversione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
         alert("Il Cognome del Magistrato è obbligatorio");
         return false;
      }
      if(document.LoadInserisciRichiestaConversione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
         alert("Il Nome del Magistrato è obbligatorio");
         return false;
      }
       if(document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS%>.value=="")
      {
         alert("La sede dell'ufficio di sorveglianza è obbligatoria");
         document.LoadInserisciRichiestaConversione.<%=ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS%>.focus();
         return false;
      }
    	
    	//=============================================================
      // controllo correttezza campo 'Data Emissione Provvedimento' 
      //=============================================================
      var data_to_verify = document.LoadInserisciRichiestaConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+ 
                           document.LoadInserisciRichiestaConversione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+ 
                           document.LoadInserisciRichiestaConversione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Emissione non corretta'); 
        document.LoadInserisciRichiestaConversione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus(); 
        return false; 
      } 

      //=============================================================
      // controllo correttezza campo 'Data Trasmissione Provvedimento' 
      //=============================================================
      var data_to_verify = document.LoadInserisciRichiestaConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+ 
                           document.LoadInserisciRichiestaConversione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+ 
                           document.LoadInserisciRichiestaConversione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value; 
      if (!ControllaData(data_to_verify) && data_to_verify.length>2){ 
        alert('Data Trasmissione non corretta'); 
        document.LoadInserisciRichiestaConversione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.focus(); 
        return false; 
      } 

      return true; 
    } 

    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
    }
    
    function ListaUDS(a_formname,a_fieldname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    
    function ListaComuniUfficio(a_formname,a_fieldname)
    {
      codTipoUfficio = document.LoadInserisciRichiestaConversione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.value;

      if (codTipoUfficio=='-'){
        alert("Selezionare il tipo di ufficio emittente");
        document.LoadInserisciRichiestaConversione.<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>.focus();
      }
      else {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&TipoUfficio="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
    }
    
	
  </script>
</head>

<body class="corpo">
    <table >
     <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        RichiestaConversioneModel lRichiestaConversione = new RichiestaConversioneModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.siep.penapecuniaria.action.ActInserisciTrasmissioneConversione"; 
        }
        %>
        <font class="campo">Trasmissione Atti per Conversione</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="<%= IWebConstants.PG_MAIN %>" name="LoadInserisciRichiestaConversione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">

  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
     
	 <table width="100%" >
		<tr>
			<td class="l">Posizione Giuridica </td>
			<td class="L" colspan=5><font class="campo">
			<%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){%>
				DETENUTO PER ALTRA CAUSA
			<%}
			else{%>
				<%=lPosizione.getDescrPosizioneGiuridica()%>
			<%}%></font>
			</td>
		</tr>
	</table>
	<table width="100%" >
	
			<%if((residenzaassociata.getResidenza()!= null) ||
				 (domicilioassociato.getResidenza()!= null) )
				 { %>   
				 			<tr><td>&nbsp;</td></tr>
							<tr><td class="Titolo" colspan=6>Dati Relativi Alla Residenza Del Soggetto</td></tr>
			 <%  } %>
		<tr>
			<%if(lPosizione.isLibero() && residenzaassociata != null && residenzaassociata.getResidenza()!= null){%>   
				<td class="l">Residenza</td> 
				<td class="L" colspan=5><font class="campo">
				<%=residenzaassociata.getResidenza().getIndirizzo()%>&nbsp;<%=residenzaassociata.getResidenza().getDescrComune()%>
			<%}%></font>         
			</td>
		</tr>
		<tr>
			<%if(lPosizione.isLibero() && domicilioassociato != null && domicilioassociato.getResidenza() != null){%>   
				<td class="l">Domicilio</td> 
				<td class="L" colspan=5><font class="campo">
				<%=domicilioassociato.getResidenza().getIndirizzo()%>&nbsp;<%=domicilioassociato.getResidenza().getDescrComune()%>
			<%}%></font>         
			</td>
		</tr>
<%      if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")){
           if( lAltraCausa.getIstitutoDetenzione()!= null){
%>
           <tr>
           		<td class="l">Detenuto presso </td>
           		<td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
           		</td>
           </tr>
<%         if (lAltraCausa.getAltroLuogo()!=null){
%>         <tr>
                 <td class="l">Altro Luogo </td >
                 <td class="L" colspan=5><font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                 </td>
           </tr>
<%
               }
            }
        }
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
          <td class="l">Detenuto presso </td>
          <td class="L" colspan=5>
          <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
          di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
          </td>
          </tr>
<%
        }%>
 
<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
             </tr>
<%
          }
        }

 if(lFascicoloAssociato.getCodTipoPosLibero().equals("I"))
  {
%>
    <tr>
      <td class="L">
        <font class="campo">Irreperibile</font>
      </td>
    </tr>
<%}%>
	</table>
	<table width="90%" >
	<tr><td>&nbsp;</td></tr>
    <tr><td class="Titolo" colspan=6>Dati Richiesta Conversione</td></tr>

		<tr><td>&nbsp;</td></tr>
  		<tr>
    		<td class="l">Anno/Numero Partita</td>
    		<td class="l"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getAnnoPartita()) %>
							/<%=StringUtils.toStringJSP(richiestaconversione.getNumPartita()) %></font>&nbsp;</td>
 	    	<td class="l" colspan="4"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getNumExCampione()) %></font>&nbsp;</td>
		</tr>
		<tr>
    		<td class="l">Autorità</td>
    		<td class="l" colspan="5"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDescrTipoAutoritaEmittente()) %>
		     di <%=StringUtils.toStringJSP(richiestaconversione.getDescrLuogoEmittente()) %></font>&nbsp;</td>
		</tr>
		<tr>
		    <td class="l">Data Ricezione Atto</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataRicezioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
		    <td class="l">Data Iscrizione Atto</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataIscrizioneAtto(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
		    <td class="l">Data Esazione</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataEsazione(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
		</tr>
		<tr>
		    <td class="l">Multa: Importo</td>
		    <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoMulta()) %></font>&nbsp;</td>
		    <td class="l">Data Prescrizione </td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneMulta(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			<% if (richiestaconversione.getFlagImprescrittibileMulta().equals("S")) {%>
				<td class="l" colspan="2">Imprescrittibile</td>
			<%}%>
		</tr>
		<tr>
		    <td class="l">Ammenda: Importo</td>
		    <td class="l"><font class="campo"><%=StringUtils.toEuroFormat(richiestaconversione.getImportoAmmenda()) %></font>&nbsp;</td>
		    <td class="l">Data Prescrizione</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(richiestaconversione.getDataPrescrizioneAmmenda(),"dd-MM-yyyy"))%> </font>&nbsp;</td>
			<% if (richiestaconversione.getFlagImprescrittibileAmmenda().equals("S")){%>
    			<td class="l" colspan="2">Imprescrittibile</td>
    		<%}%>
		</tr>
		</table>
		<br>
		<table width="90%" >
   	 	<tr>
   	 		<td class="Titolo" colspan="6"> Magistrato Firmatario </td>
   	 	</tr>
    	<tr>
	      	<td class="l">Magistrato<font class="ob">(*)</font></td>
	      	<td class="L" colspan="4">
	        	<input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
	        	<input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
	          		<a href="Javascript:ListaMagistrati('LoadInserisciRichiestaConversione');">
	            		<img src="/images/filefolder.gif" border=0>
	          		</a>
	       	</td>
       		<td>
        		<input  type="hidden"  title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>"  name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  >
       		</td>
    	</tr>
    	</table>
    	<br>
    	<table width="90%" >
    	<tr>
    		<td class="Titolo" colspan="6">Destinatari</td>
    	</tr>
    	</table>
    	<table width="90%" >
		  <!-- 24/07/2015  Notifica per l'Ente di Sorveglianza -->
			  <tr>
				<td class="L">Magistrato di Sorveglianza <font class=ob>(*)</font></td>
			    <td class="L">
		    	  <select Title="Magistrato di Sorveglianza" class="small" name="<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>" >
			      <%=tipoUDS%>
			      </select>
			   	</td>
		   		<td class="L" COLSPAN=2>
			      <input type="text" title="ufficio" value="<%=StringUtils.toStringJSP(comuneUDS,"")%>"  name="<%=ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS%>" maxlength="35" size="25">
      			  <a href="Javascript:ListaComuniUfficio('LoadInserisciRichiestaConversione','<%=ICostantiPenaPecuniaria.CAMPO_COD_SEDE_UDS%>');">
		    	     <img src="/images/filefolder.gif" border=0>
			      </a> 
			    </td>
	    	</tr>
		   	<tr>
	    		<td class="l">Autorità</td>
	    		<td class="l" colspan="6"><font class="campo"><%=StringUtils.toStringJSP(richiestaconversione.getDescrTipoAutoritaEmittente()) %>
			     di <%=StringUtils.toStringJSP(richiestaconversione.getDescrLuogoEmittente()) %></font>&nbsp;</td>
				 <td class="l"> <input type="checkbox" name="notificaufficio" value="01"></td>
				 
			</tr>
    	</table>
    	<input  type="hidden" value="<%=StringUtils.toStringJSP(richiestaconversione.getCodTipoAutoritaEmittente())%>"  name="<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>"  >
		<input  type="hidden" value="<%=StringUtils.toStringJSP(richiestaconversione.getCodLuogoEmittente())%>"  name="<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>"  >
				 	
	<table width="90%" >
	<tr>

        <td class="l">Data Emissione</td>
        <td class="L">
          <input title = "Giorno Data Emissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Emissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Emissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"  <%=IWebConstants.UTIL_DATA_ANNO%>>
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input title = "Giorno Data Trasmissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Trasmissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Trasmissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>

      </tr>
    </table>
	<br>
	<table width="90%" >
    <tr>
    	<td class="lNoBord" colspan="2">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</form>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaConversione");
<%--
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_GIORNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_MESE_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaPecuniaria.CAMPO_ANNO_DATA_RICEZIONE_ATTO %>","req","Il campo Data Ricezione Atto è obbligatorio");
 --%>
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Data Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>","req","Il campo Data Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>","req","Il campo Data Emissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>","req","Il campo Data Trasmissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>","req","Il campo Data Trasmissione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>","req","Il campo Data Trasmissione è obbligatorio");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");


  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>