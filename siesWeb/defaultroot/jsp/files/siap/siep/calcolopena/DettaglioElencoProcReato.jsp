<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator" %>


<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.model.ReatoCircostanzaModel"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel"%>


<jsp:useBean id="reati"      	scope="request" class="java.util.Vector" />
<jsp:useBean id="NomeAzione" 	scope="request" class="java.lang.String" />
<jsp:useBean id="reatiCumulo"   scope="request" class="java.util.Vector" />


<html>

  <head>
    <title> [S.I.E.S.] - Ricerca Procedimento per Reato - Dettaglio Reato - </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>

  </head>


<BODY class="corpo">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
          <font class="label">Funzione :</font>&nbsp;
          <font class="campo">Dettaglio Procedimento per Reato</font>
        </td>
         <%if(request.getParameter("NomeAzione") != null && request.getParameter("NomeAzione").equals("siap.siep.calcolopena.action.ActElencoProcReato"))
          {%>
          <td class="LBG">
            <a href="javascript:history.go(-1);">
             <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
            </a>
          </td>
        <%}%>
      </tr>
    </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
<%



%>
        <table cellspacing=1 cellpadding=1 width=95%>
          <tr>
            <td class="Titolo">Reati</td>
          </tr>
<%
		Iterator lIterReati = null;
		
		if(reatiCumulo!=null && reatiCumulo.size()>0)
		{	
			lIterReati = reatiCumulo.iterator();
        	while(lIterReati.hasNext())
        	{
      	  		ReatoCircostanzaCumuloModel lReatoCircostanza = (ReatoCircostanzaCumuloModel)lIterReati.next();
          		ReatoCumuloModel lReato = lReatoCircostanza.getReatoCum();
          		ReatoCumuloModel[] lCircostanze = lReatoCircostanza.getCircostanzeCum();

	          boolean lFlagAnnoNumero = false;
	          if( lReato.getAnnoFonte() != null
	              && !lReato.getAnnoFonte().equals("")
	              && lReato.getNumeroFonte() != null
	              && !lReato.getNumeroFonte().equals("") )
	          {
	            lFlagAnnoNumero = true;
	          }
	%>
	            <tr>
	              <td class="l">
	<%
                  //REATO
                  if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
                  {
%>
                    <font class="label">
<%
                      out.println(" ** Reato N." + lReato.getProgrNumeroManuale()+": ");
%>
                    </font>

<%
                  }
                  else
                  {
                    out.println(" ** Reato N." + lReato.getProgrReato()+": ");
                  }
                  %></font><%
                  %><font class="campo"><%

                  if(lFlagAnnoNumero)
                  {
                    if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte()+" ");
                    if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
                      out.println(lReato.getAnnoFonte());
                    if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
                      out.println("/"+lReato.getNumeroFonte());
                  }

                  if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
                    out.println("art."+lReato.getArticolo());
                  if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
                    out.println(" "+lReato.getDescrSottonumerazione());

                  if(!lFlagAnnoNumero)
                  {
                    if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte());
                  }

                  if(lReato.getComma() != null && !lReato.getComma().equals(""))
                    out.println(" c. "+lReato.getComma());
                  if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
                    out.println(" l. "+lReato.getLettera());
                  if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
                    out.println(" n. "+lReato.getNumero());

                  //CIRCOSTANZE
                  if(lCircostanze != null)
                  {
                    ReatoCumuloModel lCirc = null;
                    for(int i=0; i<lCircostanze.length; i++)
                    {
                      lCirc = lCircostanze[i];
%>
                      ,
<%
                      boolean lFlagAnnoNumeroCirc = false;
                      if( lCirc.getAnnoFonte() != null
                          && !lCirc.getAnnoFonte().equals("")
                          && lCirc.getNumeroFonte() != null
                          && !lCirc.getNumeroFonte().equals("") )
                      {
                        lFlagAnnoNumeroCirc = true;
                      }
                      if(lFlagAnnoNumeroCirc)
                      {
                        if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                          out.println(lCirc.getDescrFonte()+" ");
                        if(lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().equals(""))
                          out.println(lCirc.getAnnoFonte());
                        if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
                          out.println("/"+lCirc.getNumeroFonte());
                      }

                      if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
                        out.println("art."+lCirc.getArticolo());
                      if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
                        out.println(" "+lCirc.getDescrSottonumerazione());

                      if(!lFlagAnnoNumeroCirc)
                      {
                        if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                          out.println(lCirc.getDescrFonte());
                      }

                      if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
                        out.println(" c. "+lCirc.getComma());
                      if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
                        out.println(" l. "+lCirc.getLettera());
                      if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
                        out.println(" n. "+lCirc.getNumero());
                    }
                  }
%>
                </font>
<%
                  if(lReato.getStringaConsumazione()!= null)
                  {
%>
                    <!--  <font class="label">Data</font> -->
                    <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
<%
                  }

 				if(lReato.getNote() != null && !lReato.getNote().equals(""))
        		{
%>
        			<font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
<%
        		}

                  if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals(""))
                  {
%>
                    <font class="label">Luogo</font>&nbsp;
                    <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
<%
                  }
%>

              </td>
            </tr>
<%
        }
%>
			<tr><td>&nbsp;</td></tr>
			<tr><td><font class="label">( ** ) Reato appartenente ad uno dei Titoli assorbiti in Cumulo</font></td></tr>
<%         	
	}
	else if(reati!=null && reati.size()>0)
	{	
		lIterReati = reati.iterator();
       	while(lIterReati.hasNext())
       	{
   	  		ReatoCircostanzaModel lReatoCircostanza = (ReatoCircostanzaModel)lIterReati.next();
       		ReatoModel lReato = lReatoCircostanza.getReato();
       		ReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();

            boolean lFlagAnnoNumero = false;
	        if( lReato.getAnnoFonte() != null
	             && !lReato.getAnnoFonte().equals("")
	             && lReato.getNumeroFonte() != null
	             && !lReato.getNumeroFonte().equals("") )
	        {
	            lFlagAnnoNumero = true;
	        }
	%>
	        <tr>
	          <td class="l">
	
	<%
                  //REATO
            if (lReato.getProgrNumeroManuale() != null && !lReato.getProgrNumeroManuale().equals(""))
            {
%>
                  <font class="label">
<%
                   out.println("Reato N." + lReato.getProgrNumeroManuale()+": ");
%>
                  </font>

<%
            }
                  else
                  {
                    out.println("Reato N." + lReato.getProgrReato()+": ");
                  }
                  %></font><%
                  %><font class="campo"><%

                  if(lFlagAnnoNumero)
                  {
                    if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte()+" ");
                    if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
                      out.println(lReato.getAnnoFonte());
                    if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
                      out.println("/"+lReato.getNumeroFonte());
                  }

                  if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
                    out.println("art."+lReato.getArticolo());
                  if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
                    out.println(" "+lReato.getDescrSottonumerazione());

                  if(!lFlagAnnoNumero)
                  {
                    if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
                      out.println(lReato.getDescrFonte());
                  }

                  if(lReato.getComma() != null && !lReato.getComma().equals(""))
                    out.println(" c. "+lReato.getComma());
                  if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
                    out.println(" l. "+lReato.getLettera());
                  if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
                    out.println(" n. "+lReato.getNumero());

                  //CIRCOSTANZE
                  if(lCircostanze != null)
                  {
                    ReatoModel lCirc = null;
                    for(int i=0; i<lCircostanze.length; i++)
                    {
                      lCirc = lCircostanze[i];
%>
                      ,
<%
                      boolean lFlagAnnoNumeroCirc = false;
                      if( lCirc.getAnnoFonte() != null
                          && !lCirc.getAnnoFonte().equals("")
                          && lCirc.getNumeroFonte() != null
                          && !lCirc.getNumeroFonte().equals("") )
                      {
                        lFlagAnnoNumeroCirc = true;
                      }
                      if(lFlagAnnoNumeroCirc)
                      {
                        if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                          out.println(lCirc.getDescrFonte()+" ");
                        if(lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().equals(""))
                          out.println(lCirc.getAnnoFonte());
                        if(lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
                          out.println("/"+lCirc.getNumeroFonte());
                      }

                      if(lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
                        out.println("art."+lCirc.getArticolo());
                      if(lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("") && !lCirc.getDescrSottonumerazione().equals("-"))
                        out.println(" "+lCirc.getDescrSottonumerazione());

                      if(!lFlagAnnoNumeroCirc)
                      {
                        if(lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("") && !lCirc.getDescrFonte().equals("-"))
                          out.println(lCirc.getDescrFonte());
                      }

                      if(lCirc.getComma() != null && !lCirc.getComma().equals(""))
                        out.println(" c. "+lCirc.getComma());
                      if(lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
                        out.println(" l. "+lCirc.getLettera());
                      if(lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
                        out.println(" n. "+lCirc.getNumero());
                    }
                  }
%>
                </font>
<%
                  if(lReato.getStringaConsumazione()!= null)
                  {
%>
                    <!--  <font class="label">Data</font> -->
                    <font class="campo"><%=StringUtils.toStringJSP(lReato.getStringaConsumazione())%>,</font>
<%
                  }

 				if(lReato.getNote() != null && !lReato.getNote().equals(""))
        		{
%>
        			<font class="campo">&nbsp;<%=StringUtils.toStringJSP(lReato.getNote())%>&nbsp;</font>
<%
        		}

                  if(lReato.getDescLuogo()!= null && !lReato.getDescLuogo().equals(""))
                  {
%>
                    <font class="label">Luogo</font>&nbsp;
                    <font class="campo"><%=StringUtils.toStringJSP(lReato.getDescLuogo())%></font>
<%
                  }
%>

              </td>
            </tr>
<%
        }
	}	
	
%>

				</td>
</tr>
		</table>

</body>
</html>