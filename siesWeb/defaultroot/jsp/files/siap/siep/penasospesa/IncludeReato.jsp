<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Enumeration" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.siep.reato.model.ReatoModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.circostanza.model.CircostanzaModel" %>
<%@ page import="siap.siep.circostanza.action.ICostantiCircostanza" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>

<%@ page import="f3b.log.LogF3B" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche"%>

<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="reati" 			scope="session" class="java.util.Vector" />
<jsp:useBean id="Circ_reati" 		scope="request" class="java.util.Vector" />
<jsp:useBean id="ComingFromInsert" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"     scope="request" class="java.lang.String"/>
<jsp:useBean id="continuazioni" 	scope="request" class="java.util.Hashtable"/>
<jsp:useBean id="modo"       		scope="request" class="java.lang.String"/>

<jsp:useBean id="allreati" 			scope="session" class="java.util.Vector" />
<jsp:useBean id="TipiSottonumerazione" 			scope="request" class="java.util.Vector" />

<%
	FascicoloSiepModel lFascicolo = null;
	SentenzaModel lSentenza = null;
	BigDecimal lProgrPrimoReato = null;
	if(modo.compareTo("SIGE") != 0)
	{
  		lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
  		lSentenza = lFascicolo.getSentenza();
	}
	// [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	siesLogger.debug("reati.size()="+reati.size());
	if(reati != null && reati.size() > 0)
  		lProgrPrimoReato = ((ReatoModel)reati.get(0)).getIdReato();

    Vector vetart_qual  = new Vector( DecodificheManager.getInstance().getSottonumerazione() );

%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Reato</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }
function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("AltroIm");
    document.Abbandona.A.disabled = true;
    document.AltroIm.D.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
  </head>
<%
ReatoModel lReato=new ReatoModel();
Iterator itx = reati.iterator();
if (reati.size()==1)
{
    lReato=(ReatoModel)reati.get(0);
}else
    while ( itx.hasNext())
   {
    ReatoModel reato = (ReatoModel)itx.next();
    if(reato.getProgrCircostanza()!=null)
     if(reato.getProgrCircostanza().intValue() == 1)
       lReato=reato;
   }

%>
  <body class="corpo">
  <div align="center">
  <table width="100%">
    <tr>
      <td class="int" >N. Reato</td>
      <td class="int" >Data Reato</td>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art. Qual.</td>
      <td class="int">Comma</td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
	  //***************************************
%>
      <td class="int">Comma Qual.</td>

      <td class="int">Lettera</td>
      <td class="int">Numero</td>
      <td class="int">Note</td>

    </tr>
<%
  itx = reati.iterator();
  while ( itx.hasNext())
  {
    ReatoModel reato = (ReatoModel)itx.next();
    String PenaDet=new String("");
	if (reato.getNumAnni() != null)
      PenaDet += "anni " +reato.getNumAnni();
	if (reato.getNumMesi() != null)
      PenaDet += " mesi "+ reato.getNumMesi();
	if (reato.getNumGiorni() != null)
      PenaDet += " giorni "+ reato.getNumGiorni();

	Iterator itxOggetto = vetart_qual.iterator();
	while(itxOggetto.hasNext()){
	  DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
	  if(lDecMod.getCode().equals(reato.getCodSottonumerazione()))
		reato.setDescrSottonumerazione(lDecMod.getDescription());
	  if(lDecMod.getCode().equals(reato.getCommaQualificante()))
			reato.setDescrCommaQualificante(lDecMod.getDescription());	   	
	  
	}

  String lProgressivo = "";
  if(reato.getProgrCircostanza()!=null)
	  if(reato.getProgrCircostanza().intValue() == 1)
	   lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();

%>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(lProgressivo)%>&nbsp;</td>
    <%if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null
         || reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
{%>

      <td class="l">
    <%if(reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
    {
        String lStrGGInizio = StringUtils.toStringJSP( reato.getGiornoInizio(), "**");
        if ( !lStrGGInizio.equals("**") && lStrGGInizio.length() == 1)
          lStrGGInizio = "0"+lStrGGInizio;

        String lStrMMInizio = StringUtils.toStringJSP( reato.getMeseInizio(), "**");
        if ( !lStrMMInizio.equals("**") && lStrMMInizio.length() == 1)
          lStrMMInizio = "0"+lStrMMInizio;

        String lStrAAInizio = StringUtils.toStringJSP( reato.getAnnoInizio(), "**");
%>
            <%=lStrGGInizio%>-
            <%=lStrMMInizio%>-
            <%=lStrAAInizio%>

<%
    }
    if((reato.getGiornoInizio() != null || reato.getMeseInizio() != null || reato.getAnnoInizio() != null)
         &&( reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null))
{%>
/
<%}%>
<%
 if(reato.getGiornoFine() != null || reato.getMeseFine() != null || reato.getAnnoFine() != null)
    {
%>
<%
        String lStrGGFine = StringUtils.toStringJSP( reato.getGiornoFine(), "**");
        if ( !lStrGGFine.equals("**") && lStrGGFine.length() == 1)
          lStrGGFine = "0"+lStrGGFine;

        String lStrMMFine = StringUtils.toStringJSP( reato.getMeseFine(), "**");
        if ( !lStrMMFine.equals("**") && lStrMMFine.length() == 1)
          lStrMMFine = "0"+lStrMMFine;

        String lStrAAFine = StringUtils.toStringJSP( reato.getAnnoFine(), "**");
%>
            <%=lStrGGFine%>-
            <%=lStrMMFine%>-
            <%=lStrAAFine%>
<%
    }
%>
</td>
<%
	}
    else
    {
%>
      <td class="l">-</td>
<%
	}
%>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getAnnoFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getNumeroFonte(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getArticolo(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrSottonumerazione(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getComma(),"-")%>&nbsp;</td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
	  //***************************************
%>
      <td class="l"><%=StringUtils.toStringJSP(reato.getDescrCommaQualificante(),"-")%>&nbsp;</td>

	  <td class="l"><%=StringUtils.toStringJSP(reato.getLettera(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getNumero(),"-")%>&nbsp;</td>
      <td class="l"><%=StringUtils.toStringJSP(reato.getNote(),"-")%>&nbsp;</td>

<%
if(lTipoFunzione.equals("") )
	if (modo.compareTo("SIGE") == 0) // lTipoFunzione per capire che si proviene da iscrizione guidata
	{
%>
<%
	} 
	else 
	{ 
%>
<% 
}   }
%>
    </tr>
	<%
	if (continuazioni.size() > 0) {

		%><tr><td class="titolo" colspan="12">Continuazione Reati</td></tr><%
		
		String strCont = "";
		String progReatoCont = "";
		String tipoCont = "";
		Collection coll = continuazioni.values();
		Iterator itxColl = coll.iterator();
		int conta = 0;
		
		Vector vectCont = new Vector();

		while (itxColl.hasNext()) {
			
			vectCont = (Vector)itxColl.next();
			Iterator itxVect = vectCont.iterator();
			conta = 0;
			%>
			<tr><td class="l" colspan="11">
			<%
			while (itxVect.hasNext()) {
				
				strCont = (String)itxVect.next();
				String[] arrStr = strCont.split("@!");
				
				if (conta == 0) {
					
					if (strCont.equalsIgnoreCase("C2"))
						tipoCont = "CONTINUAZIONE";
					else if (strCont.equalsIgnoreCase("C1"))
						tipoCont = "CONCORSO FORMALE";		
					
					%><%=tipoCont%> tra i reati di cui ai nr. <%
				}
				else {
					%><%=StringUtils.toStringJSP(arrStr[1])%> <%
					progReatoCont = arrStr[0];
				}
				
				conta++;
			}
			%></td>
			<%
			boolean lFlagV = lFascicolo.getFlagValidato().equalsIgnoreCase("S");
			if (!lFlagV) {
			%>
		      <td class="c">
		      <table>
		      <tr>           
		              <td>
		                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadModificaContinuazioneReati">
		                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
		                </a>
		              </td>
		              <td>
		                <a href="Javascript:conferma('siap.siep.reato.action.ActCancellaContinuazioneReati','<%=ICostantiReato.CAMPO_PROGR_REATO%>','<%=progReatoCont%>');">
		                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
		                </a>
		              </td>		
		      </tr>
		      </table>
		      </td>
		      <%
		      }
		      %>			
			  </tr>
			  <%
		}
	}
    %>
    
<% if (Circ_reati.size()>0)
{
%>
<tr><td class="titolo" colspan="12">Aggravanti soggettive/Attenuanti</td></tr>

    <tr>
      <td class="int"></td>
      <td class="int"></td>
      <td class="int">Fonte</td>
      <td class="int">Anno</td>
      <td class="int">Numero</td>
      <td class="int">Articolo</td>
      <td class="int">Art. Qual.</td>
      <td class="int">Comma</td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
	  //***************************************
%>
      <td class="int">Comma Qual.</td>

      <td class="int">Lettera</td>
      <td class="int">Numero</td>
      <td class="int">Note</td>

<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
      <td class="int">Azioni</td>
<%}%>
    </tr>
<%
	String flagApp = null;
	String descBil = null;
	String noteBil = null;
	String flagGiu = null;

  Iterator itx2 = Circ_reati.iterator();
  while ( itx2.hasNext())
  {
    CircostanzaModel CiReato = (CircostanzaModel)itx2.next();
    flagApp = CiReato.getFlagSentenzaApplicazPena();
	descBil = CiReato.getDescrBilanciamentoCircostanze();
	noteBil = CiReato.getNoteBilanciamento();
	flagGiu = CiReato.getFlagGiudizioAbbreviato();  

%>
   <tr>
      <td class="l"></td>
      <td class="l"></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getAnnoFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumeroFonte(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getArticolo(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrSottonumerazione(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getComma(),"-")%></td>
<%
	  //***************************************
	  //Federica - a9-rr-078
	  //aggiunto campo Comma-Qualificante 
	  //***************************************
%>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getDescrCommaQualificante(),"-")%>&nbsp;</td>

      <td class="l"><%=StringUtils.toStringJSP(CiReato.getLettera(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNumero(),"-")%></td>
      <td class="l"><%=StringUtils.toStringJSP(CiReato.getNote(),"-")%></td>

<%if(lTipoFunzione.equals("")) // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
      <td class="c">
      <table>
      <tr>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.circostanza.action.ActLoadDettaglioCircostanza&<%=ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA%>=<%=CiReato.getIdCircostanza()%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>
              </td>
 <%
boolean lFlagValidato=lFascicolo.getFlagValidato().equalsIgnoreCase("S");
if(!lFlagValidato)
            {%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.circostanza.action.ActLoadModificaCircostanza&<%=ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA%>=<%=CiReato.getIdCircostanza()%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
              <td>
                <a href="Javascript:conferma('siap.siep.circostanza.action.ActCancellaCircostanza','<%=ICostantiCircostanza.CAMPO_ID_CIRCOSTANZA%>','<%=CiReato.getIdCircostanza()%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%}%>
      </tr>
      </table>
      </td>
<%}%>
    </tr>
<%
  }
%>
    </table>
    <table cellspacing="2" cellpadding="2" width="100%">
      <tr>
        <td class="l" rowspan="2">Sentenza di applicazione pena</td>
        <td class="l" rowspan="2">
          <font class="campo">
<%
            if(flagApp != null && flagApp.equals("S"))
            {
%>
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
<%
            }
%>
          </font>
         </td>
        <td class="l">Bilanciamento circostanze</td>
        <td class="l">
        <%if(descBil!=null && !descBil.equals("")){%>
          <font class="campo">
            <%=descBil%>
          </font>
        <%}%>
          &nbsp;
        </td>
        
	      <td class="l" rowspan="2">Giudizio abbreviato</td>
	      <td class="l" rowspan="2">
	          <font class="campo">
				<%
	            if(flagGiu != null && flagGiu.equals("S"))
	            {
				%>
	              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
				<%
	            }
				%>
	          </font>&nbsp;			        
	      </td>
      	</tr>
      	
      	<tr>
      		<td class="l">Note Bilanciamento circostanze</td>
		        <td class="l">
		        <%if(noteBil!=null && !noteBil.equals("")){%>
		          <font class="campo">
		            <%=noteBil%>
		          </font>
		        <%}%>
		          &nbsp;
		        </td>
      		</tr>
    </table>
  </div>
<% }%>

<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
{%>
<table>
<tr>
<td class="lNoBord">
  <FORM method="POST" name="AltroIm" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActLoadDettaglioReato&lTipoFunzione=<%=lTipoFunzione%>&<%=ICostantiReato.CAMPO_ID_REATO%>=<%=lReato.getIdReato()%>">
      <br><INPUT class="bottone" type="button" name="D" value="Prosegui" onclick="Javascript:Disabilita();">
  </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.reato.action.ActRicercaReato&<%=ICostantiReato.CAMPO_PROGR_REATO%>=<%=lReato.getProgrReato()%>&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="button" name="A" value="Abbandona" onclick="Javascript:Verify();">
 </FORM>
</td>
</tr>
</table>
<%}%>

  </body>
</html>