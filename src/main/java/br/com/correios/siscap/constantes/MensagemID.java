package br.com.correios.siscap.constantes;


public interface MensagemID {
	
	String ERRO = "erro";
	String PREENCHA_UM_DOS_CAMPOS = "preencha.um.dos.campos";
	
	//segui
	String PERFIL_INEXISTENTE = "profile.inexistente";
	String SEM_SERVICO_SEGUI = "segui.sem.servico";
	String NAO_POSSIVEL_REALIZAR_PESQUISA_USUARIO = "segui.erro.pesquisa.usuario";
	String NAO_POSSIVEL_INCLUIR_USUARIO_SEGUI = "segui.erro.incluir.usuario";
	String NAO_POSSIVEL_REALIZAR_PESQUISA_GRUPO_USUARIO = "segui.erro.pesquisa.grupo.usuario";
	String NAO_POSSIVEL_CONSULTAR_GRUPO_SEGUI = "segui.erro.pesqisa.grupo";
	String NAO_POSSIVEL_CONSULTAR_USUARIOS_GRUPO_SEGUI = "segui.erro.pesguisa.usuarios.grupo";
	String CAMPO_OBRIGATORIO = "campo.obrigatorio";
	String NAO_POSSIVEL_CADASTRAR_USUARIO_SEGUI = "segui.erro.cadastrar.usuario.grupo";
	String USUARIO_NAO_CADASTRADO_SEGUI = "usuarioSiscap.nao.cadastrado.segui";
	String GRUPO_SEGUI_EXISTENTE = "grupo.segui.existente";
	
	//Reflexão
	String ARGUMENTO_INVALIDO_NO_METODO = "argumento.invalido.no.metodo";
	String ACESSO_NEGADO_AO_METODO = "acesso.negado.ao.metodo";
	String CLASSE_NAO_ENCONTRADA = "classe.nao.encontrada";
	String CLASSE_NAO_INSTANCIADA = "classe.nao.instanciada";
	String ERRO_INVOCAR_METODO = "erro.ao.invocar.metodo";
	String METODO_NAO_ENCONTRADO = "metodo.nao.encontrado";
	String SECURITY_EXCEPTION_NO_METODO_C_PARAMETRO = "security.exception.no.metodo.com.parametro";
	String PATH_ATRIBUTO_VAZIO = "path.do.atributo.vazio";
	
	//Geral
	String CODIGO_REGISTRO_EXISTENTE = "codigo.registro.existente";
	String NENHUM_REGISTRO_ENCONTRADO = "nenhum.registro.encontrado";
	String CODIGO_REGISTRO_DUPLICADO = "codigo.registro.duplicado";
	String ALTERACAO_SUCESSO = "alteracao.sucesso";
	String EXCLUSAO_SUCESSO = "exclusao.sucesso";
	String INCLUSAO_SUCESSO = "inclusao.sucesso";
	String SESSAO_ATIVA_APLICACAO = "sessao.ativa.aplicacao";
	String REGISTRO_ENCONTRADO = "registro.encontrado";
	String REGISTRO_NAO_ENCONTRADO = "registro.nao.encontrado";	
	String DADOS_OBRIGATORIOS_NAO_INFORMADOS = "dados.obrigatorios.nao.informados";
	String PAGINA_INEXISTENTE = "pagina.inexistente";
	String PAGINA_AJUDA_EXISTENTE = "pagina.ajuda.existente";
	String MINIMO_DE_CARACTERES_PARA_CONSULTA = "minimo.caracteres.consulta";
	String USUARIO_EXTERNO_SEM_VINCULACAO_COM_PJ = "usuario.sem.vinculacao.pj";
	
	//GRUPO ATENDIMENTO
	String GRUPO_ALTERADO = "grupo.atendimento.alterado";
	String GRUPO_NAO_EXCLUIDO = "grupo.atendimento.nao.excluido";
	String CRONOGRAMA_ALTERADO = "cronograma.alterado";;
	
	//PEDIDO
	String PENDENCIA_NO_CADASTRO = "pendencia.cadastro";
	String SEM_VINCULACAO_GRUPO_ATENDIMENTO = "sem.vinculo.grupo.atendimento";
	String FORA_DO_PERIODO_DE_ABERTURA_DO_PEDIDO = "fora.periodo.pedido";
	String PEDIDO_POSSUI_ESSE_ITEM = "pedido.possui.esse.item";
	
	
	
	
}
