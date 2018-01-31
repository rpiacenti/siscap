package br.com.correios.ppjsiscap.seguranca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CarregaMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6892055231168775370L;

	public CarregaMenu() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<ItemMenuSiscap> getMenu() {

		ArrayList<ItemMenuSiscap> tpMenu = new ArrayList<ItemMenuSiscap>();
		ArrayList<ItemMenuSiscap> arMenu = new ArrayList<ItemMenuSiscap>();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.getExternalContext() != null) {

			try {
				/*
				 * URL url = new
				 * URL("http://localhost:8080/siscap/resources/menu/siscap_menu.xml"
				 * );
				 * 
				 * URLConnection connection = url.openConnection();
				 */

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				
				Document doc = dBuilder.parse(new InputSource(new StringReader(
						getXMLMenu())));
			
				
				
				// Document doc1 = dBuilder.parse("siscap_menu.xml");
				// Document doc = dBuilder.parse(connection.getInputStream());
				// optional, but recommended
				// read this -
				// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("item_menu");

				for (int temp = 0; temp < nList.getLength() - 1; temp++) {

					Node nNode = nList.item(temp);

					// System.out.println("\nCurrent Element :" +
					// nNode.getNodeName());

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						ItemMenuSiscap menu = new ItemMenuSiscap();
						Element eElement = (Element) nNode;

						menu.setCo_item(eElement
								.getElementsByTagName("itm_co_item").item(0)
								.getTextContent());
						menu.setNivel(Integer.parseInt(eElement
								.getElementsByTagName("itm_nu_nivel").item(0)
								.getTextContent()));
						if (eElement.getElementsByTagName("itm_tx_url").item(0)
								.getTextContent().indexOf("http") > -1) {
							menu.setUrl(eElement
									.getElementsByTagName("itm_tx_url").item(0)
									.getTextContent());
						} else {
							menu.setUrl("/siscap/"
									+ eElement
											.getElementsByTagName("itm_tx_url")
											.item(0).getTextContent());
						}

						menu.setDescricao(eElement
								.getElementsByTagName("itm_tx_descricao")
								.item(0).getTextContent());
						menu.setSuperior(eElement
								.getElementsByTagName("itm_tx_superior")
								.item(0).getTextContent());
						menu.setStatus(eElement
								.getElementsByTagName("itm_status").item(0)
								.getTextContent());
						menu.setOrdem(Integer.parseInt(eElement
								.getElementsByTagName("itm_nu_ordem").item(0)
								.getTextContent()));
						tpMenu.add(menu);
						menu = null;
					}
				}
				// url = null;

			} catch (Exception e) {
				e.printStackTrace();
			}
			// Ordenação pelo atributo ORDEM

			for (int t = 0; t < 20; t++) {
				for (int i = 0; i < tpMenu.size(); i++) {
					if (t == tpMenu.get(i).getOrdem()
							&& (tpMenu.get(i).getOrdem() < 99)) {
						// System.out.println(tpMenu.get(i).getOrdem() + " -- "
						// + tpMenu.get(i).getDescricao());
						arMenu.add(tpMenu.get(i));
						break;
					}
				}
			}
			// System.out.println("Tamanho Menu resultandao !!!!!!!!!--->"+arMenu.size());
			tpMenu = null;
		}
		return arMenu;
	}

	public String getXMLMenu() throws IOException {

		String relativeWebPath = "/resources/menu/siscap_menu.xml";

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext
				.getExternalContext().getContext();
		String absoluteDiskPath = servletContext.getRealPath(relativeWebPath);
		File file = new File(absoluteDiskPath);

		FileReader arq = new FileReader(file);
		BufferedReader lerArq = new BufferedReader(arq);

		String line;
		String retorno = null;
		int nlin = 0;
		System.out.println("/////// Carreguei Menu  //////////////////");

		while ((line = lerArq.readLine()) != null) {
			retorno = retorno + line;

		}
		lerArq.close();
		retorno = retorno.substring(4, retorno.length());
		return retorno;
	}
}
