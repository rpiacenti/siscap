package br.com.correios.ppjsiscap.enums;

public enum UFCD {
	UFLESTE("'ES','MS','PR','RJ','RS','SC','SP'","UF Leste"),
	UFOESTE("'AC','AL','AP','AM','BA','CE','DF','GO','MA','MT','MG','PA','PB','PE','PI','RN','RO','RR','SE','TO'","UF Oeste");
	
//	"Acre","	AC	 
//	"Alagoas","	AL	 
//	"Amapá","	AP	 
//	"Amazonas","	AM	 
//	"Bahia","	BA	 
//	"Ceará","	CE	 
//	"Distrito Federal","	DF	 
//	"Espírito Santo","	ES	 
//	"Goiás","	GO	 
//	"Maranhão","	MA	 
//	"Mato Grosso","	MT	 
//	"Mato Grosso do Sul","	MS	 
//	"Minas Gerais","	MG	 
//	"Pará","	PA	 
//	"Paraíba","	PB	 
//	"Paraná","	PR	 
//	"Pernambuco","	PE	 
//	"Piauí","	PI	 
//	"Rio de Janeiro","RJ	 
//	"Rio Grande do Norte","	RN	 
//	"Rio Grande do Sul","RS	 
//	"Rondônia","RO	 
//	"Roraima","RR	 
//	"Santa Catarina","SC	 
//	"São Paulo","SP	 
//	"Sergipe","SE	 
//	"Tocantins","TO
	
	
	private String sigla;
	private String cd;

	private UFCD(String sigla, String cd) {
		this.sigla = sigla;
		this.cd = cd;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}
	


}
