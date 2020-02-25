<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="prescrizione" scope="request" class="siap.sius.prescrizione.model.PrescrizioneModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nextaction" scope="request" class="java.lang.String"/>

<jsp:useBean id="UffMagComp" scope="request" class="java.lang.String"/>
<jsp:useBean id="LuogoProva" scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneCSSA" scope="request" class="java.lang.String"/>
<jsp:useBean id="IdEvento" scope="request" class="java.lang.String"/>

<jsp:useBean id="PrescrizioniSanzioniSostitutive" scope="request" class="java.util.ArrayList"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Prescrizione </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco dei Comuni.
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Magistrati presso Uffici di Sorveglianza di:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      
            
    </script>
  <script language="JavaScript">
  
  	// Funzione per TRIM della stringa a destra e sinistra
  	function trim(stringa)
  	{    
  		while (stringa.substring(0,1) == ' ')
  		{        
  			stringa = stringa.substring(1, stringa.length);    
  		}    
  		while (stringa.substring(stringa.length-1, stringa.length) == ' ')
  		{        
  			stringa = stringa.substring(0,stringa.length-1);    
  		}    
  		return stringa;
  	}
  	
    function Verify()
    {
		// crea array di controllo per le prescrizioni
		// l'array contiene il codice della prescrizione    
    	codiceElementi = new Array();
<%
    	Iterator itxx = PrescrizioniSanzioniSostitutive.iterator();
    	while ( itxx.hasNext())
    	{
    		DecodificheModel lPrescrizioneMod = (DecodificheModel)itxx.next();
 %>
 			codiceElementi.push(<%=lPrescrizioneMod.getCode()%>); 	
 <% 	
    	}
 %>  	
 		// esegue il ciclo degli elementi 
 		for(x=0; x<codiceElementi.length; x++)
 		{
 			var campoCheck = eval('document.LoadInserisciPrescrizione.CAMPO_CK_'+codiceElementi[x]+';'); 
			// controlla che il campo check sia avvalorato	
 			if(campoCheck.checked)
 			{
 				var campo = eval('document.LoadInserisciPrescrizione.CAMPO_TESTO_'+codiceElementi[x]+';');
 				
 				// Controlla esistenza dei campi testo
 				if( campo!= null)
    			{
    				// controlla per più di un campo
    				if(campo.length > 0)
    				{
    					for(i=0; i<campo.length; i++)
    					{
  							if(trim(campo[i].value)=="")
  							{	
  								alert("Prescrizioni\n\n Il campo: "+(i+1)+" alla riga: "+(x+1)+" è obbligatorio");
  								return false;
  							}
    					}
    			
    				}
    				else{
    					if(trim(campo.value)=="")
  							{	
  								alert("Prescrizioni\n\n Il campo alla riga: "+(x+1)+" è obbligatorio");
  								return false;
  							}
    				}
    			}
 			}
 		}
    	
    	return true;
    }
  </script>


    
  </head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <%
        PrescrizioneModel lPrescrizione = new PrescrizioneModel();
        String lAzione = new String();
        if( modalita.equals("I") )
        {
          lPrescrizione.setDescrLuogoAffidamento( LuogoProva );
          lPrescrizione.setDescrUffMagistratoCompetente(UffMagComp);
          lPrescrizione.setDescrComuneCssaCompetente(ComuneCSSA);
          lAzione = "siap.sius.prescrizione.action.ActInserisciPrescrizioneNew";
      %>
        <font class="campo">Inserimento Prescrizioni</font>
      <%
        }
        else if( modalita.equals("M") )
        {
          lAzione = "siap.sius.prescrizione.action.ActModificaPrescrizione";
          lPrescrizione = prescrizione;
      %>
        <font class="campo">Modifica delle Prescrizioni</font>
        
      <%
        }
      %>
      </td>
    </tr>
  </table>

  <tr>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  </tr>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPrescrizione">
    <table cellspacing=4 cellpadding=4>
      <tr>
				<td class="l" colspan=2>Specificare Prescrizioni:</td>
      </tr>
<% 
		//Scrive le Prescrizioni prese dalla cg_ref_codes decodificate
 		Iterator itx = PrescrizioniSanzioniSostitutive.iterator();
    	while ( itx.hasNext())
    	{
    		DecodificheModel lPrescrizioneMod = (DecodificheModel)itx.next();
 %>
 			<%=lPrescrizioneMod.getDescription() %> 
 			 	
 <% 	
    	}
%>
      <tr>
        <td colspan=2>
          <input class="bottone" type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IdEvento%>" >
          <input type="HIDDEN" name="<%=ICostantiPrescrizione.CAMPO_COMUNE_CSSA_COMP%>" >
          <input type="HIDDEN" name="nextaction" value="<%=nextaction%>" >
          <input type="HIDDEN" name="<%=ICostantiPrescrizione.CAMPO_ID_CSSA_COMPETENTE%>" value="<%=prescrizione.getIdCssaCompetente()%>">
        </td>
      </tr>

  </table>
  </form>
    <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("LoadInserisciPrescrizione");
      frmvalidator.setAddnlValidationFunction("Verify");
    </script>

  </body>
</html>