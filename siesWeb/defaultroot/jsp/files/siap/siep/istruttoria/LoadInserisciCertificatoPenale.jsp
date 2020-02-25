<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.utente.model.UtenteModel"%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<jsp:useBean id="datairrevocabilita" scope="request" class="java.lang.String"/>
<jsp:useBean id="codicecomune"       scope="request" class="java.lang.String"/>
<%-- 
<jsp:useBean id="sedegiudiziaria"     scope="request" class="java.lang.String"/>
--%>
<%
	FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

// Per tutti i provvedimenti che lo gestiscono
	// aggiungere campo OBBLIGATORIO editabile CAMPO_CASELLARIO
	//       1. precaricato a '-' se lo stato nascita dell'imputato è blank
	//       2. altrimenti COD_UFFICIO dell'utente collegato
	// ad eccezione dei quattro provvedimento sotto elencati.
	// SOLO per questi 4 provvedimenti e se l'imputato è straniero (stato nascita diverso da ITALIA) --> casellario = ROMA
	//   -- Computo fungibilità
	//   -- Unificazione delle pene concorrenti
	//   -- Rideterminazione della pena
	//   -- Sospensione pena 656 
	SoggettoModel lSoggettoAssociato = lFascicoloAssociato.getSoggetto();
	
	UtenteModel lUtenteMod = new UtenteModel((UtenteModel) session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	UfficioModel lUfficioUtenteConnesso = lUtenteMod.getUfficioUtente();
	 
	String lCasellario = lUfficioUtenteConnesso.getDescrComune();
	
	if( lSoggettoAssociato != null 
	    && 
	    	(    lSoggettoAssociato.getCodStatoNascita() == null
	    	  ||  "".equals(lSoggettoAssociato.getCodStatoNascita()) 
	    	  || "-".equals(lSoggettoAssociato.getCodStatoNascita())
	    	 )
	   )
	{
	  lCasellario = "-";
	}
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Certificato Penale</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
	    var desktop;
	    function ListaComuni(a_formname,a_fieldname)
	    {
	     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	    }

	    function Verify()
		  {
		  	//Il "Casellario Giudiziale" è obbligatorio
     		if (   document.LoadInserisciCertificatoPenale.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '-' 
     		    || document.LoadInserisciCertificatoPenale.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>.value == '' 
     		   )
        {
           alert("Il campo Casellario Giudiziale è obbligatorio!");

           return false;
        }		  	
		  	
			  if (document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
				  document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
			  if (document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
				  document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;
	
			  var data_to_verify = document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
	
	      if (!ControllaData(data_to_verify) )
			  {
	        alert('Data di emissione non valida');
				  
				  return false;
			  }
			  
    if(!CompareDate("<%=datairrevocabilita%>",data_to_verify))
	    {
	        alert("La Data di Emissione del Documento non può essere Inferiore alla Data di Irrevocabilità della Sentenza");
	        document.LoadInserisciCertificatoPenale.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
	        return false;
	    }
	  }
  </script>

 </head>
 <body class="corpo">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      	<font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta Certificato Penale</font>
      </td>
    </tr>
  </table>
	<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciCertificatoPenale" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.istruttoria.action.ActInserisciCertificatoPenale">
  <input type="hidden" name="codicecomcas" value="<%=codicecomune%>">
<%-- 
  <input type="hidden" name="sedegiudiziaria" value="<%=sedegiudiziaria%>">
--%>
  <table>
   <tr>
     <td class="Titolo" colspan=2>Destinatario  </td>
   </tr>

   <tr>
     <td class="l">
     	CASELLARIO di 
     </td>
		 <td class="l" id="inputCasellario">
     	 <input title="Sede Casellario Giudiziale" value="<%= StringUtils.toStringJSP( lCasellario ) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('LoadInserisciCertificatoPenale','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS %>');">
         <img src="/images/filefolder.gif" border=0>
       </a>
     </td>
   </tr>
   
   <tr>
     <td class="Titolo" colspan=2>Oggetto  </td>
   </tr>

   <tr>
        <td class="l">Data richiesta : </td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>


</table>
	</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciCertificatoPenale");


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


 </script>

	</body>
</html>