<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.scambiosanzione.action.ICostantiScambioSanzione"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="posizioneGiuridica"  scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"     scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="posizioneAltraCausa" scope="request" class="java.lang.String"/>

<jsp:useBean id="provvedimento"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="tipoprovvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita" scope="request" class="java.util.ArrayList"/>

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
<title>[S.I.E.S.]</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    
     var desktop; 
  	 function ListaDocumentiSius(a_formname)
     {
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.scambiosanzione.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiScambioSanzione.CAMPO_NATURA_SS%>=<%=ICostantiScambioSanzione.REVOCA_CONVERSIONE%>", "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
     }

    function Verify()
    {
        if (document.LoadInserisciRevocaConversione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>[document.LoadInserisciRevocaConversione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '-')
    	{
			alert('Selezionare una Posizione Giuridica!');			 
			return false;
		}	
 
   	
         if (document.LoadInserisciRevocaConversione.<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO %>[document.LoadInserisciRevocaConversione.<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO%>.selectedIndex].value == '-')
    	{
			alert('Selezionare Tipo Provvedimento!');			 
			return false;
		}
			
        if(document.LoadInserisciRevocaConversione.<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT%>.value=="")
        {
          alert("La Sede dell'Autorità Emittente è obbligatoria");
          return false;
        }						
    
     }
     
    function AbilitaAC()
    {
		document.LoadInserisciRevocaConversione.<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>.disabled =true;
		document.LoadInserisciRevocaConversione.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.disabled =true;
			   
        if (document.LoadInserisciRevocaConversione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>[document.LoadInserisciRevocaConversione.<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>.selectedIndex].value == '10')
    	{
			document.LoadInserisciRevocaConversione.<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>.disabled =false;
			document.LoadInserisciRevocaConversione.<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>.disabled =false;
		}	
					
     }     
   
       function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
      {
         var desktop;
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      } 
 
      // ** GESTIONE COMBO BOX **
      // Matrice di tante righe quanti sono i tipi provvedimento
      // e tante colonne quante sono le combo da gestire
      var lNumProvvedimento=<%=provvedimento.size()%>;
      var lNumCombo = 1;

      var lMatrice=new Array(lNumProvvedimento)
      for (i=0; i<lNumProvvedimento; i++)
        lMatrice[i]=new Array(lNumCombo);

<%
      //Autorita
      for(int i=0; i<provvedimento.size(); i++)
      {
        List lAut = (List)autorita.get(i);

        out.println("\n\n\tvar lAutorita"+i+"=new Array()\n");

        Iterator lIterAutorita = lAut.iterator();
        DecodificheModel lDec = null;
        int idx = 0;
        while(lIterAutorita.hasNext())
        {
          lDec = (DecodificheModel) lIterAutorita.next();
          out.println("\tlAutorita"+i+"["+idx+"]=new Option(\""+lDec.getDescription()+"\",\""+lDec.getCodiceAlternativo()+"\");");
          idx++;
    	  }
      }

%>

      // La matrice contiene l'insieme delle opzioni selezionabili
      // strutturare in questo modo:
      // ci sono tante righe quante sono le opzioni della combo 'Provvedimento'
      // e tante colonne quante sono le combo da relazionare
      lMatrice[0][0] = lAutorita1;  // Contiene un array di oggetti Option
      
      lMatrice[1][0] = lAutorita0;  // Contiene un array di oggetti Option

      lMatrice[2][0] = lAutorita2;  // Contiene un array di oggetti Option


      function initCombo()
      {
<%
        int lIndex = 2;
%>
        document.LoadInserisciRevocaConversione.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.options[<%=lIndex%>].selected=true;

        caricamento();
      }

      function caricamento()
      {
        var idxSel = document.LoadInserisciRevocaConversione.<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>.options.selectedIndex;

        loadCombo(document.LoadInserisciRevocaConversione.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>, idxSel, 0);
       }

      function loadCombo(campo, riga, colonna)
      {
        for (m=campo.options.length-1;m>=0;m--)
          campo.options[m]=null;

        for (i=0;i<lMatrice[riga][colonna].length;i++)
        {
          campo.options[i]=new Option(lMatrice[riga][colonna][i].text,lMatrice[riga][colonna][i].value)
        }

        campo.options[0].selected=true;

      }

      function controlli()
      {
          AbilitaAC();
          initCombo();
      }

     function pulisciId()
    {
      document.LoadInserisciRevocaConversione.<%=ICostantiSanzioneSostitutiva.CAMPO_ID_SANZIONE_SOSTITUTIVA%>.value="";
    }  
  </script>
</head>
<body class="corpo" onload="controlli();">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Annotazione Revoca\Conversione Sanzione Sostitutiva</font>
      </td>
    </tr>
</table>


  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRevocaConversione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sanzionesostitutiva.action.ActInserisciAnnotazioneRevocaConversione">
  <INPUT type="HIDDEN" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ID_SANZIONE_SOSTITUTIVA%>">
  <INPUT type="HIDDEN" name="<%=ICostantiScambioSanzione.CAMPO_COD_TIPO_SANZIONE%>">


  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan="4">
           <select title="Posizione Giuridica" name="<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>" onchange="AbilitaAC();">
             <%=posizioneGiuridica%>
          </select>      
      </td>
     </tr>
      <tr>
        <td class="l">Detenuto per altra causa</td>
        <td class="l" colspan="4">
          <input type='checkbox' name='<%=ICostantiFascicoloSiep.CAMPO_FLAG_ALTRA_CAUSA%>' value = 'S' <%=(lFascicoloAssociato.getFlagAltraCausa() != null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) ? "checked" : ""%>>
        </td>
      </tr>
      <tr>
        <td class="l">Tipo Misura</td>
        <td class="l" colspan="4">
          <select title="Tipo Misura" name="<%=ICostantiAltraCausa.CAMPO_COD_TIPO_POS_GIURIDICA%>">
            <%=posizioneAltraCausa%>
          </select>
        </td>
      </tr>
<%
  if(penaresidua != null && penaresidua.getFlagSanzioneSostitutiva()!=null)
  {
%>
     <tr>
      <td class="l">Sanzione sostitutiva da espiare</td>  
      <td class="L">
           <font class="campo"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%>&nbsp;</font>
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>
<% 
				 if(   (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0)
            || (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0)
           )
         {
%>
          <font class="label"> Sanz.Pec.&nbsp;</font>
          <% if (penaresidua.getImportoMultaSS() != null && penaresidua.getImportoMultaSS().intValue() != 0) { %>
          <font class="campo">Multa&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoMultaSS())%>&nbsp;</font>&euro;
          <% } %>
          <% if (penaresidua.getImportoAmmendaSS() != null && penaresidua.getImportoAmmendaSS().intValue() != 0) { %>
          <font class="campo">Ammenda&nbsp;<%=StringUtils.toEuroFormat(penaresidua.getImportoAmmendaSS())%>&nbsp;</font>&euro;
          <% } %>
<%
         }
%>   
      </td>
    </tr>

<%} %>


  </table>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan="4"> Dati Ordinanza di Revoca del Tribunale di Sorveglianza </td>
    </tr>
   <tr>
      <td class="l" colspan="4">
        <a href="Javascript:ListaDocumentiSius('LoadInserisciRevocaConversione');">
          Seleziona provvedimento di Sorveglianza dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    
     <tr>
      <td class="l" width="20%">Provvedimento emesso da<font class=ob>(*)</font></td>
        <td class="l" colspan="3">
          <select Title="Provvedimento" name="<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>" onChange="caricamento();">
<%
          Iterator lIter = provvedimento.iterator();
          while(lIter.hasNext())
          {
            DecodificheModel lDecMod = (DecodificheModel)lIter.next();
%>
            <option value="<%=lDecMod.getCode()%>"/><%=lDecMod.getDescription()%>
<%
          }
%>
          </select>
        </td>
    </tr>
    <tr>
      <td class="l">Data ricezione provvedimento</td>
      <td class="l" colspan="3">
        <input type="text" Title="Giorno Ricezione" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Ricezione" value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Ricezione" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr>
      <td class="l">Data emissione provvedimento</td>
      <td class="l">
        <input type="text" Title="Giorno Emissione provvedimento" value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" onChange="pulisciId();">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" onChange="pulisciId();">
      </td>
      <td class="l">Anno /Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento" value="" name="<%=ICostantiSanzioneSostitutiva.CAMPO_ANNO_PROVVEDIMENTO%>" type="text" size="4" maxlength="4" onChange="pulisciId();"> /
         <input Title="Numero Provvedimento" value="" name="<%=ICostantiSanzioneSostitutiva.CAMPO_NUMERO_PROVVEDIMENTO%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>

    </tr>
    <tr>
        <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
        <td class="l" colspan=3>
          <select Title="Tipo Provvedimento" name="<%=ICostantiSanzioneSostitutiva.CAMPO_TIPO_PROVVEDIMENTO%>" onChange="pulisciId();">
            <%=tipoprovvedimento%>
          </select>
        </td>
    </tr>


    <tr>
      <td class="l" >Autorità Emittente <font class=ob>(*)</font></td>
        <td class="l">
          <select Title="Autorità Emittente" name="<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>" onChange="pulisciId();">
           </select>
      </td>

      <td class="l" colspan=2>Sede  &nbsp;
          <input title="Sede Autorita"  type="text" name="<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT%>"  maxlength="35" size="35" onChange="pulisciId();">
          <a href="Javascript:ListaComuni('LoadInserisciRevocaConversione','<%=ICostantiSanzioneSostitutiva.CAMPO_SEDE_UDS_EMITT%>',document.LoadInserisciRevocaConversione.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>[document.LoadInserisciRevocaConversione.<%=ICostantiSanzioneSostitutiva.CAMPO_UFFICIO_SORVEGLIANZA%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
        </a>
       </td>
    </tr> 
    <tr>
      <td class="l">Tipo Pena da Convertire</td>
      <td class="L"><%=StringUtils.toStringJSP(penaresidua.getDescrTipoSanzione())%></td>
     
      <td class="l" colspan="3">Quantum di Pena
           <font class="label">Anni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniSS(), "0")%>&nbsp;</font>
           <font class="label">Mesi:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiSS(), "0")%>&nbsp;</font>
           <font class="label">Giorni:&nbsp; </font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniSS(), "0")%></font>

        </td>
    </tr>
    <tr>
      <td class="l" colspan="4">Pena Convertita:</td>
    </tr>  
    <tr>
      <td class="l" colspan="4">   RECLUSIONE
        Anni &nbsp;
        <font class="campo">
          <input value=""  size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_NUM_ANNI_RECLUSIONE%>" onChange="pulisciId();">
        </font>
        Mesi &nbsp;
        <font class="campo">
          <input value=""  size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_NUM_MESI_RECLUSIONE%>" onChange="pulisciId();">
        </font>
        Giorni &nbsp;
        <font class="campo">
          <input value=""  size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_NUM_GIORNI_RECLUSIONE%>" onChange="pulisciId();">
        </font>
        ARRESTO
        Anni &nbsp;
        <font class="campo">
          <input value=""  size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_NUM_ANNI_ARRESTO%>" onChange="pulisciId();">
        </font>
        Mesi &nbsp;
        <font class="campo">
          <input value="" size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_NUM_MESI_ARRESTO%>" onChange="pulisciId();">
        </font>
        Giorni &nbsp;
        <font class="campo">
          <input value=""  size="2" maxlength="2" name="<%= ICostantiScambioSanzione.CAMPO_NUM_GIORNI_ARRESTO%>" onChange="pulisciId();">
        </font>
      </td>
    </tr>  
    <tr>      
     <td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
     </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRevocaConversione");

  frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NUM_ANNI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NUM_MESI_RECLUSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NUM_GIORNI_RECLUSIONE%>","numeric");
  
  frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NUM_ANNI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NUM_MESI_ARRESTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiScambioSanzione.CAMPO_NUM_GIORNI_ARRESTO%>","numeric");
  
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");  
 

 
 
//Controlli Data Trasmissione
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","req","Il campo Giorno Ricezione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","req","Il campo Giorno Ricezione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","req","Il campo Giorno Ricezione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI%>","lt=2099");   
</script>
</body>
</html>